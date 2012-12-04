package utils;

import java.net.InetAddress;

public class BroadcastAddress {
	
	private int[] address;

	public BroadcastAddress(InetAddress address) {
		this.address = new int[4];
		this.address[0] = (address.getAddress()[0] < 0) ? address.getAddress()[0] + 256 : address.getAddress()[0];
		this.address[1] = (address.getAddress()[1] < 0) ? address.getAddress()[1] + 256 : address.getAddress()[1];
		this.address[2] = (address.getAddress()[2] < 0) ? address.getAddress()[2] + 256 : address.getAddress()[2];
		this.address[3] = 255;
	}
	
	public String toString() {
		return address[0] + "." + address[1] + "." + address[2] + "." + address[3];
	}
}
