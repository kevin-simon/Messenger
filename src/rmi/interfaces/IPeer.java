package rmi.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import datas.Identity;
import datas.Message;

public interface IPeer extends Remote {
	
	public boolean hasSuperPeers() throws RemoteException;
	public void connectTo(Identity superPeerIdentity) throws RemoteException;
	public void receiveMessage(Message message) throws RemoteException;
	public void updateIdentities(ArrayList<Identity> identity) throws RemoteException;
}
