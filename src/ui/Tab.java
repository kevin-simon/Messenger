package ui;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;

import ui.action.CloseTabAction;

public class Tab extends JPanel {

	private static final long serialVersionUID = -2613782206058548076L;
	private ChatPanel chatPanel;
	private JLabel closeButton;
	private String pseudonyme;
	
	public Tab(String pseudonyme, ChatPanel chatPanel) {
		super();
		this.pseudonyme = pseudonyme;
		this.setBackground(new Color(220,220,220));
		this.chatPanel = chatPanel;
		this.add(new JLabel(pseudonyme));
		this.closeButton = new JLabel(ResourceManager.getImage("close-tab.png"));
		this.add(this.closeButton); 
	}
	
	public ChatPanel getChatPanel() {
		return this.chatPanel;
	}
	
	public String getText() {
		return this.pseudonyme;
	}
	
	public void initCloseButton(CloseTabAction action) {
		this.closeButton.addMouseListener(action);
	}
	
	public void giveFocus() {
		this.chatPanel.giveFocus();
	}
}
