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
import ui.Window;
import utils.Discover;
import utils.OPeer;
import utils.OWindow;
import utils.Properties;
import datas.Identity;

public class Messenger implements Observer {
	
	private static Messenger instance;
	private Window window;
	private Type peerType;
	private Identity identity;
	private Discover discover;
	private Server server;
	private HashMap<String, Client<IPeer>> peers;
	private HashMap<String, Client<ISuperPeer>> superPeers;
	
	public static Messenger getInstance() {
		if (Messenger.instance == null) {
			try {
				Messenger.instance = new Messenger();
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}
		return Messenger.instance;
	}
	
	public Messenger() throws NumberFormatException, UnknownHostException {
		this.discover = new Discover();
		this.superPeers = new HashMap<String, Client<ISuperPeer>>();
		this.peerType = Type.PEER;
		discover.addObserver(this);
		discover.start();
	}
	
	public void sendBroadcast() {
		this.discover.sendPacket(this.identity.getPseudonyme(), this.peerType);
	}
	
	public void upgrade() {
		System.out.println("Mode Super-pair active !");
		this.peerType = Type.SUPER_PEER;
		this.peers = new HashMap<String, Client<IPeer>>();
	}
	
	public void startServer() {
		InetAddress localAddress = this.discover.getLocalAddress();
		this.server = new Server("Messenger", localAddress.getHostAddress(), Integer.parseInt(Properties.APP.get("rmi_port")));
		if (this.peerType == Type.SUPER_PEER) {
			try {
				SuperPeer superPeer = new SuperPeer(this, this.identity);
				this.server.start(superPeer);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		else if (this.peerType == Type.PEER) {
			try {
				Peer peer = new Peer(this, this.identity);
				this.server.start(peer);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws Exception {
		
		Messenger messenger = Messenger.getInstance();
		Window window = new Window("Messenger");
		messenger.setWindow(window);
	}
	
	public Type getPeerType() {
		return this.peerType;
	}
	
	public void setWindow(Window window) {
		this.window = window;
		this.window.addObserver(this);
	}
	
	@Override
	public void update(Observable o, Object object) {
		if (o instanceof Discover && object instanceof Identity) {
			Discover discover = (Discover) o;
			Identity clientIdentity = ((Identity) object);
			InetAddress clientAddress = discover.getBroadcastSender();
			if (clientIdentity.getType() == Type.PEER && this.peerType == Type.PEER && !discover.isLocalAddress(clientAddress)) {
				try {
					if (!((IPeer) server.getSharedObject()).hasSuperPeers()) {
						server.stop();
						this.upgrade();
						SuperPeer superPeer = new SuperPeer(this, this.identity);
						server.start(superPeer);
					}
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
			if (this.peerType == Type.SUPER_PEER && !discover.isLocalAddress(clientAddress)) {
				System.out.println("Demande d'acces d'un pair de type " + clientIdentity.getType());
				try {
					if (clientIdentity.getType() == Type.SUPER_PEER) {
						Client<ISuperPeer> client = new Client<ISuperPeer>("Messenger", clientIdentity.getAddress(), Integer.parseInt(Properties.APP.get("rmi_port")));
						this.superPeers.put(clientIdentity.getAddress(), client);
						((ISuperPeer) client.getRemoteObject()).connectTo(this.identity);
					}
					else if (clientIdentity.getType() == Type.PEER) {
						Client<IPeer> client = new Client<IPeer>("Messenger", clientIdentity.getAddress(), Integer.parseInt(Properties.APP.get("rmi_port")));
						this.peers.put(clientIdentity.getAddress(), client);
						System.out.println(((IPeer) client.getRemoteObject()));
						((IPeer) client.getRemoteObject()).connectTo(this.identity);
					}
					System.out.println("Connexion au pair " + clientIdentity.getAddress() + " via le protocole RMI");
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		}
		else if (o instanceof OPeer) {
			if (object instanceof ArrayList<?>) {
				ArrayList<Identity> identities = new ArrayList<Identity>();
				for (Object unknownObject : (ArrayList<?>) object) {
					if (unknownObject instanceof Identity) {
						identities.add((Identity) unknownObject);
					}
				}
				System.out.println(identities);
			}
		}
		else if (o instanceof OWindow) {
			if (object instanceof String) {
				String pseudonyme = (String) object;
				this.identity = new Identity(pseudonyme, this.discover.getLocalAddress(), this.peerType);
				this.startServer();
				this.sendBroadcast();
			}
		}
	}

}
