package com.sciamlab.common.model.datacatalog;

import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.sciamlab.common.model.GenericSearchChannel;
import com.sciamlab.common.model.newsfeed.Editor;
import com.sciamlab.common.model.newsfeed.Aggregator.AggregatorBuilder;
import com.sciamlab.common.model.util.GenericBuilder;

/**
 * the name corresponds to an opendata portal 
 * @author sciamlab
 *
 */
public class DataCatalog implements GenericSearchChannel<Dataset, DataPublisher> {
	
	private static final Logger logger = Logger.getLogger(DataCatalog.class);

	private final String title;
	private final String description;
	private final Date issued;
	private final Date modified;
	private final String language;
	private final URL homepage;
	private final String themeTaxonomy;
	private final OpenDataLicense license;
	private final String rights;
	//GEOJSON
	private final JSONObject spatial;
	private final Map<String, Dataset> datasets;
	private final Map<String, DataPublisher> publishers = new HashMap<String, DataPublisher>();
	
	private final String identifier;
	private final URL baseURL;
	private final URL catalogURL;
	
	private DataCatalog(DataCatalogBuilder builder){
		this.identifier = builder.identifier;
		this.title = builder.title;
		this.description = builder.description;
		this.issued = builder.issued;
		this.modified = builder.modified;
		this.language = builder.language;
		this.homepage = builder.homepage;
		this.themeTaxonomy = builder.themeTaxonomy;
		this.license = builder.license;
		this.rights = builder.rights;
		this.spatial = builder.spatial;
		this.datasets = new HashMap<String, Dataset>();
		this.baseURL = builder.baseURL;
		this.catalogURL = builder.catalogURL;
		this.publishers.putAll(builder.publishers);
	}
	
	/*
	 * DATASET
	 */
	public void addDataset(Dataset dataset){
		this.datasets.put(dataset.getIdentifier(), dataset);
	}
	public Dataset getDataset(String identifier){
		return this.datasets.get(identifier);
	}
	public boolean hasDataset(String identifier){
		return this.datasets.containsKey(identifier);
	}
	public Map<String, Dataset> getDatasets() {
		return datasets;
	}
	@Override
	public void addSearchItem(Dataset dataset){
		addDataset(dataset);
	}
	@Override
	public Dataset getSearchItem(String id){
		return getDataset(id);
	}
	@Override
	public boolean hasSearchItem(String id){
		return hasDataset(id);
	}
	@Override
	public Map<String, Dataset> getSearchItems() {
		return getDatasets();
	}
	/*
	 * DATA PUBLISHER
	 */
	@Override
	public void addPublisher(DataPublisher publisher){
		this.publishers.put(publisher.getIdentifier(), publisher);
	}
	@Override
	public boolean hasPublisher(String identifier){
		return this.publishers.containsKey(identifier);
	}
	@Override
	public DataPublisher getPublisher(String identifier){
		return this.publishers.get(identifier);
	}
	@Override
	public Map<String, DataPublisher> getPublishers() {
		return publishers;
	}

	
	public String getIdentifier() {
		return identifier;
	}
	@Override
	public String getId() {
		return identifier;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public Date getIssued() {
		return issued;
	}

	public Date getModified() {
		return modified;
	}

	public String getLanguage() {
		return language;
	}

	public URL getHomepage() {
		return homepage;
	}

	public String getThemeTaxonomy() {
		return themeTaxonomy;
	}

	public OpenDataLicense getLicense() {
		return license;
	}

	public String getRights() {
		return rights;
	}

	public JSONObject getSpatial() {
		return spatial;
	}

	public URL getBaseURL() {
		return baseURL;
	}

	public URL getCatalogURL() {
		return catalogURL;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((identifier == null) ? 0 : identifier.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DataCatalog other = (DataCatalog) obj;
		if (identifier == null) {
			if (other.identifier != null)
				return false;
		} else if (!identifier.equals(other.identifier))
			return false;
		return true;
	}

	public static class DataCatalogBuilder extends GenericBuilder{
		
		private final String identifier;
		private String title;
		private String description;
		private Date issued;
		private Date modified;
		private String language;
		private URL homepage;
		private String themeTaxonomy;
		private OpenDataLicense license;
		private String rights;
		private JSONObject spatial;
		private final Map<String, DataPublisher> publishers = new HashMap<String, DataPublisher>();

		private final URL baseURL;
		private final URL catalogURL;
		
		public static DataCatalogBuilder init(String identifier, URL baseURL, URL catalogURL){
			return new DataCatalogBuilder(identifier, baseURL, catalogURL);
		}
		
		private DataCatalogBuilder(String identifier, URL baseURL, URL catalogURL){
			this.identifier = identifier;
			this.baseURL = baseURL;
			this.catalogURL = catalogURL;
		}
		
		public DataCatalogBuilder title(String title){
			this.title = title;
			return this;
		}
		public DataCatalogBuilder description(String description){
			this.description = description;
			return this;
		}
		public DataCatalogBuilder issued(Date issued){
			this.issued = issued;
			return this;
		}
		public DataCatalogBuilder modified(Date modified){
			this.modified = modified;
			return this;
		}
		public DataCatalogBuilder language(String language){
			this.language = language;
			return this;
		}
		public DataCatalogBuilder homepage(URL homepage){
			this.homepage = homepage;
			return this;
		}
		public DataCatalogBuilder themeTaxonomy(String themeTaxonomy){
			this.themeTaxonomy = themeTaxonomy;
			return this;
		}
		public DataCatalogBuilder license(OpenDataLicense license){
			this.license = license;
			return this;
		}
		public DataCatalogBuilder rights(String rights){
			this.rights = rights;
			return this;
		}
		public DataCatalogBuilder spatial(JSONObject spatial){
			this.spatial = spatial;
			return this;
		}
		public DataCatalogBuilder publisher(DataPublisher publisher){
			this.publishers.put(publisher.getId(), publisher);
			return this;
		}
		public DataCatalog build(){
			return new DataCatalog(this);
		}
	}

}
