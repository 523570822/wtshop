package com.wtshop.controller.shop;

import org.apache.commons.lang3.StringUtils;

import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.wtshop.Pageable;
import com.wtshop.exception.ResourceNotFoundException;
import com.wtshop.interceptor.ThemeInterceptor;
import com.wtshop.model.ArticleCategory;
import com.wtshop.service.ArticleCategoryService;
import com.wtshop.service.ArticleService;
import com.wtshop.service.SearchService;

/**
 * Controller - 文章
 * 
 * 
 */
@ControllerBind(controllerKey = "/article")
@Before(ThemeInterceptor.class)
public class ArticleController extends BaseController {

	/** 每页记录数 */
	private static final int PAGE_SIZE = 20;

	private ArticleService articleService = enhance(ArticleService.class);
	private ArticleCategoryService articleCategoryService = enhance(ArticleCategoryService.class);
	private SearchService searchService = enhance(SearchService.class);

	/**
	 * 列表
	 */
	public void list() {
		Long id = getParaToLong(0);
		Integer pageNumber = getParaToInt("pageNumber");
		ArticleCategory articleCategory = articleCategoryService.find(id);
		if (articleCategory == null) {
			throw new ResourceNotFoundException();
		}
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		setAttr("articleCategory", articleCategory);
		setAttr("page", articleService.findPage(articleCategory, null, true, pageable));
		render("/shop/${theme}/article/list.ftl");
	}

	/**
	 * 搜索
	 */
	public void search() {
		String keyword = getPara("keyword"); 
		
		Integer pageNumber = getParaToInt("pageNumber");
		if (StringUtils.isEmpty(keyword)) {
			redirect(ERROR_VIEW);
			return;
		}
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		setAttr("articleKeyword", keyword);
		setAttr("page", searchService.search(keyword, pageable));
		render("/shop/${theme}/article/search.ftl");
	}

	/**
	 * 点击数
	 */
	public void hits() {
		Long id = getParaToLong(0);
		renderJson(articleService.viewHits(id));
	}

}