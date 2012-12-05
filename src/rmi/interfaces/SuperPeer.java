package rmi.interfaces;

import java.rmi.RemoteException;
import java.util.ArrayList;

import rmi.Client;

public class SuperPeer extends Peer implements ISuperPeer {

	private static final long serialVersionUID = -906717677759121748L;

	public SuperPeer() throws RemoteException {
		super();
		this.superPeers = new ArrayList<Client<ISuperPeer>>();
	}

}
