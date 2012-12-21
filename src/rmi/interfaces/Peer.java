package rmi.interfaces;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Observer;

import rmi.Client;
import utils.OPeer;
import utils.Upgrade;
import datas.Identity;
import datas.Message;

public class Peer extends UnicastRemoteObject implements IPeer {

	private static final long serialVersionUID = -3914853933537229529L;
	protected Identity superPeer;
	protected OPeer observableObject;
	protected Upgrade upgradeObject;
	protected Identity localIdentity;

	public Peer(Observer observer, Identity identity) throws RemoteException {
		super();
		this.observableObject = new OPeer();
		this.observableObject.addObserver(observer);
		this.upgradeObject = new Upgrade();
		this.upgradeObject.addObserver(observer);
		this.localIdentity = identity;
	}
	
	@Override
	public void connectTo(Identity superPeerIdentity) throws RemoteException {
		if (this.superPeer == null || !superPeerIdentity.getIdentity().equals(this.superPeer.getIdentity())) {
			try {
				Client<ISuperPeer> client = new Client<ISuperPeer>("Messenger", superPeerIdentity.getAddress(), superPeerIdentity.getPort());
				((ISuperPeer) client.getRemoteObject()).subscribePeer(localIdentity);
				this.superPeer = superPeerIdentity;
				System.out.println("Connexion au super pair : " + superPeerIdentity.getAddress() + ":" + superPeerIdentity.getPort() + " effectuee");
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void receiveMessage(Message message) throws RemoteException {
		this.observableObject.setChanged();
		this.observableObject.notifyObservers(message);
	}

	@Override
	public boolean hasSuperPeers() throws RemoteException {
		return this.superPeer != null;
	}

	@Override
	public void updateIdentities(ArrayList<Identity> identities) throws RemoteException {
		this.observableObject.setChanged();
		this.observableObject.notifyObservers(identities);
	}

	@Override
	public void sendMessage(Message message) throws RemoteException {
		if (this.superPeer != null) {
			Client<ISuperPeer> client = new Client<ISuperPeer>("Messenger", this.superPeer.getAddress(), this.superPeer.getPort());
			ISuperPeer superPeer = ((ISuperPeer) client.getRemoteObject());
			if (superPeer != null) {
				superPeer.tranferMessage(message);
			}
			else {
				this.superPeer = null;
				this.upgradeObject.setChanged();
				this.upgradeObject.notifyObservers(message);
			}
		}
		else {
			this.upgradeObject.setChanged();
			this.upgradeObject.notifyObservers(message);
		}
	}

	@Override
	public void disconnect() throws RemoteException {
		if (this.superPeer != null) {
			Client<ISuperPeer> client = new Client<ISuperPeer>("Messenger", this.superPeer.getAddress(), this.superPeer.getPort());
			ISuperPeer superPeer = ((ISuperPeer) client.getRemoteObject());
			if (superPeer != null) {
				superPeer.disconnect(this.localIdentity);
			}
			else {
				this.superPeer = null;
				this.upgradeObject.setChanged();
				this.upgradeObject.notifyObservers(null);
			}
		}
		else {
			this.upgradeObject.setChanged();
			this.upgradeObject.notifyObservers(null);
		}
	}

	@Override
	public void upgradePeer(ArrayList<Identity> identities) throws RemoteException {
		System.out.println("Passage en mode Super-Pair demandé");
		this.upgradeObject.setChanged();
		this.upgradeObject.notifyObservers(identities);
	}
	
	

}
