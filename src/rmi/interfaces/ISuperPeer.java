package rmi.interfaces;

import java.rmi.RemoteException;
import java.util.ArrayList;

import datas.Identity;
import datas.Message;

public interface ISuperPeer extends IPeer {

	public ArrayList<Identity> getOnlinePeers() throws RemoteException;
	public void subscribePeer(Identity identity) throws RemoteException;
	public void addSuperPeer(Identity identity) throws RemoteException;
	public void tranferMessage(Message message) throws RemoteException;
}
