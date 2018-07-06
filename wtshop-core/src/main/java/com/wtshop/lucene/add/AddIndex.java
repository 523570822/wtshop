package com.wtshop.lucene.add;

import java.io.IOException;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;

import com.wtshop.lucene.Documents.Documents;
import com.wtshop.lucene.config.Config;
import com.wtshop.model.Article;
import com.wtshop.model.Goods;

public class AddIndex {

	/**
	 * 添加文章索引
	 * @param list
	 * @throws IOException
	 */
	public void IndexArticle(Article article) throws IOException{
		IndexWriterConfig conf = new IndexWriterConfig(Config.ANALYZER);
		IndexWriter writer = new IndexWriter(Config.DIRECTORY, conf);
		try {
			Documents doc = new Documents();
			doc.put("id", article.getId());
			doc.put("title", article.getTitle());
			doc.put("content", article.getContent());
			doc.put("is_publication", article.getIsPublication());
			doc.put("create_date", article.getCreateDate());
			doc.put("author", article.getAuthor());
			doc.put("text", article.getText());
			writer.addDocument(doc.getDocument());
			writer.commit();
		} catch (Exception e) {
			throw e;
		}finally {
			writer.close();
		}
	}
	
	/**
	 * 添加货品索引
	 * @param list
	 * @throws IOException
	 */
	public void IndexGoods(Goods goods) throws IOException{
		IndexWriterConfig conf = new IndexWriterConfig(Config.ANALYZER);
		IndexWriter writer = new IndexWriter(Config.DIRECTORY, conf);
		try {
			Documents doc = new Documents();
			doc.put("id", goods.getId());
			doc.put("sn", goods.getSn());
			doc.put("keyword", goods.getKeyword());
			doc.put("name", goods.getName());
			doc.put("caption", goods.getCaption());
			doc.put("type", goods.getType());
			doc.put("thumbnail", goods.getThumbnail());
			doc.put("product_images", goods.getProductImages());
			doc.put("is_marketable", goods.getIsMarketable());
			doc.put("is_list", goods.getIsList());
			doc.put("price", goods.getPrice());
			doc.put("is_top", goods.getIsTop());
			doc.put("sales", goods.getSales());
			doc.put("score", goods.getScore());
			doc.put("create_date", goods.getCreateDate());
			writer.addDocument(doc.getDocument());
			writer.commit();
		} catch (Exception e) {
			throw e;
		}finally {
			writer.close();
		}
	}
}
