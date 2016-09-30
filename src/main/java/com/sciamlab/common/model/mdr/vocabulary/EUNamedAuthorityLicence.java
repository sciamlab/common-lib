package com.sciamlab.common.model.mdr.vocabulary;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sciamlab.common.exception.SciamlabException;
import com.sciamlab.common.model.mdr.EUNamedAuthorityEntry;
import com.sciamlab.common.model.mdr.EUNamedAuthorityVocabulary;

public class EUNamedAuthorityLicence extends EUNamedAuthorityEntry {
	
	public final URI exactMatch;
	public final String classification;
	public final String version;
	public final Map<String, String> acronyms;
	
//	private EUNamedAuthorityLicence(String authority_code, Date start_use, Map<String, String> labels
//			, URI exactMatch, String classification, String version, Map<String, String> acronyms) throws URISyntaxException {
//		super(EUNamedAuthorityVocabulary.LICENCE, authority_code, start_use, labels);
//		this.exactMatch = exactMatch;
//		this.classification = classification;
//		this.version = version;
//		this.acronyms = acronyms;
//	}
	private EUNamedAuthorityLicence(Builder builder) {
		super(builder);
		this.exactMatch = builder.exactMatch;
		this.classification = builder.classification;
		this.version = builder.version;
		this.acronyms = builder.acronyms;
	}

	@Override
	public String toJSONString(){
		return new JSONObject(super.toJSONString())
				.put("exactMatch", exactMatch)
				.put("classification", classification)
				.put("version", version)
				.put("acronyms", new JSONObject(acronyms)).toString();
	}
	
	public static class Builder extends EUNamedAuthorityEntry.Builder{
		public String version;
		public String classification;
		public URI exactMatch;
		public Map<String, String> acronyms = new HashMap<String, String>();
		
		public Builder(String authority_code) throws SciamlabException{
			super(EUNamedAuthorityVocabulary.LICENCE, authority_code);
		}
		
		public Builder(Element record) throws SciamlabException {
			super(EUNamedAuthorityVocabulary.LICENCE, record);
			if(record.getElementsByTagName("exactMatch").getLength()>0){
				try {
					this.exactMatch = new URI(record.getElementsByTagName("exactMatch").item(0).getFirstChild().getNodeValue());
				} catch (DOMException | URISyntaxException e) {
					throw new SciamlabException(e);
				}
			}
			if(record.getElementsByTagName("version").getLength()>0)
				this.version = record.getElementsByTagName("version").item(0).getFirstChild().getNodeValue();
			this.classification = record.getElementsByTagName("classification").item(0).getFirstChild().getNodeValue();
			//acronyms
			if(record.getElementsByTagName("acronym").getLength()>0){
				NodeList nl = ((Element)record.getElementsByTagName("acronym").item(0)).getElementsByTagName("lg.version");
				if (nl != null && nl.getLength() > 0) {
			        for (int i = 0; i < nl.getLength(); i++) {
			            if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
			                Element el = (Element) nl.item(i);
			                String lang = el.getAttribute("lg");
			                acronyms.put(lang, el.getTextContent());
			            }
			        }
			    }
			}
		}
		
		public Builder exactMatch(URI exactMatch){
			this.exactMatch = exactMatch;
			return this;
		}
		public Builder classification(String classification){
			this.classification = classification;
			return this;
		}
		public Builder version(String version){
			this.version = version;
			return this;
		}
		public Builder acronym(String lang, String acronym){
			this.acronyms.put(lang, acronym);
			return this;
		}
		
		public EUNamedAuthorityLicence build() throws URISyntaxException{
//			return new EUNamedAuthorityLicence(authority_code, start_use, labels, exactMatch, classification, version, acronyms);
			return new EUNamedAuthorityLicence(this);
		}
	}


}
