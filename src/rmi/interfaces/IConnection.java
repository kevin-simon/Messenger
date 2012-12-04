package rmi.interfaces;

import java.io.Serializable;
import java.rmi.Remote;

import rmi.Type;

public interface IConnection extends Remote, Serializable {

	public Type getPeerType();
}
