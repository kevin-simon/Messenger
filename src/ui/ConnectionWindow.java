package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.Border;

import ui.action.ConnectionAction;
import ui.action.EnterAction;
import ui.action.SubscribeAction;
import ui.location.Location;


public class ConnectionWindow extends JPanel {

	private static final long serialVersionUID = 3450851055884911988L;
	
	private Window window;
	private JTextField subscribePseudonyme;
	private JPasswordField subscribePassword;
	private JTextField connectionPseudonyme;
	private JPasswordField connectionPassword;

	public ConnectionWindow(Window window) {
	    super(new GridLayout(1, 2));
	    this.window = window;
	    JPanel mainPanel = this.window.mainPanel();
	    this.setBackground(new Color(240,240,240));
	    this.showSubscribeArea();

	    this.showConnectionArea();
	    Border outside = BorderFactory.createLineBorder(new Color(220,220,220), 100);
	    Border middle = BorderFactory.createLineBorder(new Color(170,170,170));
	    Border inside = BorderFactory.createLineBorder(new Color(85,85,85));
	    Border inter = BorderFactory.createCompoundBorder(middle, inside);
	    this.setBorder(BorderFactory.createCompoundBorder(outside, inter));
	    mainPanel.setLayout(new BorderLayout());
	    mainPanel.removeAll();
	    mainPanel.add(this, BorderLayout.CENTER);
	    this.window.setVisible(true);
	}
	
	private void showSubscribeArea() {
		GridBagLayout layout = new GridBagLayout();
		JPanel subscribeArea = new JPanel(layout);
		Border inside = BorderFactory.createMatteBorder(0,0,0,1,new Color(170,170,170));
	    Border outside = BorderFactory.createEmptyBorder(30,30,30,0);
	    subscribeArea.setBorder(BorderFactory.createCompoundBorder(outside, inside));
	    GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx = 0;
		gbc.insets = new Insets(0, 10, 40, 40);
	    subscribeArea.add(new JLabel(Location.get("subscribeTitle"), ResourceManager.getImage("subscribe.png"), JLabel.CENTER), gbc);
	    gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.weightx = 0;
		gbc.insets = new Insets(5, 0, 5, 10);
		gbc.anchor = GridBagConstraints.LINE_START;
	    subscribeArea.add(new JLabel(Location.get("pseudonymeLabel")), gbc);
	    gbc = new GridBagConstraints();
	    gbc.gridx = 1;
	    gbc.gridy = 1;
	    gbc.weightx = 1;
	    gbc.gridwidth = GridBagConstraints.REMAINDER;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    gbc.insets = new Insets(0, 0, 0, 30);
	    this.subscribePseudonyme = new JTextField();
	    subscribeArea.add(this.subscribePseudonyme, gbc);
	    gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 1;
		gbc.weightx = 0;
		gbc.insets = new Insets(5, 0, 5, 10);
		gbc.anchor = GridBagConstraints.LINE_START;
	    subscribeArea.add(new JLabel(Location.get("passwordLabel")), gbc);
	    gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 3;
		gbc.gridwidth = 2;
		gbc.weightx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(0, 0, 0, 30);
		this.subscribePassword = new JPasswordField();
	    subscribeArea.add(this.subscribePassword, gbc);
	    gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(40, 10, 10, 30);
		JButton subscribeButton = new JButton(Location.get("subscribeButton"));
		subscribeButton.addActionListener(new SubscribeAction(this.window));
	    subscribeArea.add(subscribeButton, gbc);
		this.subscribePassword.addKeyListener(new EnterAction(subscribeButton));
	    this.add(subscribeArea);
	}
	
	private void showConnectionArea() {
		GridBagLayout layout = new GridBagLayout();
		JPanel connectionArea = new JPanel(layout);
		Border inside = BorderFactory.createMatteBorder(0,1,0,0,new Color(170,170,170));
	    Border outside = BorderFactory.createEmptyBorder(30,0,30,30);
	    connectionArea.setBorder(BorderFactory.createCompoundBorder(outside, inside));
	    GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx = 0;
		gbc.insets = new Insets(0, 40, 40, 10);
	    connectionArea.add(new JLabel(Location.get("connectionTitle"), ResourceManager.getImage("connection.png"), JLabel.CENTER), gbc);
	    gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.weightx = 0;
		gbc.insets = new Insets(5, 30, 5, 10);
		gbc.anchor = GridBagConstraints.LINE_START;
	    connectionArea.add(new JLabel(Location.get("pseudonymeLabel")), gbc);
	    gbc = new GridBagConstraints();
	    gbc.gridx = 1;
	    gbc.gridy = 1;
	    gbc.weightx = 1;
	    gbc.gridwidth = GridBagConstraints.REMAINDER;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    gbc.insets = new Insets(0, 0, 0, 0);
	    this.connectionPseudonyme = new JTextField();
	    connectionArea.add(this.connectionPseudonyme, gbc);
	    gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 1;
		gbc.weightx = 0;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.insets = new Insets(5, 30, 5, 10);
	    connectionArea.add(new JLabel(Location.get("passwordLabel")), gbc);
	    gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 3;
		gbc.gridwidth = 2;
		gbc.weightx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(0, 0, 0, 0);
		this.connectionPassword = new JPasswordField();
	    connectionArea.add(this.connectionPassword, gbc);
	    gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(40, 40, 10, 10);
		JButton connectionButton = new JButton(Location.get("connectionButton"));
		connectionButton.addActionListener(new ConnectionAction(this.window));
	    connectionArea.add(connectionButton, gbc);
		this.connectionPassword.addKeyListener(new EnterAction(connectionButton));
		this.add(connectionArea);
	}
	
	public JComponent[] getSubscribeDatas() {
		JComponent[] datas = new JComponent[2];
		datas[0] = this.subscribePseudonyme;
		datas[1] = this.subscribePassword;
		return datas;
	}
	
	public JComponent[] getConnectionDatas() {
		JComponent[] datas = new JComponent[2];
		datas[0] = this.connectionPseudonyme;
		datas[1] = this.connectionPassword;
		return datas;
	}

}
