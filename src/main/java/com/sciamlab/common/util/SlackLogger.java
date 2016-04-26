package com.sciamlab.common.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONObject;

public class SlackLogger{
	
	private final URL url;
	private final String channel;
	private final String username;
	
	private static final String RED = "#D00000";
	private static final String GREEN = "#36a64f";
	private static final String YELLOW = "#FFC906";
	private static final String BLUE = "#06B9FF";

	public static final String DANGER = RED;
	public static final String SUCCESS = GREEN;
	public static final String WARNING = YELLOW;
	public static final String INFO = BLUE;
	
	private HTTPClient http = new HTTPClient();
	
	public SlackLogger(SlackLoggerBuilder builder) {
		this.url = builder.url;
		this.channel = builder.channel;
		this.username = builder.username;
	}
	
	public String log(String text){
		return this.log(text, null, null, null);
	}
	
	private String log(final String text, String color){
		return this.log(null, text, color, new ArrayList<Map<String, Object>>(){{add(new HashMap<String, Object>(){{put("title", text);}});}});
	}
	public String success(final String text){
		return this.log(text, SUCCESS);
	}
	public String warning(final String text){
		return this.log(text, WARNING);
	}
	public String danger(final String text){
		return this.log(text, DANGER);
	}
	public String info(final String text){
		return this.log(text, INFO);
	}
	
	public String log(String text, String fallback, String color, List<Map<String,Object>> fields){
		JSONObject json = new JSONObject();
//		json.put("text", message);
		if(this.channel!=null)
			json.put("channel", channel);
		if(this.username!=null)
			json.put("username", username);
		if(text!=null)
			json.put("text", text);
		if(fallback!=null)
			json.put("fallback", fallback);
//		json.put("pretext", "Optional text that should appear above the formatted data");
		if(color!=null)
			json.put("color", color);
		if(fields!=null){
			JSONArray array = new JSONArray();
			for(Map<String, Object> f : fields)
				array.put(new JSONObject(f));
			json.put("fields", fields);
		}
//		.put("icon_emoji", ":ghost:")
		;
		Response response = http.doPOST(url, "payload="+json.toString(), MediaType.APPLICATION_FORM_URLENCODED_TYPE, null, null);
		return response.readEntity(String.class);
	}
	
	public static void main(String[] args) throws MalformedURLException {
		SlackLogger slack = SlackLoggerBuilder.init(new URL("https://hooks.slack.com/services/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")).build();
		System.out.println(slack.log(null, "ciao", GREEN, new ArrayList<Map<String, Object>>(){{add(new HashMap<String, Object>(){{put("title", "title");put("value", "value\nvalue");}});}}));
//		System.out.println(slack.log("Optional text that should appear within the attachment <https://beta.sciamlab.com/amaca_log/|log here>"));
//		System.out.println(slack.success("success"));
//		System.out.println(slack.warning("warning"));
//		System.out.println(slack.danger("danger"));
//		System.out.println(slack.info("info"));
	}

	
	public static class SlackLoggerBuilder{
		
		private final URL url;
		private String channel;
		private String username;
		
		public static SlackLoggerBuilder init(URL url){
			return new SlackLoggerBuilder(url);
		}
		
		private SlackLoggerBuilder(URL url){
			this.url = url;
		}
		
		public SlackLoggerBuilder username(String username){
			this.username = username;
			return this;
		}
		
		public SlackLoggerBuilder channel(String channel){
			this.channel = channel;
			return this;
		}
		
		public SlackLogger build(){
			return new SlackLogger(this);
		}
	}
}
