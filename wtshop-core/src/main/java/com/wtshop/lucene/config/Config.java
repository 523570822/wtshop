package com.wtshop.lucene.config;

import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.store.FSDirectory;


public class Config {
	public static Analyzer					ANALYZER	= null;
	public static FSDirectory				DIRECTORY	= null;
	public static List<Map<String, String>>	xmlDem		= null;
	public static List<Map<String, String>>	isQuery		= null;

}
