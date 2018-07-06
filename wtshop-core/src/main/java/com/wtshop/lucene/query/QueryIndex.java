package com.wtshop.lucene.query;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.SortField.Type;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopFieldCollector;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Page;
import com.wtshop.lucene.config.Config;

public class QueryIndex {
	
	/**
	 * 关键字查询
	 * @param v
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 * @throws InvalidTokenOffsetsException
	 */
	public JSONArray Index(String v) throws IOException, ParseException, InvalidTokenOffsetsException{
		DirectoryReader ireader = DirectoryReader.open(Config.DIRECTORY);  
		IndexSearcher searcher = new IndexSearcher(ireader);   
		try {
			Query query = new MultiFieldQueryParser(queryField(), Config.ANALYZER).parse(v);
//			TopDocs rs = searcher.search(query,100000);    
			TopDocs rs = searcher.search(query,100000,sortField());
			QueryScorer scorer=new QueryScorer(query);  
			return dataHandle(rs, scorer, searcher);
		}finally {
			ireader.close();
		}
	}
	
	/**
	 * 分页查询
	 * @param v
	 * @param pageNumber
	 * @param pageSize
	 * @param obj
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 * @throws InvalidTokenOffsetsException
	 */
	public <T> Page<T> Index(String v, int pageNumber, int pageSize, T obj) throws IOException, ParseException, InvalidTokenOffsetsException {
		DirectoryReader ireader = DirectoryReader.open(Config.DIRECTORY);
		IndexSearcher searcher = new IndexSearcher(ireader);
		Page<T> pages;
		try {
			Query query = new MultiFieldQueryParser(queryField(), Config.ANALYZER).parse(v);
			TopFieldCollector create = TopFieldCollector.create(sortField(), pageNumber + pageSize, false, false, false);
			searcher.search(query, create);
			int totalHits = create.getTotalHits();
			ScoreDoc[] scoreDocs = create.topDocs(pageNumber, pageSize).scoreDocs;
			QueryScorer scorer = new QueryScorer(query);
			List<T> dataHandle = dataHandle(scoreDocs, scorer, searcher, obj);
			int totalPage = (int) (totalHits / pageSize);
			if (totalHits % pageSize != 0) {
				totalPage++;
			}
			pages = new Page<T>(dataHandle, pageNumber, pageSize, totalPage, totalHits);
		} finally {
			ireader.close();
		}
		return pages;
	}
	
	/**
	 * 查询字段解析
	 * @return
	 */
	private String[] queryField() {
		String str = "";
		for (Map<String, String> map : Config.isQuery) {
			str += map.get("name") + ",";
		}
		str = str.substring(0, str.length() - 1);
		return str.split(",");
	}
	
	/**
	 * 排序器
	 */
	private Sort sortField() {
		Sort sort = new Sort();
		List<Map<String, String>> xmlDem = Config.xmlDem;
		List<SortField> list = new ArrayList<>();
		for (Map<String, String> map : xmlDem) {
			String isSort = map.get("isSort");
			if (StringUtils.isNotEmpty(isSort)) {
				if (isSort.indexOf("y") != -1) {
					SortField intValues = null;
					String name = map.get("name");
					String type = map.get("type");
					String[] split = isSort.split("-");
					boolean flag = false;
					if (split.length > 1) {
						String str = split[1];
						if (str.equals("y")) {
							flag = true;
						}
					}
					switch (type) {
					case "int":
						intValues = new SortField(name, Type.INT, flag);
						break;
					case "string":
						intValues = new SortField(name, Type.STRING, flag);
						break;
					default:
						intValues = new SortField(name, Type.STRING, flag);
					}
					list.add(intValues);
				}
			}
		}
		switch (list.size()) {
		case 1:
			sort.setSort(list.get(0));
			break;
		case 2:
			sort.setSort(list.get(0), list.get(1));
			break;
		case 3:
			sort.setSort(list.get(0), list.get(1), list.get(2));
			break;
		case 4:
			sort.setSort(list.get(0), list.get(1), list.get(2), list.get(3));
			break;
		case 5:
			sort.setSort(list.get(0), list.get(1), list.get(2), list.get(3), list.get(4));
			break;
		case 6:
			sort.setSort(list.get(0), list.get(1), list.get(2), list.get(3), list.get(4), list.get(5));
			break;
		case 7:
			sort.setSort(list.get(0), list.get(1), list.get(2), list.get(3), list.get(4), list.get(5), list.get(6));
			break;
		case 8:
			sort.setSort(list.get(0), list.get(1), list.get(2), list.get(3), list.get(4), list.get(5), list.get(6), list.get(7));
			break;
		default:
			sort.setSort(new SortField("id", Type.INT, false));
			break;
		}
		return sort;
	}
	
	/**
	 * 处理数据
	 * @param name
	 * @param value
	 * @param scorer
	 * @param isQuery
	 * @return
	 * @throws IOException
	 * @throws InvalidTokenOffsetsException
	 */
	private String addHighlight(String name, String value, QueryScorer scorer, String isQuery) throws IOException, InvalidTokenOffsetsException {
		if (isQuery.equals("y")) {
			SimpleHTMLFormatter formatter = new SimpleHTMLFormatter("", "");
			//SimpleHTMLFormatter formatter = new SimpleHTMLFormatter("<font color='red'>","</font>");
			Fragmenter fragmenter = new SimpleSpanFragmenter(scorer);
			Highlighter highlight = new Highlighter(formatter, scorer);
			highlight.setTextFragmenter(fragmenter);
			TokenStream tokenStream = Config.ANALYZER.tokenStream(name, new StringReader(value));
			String bestFragment = highlight.getBestFragment(tokenStream, value);
			if (StringUtils.isNotEmpty(bestFragment)) {
				return bestFragment;
			} else {
				return value;
			}
		} else {
			return value;
		}

	}
	
	/**
	 * json数组 处理器
	 * 
	 * @throws InvalidTokenOffsetsException
	 * @throws IOException
	 */
	private JSONArray dataHandle(TopDocs rs, QueryScorer scorer, IndexSearcher searcher) throws IOException, InvalidTokenOffsetsException {
		JSONArray array = new JSONArray();
		ScoreDoc[] scoreDocs = rs.scoreDocs;
		for (ScoreDoc scoreDoc : scoreDocs) {
			float relativeScore = scoreDoc.score;
			Document doc = searcher.doc(scoreDoc.doc);
			doc.add(new StringField("relativeScore", relativeScore + "", Field.Store.NO));
			JSONObject jsonObject = new JSONObject();
			List<Map<String, String>> xmlDem = Config.xmlDem;
			for (Map<String, String> map : xmlDem) {
				String name = map.get("name");
				String isQuery = map.get("isQuery");
				String value = doc.get(name);
				if (StringUtils.isEmpty(value)) {
					jsonObject.put(name, value);
				} else {
					jsonObject.put(name, addHighlight(name, value, scorer, isQuery));
				}
				jsonObject.put("relativeScore", doc.get("relativeScore"));
			}
			array.add(jsonObject);
		}
		return array;
	}


	/**
	 * json数组 处理器
	 * @param <T>
	 * @throws InvalidTokenOffsetsException 
	 * @throws IOException 
	 */
	@SuppressWarnings("unchecked")
	private <T> List<T> dataHandle(ScoreDoc[] scoreDocs, QueryScorer scorer, IndexSearcher searcher, T obj) throws IOException, InvalidTokenOffsetsException {
		List<T> list = new ArrayList<T>();
		for (ScoreDoc scoreDoc : scoreDocs) {
			float relativeScore = scoreDoc.score;
			Document doc = searcher.doc(scoreDoc.doc);
			doc.add(new StringField("relativeScore", relativeScore + "", Field.Store.NO));
			JSONObject jsonObject = new JSONObject();
			List<Map<String, String>> xmlDem = Config.xmlDem;
			for (Map<String, String> map : xmlDem) {
				String name = map.get("name");
				if (name.equals("relativeScore")) {
					continue;
				}
				String isQuery = map.get("isQuery");
				String value = doc.get(name);
				if (StringUtils.isEmpty(value)) {
					jsonObject.put(name, value);
				} else {
					jsonObject.put(name, addHighlight(name, value, scorer, isQuery));
				}
				jsonObject.put("relativeScore", doc.get("relativeScore"));
			}
			T value = (T) JSONObject.parseObject(jsonObject.toJSONString(), obj.getClass());
			list.add(value);
		}
		return list;
	}

	/** 
	 * @Title: searchTotalRecord 
	 * @Description: 获取符合条件的总记录数 
	 * @param query 
	 * @return 
	 * @throws IOException 
	 */  
	public static int searchTotalRecord(IndexSearcher search, Query query) {
		ScoreDoc[] docs = null;
		try {
			TopDocs topDocs = search.search(query, Integer.MAX_VALUE);
			if (topDocs == null || topDocs.scoreDocs == null || topDocs.scoreDocs.length == 0) {
				return 0;
			}
			docs = topDocs.scoreDocs;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return docs.length;
	}
}
