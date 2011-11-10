package adb.project2;

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
	private Long t_coverage = null;
	private String bingAppId = null;
	private List<String> classificationsForDB = null;
	
	/**
	 * Entry point for our program.
	 * @param args
	 */
	public static void main(String[] args) {
		String bingAppID = "E69E241D81BD12B3CAB2FAC07061D2DA6C00117E";
		Double specificity = 0.6;
		Long coverage = (long)100;
		String database = "hardwarecentral.com";
		
		System.out.println("Classifying Database: " + database);
		System.out.println();
		ClassifyHiddenDB classifyHiddenDB = new ClassifyHiddenDB(bingAppID, specificity, coverage, database);
		try {
			Map<String, List<String>> queriesForClassification = QueryHelper.getQueriesForClassification("ROOT.txt", "Root");
			classifyHiddenDB.classifyDB("ROOT", queriesForClassification);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
//		Classification.printClassifications(" -FINAL- ");
		
		printClassificationForDB(classifyHiddenDB);
		
	}
	
	
	public ClassifyHiddenDB(String bingAppID, double tSpec, long tCover, String db) {
		this.databaseURL = db;
		this.bingAppId = bingAppID;
		this.t_specificity = new Double(tSpec);
		this.t_coverage = new Long(tCover);
		this.classificationsForDB = new ArrayList<String>();
	}

	
	private void classifyDB(String currentType, Map<String, List<String>> queriesForClassification) throws JSONException, IOException{
		Classification.clearObjectMap();
		
		for(String classificationType : queriesForClassification.keySet()){
			List<String> queries = queriesForClassification.get(classificationType);
			int numRelevent = 0;
			int numTotal = 0;
			JSONObject resultObj = null;
			Classification classification = Classification.getByType(classificationType);
			for(String q : queries){
//				System.out.println(" \t -" + q);
				resultObj = runSearch(bingAppId, q);
//				System.out.println(classificationType + " : " + q + " :: total=" + JSONHelper.getTotalFromSearch(resultObj));
				classification.setCoverage(classification.getCoverage() + JSONHelper.getTotalFromSearch(resultObj));
//				Classification.printClassifications(" -1- ");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		Classification.calculateSpecificity();
		Classification.printClassifications();		// print specificity and coverage for each classification.
		
//		Classification.printClassifications(" -After "+currentType+"- ");
		
		List<String> qualifyingClassifications = Classification.getQualifyingClassificationTypes(this.t_coverage, this.t_specificity);
		if(qualifyingClassifications == null || qualifyingClassifications.size() <= 0){
//			System.out.println(" Couldnt classify the DB More.");
//			System.out.println(" Current level of classification is " + currentType);
			this.classificationsForDB.add(currentType);
		} else {
//			System.out.println(" Going deeper to the next level of classification ");
			for(String s : qualifyingClassifications){
//				System.out.println(" DB Classified as --> " + s);
				String nextLevel = s.substring(s.lastIndexOf(':') + 1);
				try {
					Map<String, List<String>> queriesForClassificationFurther = QueryHelper.getQueriesForClassification(nextLevel+".txt", s);
					this.classifyDB(s, queriesForClassificationFurther);
				} catch (IOException e) {
//					e.printStackTrace();
					this.classificationsForDB.add(s);
					continue;
				} catch (JSONException e) {
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
	
	
	public static void printClassificationForDB(ClassifyHiddenDB classifyHiddenDB){
		System.out.println();
		System.out.println();
		System.out.println("Classification: ");
		for(String s : classifyHiddenDB.classificationsForDB){
			System.out.println("-> " + s.replace(':', '/'));
		}
		System.out.println();
		System.out.println();
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
