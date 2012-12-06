package rmi.interfaces;

import java.net.InetAddress;
import java.rmi.RemoteException;
import java.util.ArrayList;

import rmi.Client;
import utils.Properties;
import datas.Message;

public class SuperPeer extends Peer implements ISuperPeer {

	private static final long serialVersionUID = -906717677759121748L;
	
	private ArrayList<Client<IPeer>> onlinePeers;
	private ArrayList<Message> transmittedMessages;

	public SuperPeer(ArrayList<InetAddress> inetAddresses) throws RemoteException {
		super(inetAddresses);
		this.superPeers = new ArrayList<Client<ISuperPeer>>();
		this.onlinePeers = new ArrayList<Client<IPeer>>();
		this.transmittedMessages = new ArrayList<Message>();
		for (InetAddress localAddress : inetAddresses) {
			this.onlinePeers.add(new Client<IPeer>("Messenger", localAddress.getHostAddress(), Integer.parseInt(Properties.APP.get("rmi_port"))));
		}
	}
	
	@Override
	public ArrayList<Client<IPeer>> getOnlinePeers() {
		return this.onlinePeers;
	}

	@Override
	public void tranferMessage(Message message) throws RemoteException {
		boolean messageTransmit = false;
		for (Client<IPeer> client : this.onlinePeers) {
			if (client.getHostAddress().equals(message.getReceiver().getAddress())) {
				((IPeer) client.getRemoteObject()).receiveMessage(message);
				System.out.println("Reception du message !");
				messageTransmit = true;
				break;
			}
		}
		if (!messageTransmit) {
			this.transmittedMessages.add(message);
			for (Client<ISuperPeer> superPeer : this.superPeers) {
				((ISuperPeer) superPeer.getRemoteObject()).tranferMessage(message);
			}
			System.out.println("Transfert du message a " + this.superPeers.size() + " super-pairs !");
		}
	}

	@Override
	public void subscribePeer(InetAddress inetAddress) throws RemoteException {
		this.onlinePeers.add(new Client<IPeer>("Messenger", inetAddress.getHostAddress(), Integer.parseInt(Properties.APP.get("rmi_port"))));
	}

	@Override
	public void addSuperPeer(InetAddress inetAddress) throws RemoteException {
		this.superPeers.add(new Client<ISuperPeer>("Messenger", inetAddress.getHostAddress(), Integer.parseInt(Properties.APP.get("rmi_port"))));
	}

	
}
