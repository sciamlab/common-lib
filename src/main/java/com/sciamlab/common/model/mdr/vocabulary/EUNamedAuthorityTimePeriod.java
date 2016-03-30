package com.sciamlab.common.model.mdr.vocabulary;

import java.net.URISyntaxException;
import java.util.Date;
import java.util.Map;

import org.json.JSONObject;
import org.w3c.dom.Element;

import com.sciamlab.common.exception.SciamlabException;
import com.sciamlab.common.model.mdr.EUNamedAuthorityEntry;
import com.sciamlab.common.model.mdr.EUNamedAuthorityVocabulary;

public class EUNamedAuthorityTimePeriod extends EUNamedAuthorityEntry {

	public final String period_qualifier;
	
	private EUNamedAuthorityTimePeriod(String authority_code, Date start_use, Map<String, String> labels
			, String period_qualifier) throws URISyntaxException {
		super(EUNamedAuthorityVocabulary.TIMEPERIOD, authority_code, start_use, labels);
		this.period_qualifier = period_qualifier;
	}

	@Override
	public String toJSONString(){
		return new JSONObject(super.toJSONString())
				.put("period-qualifier", period_qualifier).toString();
	}
	
	public static class Builder extends EUNamedAuthorityEntry.Builder{
		public String period_qualifier;
		
		public Builder(String authority_code){
			super(EUNamedAuthorityVocabulary.TIMEPERIOD, authority_code);
		}
		
		public Builder(Element record) throws SciamlabException {
			super(EUNamedAuthorityVocabulary.TIMEPERIOD, record);
			if(record.getElementsByTagName("period.qualifier").getLength()>0)
				this.period_qualifier = record.getElementsByTagName("period.qualifier").item(0).getFirstChild().getNodeValue();
		}
		
		public Builder period_qualifier(String period_qualifier){
			this.period_qualifier = period_qualifier;
			return this;
		}
		
		public EUNamedAuthorityTimePeriod build() throws URISyntaxException{
			return new EUNamedAuthorityTimePeriod(authority_code, start_use, labels, period_qualifier);
		}
	}
}
