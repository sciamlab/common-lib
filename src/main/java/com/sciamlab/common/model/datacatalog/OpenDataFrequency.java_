package com.sciamlab.common.model.datacatalog;

import java.net.URI;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONObject;

public enum OpenDataFrequency {
	
	/*
	 * refer to : https://joinup.ec.europa.eu/asset/dcat_application_profile/issue/vo4-choose-between-dcmi-and-sdmx-frequency-vocabulary
	 */
	
	TRIENNIAL("TRIENNIAL"),
	BIENNIAL("BIENNIAL"),
	ANNUAL("ANNUAL"),
	SEMIANNUAL("SEMIANNUAL"),
	THREE_TIMES_A_YEAR("THREE-TIMES-A-YEAR"),
	QUARTERLY("QUARTERLY"),
	BIMONTHLY("BIMONTHLY"),
	MONTHLY("MONTHLY"),
	SEMIMONTHLY("SEMIMONTHLY"),
	BIWEEKLY("BIWEEKLY"),
	THREE_TIMES_A_MONTH("THREE-TIMES-A-MONTH"),
	WEEKLY("WEEKLY"),
	SEMIWEEKLY("SEMIWEEKLY"),
	THREE_TIMES_A_WEEK("THREE-TIMES-A-WEEK"),
	DAILY("DAILY"),
	CONTINUOUS("CONTINUOUS"),
	IRREGULAR("IRREGULAR");

	private String id;
	private URI uri;
//	private String qualified_name;
	private String name;
	private String definition;
	private Set<String> alias = new HashSet<String>();;
	
	public static Map<String, OpenDataFrequency> byId = new TreeMap<String, OpenDataFrequency>();
	private static Map<String, OpenDataFrequency> byAlias = new TreeMap<String, OpenDataFrequency>();
	public static OpenDataFrequency getByAlias(String alias){
		return byAlias.get(alias.toLowerCase().replace("\n", "").trim());
	}
	
	private OpenDataFrequency(String id){
		this.id = id;
	}
	
	public String id() { return id; }
	public URI uri() { return uri; }
	public OpenDataFrequency uri(URI uri) { 
		this.uri = uri; 
		return this;
	}
//	public String qualifiedName() { return qualified_name; }
//	public OpenDataFrequency qualifiedName(String qualified_name) { 
//		this.qualified_name = qualified_name; 
//		return this;
//	}
	public String namee() { return name; }
	public OpenDataFrequency namee(String name) { 
		this.name = name; 
		return this;
	}
	public String definition() { return definition; }
	public OpenDataFrequency definition(String definition) { 
		this.definition = definition; 
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
	public Set<String>  alias(Collection<String> aliases){
		for(String alias : aliases){
			alias(alias);
		}
		return this.alias;
	}
	
	static{
    	for(OpenDataFrequency frequency : OpenDataFrequency.values()){
    		byId.put(frequency.id, frequency);
    		frequency.alias(frequency.id);
    		frequency.alias(frequency.id.toUpperCase());
    		frequency.alias(frequency.id.toLowerCase());
    	}
    }
	
    @Override 
    public String toString() { 
    	JSONObject json = new JSONObject();
    	json.put("id", this.id);
    	json.put("uri", this.uri);
//    	json.put("qualified_name", this.qualified_name);
    	json.put("name", this.name);
    	json.put("definition", this.definition);
    	json.put("alias", new JSONArray().put(alias));
    	return json.toString();
    }
}
