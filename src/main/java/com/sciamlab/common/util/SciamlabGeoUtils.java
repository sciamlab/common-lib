package com.sciamlab.common.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class SciamlabGeoUtils {
	
	public static JSONObject mergePolygonIntoMultiPolygon(List<JSONObject> geometries){
		JSONObject out = new JSONObject();
		out.put("type", "MultiPolygon");
		out.put("coordinates", new JSONArray());
		JSONArray multi = new JSONArray();
		for(JSONObject g : geometries){
			JSONArray coord = g.getJSONArray("coordinates").getJSONArray(0);
			if("Polygon".equals(g.getString("type"))){
				multi.put(coord);
			}else if("MultiPolygon".equals(g.getString("type"))){
				for(Object pol : SciamlabCollectionUtils.asList(coord)){
					multi.put((JSONArray)pol);
				}
			}
		}
		out.getJSONArray("coordinates").put(multi);

		return out;
	}
	
	public static JSONObject mergePolygonIntoMultiPolygon(final JSONObject g1, final JSONObject g2){
		return mergePolygonIntoMultiPolygon(new ArrayList<JSONObject>(){{ add(g1); add(g2); }});
	}
	
	public static void main(String[] args) {
		final JSONObject p1 =  new JSONObject("{ \"type\": \"Polygon\", \"coordinates\": [ [ [ 14.0625, 43.83452678223684 ], [ 14.0625, 58.81374171570782 ], [ 47.8125, 58.81374171570782 ], [ 47.8125, 43.83452678223684 ], [ 14.0625, 43.83452678223684 ] ] ] }");
		final JSONObject p2 =  new JSONObject("{ \"type\": \"Polygon\", \"coordinates\": [ [ [ 68.5546875, 39.90973623453719 ], [ 68.5546875, 58.81374171570782 ], [ 101.6015625, 58.81374171570782 ], [ 101.6015625, 39.90973623453719 ], [ 68.5546875, 39.90973623453719 ] ] ] }");
		final JSONObject m1 = mergePolygonIntoMultiPolygon(new ArrayList<JSONObject>(){{ add(p1); add(p2); }});
		System.out.println(m1.toString(2));
		final JSONObject p3 =  new JSONObject("{ \"type\": \"Polygon\", \"coordinates\": [ [ [ 111.796875, 56.072035471800866 ], [ 111.796875, 70.19999407534661 ], [ 160.3125, 70.19999407534661 ], [ 160.3125, 56.072035471800866 ], [ 111.796875, 56.072035471800866 ] ] ] }");
		JSONObject m2 = mergePolygonIntoMultiPolygon(new ArrayList<JSONObject>(){{ add(m1); add(p3); }});
		System.out.println(m2.toString(2));
		
	}
	
//	public static void main(String[] args) throws MalformedURLException, IOException {
//		System.out.println(NominatimGeocoder.resolve("Viale di Trastevere, Roma").toString(2));
//	}

	public static class NominatimGeocoder{
		/**
		 * the method is synchronized since the parallel access to the service id forbidden
		 * @param query
		 * @return
		 * @throws MalformedURLException
		 * @throws IOException
		 */
		public static synchronized JSONObject resolve(String query) throws MalformedURLException, IOException{
			String result = new HTTPClient().doGET(
					new URL("http://nominatim.openstreetmap.org/search.php?format=json&countrycodes=it&q="+URLEncoder.encode(query,"UTF-8"))).readEntity(String.class);
			JSONArray array = new JSONArray(result);
			return (array.length()>0)? (JSONObject)array.get(0) : null;
		}
		
		public static JSONObject resolveAsPoint(String query) throws MalformedURLException, IOException{
			JSONObject resolve = resolve(query);
			return (resolve!=null) ? new JSONObject().put("type", "Point").put("coordinates", new JSONArray().put(new Double(resolve.getString("lon"))).put(new Double(resolve.getString("lat")))) : null;
		}
	}
}
	
