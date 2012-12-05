package rmi.interfaces;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Observable;

public class Connection extends Observable implements IConnection {

	private static final long serialVersionUID = -5106772794510276705L;
	
	public Connection() {}

	@Override
	public void sendConnectionInformations(ArrayList<InetAddress> inetAddresses) {
		this.setChanged();
		this.notifyObservers(inetAddresses);
		for (InetAddress address : inetAddresses)
			System.out.println(address);
	}

}
