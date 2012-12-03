package ui.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ui.Window;

public abstract class Action implements ActionListener {

	protected Window window;
	
	public Action(Window window) {
		this.window = window;
	}
	
	public abstract void actionPerformed(ActionEvent event);
}
