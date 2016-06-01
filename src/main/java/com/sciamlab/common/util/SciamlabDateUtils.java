package com.sciamlab.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 
 * @author SciamLab
 *
 */

public class SciamlabDateUtils {

	private static final DateFormat ISO8061_FORMATTER = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

	public static Date parseStringDate(String str_date, Locale locale){
		List<SimpleDateFormat> knownPatterns = new ArrayList<SimpleDateFormat>();
		knownPatterns.add(new SimpleDateFormat("dd/MMM/yyyy",locale));
		knownPatterns.add(new SimpleDateFormat("dd MMMMM yyyy",locale));
		knownPatterns.add(new SimpleDateFormat("ddMMMMM yyyy",locale));
		knownPatterns.add(new SimpleDateFormat("dMMMMM yyyy",locale));
		knownPatterns.add(new SimpleDateFormat("d MMMMMyyyy",locale));
		knownPatterns.add(new SimpleDateFormat("dd-MM-yyyy",locale));
		knownPatterns.add(new SimpleDateFormat("dd/MM/yyyy",locale));
		knownPatterns.add(new SimpleDateFormat("MMMMM yyyy",locale));

		for (SimpleDateFormat pattern : knownPatterns) {
			try {
				// Take a try
				return pattern.parse(str_date);

			} catch (ParseException pe) {
				// Loop on
			}
		}
		System.err.println("pattern for parsing the date: [" + str_date + "] not available");
		return null;
	}
	
	public static Date parseStringDate(String str_date, String pattern) throws ParseException{
		return new SimpleDateFormat(pattern).parse(str_date);
	}

	public static Date getDateFromIso8061DateString(String dateString) {
		try {
			return ISO8061_FORMATTER.parse(dateString);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	public static String getCurrentDateAsIso8061String() {
		return ISO8061_FORMATTER.format(new Date());
	}

	public static String getDateAsIso8061String(Date date) {
		return ISO8061_FORMATTER.format(date);
	}

	public static String getCurrentDateAsFormattedString(String format) {
		return new SimpleDateFormat(format).format(new Date());
	}

	public static String getDateAsFormattedString(Date date, String format) {
		return new SimpleDateFormat(format).format(date);
	}
}
