package com.sciamlab.common.model.datacatalog;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.sciamlab.common.model.GenericPublisher;
import com.sciamlab.common.model.util.GenericBuilder;

/**
 * the publisher corresponds to a CKAN organization 
 * @author sciamlab
 *
 */
public class DataPublisher implements GenericPublisher<Dataset>{
	
	private static final Logger logger = Logger.getLogger(DataPublisher.class);
	
	private final String name;
	private final InternetAddress mbox;
	private final Map<String, DataCatalog> catalogs;
	private final Map<String, Dataset> datasets;
	
	private final String identifier;
	private URL image;
	private final String type;
	private final URL amacaURI;
	private URL spcURI;
	private URL dbpediaURI;
	private String description;
	private URL homepage;
	//GEOJSON
	private JSONObject spatial;
	
	private DataPublisher(DataPublisherBuilder builder){
		this.identifier = builder.identifier;
		this.name = builder.name;
		this.mbox = builder.mbox;
		this.catalogs = new HashMap<String, DataCatalog>();
		this.datasets = new HashMap<String, Dataset>();
		this.setImage(builder.image);
		this.type = builder.type;
		this.amacaURI = builder.amacaURI;
		this.setSpcURI(builder.spcURI);
		this.setDbpediaURI(builder.dbpediaURI);
		this.setDescription(builder.description);
		this.setHomepage(builder.homepage);
		this.setSpatial(builder.spatial);
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

	//TODO IMPORTANT: why commented?? CKANSerializerAdapter.class uses the getCatalogs() method and the list will be empty......!?!?
//	public void addCatalog(Aggregator catalog){
//		this.catalogs.put(catalog.getIdentifier(), catalog);
//		if(!catalog.hasPublisher(this.identifier))
//			catalog.addPublisher(this);
//	}
//	public Aggregator getCatalog(String identifier){
//		return this.catalogs.get(identifier);
//	}
//	public boolean hasCatalog(String identifier){
//		return this.catalogs.containsKey(identifier);
//	}
	
	public URL getHomepage() {
		return homepage;
	}

	public void setHomepage(URL homepage) {
		this.homepage = homepage;
	}
	
	public void setImage(URL image){
		this.image = image;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public URL getImage() {
		return image;
	}

	public String getType() {
		return type;
	}

	public URL getAmacaURI() {
		return amacaURI;
	}

	public URL getSpcURI() {
		return spcURI;
	}

	public void setSpcURI(URL spcURI) {
		this.spcURI = spcURI;
	}

	public URL getDbpediaURI() {
		return dbpediaURI;
	}

	public void setDbpediaURI(URL dbpediaURI) {
		this.dbpediaURI = dbpediaURI;
	}

	public String getIdentifier() {
		return identifier;
	}
	@Override
	public String getId() {
		return getIdentifier();
	}

	public String getName() {
		return name;
	}

	public InternetAddress getMbox() {
		return mbox;
	}

	public Map<String, DataCatalog> getCatalogs() {
		return catalogs;
	}

	public JSONObject getSpatial() {
		return spatial;
	}

	public void setSpatial(JSONObject spatial) {
		this.spatial = spatial;
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
		DataPublisher other = (DataPublisher) obj;
		if (identifier == null) {
			if (other.identifier != null)
				return false;
		} else if (!identifier.equals(other.identifier))
			return false;
		return true;
	}
	
	public static class DataPublisherBuilder extends GenericBuilder{
		
		private final String identifier;
		private final String name;
		private InternetAddress mbox;

		private URL image;
		private String type;
		private URL amacaURI;
		private URL spcURI;
		private URL dbpediaURI;
		private String description;
		private URL homepage;
		private JSONObject spatial;
		
		public static DataPublisherBuilder init(String identifier, String name){
			return new DataPublisherBuilder(identifier, name);
		}
		
		private DataPublisherBuilder(String identifier, String name){
			this.identifier = identifier;
			this.name = name;
		}
		
		public DataPublisherBuilder mbox(String mbox){
			try {
				this.mbox = new InternetAddress(mbox, true);
//				this.mbox.validate();
			} catch (AddressException ex) {
				logger.warn("["+this.identifier+"] mbox "+mbox+" is not valid");
			}
			
			return this;
		}
		public DataPublisherBuilder image(URL image){
			this.image = image;
			return this;
		}
		public DataPublisherBuilder type(String type){
			this.type = type;
			return this;
		}
		public DataPublisherBuilder amacaURI(URL amacaURI){
			this.amacaURI = amacaURI;
			return this;
		}
		public DataPublisherBuilder spcURI(URL spcURI){
			this.spcURI = spcURI;
			return this;
		}
		public DataPublisherBuilder dbpediaURI(URL dbpediaURI){
			this.dbpediaURI = dbpediaURI;
			return this;
		}
		public DataPublisherBuilder description(String description){
			this.description = description;
			return this;
		}
		public DataPublisherBuilder homepage(URL homepage){
			this.homepage = homepage;
			return this;
		}
		public DataPublisherBuilder spatial(JSONObject spatial){
			this.spatial = spatial;
			return this;
		}
		
		public DataPublisher build(){
			return new DataPublisher(this);
		}
	}
}
