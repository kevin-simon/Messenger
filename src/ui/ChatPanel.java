package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import ui.action.EnterAction;
import ui.action.SendMessageAction;
import datas.Identity;
import datas.Message;

public class ChatPanel extends JPanel {

	private static final long serialVersionUID = 1284487099266382368L;
	private Window window;
	private JTextField message;
	private JEditorPane chatRoom;
	private String previousUser;
	private Identity friend; 
	
	public ChatPanel(Window window, Identity friend) {
		super(new BorderLayout());
		this.friend = friend;
		this.chatRoom = new JEditorPane();
		this.previousUser = "";
		JPanel panel = new JPanel(new BorderLayout());
		JScrollPane scrollPane = new JScrollPane(this.chatRoom);
		scrollPane.setBorder(null);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setPreferredSize(new Dimension(200, 300));
		panel.add(scrollPane, BorderLayout.CENTER);
		this.window = window;
		this.setBackground(new Color(220,220,220));
		this.chatRoom.setBackground(Color.WHITE);
		this.chatRoom.setEditable(false);
		this.chatRoom.setBorder(BorderFactory.createLineBorder(new Color(200,200,200)));
		this.add(panel, BorderLayout.CENTER);
		JPanel sendText = new JPanel(new BorderLayout());
		this.message = new JTextField();
		sendText.add(this.message, BorderLayout.CENTER);
		JButton send = new JButton("Send");
		send.addActionListener(new SendMessageAction(this.window, this));
		Border outside = BorderFactory.createLineBorder(new Color(200,200,200));
		Border inside = BorderFactory.createEmptyBorder(3,6,3,6);
		Border in = BorderFactory.createCompoundBorder(outside, inside);
		Border out = BorderFactory.createMatteBorder(1,10,1,1,new Color(220,220,220));
		this.message.addKeyListener(new EnterAction(send));
		this.message.setBorder(BorderFactory.createLineBorder(new Color(200,200,200)));
		send.setBorder(BorderFactory.createCompoundBorder(out, in));
		sendText.add(send, BorderLayout.LINE_END);
		sendText.setBackground(new Color(220,220,220));
		sendText.setBorder(BorderFactory.createEmptyBorder(10, 5, 5, 5));
		this.add(sendText, BorderLayout.PAGE_END);
		this.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
	}
	
	public Identity getFriendIdentity() {
		return this.friend;
	}
	
	public String getMessage() {
		String message = this.message.getText();
		this.message.setText("");
		return message;
	}
	
	public void showMessage(Message message) {
		String texte = "";
		if (!this.previousUser.equals(message.getSender().getPseudonyme()) && !this.previousUser.equals("")) {
			texte += "--------------------\n";
		}
		texte += this.getDate() + " " + message.getSender().getPseudonyme() + "\n";
		texte += message.getMessage() + "\n";
		Document doc = this.chatRoom.getDocument();
		try {
			doc.insertString(doc.getLength(), texte, null);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		this.previousUser = message.getSender().getPseudonyme();
	}
	
	public String getDate() {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
        Date date = new Date();
        return "(" + dateFormat.format(date) + ")";
	}
	
	public void giveFocus() {
		this.message.requestFocusInWindow();
		this.message.requestFocus(true);
	}
	
}
