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

public enum OpenDataFormat {
	
	JPG("JPG"),
	TURTLE("TURTLE"),
	N_TRIPLE("N-TRIPLE"),
	SPARQL("SPARQL"),
	OWL("OWL"),
	RDF("RDF"),
	PDF("PDF"),
	HTML("HTML"),
	XML("XML"),
	ZIP_XML("ZIP:XML"),
	PLAIN("PLAIN"),
	CSW("CSW"),
	XSD("XSD"),
	XLS("XLS"),
	ZIP_XLS("ZIP:XLS"),
	CSV("CSV"),
	TSV("TSV"),
	ZIP_CSV("ZIP:CSV"),
	SHP("SHP"),
	ZIP_SHP("ZIP:SHP"),
	KML("KML"),
	KMZ("KMZ"),
	ODC("ODC"),
	GTFS("GTFS"),
	DBF("DBF"),
	SHX("SHX"),
	GEOJSON("GEOJSON"),
	JSON("JSON"),
	JSON_LD("JSON-LD"),
	MXD("MXD"),
	ZIP("ZIP"),
	TXT("TXT"),
	ZIP_TXT("ZIP:TXT"),
	DXF("DXF-DWG"),
	ARCEXPLORER("ARCEXPLORER"),
	ZIP_ODS("ZIP:ODS"),
	ZIP_RDF("ZIP:RDF"),
	ODS("ODS"),
	ODT("ODT"),
	TIFF("TIFF"),
	PNG("PNG"),
	BLENDER("BLENDER"),
	GRID_ESRI("GRID-ESRI"),
	ZIP_GRID("ZIP:GRID"),
	WCS("WCS"),
	WCF("WCF"),
	WFS("WFS"),
	WMS("WMS"),
	DOC("DOC"),
	GML("GML"),
	MDB("MDB"),
	ZIP_GPX("ZIP:GPX"),
	GPX("GPX"),
	SBN("SBN"),
	OAI_PMH("OAI-PMH"),
	ECW("ECW"),
	ICAL("ICAL"),
	TTL("TTL"),
	SBX("SBX"),
	RTF("RTF"),
	LZMA("LZMA"),
	WEBGIS("WEBGIS"),
	IMAGE("IMAGE"),
	ODATA_XML("ODATA-XML"),
	OTHER("OTHER");
	
	private String id;
	private Set<String> alias = new HashSet<String>();;
	
	public static Map<String, OpenDataFormat> byId = new TreeMap<String, OpenDataFormat>();
	private static Map<String, OpenDataFormat> byAlias = new TreeMap<String, OpenDataFormat>();
	public static OpenDataFormat getByAlias(String alias){
		return byAlias.get(alias.toLowerCase().replace("\n", "").trim());
	}
	
	private OpenDataFormat(String id){
		this.id = id;
	}
	
	public String id() { return id; }
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
    		byId.put(format.id, format);
    		format.alias(format.id);
    		format.alias(format.id.toUpperCase());
    		format.alias(format.id.toLowerCase());
    	}
    }
	
    @Override 
    public String toString() { 
    	JSONObject json = new JSONObject();
    	json.put("id", this.id);
    	json.put("alias", new JSONArray().put(alias));
    	return json.toString();
    }
    
    @Deprecated
    public static Set<OpenDataFormat> toOpenDataFormats(List<String> formats) throws OpenDataFormatException{
    	Set<OpenDataFormat> list = new HashSet<OpenDataFormat>();
    	for(String f : formats){
    		list.add(toOpenDataFormat(f));
    	}
    	return list;
    }
    
    @Deprecated
    public static OpenDataFormat toOpenDataFormat(String f) throws OpenDataFormatException{
		f = f.trim();
		if(!byAlias.containsKey(f)) {
			throw new OpenDataFormatException(f);
		}
		return OpenDataFormat.byAlias.get(f);
    }
    
    
}
