package rmi.interfaces;

import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Observer;

import rmi.Client;
import utils.OPeer;
import utils.Properties;
import datas.Message;

public class Peer extends UnicastRemoteObject implements IPeer {

	private static final long serialVersionUID = -3914853933537229529L;
	protected ArrayList<Client<ISuperPeer>> superPeers;
	private OPeer observableObject;

	public Peer() throws RemoteException {
		super();
		this.observableObject = new OPeer();
		this.superPeers = new ArrayList<Client<ISuperPeer>>();
	}
	
	public void addObserver(Observer observer) {
		this.observableObject.addObserver(observer);
	}

	@Override
	public void connectTo(ArrayList<InetAddress> superPeerAddresses) throws RemoteException {
		for (InetAddress superPeerAddress : superPeerAddresses) {
			try {
				this.superPeers.add(new Client<ISuperPeer>("Messenger", superPeerAddress.getHostAddress(), Integer.parseInt(Properties.APP.get("rmi_port"))));
				System.out.println("Connexion au super pair : " + superPeerAddress.getHostAddress());
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
		return this.superPeers.size() != 0;
	}

}
