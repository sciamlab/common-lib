package com.sciamlab.common.model.mdr;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.w3c.dom.Element;

import com.sciamlab.common.exception.SciamlabException;
import com.sciamlab.common.util.SciamlabCollectionUtils;
import com.sciamlab.common.util.SciamlabStreamUtils;
import com.sciamlab.common.util.XMLLoader;

public class EUNamedAuthorityVocabularyMap implements Serializable{ 
	
	private static final long serialVersionUID = -8521456245221650967L;
	private static final Logger logger = Logger.getLogger(EUNamedAuthorityVocabularyMap.class);
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("EUNamedAuthorityVocabularyMap [map=");
		for(Entry<EUNamedAuthorityVocabulary, EUNamedAuthorityEntryMap<? extends EUNamedAuthorityEntry>> e : map.entrySet()){
			sb.append("{"+e.getKey().name()+","+e.getValue().toString()+"}");
		}
		return  sb.append("]").toString();
	}

	private final HashMap<EUNamedAuthorityVocabulary, EUNamedAuthorityEntryMap<? extends EUNamedAuthorityEntry>> map = 
		new HashMap<EUNamedAuthorityVocabulary, EUNamedAuthorityEntryMap<? extends EUNamedAuthorityEntry>>();
	
	private static EUNamedAuthorityVocabularyMap instance = new EUNamedAuthorityVocabularyMap();
    private EUNamedAuthorityVocabularyMap() {}
    
    public static synchronized void instance(EUNamedAuthorityVocabularyMap new_instance) {
    	instance = new_instance;
    }
    public static synchronized EUNamedAuthorityVocabularyMap instance() {
//    	if(instance==null)
//    		instance = new EUNamedAuthorityVocabularyMap();
    	return instance;
    }
    public static <E extends EUNamedAuthorityEntry> EUNamedAuthorityEntryMap<E> load(EUNamedAuthorityVocabulary vocabulary) throws SciamlabException {
    	return instance.load_internal(vocabulary);
    }
    public static <E extends EUNamedAuthorityEntry> EUNamedAuthorityEntryMap<E> load(EUNamedAuthorityVocabulary vocabulary, String alias_file) throws SciamlabException {
    	return instance.load_internal(vocabulary, alias_file);
    }
    public static <E extends EUNamedAuthorityEntry> EUNamedAuthorityEntryMap<E> load(EUNamedAuthorityVocabulary vocabulary, JSONObject aliases) throws SciamlabException {
    	return instance.load_internal(vocabulary, aliases);
    }
    
    public static <E extends EUNamedAuthorityEntry> EUNamedAuthorityEntryMap<E> get(EUNamedAuthorityVocabulary vocabulary) {//throws SciamlabException {
    	return 
    			(EUNamedAuthorityEntryMap<E>) 
//    			(instance.map.containsKey(vocabulary) ? 
    					instance.map.get(vocabulary) 
//    					: instance.load(vocabulary))
    			;
    }
    
	private <E extends EUNamedAuthorityEntry> EUNamedAuthorityEntryMap<E> load_internal(EUNamedAuthorityVocabulary vocabulary) throws SciamlabException {
		JSONObject aliases = null;
		try(InputStream is = SciamlabStreamUtils.getInputStream(vocabulary.alias_file)){
			aliases = new JSONObject(SciamlabStreamUtils.convertStreamToString(is));
		}catch(FileNotFoundException e){
			logger.warn("Default alias file "+vocabulary.alias_file+" not available for vocabulary "+vocabulary.name());
		} catch (IOException e) {
			throw new SciamlabException(e);
		}
		return load_internal(vocabulary, aliases);
	}
	
	private <E extends EUNamedAuthorityEntry> EUNamedAuthorityEntryMap<E> load_internal(EUNamedAuthorityVocabulary vocabulary, String alias_file) throws SciamlabException{
		if(alias_file==null){
			//using default alias file
			return load_internal(vocabulary);
		}
		JSONObject aliases = null;
		try(InputStream is = SciamlabStreamUtils.getInputStream(alias_file)){
			aliases = new JSONObject(SciamlabStreamUtils.convertStreamToString(is));
		}catch(FileNotFoundException e){
			logger.warn("Provided alias file "+alias_file+" not available for vocabulary "+vocabulary.name());
		} catch (IOException e) {
			throw new SciamlabException(e);
		}
		return load_internal(vocabulary, aliases);
	}
	
	private <E extends EUNamedAuthorityEntry> EUNamedAuthorityEntryMap<E> load_internal(EUNamedAuthorityVocabulary vocabulary, JSONObject aliases) throws SciamlabException {
		
		EUNamedAuthorityEntryMap<E> entry_map = new EUNamedAuthorityEntryMap<E>();
		try{
			List<Element> xml = new ArrayList<Element>();
			try(InputStream cached_is = SciamlabStreamUtils.getInputStream(vocabulary.file)){
				xml = XMLLoader.load(cached_is, "record");
			}catch(FileNotFoundException e){
				logger.warn("Local cache of "+vocabulary.file+" not found. Loading remotely...");
				try(InputStream remote_is = SciamlabStreamUtils.getRemoteInputStream(vocabulary.url.toString())){
					xml = XMLLoader.load(remote_is, "record");
				} 
			}
			for(Element record : xml){
				if(Boolean.parseBoolean(record.getAttribute("deprecated")))
					continue;
				E entry = (E) vocabulary.builder.getDeclaredConstructor(Element.class).newInstance(new Object[] {record}).build();
				entry_map.add(entry);
				
				//loading aliases from json, if any
				if(aliases!=null){
					JSONObject json = aliases.optJSONObject(entry.authority_code);
					if(json!=null){
						entry_map.alias(entry, SciamlabCollectionUtils.asStringList(json.optJSONArray("alias")));
					}
				}
			}
			
		}catch(Exception e){
			throw new SciamlabException(e);
		}
		instance.map.put(vocabulary, entry_map);
		return entry_map;
	}
	
}
