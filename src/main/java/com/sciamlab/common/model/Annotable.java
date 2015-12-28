package com.sciamlab.common.model;

import java.util.Collection;

import org.json.JSONObject;

public interface Annotable {
	public String getText();
	public void setResources(Collection<JSONObject> resources);
}
