package com.sciamlab.common.nlp;

import java.net.URI;

import org.json.JSONObject;
import org.json.JSONString;

/**
 * This represent a single thesauro item with code,label and an associated URI
 * 
 * @author alessio
 *
 */
public abstract class EurovocThesaurusItem implements JSONString {
	
	private String code;
	private String label;
	private URI uri;
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public URI getUri() {
		return uri;
	}

	public void setUri(URI uri) {
		this.uri = uri;
	}

	@Override
	public String toJSONString() {
		JSONObject thesItem = new JSONObject();
		thesItem.put("code", this.code);
		thesItem.put("label", this.label);
		thesItem.put("uri", this.uri.toString());		
		return thesItem.toString();
	}	
}
