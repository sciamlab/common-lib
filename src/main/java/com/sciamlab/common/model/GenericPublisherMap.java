package com.sciamlab.common.model;

import java.util.TreeMap;


public class GenericPublisherMap<P extends GenericPublisher> extends TreeMap<String, P> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2183605006995950117L;

	public P getDefault(GenericSearchChannel<? extends GenericSearchItem, P> channel){
		return get(channel.getId());
	}
}
