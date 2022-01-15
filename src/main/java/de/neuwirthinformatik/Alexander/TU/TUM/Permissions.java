package de.neuwirthinformatik.Alexander.TU.TUM;

import java.nio.charset.Charset;
import java.security.*;

public class Permissions {
	
	public static boolean has(String key) {return has(TUM.settings,key);}
	public static boolean has(SettingsPanel sp,String key) {
		if (sp.IS_FULL_FEATURED) {
			return true;
		} 
		else {
			String m = MD5(key);
			for (String p : sp.permissions) {
				if (m.equals(p))
					return true;
			}
			return false;
		}
	}

	public static String MD5(String md5) {
		try {
			java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
			byte[] array = md.digest(md5.getBytes(Charset.forName("UTF-8")));
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < array.length; ++i) {
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
			}
			return sb.toString();
		} catch (java.security.NoSuchAlgorithmException e) {
		}
		return null;
	}
	

	
}
