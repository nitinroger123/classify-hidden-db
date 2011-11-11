package adb.project2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
	private HashMap<String, HashSet<String>> topFourMap;

	/**
	 * Entry point for our program.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		String bingAppID = "E69E241D81BD12B3CAB2FAC07061D2DA6C00117E";
		Double specificity = 0.6;
		Long coverage = (long) 100;
		String database = "nba.com";

		System.out.println("Classifying Database: " + database);
		System.out.println();
		ClassifyHiddenDB classifyHiddenDB = new ClassifyHiddenDB(bingAppID,
				specificity, coverage, database);
		try {
			Map<String, List<String>> queriesForClassification = QueryHelper
					.getQueriesForClassification("ROOT.txt", "Root");
			classifyHiddenDB.classifyDB("ROOT", queriesForClassification);
			classifyHiddenDB.printClassificationForDB(classifyHiddenDB);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	public ClassifyHiddenDB(String bingAppID, double tSpec, long tCover,
			String db) {
		this.databaseURL = db;
		this.bingAppId = bingAppID;
		this.t_specificity = new Double(tSpec);
		this.t_coverage = new Long(tCover);
		this.classificationsForDB = new ArrayList<String>();
		this.topFourMap = new HashMap<String, HashSet<String>>();
	}

	private void classifyDB(String currentType,
			Map<String, List<String>> queriesForClassification)
			throws JSONException, IOException {
		Classification.clearObjectMap();

		for (String classificationType : queriesForClassification.keySet()) {
			List<String> queries = queriesForClassification
					.get(classificationType);
			int numRelevent = 0;
			int numTotal = 0;
			JSONObject resultObj = null;
			Classification classification = Classification
					.getByType(classificationType);
			for (String q : queries) {
				resultObj = runSearch(bingAppId, q);
				/**
				 * save the top 4 result
				 * docs from each query.
				 */
				populateDocs(classificationType.substring(0, classificationType.lastIndexOf(':')), resultObj);
				classification.setCoverage(classification.getCoverage()
						+ JSONHelper.getTotalFromSearch(resultObj));
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		Classification.calculateSpecificity();
		// print specificity and coverage
		Classification.printClassifications(); 
		List<String> qualifyingClassifications = Classification
				.getQualifyingClassificationTypes(this.t_coverage,
						this.t_specificity);
		if (qualifyingClassifications == null
				|| qualifyingClassifications.size() <= 0) {
			this.classificationsForDB.add(currentType);
		} else {
			for (String s : qualifyingClassifications) {
				String nextLevel = s.substring(s.lastIndexOf(':') + 1);
				try {
					Map<String, List<String>> queriesForClassificationFurther = QueryHelper
							.getQueriesForClassification(nextLevel + ".txt", s);
					this.classifyDB(s, queriesForClassificationFurther);
				} catch (IOException e) {
					this.classificationsForDB.add(s);
					continue;
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}

	}
	
	/**
	 * Populate docs with the top 4 results
	 * @param resultObj
	 * @throws JSONException 
	 */
	private void populateDocs(String category, JSONObject resultObj) throws JSONException {
		HashSet<String> topFour = JSONHelper.getTopFourResults(resultObj);
		HashSet<String> curr = topFourMap.get(category);
		if (curr == null) {
			topFourMap.put(category, topFour);
		}
		else {
			curr.addAll(topFour);
			topFourMap.put(category, curr);
		}
		
	}

	private JSONObject runSearch(String appID, String query)
			throws JSONException, IOException {
		query = "site:" + this.databaseURL + " " + query;
		String requestURL = BingSearch.getRequestString(query, appID);
		JSONObject result = BingSearch.getSearchResults(requestURL);
		return result;
	}

	public void printClassificationForDB(
			ClassifyHiddenDB classifyHiddenDB) {
		System.out.println();
		System.out.println();
		System.out.println("Classification: ");
		for (String s : classifyHiddenDB.classificationsForDB) {
			System.out.println("-> " + s.replace(':', '/'));
		}
		
		/**
		 * Call the summarize method
		 */
		for (String s: topFourMap.keySet()) {
			System.out.println("KEY IS "+s);
			HashSet<String> set = topFourMap.get(s);
			for(String ss: set) {
				System.out.println(ss);
			}
		}
		ContentSummary.summarize(topFourMap, classifyHiddenDB.classificationsForDB, databaseURL);
		System.out.println();
		System.out.println();
	}

	/**
	 * sample searc routine.. to be removed
	 * 
	 * @param appID
	 * @param query
	 * @throws JSONException
	 * @throws IOException
	 * @deprecated
	 */
	@Deprecated
	private static void sampleRunSearch(String appID, String query)
			throws JSONException, IOException {

		String requestString = "http://api.search.live.net/json.aspx?"
				+ "Appid=" + appID + "&query=" + query + "&sources=Web"
				+ "&web.count=10";
		JSONObject result = BingSearch.getSearchResults(requestString);
		System.out.println(result.getJSONObject("SearchResponse")
				.getJSONObject("Web").get("Total"));
	}
}
