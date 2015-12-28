package com.sciamlab.common.model.datacatalog;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;

import com.sciamlab.common.exception.OpenDataFormatException;
import com.sciamlab.common.exception.OpenDataLicenseException;
import com.sciamlab.common.model.util.GenericBuilder;
import com.sciamlab.common.model.util.report.ReportItem_FileFormatError;
import com.sciamlab.common.model.util.report.ReportItem_LicenseError;
import com.sciamlab.common.model.util.report.ReportItem_URLError;
import com.sciamlab.common.util.HTTPClient;
import com.sciamlab.common.util.SciamlabStringUtils;
/**
 * the distribution corresponds to a CKAN resource 
 * @author sciamlab
 *
 */
public class Distribution {

	private static final Logger logger = Logger.getLogger(Distribution.class);

	private final String title;
	private final String description;
	private final Date issued;
	private final Date modified;
	private final OpenDataLicense license;
	private final String rights;
	private final URL accessURL;
	private final URL downloadURL;
	private final long bytesize;
	private final String mediaType;
	private final OpenDataFormat format;
	private Dataset dataset;

	private Distribution(DistributionBuilder builder){
		this.title = builder.title;
		this.description = builder.description;
		this.issued = builder.issued;
		this.modified = builder.modified;
		this.license = builder.license;
		this.rights = builder.rights;
		this.accessURL = builder.accessURL;
		this.downloadURL = builder.downloadURL;
		this.bytesize = builder.bytesize;
		this.mediaType = builder.mediaType;
		this.format = builder.format;
	}

	public Dataset getDataset() {
		return dataset;
	}

	public void setDataset(Dataset dataset) {
		this.dataset = dataset;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public Date getIssued() {
		return issued;
	}

	public Date getModified() {
		return modified;
	}

	public OpenDataLicense getLicense() {
		return license;
	}

	public String getRights() {
		return rights;
	}

	public URL getAccessURL() {
		return accessURL;
	}

	public URL getDownloadURL() {
		return downloadURL;
	}

	public long getBytesize() {
		return bytesize;
	}

	public String getMediaType() {
		return mediaType;
	}

	public OpenDataFormat getFormat() {
		return format;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((title == null) ? 0 : title.hashCode());
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
		Distribution other = (Distribution) obj;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

	public static class DistributionBuilder extends GenericBuilder{

		private static HTTPClient http = new HTTPClient();

		private final String title;
		private String description;
		private Date issued;
		private Date modified;
		private final OpenDataLicense license;
		private String rights;
		private final URL accessURL;
		private final URL downloadURL;
		private long bytesize;
		private String mediaType;
		private final OpenDataFormat format;

		public static DistributionBuilder init(String title, String format_id, String downloadURL, String accessURL, String license_id, DataCatalog catalog) 
				throws MalformedURLException, OpenDataFormatException, OpenDataLicenseException{
			return new DistributionBuilder(title, format_id, downloadURL, accessURL, license_id, catalog);
		} 

		private DistributionBuilder(String title, String format_id, String downloadURL, String accessURL, String license_id, DataCatalog catalog) 
				throws MalformedURLException, OpenDataFormatException, OpenDataLicenseException{

			/*
			 * FILE FORMAT NORMALIZATION
			 */
			this.format = (format_id!=null) ? OpenDataFormat.getByAlias(format_id) : null;
			if(format == null){
				report.add(new ReportItem_FileFormatError(title, accessURL, "File format not recognized", format_id));
				throw new OpenDataFormatException(format_id);
			}
			/*
			 * TITLE
			 */
			this.title = (title == null || "".equals(title)) ? "File format "+this.format.id() : title.trim();
			/*
			 * LICENSE NORMALIZATION
			 */
			if(license_id==null){
				license_id = OpenDataLicense.NOT_SPEC.id();
				report.add(new ReportItem_LicenseError(title, accessURL, "License not specified", license_id));
			}
			this.license = OpenDataLicense.getByAlias(license_id);
			if(this.license==null){
				report.add(new ReportItem_LicenseError(title, accessURL, "License not recognized", license_id));
				throw new OpenDataLicenseException(license_id);
			}
			
			/*
			 * ACCESS URL NORMALIZATION
			 */
			this.accessURL = new URL(accessURL);
			if(!http.isOK(this.accessURL)){
				Status status = http.getURLResponseStatus(this.accessURL);
				report.add(new ReportItem_URLError(title, "TODO find a way to put ds url here!", "Returns code "+status.getStatusCode()+" ["+status+"]", accessURL));
				logger.warn("["+catalog.getIdentifier()+" --> "+accessURL+" --> "+this.title+"] access url "+accessURL+" returned code "+status.getStatusCode()+" ["+status+"]");
			}
			/*
			 * DOWNLOAD URL NORMALIZATION
			 */
			URL tmpURL = null;
			try{
				// the new URL may throw the MalformedURLException
				tmpURL = new URL(downloadURL);
				if ( !tmpURL.toString().startsWith("ftp") ) {
					if(!http.isOK(tmpURL)){
						Status status = http.getURLResponseStatus(tmpURL);
						report.add(new ReportItem_URLError(title, accessURL, "Returned code "+status.getStatusCode()+" ["+status+"]", downloadURL));
						logger.warn("["+catalog.getIdentifier()+" --> "+accessURL+" --> "+this.title+"] download url "+downloadURL+" returned code "+status.getStatusCode()+" ["+status+"]");
					}
				}
			} catch (MalformedURLException e){
				report.add(new ReportItem_URLError(title, accessURL, "URL not valid", downloadURL));
				//				//try clean url
				tmpURL = new URL(cleanUrl(catalog, downloadURL));
				if(!http.isOK(tmpURL)) {
					Status status = http.getURLResponseStatus(tmpURL);
					logger.warn("["+catalog.getIdentifier()+" --> "+accessURL+" --> "+this.title+"] download url "+downloadURL+" returned code "+status.getStatusCode()+" ["+status+"] (also after cleaning attempt!)");
				}
			} catch (javax.ws.rs.ProcessingException e) {
				if(e.getMessage().contains("Server redirected too many  times"))
					report.add(new ReportItem_URLError(title, accessURL, e.getMessage(), downloadURL));
				else if(e.getCause().getMessage().contains("Read timed out") || e.getCause().getMessage().contains("connect timed out")){
					report.add(new ReportItem_URLError(title, accessURL, e.getCause().getMessage(), downloadURL));
					logger.warn("["+catalog.getIdentifier()+" --> "+accessURL+" --> "+this.title+"] download url "+downloadURL+" "+e.getCause().getMessage());
				}else
					throw e;
				//			} catch (java.net.ConnectException e) {
				//				TODO fix ConnectException issue 
				//				throw e;
			}
			this.downloadURL = tmpURL;

		}

		public DistributionBuilder description(String description){
			if(description!=null && !"".equals(description)){
				this.description = SciamlabStringUtils.replaceStopWords(description, SciamlabStringUtils.STOP_WORDS_EXTENDED);
				this.description = SciamlabStringUtils.removeHTML(description);
			}
			return this;
		}
		public DistributionBuilder issued(Date issued){
			this.issued = issued;
			return this;
		}
		public DistributionBuilder modified(Date modified){
			this.modified = modified;
			return this;
		}
		public DistributionBuilder rights(String rights){
			this.rights = rights;
			return this;
		}
		public DistributionBuilder bytesize(long bytesize){
			this.bytesize = bytesize;
			return this;
		}
		public DistributionBuilder mediaType(String mediaType){
			this.mediaType = mediaType;
			return this;
		}

		public Distribution build(){
			return new Distribution(this);
		}

		private static String cleanUrl(DataCatalog catalog, String origUrl) {
			String url = origUrl;
			//Normalize url
			url = url.replaceAll(" ","%20");
			//protocol missing --> relative address
			if (!url.contains("://")){
				if(!url.startsWith("/"))
					url = "/" + url;
				url = catalog.getBaseURL() + url;
			}
			//			URL originalURL = new URL(url);
			//			if(!http.isOK(originalURL)) {
			//				//try to check if test fails for internal address
			//				URL newURL = new URL(originalURL.getProtocol(), catalog.getBaseURL().getHost(), catalog.getBaseURL().getPort(), originalURL.getFile());
			//	            url = newURL.toString();
			//			}
			return url;
		}
	}
}
