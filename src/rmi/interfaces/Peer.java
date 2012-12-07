package rmi.interfaces;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Observer;

import rmi.Client;
import utils.OPeer;
import utils.Properties;
import datas.Identity;
import datas.Message;

public class Peer extends UnicastRemoteObject implements IPeer {

	private static final long serialVersionUID = -3914853933537229529L;
	protected Identity superPeer;
	protected OPeer observableObject;
	protected Identity localIdentity;

	public Peer(Observer observer, Identity identity) throws RemoteException {
		super();
		this.observableObject = new OPeer();
		this.observableObject.addObserver(observer);
		this.localIdentity = identity;
	}
	
	@Override
	public void connectTo(Identity superPeerIdentity) throws RemoteException {
		try {
			Client<ISuperPeer> client = new Client<ISuperPeer>("Messenger", superPeerIdentity.getAddress(), Integer.parseInt(Properties.APP.get("rmi_port")));
			((ISuperPeer) client.getRemoteObject()).subscribePeer(localIdentity);
			this.superPeer = superPeerIdentity;
			System.out.println("Connexion au super pair : " + superPeerIdentity.getAddress() + " effectuee");
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
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
	
	

}
