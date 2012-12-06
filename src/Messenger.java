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
import utils.OPeer;
import utils.Properties;
import datas.Identity;
import datas.Message;

public class Messenger implements Observer {
	
	private static Messenger instance;
	private Type peerType;
	
	private Discover discover;
	private HashMap<InetAddress, Server> servers;
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
		this.servers = new HashMap<InetAddress, Server>();
		this.peerType = Type.PEER;
		discover.addObserver(this);
		discover.start();
	}
	
	public void sendBroadcast(String pseudonyme) {
		this.discover.sendPacket(pseudonyme, this.peerType);
	}
	
	public void upgrade() {
		System.out.println("Mode Super-pair active !");
		this.peerType = Type.SUPER_PEER;
		this.peers = new HashMap<String, Client<IPeer>>();
	}
	
	public void startServers() {
		for (InetAddress address : this.discover.getLocalAddresses()) {
			if (!address.isLoopbackAddress()) {
				Server server = new Server("Messenger", address.getHostAddress(), Integer.parseInt(Properties.APP.get("rmi_port")));
				if (this.peerType == Type.SUPER_PEER) {
					try {
						SuperPeer superPeer = new SuperPeer(address);
						superPeer.addObserver(this);
						server.start(superPeer);
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
				else if (this.peerType == Type.PEER) {
					try {
						Peer peer = new Peer();
						peer.addObserver(this);
						server.start(peer);
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
				this.servers.put(address, server);
			}
		}
		
	}

	public static void main(String[] args) throws Exception {
		
		Messenger messenger = Messenger.getInstance();
		messenger.startServers();
		messenger.sendBroadcast("kekekiwi");
		
		/*Client<IPeer> client = new Client<IPeer>("Messenger", "192.168.1.15", 2001);
		IPeer superPeer = (IPeer) client.getRemoteObject();
		Message message = new Message(new Identity("Kekekiwi", InetAddress.getByName("192.168.1.8"), Type.SUPER_PEER), new Identity("Kekekiwi", InetAddress.getByName("192.168.1.8"), Type.SUPER_PEER), "Pouet !");
		superPeer.receiveMessage(message);*/
		
		//new Window("Messenger");
	}
	
	public Type getPeerType() {
		return this.peerType;
	}
	
	@Override
	public void update(Observable o, Object object) {
		if (o instanceof Discover && object instanceof Identity) {
			Identity clientIdentity = ((Identity) object);
			InetAddress clientAddress = ((Discover) o).getBroadcastSender();
			if (this.peerType == Type.PEER && !((Discover) o).isLocalAddress(clientAddress)) {
				ArrayList<InetAddress> serversAddresses = new ArrayList<InetAddress>(this.servers.keySet());
				for (int i = 0 ; i < serversAddresses.size() ; i++) {
					try {
						InetAddress address = serversAddresses.get(i);
						if (!((IPeer) this.servers.get(address).getSharedObject()).hasSuperPeers()) {
							this.upgrade();
							this.servers.get(address).stop();
							Server server = new Server("Messenger", address.getHostAddress(), Integer.parseInt(Properties.APP.get("rmi_port")));
							SuperPeer superPeer = new SuperPeer(address);
							superPeer.addObserver(this);
							server.start(superPeer);
							this.servers.put(serversAddresses.get(i), server);
						}
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
			}
			if (this.peerType == Type.SUPER_PEER && !((Discover) o).isLocalAddress(clientAddress)) {
				System.out.println("Demande d'acces d'un pair de type " + clientIdentity.getType());
				try {
					if (clientIdentity.getType() == Type.SUPER_PEER) {
						Client<ISuperPeer> client = new Client<ISuperPeer>("Messenger", clientAddress.getHostAddress(), Integer.parseInt(Properties.APP.get("rmi_port")));
						this.superPeers.put(clientAddress.getHostAddress(), client);
						((ISuperPeer) client.getRemoteObject()).connectTo(((Discover) o).getLocalAddresses());
					}
					else if (clientIdentity.getType() == Type.PEER) {
						Client<IPeer> client = new Client<IPeer>("Messenger", clientAddress.getHostAddress(), Integer.parseInt(Properties.APP.get("rmi_port")));
						this.peers.put(clientAddress.getHostAddress(), client);
						((IPeer) client.getRemoteObject()).connectTo(((Discover) o).getLocalAddresses());
					}
					System.out.println("Connexion au pair " + clientAddress.getHostAddress() + " via le protocole RMI");
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		}
		else if (o instanceof OPeer && object instanceof Message) {
			Message message = (Message) object;
			System.out.println("Reception d'un message de la part de " + message.getSender());
			System.out.println(message.getMessage());
		}
	}

}
