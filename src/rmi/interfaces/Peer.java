package rmi.interfaces;

import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
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
	protected InetAddress localAddress;

	public Peer(InetAddress inetAddresses) throws RemoteException {
		super();
		this.observableObject = new OPeer();
		this.localAddress = inetAddresses;
		this.superPeers = new ArrayList<Client<ISuperPeer>>();
	}
	
	public void addObserver(Observer observer) {
		this.observableObject.addObserver(observer);
	}

	@Override
	public void connectTo(InetAddress superPeerAddress) throws RemoteException {
		System.out.println(superPeerAddress);
		try {
			Client<ISuperPeer> client = new Client<ISuperPeer>("Messenger", superPeerAddress.getHostAddress(), Integer.parseInt(Properties.APP.get("rmi_port")));
			System.out.println(client.getClientHost());
			((ISuperPeer) client.getRemoteObject()).subscribePeer(localAddress);
			this.superPeers.add(client);
			System.out.println("Connexion au super pair : " + superPeerAddress.getHostAddress());
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (ServerNotActiveException e) {
			// TODO Auto-generated catch block
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
		return this.superPeers.size() != 0;
	}

}
