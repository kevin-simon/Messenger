package ui.action;

import java.awt.event.ActionEvent;

import javax.swing.JPanel;

import ui.Window;

public class ExitAction extends Action {

	public ExitAction(Window window) {
		super(window);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		JPanel mainPanel = this.window.mainPanel();
		this.window.initializeToolbar();
		mainPanel.removeAll();
		mainPanel.repaint();
		this.window.loadConnection();
	}

}
