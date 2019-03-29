package com.wtshop.api.controller;

import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.wtshop.api.interceptor.TokenInterceptor;
import com.wtshop.model.InterestCategory;
import com.wtshop.service.InterestCategoryService;
import com.wtshop.util.ApiResult;
import com.wtshop.api.common.result.ProductCategoryModel;
import com.wtshop.api.common.result.ProductCategoryResult;
import com.wtshop.api.interceptor.ErrorInterceptor;
import com.wtshop.interceptor.WapInterceptor;
import com.wtshop.model.ProductCategory;
import com.wtshop.service.ProductCategoryService;

import java.util.ArrayList;
import java.util.List;


/**
 * Controller - 商品分类
 * 
 * 
 */
@ControllerBind(controllerKey = "/api/product_category")
@Before({WapInterceptor.class, ErrorInterceptor.class})
public class ProductCategoryAPIController extends BaseAPIController {
	
	private ProductCategoryService productCategoryService = new ProductCategoryService();
	private InterestCategoryService interestCategoryService = enhance(InterestCategoryService.class);
	/**
	 * 首页
	 * {"msg":"","code":1,"data":{"productCategories":[{"create_date":"2017-05-18 09:30:26","grade":0,"id":243,"image":"/upload/image/201705/ceeb6151-b51c-40db-92eb-59fae3d28880.png","modify_date":"2017-05-24 14:04:30","name":"护肤产品","orders":1,"parent_id":null,"seo_description":null,"seo_keywords":null,"seo_title":null,"tree_path":",","version":1},{"create_date":"2017-05-18 09:31:58","grade":1,"id":245,"image":"/upload/image/201705/358782ae-05fe-4a0d-96e2-0566e1f295ce.jpg","modify_date":"2017-05-23 13:40:05","name":"洗面奶","orders":1,"parent_id":243,"seo_description":null,"seo_keywords":null,"seo_title":null,"tree_path":",243,","version":2},{"create_date":"2017-05-18 09:35:44","grade":1,"id":252,"image":"/upload/image/201705/e3464e7b-21dd-4fbb-b1b8-26d8b53e677a.jpg","modify_date":"2017-05-23 13:50:15","name":"染发","orders":1,"parent_id":244,"seo_description":null,"seo_keywords":null,"seo_title":null,"tree_path":",244,","version":1},{"create_date":"2017-05-18 09:31:27","grade":0,"id":244,"image":null,"modify_date":"2017-05-18 09:31:27","name":"美发产品","orders":2,"parent_id":null,"seo_description":null,"seo_keywords":null,"seo_title":null,"tree_path":",","version":0},{"create_date":"2017-05-18 09:32:27","grade":1,"id":246,"image":"/upload/image/201705/14089256-79bd-470e-8a63-f255e2100381.jpg","modify_date":"2017-05-23 13:40:39","name":"护肤水","orders":2,"parent_id":243,"seo_description":null,"seo_keywords":null,"seo_title":null,"tree_path":",243,","version":1},{"create_date":"2017-05-18 09:35:55","grade":1,"id":253,"image":"/upload/image/201705/6ab2144c-a415-41a8-a850-68a2d592a24f.jpg","modify_date":"2017-05-23 13:50:54","name":"护发","orders":2,"parent_id":244,"seo_description":null,"seo_keywords":null,"seo_title":null,"tree_path":",244,","version":1},{"create_date":"2017-05-18 09:33:03","grade":1,"id":247,"image":"/upload/image/201705/84332c22-cae9-4921-8997-89b43675fe1a.jpg","modify_date":"2017-05-23 13:46:13","name":"BB霜","orders":3,"parent_id":243,"seo_description":null,"seo_keywords":null,"seo_title":null,"tree_path":",243,","version":2},{"create_date":"2017-05-18 09:36:05","grade":1,"id":254,"image":"/upload/image/201705/eb8b26dd-f579-40ef-965f-eec28d59cde2.jpg","modify_date":"2017-05-23 13:59:58","name":"洗发","orders":3,"parent_id":244,"seo_description":null,"seo_keywords":null,"seo_title":null,"tree_path":",244,","version":1},{"create_date":"2017-05-18 09:34:41","grade":1,"id":248,"image":"/upload/image/201705/2b51b201-9b07-4736-a9a6-356f36f0a413.jpg","modify_date":"2017-05-23 13:47:07","name":"隔离霜","orders":4,"parent_id":243,"seo_description":null,"seo_keywords":null,"seo_title":null,"tree_path":",243,","version":1},{"create_date":"2017-05-18 09:36:16","grade":1,"id":255,"image":"/upload/image/201705/6fcda974-5819-45f7-8669-662066782513.jpg","modify_date":"2017-05-23 13:59:28","name":"烫发","orders":4,"parent_id":244,"seo_description":null,"seo_keywords":null,"seo_title":null,"tree_path":",244,","version":1},{"create_date":"2017-05-18 09:35:00","grade":1,"id":249,"image":"/upload/image/201705/42ab27a7-0ccc-4e7b-a576-ecdb9c531c55.jpg","modify_date":"2017-05-23 13:48:54","name":"防晒霜","orders":5,"parent_id":243,"seo_description":null,"seo_keywords":null,"seo_title":null,"tree_path":",243,","version":1},{"create_date":"2017-05-18 09:35:10","grade":1,"id":250,"image":"/upload/image/201705/ed236510-3090-4d53-b4e5-0ef4e7744689.jpg","modify_date":"2017-05-23 13:58:27","name":"面霜","orders":6,"parent_id":243,"seo_description":null,"seo_keywords":null,"seo_title":null,"tree_path":",243,","version":1},{"create_date":"2017-05-18 09:35:24","grade":1,"id":251,"image":"/upload/image/201705/2606946a-4020-489d-843d-dc9f396885ae.jpg","modify_date":"2017-05-23 13:49:39","name":"面膜","orders":7,"parent_id":243,"seo_description":null,"seo_keywords":null,"seo_title":null,"tree_path":",243,","version":1}]}}
	 */
	public void index() {
		List<InterestCategory> findOneAndTwoRoots = interestCategoryService.findAll();
		renderJson(ApiResult.success(findOneAndTwoRoots));
	}

	/**
	 * 根据二级分类获取三级分类
	 * {"msg":"","code":1,"data":[{"create_date":"2017-05-18 10:39:57","grade":2,"id":324,"image":null,"modify_date":"2017-05-18 10:39:57","name":"去屑止痒","orders":1,"parent_id":255,"seo_description":null,"seo_keywords":null,"seo_title":null,"tree_path":",244,255,","version":0},{"create_date":"2017-05-18 10:40:19","grade":2,"id":325,"image":null,"modify_date":"2017-05-18 10:40:19","name":"深层修复","orders":2,"parent_id":255,"seo_description":null,"seo_keywords":null,"seo_title":null,"tree_path":",244,255,","version":0},{"create_date":"2017-05-18 10:40:36","grade":2,"id":326,"image":null,"modify_date":"2017-05-18 10:40:36","name":"柔顺滋养","orders":3,"parent_id":255,"seo_description":null,"seo_keywords":null,"seo_title":null,"tree_path":",244,255,","version":0}]}
	 */
	public void roots(){
		Long rootsId = getParaToLong("id");
		List<ProductCategory> thirdRoots = productCategoryService.findChildren(rootsId, false, null, false);
		renderJson(ApiResult.success(thirdRoots));
	}


}
