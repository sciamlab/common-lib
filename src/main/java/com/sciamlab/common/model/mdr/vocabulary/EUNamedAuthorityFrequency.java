package com.sciamlab.common.model.mdr.vocabulary;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.Map;

import org.json.JSONObject;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sciamlab.common.exception.SciamlabException;
import com.sciamlab.common.model.mdr.EUNamedAuthorityEntry;
import com.sciamlab.common.model.mdr.EUNamedAuthorityVocabulary;

public class EUNamedAuthorityFrequency extends EUNamedAuthorityEntry {

	public final URI sameAs;
	public final String description;
	
	
	private EUNamedAuthorityFrequency(String authority_code, Date start_use, Map<String, String> labels
			, URI sameAs, String description) throws URISyntaxException {
		super(EUNamedAuthorityVocabulary.FREQUENCY, authority_code, start_use, labels);
		this.sameAs = sameAs;
		this.description = description;
	}
	
	@Override
	public String toJSONString(){
		return new JSONObject(super.toJSONString())
				.put("sameAs", sameAs)
				.put("description", description).toString();
	}
	
	public static class Builder extends EUNamedAuthorityEntry.Builder{
		private URI sameAs;
		private String description;
		
		public Builder(String authority_code){
			super(EUNamedAuthorityVocabulary.FREQUENCY, authority_code);
		}
		
		public Builder(Element record) throws SciamlabException {
			super(EUNamedAuthorityVocabulary.FREQUENCY, record);
			if(record.getElementsByTagName("sameAs").getLength()>0)
				try {
					this.sameAs = new URI(record.getElementsByTagName("sameAs").item(0).getFirstChild().getNodeValue());
				} catch (DOMException | URISyntaxException e) {
					throw new SciamlabException(e);
				}
			//english description
			NodeList nl = record.getElementsByTagName("source");
		    if (nl != null && nl.getLength() > 0) {
		        for (int i = 0; i < nl.getLength(); i++) {
		            if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
		                Element el = (Element) nl.item(i);
		                if ("eng".equals(el.getAttribute("lg"))) {
		                	this.description = el.getElementsByTagName("description").item(0).getTextContent();
		                	break;
		                }
		            }
		        }
		    }
		}
		
		public Builder sameAs(URI sameAs){
			this.sameAs = sameAs;
			return this;
		}
		public Builder description(String description){
			this.description = description;
			return this;
		}
		public EUNamedAuthorityFrequency build() throws URISyntaxException{
			return new EUNamedAuthorityFrequency(authority_code, start_use, labels, sameAs, description);
		}
	}
	
}
