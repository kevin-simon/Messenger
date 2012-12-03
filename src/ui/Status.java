package ui;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import ui.location.Location;

public class Status extends JLabel {

	private static final long serialVersionUID = 3205526723046939511L;
	
	private boolean online;

	public Status(String pseudonyme) {
		super();
		this.setText(pseudonyme);
		this.setIcon(ResourceManager.getImage("offline.png"));
		this.setHorizontalAlignment(SwingConstants.CENTER);
		this.setHorizontalTextPosition(SwingConstants.RIGHT);
		this.setVerticalAlignment(SwingConstants.CENTER);
		this.setVerticalTextPosition(SwingConstants.CENTER);
		this.setBorder(BorderFactory.createEmptyBorder(0,5,0,10));
		this.setToolTipText(Location.get("offline"));
		this.online = false;
	}
	
	public void login() {
		this.setIcon(ResourceManager.getImage("online.png"));
		this.setToolTipText(Location.get("online"));
		this.online = true;
		this.repaint();
	}
	
	public void logout() {
		this.setIcon(ResourceManager.getImage("offline.png"));
		this.setToolTipText(Location.get("offline"));
		this.online = false;
		this.repaint();
	}
	
	public boolean isOnline() {
		return this.online;
	}
}
