package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class FriendsZone extends JPanel {

	private static final long serialVersionUID = -5440717821701838804L;

	private Window window;
	private JPanel friends;
	
	public FriendsZone(Window window) {
		super(new BorderLayout());
		this.setBackground(new Color(220,220,220));
		this.window = window;
		JPanel mainPanel = this.window.mainPanel();
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0;
		gbc.fill = GridBagConstraints.VERTICAL;
		this.initializeFriends();
	    mainPanel.add(this, BorderLayout.LINE_START);
	}
	
	public void initializeFriends() {
		this.friends = new JPanel();

		this.friends.setBackground(new Color(220,220,220));
		this.friends.setLayout(new BoxLayout(this.friends, BoxLayout.Y_AXIS));
		JScrollPane scroll = new JScrollPane(this.friends);
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(new Color(220,220,220));
		panel.add(scroll, BorderLayout.CENTER);
		scroll.setBorder(null);
		
		scroll.setPreferredSize(new Dimension(200, 300));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		this.add(scroll, BorderLayout.CENTER);
	}
	
	public void addFriend(String pseudonyme, FriendStatus status) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = this.friends.getComponentCount();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.LINE_START;
		JLabel friend = new JLabel(pseudonyme, JLabel.CENTER);
		if (status == FriendStatus.ONLINE) {
			friend.setIcon(ResourceManager.getImage("friend-online.png"));
			friend.setToolTipText(pseudonyme + " is online");
			friend.addMouseListener(new MouseListener() {
				
				@Override
				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void mousePressed(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void mouseExited(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void mouseEntered(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() == 2) {
						window.openConversation(((JLabel) e.getComponent()).getText());
					}
				}
			});
		}
		else if (status == FriendStatus.WAITING) {
			friend.setIcon(ResourceManager.getImage("inconnu.png"));
			friend.setToolTipText(pseudonyme + " wait your acceptation");
		}
		else {
			friend.setIcon(ResourceManager.getImage("friend-offline.png"));
			friend.setToolTipText(pseudonyme + " is offline");
		}
		friend.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		this.friends.add(friend);
	}
}
