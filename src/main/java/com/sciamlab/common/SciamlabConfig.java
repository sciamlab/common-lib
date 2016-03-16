package com.sciamlab.common;

import java.util.Properties;

public class SciamlabConfig {
	
	protected static Properties PROPERTIES = new Properties();

	public static String getProperty(String key, String defaultValue){
		return PROPERTIES.getProperty(key, defaultValue);
	}
	public static String getProperty(String key){
		return PROPERTIES.getProperty(key);
	}
	public static Properties setProperty(String key, String value){
		PROPERTIES.setProperty(key, value);
		return PROPERTIES;
	}
	
}
