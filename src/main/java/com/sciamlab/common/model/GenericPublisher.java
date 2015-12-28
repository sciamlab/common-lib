package com.sciamlab.common.model;

import java.util.Map;


public interface GenericPublisher<SI extends GenericSearchItem>{
	
	public String getId();
	
	public void addSearchItem(SI item);
	public SI getSearchItem(String identifier);
	public boolean hasSearchItem(String identifier);
	public Map<String, SI> getSearchItems();
}
