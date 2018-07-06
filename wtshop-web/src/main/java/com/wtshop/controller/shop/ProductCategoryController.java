package com.wtshop.controller.shop;

import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.wtshop.interceptor.ThemeInterceptor;
import com.wtshop.service.ProductCategoryService;

/**
 * Controller - 商品分类
 * 
 * 
 */
@ControllerBind(controllerKey = "/product_category")
@Before(ThemeInterceptor.class)
public class ProductCategoryController extends BaseController {

	private ProductCategoryService productCategoryService = new ProductCategoryService();

	/**
	 * 首页
	 */
	public void index() {
		setAttr("rootProductCategories", productCategoryService.findRoots());
		render("/shop/${theme}/product_category/index.ftl");
	}

}