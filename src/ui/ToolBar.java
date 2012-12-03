package ui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import ui.action.Action;
import ui.action.ItemMouseOverAction;

public class ToolBar extends JToolBar {

	private static final long serialVersionUID = -350011054358769608L;
	
	public ToolBar() {
		this.setFloatable(false);
		this.setBackground(new Color(220,220,220));
		this.setBorder(BorderFactory.createMatteBorder(0,0,1,0, Color.LIGHT_GRAY));
	}
	
	public JComponent addItem(String text, ImageIcon icon, Action action) {
		return this.addItem(text, icon, action, -1);
	}

	public JComponent addItem(String text, ImageIcon icon, Action action, int position) {
		JComponent item = null;
		if (action == null) {
			JLabel label = new JLabel(icon);
			label.setToolTipText(text);
			label.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
			item = label;
		}
		else {
			JButton button = new JButton(icon);
			button.setToolTipText(text);
			button.addActionListener(action);
			button.setBorderPainted(false);
			Border outside = BorderFactory.createLineBorder(Color.LIGHT_GRAY);
			Border inside = BorderFactory.createEmptyBorder(2,2,2,2);
			Border in = BorderFactory.createCompoundBorder(outside, inside);
			Border out = BorderFactory.createLineBorder(new Color(220,220,220),3);
			button.setBorder(BorderFactory.createCompoundBorder(out, in));
			button.setFocusPainted(false);
			button.addMouseListener(new ItemMouseOverAction());
			button.setHorizontalAlignment(SwingConstants.CENTER);
			button.setHorizontalTextPosition(SwingConstants.CENTER);
			button.setVerticalAlignment(SwingConstants.CENTER);
			button.setVerticalTextPosition(SwingConstants.BOTTOM);
			item = button;
		}
		item.setBackground(new Color(220,220,220));
		if (position == -1) {
			this.add(item);
		}
		else {
			this.add(item, position);
		}
		return item;
	}
	
	public void addSeparator(int position) {
		Separator separator = new Separator();
		separator.setSeparatorSize(new Dimension(3,24));
		separator.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		if (position == -1) {
			this.add(separator);
		}
		else {
			this.add(separator, position);
		}
	}
	
	public void addSeparator() {
		this.addSeparator(-1);
	}
}
