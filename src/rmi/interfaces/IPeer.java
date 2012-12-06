package rmi.interfaces;

import java.net.InetAddress;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import datas.Message;

public interface IPeer extends Remote {
	
	public boolean hasSuperPeers() throws RemoteException;
	public void connectTo(ArrayList<InetAddress> superPeerAddresses) throws RemoteException;
	public void receiveMessage(Message message) throws RemoteException;
}
