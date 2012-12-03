package ui.action;

import java.awt.event.ActionEvent;

import ui.Window;

public class AboutAction extends Action {

	public AboutAction(Window window) {
		super(window);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		this.window.aboutWindow();
	}

}
