package com.sciamlab.common.util;

import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

public class SciamlabLODUtils {

	private static HTTPClient http = new HTTPClient();

	public static JSONObject getDBpediaResource(URL uri) throws MalformedURLException, JSONException  {
		String response = http.doGET(new URL("http://it.dbpedia.org/sparql?default-graph-uri=http%3A%2F%2Fit.dbpedia.org&query=DESCRIBE+%3C"
				+ uri + "%3E&format=application%2Fjson")).readEntity(String.class);
		return new JSONObject(response).getJSONObject(uri.toString());
	}

}
