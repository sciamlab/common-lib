package com.sciamlab.common.model.newsfeed;

import org.json.JSONObject;

import com.sciamlab.common.model.GenericSearchChannel;
import com.sciamlab.common.model.GenericSearchItem;
import com.sciamlab.common.model.datacatalog.OpenDataCategory;
import com.sciamlab.common.nlp.EurovocConcept;
import com.sciamlab.common.nlp.EurovocField;
import com.sciamlab.common.nlp.EurovocMicroThesaurus;

public class News implements GenericSearchItem {
	
	private String id;
	private JSONObject json;
	private Aggregator aggregator;
	private Editor editor;
	
	public News(String id, JSONObject json) {
		super();
		this.id = id;
		this.json = json;
	}

	@Override
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public JSONObject getJson() {
		return json;
	}

	public void setJson(JSONObject json) {
		this.json = json;
	}
	
	@Override
	public Aggregator getChannel() {
		return aggregator;
	}
	@Override
	public Editor getPublisher() {
		return editor;
	}

}
