import java.util.Observable;
import java.util.Observer;

import org.lightcouch.CouchDbClient;

import rmi.Type;
import utils.BroadcastObject;
import utils.Discover;
import utils.Properties;

import com.google.gson.JsonObject;

public class Messenger implements Observer {
	
	private static Messenger instance;
	private Type peerType;
	
	public static Messenger getInstance() {
		if (Messenger.instance == null) {
			Messenger.instance = new Messenger();
		}
		return Messenger.instance;
	}
	
	public Messenger() {
		this.peerType = Type.SUPER_PEER;
	}

	public static void main(String[] args) throws Exception {
		Messenger messenger = Messenger.getInstance();
		//messenger.subscribe();
		//new Window("Messenger");*/
		Discover discover = new Discover();
		discover.addObserver(messenger);
		discover.start();
		discover.sendPacket(messenger.getPeerType());
	}
	
	public Type getPeerType() {
		return this.peerType;
	}
	
	public void subscribe() {
		CouchDbClient dbClient = new CouchDbClient("messenger_application", true, "http", Properties.APP.get("subscribe_ip"), 5984, null, null);
		/*Map<String, Object> map = new HashMap<String, Object>();
		map.put("_id", "peers");
		map.put("192.168.1.8", this.peerType.toString());
		dbClient.update(map);*/
		JsonObject json = dbClient.find(JsonObject.class, "peers");
		System.out.println(json);
		
	}

	@Override
	public void update(Observable o, Object object) {
		if (o instanceof Discover && object instanceof BroadcastObject) {
			BroadcastObject broadcastObject = (BroadcastObject) object;
			if (broadcastObject.sourceType == Type.PEER && this.peerType == Type.SUPER_PEER) {
				
			}
			System.out.println(object);
		}
	}

}
