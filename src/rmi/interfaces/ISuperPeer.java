package rmi.interfaces;

import java.net.InetAddress;
import java.rmi.RemoteException;
import java.util.ArrayList;

import rmi.Client;
import datas.Message;

public interface ISuperPeer extends IPeer {

	public ArrayList<Client<IPeer>> getOnlinePeers() throws RemoteException;
	public void subscribePeer(InetAddress inetAddress) throws RemoteException;
	public void addSuperPeer(InetAddress inetAddress) throws RemoteException;
	public void tranferMessage(Message message) throws RemoteException;
}
