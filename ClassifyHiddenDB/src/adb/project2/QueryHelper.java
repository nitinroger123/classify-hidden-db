package adb.project2;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryHelper {

	/**
	 * get the queries from the file name and forms a hashmap as follows <br/>
	 *  "type" - list of query Strings for this type. 
	 * @param filename
	 * @throws IOException 
	 */
	public static Map<String, List<String>> getQueriesForClassification(String fileName) throws IOException{
		Map<String, List<String>> queriesForClassification = new HashMap<String, List<String>>();
		
		BufferedReader br = new BufferedReader(
				new InputStreamReader(new BufferedInputStream(
						new FileInputStream(new File(fileName)))));
		String tmpLineStr = null;
		while (true) {
			tmpLineStr = br.readLine();
			if (tmpLineStr == null || tmpLineStr.isEmpty()) {
				break;
			}
			getQueryFromLine(queriesForClassification, tmpLineStr);
		}
		br.close();
		
		printQueriesForClassification(fileName, queriesForClassification);
		return queriesForClassification;
	}

	/**
	 * converts the line string to a query format.
	 * @param queriesForClassification
	 * @param tmpLineStr
	 */
	private static void getQueryFromLine(Map<String, List<String>> queriesForClassification, String tmpLineStr){
		int spaceIndex = tmpLineStr.indexOf(' ');
		String dbType, query;
		if(spaceIndex==-1){
			System.err.println(" No -1 ");
			System.exit(0);
		}
		dbType = tmpLineStr.substring(0, spaceIndex).trim().toUpperCase();
		query = tmpLineStr.substring(spaceIndex+1).trim().toLowerCase();
		
		if(queriesForClassification.containsKey(dbType)){
			queriesForClassification.get(dbType).add(query);
		} else {
			queriesForClassification.put(dbType, new ArrayList<String>());
			queriesForClassification.get(dbType).add(query);
		}
	}
	
	private static void printQueriesForClassification(String fileName, Map<String, List<String>> queriesForClassification){
		System.out.println("---printQueriesForClassification Start-----------------");
		System.out.println("from file=" + fileName);
		for(String clType : queriesForClassification.keySet()){
			List<String> queries = queriesForClassification.get(clType);
			System.out.println("Cl Type = " + clType);
			for(String q : queries){
				System.out.println(" \t -" + q);
			}
		}
		System.out.println("---printQueriesForClassification End-----------------");
	}
	
}
