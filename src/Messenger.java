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
			server.start(new Connection(this.peerType));
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
		if (o instanceof Discover && object instanceof InetAddress) {
			InetAddress clientAddress = ((InetAddress) object);
			boolean isLocalClient = ((Discover) o).getLocalAddresses().contains(clientAddress);
			if (this.peerType == Type.SUPER_PEER && !isLocalClient) {
				System.out.println("Demande d'acces d'un pair");
				try {
					Client<IConnection> client = new Client<IConnection>("Messenger", clientAddress.getHostAddress(), Integer.parseInt(Properties.APP.get("rmi_port")));
					this.managedPeers.put(clientAddress.getHostAddress(), client);
					System.out.println(((IConnection) client.getObject()).getPeerType());
				} catch (NumberFormatException | RemoteException e) {
					e.printStackTrace();
				}
				System.out.println("Connexion au pair via le protocole RMI");
			}
			System.out.println(object);
		}
	}

}
