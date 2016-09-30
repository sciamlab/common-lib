package com.sciamlab.common.model.mdr;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import com.sciamlab.common.model.mdr.vocabulary.EUNamedAuthorityCountry;
import com.sciamlab.common.model.mdr.vocabulary.EUNamedAuthorityCurrency;
import com.sciamlab.common.model.mdr.vocabulary.EUNamedAuthorityDataTheme;
import com.sciamlab.common.model.mdr.vocabulary.EUNamedAuthorityFileType;
import com.sciamlab.common.model.mdr.vocabulary.EUNamedAuthorityFrequency;
import com.sciamlab.common.model.mdr.vocabulary.EUNamedAuthorityLanguage;
import com.sciamlab.common.model.mdr.vocabulary.EUNamedAuthorityLicence;
import com.sciamlab.common.model.mdr.vocabulary.EUNamedAuthorityTimePeriod;

public enum EUNamedAuthorityVocabulary{
	
	COUNTRY("country", "countries.xml", "alias_countries.json", 
//			EUNamedAuthorityCountry.class, 
			EUNamedAuthorityCountry.Builder.class),
	CURRENCY("currency", "currencies.xml", "alias_currencies.json", 
//			EUNamedAuthorityCurrency.class, 
			EUNamedAuthorityCurrency.Builder.class),
	DATA_THEME("data-theme", "data-theme.xml", "alias_data-theme.json", 
//			EUNamedAuthorityDataTheme.class, 
			EUNamedAuthorityDataTheme.Builder.class),
	FILE_TYPE("file-type", "filetypes.xml", "alias_filetypes.json", 
//			EUNamedAuthorityFileType.class, 
			EUNamedAuthorityFileType.Builder.class),
	FREQUENCY("frequency", "frequencies.xml", "alias_frequencies.json", 
//			EUNamedAuthorityFrequency.class, 
			EUNamedAuthorityFrequency.Builder.class),
	LANGUAGE("language", "languages.xml", "alias_languages.json", 
//			EUNamedAuthorityLanguage.class, 
			EUNamedAuthorityLanguage.Builder.class),
	LICENCE("licence", "licences.xml", "alias_licences.json", 
//			EUNamedAuthorityLicence.class, 
			EUNamedAuthorityLicence.Builder.class),
	TIMEPERIOD("timeperiod", "timeperiods.xml", "alias_timeperiods.json", 
//			EUNamedAuthorityTimePeriod.class, 
			EUNamedAuthorityTimePeriod.Builder.class);
	
	
	private final static String BASE_URI = "http://publications.europa.eu/resource/authority/";
	private final static String BASE_URL = "http://publications.europa.eu/mdr/resource/authority/";
	
	public final String id;
	public final URI uri;
	public final URL url;
	public final String file;
	public final String alias_file;
//	private Class<? extends EUNamedAuthorityEntry> clazz;
	public final Class<? extends EUNamedAuthorityEntry.Builder> builder;
	
	private EUNamedAuthorityVocabulary(String id, String file, String alias_file, 
//			Class<? extends EUNamedAuthorityEntry> clazz, 
			Class<? extends EUNamedAuthorityEntry.Builder> builder){
		this.id = id;
		this.alias_file = alias_file;
		this.file = file;
		try { this.uri = new URI(BASE_URI+id+"/"+id); 		} catch (URISyntaxException e) { throw new RuntimeException(e); }
		try { this.url = new URL(BASE_URL+id+"/xml/"+file); } catch (MalformedURLException e) { throw new RuntimeException(e); }
//		this.clazz = clazz;
		this.builder = builder;
	}
	
	public String id() { return id; }
	public URI uri() { return uri; }
	public String file() { return file; }
	public String alias_file() { return alias_file; }
}	