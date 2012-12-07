package datas;

import java.io.Serializable;
import java.net.InetAddress;

import rmi.Type;

public class Identity implements Serializable {

	private static final long serialVersionUID = -8431383752355988588L;
	
	private String pseudonyme;
	private InetAddress address;
	private Type type;
	
	public Identity(Identity identity) {
		this(identity.getAddress(), identity.getInetAddress(), identity.getType());
	}

	public Identity(String pseudonyme, InetAddress inetAddress, Type type) {
		this.pseudonyme = pseudonyme;
		this.address = inetAddress;
		this.type = type;
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
	
	public String toString() {
		return this.pseudonyme + "@" + this.address.getHostAddress();
	}
}
