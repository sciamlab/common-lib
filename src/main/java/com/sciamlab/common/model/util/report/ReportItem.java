package com.sciamlab.common.model.util.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


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
	
	public String toCSV(){
		StringBuffer csv = new StringBuffer();
		csv.append("\"").append(date).append("\"");
		csv.append(Report.CSV_SEPARATOR).append("\"").append(type).append("\"");
		csv.append(Report.CSV_SEPARATOR).append("\"").append(parentResource).append("\"");
		csv.append(Report.CSV_SEPARATOR).append("\"").append(landingPage).append("\"");
		csv.append(Report.CSV_SEPARATOR).append("\"").append(description).append("\"");
		return csv.toString();
	}
	
	public static String headerToCSV(){
		StringBuffer csv = new StringBuffer();
		boolean first = true;
		for(String col : HEADER){
			if(first)
				first = false;
			else
				csv.append(Report.CSV_SEPARATOR);
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
