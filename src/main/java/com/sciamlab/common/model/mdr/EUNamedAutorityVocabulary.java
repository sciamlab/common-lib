package com.sciamlab.common.model.mdr;

import java.net.MalformedURLException;
import java.net.URL;

public enum EUNamedAutorityVocabulary {

	CURRENCY("currency", "http://publications.europa.eu/mdr/resource/authority/currency/xml/currencies.xml"),
	COUNTRY("country", "http://publications.europa.eu/mdr/resource/authority/country/xml/countries.xml"),
	DATA_THEME("data-theme", "http://publications.europa.eu/mdr/resource/authority/data-theme/xml/data-theme.xml"),
	FILE_TYPE("file-type", "http://publications.europa.eu/mdr/resource/authority/file-type/xml/filetypes.xml"),
	FREQUENCY("frequency", "http://publications.europa.eu/mdr/resource/authority/frequency/xml/frequencies.xml"),
	LANGUAGE("language", "http://publications.europa.eu/mdr/resource/authority/language/xml/languages.xml"),
	LICENCE("licence", "http://publications.europa.eu/mdr/resource/authority/licence/xml/licences.xml"),
	TIMEPERIOD("timeperiod", "http://publications.europa.eu/mdr/resource/authority/timeperiod/xml/timeperiods.xml");
	
	private String id;
	private URL url;
	
	private EUNamedAutorityVocabulary(String id, String url){
		this.id = id;
		try {
			this.url = new URL(url);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String id() { return id; }
	public URL url() { return url; }
}
