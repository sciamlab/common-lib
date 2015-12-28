package com.sciamlab.common.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;

import org.json.JSONObject;

public interface TextAnnotator {

	public Collection<JSONObject> annotate(String text) throws MalformedURLException, IOException;
}
