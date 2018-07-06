package com.wtshop.lucene.Analyzer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.wtshop.lucene.Analyzer.AnsjAnalyzers.TYPE;

/**
 * 分词器集成
 * @author Administrator
 *
 */
public class AnalyzerPlugin {

	private String type="";

	public AnalyzerPlugin(String type){
		this.type=type;
	}

	public Analyzer getAnalyzer(){
		Analyzer analyzer = null;
		switch(this.type){
		/**   =======================     ansj      =====================   **/
		case AnalyzerType.ANSJBase : analyzer = new AnsjAnalyzers(TYPE.Base);
		break;
		case AnalyzerType.ANSJDic : analyzer = new AnsjAnalyzers(TYPE.dic);
		break;
		case AnalyzerType.ANSJIndex : analyzer = new AnsjAnalyzers(TYPE.index);
		break;
		case AnalyzerType.ANSJNlp : analyzer = new AnsjAnalyzers(TYPE.Nlp);
		break;
		case AnalyzerType.ANSJTO : analyzer = new AnsjAnalyzers(TYPE.to);
		break;
		
		/**   =======================     lucene      =====================   **/
		
		//英文
		case AnalyzerType.WhitespaceAnalyzer : analyzer = new WhitespaceAnalyzer();
		break;
		case AnalyzerType.SimpleAnalyzer : analyzer = new SimpleAnalyzer();
		break;
		case AnalyzerType.StopAnalyzer : analyzer = new StopAnalyzer();
		break;
		case AnalyzerType.StandardAnalyzer : analyzer = new StandardAnalyzer();
		break;
		
		//中文
		case AnalyzerType.CJKAnalyzer : analyzer = new CJKAnalyzer();
		break;
		case AnalyzerType.SmartChineseAnalyzer : analyzer = new SmartChineseAnalyzer();
		break;
		case AnalyzerType.KeywordAnalyzer : analyzer = new KeywordAnalyzer();
		break;
		
		/**   =======================     Ik 智能分词      =====================   **/
		case AnalyzerType.IKAnalyzer : analyzer = new IKAnalyzer(true);
		break;
		
		default: analyzer = new AnsjAnalyzers(TYPE.Base);
		}
		return analyzer;
	};
}
