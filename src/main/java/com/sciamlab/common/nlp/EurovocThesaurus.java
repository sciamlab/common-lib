package com.sciamlab.common.nlp;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public final class EurovocThesaurus {
	
	public final Map<String, String> conceptMap;
//	private static final String DESCRIPTOR_FILE_PREFIX = "desc_";
//	private static final String DESCRIPTOR_TAG = "DESCRIPTEUR_ID";
	public final Map<String, String> domainMap;
	public final Map<String, List<String>> domainToMicroThesaurusMap = new HashMap<String, List<String>>();
	public final Map<String, List<String>> microThesaurusToDomainMap = new HashMap<String, List<String>>();

//	private static final String DOMAIN_FILE_PREFIX = "dom_";
//	private static final String DOMAIN_TAG = "DOMAINE_ID";
	public final Map<String, String> microThesaurusMap;
//	private static final String THESAURUS_FILE_PREFIX = "thes_";
//	private static final String THESAURUS__TAG = "THESAURUS_ID";
	public final Map<String, List<String>> narrowerToBroaderMap;
	public final Map<String, List<String>> broaderToNarrowerMap = new HashMap<String, List<String>>();
//	private static final String broaderTermsFile = "relation_bt.xml";
//	private static final String broaderTermsFirstTag = "SOURCE_ID";
//	private static final String broaderTermsSecondTag = "CIBLE_ID";
//	public final Map<String, List<String>> relatedMap;
//	private static final String relatedTermsFile = "relation_rt.xml";
//	private static final String relatedTermsFirstTag = "DESCRIPTEUR1_ID";
//	private static final String relatedTermsSecondTag = "DESCRIPTEUR2_ID";
	public final Map<String, List<String>> conceptToMicroThesaurusMap;
	public final Map<String, List<String>> microThesaurusToConceptMap = new HashMap<String, List<String>>();
//	private static final String microThesaurusFile = "desc_thes.xml";
//	private static final String microThesaurusFirstTag = "DESCRIPTEUR_ID";
//	private static final String microThesaurusSecondTag = "THESAURUS_ID";
	
	public EurovocThesaurus(Builder builder) {
		if (!(builder.dir.isDirectory())) {
			throw new RuntimeException("The file: " + builder.dir + "is not a directory");
		}
		this.domainMap = initialiseDescriptorMapping(new File(builder.dir, "dom_" + builder.lang + ".xml"), "DOMAINE_ID");

		this.conceptMap = initialiseDescriptorMapping(new File(builder.dir, "desc_" + builder.lang + ".xml"), "DESCRIPTEUR_ID");

		this.microThesaurusMap = initialiseDescriptorMapping(new File(builder.dir, "thes_" + builder.lang + ".xml"), "THESAURUS_ID");
		
		for(Entry<String, List<String>> d : domainToMicroThesaurusMap.entrySet()){
			for(String mt : d.getValue()){
				if(!microThesaurusToDomainMap.containsKey(mt))
					microThesaurusToDomainMap.put(mt, new ArrayList<String>());
				microThesaurusToDomainMap.get(mt).add(d.getKey());
			}
		}

		this.narrowerToBroaderMap = initialiseRelationMapping(new File(builder.dir, "relation_bt.xml"), "SOURCE_ID", "CIBLE_ID");
		for(Entry<String, List<String>> nt : narrowerToBroaderMap.entrySet()){
			for(String bt : nt.getValue()){
				if(!broaderToNarrowerMap.containsKey(bt))
					broaderToNarrowerMap.put(bt, new ArrayList<String>());
				broaderToNarrowerMap.get(bt).add(nt.getKey());
			}
		}

//		this.relatedMap = initialiseRelationMapping(new File(directory, "relation_rt.xml"), "DESCRIPTEUR1_ID", "DESCRIPTEUR2_ID");

		this.conceptToMicroThesaurusMap = initialiseRelationMapping(new File(builder.dir, "desc_thes.xml"), "DESCRIPTEUR_ID", "THESAURUS_ID");
		for(Entry<String, List<String>> c : conceptToMicroThesaurusMap.entrySet()){
			for(String mt : c.getValue()){
				if(!microThesaurusToConceptMap.containsKey(mt))
					microThesaurusToConceptMap.put(mt, new ArrayList<String>());
				microThesaurusToConceptMap.get(mt).add(c.getKey());
			}
		}
	}

	private Map<String, List<String>> initialiseRelationMapping(File file, String firstTag, String secondTag) {
		Map<String, List<String>> result = new HashMap<String, List<String>>();
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.parse(file);
			NodeList records = doc.getElementsByTagName("RECORD");

			for (int i = 0; i < records.getLength(); ++i) {
				Element record = (Element) records.item(i);
				String term = record.getElementsByTagName(firstTag).item(0).getFirstChild().getNodeValue();
				String related = record.getElementsByTagName(secondTag).item(0).getFirstChild().getNodeValue();
				if ((term == null) || (term.trim().isEmpty()) || (related == null) || (related.trim().isEmpty()))
					continue;
				if (!(result.containsKey(term))) {
					result.put(term, new ArrayList<String>());
				}
				result.get(term).add(related);
//				if ("THESAURUS_ID".equals(secondTag)) {
////					this.tree.get(this.domainMap.get(related.substring(0, 2))).get(this.microThesaurusMap.get(related)).add(this.conceptMap.get(term));
//					this.tree.get(related.substring(0, 2)).get(related).add(term);
//				}
			}

		} catch (Exception e) {
			throw new RuntimeException("Could not initialize thesaurusInfo ", e);
		}
		return result;
	}

	private Map<String, String> initialiseDescriptorMapping(File file, String tag) {
		Map<String, String> result = new HashMap<String, String>();
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.parse(file);
			NodeList records = doc.getElementsByTagName("RECORD");

			for (int i = 0; i < records.getLength(); ++i) {
				Element record = (Element) records.item(i);
				String id = record.getElementsByTagName(tag).item(0).getFirstChild().getNodeValue();
				String label = record.getElementsByTagName("LIBELLE").item(0).getFirstChild().getNodeValue();
				result.put(id, label);

				if ("DOMAINE_ID".equals(tag)) {
//					this.tree.put(label, new HashMap<String, List<String>>());
					this.domainToMicroThesaurusMap.put(id, new ArrayList<String>());
				} else if ("THESAURUS_ID".equals(tag)) {
					String domainCode = id.substring(0, 2);
//					String domainName = (String) this.domainMap.get(domainCode);
//					this.tree.get(domainName).put(label, new ArrayList<String>());
					this.domainToMicroThesaurusMap.get(domainCode).add(id);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Could not initialize thesaurusInfo while parsing: " + file, e);
		}

		return result;
	}
	
	public static class Builder{
		private final File dir;
		private final String lang;
		
		public Builder(String dir_path, String lang) {
			this.dir = new File(dir_path);
			this.lang = lang;
		}

		public Builder(File dir, String lang) {
			this.dir = dir;
			this.lang = lang;
		}

		public EurovocThesaurus build(){
			return new EurovocThesaurus(this);
		}
	}

}