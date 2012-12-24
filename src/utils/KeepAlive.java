package utils;

import java.util.Observable;
import java.util.Observer;

import rmi.Type;

import datas.Identity;

public class KeepAlive extends Observable implements Runnable, Observer {

	public Identity identity;
	public Discover discover;
	
	public KeepAlive(Observer observer, String pseudonyme, Type peerType) {
		this.discover = new Discover();
		this.discover.addObserver(this);
		this.discover.start();
		this.addObserver(observer);
		this.identity = new Identity(pseudonyme, this.discover.getLocalAddress(), peerType, Integer.parseInt(Properties.APP.get("rmi_port")));
	}
	
	public Identity getIdentity() {
		return this.identity;
	}
	
	public void start() {
		System.out.println("Démarrage du Keeping-alive (" + Properties.APP.get("keepalive_time") + "s)");
		new Thread(this).start();
	}
	
	public void stop() {
		this.discover.stop();
	}
	
	@Override
	public void run() {
		while (true) {
			this.discover.start();
			this.sendBroadcast();
			try {
				Thread.sleep(Integer.parseInt(Properties.APP.get("keepalive_time")) * 1000);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		this.setChanged();
		this.notifyObservers(arg);
	}

	public void sendBroadcast() {
		this.discover.sendPacket(this.identity.getPseudonyme(), this.identity.getType(), this.identity.getPort());
	}

}
