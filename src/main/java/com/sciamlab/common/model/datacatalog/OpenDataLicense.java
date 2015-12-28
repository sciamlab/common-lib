package com.sciamlab.common.model.datacatalog;

import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONObject;

public enum OpenDataLicense{

	AGAINSTDRM("AGAINST-DRM"),
	IODL2("IODL2"),
	IODL1("IODL1"),
	CC_ZERO("CC-ZERO"),
	CC_BY("CC-BY"),
	CC_BY_SA("CC-BY-SA"),
	CC_BY_ND("CC-BY-ND"),
	CC_BY_NC("CC-BY-NC"),
	CC_BY_NC_SA("CC-BY-NC-SA"),
	CC_BY_NC_ND("CC-BY-NC-ND"),
	CC0_10("CC0-1.0"),
	CC_BY_30("CC-BY-3.0"),
	CC_BY_25("CC-BY-2.5"),
	CC_BY_40("CC-BY-4.0"),
	CC_BY_SA_30("CC-BY-SA-3.0"),
	CC_BY_SA_40("CC-BY-SA-4.0"),
	CC_BY_ND_30("CC-BY-ND-3.0"),
	CC_BY_ND_40("CC-BY-ND-4.0"),
	CC_BY_NC_30("CC-BY-NC-3.0"),
	CC_BY_NC_40("CC-BY-NC-4.0"),
	CC_BY_NC_SA_40("CC-BY-NC-SA-4.0"),
	CC_BY_NC_ND_30("CC-BY-NC-ND-3.0"),
	CC_BY_NC_ND_40("CC-BY-NC-ND-4.0"),
	ODBL_10("ODbL-1.0"),
	ODC_BY("ODC-BY"),
	ODC_PDDL_10("ODC-PDDL-1.0"),
	COPYRIGHT("COPYRIGHT"),
	NOT_SPEC("NOT-SPEC");
	
	private String id;
	private String title;
	private URL url;
	private Set<String> alias = new HashSet<String>();
	
	private Boolean domain_content;
	private Boolean domain_data;
	private Boolean domain_software;
	private String family;
	private Boolean is_generic;
	private String maintainer;
	private String od_conformance;
	private String osd_conformance;
	private Boolean is_okd_compliant;
	private Boolean is_osi_compliant;
	private String status;
	
	public static Map<String, OpenDataLicense> byId = new TreeMap<String, OpenDataLicense>();
	private static Map<String, OpenDataLicense> byAlias = new TreeMap<String, OpenDataLicense>();
	public static OpenDataLicense getByAlias(String alias){
		return byAlias.get(alias.replace("\r\n", " ").replace("\n", " ").replace("  ", " ").toLowerCase().trim());
	}
	
	private OpenDataLicense(String id){
		this.id = id;
	}
	
	public String id() { return id; }
	public String title() { return title; }
	public OpenDataLicense title(String title) { 
		this.title = title; 
		return this;
	}
	public URL url() { return url; }
	public OpenDataLicense url(URL url) { 
		this.url = url;
		return this;
	}
	
	public Boolean domain_content() { return domain_content;}
	public OpenDataLicense domain_content(Boolean domain_content) { 
		this.domain_content = domain_content; 
		return this;
	}
	public Boolean domain_data() { return domain_data;}
	public OpenDataLicense domain_data(Boolean domain_data) { 
		this.domain_data = domain_data; 
		return this;
	}
	public Boolean domain_software() { return domain_software;}
	public OpenDataLicense domain_software(Boolean domain_software) { 
		this.domain_software = domain_software; 
		return this;
	}
	public String family() { return family;}
	public OpenDataLicense family(String family) { 
		this.family = family; 
		return this;
	}
	public Boolean is_generic() { return is_generic;}
	public OpenDataLicense is_generic(Boolean is_generic) { 
		this.is_generic = is_generic; 
		return this;
	}
	public String maintainer() { return maintainer;}
	public OpenDataLicense maintainer(String maintainer) { 
		this.maintainer = maintainer; 
		return this;
	}
	public String od_conformance() { return od_conformance;}
	public OpenDataLicense od_conformance(String od_conformance) { 
		this.od_conformance = od_conformance; 
		return this;
	}
	public String osd_conformance() { return osd_conformance;}
	public OpenDataLicense osd_conformance(String osd_conformance) { 
		this.osd_conformance = osd_conformance; 
		return this;
	}
	public Boolean is_okd_compliant() { return is_okd_compliant;}
	public OpenDataLicense is_okd_compliant(Boolean is_okd_compliant) { 
		this.is_okd_compliant = is_okd_compliant; 
		return this;
	}
	public Boolean is_osi_compliant() { return is_osi_compliant;}
	public OpenDataLicense is_osi_compliant(Boolean is_osi_compliant) { 
		this.is_osi_compliant = is_osi_compliant; 
		return this;
	}
	public String status() { return status;}
	public OpenDataLicense status(String status) { 
		this.status = status; 
		return this;
	}
	
	public Set<String> alias() { return alias; }
	public Set<String> alias(String alias){
		this.alias.add(alias);
		this.alias.add(alias.toUpperCase());
		this.alias.add(alias.toLowerCase());
		byAlias.put(alias, this);
		byAlias.put(alias.toUpperCase(), this);
		byAlias.put(alias.toLowerCase(), this);
		return this.alias;
	}
	public Set<String>  alias(Collection<String> aliases){
		for(String alias : aliases){
			alias(alias);
		}
		return this.alias;
	}
	
	static{
    	for(OpenDataLicense license : OpenDataLicense.values()){
    		byId.put(license.id, license);
    		license.alias(license.id);
    		license.alias(license.id.toUpperCase());
    		license.alias(license.id.toLowerCase());
    	}
    }
	
    @Override 
    public String toString() { 
    	JSONObject json = new JSONObject();
    	json.put("id", id);
    	if(title!=null) json.put("title", title);
    	if(url!=null) json.put("url", url);
    	if(domain_content!=null) json.put("domain_content", domain_content);
    	if(domain_data!=null) json.put("domain_data", domain_data);
    	if(domain_software!=null) json.put("domain_software", domain_software);
    	if(family!=null) json.put("family", family);
    	if(is_generic!=null) json.put("is_generic", is_generic);
    	if(maintainer!=null) json.put("maintainer", maintainer);
    	if(od_conformance!=null) json.put("od_conformance", od_conformance);
    	if(osd_conformance!=null) json.put("osd_conformance", osd_conformance);
    	if(is_okd_compliant!=null) json.put("is_okd_compliant", is_okd_compliant);
    	if(is_osi_compliant!=null) json.put("is_osi_compliant", is_osi_compliant);
    	if(status!=null) json.put("status", status);
    	json.put("alias", new JSONArray().put(alias));
    	return json.toString();
    }
}
