package com.sciamlab.common.model.newsfeed;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.sciamlab.common.model.GenericPublisher;
import com.sciamlab.common.model.util.GenericBuilder;

/**
 * the editor corresponds to a news publisher (like repubblica.it) 
 * @author sciamlab
 *
 */
public class Editor implements GenericPublisher<News> {
	
	private static final Logger logger = Logger.getLogger(Editor.class);

	private final String identifier;
	private final String name;
	private final URL homepage;
	private final Map<String, News> news;
	
	private Editor(EditorBuilder builder){
		this.identifier = builder.identifier;
		this.name = builder.name;
		this.homepage = builder.homepage;
		this.news = new HashMap<String, News>();
	}
	
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
		Editor other = (Editor) obj;
		if (identifier == null) {
			if (other.identifier != null)
				return false;
		} else if (!identifier.equals(other.identifier))
			return false;
		return true;
	}

	public static class EditorBuilder extends GenericBuilder{
		
		private final String identifier;
		private String name;
		private URL homepage;
		
		public static EditorBuilder init(String identifier){
			return new EditorBuilder(identifier);
		}
		
		private EditorBuilder(String identifier){
			this.identifier = identifier;
		}
		public EditorBuilder name(String name){
			this.name = name;
			return this;
		}
		public EditorBuilder homepage(URL homepage){
			this.homepage = homepage;
			return this;
		}
		public Editor build(){
			return new Editor(this);
		}
	}
}
