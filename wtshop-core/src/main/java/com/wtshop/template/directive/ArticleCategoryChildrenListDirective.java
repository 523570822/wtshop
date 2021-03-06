package com.wtshop.template.directive;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.jfinal.aop.Enhancer;
import com.wtshop.model.ArticleCategory;
import com.wtshop.service.ArticleCategoryService;
import com.wtshop.util.FreeMarkerUtils;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * 模板指令 - 下级文章分类列表
 * 
 * 
 */
public class ArticleCategoryChildrenListDirective extends BaseDirective {

	/** "文章分类ID"参数名称 */
	private static final String ARTICLE_CATEGORY_ID_PARAMETER_NAME = "article_category_id";

	/** "是否递归"参数名称 */
	private static final String RECURSIVE_PARAMETER_NAME = "recursive";

	/** 变量名称 */
	private static final String VARIABLE_NAME = "articleCategories";

	private ArticleCategoryService articleCategoryService = Enhancer.enhance(ArticleCategoryService.class);

	/**
	 * 执行
	 * 
	 * @param env
	 *            环境变量
	 * @param params
	 *            参数
	 * @param loopVars
	 *            循环变量
	 * @param body
	 *            模板内容
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		Long articleCategoryId = FreeMarkerUtils.getParameter(ARTICLE_CATEGORY_ID_PARAMETER_NAME, Long.class, params);
		Boolean recursive = FreeMarkerUtils.getParameter(RECURSIVE_PARAMETER_NAME, Boolean.class, params);
		Integer count = getCount(params);
		boolean useCache = useCache(env, params);
		List<ArticleCategory> articleCategories = articleCategoryService.findChildren(articleCategoryId, recursive != null ? recursive : true, count, useCache);
		setLocalVariable(VARIABLE_NAME, articleCategories, env, body);
	}

}