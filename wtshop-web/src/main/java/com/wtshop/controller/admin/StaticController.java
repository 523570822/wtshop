package com.wtshop.controller.admin;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;
import com.wtshop.model.Article;
import com.wtshop.model.ArticleCategory;
import com.wtshop.model.Goods;
import com.wtshop.model.ProductCategory;
import com.wtshop.service.ArticleCategoryService;
import com.wtshop.service.ArticleService;
import com.wtshop.service.CacheService;
import com.wtshop.service.GoodsService;
import com.wtshop.service.ProductCategoryService;
import com.wtshop.service.StaticService;

/**
 * Controller - 静态化
 * 
 * 
 */
@ControllerBind(controllerKey = "/admin/static")
public class StaticController extends BaseController {

	/**
	 * 生成类型
	 */
	public enum GenerateType {
		/**
		 * 首页
		 */
		index,

		/**
		 * 文章
		 */
		article,

		/**
		 * 商品
		 */
		goods,

		/**
		 * 其它
		 */
		other
	}

	private ArticleService articleService = enhance(ArticleService.class);
	private ArticleCategoryService articleCategoryService = enhance(ArticleCategoryService.class);
	private GoodsService goodsService = enhance(GoodsService.class);
	private ProductCategoryService productCategoryService = enhance(ProductCategoryService.class);
	private StaticService staticService = enhance(StaticService.class);
	private CacheService cacheService = enhance(CacheService.class);

	/**
	 * 生成静态
	 */
	public void generate() {
		setAttr("generateTypes", StaticController.GenerateType.values());
		setAttr("defaultBeginDate", DateUtils.addDays(new Date(), -7));
		setAttr("defaultEndDate", new Date());
		setAttr("articleCategoryTree", articleCategoryService.findTree());
		setAttr("productCategoryTree", productCategoryService.findTree());
		render("/admin/static/generate.ftl");
	}

	/**
	 * 生成静态
	 */
	public void generateSubmit() {
		String generateTypeName = getPara("generateType");
		StaticController.GenerateType generateType = StrKit.notBlank(generateTypeName) ? StaticController.GenerateType.valueOf(generateTypeName) : null;
		Long articleCategoryId = getParaToLong("articleCategoryId");
		Long productCategoryId = getParaToLong("productCategoryId");
		Date beginDate = getParaToDate("beginDate");
		Date endDate = getParaToDate("endDate");
		Integer first = getParaToInt("first");
		Integer count = getParaToInt("count");
		
		long startTime = System.currentTimeMillis();
		if (beginDate != null) {
			Calendar calendar = DateUtils.toCalendar(beginDate);
			calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY));
			calendar.set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE));
			calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND));
			beginDate = calendar.getTime();
		}
		if (endDate != null) {
			Calendar calendar = DateUtils.toCalendar(endDate);
			calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMaximum(Calendar.HOUR_OF_DAY));
			calendar.set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE));
			calendar.set(Calendar.SECOND, calendar.getActualMaximum(Calendar.SECOND));
			endDate = calendar.getTime();
		}
		if (first == null || first < 0) {
			first = 0;
			cacheService.clear();
		}
		if (count == null || count <= 0) {
			count = 100;
		}
		int generateCount = 0;
		boolean isCompleted = true;
		switch (generateType) {
		case index:
			generateCount = staticService.generateIndex();
			break;
		case article:
			ArticleCategory articleCategory = articleCategoryService.find(articleCategoryId);
			List<Article> articles = articleService.findList(articleCategory, true, null, beginDate, endDate, first, count);
			for (Article article : articles) {
				generateCount += staticService.generate(article);
			}
			first += articles.size();
			if (articles.size() == count) {
				isCompleted = false;
			}
			break;
		case goods:
			ProductCategory productCategory = productCategoryService.find(productCategoryId);
			List<Goods> goodsList = goodsService.findList(productCategory, true, null, beginDate, endDate, first, count);
			for (Goods goods : goodsList) {
				generateCount += staticService.generate(goods);
			}
			first += goodsList.size();
			if (goodsList.size() == count) {
				isCompleted = false;
			}
			break;
		case other:
			generateCount = staticService.generateOther();
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