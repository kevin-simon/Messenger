package utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;

import rmi.Type;
import datas.Identity;

public class Discover extends Observable implements Runnable {
	
	private HashMap<String, InetAddress> broadcastAddresses;
	private HashMap<String, InetAddress> localAddresses;
	private InetAddress broadcastSender;

	public Discover() {
		this.broadcastAddresses = new HashMap<String,InetAddress>();
		this.localAddresses = new HashMap<String,InetAddress>();
		try {
			Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
			while (networkInterfaces.hasMoreElements()) {
				NetworkInterface networkInterface = networkInterfaces.nextElement();
				List<InterfaceAddress> interfaceAddresses = networkInterface.getInterfaceAddresses();
				for (InterfaceAddress interfaceAddress : interfaceAddresses) {
					InetAddress broadcastAddress;
					if (!interfaceAddress.getAddress().isLoopbackAddress()) {
						this.localAddresses.put(networkInterface.getName(), interfaceAddress.getAddress());
						if ((broadcastAddress = interfaceAddress.getBroadcast()) != null) {
							this.broadcastAddresses.put(networkInterface.getName(), broadcastAddress);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(this.localAddresses);
		System.out.println(this.broadcastAddresses);
	}
	
	public boolean isLocalAddress(InetAddress inetAddress) {
		for (InetAddress localAddress : this.localAddresses.values()) {
			if (localAddress.getHostAddress().equals(inetAddress.getHostAddress())) {
				return true;
			}
		}
		return false;
	}
	
	public ArrayList<InetAddress> getLocalAddresses() {
		return new ArrayList<InetAddress>(this.localAddresses.values());
	}
	
	public void start() {
		new Thread(this).start();
	}
	
	public void sendPacket(String pseudonyme, Type peerType) {
		try {
			int broadcastPort = Integer.parseInt(Properties.APP.get("broadcast_port"));
			DatagramSocket serverSocket = new DatagramSocket();
		    serverSocket.setBroadcast(true);
		    for (String networkInterface : this.broadcastAddresses.keySet()) {
		    	InetAddress broadcastAddress = this.broadcastAddresses.get(networkInterface);
		    	InetAddress localAddress = this.localAddresses.get(networkInterface);
		    	ByteArrayOutputStream  baos = new ByteArrayOutputStream();
		        ObjectOutputStream oos = new ObjectOutputStream(baos);
		        oos.writeObject(new Identity(pseudonyme, localAddress, peerType));
		        oos.flush();
		        
		    	System.out.println("Sending Discovery message to " + broadcastAddress + " Via UDP port " + broadcastPort);
	
		    	byte[] buffer = baos.toByteArray();
	
		        int number = buffer.length;;
		        byte[] data = new byte[4];
	
		        for (int i = 0; i < 4; ++i) {
		            int shift = i << 3;
		            data[3-i] = (byte)((number & (0xff << shift)) >>> shift);
		        }
		        
		        DatagramPacket packet = new DatagramPacket(data, 4, broadcastAddress, broadcastPort);
		        serverSocket.send(packet);
	
		        packet = new DatagramPacket(buffer, buffer.length, broadcastAddress, broadcastPort);
		        serverSocket.send(packet);
		    }
		    serverSocket.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public InetAddress getBroadcastSender() {
		return this.broadcastSender;
	}
	
	public void run() {
		while (true) {
			try {
				int broadcastPort = Integer.parseInt(Properties.APP.get("broadcast_port"));
				
				DatagramSocket socket = new DatagramSocket(broadcastPort);
				 byte[] data = new byte[4];
			    DatagramPacket packet = new DatagramPacket(data, data.length );
			    socket.receive(packet);
				if (!this.localAddresses.values().contains(packet.getAddress())) {
				    System.out.println("Reception d'un packet broadcaste depuis " + packet.getAddress());
				    int len = 0;
				    for (int i = 0; i < 4; ++i) {
				    	len |= (data[3-i] & 0xff) << (i << 3);
				    }
		
				    byte[] buffer = new byte[len];
				    packet = new DatagramPacket(buffer, buffer.length );
				    socket.receive(packet);
				    this.broadcastSender = packet.getAddress();
				    ByteArrayInputStream baos = new ByteArrayInputStream(buffer);
				    ObjectInputStream oos = new ObjectInputStream(baos);
				    this.setChanged();
				    this.notifyObservers(oos.readObject());
			    }
			    socket.close();
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
    }
	
}
