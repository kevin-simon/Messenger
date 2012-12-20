package utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.BindException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;
import java.util.Observable;

import rmi.Type;
import datas.Identity;

public class Discover extends Observable implements Runnable {
	
	private InetAddress broadcastAddress;
	private InetAddress localAddress;
	private InetAddress broadcastSender;
	private DatagramSocket socket;
	private boolean start;

	public Discover() {
		try {
			Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
			while (networkInterfaces.hasMoreElements() && this.localAddress == null) {
				NetworkInterface networkInterface = networkInterfaces.nextElement();
				List<InterfaceAddress> interfaceAddresses = networkInterface.getInterfaceAddresses();
				for (InterfaceAddress interfaceAddress : interfaceAddresses) {
					this.broadcastAddress = interfaceAddress.getBroadcast();
					if (!interfaceAddress.getAddress().isLoopbackAddress() && broadcastAddress != null) {
						this.localAddress = interfaceAddress.getAddress();
					}
				}
				if (networkInterface.getName() == Properties.APP.get("network_interface")) {
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean isLocalAddress(InetAddress inetAddress) {
		return localAddress.getHostAddress().equals(inetAddress.getHostAddress());
	}
	
	public InetAddress getLocalAddress() {
		return this.localAddress;
	}
	
	public void start() {
		new Thread(this).start();
	}
	
	public void sendPacket(String pseudonyme, Type peerType, int port) {
		try {
			int broadcastPort = Integer.parseInt(Properties.APP.get("broadcast_port"));
			DatagramSocket serverSocket = new DatagramSocket();
		    serverSocket.setBroadcast(true);
	    	ByteArrayOutputStream  baos = new ByteArrayOutputStream();
	        ObjectOutputStream oos = new ObjectOutputStream(baos);
	        oos.writeObject(new Identity(pseudonyme, this.localAddress, peerType, port));
	        oos.flush();
	        
	    	System.out.println("Sending Discovery message to " + broadcastAddress + " Via UDP port " + broadcastPort);

	    	byte[] buffer = baos.toByteArray();

	        int number = buffer.length;;
	        byte[] data = new byte[4];

	        for (int i = 0; i < 4; ++i) {
	            int shift = i << 3;
	            data[3-i] = (byte)((number & (0xff << shift)) >>> shift);
	        }
	        
	        DatagramPacket packet = new DatagramPacket(data, 4, this.broadcastAddress, broadcastPort);
	        serverSocket.send(packet);

	        packet = new DatagramPacket(buffer, buffer.length, this.broadcastAddress, broadcastPort);
	        serverSocket.send(packet);
		    serverSocket.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public InetAddress getBroadcastSender() {
		return this.broadcastSender;
	}
	
	public void run() {
		do {
			try {
				int broadcastPort = Integer.parseInt(Properties.APP.get("broadcast_port"));
				
				this.socket = new DatagramSocket(broadcastPort);
				this.start = true;
				byte[] data = new byte[4];
			    DatagramPacket packet = new DatagramPacket(data, data.length );
			    this.socket.receive(packet);
				System.out.println("Reception d'un packet broadcaste depuis " + packet.getAddress());
			    int len = 0;
			    for (int i = 0; i < 4; ++i) {
			    	len |= (data[3-i] & 0xff) << (i << 3);
			    }
	
			    byte[] buffer = new byte[len];
			    packet = new DatagramPacket(buffer, buffer.length );
			    this.socket.receive(packet);
			    this.broadcastSender = packet.getAddress();
			    ByteArrayInputStream baos = new ByteArrayInputStream(buffer);
			    ObjectInputStream oos = new ObjectInputStream(baos);
			    this.setChanged();
			    this.notifyObservers(oos.readObject());
			    this.socket.close();
			} catch(BindException ex) {
				System.out.println("Une autre instance écoute déjà sur ce port");
				System.out.println("Pas de passage en mode écoute");
				this.start = false;
			} catch(SocketException ex) {
				System.out.println("Fermeture de l'écoute");
				this.start = false;
			} catch (Exception ex) {
				ex.printStackTrace();
				System.exit(1);
			}
		} while (this.start);
    }

	public void stop() {
		if (this.socket != null) {
			this.socket.close();
		}
	}
}
