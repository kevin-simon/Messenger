import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.lightcouch.CouchDbClient;

import rmi.Type;
import utils.Properties;

import com.google.gson.JsonObject;

public class Messenger {
	
	private static Messenger instance;
	private static Type peerType;
	
	public static Messenger getInstance() {
		if (Messenger.instance == null) {
			Messenger.instance = new Messenger();
		}
		return Messenger.instance;
	}
	
	public Messenger() {
		this.peerType = Type.PEER;
	}

	public static void main(String[] args) throws Exception {
		/*Messenger messenger = Messenger.getInstance();
		messenger.subscribe();
		//new Window("Messenger");*/
		
		String Broadcastaddress = new String("192.168.1.255");
	    int port = 2000;
	    DatagramSocket serverSocket = new DatagramSocket();
	    serverSocket.setBroadcast(true);
	    InetAddress IPAddress = InetAddress.getByName(Broadcastaddress);
	    System.out.println("Sending Discovery message to " + IPAddress + " Via UDP port " + port);

	    byte[] sendData = new byte[4];
	    sendData[0] = 'F';
	    sendData[1] = 'I';
	    sendData[2] = 'N';
	    sendData[3] = 'D';

	    DatagramPacket sendPacket = new DatagramPacket(sendData,sendData.length,IPAddress,port);

	    int i = 0;
	    new Thread() {
	        public void run() {
	     
	            try {
	                int port = 2000;
	                DatagramSocket dsocket = new DatagramSocket(port);
	                byte[] buffer = new byte[2048];
	     
	                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
	                dsocket.receive(packet);
	                System.out.println("Receiving...");
	                String msg = new String(buffer, 0, packet.getLength());
	                System.out.println(packet.getAddress().getHostAddress() + " : " + msg);
	                packet.setLength(buffer.length);
	                dsocket.close();
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
	    }.start();
	    serverSocket.send(sendPacket);
	    System.out.println("Packet sent");
	}
	
	public void subscribe() {
		CouchDbClient dbClient = new CouchDbClient("messenger_application", true, "http", Properties.APP.get("subscribe_ip"), 5984, null, null);
		/*Map<String, Object> map = new HashMap<String, Object>();
		map.put("_id", "peers");
		map.put("192.168.1.8", this.peerType.toString());
		dbClient.update(map);*/
		JsonObject json = dbClient.find(JsonObject.class, "peers");
		System.out.println(json);
		
	}

}
