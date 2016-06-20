package com.sciamlab.common.util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilderException;

import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

/**
 * 
 * @author SciamLab
 *
 */

public class HTTPClient {
	
	private Client c;
	
	public HTTPClient() {
		this.c = ClientBuilder.newClient(); 
	}
	
	public HTTPClient(int timeout) {
		this();
		c.property(ClientProperties.CONNECT_TIMEOUT, timeout);
		c.property(ClientProperties.READ_TIMEOUT,    timeout);
	}
	
	public HTTPClient(HttpAuthenticationFeature feature) {
		this(); 
		c.register(feature);    
	}

//	private static Map<URL, Integer> urlCacheStatus = new HashMap<URL, Integer>(); 
	private static Cache<URL, Integer> urlCacheStatus = CacheBuilder.newBuilder().maximumSize(10000).build();
//	private synchronized boolean checkAndUpdateCache(String url){
//		if(URLS_CACHE.getIfPresent(url)!=null)
//			return false;
//		URLS_CACHE.put(url, url);
//		return true;
//	}
	
	public Response.Status getURLResponseStatus(URL url) throws UriBuilderException {
		Integer cached = urlCacheStatus.getIfPresent(url);
		if (cached!=null)
			return Response.Status.fromStatusCode(cached);
		
		Response r = this.doHEAD(url);
		urlCacheStatus.put(url, r.getStatus());
		return Response.Status.fromStatusCode(r.getStatus());
	}
	
	public boolean isOK(URL url) throws UriBuilderException{
//		return this.is(new ArrayList<Response.Status>(){{add(Response.Status.OK);add(Response.Status.SEE_OTHER);add(Response.Status.MOVED_PERMANENTLY);}}, url);
		return getURLResponseStatus(url).getStatusCode()<400;
	}
	
	public boolean is(final Response.Status status, URL url){
		return is(new ArrayList<Response.Status>(){{add(status);}}, url);
	}
	
	public boolean is(List<Response.Status> status, URL url){
		try {
			return status.contains(getURLResponseStatus(url));
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * this method does't set any user agent, header and query params
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public Response doGET(URL url) {
		return this.doGET(url, null, null, null);
	}
	
	/**
	 * this method does't set any user agent, header and query params
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public Response doGET(URL url, MediaType mediaType) {
		return this.doGET(url, null, null, mediaType);
	}
	
	/**
	 * this method doesn't set the user agent
	 * 
	 * @param url
	 * @param params
	 * @param header
	 * @return
	 */
	public Response doGET(URL url, MultivaluedMap<String, String> params, MultivaluedMap<String, String> header) {
		return this.doGET(url, null, params, header, null);
	}

	/**
	 * this method doesn't set the user agent
	 * 
	 * @param url
	 * @param params
	 * @param header
	 * @return
	 */
	public Response doGET(URL url, MultivaluedMap<String, String> params, MultivaluedMap<String, String> header, MediaType mediaType) {
		return this.doGET(url, null, params, header, mediaType);
	}
	
	/**
	 * 
	 * this method follows the redirect, if any
	 * 
	 * @param url
	 * @param user_agent
	 * @param params
	 * @param header
	 * @return
	 */
	public Response doGET(URL url, String user_agent, MultivaluedMap<String, String> params, MultivaluedMap<String, String> header, MediaType mediaType){
		return doGET(url, user_agent, true, params, header, mediaType);
	}
	
	public Response doGET(URL url, String user_agent, boolean follow_redirects, MultivaluedMap<String, String> params, MultivaluedMap<String, String> header, MediaType mediaType){
		WebTarget wt = c.target(url.toString()).path("");
		if(!follow_redirects)
			wt.property(ClientProperties.FOLLOW_REDIRECTS, Boolean.FALSE);
		if(params!=null){
			for(String k : params.keySet()){
				List<String> values = params.get(k);
				for(String v : values){
					wt = wt.queryParam(k, v);
				}
			}
		}
		Builder b = mediaType!=null?wt.request(mediaType):wt.request();
		if(user_agent!=null)
			b.header("User-Agent", user_agent);
		if(header!=null){
			for(String k : header.keySet()){
				List<String> values = header.get(k);
				for(String v : values){
					b.header(k, v);
				}
			}
		}
		return b.get();
	}
	
	public Response doHEAD(URL url) throws UriBuilderException{
		return this.doHEAD(url, null, true, null, null);
	}
	
	public Response doHEAD(
			URL url, String user_agent, boolean follow_redirects, MultivaluedMap<String, String> params, MultivaluedMap<String, String> header) 
					throws UriBuilderException{
		WebTarget wt = c.target(url.toString()).path("");
		if(!follow_redirects)
			wt.property(ClientProperties.FOLLOW_REDIRECTS, Boolean.FALSE);
		if(params!=null){
			for(String k : params.keySet()){
				List<String> values = params.get(k);
				for(String v : values){
					wt = wt.queryParam(k, v);
				}
			}
		}
		Builder b = wt.request();
		if(user_agent!=null)
			b.header("User-Agent", user_agent);
		if(header!=null){
			for(String k : header.keySet()){
				List<String> values = header.get(k);
				for(String v : values){
					b.header(k, v);
				}
			}
		}
		return b.head();
	}
	
	public Response doPOST(URL url, String body, MediaType media_type, MultivaluedMap<String, String> params, MultivaluedMap<String, String> header) {
		WebTarget wt = c.target(url.toString()).path("");
		if(params!=null){
			for(String k : params.keySet()){
				List<String> values = params.get(k);
				for(String v : values){
					wt.queryParam(k, v);
				}
			}
		}
		Builder b = wt.request();
		if(header!=null){
			for(String k : header.keySet()){
				List<String> values = header.get(k);
				for(String v : values){
					b.header(k, v);
				}
			}
		}
		return b.post(Entity.entity(body, media_type));
	}
	
	public Response doPUT(URL url, String body, MediaType media_type, MultivaluedMap<String, String> params, MultivaluedMap<String, String> header) {
		WebTarget wt = c.target(url.toString()).path("");
		if(params!=null){
			for(String k : params.keySet()){
				List<String> values = params.get(k);
				for(String v : values){
					wt.queryParam(k, v);
				}
			}
		}
		Builder b = wt.request();
		if(header!=null){
			for(String k : header.keySet()){
				List<String> values = header.get(k);
				for(String v : values){
					b.header(k, v);
				}
			}
		}
		return b.put(Entity.entity(body, media_type));
	}
	
	public Response doDELETE(URL url, MultivaluedMap<String, String> params, MultivaluedMap<String, String> header) {
		WebTarget wt = c.target(url.toString()).path("");
		if(params!=null){
			for(String k : params.keySet()){
				List<String> values = params.get(k);
				for(String v : values){
					wt.queryParam(k, v);
				}
			}
		}
		Builder b = wt.request(MediaType.APPLICATION_JSON_TYPE);
		if(header!=null){
			for(String k : header.keySet()){
				List<String> values = header.get(k);
				for(String v : values){
					b.header(k, v);
				}
			}
		}
		return b.delete();
	}
	
}
