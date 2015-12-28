package com.sciamlab.common.model.newsfeed;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.sciamlab.common.model.GenericSearchChannel;
import com.sciamlab.common.model.datacatalog.DataPublisher;
import com.sciamlab.common.model.datacatalog.Dataset;
import com.sciamlab.common.model.util.GenericBuilder;

/**
 * the channel corresponds to a news source (like EMM) 
 * @author sciamlab
 *
 */
public class Aggregator implements GenericSearchChannel<News, Editor> {
	
	private static final Logger logger = Logger.getLogger(Aggregator.class);

	private final String identifier;
	private final String name;
	private final URL homepage;
	private final Map<String, News> news;
	
	private final URL baseURL;
	private final URL catalogURL;
	private Map<String, Editor> editors = new HashMap<String, Editor>();
	
	private Aggregator(AggregatorBuilder builder){
		this.identifier = builder.identifier;
		this.name = builder.name;
		this.homepage = builder.homepage;
		this.news = new HashMap<String, News>();
		this.baseURL = builder.baseURL;
		this.catalogURL = builder.catalogURL;
		this.editors.putAll(builder.editors);
	}
	
	/*
	 * NEWS
	 */
	public void addNews(News news){
		this.news.put(news.getId(), news);
	}
	public News getNews(String id){
		return this.news.get(id);
	}
	public boolean hasNews(String id){
		return this.news.containsKey(id);
	}
	public Map<String, News> getNews() {
		return news;
	}
	@Override
	public void addSearchItem(News news){
		addNews(news);
	}
	@Override
	public News getSearchItem(String id){
		return getNews(id);
	}
	@Override
	public boolean hasSearchItem(String id){
		return hasNews(id);
	}
	@Override
	public Map<String, News> getSearchItems() {
		return getNews();
	}
	
	/*
	 * EDITORS
	 */
	@Override
	public void addPublisher(Editor editor){
		this.editors.put(editor.getIdentifier(), editor);
	}
	@Override
	public boolean hasPublisher(String identifier){
		return this.editors.containsKey(identifier);
	}
	@Override
	public Editor getPublisher(String identifier){
		return this.editors.get(identifier);
	}
	@Override
	public Map<String, Editor> getPublishers() {
		return editors;
	}
	
	public String getIdentifier() {
		return identifier;
	}
	@Override
	public String getId() {
		return identifier;
	}

	public String getName() {
		return name;
	}

	public URL getHomepage() {
		return homepage;
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
		Aggregator other = (Aggregator) obj;
		if (identifier == null) {
			if (other.identifier != null)
				return false;
		} else if (!identifier.equals(other.identifier))
			return false;
		return true;
	}

	public static class AggregatorBuilder extends GenericBuilder{
		
		private final String identifier;
		private String name;
		private URL homepage;
		private final URL baseURL;
		private final URL catalogURL;
		private final Map<String, Editor> editors = new HashMap<String, Editor>();
		
		public static AggregatorBuilder init(String identifier, URL baseURL, URL catalogURL){
			return new AggregatorBuilder(identifier, baseURL, catalogURL);
		}
		
		private AggregatorBuilder(String identifier, URL baseURL, URL catalogURL){
			this.identifier = identifier;
			this.baseURL = baseURL;
			this.catalogURL = catalogURL;
		}
		public AggregatorBuilder name(String name){
			this.name = name;
			return this;
		}
		public AggregatorBuilder homepage(URL homepage){
			this.homepage = homepage;
			return this;
		}
		public AggregatorBuilder editor(Editor editor){
			this.editors.put(editor.getId(), editor);
			return this;
		}
		public Aggregator build(){
			return new Aggregator(this);
		}
	}

	
}
