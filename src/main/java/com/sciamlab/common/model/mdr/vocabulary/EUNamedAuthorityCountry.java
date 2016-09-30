package com.sciamlab.common.model.mdr.vocabulary;

import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sciamlab.common.exception.SciamlabException;
import com.sciamlab.common.model.mdr.EUNamedAuthorityEntry;
import com.sciamlab.common.model.mdr.EUNamedAuthorityVocabulary;
import com.sciamlab.common.util.SciamlabDateUtils;

public class EUNamedAuthorityCountry extends EUNamedAuthorityEntry {

	public final String classification;
	public final Boolean euro;
	public final Date euro_date;
	public final String continent;
 	public final Set<String> languages;
	public final Map<String, String> long_labels;
	
//	private EUNamedAuthorityCountry(String authority_code, Date start_use, Map<String, String> labels
//			, String classification, Boolean euro, Date euro_date, String continent, Set<String> languages, Map<String, String> long_labels) throws URISyntaxException {
//		super(EUNamedAuthorityVocabulary.COUNTRY, authority_code, start_use, labels);
//		this.classification = classification;
//		this.euro = euro;
//		this.euro_date = euro_date;
//		this.continent = continent;
//		this.languages = languages;
//		this.long_labels = long_labels;
//	}
	private EUNamedAuthorityCountry(Builder builder) {
		super(builder);
		this.classification = builder.classification;
		this.euro = builder.euro;
		this.euro_date = builder.euro_date;
		this.continent = builder.continent;
		this.languages = builder.languages;
		this.long_labels = builder.long_labels;
	}

	@Override
	public String toJSONString(){
		return new JSONObject(super.toJSONString())
				.put("classification", classification)
				.put("euro", euro)
				.put("euro-date", euro_date!=null ? SciamlabDateUtils.getDateAsIso8061String(euro_date) : JSONObject.NULL)
				.put("continent", continent)
				.put("long-labels", new JSONObject(long_labels))
				.put("languages", new JSONArray(languages)).toString();
	}
	
	public static class Builder extends EUNamedAuthorityEntry.Builder{
		public String classification;
		public Boolean euro = false;
		public Date euro_date = null;
		public String continent;
	 	public Set<String> languages = new HashSet<String>();
		public Map<String, String> long_labels = new HashMap<String, String>();
		
		public Builder(String authority_code) throws SciamlabException{
			super(EUNamedAuthorityVocabulary.COUNTRY, authority_code);
		}
		
		public Builder(Element record) throws SciamlabException {
			super(EUNamedAuthorityVocabulary.COUNTRY, record);
			//country.membership
			Element country_membership = (Element)record.getElementsByTagName("country.membership").item(0);
			this.classification = country_membership.getAttribute("country.classification");
			this.euro = Boolean.parseBoolean(country_membership.getAttribute("currency.euro"));
			if(this.euro){
				try {
					this.euro_date = new SimpleDateFormat("yyyy-MM-dd").parse(((Element)country_membership.getElementsByTagName("currency.euro.date").item(0)).getTextContent());
				} catch (DOMException | ParseException e) {
					throw new SciamlabException(e);
				}
				this.continent = ((Element)country_membership.getElementsByTagName("linkCONTcode").item(0)).getTextContent();
			}
			//official.languages
			NodeList nl_langs = ((Element)record.getElementsByTagName("official.languages").item(0)).getElementsByTagName("code");
			if (nl_langs != null && nl_langs.getLength() > 0) {
		        for (int i = 0; i < nl_langs.getLength(); i++) {
		            if (nl_langs.item(i).getNodeType() == Node.ELEMENT_NODE) {
		                Element el = (Element) nl_langs.item(i);
		                String lang = el.getTextContent();
	                	languages.add(lang);
		            }
		        }
		    }
			//long.labels
			NodeList nl = ((Element)record.getElementsByTagName("long.label").item(0)).getElementsByTagName("lg.version");
			if (nl != null && nl.getLength() > 0) {
		        for (int i = 0; i < nl.getLength(); i++) {
		            if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
		                Element el = (Element) nl.item(i);
		                String lang = el.getAttribute("lg");
		                if(!long_labels.containsKey(lang))
		                	long_labels.put(lang, el.getTextContent());
		            }
		        }
		    }
		}
		
		public Builder classification(String classification){
			this.classification = classification;
			return this;
		}
		public Builder euro(Boolean euro, Date euro_date){
			this.euro = euro;
			this.euro_date = euro_date;
			return this;
		}
		public Builder continent(String continent){
			this.continent = continent;
			return this;
		}
		public Builder language(String lang){
			this.languages.add(lang);
			return this;
		}
		public Builder long_label(String lang, String long_label){
			this.long_labels.put(lang, long_label);
			return this;
		}
		
		public EUNamedAuthorityCountry build() throws URISyntaxException{
//			return new EUNamedAuthorityCountry(authority_code, start_use, labels, classification, euro, euro_date, continent, languages, long_labels);
			return new EUNamedAuthorityCountry(this);
		}
	}

}
