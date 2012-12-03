import ui.Window;
import utils.Properties;
import rmi.Type;

public class Messenger {
	
	private static Messenger instance;
	private static Type peerType;
	
	public static Messenger getInstance() {
		if (Messenger.instance == null) {
			Messenger.instance = new Messenger();
		}
		return Messenger.instance;
	}
	
	public Messenger() {
	}

	public static void main(String[] args) {
		Messenger messenger = Messenger.getInstance();
		messenger.subscribe();
		new Window("Messenger");
	}
	
	public void subscribe() {
		System.out.println("http://" + Properties.APP.get("subscribe_ip") + ":5984/MessengerApplication/peers");
	}

}
