package com.sciamlab.common.model.util.report;

import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.sciamlab.common.util.SciamlabStreamUtils;

public class Report extends ArrayList<ReportItem>{
	
	private static final long serialVersionUID = -1917352321694600369L;
	
	private static final Logger logger = Logger.getLogger(Report.class);

	private static String DEFAULT_LOG_FILE = "log4j.properties";
//	private static String DEFAULT_REPORT_LOG_FILE = "REPORT.csv";
	
	public static final String CSV = "csv";
	public static final String COMMA = ",";
	
	public final String csv_separator;
	public final String file_prefix;
	public final String name;
	
	public static final Map<String, Method> SUPPORTED_FORMATS = new HashMap<String, Method>();
	
	private static final Class noparams[] = {};
	
	static{
		try {
			SUPPORTED_FORMATS.put(CSV, Report.class.getDeclaredMethod("printAsCSV", noparams));
		} catch (NoSuchMethodException | SecurityException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}
	
	private Report(Builder builder) {
		this.name = builder.name;
		this.csv_separator = builder.csv_separator;
		this.file_prefix = builder.file_prefix;
	}

	public void printAs(String format) throws Exception {
		if(!SUPPORTED_FORMATS.containsKey(format))
			throw new Exception("Report format not valid. Please choose one in "+SUPPORTED_FORMATS.keySet());
			
		SUPPORTED_FORMATS.get(format).invoke(this, null);
	}
	
	public void printAsCSV(){
		if(this.size()==0)
			return;
		try {
			String reportPath = System.getProperty("report_filepath");
			String newReportPath = reportPath.replace(reportPath.substring(reportPath.lastIndexOf("/")+1, reportPath.length()), file_prefix+name.replace(" ", "_")+".CSV");
			System.setProperty("report_filepath", newReportPath);
			PropertyConfigurator.configure(SciamlabStreamUtils.getInputStream(System.getProperty("logprops_filepath", DEFAULT_LOG_FILE)));
			Logger loggerReport = Logger.getLogger("reportLogger");
			loggerReport.info(ReportItem.headerToCSV(csv_separator));
			for (ReportItem item: this) {
				loggerReport.info(item.toCSV(csv_separator));
			}
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public String getName() {
		return name;
	}

	public synchronized void addReportItems(Report report){
		this.addAll(report);
	}
	
	public static class Builder{
		
		private String csv_separator = COMMA;
		private String file_prefix = "REPORT_";
		private String name = "";
		
		public Builder(){ }
		
		public Builder name(String name){
			this.name = name;
			return this;
		}
		public Builder csv_separator(String csv_separator){
			if(csv_separator!=null)
				this.csv_separator = csv_separator;
			return this;
		}
		public Builder file_prefix(String file_prefix){
			if(file_prefix!=null)
				this.file_prefix = file_prefix;
			return this;
		}
		
		public Report build(){
			return new Report(this);
		}
	}
}
