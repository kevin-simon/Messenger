package utils;

import java.io.Serializable;
import java.net.InetAddress;

import rmi.Type;

public class BroadcastObject implements Serializable {

	private static final long serialVersionUID = 5713054423295043028L;
	
	public InetAddress source;
	public Type sourceType;

}
