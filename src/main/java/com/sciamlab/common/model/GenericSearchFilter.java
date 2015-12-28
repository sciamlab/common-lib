package com.sciamlab.common.model;

import java.util.Date;

public class GenericSearchFilter {
	private String query;
	private Date start;
	private Date stop;
	
	public GenericSearchFilter(){ }
	
	public GenericSearchFilter(String query) {
		this.query = query;
	}
	public String query() {
		return query;
	}
	public GenericSearchFilter query(String query) {
		this.query = query;
		return this;
	}
	public Date start() {
		return start;
	}
	public GenericSearchFilter start(Date start) {
		this.start = start;
		return this;
	}
	public Date stop() {
		return stop;
	}
	public GenericSearchFilter stop(Date stop) {
		this.stop = stop;
		return this;
	}

	@Override
	public String toString() {
		return "GenericSearchFilter [query=" + query + ", start=" + start
				+ ", stop=" + stop + "]";
	}
	
	
}
