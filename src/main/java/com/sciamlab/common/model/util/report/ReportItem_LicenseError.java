package com.sciamlab.common.model.util.report;


public class ReportItem_LicenseError extends ReportItem {
	
	public String type = "LICENSE_NOT_DEFINED";

	public ReportItem_LicenseError(String parentResource, String landingPage, String description, String resource) {
		super(parentResource, landingPage, description, resource);
	}

	
}
