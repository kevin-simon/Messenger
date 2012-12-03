package ui.action;

import java.awt.event.ActionEvent;

import ui.Window;

public class LogoutAction extends Action {

	public LogoutAction(Window window) {
		super(window);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		this.window.getStatus().logout();
	}

}
