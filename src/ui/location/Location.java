package ui.location;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import utils.Properties;

import ui.ResourceManager;

public class Location extends java.util.Properties {

	private static final long serialVersionUID = -719160265953187101L;
	private static Location location = new Location(Properties.UI.get("language"));
	
	private Location(String language) {
		super();
        FileInputStream fis;
		try {
			fis = new FileInputStream(ResourceManager.getLocationFile(language));
	        this.load(fis);    
	        fis.close();
		} catch (FileNotFoundException exception) {
			exception.printStackTrace();
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
	
	public static String get(String id) {
		String result = "Unknown data";
		if (Location.location.containsKey(id)) {
			result = Location.location.getProperty(id);
		}
		return result;
	}
}
