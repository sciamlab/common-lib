package com.sciamlab.common.model.datacatalog;

import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.sciamlab.common.exception.OpenDataCategoryException;
import com.sciamlab.common.exception.OpenDataFrequencyException;
import com.sciamlab.common.exception.SciamlabRuntimeException;
import com.sciamlab.common.model.Classifiable;
import com.sciamlab.common.model.GenericSearchItem;
import com.sciamlab.common.model.util.GenericBuilder;
import com.sciamlab.common.model.util.report.ReportItem_EmailFormat;
import com.sciamlab.common.model.util.report.ReportItem_FrequencyError;
import com.sciamlab.common.nlp.EurovocConcept;
import com.sciamlab.common.nlp.EurovocField;
import com.sciamlab.common.nlp.EurovocMicroThesaurus;
import com.sciamlab.common.util.HTTPClient;
import com.sciamlab.common.util.SciamlabStringUtils;

/**
 * 
 * @author sciamlab
 *
 */
public class Dataset implements GenericSearchItem, Classifiable {

	private static final Logger logger = Logger.getLogger(Dataset.class);

	private final String identifier;
	private final String other_identifier;
	private final String title;
	private final String description;
	private final Date issued;
	private final Date modified;
	private final Locale language;
	private final OpenDataFrequency accrualPeriodicity;
	private final String temporal;
	private final String contactPoint;
	private final InternetAddress contactPointEmail;
	private final String theme;
	private OpenDataCategory category;
	private EurovocConcept concept;
	private EurovocMicroThesaurus microThesaurus;
	private EurovocField field;
	private final Set<String> keywords;
	private final URL landingPage;
	private final JSONObject spatial;	// <---GEOJSON
	private final DataCatalog catalog;
	private final DataPublisher publisher;
	private final Map<String, Distribution> distributions;

	private Dataset(DatasetBuilder builder){
		this.identifier = builder.identifier;
		this.other_identifier = builder.other_identifier;
		this.title = builder.title;
		this.description = builder.description;
		this.issued = builder.issued;
		this.modified = builder.modified;
		this.language = builder.language;
		this.accrualPeriodicity = builder.accrualPeriodicity;
		this.temporal = builder.temporal;
		this.theme = builder.theme;
//		this.concept = builder.concept;
//		this.microThesaurus = builder.microThesaurus;
//		this.field = builder.field;
		this.contactPoint = builder.contactPoint;
		this.contactPointEmail = builder.contactPointEmail;
		this.keywords = builder.keywords;
		this.landingPage = builder.landingPage;
		this.spatial = builder.spatial;
		this.catalog = builder.catalog;
		if(!catalog.hasDataset(identifier))
			catalog.addDataset(this);
		this.publisher = builder.publisher;
		if(!publisher.hasDataset(identifier))
			publisher.addDataset(this);
		this.distributions = builder.distributions;
		//setting the dataset as owner in all the distributions
		for(Distribution d : this.distributions.values()){
			d.setDataset(this);
		}
	}
	
	public boolean hasKeyword(String keyword){
		return this.keywords.contains(keyword);
	}
	
	public Distribution getDistribution(String identifier){
		return this.distributions.get(identifier);
	}
	public boolean hasDistribution(String identifier){
		return this.distributions.containsKey(identifier);
	}
	
	public String getIdentifier() {
		return identifier;
	}
	@Override
	public String getId() {
		return identifier;
	}

	public String getTitle() {
		return title;
	}
	
	public String getOtherIdentifier() {
		return other_identifier;
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
	
	public Locale getLanguage() {
		return language;
	}

	public OpenDataFrequency getAccrualPeriodicity() {
		return accrualPeriodicity;
	}

	public String getTemporal() {
		return temporal;
	}

	public String getTheme() {
		return theme;
	}

	public EurovocConcept getConcept() {
		return concept;
	}

	public OpenDataCategory getCategory() {
		return category;
	}

	public void setCategory(OpenDataCategory category) {
		this.category = category;
	}

	public EurovocMicroThesaurus getMicroThesaurus() {
		return microThesaurus;
	}

	public EurovocField getField() {
		return field;
	}

	public void setConcept(EurovocConcept concept) {
		this.concept = concept;
	}

	public void setMicroThesaurus(EurovocMicroThesaurus microThesaurus) {
		this.microThesaurus = microThesaurus;
	}

	public void setField(EurovocField field) {
		this.field = field;
	}

	@Override
	public String getText() {
		StringBuffer sb = new StringBuffer();
		if(title!=null) sb.append(title);
		if(description!=null) sb.append(", "+description);
		for (Object o: keywords)
			sb.append(", "+(String)o);
		return sb.toString();
	}
	
	public String getContactPoint() {
		return contactPoint;
	}
	public InternetAddress getContactPointEmail() {
		return contactPointEmail;
	}

	public Set<String> getKeywords() {
		return keywords;
	}

	public URL getLandingPage() {
		return landingPage;
	}

	public JSONObject getSpatial() {
		return spatial;
	}

	public DataCatalog getDataCatalog() {
		return catalog;
	}
	
	@Override
	public DataCatalog getChannel() {
		return this.catalog;
	}
	
	@Override
	public DataPublisher getPublisher() {
		return this.publisher;
	}

	public DataPublisher getDataPublisher() {
		return publisher;
	}

	public Map<String, Distribution> getDistributions() {
		return distributions;
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
		Dataset other = (Dataset) obj;
		if (identifier == null) {
			if (other.identifier != null)
				return false;
		} else if (!identifier.equals(other.identifier))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Dataset [identifier=" + identifier + ", title=" + title
				+ ", description=" + description + ", issued=" + issued
				+ ", modified=" + modified + ", language=" + language
				+ ", accrualPeriodicity=" + accrualPeriodicity + ", temporal="
				+ temporal + ", theme=" + theme + ", contactPoint="
				+ contactPoint + ", contactPointEmail=" + contactPointEmail
				+ ", keywords=" + keywords + ", landingPage=" + landingPage
				+ ", spatial=" + spatial + "]";
	}

	public static class DatasetBuilder extends GenericBuilder{
		
		private static HTTPClient http = new HTTPClient();
		
		private final String identifier;
		private String other_identifier;
		private final String title;
		private String description;
		private Date issued;
		private Date modified;
		private Locale language;
		private OpenDataFrequency accrualPeriodicity;
		private String temporal;
		private String contactPoint;
		private InternetAddress contactPointEmail;
		private String theme;
		private Set<String> keywords = new HashSet<String>();
		private final URL landingPage;
		private JSONObject spatial;
		private final DataCatalog catalog;
		private final DataPublisher publisher;
		private Map<String, Distribution> distributions = new HashMap<String, Distribution>();
		
		public static DatasetBuilder init(String identifier, String title, URL landingPage, DataCatalog catalog, DataPublisher publisher){
			return new DatasetBuilder(identifier, title, landingPage, catalog, publisher);
		}
		
		private DatasetBuilder(String identifier, String title, URL landingPage, DataCatalog catalog, DataPublisher publisher){
			this.identifier = identifier
//					.toLowerCase()
//					.replaceAll("%c3%a0", "a")		// à
//					.replaceAll("%e2%82%ac", "e")	// €
//					.replaceAll("%c3%a8", "e") 		// è
//					.replaceAll("%e2%80%99", "-") 	// '
					;
			this.title = SciamlabStringUtils.replaceStopWords(title, SciamlabStringUtils.STOP_WORDS_EXTENDED);
			if(!http.isOK(landingPage)){
				Status status = http.getURLResponseStatus(landingPage);
				logger.warn("["+catalog.getIdentifier()+" --> "+this.identifier+"] landing page "+landingPage+" returned code "+status.getStatusCode()+" ["+status+"]");
			}
			this.landingPage = landingPage;
			this.catalog = catalog;
			this.publisher = publisher;
		}
		
		public DatasetBuilder addKeyword(String keyword){
			keyword = SciamlabStringUtils.replaceStopWords(keyword, SciamlabStringUtils.STOP_WORDS_EXTENDED_FOR_TAGS);
			while(keyword.contains("  ")){
				keyword = keyword.replaceAll("  ", " ");
			}
			if(keyword.endsWith(".")) keyword = keyword.substring(0, keyword.length()-1);
			keyword = keyword.toLowerCase().trim();
			if(keyword.length()<2) return this; //skip empty and 1 char strings
			if(keyword.length()>=20) return this; //skip tags longer than 20
			this.keywords.add(keyword);
			return this;
		}
		public DatasetBuilder addKeywords(Set<String> keywords){
			for(String keyword : keywords)
				this.addKeyword(keyword);
			return this;
		}
		public DatasetBuilder addDistribution(Distribution distribution){
			this.distributions.put(distribution.getTitle(), distribution);
			return this;
		}
		
		public DatasetBuilder description(String description){
			if(description!=null && !"".equals(description))
				this.description = SciamlabStringUtils.replaceStopWords(description, SciamlabStringUtils.STOP_WORDS_EXTENDED);
			return this;
		}
		public DatasetBuilder issued(Date issued){
			this.issued = issued;
			return this;
		}
		public DatasetBuilder modified(Date modified){
			this.modified = modified;
			return this;
		}
		public DatasetBuilder otherIdentifier(String other_identifier){
			this.other_identifier = other_identifier;
			return this;
		}
		public DatasetBuilder language(Locale language){
			this.language = language;
			return this;
		}
		public DatasetBuilder accrualPeriodicity(String accrualPeriodicity) throws OpenDataFrequencyException{
			if(accrualPeriodicity==null)
				throw new OpenDataFrequencyException(accrualPeriodicity);
			accrualPeriodicity = accrualPeriodicity.trim().toLowerCase();
			this.accrualPeriodicity = OpenDataFrequency.getByAlias(accrualPeriodicity);
			if(this.accrualPeriodicity==null){
				report.add(new ReportItem_FrequencyError(title, landingPage.toString(), "Frequency not recognized", accrualPeriodicity));
				throw new OpenDataFrequencyException(accrualPeriodicity);
			}
			return this;
		}
		public DatasetBuilder temporal(String temporal){
			if(temporal!=null && !"".equals(temporal))
				this.temporal = temporal;
			return this;
		}
		public DatasetBuilder theme(String theme) throws OpenDataCategoryException{
			if(theme!=null && !"".equals(theme))
				this.theme = theme;
			return this;
		}
		public DatasetBuilder contactPoint(String contactPoint){
			if(contactPoint!=null)
				this.contactPoint = SciamlabStringUtils.replaceStopWords(contactPoint, SciamlabStringUtils.STOP_WORDS_EXTENDED);
			return this;
		}
		public DatasetBuilder contactPointEmail(String contactPointEmail){
			try{
				if(contactPointEmail!=null)
					this.contactPointEmail = new InternetAddress(contactPointEmail.trim().toLowerCase(), true);
			}catch(AddressException e){
				getReport().add(new ReportItem_EmailFormat(identifier, this.landingPage.toString(), "Email format not valid", contactPointEmail));
			}
			return this;
		}
		public DatasetBuilder spatial(JSONObject spatial){
			this.spatial = spatial;
			return this;
		}
			
		public Dataset build(){
			if(contactPoint==null || "".equals(contactPoint)) contactPoint = publisher.getName();
			if(contactPointEmail==null) contactPointEmail = publisher.getMbox();
			if(spatial==null && publisher.getSpatial()!=null) spatial = publisher.getSpatial();
			if(accrualPeriodicity==null) accrualPeriodicity = OpenDataFrequency.IRREGULAR;
			if(distributions.isEmpty())
				throw new SciamlabRuntimeException("Distribtion list empty for dataset: "+identifier);
			Dataset dataset = new Dataset(this);
			this.catalog.addDataset(dataset);
			this.publisher.addDataset(dataset);
			return dataset;
		}
	}
}
