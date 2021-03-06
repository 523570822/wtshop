package com.wtshop.template.directive;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.jfinal.aop.Enhancer;
import com.wtshop.Filter;
import com.wtshop.Order;
import com.wtshop.model.Attribute;
import com.wtshop.service.AttributeService;
import com.wtshop.util.FreeMarkerUtils;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * 模板指令 - 属性
 * 
 * 
 */
public class AttributeListDirective extends BaseDirective {

	/** "商品分类ID"参数名称 */
	private static final String PRODUCT_CATEGORY_ID_PARAMETER_NAME = "product_category_id";

	/** 变量名称 */
	private static final String VARIABLE_NAME = "attributes";

	private AttributeService attributeService = Enhancer.enhance(AttributeService.class);

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
		Long productCategoryId = FreeMarkerUtils.getParameter(PRODUCT_CATEGORY_ID_PARAMETER_NAME, Long.class, params);
		Integer count = getCount(params);
		List<Filter> filters = getFilters(params, Attribute.class);
		List<Order> orders = getOrders(params);
		boolean useCache = useCache(env, params);
		List<Attribute> attributes = attributeService.findList(productCategoryId, count, filters, orders, useCache);
		setLocalVariable(VARIABLE_NAME, attributes, env, body);
	}

}