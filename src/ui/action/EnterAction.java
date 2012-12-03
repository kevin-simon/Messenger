package ui.action;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;

public class EnterAction implements KeyListener {

	private JButton button;
	
	public EnterAction(JButton button) {
		this.button = button;
	}

	@Override
	public void keyTyped(KeyEvent event) {}

	@Override
	public void keyPressed(KeyEvent event) {
		if (event.getKeyCode() == 10) {
			this.button.doClick();
		}
	}

	@Override
	public void keyReleased(KeyEvent event) {}

}
