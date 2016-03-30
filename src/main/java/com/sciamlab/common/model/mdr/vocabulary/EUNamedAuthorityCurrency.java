package com.sciamlab.common.model.mdr.vocabulary;

import java.net.URISyntaxException;
import java.util.Date;
import java.util.Map;

import org.w3c.dom.Element;

import com.sciamlab.common.exception.SciamlabException;
import com.sciamlab.common.model.mdr.EUNamedAuthorityEntry;
import com.sciamlab.common.model.mdr.EUNamedAuthorityVocabulary;

public class EUNamedAuthorityCurrency extends EUNamedAuthorityEntry {
	
	private EUNamedAuthorityCurrency(String authority_code, Date start_use, Map<String, String> labels) throws URISyntaxException {
		super(EUNamedAuthorityVocabulary.CURRENCY, authority_code, start_use, labels);
	}

	public static class Builder extends EUNamedAuthorityEntry.Builder{
		
		public Builder(String authority_code){
			super(EUNamedAuthorityVocabulary.CURRENCY, authority_code);
		}
		
		public Builder(Element record) throws SciamlabException {
			super(EUNamedAuthorityVocabulary.CURRENCY, record);
		}
		
		public EUNamedAuthorityCurrency build() throws URISyntaxException{
			return new EUNamedAuthorityCurrency(authority_code, start_use, labels);
		}
	}

}
