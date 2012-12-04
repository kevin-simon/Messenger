package rmi.interfaces;

import rmi.Type;

public class Connection implements IConnection {

	private static final long serialVersionUID = -5106772794510276705L;

	private Type type;
	
	public Connection(Type type) {
		this.type = type;
	}
	
	@Override
	public Type getPeerType() {
		return this.type;
	}

}
