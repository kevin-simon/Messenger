package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.util.HashMap;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import ui.action.CloseTabAction;
import datas.Identity;

public class ConversationsZone extends JTabbedPane {
	
	private static final long serialVersionUID = 5835671053875226860L;
	private Window window;
	private HashMap<Identity, Tab> tabList;

	public ConversationsZone(Window window) {
		super();
		this.tabList = new HashMap<Identity, Tab>();
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
	
	public Tab getTab(Identity friendIdentity) {
		if (!this.tabList.containsKey(friendIdentity)) {
			for (Identity identity : this.tabList.keySet()) {
				if (identity.getIdentity().equals(friendIdentity.getIdentity())) {
					return this.tabList.get(identity);
				}
			}
			this.addTab(friendIdentity);
		}
		return this.tabList.get(friendIdentity);
	}

	public void addTab(Identity friend) {
		Tab tab = null;
		for (Identity identity : this.tabList.keySet()) {
			if (identity.getIdentity().equals(friend.getIdentity())) {
				friend = identity;
			}
		}
		if (!this.tabList.containsKey(friend)) {
			tab = new Tab(friend, new ChatPanel(this.window, friend));
			super.addTab(tab.getIdentity().getIdentity(), tab.getChatPanel());
			this.setTabComponentAt(this.tabList.size(), tab);
			tab.initCloseButton(new CloseTabAction(this, tab));
			this.tabList.put(friend, tab);
		}
		else {
			tab = this.tabList.get(friend);
		}
		this.setSelectedIndex(this.indexOfTabComponent(tab));
		this.window.setVisible(true);
		tab.giveFocus();
	}
	
	public void removeTab(Tab tab) {
		this.removeTabAt(this.indexOfTabComponent(tab));
		this.tabList.remove(tab.getIdentity());
	}
	
}
