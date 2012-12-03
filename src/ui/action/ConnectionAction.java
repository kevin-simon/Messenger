package ui.action;

import java.awt.event.ActionEvent;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import ui.Window;
import utils.Security;

public class ConnectionAction extends Action {

	public ConnectionAction(Window window) {
		super(window);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		JComponent[] datas = this.window.getConnectionWindow().getConnectionDatas();
		JTextField pseudonyme = (JTextField) datas[0];
		JPasswordField password = (JPasswordField) datas[1];
		if (accepteConnection(pseudonyme.getText(), Security.encrypt(password.getPassword()))) {
			JPanel mainPanel = this.window.mainPanel();
			mainPanel.removeAll();
			mainPanel.repaint();
			this.window.loadMessenger(pseudonyme.getText());
		}
	}
	
	public boolean accepteConnection(String pseudonyme, String password) {
		return true;
	}

}
