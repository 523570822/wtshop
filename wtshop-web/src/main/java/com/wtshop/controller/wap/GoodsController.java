package com.wtshop.controller.wap;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.wtshop.Pageable;
import com.wtshop.RequestContextHolder;
import com.wtshop.exception.ResourceNotFoundException;
import com.wtshop.model.Consultation;
import com.wtshop.model.Goods;
import com.wtshop.model.ProductCategory;
import com.wtshop.model.Review;
import com.wtshop.service.ConsultationService;
import com.wtshop.service.GoodsService;
import com.wtshop.service.MemberService;
import com.wtshop.service.ProductCategoryService;
import com.wtshop.service.ReviewService;
import com.wtshop.service.SearchService;


/**
 * Controller - 货品
 * 
 *
 */
@ControllerBind(controllerKey = "/wap/goods")
public class GoodsController extends BaseController {
	
	private ProductCategoryService productCategoryService = enhance(ProductCategoryService.class);
	private ConsultationService consultationService = enhance(ConsultationService.class);
	private GoodsService goodsService = enhance(GoodsService.class);
	private MemberService memberService = enhance(MemberService.class);
	private SearchService searchService = enhance(SearchService.class);
	private ReviewService reviewService = enhance(ReviewService.class);
	
	/**
	 * 列表
	 */
	public void list() {
		Long productCategoryId = getParaToLong("productCategoryId");
		ProductCategory productCategory = productCategoryService.find(productCategoryId);
		if (productCategory == null) {
			throw new ResourceNotFoundException();
		}
		
		String orderTypeName = getPara("orderType");
		Goods.OrderType orderType = StrKit.notBlank(orderTypeName) ? Goods.OrderType.valueOf(orderTypeName) : null;
		
		String startPriceStr = getPara("startPrice", null);
		BigDecimal startPrice = null;
		if (StrKit.notBlank(startPriceStr)) {
			startPrice = new BigDecimal(startPriceStr);
		}
		
		String endPriceStr = getPara("endPrice", null);
		BigDecimal endPrice = null;
		if (StrKit.notBlank(endPriceStr)) {
			endPrice = new BigDecimal(endPriceStr);
		}
		Integer pageNumber = getParaToInt("pageNumber", 1);
		Integer pageSize = getParaToInt("pageSize", 20);
		Pageable pageable = new Pageable(pageNumber, pageSize);
		Boolean is_vip = false;
		setAttr("title" , productCategory.getName());
		setAttr("productCategory", productCategory);
		setAttr("orderType", orderType == null ? "all" : orderType);
		setAttr("pages", goodsService.findPage(is_vip, true, null, productCategory, null, null, null, null, startPrice, endPrice, true, true, null, null, null, null, orderType, pageable));
		render("/wap/goods/list.ftl");
	}

	/**
	 * 详情
	 */
	public void detail() {
		Long id = getParaToLong("id");
		Goods goods = goodsService.find(id);
		Pageable pageable = new Pageable(1, 20);
		Boolean favorite = false;
		
		if (goods == null) {
			redirect("/wap/index.html");
			return;
		}
		
		RequestContextHolder.setRequestAttributes(getRequest());
		if (goods.getFavoriteMembers().contains(memberService.getCurrent())) {
			favorite = true;
		}
		Page<Consultation> consultationPages = consultationService.findPage(null, goods, true, pageable);
		Page<Review> reviewPages = reviewService.findPage(null, goods, null, null, pageable);
		setAttr("goods", goods);
		setAttr("favorite", favorite);
		setAttr("consultationPages", consultationPages);
		setAttr("reviewPages", reviewPages);
		setAttr("title" , goods.getName());
		render("/wap/goods/detail.ftl");
	}
	
	/**
	 * 搜索
	 */
	public void search() {
		String keyword = getPara("keyword");
		
		String startPriceStr = getPara("startPrice");
		BigDecimal startPrice = null;
		if (StrKit.notBlank(startPriceStr)) {
			startPrice = new BigDecimal(startPriceStr);
		}
		
		String endPriceStr = getPara("endPrice");
		BigDecimal endPrice = null;
		if (StrKit.notBlank(endPriceStr)) {
			endPrice = new BigDecimal(endPriceStr);
		}
		String orderTypeName = getPara("orderType");
		Goods.OrderType orderType = StrKit.notBlank(orderTypeName) ? Goods.OrderType.valueOf(orderTypeName) : null;
		
		Integer pageNumber = getParaToInt("pageNumber");
		Integer pageSize = getParaToInt("pageSize");
		
		if (StringUtils.isEmpty(keyword)) {
			setAttr("goodsList", null);
			setAttr("title" , "搜索 0 的结果");
			render("/wap/goods/search.ftl");
			return;
		} 
		Pageable pageable = new Pageable(pageNumber, pageSize);
		Page<Goods> goodsList = searchService.search(keyword, startPrice, endPrice, orderType, pageable);
		setAttr("orderTypes", Goods.OrderType.values());
		setAttr("goodsKeyword", keyword);
		setAttr("startPrice", startPrice);
		setAttr("endPrice", endPrice);
		setAttr("orderType", orderType == null ? "all" : orderType);
		setAttr("goodsList", goodsList.getList());
		setAttr("title" , "搜索 " + goodsList.getList().size() + " 的结果");
		render("/wap/goods/search.ftl");
	}
	
	
}
