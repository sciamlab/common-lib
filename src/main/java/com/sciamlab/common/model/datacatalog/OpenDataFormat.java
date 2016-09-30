package com.sciamlab.common.model.datacatalog;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sciamlab.common.exception.OpenDataFormatException;
import com.sciamlab.common.exception.SciamlabException;
import com.sciamlab.common.model.mdr.EUNamedAuthorityVocabulary;
import com.sciamlab.common.model.mdr.EUNamedAuthorityVocabularyMap;
import com.sciamlab.common.model.mdr.vocabulary.EUNamedAuthorityFileType;

public enum OpenDataFormat {
	
//	JPG("JPG"),
//	TURTLE("TURTLE"),
//	N_TRIPLE("N-TRIPLE"),
//	SPARQL("SPARQL"),
//	OWL("OWL"),
//	RDF("RDF"),
//	PDF("PDF"),
//	HTML("HTML"),
//	XML("XML"),
//	ZIP_XML("ZIP:XML"),
//	PLAIN("PLAIN"),
//	CSW("CSW"),
//	XSD("XSD"),
//	XLS("XLS"),
//	ZIP_XLS("ZIP:XLS"),
//	CSV("CSV"),
//	TSV("TSV"),
//	ZIP_CSV("ZIP:CSV"),
//	SHP("SHP"),
//	ZIP_SHP("ZIP:SHP"),
//	KML("KML"),
//	KMZ("KMZ"),
//	ODC("ODC"),
//	GTFS("GTFS"),
//	DBF("DBF"),
//	SHX("SHX"),
//	GEOJSON("GEOJSON"),
//	JSON("JSON"),
//	JSON_LD("JSON-LD"),
//	MXD("MXD"),
//	ZIP("ZIP"),
//	TXT("TXT"),
//	ZIP_TXT("ZIP:TXT"),
//	DXF("DXF-DWG"),
//	ARCEXPLORER("ARCEXPLORER"),
//	ZIP_ODS("ZIP:ODS"),
//	ZIP_RDF("ZIP:RDF"),
//	ODS("ODS"),
//	ODT("ODT"),
//	TIFF("TIFF"),
//	PNG("PNG"),
//	BLENDER("BLENDER"),
//	GRID_ESRI("GRID-ESRI"),
//	ZIP_GRID("ZIP:GRID"),
//	WCS("WCS"),
//	WCF("WCF"),
//	WFS("WFS"),
//	WMS("WMS"),
//	DOC("DOC"),
//	GML("GML"),
//	MDB("MDB"),
//	ZIP_GPX("ZIP:GPX"),
//	GPX("GPX"),
//	SBN("SBN"),
//	OAI_PMH("OAI-PMH"),
//	ECW("ECW"),
//	ICAL("ICAL"),
//	TTL("TTL"),
//	SBX("SBX"),
//	RTF("RTF"),
//	LZMA("LZMA"),
//	WEBGIS("WEBGIS"),
//	IMAGE("IMAGE"),
//	ODATA_XML("ODATA-XML"),
//	ATOM("ATOM"),
//	OTHER("OTHER");
	
	JPEG,//("JPEG"),
	RDF_TURTLE,//("RDF_TURTLE"),
	RDF_N_TRIPLES,//("RDF_N_TRIPLES"),
	SPARQLQ,//("SPARQLQ"),
	OWL,//("OWL"),
	RDF,//("RDF"),
	PDF,//("PDF"),
	HTML,//("HTML"),
	XML,//("XML"),
	TXT,//("TXT"),
	SCHEMA_XML,//("SCHEMA_XML"),
	XLS,//("XLS"),
	CSV,//("CSV"),
	TSV,//("TSV"),
	SHP,//("SHP"),
	GRID_ASCII,//("GRID_ASCII"),
	KML,//("KML"),
	KMZ,//("KMZ"),
	ODC,//("ODC"),
	DBF,//("DBF"),
	JSON,//("JSON"),
	JSON_LD,//("JSON-LD"),
	MXD,//("MXD"),
	ZIP,//("ZIP"),
	MAP_PRVW,//("MAP_PRVW"),
	ODS,//("ODS"),
	ODT,//("ODT"),
	TIFF,//("TIFF"),
	PNG,//("PNG"),
	DOC,//("DOC"),
	GML,//("GML"),
	MDB,//("MDB"),
	ECW,//("ECW"),
	RTF,//("RTF"),
	ATOM,//("ATOM"),
	
	WCS,//("WCS"),
	WFS,//("WFS"),
	WMS;//("WMS");
	
//	private String id;
	private Set<String> alias = new HashSet<String>();;
	private EUNamedAuthorityFileType euNamedAuthorityFileType = null;
	
//	public static Map<String, OpenDataFormat> byId = new TreeMap<String, OpenDataFormat>();
	private static Map<String, OpenDataFormat> byAlias = new TreeMap<String, OpenDataFormat>();
	public static OpenDataFormat getByAlias(String alias){
		return byAlias.get(alias.toLowerCase().replace("\n", "").trim());
	}
	
	private OpenDataFormat(){//String id){
//		this.id = id;
//		this.euNamedAuthorityFileType = EUNamedAuthorityVocabularyMap.<EUNamedAuthorityFileType>get(EUNamedAuthorityVocabulary.FILE_TYPE).get(id);
		this.euNamedAuthorityFileType = EUNamedAuthorityVocabularyMap.<EUNamedAuthorityFileType>get(EUNamedAuthorityVocabulary.FILE_TYPE).get(this.name());
	}
	
//	public String id() { return id; }
	public EUNamedAuthorityFileType euNamedAuthorityFileType() { return euNamedAuthorityFileType; }
	public Set<String> alias() { return alias; }
	public Set<String> alias(String alias){
		byAlias.put(alias, this);
		byAlias.put(alias.toUpperCase(), this);
		byAlias.put(alias.toLowerCase(), this);
		return this.alias;
	}
	public Set<String> alias(Collection<String> aliases){
		for(String alias : aliases){
			alias(alias);
		}
		return this.alias;
	}
	
	static{
    	for(OpenDataFormat format : OpenDataFormat.values()){
//    		byId.put(format.id, format);
//    		format.alias(format.id);
//    		format.alias(format.id.toUpperCase());
//    		format.alias(format.id.toLowerCase());
    		format.alias(format.name());
    		format.alias(format.name().toUpperCase());
    		format.alias(format.name().toLowerCase());
    	}
    }
	
    @Override 
    public String toString() { 
    	JSONObject json = new JSONObject();
//    	json.put("id", this.id);
    	json.put("name", this.name());
    	json.put("alias", new JSONArray().put(alias));
    	return json.toString();
    }
    
}
