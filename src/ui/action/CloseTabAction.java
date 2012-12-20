package ui.action;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import ui.ConversationsZone;
import ui.Tab;

public class CloseTabAction implements MouseListener {

	private Tab tab;
	private ConversationsZone conversationsZone;
	
	public CloseTabAction(ConversationsZone conversationsZone, Tab tab) {
		this.conversationsZone = conversationsZone;
		this.tab = tab;
	}
	
	@Override
	public void mouseClicked(MouseEvent event) {
		this.conversationsZone.removeTab(this.tab);
	}
	public void mousePressed(MouseEvent event) {}
	public void mouseReleased(MouseEvent event) {}
	public void mouseEntered(MouseEvent event) {}
	public void mouseExited(MouseEvent event) {}

}
