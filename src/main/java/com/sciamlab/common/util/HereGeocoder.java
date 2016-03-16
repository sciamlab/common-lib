package com.sciamlab.common.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONObject;

public class HereGeocoder implements Geocoder{
	
	public final static String DEFAULT_URL = "https://geocoder.cit.api.here.com/6.2/geocode.json";
	public final static String DEFAULT_GEN = "9";

	private final URL url;
	private final String app_id;
	private final String app_code;
	private final String gen;
	
	public HereGeocoder(HereGeocoderBuilder builder) {
		this.url = builder.url;
		this.app_id = builder.app_id;
		this.app_code = builder.app_code;
		this.gen = builder.gen;
	}

	/**
	 * the method is synchronized since the parallel access to the service id forbidden
	 * @param query
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public synchronized JSONObject resolve(String query) throws MalformedURLException, IOException{
		String result = new HTTPClient().doGET(new URL(url+"?app_id="+app_id+"&app_code="+app_code+"&gen="+gen
				+ "&searchtext="+URLEncoder.encode(query,"UTF-8"))).readEntity(String.class);
		JSONArray array = new JSONObject(result).getJSONObject("Response").getJSONArray("View");
		return (array.length()>0)? array.getJSONObject(0).getJSONArray("Result").getJSONObject(0).getJSONObject("Location") : null;
	}
	
	public JSONObject resolveAsPoint(String query) throws MalformedURLException, IOException{
		JSONObject resolve = resolve(query);

		return (resolve!=null) ? new JSONObject().put("type", "Point").put("coordinates", new JSONArray()
				.put(resolve.getJSONObject("DisplayPosition").getDouble("Longitude"))
				.put(resolve.getJSONObject("DisplayPosition").getDouble("Latitude"))) : null;
	}

	
	public static class HereGeocoderBuilder{
		
		private URL url;
		private final String app_id;
		private final String app_code;
		private String gen;
		
		public static HereGeocoderBuilder init(String app_id, String app_code){
			return new HereGeocoderBuilder(app_id, app_code);
		}
		
		private HereGeocoderBuilder(String app_id, String app_code){
			try {
				this.url = new URL(DEFAULT_URL);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			this.gen = DEFAULT_GEN;
			this.app_id = app_id;
			this.app_code = app_code;
		}
		
		public HereGeocoderBuilder url(URL url){
			this.url = url;
			return this;
		}
		public HereGeocoderBuilder gen(String gen){
			this.gen = gen;
			return this;
		}
		
		public HereGeocoder build(){
			return new HereGeocoder(this);
		}
	}
}
