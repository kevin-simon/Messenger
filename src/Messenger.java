import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import rmi.Client;
import rmi.Server;
import rmi.Type;
import rmi.interfaces.IPeer;
import rmi.interfaces.ISuperPeer;
import rmi.interfaces.Peer;
import rmi.interfaces.SuperPeer;
import utils.Discover;
import utils.Properties;

public class Messenger implements Observer {
	
	private static Messenger instance;
	private Type peerType;
	
	private ArrayList<Server> servers;
	private HashMap<String, Client<IPeer>> peers;
	private HashMap<String, Client<ISuperPeer>> superPeers;
	
	public static Messenger getInstance() {
		if (Messenger.instance == null) {
			try {
				Messenger.instance = new Messenger();
			} catch (NumberFormatException | UnknownHostException e) {
				e.printStackTrace();
			}
		}
		return Messenger.instance;
	}
	
	public Messenger() throws NumberFormatException, UnknownHostException {
		this.superPeers = new HashMap<String, Client<ISuperPeer>>();
		this.servers = new ArrayList<Server>();
		this.peerType = Type.PEER;
	}
	
	public void upgrade() {
		System.out.println("Mode Super-pair active !");
		this.peerType = Type.SUPER_PEER;
		this.peers = new HashMap<String, Client<IPeer>>();
	}
	
	public void startServers(ArrayList<InetAddress> addresses) {
		for (InetAddress address : addresses) {
			Server server = new Server("Messenger", address.getHostAddress(), Integer.parseInt(Properties.APP.get("rmi_port")));
			if (this.peerType == Type.SUPER_PEER) {
				try {
					server.start(new SuperPeer());
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
			else if (this.peerType == Type.PEER) {
				try {
					server.start(new Peer());
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
			this.servers.add(server);
		}
		
	}

	public static void main(String[] args) throws Exception {
		Discover discover = new Discover();
		
		Messenger messenger = Messenger.getInstance();
		//messenger.upgrade();
		messenger.startServers(discover.getLocalAddresses());
		discover.addObserver(messenger);
		discover.start();
		discover.sendPacket(messenger.getPeerType());
		//new Window("Messenger");
	}
	
	public Type getPeerType() {
		return this.peerType;
	}
	
	@Override
	public void update(Observable o, Object object) {
		if (o instanceof Discover && object instanceof Type) {
			Type clientType = ((Type) object);
			InetAddress clientAddress = ((Discover) o).getBroadcastSender();
			ArrayList<InetAddress> localAddresses = ((Discover) o).getLocalAddresses();
			boolean isLocalAddress = false;
			for (InetAddress localAddress : localAddresses) {
				if (localAddress.getHostAddress().equals(clientAddress.getHostAddress())) {
					isLocalAddress = true;
				}
			}
			if (this.peerType == Type.SUPER_PEER && !isLocalAddress) {
				System.out.println("Demande d'acces d'un pair de type " + clientType);
				try {
					if (clientType == Type.SUPER_PEER) {
						Client<ISuperPeer> client = new Client<ISuperPeer>("Messenger", clientAddress.getHostAddress(), Integer.parseInt(Properties.APP.get("rmi_port")));
						this.superPeers.put(clientAddress.getHostAddress(), client);
						((ISuperPeer) client.getRemoteObject()).connectTo(localAddresses);
					}
					else if (clientType == Type.PEER) {
						System.out.println(clientAddress);
						Client<IPeer> client = new Client<IPeer>("Messenger", clientAddress.getHostAddress(), Integer.parseInt(Properties.APP.get("rmi_port")));
						this.peers.put(clientAddress.getHostAddress(), client);
						System.out.println(client.getRemoteObject());
						IPeer peer = ((IPeer) client.getRemoteObject());
						System.out.println(peer);
						peer.connectTo(localAddresses);
					}
					System.out.println("Connexion au pair " + clientAddress.getHostAddress() + " via le protocole RMI");
				} catch (NumberFormatException | RemoteException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
