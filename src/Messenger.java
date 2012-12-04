import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import org.lightcouch.CouchDbClient;

import rmi.Client;
import rmi.Server;
import rmi.Type;
import rmi.interfaces.Connection;
import rmi.interfaces.IConnection;
import utils.Discover;
import utils.Properties;

import com.google.gson.JsonObject;

public class Messenger extends Server implements Observer {
	
	private static Messenger instance;
	private Type peerType;
	
	private ArrayList<Client<IConnection>> managedPeers;
	
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
		super("Messenger", "192.168.2.1", Integer.parseInt(Properties.APP.get("rmi_port")));
		this.peerType = Type.PEER;
	}
	
	public void upgrade() {
		this.peerType = Type.SUPER_PEER;
		this.managedPeers = new ArrayList<Client<IConnection>>();
	}

	public static void main(String[] args) throws Exception {
		Messenger messenger = Messenger.getInstance();
		messenger.upgrade();
		messenger.start(new Connection(messenger.peerType));
		//messenger.subscribe();
		Discover discover = new Discover();
		discover.addObserver(messenger);
		discover.start();
		discover.sendPacket(messenger.getPeerType());
		//new Window("Messenger");
	}
	
	public Type getPeerType() {
		return this.peerType;
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

	@Override
	public void update(Observable o, Object object) {
		if (o instanceof Discover && object instanceof InetAddress) {
			InetAddress clientAddress = ((InetAddress) object);
			boolean isLocalClient = ((Discover) o).getLocalAddresses().contains(clientAddress);
			if (this.peerType == Type.SUPER_PEER && !isLocalClient) {
				System.out.println("Demande d'accès d'un pair");
				try {
					Client<IConnection> client = new Client<IConnection>("Messenger", clientAddress.getHostAddress(), Integer.parseInt(Properties.APP.get("rmi_port")));
					this.managedPeers.add(client);
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
