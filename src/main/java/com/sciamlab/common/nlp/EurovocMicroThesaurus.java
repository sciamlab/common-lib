package com.sciamlab.common.nlp;

import java.net.URI;
import java.net.URISyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

public class EurovocMicroThesaurus extends EurovocThesaurusItem {

	public static EurovocMicroThesaurus fromJSON(JSONObject json) throws JSONException, URISyntaxException {
		if(json==null)
			return null;
		EurovocMicroThesaurus ti = new EurovocMicroThesaurus();
		ti.setCode(json.getString("code"));
		ti.setLabel(json.getString("label"));
		ti.setUri(new URI(json.getString("uri")));
		return ti;
	}
}
