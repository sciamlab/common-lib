package com.sciamlab.common.model;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sciamlab.common.model.util.GenericBuilder;

/**
 * the GenericSearchChannelImpl corresponds to a search portal (like http://www.paginegialle.it/)
 * @author sciamlab
 *
 */
public class GenericSearchChannelImpl<SI extends GenericSearchItem, P extends GenericPublisher> implements GenericSearchChannel<SI, P> {
	
	private static final Logger logger = Logger.getLogger(GenericSearchChannelImpl.class);

	private final Map<String, SI> searchItems;
	private final Map<String, P> publishers;
	
	@Expose 
	private final String title;
	@Expose 
	@SerializedName("id")
	private final String identifier;
	@Expose 
	private final URL baseURL;
	@Expose 
	private final URL channelURL;
	
	private GenericSearchChannelImpl(GenericSearchChannelImplBuilder builder){
		this.identifier = builder.identifier;
		this.title = builder.title;
		this.searchItems = new HashMap<String, SI>();
		this.publishers = new HashMap<String, P>();
		this.baseURL = builder.baseURL;
		this.channelURL = builder.catalogURL;
	}
	
	@Override
	public Map<String, SI> getSearchItems() {
		return searchItems;
	}
	@Override
	public void addSearchItem(SI searchItem) {
		this.searchItems.put(searchItem.getId(), searchItem);
	}
	@Override
	public SI getSearchItem(String id) {
		return this.searchItems.get(identifier);
	}
	@Override
	public boolean hasSearchItem(String id) {
		return this.searchItems.containsKey(identifier);
	}

	
	@Override
	public void addPublisher(P publisher) {
		this.publishers.put(publisher.getId(), publisher);
	}
	@Override
	public P getPublisher(String id) {
		return publishers.get(id);
	}
	@Override
	public boolean hasPublisher(String id) {
		return publishers.containsKey(id);
	}
	@Override
	public Map<String, P> getPublishers() {
		return publishers;
	}
	
	public String getIdentifier() {
		return identifier;
	}
	@Override
	public String getId() {
		return identifier;
	}

	public String getTitle() {
		return title;
	}

	

	public URL getBaseURL() {
		return baseURL;
	}

	public URL getChannelURL() {
		return channelURL;
	}

	@Override
	public int hashCode() {
		final int prime = 35;
		int result = 1;
		result = prime * result
				+ ((identifier == null) ? 0 : identifier.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GenericSearchChannelImpl other = (GenericSearchChannelImpl) obj;
		if (identifier == null) {
			if (other.identifier != null)
				return false;
		} else if (!identifier.equals(other.identifier))
			return false;
		return true;
	}

	public static class GenericSearchChannelImplBuilder<SI extends GenericSearchItem, P extends GenericPublisher> extends GenericBuilder{
		
		private final String identifier;
		private String title;
		private final URL baseURL;
		private final URL catalogURL;
		
		public static GenericSearchChannelImplBuilder init(String identifier, URL baseURL, URL catalogURL){
			return new GenericSearchChannelImplBuilder(identifier, baseURL, catalogURL);
		}
		
		private GenericSearchChannelImplBuilder(String identifier, URL baseURL, URL catalogURL){
			this.identifier = identifier;
			this.baseURL = baseURL;
			this.catalogURL = catalogURL;
		}
		
		public GenericSearchChannelImplBuilder<SI, P> title(String title){
			this.title = title;
			return this;
		}
		
		public GenericSearchChannelImpl<SI, P> build(){
			return new GenericSearchChannelImpl<SI, P>(this);
		}
	}


}
