package rmi.interfaces;

import java.net.InetAddress;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Observable;

import rmi.Client;
import utils.Properties;

public class Connection extends Observable implements IConnection {

	private static final long serialVersionUID = -5106772794510276705L;
	
	private ArrayList<InetAddress> inetAddresses;
	private ArrayList<Client<IConnection>> clients;
	
	public Connection(ArrayList<InetAddress> inetAddresses) {
		this.inetAddresses = inetAddresses;
		this.clients = new ArrayList<Client<IConnection>>();
	}

	@Override
	public void sendConnectionInformations(ArrayList<InetAddress> inetAddresses) {
		System.out.println(this.inetAddresses);
		this.setChanged();
		this.notifyObservers(inetAddresses);
		for (InetAddress address : inetAddresses)
			try {
				this.clients.add(new Client<IConnection>("Messenger", address.getHostAddress(), Integer.parseInt(Properties.APP.get("rmi_port"))));
			} catch (NumberFormatException | RemoteException e) {
				e.printStackTrace();
			}
		
	}

}
