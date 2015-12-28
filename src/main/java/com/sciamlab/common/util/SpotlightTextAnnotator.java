package com.sciamlab.common.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;

import javax.ws.rs.core.MediaType;

import org.json.JSONArray;
import org.json.JSONObject;

public class SpotlightTextAnnotator implements TextAnnotator{

	public final static String DEFAULT_URL = "http://spotlight.sztaki.hu:2230/rest/annotate";
	public final static String DEFAULT_CONFIDENCE = "0.2";
	public final static String DEFAULT_FORMAT = "application/json";
	
	private final URL url;
	private final MediaType format;
	private final Double confidence;
	
	public SpotlightTextAnnotator(SpotlightTextAnnotatorBuilder builder) {
		this.url = builder.url;
		this.format = builder.format;
		this.confidence = builder.confidence;
	}

	public Collection<JSONObject> annotate(String text) throws MalformedURLException, IOException{
		String result = new HTTPClient().doGET(new URL(this.url.toString()+"?text="+URLEncoder.encode(text,"UTF-8")+"&confidence="+confidence), format).readEntity(String.class);
		JSONObject json = new JSONObject(result);
		Collection<JSONObject> concepts = new ArrayList<JSONObject>();
		if(json.has("Resources")){
			JSONArray resources = json.getJSONArray("Resources");
			for(int i=0 ; i<resources.length() ; i++)
				concepts.add(resources.getJSONObject(i));
		}
		return concepts;
	}
	
	public static class SpotlightTextAnnotatorBuilder{
		
		private final URL url;
		private final MediaType format;
		private final Double confidence;
		
		public static SpotlightTextAnnotatorBuilder init(URL url, Double confidence, MediaType format){
			return new SpotlightTextAnnotatorBuilder(url, confidence, format);
		}
		public static SpotlightTextAnnotatorBuilder init(){
			try {
				return new SpotlightTextAnnotatorBuilder(new URL(DEFAULT_URL), new Double(DEFAULT_CONFIDENCE), MediaType.valueOf(DEFAULT_FORMAT));
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		
		public SpotlightTextAnnotatorBuilder (URL url, Double confidence, MediaType format){
			this.url = url;
			this.format = format;
			this.confidence = confidence;
		}
		
		public SpotlightTextAnnotator build(){
			return new SpotlightTextAnnotator(this);
		}
	}
}
