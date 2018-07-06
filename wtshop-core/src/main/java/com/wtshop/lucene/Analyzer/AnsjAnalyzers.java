package com.wtshop.lucene.Analyzer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.ansj.lucene.util.AnsjTokenizer;
import org.ansj.splitWord.analysis.BaseAnalysis;
import org.ansj.splitWord.analysis.DicAnalysis;
import org.ansj.splitWord.analysis.IndexAnalysis;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;
import org.nlpcn.commons.lang.util.IOUtil;
import org.nlpcn.commons.lang.util.StringUtil;
import org.nlpcn.commons.lang.util.logging.Log;
import org.nlpcn.commons.lang.util.logging.LogFactory;

import com.jfinal.kit.LogKit;

/**
 * 重写分词器
 * 
 * @author Administrator
 *
 */
public class AnsjAnalyzers extends Analyzer {

	public final Log	logger	= LogFactory.getLog(AnsjAnalyzers.class);

	/**
	 * dic equals user , query equals to
	 * 
	 * @author ansj
	 *
	 */
	public static enum TYPE {
		index, query, to, dic, user, search, Nlp, Base
	}

	private Set<String>	filter;
	private TYPE		type;

	public AnsjAnalyzers(TYPE type, Set<String> filter) {
		this.type = type;
		this.filter = filter;
	}

	public AnsjAnalyzers(TYPE type, String stopwordsDir) {
		this.type = type;
		this.filter = filter(stopwordsDir);
	}

	public AnsjAnalyzers(TYPE type) {
		this.type = type;
	}

	public AnsjAnalyzers() {
		this.type = TYPE.query;
	}

	private Set<String> filter(String stopwordsDir) {
		if (StringUtil.isBlank(stopwordsDir)) {
			return null;
		}
		try {
			List<String> readFile2List = IOUtil.readFile2List(stopwordsDir, IOUtil.UTF8);
			return new HashSet<String>(readFile2List);
		} catch (FileNotFoundException e) {
			LogKit.warn("FileNotFound" + e);
		} catch (UnsupportedEncodingException e) {
			LogKit.warn("UnsupportedEncoding" + e);
		}
		return null;
	}

	@Override
	protected TokenStreamComponents createComponents(String text) {
		BufferedReader reader = new BufferedReader(new StringReader(text));
		Tokenizer tokenizer = null;

		tokenizer = getTokenizer(reader, this.type, this.filter);
		return new TokenStreamComponents(tokenizer);
	}

	public static Tokenizer getTokenizer(BufferedReader reader, TYPE type, Set<String> filter) {
		Tokenizer tokenizer;

		switch (type) {
		case index:
			if (reader == null) {
				tokenizer = new AnsjTokenizer(new IndexAnalysis(), filter);
			} else {
				tokenizer = new AnsjTokenizer(new IndexAnalysis(reader), filter);
			}
			break;
		case dic:
			if (reader == null) {
				tokenizer = new AnsjTokenizer(new DicAnalysis(), filter);
			} else {
				tokenizer = new AnsjTokenizer(new DicAnalysis(reader), filter);
			}
			break;

		case to:
			if (reader == null) {
				tokenizer = new AnsjTokenizer(new ToAnalysis(), filter);
			} else {
				tokenizer = new AnsjTokenizer(new ToAnalysis(reader), filter);
			}
			break;
		case Nlp:
			if (reader == null) {
				tokenizer = new AnsjTokenizer(new NlpAnalysis(), filter);
			} else {
				tokenizer = new AnsjTokenizer(new NlpAnalysis(reader), filter);
			}
			break;
		case Base:
			if (reader == null) {
				tokenizer = new AnsjTokenizer(new BaseAnalysis(), filter);
			} else {
				tokenizer = new AnsjTokenizer(new BaseAnalysis(reader), filter);
			}
			break;
		default:
			if (reader == null) {
				tokenizer = new AnsjTokenizer(new ToAnalysis(), filter);
			} else {
				tokenizer = new AnsjTokenizer(new ToAnalysis(reader), filter);
			}
		}

		return tokenizer;
	}
}