package adb.project2;

import java.util.HashMap;
import java.util.Map;

public class Classification {
	
	private static Map<String, Classification> OBJECT_MAP = new HashMap<String, Classification>();
	private String classificatonType;
	private Double spec;
	private Long releventDocs;
	
	
	private Classification(String type) {
		this.classificatonType = type;
		this.spec = 0.0;
		this.releventDocs = (long) 0;
	}
	
	public static Classification getByType(String type){
		type = type.toUpperCase();
		if(OBJECT_MAP.containsKey(type)){
			return OBJECT_MAP.get(type);
		} else {
			OBJECT_MAP.put(type, new Classification(type));
			return OBJECT_MAP.get(type);
		}
	}
	
	
	
	public String getClassificatonType() {
		return classificatonType;
	}
	public void setClassificatonType(String classificatonType) {
		this.classificatonType = classificatonType;
	}
	public Double getSpec() {
		return spec;
	}
	public void setSpec(Double spec) {
		this.spec = spec;
	}
	public Long getReleventDocs() {
		return releventDocs;
	}
	public void setReleventDocs(Long releventDocs) {
		this.releventDocs = releventDocs;
	}
	
	@Override
	public String toString() {
		return "[Classification] classificatonType=" + getClassificatonType() + ", releventDocs=" + getReleventDocs();
	}
	
	public static void printClassifications(String someStrToIdentify){
		for(String s : OBJECT_MAP.keySet()){
			System.out.print(someStrToIdentify + " : ");
			System.out.println(OBJECT_MAP.get(s));
		}
	}
}
