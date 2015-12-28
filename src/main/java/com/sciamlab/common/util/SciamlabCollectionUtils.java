package com.sciamlab.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class SciamlabCollectionUtils {
	
	public static List<Object> asList(JSONArray array){
		List<Object> list = new ArrayList<Object>();
		if(array!=null)			
			for(int i=0 ; i<array.length() ; i++)
				list.add(array.get(i));
		return list;
	}
	
	public static List<String> asStringList(JSONArray array){
		List<String> list = new ArrayList<String>();
		if(array!=null)			
			for(int i=0 ; i<array.length() ; i++)
				list.add(array.getString(i));
		return list;
	}
	
	public static List<JSONObject> asJSONObjectList(JSONArray array){
		List<JSONObject> list = new ArrayList<JSONObject>();
		if(array!=null)			
			for(int i=0 ; i<array.length() ; i++)
				list.add(array.getJSONObject(i));
		return list;
	}
	
	public static Map<String, Object> asMap(JSONObject object){
		Map<String, Object> map = new HashMap<String, Object>();
		if(object!=null)			
			for(Object k : object.keySet())
				map.put((String)k, object.get((String)k));
		return map;
	}
	
	public static Map<String, String> asStringMap(JSONObject object){
		Map<String, String> map = new HashMap<String, String>();
		if(object!=null)			
			for(Object k : object.keySet())
				map.put((String)k, object.getString((String)k));
		return map;
	}
	
	public static Map<String, JSONObject> asJSONObjectMap(JSONObject object){
		Map<String, JSONObject> map = new HashMap<String, JSONObject>();
		if(object!=null)			
			for(Object k : object.keySet())
				map.put((String)k, object.getJSONObject((String)k));
		return map;
	}

}
