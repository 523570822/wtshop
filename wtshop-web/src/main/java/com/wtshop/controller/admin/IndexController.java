package com.wtshop.controller.admin;

import com.jfinal.aop.Enhancer;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;
import com.wtshop.entity.WxaTemplate;
import com.wtshop.model.Article;
import com.wtshop.model.Goods;
import com.wtshop.service.*;
import com.wtshop.util.ApiResult;
import com.wtshop.util.MathUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
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
	private AccountService accountService = Enhancer.enhance(AccountService.class);
	com.jfinal.log.Logger logger = com.jfinal.log.Logger.getLogger(IndexController.class);
	/**
	 * 生成类型
	 */
	/**
	 *
	 */
	public void ceshi() {
		WxaTemplate template=new WxaTemplate();
		//template.setTouser("o8dwZ49PfD9hP11ey770zf4STzCo");
		template.setTouser("o8dwZ49PfD9hP11ey770zf4STzCo");
		//	template.setEmphasis_keyword("给力");
		//template.setForm_id("wx02094520098596824ccaba5c1382802700");
		template.setForm_id("wx020956231370586cdaef17241143486600");
		template.setPage("pages/main/main");
		template.setTemplate_id("sK2pxYoo46AY-ijs_f_cfSsMG91Rn-TzHAmeZmcUYFI");
		template.add("keyword1","4200000397201909029891366229");

		SimpleDateFormat sdf =new SimpleDateFormat("yyyy年MM月dd HH:mm:ss SSS" );
		Date d= new Date();
		String str = sdf.format(d);
		template.add("keyword2",str);
		template.add("keyword3","12元");
		template.add("keyword3",MathUtil.getInt("11"));
		logger.info("微信推送开始"+template.build().toString());
		Map<String, Object> ddd123 = accountService.getXCXSend(template);
		logger.info("微信推送结束"+ddd123.toString());
	//	List<IntegralStore> integralStoreList=integralStoreService.findLogByMemberId(68l);
		//List<FullReduction> kkk = fullReductionService.findAll();
		renderJson(ApiResult.success(ddd123.toString()));
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