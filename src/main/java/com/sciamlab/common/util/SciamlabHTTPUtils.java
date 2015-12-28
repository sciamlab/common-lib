package com.sciamlab.common.util;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.log4j.Logger;

public class SciamlabHTTPUtils {

	private static final Logger logger = Logger.getLogger(SciamlabHTTPUtils.class);

	public static void copyAllHeadersToResponse(ResponseBuilder response, HttpServletRequest request){
		Enumeration<String> headers = request.getHeaderNames();
		while(headers.hasMoreElements()){
			String h = headers.nextElement();
			logger.info("Copying header: "+h+" = "+request.getHeader(h));
			response.header(h, request.getHeader(h));
		}
	}
	
	public static String addAllQueryStringParametersToUri(String uri, MultivaluedMap<String,String> map){
		for(String p_name : map.keySet()){
			String p_value = map.getFirst(p_name);
			uri += "&"+p_name+"="+p_value;
		}
		return uri;
	}
}
