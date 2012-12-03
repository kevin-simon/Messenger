package ui.action;

import java.awt.event.ActionEvent;

import javax.swing.JComponent;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import ui.Window;
import utils.Security;

public class SubscribeAction extends Action {

	public SubscribeAction(Window window) {
		super(window);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		JComponent[] datas = this.window.getConnectionWindow().getSubscribeDatas();
		JTextField pseudonyme = (JTextField) datas[0];
		JPasswordField password = (JPasswordField) datas[1];
		System.out.println(pseudonyme.getText());
		System.out.println(Security.encrypt(password.getPassword()));
	}

}
