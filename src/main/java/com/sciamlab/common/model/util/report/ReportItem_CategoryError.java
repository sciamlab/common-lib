package com.sciamlab.common.model.util.report;


public class ReportItem_CategoryError extends ReportItem {
	
	public String type = "CATEGORY_NOT_DEFINED";

	public ReportItem_CategoryError(String parentResource, String landingPage, String description, String resource) {
		super(parentResource, landingPage, description, resource);
	}

	
}
