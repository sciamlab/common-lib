package com.sciamlab.common.model.util.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.sciamlab.common.util.SciamlabDateUtils;


public class ReportItem {
	
	public static final List<String> HEADER = new ArrayList<String>(){{add("Date");add("Type");add("Parent resource");add("Landing page");add("Description");add("Bad resource");}};
	
	private String type = "GENERIC";
	
	protected final Date    date = new Date();
	protected final String  parentResource;
	protected final String  landingPage;
	protected final String  description;
	protected final String  resource;
	
	public ReportItem(String parentResource, String landingPage, String description, String resource) {
		this.parentResource = parentResource;
		this.landingPage = landingPage;
		this.description = description;
		this.resource = resource;
	}
	
	public String toCSV(String separator){
		StringBuffer csv = new StringBuffer();
		csv.append("\"").append(SciamlabDateUtils.getDateAsIso8061String(date)).append("\"");
		csv.append(separator).append("\"").append(type).append("\"");
		csv.append(separator).append("\"").append(parentResource).append("\"");
		csv.append(separator).append("\"").append(landingPage).append("\"");
		csv.append(separator).append("\"").append(description).append("\"");
		return csv.toString();
	}
	
	public static String headerToCSV(String separator){
		StringBuffer csv = new StringBuffer();
		boolean first = true;
		for(String col : HEADER){
			if(first)
				first = false;
			else
				csv.append(separator);
			csv.append("\""+col+"\"");
		}
		return csv.toString();
	}

	public Date getDate() {
		return date;
	}

	public String getParentResource() {
		return parentResource;
	}

	public String getLandingPage() {
		return landingPage;
	}

	public String getDescription() {
		return description;
	}

	public String getResource() {
		return resource;
	}

}
