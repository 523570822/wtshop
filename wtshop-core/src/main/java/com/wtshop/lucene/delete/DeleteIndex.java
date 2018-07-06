package com.wtshop.lucene.delete;

import java.io.IOException;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;

import com.wtshop.lucene.config.Config;

public class DeleteIndex {
	
	public void deleteIndex(String key){
		
	}
	
	public void deleteAllIndex() throws IOException{
		IndexWriterConfig writerConfig = new IndexWriterConfig(Config.ANALYZER);
		IndexWriter indexWriter = new IndexWriter(Config.DIRECTORY, writerConfig);
		try {
			indexWriter.deleteAll();
			indexWriter.commit();
		}finally {
			indexWriter.close();
		}
	}
}
