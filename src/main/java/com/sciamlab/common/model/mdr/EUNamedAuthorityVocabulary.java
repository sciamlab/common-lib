package com.sciamlab.common.model.mdr;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.w3c.dom.Element;

import com.sciamlab.common.exception.SciamlabException;
import com.sciamlab.common.model.mdr.vocabulary.EUNamedAuthorityCountry;
import com.sciamlab.common.model.mdr.vocabulary.EUNamedAuthorityCurrency;
import com.sciamlab.common.model.mdr.vocabulary.EUNamedAuthorityDataTheme;
import com.sciamlab.common.model.mdr.vocabulary.EUNamedAuthorityFileType;
import com.sciamlab.common.model.mdr.vocabulary.EUNamedAuthorityFrequency;
import com.sciamlab.common.model.mdr.vocabulary.EUNamedAuthorityLanguage;
import com.sciamlab.common.model.mdr.vocabulary.EUNamedAuthorityLicence;
import com.sciamlab.common.model.mdr.vocabulary.EUNamedAuthorityTimePeriod;
import com.sciamlab.common.util.SciamlabCollectionUtils;
import com.sciamlab.common.util.SciamlabStreamUtils;
import com.sciamlab.common.util.XMLLoader;

public enum EUNamedAuthorityVocabulary {
	
	COUNTRY("country", "countries.xml", "alias_countries.json", EUNamedAuthorityCountry.class, EUNamedAuthorityCountry.Builder.class),
	CURRENCY("currency", "currencies.xml", "alias_currencies.json", EUNamedAuthorityCurrency.class, EUNamedAuthorityCurrency.Builder.class),
	DATA_THEME("data-theme", "data-theme.xml", "alias_data-theme.json", EUNamedAuthorityDataTheme.class, EUNamedAuthorityDataTheme.Builder.class),
	FILE_TYPE("file-type", "filetypes.xml", "alias_filetypes.json", EUNamedAuthorityFileType.class, EUNamedAuthorityFileType.Builder.class),
	FREQUENCY("frequency", "frequencies.xml", "alias_frequencies.json", EUNamedAuthorityFrequency.class, EUNamedAuthorityFrequency.Builder.class),
	LANGUAGE("language", "languages.xml", "alias_languages.json", EUNamedAuthorityLanguage.class, EUNamedAuthorityLanguage.Builder.class),
	LICENCE("licence", "licences.xml", "alias_licences.json", EUNamedAuthorityLicence.class, EUNamedAuthorityLicence.Builder.class),
	TIMEPERIOD("timeperiod", "timeperiods.xml", "alias_timeperiods.json", EUNamedAuthorityTimePeriod.class, EUNamedAuthorityTimePeriod.Builder.class),;
	
	private static final Logger logger = Logger.getLogger(EUNamedAuthorityVocabulary.class);

	public final static String BASE_URI = "http://publications.europa.eu/resource/authority/";
	public final static String BASE_URL = "http://publications.europa.eu/mdr/resource/authority/";
	
	private String id;
	private URI uri;
	private URL url;
	private String file;
	private String alias_file;
	private Class<? extends EUNamedAuthorityEntry> clazz;
	private Class<? extends EUNamedAuthorityEntry.Builder> builder;
	public EUNamedAuthorityVocabularyMap<? extends EUNamedAuthorityEntry> entryMap;
	
	private EUNamedAuthorityVocabulary(String id, String file, String alias_file, Class<? extends EUNamedAuthorityEntry> clazz, Class<? extends EUNamedAuthorityEntry.Builder> builder){
		this.id = id;
		this.alias_file = alias_file;
		this.file = file;
		try {
			this.uri = new URI(BASE_URI+id+"/xml/"+file);
			this.url = new URL(BASE_URL+id+"/xml/"+file);
		} catch (MalformedURLException | URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.clazz = clazz;
		this.builder = builder;
	}
	
	public String id() { return id; }
	public URI uri() { return uri; }
	public String file() { return file; }
	public String alias_file() { return alias_file; }
		
	/*
	 * ALIASES
	 */
	private Map<String, EUNamedAuthorityEntry> byAlias = new TreeMap<String, EUNamedAuthorityEntry>();
	public <E extends EUNamedAuthorityEntry> E getByAlias(String alias){
		return (E) byAlias.get(alias.toLowerCase().trim());
	}
	public EUNamedAuthorityVocabulary setAlias(EUNamedAuthorityEntry entry, String alias){
		byAlias.put(alias.toLowerCase().trim(), entry);
		return this;
	}
	public EUNamedAuthorityVocabulary setAliases(EUNamedAuthorityEntry entry, Collection<String> aliases){
		for(String alias : aliases)
			setAlias(entry, alias);
		return this;
	}
	
	
	/*
	 * FACTORY
	 */
	private final static Map<EUNamedAuthorityVocabulary, EUNamedAuthorityVocabularyMap<? extends EUNamedAuthorityEntry>> VOCABULARY_CACHE_MAP = 
			new HashMap<EUNamedAuthorityVocabulary, EUNamedAuthorityVocabularyMap<? extends EUNamedAuthorityEntry>>();
	
	public <E extends EUNamedAuthorityEntry> EUNamedAuthorityVocabularyMap<E> load() throws SciamlabException {
		JSONObject aliases = null;
		try(InputStream is = SciamlabStreamUtils.getInputStream(alias_file)){
			aliases = new JSONObject(SciamlabStreamUtils.convertStreamToString(is));
		}catch(FileNotFoundException e){
			logger.warn("Default alias file "+alias_file+" not available for vocabulary "+name());
		} catch (IOException e) {
			throw new SciamlabException(e);
		}
		return load(aliases);
	}
	
	public EUNamedAuthorityVocabularyMap<? extends EUNamedAuthorityEntry> load(String alias_file) throws SciamlabException{
		if(alias_file==null){
			//using default alias file
			return load();
		}
		JSONObject aliases = null;
		try(InputStream is = SciamlabStreamUtils.getInputStream(alias_file)){
			aliases = new JSONObject(SciamlabStreamUtils.convertStreamToString(is));
		}catch(FileNotFoundException e){
			logger.warn("Provided alias file "+alias_file+" not available for vocabulary "+name());
		} catch (IOException e) {
			throw new SciamlabException(e);
		}
		return load(aliases);
	}
	
	public <E extends EUNamedAuthorityEntry> EUNamedAuthorityVocabularyMap<E> load(JSONObject aliases) throws SciamlabException {
		//check if already loaded
		if(EUNamedAuthorityVocabulary.VOCABULARY_CACHE_MAP.containsKey(this))
			return (EUNamedAuthorityVocabularyMap<E>) EUNamedAuthorityVocabulary.VOCABULARY_CACHE_MAP.get(this);
		
		EUNamedAuthorityVocabularyMap<E> map = new EUNamedAuthorityVocabularyMap<E>();
		this.entryMap = map;
		try{
			List<Element> xml = new ArrayList<Element>();
			try(InputStream cached_is = SciamlabStreamUtils.getInputStream(file)){
				xml = XMLLoader.load(cached_is, "record");
			}catch(FileNotFoundException e){
				logger.warn("Local cache of "+file+" not found. Loading remotely...");
				try(InputStream remote_is = SciamlabStreamUtils.getRemoteInputStream(url.toString())){
					xml = XMLLoader.load(remote_is, "record");
				} 
			}
			for(Element record : xml){
				if(Boolean.parseBoolean(record.getAttribute("deprecated")))
					continue;
				E entry = (E) builder.getDeclaredConstructor(Element.class).newInstance(new Object[] {record}).build();
				
				//loading aliases from json, if any
				if(aliases!=null){
					JSONObject json = aliases.optJSONObject(entry.authority_code);
					if(json!=null)
						entry.setAliases(SciamlabCollectionUtils.asStringList(json.optJSONArray("alias")));
				}
				
				map.put(entry.authority_code, entry);
			}
		}catch(IOException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | URISyntaxException e){
			throw new SciamlabException(e);
		}
		
		//adding to loaded vocabularies
		EUNamedAuthorityVocabulary.VOCABULARY_CACHE_MAP.put(this, map);
		return map;
	}
	
}
