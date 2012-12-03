package ui.action;

import java.awt.event.ActionEvent;

import ui.Window;

public class LoginAction extends Action {

	public LoginAction(Window window) {
		super(window);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		this.window.getStatus().login();
	}

}
