package com.wtshop.lucene.update;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;

import com.wtshop.lucene.Documents.Documents;
import com.wtshop.lucene.config.Config;
@SuppressWarnings("all")
public class UpdateIndex {
	/**
	 * 更新索引
	 * @param list
	 * @throws IOException
	 */
	public void Index(List<HashMap> list) throws IOException{
		IndexWriterConfig conf = new IndexWriterConfig(Config.ANALYZER);
		IndexWriter writer = new IndexWriter(Config.DIRECTORY, conf);
		try {
			for (HashMap hashMap : list) {
				Documents doc = new Documents();
				Iterator iterator = hashMap.entrySet().iterator();
				String id=null;
				while (iterator.hasNext()) {
					Map.Entry entry = (Map.Entry) iterator.next();
					doc.put(entry.getKey(), entry.getValue());
					if(entry.getKey().equals("id")){
						id=entry.getValue().toString();
					}
				}
				if(id != null){
					writer.updateDocument(new Term("id",id), doc.getDocument());
				}
			}
			writer.commit();
		} catch (Exception e) {
			throw e;
		}finally {
			writer.close();
		}
	}
}
