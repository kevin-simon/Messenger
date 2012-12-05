package rmi.interfaces;

import java.net.InetAddress;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IPeer extends Remote {
	
	public void connectTo(ArrayList<InetAddress> superPeerAddresses) throws RemoteException;
}
