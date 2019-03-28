package com.wtshop.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.wtshop.dao.GoodsDao;
import com.wtshop.model.Area;
import com.wtshop.model.ProductCategory;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;

import com.jfinal.aop.Enhancer;
import com.jfinal.kit.LogKit;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.wtshop.CommonAttributes;
import com.wtshop.Pageable;
import com.wtshop.dao.ArticleDao;
import com.wtshop.lucene.Analyzer.AnalyzerType;
import com.wtshop.lucene.plugin.LucenePlugin;
import com.wtshop.model.Article;
import com.wtshop.model.Goods;

/**
 * Service - 搜索
 * 
 * 
 */
public class SearchService {

	/** 模糊查询最小相似度 */
	//private static final float FUZZY_QUERY_MINIMUM_SIMILARITY = 0.5F;

	private ArticleDao articleDao = Enhancer.enhance(ArticleDao.class);
	private GoodsDao goodsDao = Enhancer.enhance(GoodsDao.class);
	private ProductCategoryService productCategoryService = Enhancer.enhance(ProductCategoryService.class);
	private AreaService areaService = Enhancer.enhance(AreaService.class);
	private LucenePlugin lucenePlugin;
	
	/**
	 * 创建索引
	 */
	public void index() {
		index(Article.class);
		index(Goods.class);
	}

	/**
	 * 创建索引
	 * 
	 * @param type
	 *            索引类型
	 */
	public void index(Class<?> type) {
		try {
			
			if (type == Article.class) {
				for (int i = 0; i < articleDao.count(); i += 20) {
					List<Article> articles = articleDao.findList(i, 20, null, null);
					for (Article article : articles) {
						lucenePlugin = new LucenePlugin(getLuceneDir(), "article", AnalyzerType.IKAnalyzer);
						lucenePlugin.addArticle(article);
					}
				}
			} else if (type == Goods.class) {
				for (int i = 0; i < goodsDao.count(); i += 20) {
					List<Goods> goodsList = goodsDao.findList(i, 20, null, null);
					for (Goods goods : goodsList) {
						lucenePlugin = new LucenePlugin(getLuceneDir(), "goods", AnalyzerType.IKAnalyzer);
						lucenePlugin.addGoods(goods);
					}
				}
			}
		} catch (IOException e) {
			LogKit.info("索引异常" + e);
			e.printStackTrace();
		}
	}

	/**
	 * 创建索引
	 * 
	 * @param article
	 *            文章
	 */
	public void index(Article article) {
		if (article != null) {
			try {
				lucenePlugin = new LucenePlugin(getLuceneDir(), "article", AnalyzerType.IKAnalyzer);
				lucenePlugin.addArticle(article);
			} catch (IOException e) {
				LogKit.info("索引异常" + e);
				e.printStackTrace();
			}
		}

	}

	/**
	 * 创建索引
	 * 
	 * @param goods
	 *            货品
	 */
	public void index(Goods goods) {
		if (goods != null) {
			try {
				lucenePlugin = new LucenePlugin(getLuceneDir(), "goods", AnalyzerType.IKAnalyzer);
				lucenePlugin.addGoods(goods);
			} catch (IOException e) {
				LogKit.info("索引异常" + e);
				e.printStackTrace();
			}
			
		}
	}

	/**
	 * 删除索引
	 */
	public void purge() {
		purge(Article.class);
		purge(Goods.class);
	}

	/**
	 * 删除索引
	 * 
	 * @param type
	 *            索引类型
	 */
	public void purge(Class<?> type) {
		try {
			if (type == Article.class) {
				lucenePlugin = new LucenePlugin(getLuceneDir(), "article", AnalyzerType.IKAnalyzer);
				lucenePlugin.deleteAll();
			} else if (type == Goods.class) {
				lucenePlugin = new LucenePlugin(getLuceneDir(), "goods", AnalyzerType.IKAnalyzer);
				lucenePlugin.deleteAll();
			}
		} catch (IOException e) {
			LogKit.info("索引异常" + e);
			e.printStackTrace();
		}
	}

	/**
	 * 删除索引
	 * 
	 * @param article
	 *            文章
	 */
	public void purge(Article article) {
		if (article != null) {
			try {
				lucenePlugin = new LucenePlugin(getLuceneDir(), "article", AnalyzerType.IKAnalyzer);
				lucenePlugin.deleteAll();
			} catch (IOException e) {
				LogKit.info("索引异常" + e);
				e.printStackTrace();
			}
		}
	}

	/**
	 * 删除索引
	 * 
	 * @param goods
	 *            货品
	 */
	public void purge(Goods goods) {
		if (goods != null) {
			try {
				lucenePlugin = new LucenePlugin(getLuceneDir(), "goods", AnalyzerType.IKAnalyzer);
				lucenePlugin.deleteAll();
			} catch (IOException e) {
				LogKit.info("索引异常" + e);
				e.printStackTrace();
			}
		}
	}

	/**
	 * 搜索文章分页
	 * 
	 * @param keyword
	 *            关键词
	 * @param pageable
	 *            分页信息
	 * @return 文章分页
	 */
	public Page<Article> search(String keyword, Pageable pageable) {
		if (StringUtils.isEmpty(keyword)) {
			return new Page<Article>();
		}

		if (pageable == null) {
			pageable = new Pageable();
		}
		Page<Article> pages = null;
		try {
			LucenePlugin lucenePlugin = new LucenePlugin(getLuceneDir(), "article", AnalyzerType.IKAnalyzer);
			int pageNumber = pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber();
			pages = lucenePlugin.query(keyword, pageNumber, pageable.getPageSize(), new Article());
		} catch (IOException e) {
			LogKit.info("索引IO异常" + e);
			e.printStackTrace();
		} catch (ParseException e) {
			LogKit.info("索引Parse异常" + e);
			e.printStackTrace();
		} catch (InvalidTokenOffsetsException e) {
			LogKit.info("索引InvalidTokenOffsets异常" + e);
			e.printStackTrace();
		}
		return pages;
	}

	/**
	 * 搜索货品分页
	 * 
	 * @param keyword
	 *            关键词
	 * @param startPrice
	 *            最低价格
	 * @param endPrice
	 *            最高价格
	 * @param orderType
	 *            排序类型
	 * @param pageable
	 *            分页信息
	 * @return 货品分页
	 */
	public Page<Goods> search(String keyword, BigDecimal startPrice, BigDecimal endPrice, Goods.OrderType orderType, Pageable pageable) {
		if (StringUtils.isEmpty(keyword)) {
			return new Page<Goods>();
		}

		if (pageable == null) {
			pageable = new Pageable();
		}
		
		Page<Goods> pages = null;
		try {
			LucenePlugin lucenePlugin = new LucenePlugin(getLuceneDir(), "goods", AnalyzerType.IKAnalyzer);
			int pageNumber = pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber();
			pages = lucenePlugin.query(keyword, pageNumber, pageable.getPageSize(), new Goods());
		} catch (IOException e) {
			LogKit.info("索引IO异常" + e);
			e.printStackTrace();
		} catch (ParseException e) {
			LogKit.info("索引Parse异常" + e);
			e.printStackTrace();
		} catch (InvalidTokenOffsetsException e) {
			LogKit.info("索引InvalidTokenOffsets异常" + e);
			e.printStackTrace();
		}
		return pages;
	}
	
	/**
	 * Lucene索引地址
	 * @return
	 */
	private String getLuceneDir() {
		Prop prop = PropKit.use(CommonAttributes.wtshop_PROPERTIES_PATH);
		String luceneDir = prop.get("lucene.dir");
		if (StrKit.isBlank(luceneDir)) {
			luceneDir = PathKit.getWebRootPath() + "/lucene/core/";
		}
		return luceneDir;
	}


	/**
	 * 模糊搜索
	 */
	public Page<Goods> search(Boolean is_vip, Long productCategoryId, Long[] brandId, Long[] areaId, Long[] functionId, String keyword, BigDecimal startPrice, BigDecimal endPrice, Pageable pageable, Boolean modify_date, Boolean sell, Boolean review, Boolean priceUp ,Boolean priceDown){

		List<Long> productCategoryList = new ArrayList<>();
		productCategoryList.add(productCategoryId);
	/*	if(productCategoryId != null){
			List<ProductCategory> children = productCategoryService.findChildren(productCategoryId, true, null, false);
			if(children != null && children.size() > 0){
				for(ProductCategory productCategory : children){
					productCategoryList.add(productCategory.getId());
				}
			}
		}*/
		List<Long> areaList = new ArrayList<>();
		if(areaId != null &&  areaId.length > 0){
			for(Long areaIds : areaId){
				areaList.add(areaIds);
				List<Area> children = areaService.findChildren(areaService.find(areaIds), true, null);
				for(Area area : children){
					if( !areaList.contains(area.getId())){
						areaList.add(area.getId());
					}
				}
			}
		}



		return goodsDao.search(is_vip, productCategoryList, brandId, areaList, functionId, keyword, startPrice, endPrice, pageable, modify_date, sell, review, priceUp, priceDown);
	}
}