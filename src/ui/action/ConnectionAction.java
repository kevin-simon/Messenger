package ui.action;

import java.awt.event.ActionEvent;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ui.Window;

public class ConnectionAction extends Action {

	public ConnectionAction(Window window) {
		super(window);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		JComponent[] datas = this.window.getConnectionWindow().getConnectionDatas();
		JTextField pseudonyme = (JTextField) datas[0];
		JPanel mainPanel = this.window.mainPanel();
		mainPanel.removeAll();
		mainPanel.repaint();
		this.window.loadMessenger(pseudonyme.getText());
	}
	
}
