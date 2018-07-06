package com.wtshop.controller.shop;

import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.wtshop.Pageable;
import com.wtshop.exception.ResourceNotFoundException;
import com.wtshop.interceptor.ThemeInterceptor;
import com.wtshop.model.Brand;
import com.wtshop.service.BrandService;

/**
 * Controller - 品牌
 * 
 * 
 */
@ControllerBind(controllerKey = "/brand")
@Before(ThemeInterceptor.class)
public class BrandController extends BaseController {

	/** 每页记录数 */
	private static final int PAGE_SIZE = 40;

	private BrandService brandService = enhance(BrandService.class);

	/**
	 * 列表
	 */
	public void list() {
		Integer pageNumber = getParaToInt("pageNumber");
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		setAttr("page", brandService.findPage(pageable));
		render("/shop/${theme}/brand/list.ftl");
	}

	/**
	 * 内容
	 */
	public void content() {
		Long id = getParaToLong(0);
		Brand brand = brandService.find(id);
		if (brand == null) {
			throw new ResourceNotFoundException();
		}
		setAttr("brand", brand);
		render("/shop/${theme}/brand/content.ftl");
	}

}