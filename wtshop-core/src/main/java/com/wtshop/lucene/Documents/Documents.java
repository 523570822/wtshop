package com.wtshop.lucene.Documents;

import java.util.List;
import java.util.Map;

import org.apache.lucene.document.BinaryDocValuesField;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.SortedDocValuesField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.util.BytesRef;

import com.wtshop.lucene.config.Config;
/**
 * 字段添加
 * @author Administrator
 *
 */
public class Documents {

	public Documents() {
		super();
	}

	private Document document=new Document();

	public void put(Object key,Object val){
		String keys = null;
		String vals = "";
		if(key != null){
			keys=key.toString();
		}
		if(val != null){
			vals=val.toString();
		}
		List<Map<String,String>> xmlDem = Config.xmlDem;
		for (Map<String, String> map : xmlDem) {
			if(map.get("name").equals(keys)){
				String type = map.get("type");
				if(type.equals("string")){
					addTextField(keys, vals);
				}else if(type.equals("int")){
					addIntPoint(keys, Integer.parseInt(vals));
				}
			}
		}
	}

	public Document getDocument(){
		return this.document;
	}
	
	public void addIntPoint(String name, int value) {
	    Field field = new IntPoint(name, value);
	    document.add(field);
	    //要排序，必须添加一个同名的NumericDocValuesField
	    field = new NumericDocValuesField(name, value);
	    document.add(field);
	    //要存储值，必须添加一个同名的StoredField
	    field = new StoredField(name, value);
	    document.add(field);
	}
	public void addBinaryDocValuesField(String name, String value) {
	    Field field = new BinaryDocValuesField(name, new BytesRef(value));
	    document.add(field);
	    //如果需要存储，加此句
	    field = new StoredField(name, value);
	    document.add(field);
	}
	public void addStringField(String name, String value) {
	    Field field = new StringField(name, value, Field.Store.YES);
	    document.add(field);
	    field = new SortedDocValuesField(name, new BytesRef(value));
	    document.add(field);
	}
	public void addTextField(String name, String value) {
	    Field field = new TextField(name, value, Field.Store.YES);
	    document.add(field);
	    field = new SortedDocValuesField(name, new BytesRef(value));
	    document.add(field);
	}


}
