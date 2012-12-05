package rmi;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class Server {
	
	private String url;
	private boolean running;
	private Remote sharedObject;

	public Server(String name, int port) throws UnknownHostException {
		this(name, InetAddress.getLocalHost().getHostAddress(), port);
	}
	
	public Server(String name, String host, int port) {
		try {
			LocateRegistry.createRegistry(port);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		this.running = false;
		this.url = "rmi://" + host + ":" + port + "/" + name;
	}
	
	public Remote getSharedObject() {
		return this.sharedObject;
	}
	
	public void start(Remote object) {
		System.out.println("Enregistrement de l'objet avec l'url : " + this.url);
		try {
			Naming.rebind(this.url, object);
			this.sharedObject = object;
			this.running = true;
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		System.out.println("Serveur lance");
	}
	
	public boolean isRunning() {
		return this.running;
	}
	
	public void stop() {
		try {
			Naming.unbind(this.url);
			this.running = false;
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		System.out.println("Serveur arrete");
	}
}
