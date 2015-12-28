package com.sciamlab.common.util;

import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONObject;

public interface Geocoder {

	public JSONObject resolve(String query) throws MalformedURLException, IOException;
	
	public JSONObject resolveAsPoint(String query) throws MalformedURLException, IOException;
}
