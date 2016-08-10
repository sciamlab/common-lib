package com.sciamlab.common.util;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.log.Log;

public class SciamlabVelocityHelper {
	
	private static final Logger logger = Logger.getLogger(SciamlabVelocityHelper.class);

	final VelocityEngine ve;
	
	private SciamlabVelocityHelper(Builder builder){
		this.ve = new VelocityEngine(builder.velocity_props); 
		this.ve.init();
		Log log = this.ve.getLog();
	}
	
	public String getTemplateFromInputStream(Properties props, InputStream is){
		return getTemplateFromInputStream(props, is, 1);
	}
	
	public String getTemplateFromInputStream(Properties props, InputStream is, int iterations){
		return getTemplateFromString(props, SciamlabStreamUtils.convertStreamToString(is), iterations);
	}
	
	public String getTemplateFromString(Properties props, String template){
		return getTemplateFromString(props, template, 1);
	}
	
	public String getTemplateFromString(Properties props, String template, int iterations){
		VelocityContext context = new VelocityContext();
		if(props!=null)
			for(Entry<Object, Object> prop : props.entrySet()){
//				System.out.println((String) key+": "+ props.getProperty((String) key));
				context.put((String) prop.getKey(), prop.getValue());
			}
		for(int i=0 ; i<iterations ; i++){
			StringWriter swOut = new StringWriter();
	        ve.evaluate(context, swOut, "SciamlabVelocityHelper", template);
	        template = swOut.toString();
		}
		return template;
	}
	
	public static class Builder{
		
		final Properties velocity_props = new Properties();
		
		public Builder(){ }
		public Builder(Properties velocity_props){
			this.velocity_props.putAll(velocity_props);
		}
		
		public Builder prop(String key, String value){
			this.velocity_props.put(key, value);
			return this;
		}
		
		public SciamlabVelocityHelper build(){
			if(velocity_props.isEmpty())
				logger.warn("Velocity helper initiated with empty properties");
			return new SciamlabVelocityHelper(this);
		}
	}

}
