package ui.action;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import ui.Window;

public class WindowAction implements WindowListener {

	private Window window;
	
	public WindowAction(Window window) {
		this.window = window;
	}

	@Override
	public void windowClosing(WindowEvent event) {
		if (this.window.closeWindow()) {
			this.window.closeApplication();
		}
	}

	@Override
	public void windowOpened(WindowEvent event) {}
	public void windowClosed(WindowEvent event) {}
	public void windowIconified(WindowEvent event) {}
	public void windowDeiconified(WindowEvent event) {}
	public void windowActivated(WindowEvent event) {}
	public void windowDeactivated(WindowEvent event) {}

}
