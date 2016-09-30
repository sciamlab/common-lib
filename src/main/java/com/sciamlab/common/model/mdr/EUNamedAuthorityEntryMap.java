package com.sciamlab.common.model.mdr;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class EUNamedAuthorityEntryMap<E extends EUNamedAuthorityEntry> implements Serializable{

	private static final long serialVersionUID = -5171438145474190177L;

	// contiene una mappatura tra le chiavi originali e relativi alias
	private Map<String, E> values = new HashMap<String, E>();
	private Map<String, String> alias = new HashMap<String, String>();

	public EUNamedAuthorityEntryMap<E> add(E value){
		this.values.put(value.authority_code, value);
		this.alias.put(value.authority_code.toLowerCase().trim(), value.authority_code);
		return this;
	}
	
	public E get(String key){
		E value = values.get(key);
		if(value!=null)
			return value;
		return this.alias.containsKey(key.toLowerCase().trim()) ? this.values.get(this.alias.get(key.toLowerCase().trim())) : null;
	}
	
	public Collection<E> values(){
		return this.values.values();
	}

	public EUNamedAuthorityEntryMap<E> alias(E value, String alias){
		if(!this.values.containsKey(value.authority_code))
			this.add(value);
		this.alias.put(alias.toLowerCase().trim(), value.authority_code);
		return this;
	}
	
	public EUNamedAuthorityEntryMap<E> alias(E value, Collection<String> aliases){
		for(String alias : aliases)
			alias(value, alias);
		return this;
	}

	@Override
	public String toString() {
		return "EUNamedAuthorityEntryMap [values=" + values.size() + ", alias=" + alias.size() + "]";
	}
	
	
}
