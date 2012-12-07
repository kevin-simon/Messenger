package rmi.interfaces;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Observer;

import rmi.Client;
import utils.Properties;
import datas.Identity;
import datas.Message;

public class SuperPeer extends Peer implements ISuperPeer {

	private static final long serialVersionUID = -906717677759121748L;

	private ArrayList<Identity> superPeers;
	private ArrayList<Identity> onlinePeers;
	private ArrayList<Message> transmittedMessages;

	public SuperPeer(Observer observer, Identity identity) throws RemoteException {
		super(observer, identity);
		this.superPeers = new ArrayList<Identity>();
		this.onlinePeers = new ArrayList<Identity>();
		this.transmittedMessages = new ArrayList<Message>();
		this.onlinePeers.add(this.localIdentity);
	}
	
	@Override
	public ArrayList<Identity> getOnlinePeers() {
		return this.onlinePeers;
	}

	@Override
	public void tranferMessage(Message message) throws RemoteException {
		boolean messageTransmit = false;
		for (Identity peerIdentity : this.onlinePeers) {
			if (peerIdentity.getIdentity().equals(message.getReceiver().getIdentity())) {
				Client<IPeer> client = new Client<IPeer>("Messenger", peerIdentity.getAddress(), Integer.parseInt(Properties.APP.get("rmi_port")));
				((IPeer) client.getRemoteObject()).receiveMessage(message);
				messageTransmit = true;
				break;
			}
		}
		if (!messageTransmit) {
			this.transmittedMessages.add(message);
			for (Identity superPeerIdentity : this.superPeers) {
				Client<ISuperPeer> client = new Client<ISuperPeer>("Messenger", superPeerIdentity.getAddress(), Integer.parseInt(Properties.APP.get("rmi_port")));
				((ISuperPeer) client.getRemoteObject()).tranferMessage(message);
			}
			System.out.println("Transfert du message a " + this.superPeers.size() + " super-pairs !");
		}
	}

	@Override
	public void subscribePeer(Identity identity) throws RemoteException {
		this.onlinePeers.add(identity);
		this.observableObject.setChanged();
		this.observableObject.notifyObservers(this.onlinePeers);
	}

	@Override
	public void addSuperPeer(Identity identity) throws RemoteException {
		this.superPeers.add(identity);
	}

	@Override
	public void disconnect(Identity identity) throws RemoteException {
		Identity remove = null;
		for (Identity onlinePeer : this.onlinePeers) {
			if (onlinePeer.getIdentity().equals(identity.getIdentity())) {
				remove = onlinePeer;
			}
		}
		this.onlinePeers.remove(remove);
		this.observableObject.setChanged();
		this.observableObject.notifyObservers(this.onlinePeers);
	}

	
}
