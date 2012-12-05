package utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Observable;

import rmi.Type;

public class Discover extends Observable implements Runnable {
	
	private HashMap<InetAddress, BroadcastAddress> broadcastAddresses;
	private InetAddress broadcastSender;

	public Discover() {
		this.broadcastAddresses = new HashMap<InetAddress,BroadcastAddress>();
		try {
			Enumeration<NetworkInterface> networInterfaces = NetworkInterface.getNetworkInterfaces();
			while (networInterfaces.hasMoreElements()) {
				NetworkInterface networkInterface = networInterfaces.nextElement();
				Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
				while (addresses.hasMoreElements()) {
					InetAddress address = addresses.nextElement();
					if (!address.isLoopbackAddress() && address.getAddress().length == 4) {
						this.broadcastAddresses.put(address, new BroadcastAddress(address));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<InetAddress> getLocalAddresses() {
		return new ArrayList<InetAddress>(this.broadcastAddresses.keySet());
	}
	
	public void start() {
		new Thread(this).start();
	}
	
	public void sendPacket(Type peerType) {
		try {
			int broadcastPort = Integer.parseInt(Properties.APP.get("broadcast_port"));
			DatagramSocket serverSocket = new DatagramSocket();
		    serverSocket.setBroadcast(true);
		    for (InetAddress inetAddress : this.broadcastAddresses.keySet()) {
		    	
		    	ByteArrayOutputStream  baos = new ByteArrayOutputStream();
		        ObjectOutputStream oos = new ObjectOutputStream(baos);
		        oos.writeObject(peerType);
		        oos.flush();
		        
		    	InetAddress address = InetAddress.getByName(this.broadcastAddresses.get(inetAddress).toString());
		    	System.out.println("Sending Discovery message to " + address + " Via UDP port " + broadcastPort);
	
		    	byte[] buffer = baos.toByteArray();
	
		        int number = buffer.length;;
		        byte[] data = new byte[4];
	
		        for (int i = 0; i < 4; ++i) {
		            int shift = i << 3;
		            data[3-i] = (byte)((number & (0xff << shift)) >>> shift);
		        }
		        
		        DatagramPacket packet = new DatagramPacket(data, 4, address, broadcastPort);
		        serverSocket.send(packet);
	
		        packet = new DatagramPacket(buffer, buffer.length, address, broadcastPort);
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
			    Type type = (Type) oos.readObject();
			    socket.close();
			    this.setChanged();
			    this.notifyObservers(type);
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
    }
	
}
