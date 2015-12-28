package com.sciamlab.common.model.datacatalog;

import java.util.HashSet;
import java.util.Set;

import com.sciamlab.common.model.GenericSearchFilter;

public class DatasetSearchFilter extends GenericSearchFilter {

	private final Set<String> datasets;
	private final Set<String> publishers;
	private final Set<String> catalogs;

	private DatasetSearchFilter(DatasetFilterBuilder builder){
		this.datasets = builder.datasets;
		this.publishers = builder.publishers;
		this.catalogs = builder.catalogs;
	}
	
	public Set<String> datasets() {
		return datasets;
	}
	public Set<String> publishers() {
		return publishers;
	}
	public Set<String> catalogs() {
		return catalogs;
	}
	
	@Override
	public String toString() {
		return "DatasetSearchFilter [datasets=" + datasets + ", publishers=" + publishers + ", catalogs=" + catalogs + "]";
	}

	public static class DatasetFilterBuilder{
		private final Set<String> datasets = new HashSet<String>();
		private final Set<String> publishers = new HashSet<String>();
		private final Set<String> catalogs = new HashSet<String>();
		
		public static DatasetFilterBuilder init(){
			return new DatasetFilterBuilder();
		}
		
		private DatasetFilterBuilder(){	}
		
		public DatasetFilterBuilder dataset(String id){
			this.datasets.add(id);
			return this;
		}
		public DatasetFilterBuilder publisher(String id){
			this.publishers.add(id);
			return this;
		}
		public DatasetFilterBuilder catalog(String id){
			this.catalogs.add(id);
			return this;
		}

		public DatasetFilterBuilder datasets(Set<String> datasets){
			this.datasets.addAll(datasets);
			return this;
		}
		public DatasetFilterBuilder publishers(Set<String> publishers){
			this.publishers.addAll(publishers);
			return this;
		}
		public DatasetFilterBuilder catalogs(Set<String> catalogs){
			this.catalogs.addAll(catalogs);
			return this;
		}
		
		public DatasetSearchFilter build(){
			return new DatasetSearchFilter(this);
		}
	}
}
