package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.util.HashMap;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import ui.action.CloseTabAction;

public class ConversationsZone extends JTabbedPane {
	
	private static final long serialVersionUID = 5835671053875226860L;
	private Window window;
	private HashMap<String, Tab> tabList;

	public ConversationsZone(Window window) {
		super();
		this.tabList = new HashMap<String, Tab>();
		this.window = window;
		this.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		JPanel mainPanel = this.window.mainPanel();
		this.setBackground(new Color(220,220,220));
		mainPanel.setBackground(new Color(220,220,220));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.BOTH;
	    mainPanel.add(this, BorderLayout.CENTER);
	}

	public void addTab(String pseudonyme) {
		Tab tab = null;
		if (!this.tabList.containsKey(pseudonyme)) {
			tab = new Tab(pseudonyme, new ChatPanel(this.window));
			super.addTab(tab.getText(), tab.getChatPanel());
			this.setTabComponentAt(this.tabList.size(), tab);
			tab.initCloseButton(new CloseTabAction(this, tab));
			this.tabList.put(pseudonyme, tab);
		}
		else {
			tab = this.tabList.get(pseudonyme);
		}
		this.setSelectedIndex(this.indexOfTabComponent(tab));
		this.window.setVisible(true);
		tab.giveFocus();
	}
	
	public void removeTab(String pseudonyme) {
		Tab tab = this.tabList.get(pseudonyme);
		this.removeTabAt(this.indexOfTabComponent(tab));
		this.tabList.remove(pseudonyme);
	}
	
}
