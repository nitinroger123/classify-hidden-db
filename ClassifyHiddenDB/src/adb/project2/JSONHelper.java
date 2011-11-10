package adb.project2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Helper class to parse the Json
 * @author nn2270 and kt2424
 *
 */
public class JSONHelper {
	
	/**
	 * get the JSON results array for the query.
	 * @param search
	 * @return
	 * @throws JSONException
	 */
	public static JSONArray getJSONArrayFromSearch(JSONObject search) throws JSONException {
		return search.getJSONObject("SearchResponse").getJSONObject("Web").getJSONArray("Results");
	}
	
	public static Long getTotalFromSearch(JSONObject search) throws JSONException {
		 return search.getJSONObject("SearchResponse").getJSONObject("Web").getLong("Total");
	}
}
