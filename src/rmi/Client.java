package rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Client<T extends Remote> extends UnicastRemoteObject {
	
	private static final long serialVersionUID = -7089419078265136378L;
	
	private String hostAddress;
	private String serverUrl;

	public Client(String name, String hostAddress, int port) throws RemoteException {
		this.hostAddress = hostAddress;
		this.serverUrl = "rmi://" + hostAddress + ":" + port + "/" + name;
	}
	
	public String getHostAddress() {
		return this.hostAddress;
	}

	@SuppressWarnings("unchecked")
	public T getRemoteObject() {
		T remote = null;
		try {
			remote = (T) Naming.lookup(this.serverUrl);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		return remote;
	}
}

