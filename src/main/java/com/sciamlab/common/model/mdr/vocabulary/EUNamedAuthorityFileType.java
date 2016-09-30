package com.sciamlab.common.model.mdr.vocabulary;

import java.net.URISyntaxException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sciamlab.common.exception.SciamlabException;
import com.sciamlab.common.model.mdr.EUNamedAuthorityEntry;
import com.sciamlab.common.model.mdr.EUNamedAuthorityVocabulary;

public class EUNamedAuthorityFileType extends EUNamedAuthorityEntry {
	
	/*
	 * i seguenti formati non sono coperti: 
	 * "DXF-DWG": { "alias": ["I QUADRANTI .DXF", "dxf - dwg", "dwg - dxf", "DWG", "DXF"] },
	 * "BLENDER": {	"alias": ["BLENDER V.2.49B"] },
	 * ****IMPORTANTI***** "WCS": { "alias": [] }, "WCF": { "alias": [] }, "WFS": { "alias": ["wfs/shp"] }, "WMS": { "alias": [] }, 
	 * "GPX": { "alias": ["ZIP:GPX", "zip (gpx)"] },
	 * "SBN": { "alias": [] },
	 * "OAI-PMH": { "alias": [] },
	 * "ICAL": { "alias": [] },
	 * "SBX": { "alias": [] },
	 * "LZMA": { "alias": [] },
	 * "WEBGIS": { "alias": [] },
	 * "IMAGE": { "alias": ["IMAGINE Image"] },
	 * "OTHER": { "alias": ["Sottoformati vari", "file", "php", "altro", "n/a", "", "service", "application/x-msdos-program", "Public Folder"] }
	 */
	
	public final String internet_media_type;
	public final String file_extension;
	public final Map<String, String> long_labels;
	
//	private EUNamedAuthorityFileType(String authority_code, Date start_use, Map<String, String> labels
//			, String internet_media_type, String file_extension, Map<String, String> long_labels) throws URISyntaxException {
//		super(EUNamedAuthorityVocabulary.FILE_TYPE, authority_code, start_use, labels);
//		this.internet_media_type = internet_media_type;
//		this.file_extension = file_extension;
//		this.long_labels = long_labels;
//	}
	private EUNamedAuthorityFileType(Builder builder) {
		super(builder);
		this.internet_media_type = builder.internet_media_type;
		this.file_extension = builder.file_extension;
		this.long_labels = builder.long_labels;
	}

	@Override
	public String toJSONString(){
		return new JSONObject(super.toJSONString())
				.put("internet_media_type", internet_media_type)
				.put("file_extension", file_extension)
				.put("long-labels", new JSONObject(long_labels)).toString();
	}
	
	public static class Builder extends EUNamedAuthorityEntry.Builder{
		public String internet_media_type;
		public String file_extension;
		public Map<String, String> long_labels = new HashMap<String, String>();
		
		public Builder(String authority_code) throws SciamlabException{
			super(EUNamedAuthorityVocabulary.FILE_TYPE, authority_code);
		}
		
		public Builder(Element record) throws SciamlabException {
			super(EUNamedAuthorityVocabulary.FILE_TYPE, record);
			this.internet_media_type = record.getElementsByTagName("internet-media-type").item(0).getFirstChild().getNodeValue();
			if(record.getElementsByTagName("file-extension").getLength()>0)
				this.file_extension = record.getElementsByTagName("file-extension").item(0).getFirstChild().getNodeValue();
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
		
		public Builder internet_media_type(String internet_media_type){
			this.internet_media_type = internet_media_type;
			return this;
		}
		public Builder file_extension(String file_extension){
			this.file_extension = file_extension;
			return this;
		}
		public Builder long_label(String lang, String long_label){
			this.long_labels.put(lang, long_label);
			return this;
		}
		
		public EUNamedAuthorityFileType build() throws URISyntaxException{
//			return new EUNamedAuthorityFileType(authority_code, start_use, labels, internet_media_type, file_extension, long_labels);
			return new EUNamedAuthorityFileType(this);
		}
	}

}
