package rmi.interfaces;

import java.net.InetAddress;
import java.rmi.Remote;
import java.rmi.RemoteException;

import datas.Message;

public interface IPeer extends Remote {
	
	public boolean hasSuperPeers() throws RemoteException;
	public void connectTo(InetAddress superPeerAddress) throws RemoteException;
	public void receiveMessage(Message message) throws RemoteException;
}
