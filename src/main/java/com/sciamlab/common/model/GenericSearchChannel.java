package com.sciamlab.common.model;

import java.util.Map;

public interface GenericSearchChannel<SI extends GenericSearchItem, P extends GenericPublisher>{
	
	public String getId();
	
	public void addSearchItem(SI dataset);
	public SI getSearchItem(String id);
	public boolean hasSearchItem(String id);
	public Map<String, SI> getSearchItems();
	
	public void addPublisher(P publisher);
	public P getPublisher(String id);
	public boolean hasPublisher(String id);
	public Map<String, P> getPublishers();
}
