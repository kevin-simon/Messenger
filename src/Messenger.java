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
import rmi.interfaces.Peer;
import rmi.interfaces.SuperPeer;
import utils.Discover;
import utils.Properties;

public class Messenger implements Observer {
	
	private static Messenger instance;
	private Type peerType;
	
	private ArrayList<Server> servers;
	private HashMap<String, Client<IPeer>> peers;
	
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
		this.peers = new HashMap<String, Client<IPeer>>();
		this.servers = new ArrayList<Server>();
		this.peerType = Type.PEER;
	}
	
	public void upgrade() {
		this.peerType = Type.SUPER_PEER;
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
		messenger.upgrade();
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
		System.out.println(o);
		if (o instanceof Discover && object instanceof InetAddress) {
			InetAddress clientAddress = ((InetAddress) object);
			ArrayList<InetAddress> localAddresses = ((Discover) o).getLocalAddresses();
			if (this.peerType == Type.SUPER_PEER && !localAddresses.contains(clientAddress)) {
				System.out.println("Demande d'acces d'un pair");
				try {
					if (this.peers.containsKey(clientAddress.getHostAddress())) {
						System.out.println("Already started");
					}
					Client<IPeer> client = new Client<IPeer>("Messenger", clientAddress.getHostAddress(), Integer.parseInt(Properties.APP.get("rmi_port")));
					this.peers.put(clientAddress.getHostAddress(), client);
					((IPeer) client.getRemoteObject()).connectTo(localAddresses);
					System.out.println("Connexion au pair via le protocole RMI");
				} catch (NumberFormatException | RemoteException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
