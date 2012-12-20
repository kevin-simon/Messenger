package datas;

import java.io.Serializable;
import java.net.InetAddress;

import rmi.Type;

public class Identity implements Serializable {

	private static final long serialVersionUID = -8431383752355988588L;
	
	private String pseudonyme;
	private InetAddress address;
	private Type type;
	private int port;
	
	public Identity(Identity identity) {
		this(identity.getAddress(), identity.getInetAddress(), identity.getType(), identity.getPort());
	}

	public Identity(String pseudonyme, InetAddress inetAddress, Type type, int port) {
		this.pseudonyme = pseudonyme;
		this.address = inetAddress;
		this.type = type;
		this.port = port;
	}
	
	public InetAddress getInetAddress() {
		return this.address;
	}
	
	public String getAddress() {
		return this.address.getHostAddress();
	}
	
	public String getIdentity() {
		return this.toString();
	}
	
	public String getPseudonyme() {
		return this.pseudonyme;
	}
	
	public Type getType() {
		return this.type;
	}
	
	public int getPort() {
		return this.port;
	}
	
	public String toString() {
		return this.pseudonyme + "@" + this.address.getHostAddress();
	}
}
