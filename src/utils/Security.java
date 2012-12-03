package utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

public class Security {

	public static String encrypt(char[] notSecuredPassword) {
		String password = new String(notSecuredPassword);
		try {
			byte[] bytesToBeEncrypted = password.getBytes("UTF-8");  
			MessageDigest md = MessageDigest.getInstance("MD5");  
			byte[] theDigest = md.digest(bytesToBeEncrypted); 
			Formatter formatter = new Formatter();  
			for (byte b : theDigest) {  
				formatter.format("%02x", b);  
			}  	
			password = formatter.toString().toLowerCase(); 
			formatter.close();
		} catch (UnsupportedEncodingException e) {  
			e.printStackTrace();  
		} catch (NoSuchAlgorithmException e) {  
			e.printStackTrace();  
		}  
		return password;
	}

}
