package com.sciamlab.common.nlp;

import java.net.URI;

import org.json.JSONObject;
import org.json.JSONString;

public class EurovocThesaurusEntry implements JSONString {
	
	enum Type{
		DOMAIN, MICRO_THESAURUS, CONCEPT; 
	}
	
	public final Type type;
	public final String code;
	public final String label;
	public final URI uri;
	
	private EurovocThesaurusEntry(Builder builder) {
		this.type = builder.type;
		this.code = builder.code;
		this.label = builder.label;
		this.uri = builder.uri;
	}

	@Override
	public String toJSONString() {
		JSONObject thesItem = new JSONObject();
		thesItem.put("type", this.type);
		thesItem.put("code", this.code);
		thesItem.put("label", this.label);
		thesItem.put("uri", this.uri.toString());		
		return thesItem.toString();
	}	
	
	public static class Builder{
		private final Type type;
		private final String code;
		private final String label;
		private final URI uri;
		
		public Builder(Type type, String code, String label, URI uri) {
			this.type = type;
			this.code = code;
			this.label = label;
			this.uri = uri;
		}
		
		public EurovocThesaurusEntry build(){
			return new EurovocThesaurusEntry(this);
		}
	}
}
