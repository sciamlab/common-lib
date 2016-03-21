package com.sciamlab.common.model.mdr;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class EUNamedAuthorityFactory {
	
	private static Map<EUNamedAutorityVocabulary, EUNamedAuthorityVocabularyMap> VOCABULARY_MAP = new HashMap<EUNamedAutorityVocabulary, EUNamedAuthorityVocabularyMap>();

	public static EUNamedAuthorityVocabularyMap load(EUNamedAutorityVocabulary vocabulary) throws IOException, URISyntaxException{
		//check if already loaded
		if(VOCABULARY_MAP.containsKey(vocabulary))
			return VOCABULARY_MAP.get(vocabulary);
		
		EUNamedAuthorityVocabularyMap map = new EUNamedAuthorityVocabularyMap();
		Document xml = Jsoup.connect(vocabulary.url().toString()).timeout(10*1000).get();
		for(Element record : xml.select("record")){
			if(Boolean.parseBoolean(record.attr("deprecated")))
				continue;
			String code = record.select("authority-code").text();
			EUNamedAuthorityEntry.Builder entry = new EUNamedAuthorityEntry.Builder(vocabulary, code);
			if(record.select("sameAs").size()>0)
				entry.sameAs(new URI(record.select("sameAS").text()));
			if(record.select("exactMatch").size()>0)
				entry.exactMatch(new URI(record.select("exactMatch").text()));
			if(record.select("version").size()>0)
				entry.version(record.select("version").text());
			for(Element l : record.select("label").first().children()){
				if(l.hasAttr("gender.grammar") && !"M".equals(l.attr("gender.grammar")))
					continue;
				entry.label(Locale.forLanguageTag(l.attr("lg")), l.text());
			}
			entry.description(record.select("description").text());
			map.put(code, entry.build());
		}
		//adding to loaded vocabularies
		VOCABULARY_MAP.put(vocabulary, map);
		return map;
	}
	
	public static void main(String[] args) throws Exception {
		for(EUNamedAutorityVocabulary vocabulary : EUNamedAutorityVocabulary.values()){
			System.out.println("Vocabulary : "+vocabulary.toString());
			for(EUNamedAuthorityEntry entry : load(vocabulary).values()){
				System.out.println(entry);
			}
//			break;
		}
	}
}
