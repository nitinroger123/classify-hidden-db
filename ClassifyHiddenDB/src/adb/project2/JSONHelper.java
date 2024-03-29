package adb.project2;

import java.util.HashSet;

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
	
	/**
	 * Get the Top 4 docs returned by Bing
	 * @param resultObj
	 * @return
	 * @throws JSONException
	 */
	public static HashSet<String> getTopFourResults(JSONObject resultObj) throws JSONException {
		HashSet<String> topFour = new HashSet<String>();
		try{
			JSONArray results = getJSONArrayFromSearch(resultObj);
			for (int i= 0; i<results.length(); i++) {
				JSONObject obj  = results.getJSONObject(i);
				topFour.add(obj.getString("Url"));
				if (topFour.size() >= 4)
					break;
			}
		}
		catch(Exception e) {
			
		}
		
		return topFour;
	}
}
