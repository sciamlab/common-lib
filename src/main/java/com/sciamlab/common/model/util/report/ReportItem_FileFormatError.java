package com.sciamlab.common.model.util.report;


public class ReportItem_FileFormatError extends ReportItem {
	
	public String type = "FILE_FORMAT_NOT_VALID";

	public ReportItem_FileFormatError(String parentResource, String landingPage, String description, String resource) {
		super(parentResource, landingPage, description, resource);
	}

	
}
