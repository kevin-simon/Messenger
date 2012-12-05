package rmi.interfaces;

import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import rmi.Client;
import utils.Properties;

public class Peer extends UnicastRemoteObject implements IPeer {

	private static final long serialVersionUID = -3914853933537229529L;
	protected ArrayList<Client<ISuperPeer>> superPeers;

	public Peer() throws RemoteException {
		super();
		this.superPeers = new ArrayList<Client<ISuperPeer>>();
	}

	@Override
	public void connectTo(ArrayList<InetAddress> superPeerAddresses) {
		for (InetAddress superPeerAddress : superPeerAddresses) {
			try {
				this.superPeers.add(new Client<ISuperPeer>("Messenger", superPeerAddress.getHostAddress(), Integer.parseInt(Properties.APP.get("rmi_port"))));
				System.out.println("Connexion au super pair : " + superPeerAddress.getHostAddress());
			} catch (NumberFormatException | RemoteException e) {
				e.printStackTrace();
			}
		}
	}

}
