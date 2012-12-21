package rmi;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.ExportException;

public class Server {
	
	private String host;
	private int port;
	private String name;
	private boolean running;
	private Remote sharedObject;

	public Server(String name, int port) throws UnknownHostException {
		this(name, InetAddress.getLocalHost().getHostAddress(), port);
	}
	
	public Server(String name, String host, int port) {
		try {
			LocateRegistry.createRegistry(port);
		} catch (ExportException e) {
			System.out.println("Impossible de lancer le serveur : port déjà utilisé");
			System.exit(1);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		this.running = false;
		this.name = name;
		this.host = host;
		this.port = port;
	}
	
	public Remote getSharedObject() {
		return this.sharedObject;
	}
	
	public void start(Remote object) {
		System.out.println("Enregistrement de l'objet avec l'url : " + this.getUrl());
		try {
			Naming.rebind(this.getUrl(), object);
			this.sharedObject = object;
			this.running = true;
		} catch (ExportException e) {
			System.out.println("Impossible de lancer le serveur : port déjà utilisé");
			System.exit(1);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		System.out.println("Serveur lance");
	}
	
	public String getUrl() {
		return  "rmi://" + this.host + ":" + this.port + "/" + this.name;
	}
	
	public boolean isRunning() {
		return this.running;
	}
	
	public void stop() {
		try {
			Naming.unbind(this.getUrl());
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
