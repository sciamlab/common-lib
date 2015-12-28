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
public class GenericPublisherImpl<SI extends GenericSearchItem> implements GenericPublisher<SI> {
	
	private static final Logger logger = Logger.getLogger(GenericPublisherImpl.class);

	private final Map<String, SI> searchItems;
	
	@Expose 
	private final String title;
	@Expose 
	@SerializedName("id")
	private final String identifier;
	@Expose 
	private final URL homepage;
	
	private GenericPublisherImpl(GenericPublisherImplBuilder builder){
		this.identifier = builder.identifier;
		this.title = builder.title;
		this.searchItems = new HashMap<String, SI>();
		this.homepage = builder.homepage;
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

	@Override
	public Map<String, SI> getSearchItems() {
		return searchItems;
	}
	@Override
	public void addSearchItem(SI searchItem) {
		this.searchItems.put(searchItem.getId(), searchItem);
	}
	@Override
	public SI getSearchItem(String identifier) {
		return this.searchItems.get(identifier);
	}
	@Override
	public boolean hasSearchItem(String identifier) {
		return this.searchItems.containsKey(identifier);
	}
	
	public URL getHomepage() {
		return homepage;
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
		GenericPublisherImpl other = (GenericPublisherImpl) obj;
		if (identifier == null) {
			if (other.identifier != null)
				return false;
		} else if (!identifier.equals(other.identifier))
			return false;
		return true;
	}

	public static class GenericPublisherImplBuilder<SI extends GenericSearchItem> extends GenericBuilder{
		
		private final String identifier;
		private final String title;
		private URL homepage;
		
		public static GenericPublisherImplBuilder init(String identifier, String title){
			return new GenericPublisherImplBuilder(identifier, title);
		}
		
		private GenericPublisherImplBuilder(String identifier, String title){
			this.identifier = identifier;
			this.title = title;
		}
		
		public GenericPublisherImplBuilder<SI> homepage(URL homepage){
			this.homepage = homepage;
			return this;
		}
		
		public GenericPublisherImpl<SI> build(){
			return new GenericPublisherImpl<SI>(this);
		}
	}


}
