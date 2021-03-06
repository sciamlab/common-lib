package com.sciamlab.common.model.mdr;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONString;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sciamlab.common.exception.SciamlabException;
import com.sciamlab.common.util.SciamlabDateUtils;

public class EUNamedAuthorityEntry implements JSONString, Serializable {

	private static final long serialVersionUID = 3670754798696363485L;

	public final String authority_code;
//	public volatile EUNamedAuthorityVocabulary vocabulary;
	public final URI uri;
	public final Date start_use;
	public final Map<String, String> labels;
//	private final Set<String> alias = new HashSet<String>();
	
//	public Set<String> getAliases(){
//		return alias;
//	}
//
//	public EUNamedAuthorityEntry setAlias(String alias) {
//		this.alias.add(alias.toLowerCase().trim());
////		vocabulary.setAlias(this, alias);
//		return this;
//	}
//
//	public EUNamedAuthorityEntry setAliases(Collection<String> aliases) {
//		for (String a : aliases)
//			this.setAlias(a);
//		return this;
//	}

//	protected EUNamedAuthorityEntry(EUNamedAuthorityVocabulary vocabulary, String authority_code, Date start_use, Map<String, String> labels) throws URISyntaxException {
//		super();
//		this.vocabulary = vocabulary;
//		this.authority_code = authority_code;
//		this.setAlias(authority_code);
//		this.uri = new URI(vocabulary.uri() + "/" + authority_code);
//		this.start_use = start_use;
//		this.labels = labels;
//		this.setAliases(labels.values());
//	}
	protected EUNamedAuthorityEntry(Builder builder) {//throws URISyntaxException {
		super();
//		this.vocabulary = builder.vocabulary;
		this.authority_code = builder.authority_code;
		this.uri = builder.uri;
		this.start_use = builder.start_use;
		this.labels = builder.labels;
		
//		this.setAlias(authority_code);
//		this.setAliases(labels.values());
//		builder.vocabulary.setAlias(this, authority_code);
//		builder.vocabulary.setAliases(this, labels.values());
	}
	
	@Override
	public String toJSONString() {
		return new JSONObject().put("authority_code", authority_code)
				.put("uri", uri.toString())
				.put("start-use", SciamlabDateUtils.getDateAsIso8061String(start_use))
				.put("labels", new JSONObject(labels))
//				.put("alias", new JSONArray(alias))
				.toString();
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

	public static class Builder {
		protected final String authority_code;
		protected final EUNamedAuthorityVocabulary vocabulary;
		protected final URI uri;
		protected Date start_use;
		protected Map<String, String> labels = new HashMap<String, String>();

		public Builder(EUNamedAuthorityVocabulary vocabulary, String authority_code) throws SciamlabException {
			this.authority_code = authority_code;
			this.vocabulary = vocabulary;
			try {
				this.uri = new URI(vocabulary.uri() + "/" + authority_code);
			} catch (URISyntaxException e) {
				throw new SciamlabException(e);
			}
		}
		
		public Builder(EUNamedAuthorityVocabulary vocabulary, Element record) throws SciamlabException {
			this(vocabulary, record.getElementsByTagName("authority-code").item(0).getFirstChild().getNodeValue());
			try {
				this.start_use = new SimpleDateFormat("yyyy-MM-dd").parse(record.getElementsByTagName("start.use").item(0).getFirstChild().getNodeValue());
			} catch (DOMException | ParseException e) {
				throw new SciamlabException(e);
			}
			NodeList nl = ((Element)record.getElementsByTagName("label").item(0)).getElementsByTagName("lg.version");
			if (nl != null && nl.getLength() > 0) {
		        for (int i = 0; i < nl.getLength(); i++) {
		            if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
		                Element el = (Element) nl.item(i);
		                String lang = el.getAttribute("lg");
		                if(!labels.containsKey(lang))
		                	labels.put(lang, el.getTextContent());
		            }
		        }
		    }
		}

		public Builder start_use(Date start_use) {
			this.start_use = start_use;
			return this;
		}

		public Builder label(String lang, String label) {
			this.labels.put(lang, label);
			return this;
		}

		public EUNamedAuthorityEntry build() throws URISyntaxException {
//			return new EUNamedAuthorityEntry(vocabulary, authority_code, start_use, labels);
			return new EUNamedAuthorityEntry(this);
		}
	}

}
