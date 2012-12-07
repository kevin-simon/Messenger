package ui.action;

import java.awt.event.ActionEvent;

import ui.ChatPanel;
import ui.Window;

public class SendMessageAction extends Action {

	private ChatPanel chatPanel;
	private Window window;
	
	public SendMessageAction(Window window, ChatPanel chatPanel) {
		super(window);
		this.window = window;
		this.chatPanel = chatPanel;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (this.window.getStatus().isOnline()) {
			String message = this.chatPanel.getMessage();
			if (message.length() > 0) { 
				this.window.sendMessage(this.chatPanel.getFriendIdentity(), message);
			}
		}
		else {
			this.window.offlineWindow();
		}
	}

}
