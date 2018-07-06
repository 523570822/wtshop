package com.wtshop.lucene.Analyzer;

public class AnalyzerType {
	
	
	/**    ================= ansj 分词器  ==========================      **/
	
	
	public static final String ANSJTO="ToAnalysis";
	public static final String ANSJDic="DicAnalysis";
	public static final String ANSJNlp="NlpAnalysis";
	public static final String ANSJIndex="IndexAnalysis";
	public static final String ANSJBase="BaseAnalysis";
	
	
	
	/**    ================= lucene 自带分词器  ==========================      **/
	
	//英文适用
	public static final String WhitespaceAnalyzer ="WhitespaceAnalyzer";
	public static final String SimpleAnalyzer ="SimpleAnalyzer";
	public static final String StopAnalyzer ="StopAnalyzer";
	public static final String StandardAnalyzer="StandardAnalyzer";


	//中文适用
	public static final String CJKAnalyzer="CJKAnalyzer";
	public static final String SmartChineseAnalyzer="SmartChineseAnalyzer";
	public static final String KeywordAnalyzer ="KeywordAnalyzer";
	
	
	/**    =====================  IK 分词器     ===========================		**/
	
	public static final String IKAnalyzer ="IKAnalyzer";

	
	
}
