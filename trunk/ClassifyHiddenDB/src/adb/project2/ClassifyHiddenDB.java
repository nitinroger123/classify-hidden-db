package adb.project2;

import java.awt.image.SampleModel;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("unused")
public class ClassifyHiddenDB {

	private String databaseURL = null;
	private Double t_specificity = null;
	private Integer t_coverage = null;
	private String bingAppId = null;
	
	/**
	 * Entry point for our program.
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("main");
		String bingAppID = "E69E241D81BD12B3CAB2FAC07061D2DA6C00117E";
		Double specificity = 0.5;
		Integer coverage = 100;
		String database = "java.sun.com";
		
		ClassifyHiddenDB classifyHiddenDB = new ClassifyHiddenDB(bingAppID, specificity, coverage, database);
		try {
			Map<String, List<String>> queriesForClassification = QueryHelper.getQueriesForClassification("Root.txt");
			classifyHiddenDB.classifyDB("root", queriesForClassification);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		Classification.printClassifications(" -FINAL- ");
		
//		try {
//			sampleRunSearch(bingAppID, "maps");
//		} catch (JSONException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		
	}
	
	
	public ClassifyHiddenDB(String bingAppID, double tSpec, int tCover, String db) {
		this.databaseURL = db;
		this.bingAppId = bingAppID;
		this.t_specificity = new Double(tSpec);
		this.t_coverage = new Integer(tCover);
	}

	
	private void classifyDB(String currentType, Map<String, List<String>> queriesForClassification) throws JSONException, IOException{
		
		for(String classificationType : queriesForClassification.keySet()){
			List<String> queries = queriesForClassification.get(classificationType);
			int numRelevent = 0;
			int numTotal = 0;
			JSONObject resultObj = null;
			Classification classification = Classification.getByType(classificationType);
			for(String q : queries){
				System.out.println(" \t -" + q);
				resultObj = runSearch(bingAppId, q);
				System.out.println(classificationType + " : " + q + " :: total=" + JSONHelper.getTotalFromSearch(resultObj));
				classification.setReleventDocs(classification.getReleventDocs() + JSONHelper.getTotalFromSearch(resultObj));
				Classification.printClassifications(" -1- ");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	
	private JSONObject runSearch(String appID, String query) throws JSONException, IOException {
		query = "site:" + this.databaseURL + " " + query;
		String requestURL = BingSearch.getRequestString(query, appID);
		JSONObject result = BingSearch.getSearchResults(requestURL);
		return result;
	}
	
	/**
	 * sample searc routine.. to be removed
	 * @param appID
	 * @param query
	 * @throws JSONException
	 * @throws IOException
	 * @deprecated
	 */
	@Deprecated
	private static void sampleRunSearch(String appID, String query) throws JSONException, IOException {

		String requestString = "http://api.search.live.net/json.aspx?" + "Appid=" + appID + "&query="+query 
				+ "&sources=Web" + "&web.count=10";
		JSONObject result = BingSearch.getSearchResults(requestString);
		System.out.println(result.getJSONObject("SearchResponse").getJSONObject("Web").get("Total"));
	}
}
