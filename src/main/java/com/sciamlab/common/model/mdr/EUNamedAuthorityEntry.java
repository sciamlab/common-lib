package com.sciamlab.common.model.mdr;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.json.JSONObject;
import org.json.JSONString;

public class EUNamedAuthorityEntry implements JSONString{
	
	public final String BASE_URI = "http://publications.europa.eu/resource/authority/";

	public final String authority_code;
	public final EUNamedAutorityVocabulary vocabulary;
	public final URI uri;
	public final URI sameAs;
	public final URI exactMatch;
	public final String description;
	public final String version;
	public final Map<Locale, String> labels;
	
	private EUNamedAuthorityEntry(EUNamedAutorityVocabulary vocabulary, String authority_code, URI sameAs, URI exactMatch
			, String description, String version, Map<Locale, String> labels) throws URISyntaxException {
		super();
		this.authority_code = authority_code;
		this.vocabulary = vocabulary;
		this.sameAs = sameAs;
		this.exactMatch = exactMatch;
		this.uri = new URI(BASE_URI+vocabulary.id()+"/"+authority_code);
		this.description = description;
		this.version = version;
		this.labels = labels;
	}

	@Override
	public String toJSONString(){
		return new JSONObject().put("authority_code", authority_code)
				.put("uri", uri.toString())
				.put("sameAs", sameAs!=null ? sameAs : JSONObject.NULL)
				.put("exactMatch", exactMatch!=null ? exactMatch : JSONObject.NULL)
				.put("description", description!=null ? description : JSONObject.NULL)
				.put("version", version!=null ? version : JSONObject.NULL)
				.put("labels", new JSONObject(labels)).toString();
	}
	
	@Override
	public String toString() {
		return toJSONString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uri == null) ? 0 : uri.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EUNamedAuthorityEntry other = (EUNamedAuthorityEntry) obj;
		if (uri == null) {
			if (other.uri != null)
				return false;
		} else if (!uri.equals(other.uri))
			return false;
		return true;
	}

	public static class Builder{
		private String authority_code;
		private EUNamedAutorityVocabulary vocabulary;
		private URI sameAs;
		private URI exactMatch;
		private String description;
		private String version;
		private Map<Locale, String> labels = new HashMap<Locale, String>();
		
		public Builder(EUNamedAutorityVocabulary vocabulary, String authority_code){
			this.authority_code = authority_code;
			this.vocabulary = vocabulary;
		}
		
		public Builder sameAs(URI sameAs){
			this.sameAs = sameAs;
			return this;
		}
		public Builder exactMatch(URI exactMatch){
			this.exactMatch = exactMatch;
			return this;
		}
		public Builder description(String description){
			this.description = description;
			return this;
		}
		public Builder version(String version){
			this.version = version;
			return this;
		}
		public Builder label(Locale lang, String label){
			this.labels.put(lang, label);
			return this;
		}
		
		public EUNamedAuthorityEntry build() throws URISyntaxException{
			return new EUNamedAuthorityEntry(vocabulary, authority_code, sameAs, exactMatch, description, version, labels);
		}
	}
	
}
