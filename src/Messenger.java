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
import rmi.interfaces.Connection;
import rmi.interfaces.IConnection;
import utils.Discover;
import utils.Properties;

public class Messenger implements Observer {
	
	private static Messenger instance;
	private Type peerType;
	
	private ArrayList<Server> rmiServers;
	private HashMap<String, Client<IConnection>> superPeers;
	private HashMap<String, Client<IConnection>> managedPeers;
	
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
		this.superPeers = new HashMap<String, Client<IConnection>>();
		this.rmiServers = new ArrayList<Server>();
		this.peerType = Type.PEER;
	}
	
	public void upgrade() {
		this.peerType = Type.SUPER_PEER;
		this.managedPeers = new HashMap<String, Client<IConnection>>();
	}
	
	public void startServers(ArrayList<InetAddress> addresses) {
		for (InetAddress address : addresses) {
			System.out.println(address);
			Server server = new Server("Messenger", address.getHostAddress(), Integer.parseInt(Properties.APP.get("rmi_port")));
			Connection connection = new Connection(addresses);
			connection.addObserver(this);
			server.start(connection);
			this.rmiServers.add(server);
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
					if (this.managedPeers.containsKey(clientAddress.getHostAddress())) {
						System.out.println("Already started");
					}
					Client<IConnection> client = new Client<IConnection>("Messenger", clientAddress.getHostAddress(), Integer.parseInt(Properties.APP.get("rmi_port")));
					this.managedPeers.put(clientAddress.getHostAddress(), client);
					((IConnection) client.getRemoteObject()).sendConnectionInformations(localAddresses);
					System.out.println("Connexion au pair via le protocole RMI");
				} catch (NumberFormatException | RemoteException e) {
					e.printStackTrace();
				}
			}
		}
		else if (o instanceof Connection && object instanceof InetAddress) {
			InetAddress serverAddress = ((InetAddress) object);
			ArrayList<InetAddress> localAddresses = ((Discover) o).getLocalAddresses();
			if (!localAddresses.contains(serverAddress)) {
				System.out.println("Retour de connexion du super pair");
				try {
					if (this.superPeers.containsKey(serverAddress.getHostAddress())) {
						System.out.println("Already started");
					}
					Client<IConnection> client = new Client<IConnection>("Messenger", serverAddress.getHostAddress(), Integer.parseInt(Properties.APP.get("rmi_port")));
					this.superPeers.put(serverAddress.getHostAddress(), client);
				} catch (NumberFormatException | RemoteException e) {
					e.printStackTrace();
				}
			}
			for (String superPeer : this.superPeers.keySet()) {
				System.out.println(superPeer);
			}
		}
		System.out.println(object);
	}

}
