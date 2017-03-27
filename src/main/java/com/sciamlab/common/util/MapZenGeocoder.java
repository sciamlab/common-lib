package com.sciamlab.common.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONObject;

public class MapZenGeocoder implements Geocoder{
	
	private static final long serialVersionUID = 6177504724524079226L;
	
	public final static String DEFAULT_URL = "http://search.mapzen.com/v1/search";

	private final URL url;
	private final String api_key;
	
	public MapZenGeocoder(MapZenGeocoderBuilder builder) {
		this.url = builder.url;
		this.api_key = builder.api_key;
	}

	/**
	 * the method is synchronized since the parallel access to the service id forbidden
	 * @param query
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public synchronized JSONObject resolve(String query) throws MalformedURLException, IOException{
		String result = new HTTPClient().doGET(new URL(url+"?text="+URLEncoder.encode(query,"UTF-8")+ (api_key!=null ? "&api_key="+api_key : "") )).readEntity(String.class);
		JSONObject json = new JSONObject(result);
		return (json.getJSONArray("features").length()>0)? json.getJSONArray("features").getJSONObject(0) : null;
	}
	
	public JSONObject resolveAsPoint(String query) throws MalformedURLException, IOException{
		JSONObject resolve = resolve(query);
		return (resolve!=null) ? resolve.getJSONObject("geometry") : null;
	}

	
	public static class MapZenGeocoderBuilder{
		
		private final URL url;
		private String api_key;
		
		public static MapZenGeocoderBuilder init(URL url){
			return new MapZenGeocoderBuilder(url);
		}
		public static MapZenGeocoderBuilder init(){
			try {
				return new MapZenGeocoderBuilder(new URL(DEFAULT_URL));
			} catch (MalformedURLException e) {
				e.printStackTrace();
				return null;
			}
		}
		
		private MapZenGeocoderBuilder (URL url){
			this.url = url;
		}
		
		public MapZenGeocoderBuilder api_key(String api_key){
			this.api_key = api_key;
			return this;
		}
		
		public MapZenGeocoder build(){
			return new MapZenGeocoder(this);
		}
	}
}
