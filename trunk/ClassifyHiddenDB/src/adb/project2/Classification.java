package adb.project2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Classification {
	
	private static Map<String, Classification> OBJECT_MAP = new HashMap<String, Classification>();
	private String classificatonType;
	private Double specificity;
	private Long coverage;
	
	
	private Classification(String type) {
		this.classificatonType = type;
		this.specificity = 0.0;
		this.coverage = (long) 0;
	}
	
	/**
	 * returns a Classification object based on the given type.
	 * @param type
	 * @return
	 */
	public static Classification getByType(String type){
		type = type.toUpperCase();
		if(OBJECT_MAP.containsKey(type)){
			return OBJECT_MAP.get(type);
		} else {
			OBJECT_MAP.put(type, new Classification(type));
			return OBJECT_MAP.get(type);
		}
	}
	
	/**
	 * returns the types of classsifications that this DB qualifies for. 
	 * @param tc
	 * @param ts
	 * @return
	 */
	public static List<String> getQualifyingClassificationTypes(long tc, double ts){
		List<String> qualifyingClassifications = new ArrayList<String>();
		Classification refClass = null;
		for(String refClassType : OBJECT_MAP.keySet()){
			refClass = OBJECT_MAP.get(refClassType);;
			if(refClass.getCoverage() >= tc && refClass.getSpecificity() >= ts){
				qualifyingClassifications.add(refClass.getClassificatonType());
			}
		}
		return qualifyingClassifications;
	}
	
	/**
	 * calculates specificity for all Classifications
	 */
	public static void calculateSpecificity(){
		long totalDocs = getTotalDocs();
		Classification refClass = null;
		for(String refClassType : OBJECT_MAP.keySet()){
			refClass = OBJECT_MAP.get(refClassType);;
			double a = refClass.getCoverage()/totalDocs;
			System.out.println("for refClass=" + refClassType + "--" + refClass.getCoverage() + ", " + totalDocs + ", " + a);
			refClass.setSpecificity(a);
		}
	}
	
	/**
	 * returns the total number of docs returned by BING for the given queries.
	 * @return
	 */
	public static long getTotalDocs(){
		long totalDocs = 0;
		for(String s : OBJECT_MAP.keySet()){
			totalDocs += OBJECT_MAP.get(s).getCoverage();
		}
		return totalDocs;
	}
	

	public String getClassificatonType() {
		return classificatonType;
	}
	public void setClassificatonType(String classificatonType) {
		this.classificatonType = classificatonType;
	}
	public Double getSpecificity() {
		return specificity;
	}
	public void setSpecificity(Double specificity) {
		this.specificity = specificity;
	}
	public Long getCoverage() {
		return coverage;
	}
	public void setCoverage(Long coverage) {
		this.coverage = coverage;
	}

	@Override
	public String toString() {
		return "[Classification] classificatonType=" + getClassificatonType() 
				+ ", coverage=" + getCoverage() + ", specificity=" + getSpecificity();
	}
	
	/**
	 * prints all available classifications. Used for Debugging.
	 * @param someStrToIdentify
	 */
	public static void printClassifications(String someStrToIdentify){
		for(String s : OBJECT_MAP.keySet()){
			System.out.print(someStrToIdentify + " : ");
			System.out.println(OBJECT_MAP.get(s));
		}
	}
}
