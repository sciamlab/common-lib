package com.sciamlab.common.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.security.RolesAllowed;
import javax.mail.internet.InternetAddress;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 
 * @author SciamLab
 *
 */

public class SciamlabStringUtils {
	
	private static final Pattern UUID_PATTERN = Pattern.compile("^[0-9a-f]{8}(-[0-9a-f]{4}){3}-[0-9a-f]{12}$");
	
	private static void minLength(String str, int len) throws IllegalArgumentException {
		if (str==null || str.length() < len) {
			throw new IllegalArgumentException();
		}
	}

	private static void maxLength(String str, int len) throws IllegalArgumentException {
		if (str==null || str.length() > len) {
			throw new IllegalArgumentException();
		}
	}
	
//	private static Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
	private static Pattern pattern = Pattern.compile("^[_a-z0-9]+(\\.[_a-z0-9]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)*(\\.[a-z]{2,4})$");

	public static boolean isValidEmail(String email) {
		try {
			Matcher matcher = pattern.matcher(email);
			if(!matcher.matches())
				return false;
			InternetAddress ia = new InternetAddress(email.trim().toLowerCase(), true);
			ia.validate();
			return true;
		} catch (Exception e){
			return false;
		}
	}

    public static boolean isValidUuid(String uuid) {
        return UUID_PATTERN.matcher(uuid).matches();
    }
    
    public static String stackTraceToString(Exception e){
    	Writer writer = null;
    	PrintWriter printWriter = null;
		try {
			writer = new StringWriter();
			printWriter = new PrintWriter(writer);
			e.printStackTrace(printWriter);
			String s = writer.toString();
			return s;
		} finally{
			if(printWriter!=null) printWriter.close();
			if(writer!=null) try { writer.close(); } catch (IOException e1) { e1.printStackTrace(); }
		}
	}    
    
    public static String replaceStopWordsAddDashesAndLowerCase(String s){
    	if(s==null) return null;
    	s = replaceStopWords(s.toLowerCase());
    	return s.replaceAll("   ", " ")
    			.replaceAll("  ", " ")
    			.replaceAll(" - ", "-")
    			.replaceAll("' ", "'")
				.replaceAll(" ", "-")
				.replaceAll("'", "")
				.toLowerCase();
    }
    
    public static final Map<String, String> STOP_WORDS_DEFAULT = new LinkedHashMap<String, String>(){{
    	put("À", "A");
		put("Ã", "a");
		put("È", "E"); 
		put("ß", "b");
		put("ç", "c");
		put("â", "a");
		put("à", "a");
		put("a'", "a");
		put("ê", "e");
		put("è", "e");;
		put("é", "e");
		put("e'", "e");
		put("ì", "i");
		put("i'", "i");
		put("ò", "o");
		put("ô", "o");
		put("ö", "o");
		put("o'", "o");
		put("ü", "u");
		put("ù", "u");
		put("u'", "u");
	}};
	
	public static final Map<String, String> STOP_WORDS_EXTENDED = new LinkedHashMap<String, String>(){{
		putAll(SciamlabStringUtils.STOP_WORDS_DEFAULT);
		put("\n", ""); 
		put("\t", ""); 
		put("°", "");
		put("\\?", "");
		put("   ", " ");
		put("  ", " ");
//		put(" - ", "-");
		put("<p>","");
		put("</p>", "");
		put("&nbsp;", " ");
		put("\\xa0", " ");
		put("<br />", "");
		put("¿", " ");
		put("\\xbf", " ");
		put("&#039;", " ");	//.replaceAll("&#039;", " ")
		put("&#039", " ");	//.replaceAll("&#039", " ")
		put("&#39;", " ");	//.replaceAll("&#039;", " ")
		put("&#39", " ");	//.replaceAll("&#039", " ")
		put("&#x27;", " ");
		put("&quot;", "");
		put("&rsquo;", " ");
		put("&agrave", "a");	//.replaceAll("'", " ")\"
		put("\"", "");
		put("`", "'");
		put("’", "'");
		put("/", " ");
	}};
	
	public static final Map<String, String> STOP_WORDS_EXTENDED_FOR_TAGS = new LinkedHashMap<String, String>(){{
		putAll(STOP_WORDS_EXTENDED);
		put("dell&#039;ambiente", "ambiente");	
		put("opere d&#x27;arte", "arte");
		put("\\(a\\)", " a ");	
		put("\\?", "a");
		put("�", "a");
		put(" \\(temporal\\).", "");
		put(" \\(temporal\\)", "");
		put(" \\(theme\\).", "");
		put(" \\(theme\\)", "");
		put("\\(theme\\).", "");
		put(";", "");
		put("&", "and");
		put("\\(", "");
		put("\\)", "");			
		put("#", "");
		put("'", "");
		put(", ", " ");
		put(": ", " ");
		put("  ", " ");
		put(",", "");
		put(":", "");
	}};
	
    public static String replaceStopWords(String orig){
		return replaceStopWords(orig, STOP_WORDS_DEFAULT);
	}
	
	public static String replaceStopWords(String orig, Map<String, String> black_list){
		if(orig==null) return null;
		String neww = orig;
		for(String w : black_list.keySet()){
			String r = black_list.get(w);
			neww = neww.replaceAll(w, r);
		}
		return neww;
	}
    
    private static final Random random = new Random();
    private static final char[] symbols;
    
    static {
      StringBuilder tmp = new StringBuilder();
      for (char ch = '0'; ch <= '9'; ++ch)
    	  tmp.append(ch);
      for (char ch = 'a'; ch <= 'z'; ++ch)
    	  tmp.append(ch);
      for (char ch = 'A'; ch <= 'Z'; ++ch)
          tmp.append(ch);
      symbols = tmp.toString().toCharArray();
    }
    
    public static String getRandomString(int length){
    	if (length < 1)
    		throw new IllegalArgumentException("length < 1: " + length);
    	char[] buf = new char[length];
    	for (int idx = 0; idx < buf.length; ++idx) 
    		buf[idx] = symbols[random.nextInt(symbols.length)];
    	return new String(buf);
    }

    public static int getRandomNumber(int min, int max){
    	return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
    
    /**
     * this method attemps to remove html tags from text (limit to 100 loops)
     * 
     * @param orig
     * @return
     */
    public static String removeHTML(String orig){
		if(orig==null) return null;
		while(orig.contains("<script")){
			String temp = orig.substring(orig.indexOf("<script"));
			orig = orig.substring(0, orig.indexOf("<script")) + temp.substring(temp.indexOf("</script>")+"</script>".length());
		}
		
		//do not try to remove html if number of < is different from >
		if(StringUtils.countMatches(orig, "<")==StringUtils.countMatches(orig, ">")){
			int ii = 0;
			while(orig.contains("<") && orig.contains(">") ){
				String temp = orig.substring(orig.indexOf("<"));
				orig = orig.substring(0, orig.indexOf("<")) + temp.substring(temp.indexOf(">")+1);
				if(ii>100)
					break;
				ii++;
			}
		}
		
		return orig;
	}
    
//	public static boolean isValidEmailAddress(String email) {
//		boolean result = true;
//		try {
//			InternetAddress emailAddr = new InternetAddress(email);
//			emailAddr.validate();
//		} catch (AddressException ex) {
//			result = false;
//		}
//		return result;
//	}
	
	public static JSONObject getSciamlabDefaultInfo(){
		JSONObject info = new JSONObject();
		info.put("author", "SciamLab");
		info.put("contact", "api@sciamlab.com");
		return info;
	}
	
	public static JSONObject getSciamlabMethodsInfo(Class<?> clazz){
		JSONArray methods = new JSONArray();
		for(Method m : clazz.getDeclaredMethods()){
			String http_method = null;
			if(m.isAnnotationPresent(GET.class))
				http_method = "GET";
			else if(m.isAnnotationPresent(POST.class))
				http_method = "POST";
			else if(m.isAnnotationPresent(PUT.class))
				http_method = "PUT";
			else if(m.isAnnotationPresent(DELETE.class))
				http_method = "DELETE";
			if(http_method!=null){
				JSONObject json = new JSONObject();
				json.put("method", http_method);
				json.put("path", (m.isAnnotationPresent(Path.class)) ? m.getAnnotation(Path.class).value(): "/");
				if(m.isAnnotationPresent(RolesAllowed.class))
					json.put("roles", new JSONArray(Arrays.asList(m.getAnnotation(RolesAllowed.class).value())));
				else if(clazz.isAnnotationPresent(RolesAllowed.class))
					json.put("roles", new JSONArray(Arrays.asList(clazz.getAnnotation(RolesAllowed.class).value())));
				methods.put(json);
			}
		}
		return getSciamlabDefaultInfo().put("methods", methods);
	}
	
	public static JSONObject getSciamlabInfo(Map<String, Object> infos){
		JSONObject info = getSciamlabDefaultInfo();
		for(String k : infos.keySet()){
			info.put(k, infos.get(k));
		}
		return info;
	}
	
	/**
	   * <p>Find the Levenshtein distance between two Strings.</p>
	   *
	   * <p>This is the number of changes needed to change one String into
	   * another, where each change is a single character modification (deletion,
	   * insertion or substitution).</p>
	   *
	   * <p>The previous implementation of the Levenshtein distance algorithm
	   * was from <a href="http://www.merriampark.com/ld.htm">http://www.merriampark.com/ld.htm</a></p>
	   *
	   * <p>Chas Emerick has written an implementation in Java, which avoids an OutOfMemoryError
	   * which can occur when my Java implementation is used with very large strings.<br>
	   * This implementation of the Levenshtein distance algorithm
	   * is from <a href="http://www.merriampark.com/ldjava.htm">http://www.merriampark.com/ldjava.htm</a></p>
	   *
	   * <pre>
	   * StringUtils.getLevenshteinDistance(null, *)             = IllegalArgumentException
	   * StringUtils.getLevenshteinDistance(*, null)             = IllegalArgumentException
	   * StringUtils.getLevenshteinDistance("","")               = 0
	   * StringUtils.getLevenshteinDistance("","a")              = 1
	   * StringUtils.getLevenshteinDistance("aaapppp", "")       = 7
	   * StringUtils.getLevenshteinDistance("frog", "fog")       = 1
	   * StringUtils.getLevenshteinDistance("fly", "ant")        = 3
	   * StringUtils.getLevenshteinDistance("elephant", "hippo") = 7
	   * StringUtils.getLevenshteinDistance("hippo", "elephant") = 7
	   * StringUtils.getLevenshteinDistance("hippo", "zzzzzzzz") = 8
	   * StringUtils.getLevenshteinDistance("hello", "hallo")    = 1
	   * </pre>
	   *
	   * @param s  the first String, must not be null
	   * @param t  the second String, must not be null
	   * @return result distance
	   * @throws IllegalArgumentException if either String input <code>null</code>
	   */
	  public static int getLevenshteinDistance(String s, String t) {
	      if (s == null || t == null) {
	          throw new IllegalArgumentException("Strings must not be null");
	      }

	      /*
	         The difference between this impl. and the previous is that, rather 
	         than creating and retaining a matrix of size s.length()+1 by t.length()+1, 
	         we maintain two single-dimensional arrays of length s.length()+1.  The first, d,
	         is the 'current working' distance array that maintains the newest distance cost
	         counts as we iterate through the characters of String s.  Each time we increment
	         the index of String t we are comparing, d is copied to p, the second int[].  Doing so
	         allows us to retain the previous cost counts as required by the algorithm (taking 
	         the minimum of the cost count to the left, up one, and diagonally up and to the left
	         of the current cost count being calculated).  (Note that the arrays aren't really 
	         copied anymore, just switched...this is clearly much better than cloning an array 
	         or doing a System.arraycopy() each time  through the outer loop.)

	         Effectively, the difference between the two implementations is this one does not 
	         cause an out of memory condition when calculating the LD over two very large strings.
	       */
	      int n = s.length(); // length of s
	      int m = t.length(); // length of t
	      if (n == 0) {
	          return m;
	      } else if (m == 0) {
	          return n;
	      }
	      if (n > m) {
	          // swap the input strings to consume less memory
	          String tmp = s;
	          s = t;
	          t = tmp;
	          n = m;
	          m = t.length();
	      }
	      int p[] = new int[n+1]; //'previous' cost array, horizontally
	      int d[] = new int[n+1]; // cost array, horizontally
	      int _d[]; //placeholder to assist in swapping p and d
	      // indexes into strings s and t
	      int i; // iterates through s
	      int j; // iterates through t
	      char t_j; // jth character of t
	      int cost; // cost
	      for (i = 0; i<=n; i++) {
	          p[i] = i;
	      }
	      for (j = 1; j<=m; j++) {
	          t_j = t.charAt(j-1);
	          d[0] = j;

	          for (i=1; i<=n; i++) {
	              cost = s.charAt(i-1)==t_j ? 0 : 1;
	              // minimum of cell to the left+1, to the top+1, diagonally left and up +cost
	              d[i] = Math.min(Math.min(d[i-1]+1, p[i]+1),  p[i-1]+cost);
	          }
	          // copy current distance counts to 'previous row' distance counts
	          _d = p;
	          p = d;
	          d = _d;
	      }
	      // our last action in the above loop was to switch d and p, so p now 
	      // actually has the most recent cost counts
	      return p[n];
	  }
	  
	  public static String removeDuplicates(String string){

		  char[] chars = string.toCharArray();
		  Set<Character> charSet = new LinkedHashSet<Character>();
		  for (char c : chars) {
		      charSet.add(c);
		  }

		  StringBuilder sb = new StringBuilder();
		  for (Character character : charSet) {
		      sb.append(character);
		  }
		  return sb.toString();
	  }
	
	  public static void main(String[] args) {
		System.out.println(removeDuplicates("-----"));
	}
}
