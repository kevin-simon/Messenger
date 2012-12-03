package ui;

import java.net.URL;

import javax.swing.ImageIcon;

public class ResourceManager {
	
	private static ResourceManager instance;
	
	public static ResourceManager getInstance() {
		if (ResourceManager.instance == null) {
			ResourceManager.instance = new ResourceManager();
		}
		return ResourceManager.instance;
	}
	
	public ResourceManager() {}
	
	public static ImageIcon getImage(String imageName) {
		ImageIcon icone = null;
		String path = "/resources/images/" + imageName;
		URL imageUrl = ResourceManager.class.getResource(path);
		if (imageUrl == null) {
			System.err.println(imageName + " not found");
		}
		else {
			icone = new ImageIcon(imageUrl);
		}
		return icone;
	}
	
	public static String getLocationFile(String language) {
		return ResourceManager.class.getResource("/resources/locations/" + language + ".txt").getPath();
	}
	
	public static String getPropertiesFile(String properties) {
		return ResourceManager.class.getResource("/resources/properties/" + properties + ".conf").getPath();
	}
}
