package com.sciamlab.common.model;

import org.json.JSONObject;

public class JSONSearchItem implements GenericSearchItem {
	
	private String id;
	private JSONObject json;
	private GenericSearchChannel<JSONSearchItem, GenericPublisher<JSONSearchItem>> channel;
	private GenericPublisher<JSONSearchItem> publisher;
	
	public JSONSearchItem(String id, JSONObject json) {
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
	public GenericSearchChannel<JSONSearchItem, GenericPublisher<JSONSearchItem>> getChannel() {
		return channel;
	}

	public void setChannel(GenericSearchChannel<JSONSearchItem, GenericPublisher<JSONSearchItem>> channel) {
		this.channel = channel;
	}
	
	@Override
	public GenericPublisher<JSONSearchItem> getPublisher() {
		return publisher;
	}
	
	public void setPublisher(GenericPublisher<JSONSearchItem> publisher) {
		this.publisher = publisher;
	}

}
