package com.sciamlab.common.model.datacatalog;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONObject;

public enum OpenDataCategory {
	
	/*
	 * refer to : https://joinup.ec.europa.eu/node/139531
	 */

	AGRICULTURE_FISHERIES_FORESTRY_FOOD("AGRICULTURE-FISHERIES-FORESTRY-FOOD"),
	EDUCATION_CULTURE_AND_SPORT("EDUCATION-CULTURE-AND-SPORT"),
	ENVIRONMENT("ENVIRONMENT"),
	ENERGY("ENERGY"),
	TRANSPORT("TRANSPORT"),
	SCIENCE_AND_TECHNOLOGY("SCIENCE-AND-TECHNOLOGY"),
	ECONOMY_AND_FINANCE("ECONOMY-AND-FINANCE"),
	POPULATION_AND_SOCIAL_CONDITIONS("POPULATION-AND-SOCIAL-CONDITIONS"),
	HEALTH("HEALTH"),
	GOVERNMENT_PUBLIC_SECTOR("GOVERNMENT-PUBLIC-SECTOR"),
	REGIONS_CITIES("REGIONS-CITIES"),
	JUSTICE_LEGAL_SYSTEM_PUBLIC_SAFETY("JUSTICE-LEGAL-SYSTEM-PUBLIC-SAFETY"),
	INTERNATIONAL_ISSUES("INTERNATIONAL-ISSUES");
	
	private String id;
	private String name;
	private Set<String> alias = new HashSet<String>();
	
	public static Map<String, OpenDataCategory> byId = new TreeMap<String, OpenDataCategory>();
	private static Map<String, OpenDataCategory> byAlias = new TreeMap<String, OpenDataCategory>();
	public static OpenDataCategory getByAlias(String alias){
		return byAlias.get(alias.toLowerCase().replace("\n", "").trim());
	}
	
	private OpenDataCategory(String id){
		this.id = id;
	}
	
	public String id() { return id; }
	public String namee() { return name; }
	public OpenDataCategory namee(String name) { 
		this.name = name;
		return this;
	}
	public Set<String> alias() { return alias; }
	public Set<String> alias(String alias){
		this.alias.add(alias);
		this.alias.add(alias.toUpperCase());
		this.alias.add(alias.toLowerCase());
		byAlias.put(alias, this);
		byAlias.put(alias.toUpperCase(), this);
		byAlias.put(alias.toLowerCase(), this);
		return this.alias;
	}
	public Set<String> alias(Collection<String> aliases){
		for(String alias : aliases){
			alias(alias);
		}
		return this.alias;
	}

	static{
    	for(OpenDataCategory category : OpenDataCategory.values()){
    		byId.put(category.id, category);
    		category.alias(category.id);
    		category.alias(category.id.toUpperCase());
    		category.alias(category.id.toLowerCase());
    	}
    }
	
    @Override 
    public String toString() { 
    	JSONObject json = new JSONObject();
    	json.put("id", this.id);
    	json.put("name", this.name);
    	json.put("alias", new JSONArray().put(alias));
    	return json.toString();
    }
}
