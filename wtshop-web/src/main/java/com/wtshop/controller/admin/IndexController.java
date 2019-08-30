package com.wtshop.controller.admin;

import com.jfinal.aop.Enhancer;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;
import com.wtshop.model.Article;
import com.wtshop.model.Goods;
import com.wtshop.model.IntegralStore;
import com.wtshop.service.*;
import com.wtshop.util.ApiResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller - 索引
 * 
 * 
 */
@ControllerBind(controllerKey = "/admin/index")
public class IndexController extends BaseController {

	private ArticleService articleService = enhance(ArticleService.class);
	private GoodsService goodsService = enhance(GoodsService.class);
	private SearchService searchService = enhance(SearchService.class);
	private SpecialGoodsService specialGoodsService = enhance(SpecialGoodsService.class);
	private AdService adService = enhance(AdService.class);
	private FullReductionService fullReductionService =enhance(FullReductionService.class);
	private IntegralStoreService integralStoreService =Enhancer.enhance(IntegralStoreService.class);
	/**
	 * 生成类型
	 */
	/**
	 *
	 */
	public void ceshi() {

		List<IntegralStore> integralStoreList=integralStoreService.findLogByMemberId(68l);
		//List<FullReduction> kkk = fullReductionService.findAll();
		renderJson(ApiResult.success(integralStoreList));
	}
	public enum GenerateType {
		/**
		 * 文章
		 */
		article,

		/**
		 * 商品
		 */
		goods
	}

	/**
	 * 生成索引
	 */
	public void generate() {
		setAttr("generateTypes", IndexController.GenerateType.values());
		render("/admin/index/generate.ftl");
	}

	/**
	 * 生成索引
	 */
	public void generateSubmit() {
		String generateTypeName = getPara("generateType");
		GenerateType generateType = StrKit.notBlank(generateTypeName) ? GenerateType.valueOf(generateTypeName) : null;
		Boolean isPurge = getParaToBoolean("isPurge");
		Integer first = getParaToInt("first");
		Integer count = getParaToInt("count");
		
		long startTime = System.currentTimeMillis();
		if (first == null || first < 0) {
			first = 0;
		}
		if (count == null || count <= 0) {
			count = 100;
		}
		int generateCount = 0;
		boolean isCompleted = true;
		switch (generateType) {
		case article:
			if (first == 0 && isPurge != null && isPurge) {
				searchService.purge(Article.class);
			}
			List<Article> articles = articleService.findList(first, count, null, null);
			for (Article article : articles) {
				searchService.index(article);
				generateCount++;
			}
			first += articles.size();
			if (articles.size() == count) {
				isCompleted = false;
			}
			break;
		case goods:
			if (first == 0 && isPurge != null && isPurge) {
				searchService.purge(Goods.class);
			}
			List<Goods> goodsList = goodsService.findList(first, count, null, null);
			for (Goods goods : goodsList) {
				searchService.index(goods);
				generateCount++;
			}
			first += goodsList.size();
			if (goodsList.size() == count) {
				isCompleted = false;
			}
			break;
		}
		long endTime = System.currentTimeMillis();
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("first", first);
		data.put("generateCount", generateCount);
		data.put("generateTime", endTime - startTime);
		data.put("isCompleted", isCompleted);
		renderJson(data);
	}

}