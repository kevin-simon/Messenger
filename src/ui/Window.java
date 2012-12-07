package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observer;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.UIManager;

import ui.action.AboutAction;
import ui.action.WindowAction;
import ui.location.Location;
import utils.OWindow;
import datas.Identity;
import datas.Message;

public class Window extends JFrame {

	private static final long serialVersionUID = 7446192599263749847L;
	public static Color defaultColor;
	public OWindow observableObject;
	public Identity identity;
	
	private JPanel main;
	private ConnectionWindow connectionWindow;
	private FriendsZone friendsZone;
	private ConversationsZone conversationsZone;
	private ToolBar toolbar;
	private Status status;
	
	private int width;
	private int height;

	public Window(String windowsTitle) {
		super();
		this.observableObject = new OWindow();
		this.setWindowSize("800x600");
		this.setTitle(windowsTitle);
		ImageIcon icone = ResourceManager.getImage("icon.png");
		if (icone != null) {
			this.setIconImage(icone.getImage());
		}
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAction(this));
		this.setMinimumSize(new Dimension(this.width, this.height));
		this.main = new JPanel(new BorderLayout());
		this.setLayout(new BorderLayout());
		JPanel contentPane = new JPanel(new BorderLayout());
		this.setContentPane(contentPane);
		contentPane.add(this.main, BorderLayout.CENTER);
		this.toolbar = new ToolBar();
		this.initializeToolbar();
		this.setLocationRelativeTo(null);
		this.setSize(this.width, this.height);
		this.loadConnection();
	}
	
	public JPanel mainPanel() {
		return this.main;
	}
	
	public void loadConnection() {
	    this.connectionWindow = new ConnectionWindow(this);
	}
	
	public void loadMessenger(String pseudonyme) {
	    this.mainPanel().removeAll();
		JPanel mainPanel = this.mainPanel();
		mainPanel.setLayout(new BorderLayout());
		initializeFriendsZone();
		initializeConversationsZone();
		this.observableObject.setChanged();
		this.observableObject.notifyObservers(pseudonyme);
		this.setVisible(true);
	}
	
	public void initializeView(Identity identity) {
		this.identity = identity;
		this.status = new Status(identity.getIdentity());
		this.toolbar.add(this.status, 0);
		this.status.login();
		/*this.toolbar.addSeparator(1);
		this.toolbar.addItem(Location.get("loginItem"), ResourceManager.getImage("login.png"), new LoginAction(this), 2);
		this.toolbar.addItem(Location.get("logoutItem"), ResourceManager.getImage("logout.png"), new LogoutAction(this), 3);
		this.toolbar.addItem(Location.get("exitWindowItem"), ResourceManager.getImage("exit.png"), new ExitAction(this));*/
		this.setVisible(true);
	}
	
	public void initializeFriendsZone() {
		this.friendsZone = new FriendsZone(this);
	}
	
	public void initializeConversationsZone() {
	    UIManager.put("TabbedPane.contentAreaColor", new Color(220,220,220)); 
	    UIManager.put("TabbedPane.selected", new Color(220,220,220)); 
	    UIManager.put("TabbedPane.borderHightlightColor", new Color(200,200,200)); 
	    UIManager.put("TabbedPane.unselectedBackground", new Color(200,200,200)); 
	    UIManager.put("TabbedPane.darkShadow", new Color(220,220,220)); 
		this.conversationsZone = new ConversationsZone(this);
	}
	
	public void openConversation(Identity friend) {
		this.conversationsZone.addTab(friend);
	}
	
	public void initializeToolbar() {
		this.toolbar.removeAll();
		this.toolbar.repaint();
		this.getContentPane().add(this.toolbar, BorderLayout.PAGE_START);
		this.toolbar.add(Box.createHorizontalGlue());
		this.toolbar.addItem(Location.get("aboutWindowItem"), ResourceManager.getImage("about.png"), new AboutAction(this));
	}
	
	public Status getStatus() {
		return this.status;
	}
	
	private void setWindowSize(String size) {
		String[] dimensions = size.split("x");
		this.width = Integer.parseInt(dimensions[0]);
		this.height = Integer.parseInt(dimensions[1]);
	}
	
	public ConnectionWindow getConnectionWindow() {
		return this.connectionWindow;
	}
	
	public boolean closeWindow() {
		return JOptionPane.showConfirmDialog(this, Location.get("closeWindow"), "Messenger - " + Location.get("closeWindowTitle"), JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, ResourceManager.getImage("question.png")) == JOptionPane.YES_OPTION;
	}
	
	public void aboutWindow() {
		JTextArea textArea = new JTextArea(Location.get("aboutWindow") + Location.get("aboutUrlWindow") + "\n");
		textArea.setColumns(30);
		textArea.setLineWrap( true );
		textArea.setWrapStyleWord( true );
		textArea.setSize(textArea.getPreferredSize().width, 1);
		textArea.setBackground(defaultColor);
		textArea.setEnabled(false);
		textArea.setDisabledTextColor(Color.black);
		JOptionPane.showMessageDialog(this, textArea, "Messenger - " + Location.get("aboutWindowTitle"), JOptionPane.INFORMATION_MESSAGE, ResourceManager.getImage("about.png"));
	}
	
	public void offlineWindow() {
		JOptionPane.showMessageDialog(this, Location.get("offlineWindow"), "Messenger - " + Location.get("offlineWindowTitle"), JOptionPane.INFORMATION_MESSAGE, ResourceManager.getImage("about.png"));
	}

	public void addObserver(Observer observer) {
		this.observableObject.addObserver(observer);
	}

	public void updateIdentityList(ArrayList<Identity> identities) {
		this.friendsZone.removeAll();
		for (Identity identity : identities) {
			this.friendsZone.addFriend(identity, FriendStatus.ONLINE);
		}
		this.setVisible(true);
	}

	public void sendMessage(Identity friend, String message) {
		Message messageSending = new Message(this.identity, friend, message);
		this.conversationsZone.getTab(friend).getChatPanel().showMessage(messageSending);
		this.observableObject.setChanged();
		this.observableObject.notifyObservers(messageSending);
	}
	
	public void receiveMessage(Message message) {

		System.out.println("sender : " + message.getSender());
		System.out.println("receiver : " + message.getReceiver());
		System.out.println("message : " + message.getMessage());
		this.conversationsZone.getTab(message.getReceiver()).getChatPanel().showMessage(message);
	}
	
	public void closeApplication() {
		this.observableObject.setChanged();
		this.observableObject.notifyObservers(null);
	}
}
