package com.sciamlab.common.model.mdr.vocabulary;

import java.net.URISyntaxException;
import java.util.Date;
import java.util.Map;

import org.json.JSONObject;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sciamlab.common.exception.SciamlabException;
import com.sciamlab.common.model.mdr.EUNamedAuthorityEntry;
import com.sciamlab.common.model.mdr.EUNamedAuthorityVocabulary;

public class EUNamedAuthorityLanguage extends EUNamedAuthorityEntry {

	public final String description;
	
	private EUNamedAuthorityLanguage(String authority_code, Date start_use, Map<String, String> labels
			, String description) throws URISyntaxException {
		super(EUNamedAuthorityVocabulary.LANGUAGE, authority_code, start_use, labels);
		this.description = description;
	}

	@Override
	public String toJSONString(){
		return new JSONObject(super.toJSONString())
				.put("description", description).toString();
	}
	
	public static class Builder extends EUNamedAuthorityEntry.Builder{
		public String description;

		public Builder(String authority_code){
			super(EUNamedAuthorityVocabulary.LANGUAGE, authority_code);
		}
		
		public Builder(Element record) throws SciamlabException {
			super(EUNamedAuthorityVocabulary.LANGUAGE, record);
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
		
		public Builder description(String description){
			this.description = description;
			return this;
		}
		public EUNamedAuthorityLanguage build() throws URISyntaxException{
			return new EUNamedAuthorityLanguage(authority_code, start_use, labels, description);
		}
	}

}
