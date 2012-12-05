package rmi.interfaces;

import java.io.Serializable;
import java.net.InetAddress;
import java.rmi.Remote;
import java.util.ArrayList;

public interface IConnection extends Remote, Serializable {

	public void sendConnectionInformations(ArrayList<InetAddress> inetAddress);
}
