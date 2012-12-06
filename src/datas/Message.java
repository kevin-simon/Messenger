package datas;

import java.io.Serializable;

public class Message implements Serializable {

	private static final long serialVersionUID = -6750724016721019358L;

	private Identity receiver;
	private Identity sender;
	private String message;
	
	public Message(Identity sender, Identity receiver, String message) {
		this.receiver = receiver;
		this.sender = sender;
		this.message = message;
	}
	
	public Identity getSender() {
		return this.sender;
	}
	
	public Identity getReceiver() {
		return this.receiver;
	}
	
	public String getMessage() {
		return this.message;
	}
	
}
