package adb.project2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

public class ContentSummary {

	private static HashMap<String, TreeMap<String, Integer>> summaryMap = new HashMap<String, TreeMap<String,Integer>>(); 
	private static final String ROOT = "ROOT";
	
	/**
	 * Takes the HashMap with the top four docs for each query in each category
	 * and and calls lynx to summarize it.
	 * 
	 * @param topFourMap
	 * @param classification
	 */
	public static void summarize(HashMap<String, HashSet<String>> topFourMap,
			List<String> classifications, String dbName) {
		/**
		 * Merge With Root
		 */
		for (String classification : classifications) {
			String category = extractCategoryName(classification);
			mergeDocs(topFourMap, category);
		}
		
		/**
		 * Generate the Root-dbName.txt
		 */
		for(String docURL: topFourMap.get(ROOT)) {
			addToSummaryMap(ROOT,LynxHelper.runLynx(docURL));
		}
		
		for (String classification: classifications) {
			String category = extractCategoryName(classification);
			for(String docUrl: topFourMap.get(category)) {
				addToSummaryMap(category, LynxHelper.runLynx(docUrl));
			}
		}
		
		try {
			printSummary(dbName);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Make ROOT:SPORTS:BASKETBALL
	 * ROOT:SPORTS
	 * @param classification
	 * @return
	 */
	private static String extractCategoryName(String classification) {
		String [] arr = classification.split(":");
		String category = classification;
		if (arr.length > 2) {
			category = arr[0]+":"+arr[1];
		}
		return category;
	}
	
	private static void printSummary(String dbName) throws IOException {
		
		for(String s: summaryMap.keySet()) {
			File f = new File(s.replaceAll(":", "-")+"-"+dbName+".txt");
			if(f.exists()) {
				f.delete();
			}
			f.createNewFile();
			BufferedWriter writer = new BufferedWriter(new FileWriter(f));
			TreeMap<String, Integer> map = summaryMap.get(s);
			for (String ss: map.keySet()) {
				writer.write(ss+"#"+map.get(ss)+"\n");
			}
		}
	}
	
	/**
	 * Add each word identified by Lynx to the summary Map
	 * @param category
	 * @param words
	 */
	private static void addToSummaryMap(String category, Set<String> words) {
		
		if (summaryMap.get(category) == null) {
			summaryMap.put(category, new TreeMap<String, Integer>());
		}
		
		for (String s : words) {
			Integer count = summaryMap.get(category).get(s);
			if (count == null) {
				summaryMap.get(category).put(s, 1);
			}
			else {
				summaryMap.get(category).put(s, count+1);
			}
		}
	}

	/**
	 * Merge Root with the sub category that has been identified
	 * Ex is Root/Sports then merge the docs in sports with the ones in root
	 * @param topFourMap
	 * @param classification
	 */
	private static void mergeDocs(HashMap<String, HashSet<String>> topFourMap,
			String classification) {
		
		HashSet<String> rootDocs = topFourMap.get(ROOT);
		HashSet<String> subCatDocs = topFourMap.get(classification);
		rootDocs.addAll(subCatDocs);
		topFourMap.put(ROOT, rootDocs);
		
	}

}
