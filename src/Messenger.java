import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.server.ExportException;
import java.util.ArrayList;
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
import utils.KeepAlive;
import utils.OPeer;
import utils.OWindow;
import utils.Properties;
import utils.Upgrade;
import datas.Identity;
import datas.Message;

public class Messenger implements Observer {
	
	private static Messenger instance;
	private Window window;
	private Type peerType;
	private Identity identity;
	private Server server;
	private KeepAlive keepAlive;
	private ArrayList<Message> waitingMessages;
	
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
		this.peerType = Type.PEER;
		this.waitingMessages = new ArrayList<Message>();
	}
	
	public void upgrade() {
		System.out.println("Mode Super-pair active !");
		this.peerType = Type.SUPER_PEER;
	}
	
	public void startServer() {
		try {
			this.server = new Server("Messenger", this.identity.getAddress(), this.identity.getPort());
		} catch (ExportException e) {
			this.window.errorStartServer();
			System.out.println("Fermeture de l'application !");
			System.exit(1);
		}
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
		if (o instanceof KeepAlive && object instanceof Identity) {
			Identity clientIdentity = ((Identity) object);
			System.out.println(this.identity + " : " + this.peerType);
			System.out.println(clientIdentity + " : " + clientIdentity.getType());
			if (clientIdentity.getType() == Type.PEER && this.peerType == Type.PEER && clientIdentity.getPort() != this.identity.getPort()) {	
				try {
					IPeer localServer = (IPeer) this.server.getSharedObject();
					ISuperPeer superPeer = null;
					if (localServer.hasSuperPeers()) {
						Identity superPeerIdentity = localServer.getSuperPeerIdentity();
						Client<ISuperPeer> client = new Client<ISuperPeer>("Messenger", superPeerIdentity.getAddress(), superPeerIdentity.getPort());
						superPeer = client.getRemoteObject();
					}
					if (superPeer == null) {
						this.server.stop();
						this.upgrade();
						this.server.start(new SuperPeer(this, this.identity));
					}
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
			if (this.peerType == Type.SUPER_PEER && clientIdentity.getPort() != this.identity.getPort()) {
				System.out.println("Demande d'acces d'un pair de type " + clientIdentity.getType());
				try {
					if (clientIdentity.getType() == Type.SUPER_PEER) {
						Client<ISuperPeer> client = new Client<ISuperPeer>("Messenger", clientIdentity.getAddress(), clientIdentity.getPort());
						((ISuperPeer) client.getRemoteObject()).connectTo(this.identity);
					}
					else if (clientIdentity.getType() == Type.PEER) {
						Client<IPeer> client = new Client<IPeer>("Messenger", clientIdentity.getAddress(), clientIdentity.getPort());
						((IPeer) client.getRemoteObject()).connectTo(this.identity);
					}
					System.out.println("Connexion au pair " + clientIdentity.getAddress() + ":" + clientIdentity.getPort() + " via le protocole RMI");
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
				if (this.peerType == Type.SUPER_PEER) {
					for (Identity identity : identities) {
						try {
							Client<IPeer> client = new Client<IPeer>("Messenger", identity.getAddress(), identity.getPort());
							ArrayList<Identity> sendedIdentities = new ArrayList<Identity>(identities);
							sendedIdentities.remove(identity);
							((IPeer) client.getRemoteObject()).updateIdentities(sendedIdentities);
						} catch (NumberFormatException e) {
							e.printStackTrace();
						} catch (RemoteException e) {
							e.printStackTrace();
						} 
					}
				}
				identities.remove(this.identity);
				this.window.updateIdentityList(identities);
				try {
					Client<IPeer> client = new Client<IPeer>("Messenger", this.identity.getAddress(), this.identity.getPort());
					for (Message waitingMessage : this.waitingMessages) {
						((IPeer) client.getRemoteObject()).sendMessage(waitingMessage);
					}
					this.waitingMessages.clear();
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
			else if (object instanceof Message) {
				Message message = (Message) object;
				this.window.receiveMessage(message);
			}
		}
		else if (o instanceof Upgrade) {
			if (object instanceof ArrayList<?>) {
				ArrayList<Identity> identities = new ArrayList<Identity>();
				for (Object unknownObject : (ArrayList<?>) object) {
					if (unknownObject instanceof Identity) {
						identities.add((Identity) unknownObject);
					}
				}
				try {
					this.server.stop();
					this.upgrade();
					SuperPeer superPeer = new SuperPeer(this, this.identity);
					this.server.start(superPeer);
					this.keepAlive = new KeepAlive(this, this.identity.getPseudonyme(), this.peerType);
					this.window.updateIdentityList(null);
					for (Identity identity : identities) {
						Client<IPeer> client = new Client<IPeer>("Messenger", identity.getAddress(), identity.getPort());
						((IPeer) client.getRemoteObject()).connectTo(this.identity);
					}
				} catch (RemoteException e) {
					e.printStackTrace();
				} 
			}
			else if (object instanceof Message) {
				this.waitingMessages.add((Message) object);
			}
		}
		else if (o instanceof OWindow) {
			if (object instanceof String) {
				String pseudonyme = (String) object;
				this.keepAlive = new KeepAlive(this, pseudonyme, this.peerType);
				this.identity = this.keepAlive.getIdentity();
				this.window.initializeView(this.identity);
				this.startServer();
				this.keepAlive.start();
			}
			else if (object instanceof Message) {
				Message message = (Message) object;
				try {
					Client<IPeer> client = new Client<IPeer>("Messenger", this.identity.getAddress(), this.identity.getPort());
					((IPeer) client.getRemoteObject()).sendMessage(message);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (RemoteException e) {
					e.printStackTrace();
				} 
			}
			else if (object == null) {
				this.keepAlive.stop();
				this.closeApplication();
			}
		}
	}
	
	public void closeApplication() {
		try {
			if (this.identity != null) {
				Client<ISuperPeer> client = new Client<ISuperPeer>("Messenger", this.identity.getAddress(), Integer.parseInt(Properties.APP.get("rmi_port")));
				Object remoteObject = client.getRemoteObject();
				if (remoteObject instanceof ISuperPeer) {
					ArrayList<Identity> onlinePeers = ((ISuperPeer) remoteObject).getOnlinePeers();
					for (Identity identity : onlinePeers) {
						if (identity.getAddress().equals(this.identity.getAddress()) && identity.getPort() == this.identity.getPort()) {
							onlinePeers.remove(identity);
							break;
						}
					}
					if (onlinePeers.size() != 0) {
						int randomClient = (int)(Math.random() * (onlinePeers.size()-1));
						Identity newSuperPeerIdentity = onlinePeers.get(randomClient);
						Client<IPeer> newSuperPeer = new Client<IPeer>("Messenger", newSuperPeerIdentity.getAddress(), newSuperPeerIdentity.getPort());
						onlinePeers.remove(newSuperPeerIdentity);
						((IPeer) newSuperPeer.getRemoteObject()).upgradePeer(onlinePeers);
					}
				}
				else {
					((IPeer) client.getRemoteObject()).disconnect();
				}
			}
			System.out.println("Fermeture de l'application !");
			System.exit(0);
		} catch (RemoteException e) {
			e.printStackTrace();
		} 
	}

}
