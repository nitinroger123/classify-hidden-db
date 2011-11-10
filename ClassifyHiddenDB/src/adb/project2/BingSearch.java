package adb.project2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * The class to search bing with the 
 * supplied query.
 */
public class BingSearch {
	
	/* The constants required to build the request*/
	public static final String sources = "Web";
	public static final String webCount = "4";
	
	/**
	 * Build the requestURL
	 * @param query
	 * @param appID
	 * @return
	 */
	public static String getRequestString(String query, String appID) {
//		System.out.println("The query is: "+ query);
		query = query.replaceAll(" ", "%20");
		String requestString = "http://api.search.live.net/json.aspx?" + "Appid=" + appID + "&query="+query 
						+ "&sources=" + sources + "&web.count=" + webCount;
//		System.out.println("Request URL : "+requestString);
		return requestString;
	}
	
	/**
	 *Method to read all the lines from the reader. 
	 * @param rd
	 * @return
	 * @throws IOException
	 */
	private static String readAll(BufferedReader rd) throws IOException {
	    StringBuilder sb = new StringBuilder();
	    String s ="";
	    while ((s = rd.readLine()) != null) {
	      sb.append(s);
	    }
	    return sb.toString();
	  }
	
	/**
	 * Parse the JSON Result after pinging the URL.
	 * @param requestURL
	 * @return
	 * @throws JSONException
	 * @throws IOException
	 */
	public static JSONObject getSearchResults(String requestURL) throws JSONException, IOException {
		URL search = new URL(requestURL);
		URLConnection conn = search.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String resultString = readAll(in); 
		JSONObject result = new JSONObject(resultString);
		return result;
	}

}
