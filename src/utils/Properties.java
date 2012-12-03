package utils;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import ui.ResourceManager;


public class Properties extends java.util.Properties {
	
	private static final long serialVersionUID = -7870781872648363984L;
	public static Properties UI = new Properties("ui");
	
	public Properties(String properties) {
		super();
        FileInputStream fis;
		try {
			fis = new FileInputStream(ResourceManager.getPropertiesFile(properties));
	        this.load(fis);    
	        fis.close();
		} catch (FileNotFoundException exception) {
			exception.printStackTrace();
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
	
	public String get(String id) {
		String result = "Unknown data";
		if (this.containsKey(id)) {
			result = this.getProperty(id);
		}
		return result;
	}
}
