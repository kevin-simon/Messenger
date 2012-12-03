import ui.Window;

public class Messenger {
	
	private static Messenger instance;
	
	public static Messenger getInstance() {
		if (Messenger.instance == null) {
			Messenger.instance = new Messenger();
		}
		return Messenger.instance;
	}
	
	public Messenger() {
	}

	public static void main(String[] args) {
		new Window("Messenger");
	}

}
