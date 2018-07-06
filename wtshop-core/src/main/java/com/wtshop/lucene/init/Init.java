package com.wtshop.lucene.init;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.store.FSDirectory;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.wtshop.lucene.Analyzer.AnalyzerPlugin;
import com.wtshop.lucene.config.Config;

@SuppressWarnings("all")
public class Init {

	private String	path			= null;
	private String	AnalyzerTypes	= null;

	public Init() {
		super();
	}

	public Init(String path, String core, String AnalyzerType) {
		this.path = path + core;
		this.AnalyzerTypes = AnalyzerType;
	}

	public void start() throws IOException {
		Config.ANALYZER = new AnalyzerPlugin(this.AnalyzerTypes).getAnalyzer();
		Path path = Paths.get(this.path + "/data");
		Config.DIRECTORY = FSDirectory.open(path);
		Config.xmlDem = xmlDem(this.path + "/conf/config.xml", null);
		Config.isQuery = xmlDem(this.path + "/conf/config.xml", "1");
	}

	/**
	 * 解析配置文件
	 * 
	 * @param conf
	 * @return
	 */
	public static List<Map<String, String>> xmlDem(String conf, String type) {
		SAXReader saxReader = new SAXReader();
		File file = new File(conf);
		org.dom4j.Document read;
		List<Map<String, String>> list = new ArrayList<>();
		try {
			read = saxReader.read(file);
			Element rootElement = read.getRootElement();
			List<Element> elements = rootElement.elements();
			for (Element element : elements) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("name", element.attributeValue("name"));
				map.put("type", element.attributeValue("type"));
				map.put("isSort", element.attributeValue("isSort"));
				String isQuery = element.attributeValue("isQuery");
				if (type == null) {
					map.put("isQuery", element.attributeValue("isQuery"));
				} else {
					if (type.equals("1") && isQuery.equals("y")) {// 过滤要查询的数据
						map.put("isQuery", element.attributeValue("isQuery"));
					} else {
						continue;
					}
				}
				list.add(map);
			}
			return list;
		} catch (DocumentException e) {
			return null;
		}
	}

}
