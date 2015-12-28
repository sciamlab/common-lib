package com.sciamlab.common.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONObject;

public class NominatimGeocoder implements Geocoder{
	
	public final static String DEFAULT_URL = "http://nominatim.openstreetmap.org/search.php";
	public final static String DEFAULT_COUNTRYCODE = "it";
	public final static String DEFAULT_FORMAT = "json";

	private final URL url;
	private final String format;
	private final Collection<String> countrycodes;
	
	public NominatimGeocoder(NominatimGeocoderBuilder builder) {
		this.url = builder.url;
		this.format = builder.format;
		this.countrycodes = builder.countrycodes;
	}

	/**
	 * the method is synchronized since the parallel access to the service id forbidden
	 * @param query
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public synchronized JSONObject resolve(String query) throws MalformedURLException, IOException{
		String result = new HTTPClient().doGET(new URL(url+"?format="+format+"&countrycodes="+countrycodes.toString().replace("[", "").replace("]", "").replaceAll(" ", "")
				+"&q="+URLEncoder.encode(query,"UTF-8"))).readEntity(String.class);
		JSONArray array = new JSONArray(result);
		return (array.length()>0)? (JSONObject)array.get(0) : null;
	}
	
	public JSONObject resolveAsPoint(String query) throws MalformedURLException, IOException{
		JSONObject resolve = resolve(query);
		return (resolve!=null) ? new JSONObject().put("type", "Point").put("coordinates", new JSONArray().put(new Double(resolve.getString("lon"))).put(new Double(resolve.getString("lat")))) : null;
	}

	
	public static class NominatimGeocoderBuilder{
		
		private final URL url;
		private final String format;
		private final Collection<String> countrycodes;
		
		public static NominatimGeocoderBuilder init(URL url, final String countrycode, String format){
			return new NominatimGeocoderBuilder(url, new ArrayList<String>(){{add(countrycode);}}, format);
		}
		public static NominatimGeocoderBuilder init(URL url, Collection<String> countrycodes, String format){
			return new NominatimGeocoderBuilder(url, countrycodes, format);
		}
		public static NominatimGeocoderBuilder init(){
			try {
				return new NominatimGeocoderBuilder(new URL(DEFAULT_URL), new ArrayList<String>(){{add(DEFAULT_COUNTRYCODE);}}, DEFAULT_FORMAT);
			} catch (MalformedURLException e) {
				e.printStackTrace();
				return null;
			}
		}
		
		public NominatimGeocoderBuilder (URL url, Collection<String> countrycodes, String format){
			this.url = url;
			this.format = format;
			this.countrycodes = countrycodes;
		}
		
		public NominatimGeocoder build(){
			return new NominatimGeocoder(this);
		}
	}
}
