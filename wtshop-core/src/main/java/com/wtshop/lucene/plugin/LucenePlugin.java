package com.wtshop.lucene.plugin;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;

import com.alibaba.fastjson.JSONArray;
import com.jfinal.plugin.activerecord.Page;
import com.wtshop.lucene.add.AddIndex;
import com.wtshop.lucene.delete.DeleteIndex;
import com.wtshop.lucene.init.Init;
import com.wtshop.lucene.query.QueryIndex;
import com.wtshop.lucene.update.UpdateIndex;
import com.wtshop.model.Article;
import com.wtshop.model.Goods;

@SuppressWarnings("all")
public class LucenePlugin {
	
	public LucenePlugin(String path,String core,String AnalyzerType) throws IOException{
		//加载
		Init init = new Init(path, core, AnalyzerType);
		init.start();
	}
	
	/**
	 * 添加文章索引
	 * @param list
	 * @throws IOException
	 */
	public void addArticle(Article article) throws IOException{
		AddIndex addIndex = new AddIndex();
		addIndex.IndexArticle(article);
	}
	
	/**
	 * 添加货品索引
	 * @param list
	 * @throws IOException
	 */
	public void addGoods(Goods goods) throws IOException{
		AddIndex addIndex = new AddIndex();
		addIndex.IndexGoods(goods);
	}
	
	/**
	 * 更新索引
	 * @param list
	 * @throws IOException
	 */
	public void update(List<HashMap> list) throws IOException{
		UpdateIndex updateIndex = new UpdateIndex();
		updateIndex.Index(list);
	}
	
	/**
	 * 关键字查询索引
	 * @param v
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 * @throws InvalidTokenOffsetsException
	 */
	public JSONArray query(String v) throws IOException, ParseException, InvalidTokenOffsetsException{
		QueryIndex index = new QueryIndex();
		return index.Index(v);
	}
	
	/**
	 * 分页查询索引
	 * @param v
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 * @throws InvalidTokenOffsetsException
	 */
	public <T> Page<T> query(String v, int pageNumber, int pageSize, T obj) throws IOException, ParseException, InvalidTokenOffsetsException{
		QueryIndex index = new QueryIndex();
		return index.Index(v, pageNumber, pageSize, obj);
	}
	
	/**
	 * 清空索引
	 * @throws IOException
	 */
	public void deleteAll() throws IOException{
		DeleteIndex deleteIndex = new DeleteIndex();
		deleteIndex.deleteAllIndex();
	}

}
