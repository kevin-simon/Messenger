package ui.action;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;

public class ItemMouseOverAction implements MouseListener {

	public ItemMouseOverAction() {}

	@Override
	public void mouseClicked(MouseEvent event) {}
	public void mousePressed(MouseEvent event) {}
	public void mouseReleased(MouseEvent event) {}
	public void mouseEntered(MouseEvent event) {
		JButton button = (JButton) event.getSource();
		button.requestFocus();
		button.setBackground(Color.WHITE);
		button.setBorderPainted(true);
	}
	public void mouseExited(MouseEvent event) {
		JButton button = (JButton) event.getSource();
		button.setBackground(new Color(220, 220, 220));
		button.setBorderPainted(false);
	}

}
