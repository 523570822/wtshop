package com.wtshop.controller.shop;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;
import com.wtshop.Pageable;
import com.wtshop.exception.ResourceNotFoundException;
import com.wtshop.interceptor.ThemeInterceptor;
import com.wtshop.model.Attribute;
import com.wtshop.model.Brand;
import com.wtshop.model.Goods;
import com.wtshop.model.ProductCategory;
import com.wtshop.model.Promotion;
import com.wtshop.model.Tag;
import com.wtshop.service.AttributeService;
import com.wtshop.service.BrandService;
import com.wtshop.service.GoodsService;
import com.wtshop.service.ProductCategoryService;
import com.wtshop.service.PromotionService;
import com.wtshop.service.SearchService;
import com.wtshop.service.TagService;

/**
 * Controller - 货品
 * 
 *
 */
@ControllerBind(controllerKey = "/goods")
@Before(ThemeInterceptor.class)
public class GoodsController extends BaseController {

	/** 最大对比货品数 */
	public static final Integer MAX_COMPARE_GOODS_COUNT = 4;

	/** 最大浏览记录货品数 */
	public static final Integer MAX_HISTORY_GOODS_COUNT = 10;

	private GoodsService goodsService = enhance(GoodsService.class);
	private ProductCategoryService productCategoryService = enhance(ProductCategoryService.class);
	private BrandService brandService = enhance(BrandService.class);
	private PromotionService promotionService = enhance(PromotionService.class);
	private TagService tagService = enhance(TagService.class);
	private AttributeService attributeService = enhance(AttributeService.class);
	private SearchService searchService = enhance(SearchService.class);

	/**
	 * 对比栏
	 */
	public void compareBar() {
		Long[] goodsIds = getParaValuesToLong("ids");
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		if (ArrayUtils.isEmpty(goodsIds) || goodsIds.length > MAX_COMPARE_GOODS_COUNT) {
			renderJson(data);
			return;
		}

		List<Goods> goodsList = goodsService.findList(goodsIds);
		for (Goods goods : goodsList) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("id", goods.getId());
			item.put("name", goods.getName());
			item.put("price", goods.getPrice());
			item.put("marketPrice", goods.getMarketPrice());
			item.put("thumbnail", goods.getThumbnail());
			item.put("url", goods.getUrl());
			data.add(item);
		}
		renderJson(data);
	}

	/**
	 * 添加对比
	 */
	public void addCompare() {
		Long goodsId = getParaToLong("goodsId");
		Map<String, Object> data = new HashMap<String, Object>();
		Goods goods = goodsService.find(goodsId);
		if (goods == null) {
			data.put("message", ERROR_MESSAGE);
			renderJson(data);
			return;
		}

		data.put("message", SUCCESS_MESSAGE);
		data.put("id", goods.getId());
		data.put("name", goods.getName());
		data.put("price", goods.getPrice());
		data.put("marketPrice", goods.getMarketPrice());
		data.put("thumbnail", goods.getThumbnail());
		data.put("url", goods.getUrl());
		renderJson(data);
	}

	/**
	 * 浏览记录
	 */
	public void history() {
		Long[] goodsIds = getParaValuesToLong("goodsIds");
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		if (ArrayUtils.isEmpty(goodsIds) || goodsIds.length > MAX_HISTORY_GOODS_COUNT) {
			renderJson(data);
			return;
		}

		List<Goods> goodsList = goodsService.findList(goodsIds);
		for (Goods goods : goodsList) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("name", goods.getName());
			item.put("price", goods.getPrice());
			item.put("thumbnail", goods.getThumbnail());
			item.put("url", goods.getUrl());
			data.add(item);
		}
		renderJson(data);
	}

	/**
	 * 列表
	 */
	public void list() {
		Long productCategoryId = getParaToLong(0);
		if (productCategoryId == null) {
			brand_list();
		} else {
			goods_list();
		}
	}
	
	/**
	 * 货品列表
	 */
	private void goods_list() {
		Long productCategoryId = getParaToLong(0);
		String typeName = getPara("type");
		Goods.Type type = StrKit.notBlank(typeName) ? Goods.Type.valueOf(typeName) : null;
		
		String orderTypeName = getPara("orderType");
		Goods.OrderType orderType = StrKit.notBlank(orderTypeName) ? Goods.OrderType.valueOf(orderTypeName) : null;
		
		Long brandId = getParaToLong("brandId");
		Long promotionId = getParaToLong("promotionId");
		Long tagId = getParaToLong("tagId");
		Boolean is_vip = false;
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
		
		Integer pageNumber = getParaToInt("pageNumber");
		Integer pageSize = getParaToInt("pageSize");
		
		ProductCategory productCategory = productCategoryService.find(productCategoryId);
		if (productCategory == null) {
			throw new ResourceNotFoundException();
		}

		Brand brand = brandService.find(brandId);
		Promotion promotion = promotionService.find(promotionId);
		Tag tag = tagService.find(tagId);
		Map<Attribute, String> attributeValueMap = new HashMap<Attribute, String>();
		List<Attribute> attributes = productCategory.getAttributes();
		if (CollectionUtils.isNotEmpty(attributes)) {
			for (Attribute attribute : attributes) {
				String value = getPara("attribute_" + attribute.getId());
				String attributeValue = attributeService.toAttributeValue(attribute, value);
				if (attributeValue != null) {
					attributeValueMap.put(attribute, attributeValue);
				}
			}
		}

		Pageable pageable = new Pageable(pageNumber, pageSize);
		setAttr("orderTypes", Goods.OrderType.values());
		setAttr("productCategory", productCategory);
		setAttr("type", type);
		setAttr("brand", brand);
		setAttr("promotion", promotion);
		setAttr("tag", tag);
		setAttr("attributeValueMap", attributeValueMap);
		setAttr("startPrice", startPrice);
		setAttr("endPrice", endPrice);
		setAttr("orderType", orderType);
		setAttr("pageNumber", pageNumber);
		setAttr("pageSize", pageSize);
		setAttr("page", goodsService.findPage(is_vip, false, type, productCategory, brand, promotion, tag, attributeValueMap, startPrice, endPrice, true, true, null, null, null, null, orderType, pageable));
		render("/shop/${theme}/goods/list.ftl");
	}

	/**
	 * 列表
	 */
	private void brand_list() {
		String typeName = getPara("type");
		Goods.Type type = StrKit.notBlank(typeName) ? Goods.Type.valueOf(typeName) : null;
		String orderTypeName = getPara("orderType");
		Goods.OrderType orderType = StrKit.notBlank(orderTypeName) ? Goods.OrderType.valueOf(orderTypeName) : null;
		Long brandId = getParaToLong("brandId");
		Long promotionId = getParaToLong("promotionId");
		Long tagId = getParaToLong("tagId");
		
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
		
		Integer pageNumber = getParaToInt("pageNumber");
		Integer pageSize = getParaToInt("pageSize");
		
		Brand brand = brandService.find(brandId);
		Promotion promotion = promotionService.find(promotionId);
		Tag tag = tagService.find(tagId);
		Boolean is_vip = false;
		Pageable pageable = new Pageable(pageNumber, pageSize);
		setAttr("orderTypes", Goods.OrderType.values());
		setAttr("type", type);
		setAttr("brand", brand);
		setAttr("promotion", promotion);
		setAttr("tag", tag);
		setAttr("startPrice", startPrice);
		setAttr("endPrice", endPrice);
		setAttr("orderType", orderType);
		setAttr("pageNumber", pageNumber);
		setAttr("pageSize", pageSize);
		setAttr("page", goodsService.findPage(is_vip, false ,type, null, brand, promotion, tag, null, startPrice, endPrice, true, true, null, null, null, null, orderType, pageable));
		render("/shop/${theme}/goods/list.ftl");
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
		
		if (StringUtils.isEmpty(keyword)) {
			redirect(ERROR_VIEW);
			return;
		}
		Pageable pageable = getBean(Pageable.class);
		setAttr("orderTypes", Goods.OrderType.values());
		setAttr("goodsKeyword", keyword);
		setAttr("startPrice", startPrice);
		setAttr("endPrice", endPrice);
		setAttr("orderType", orderType);
		setAttr("pageable", pageable);
		setAttr("page", searchService.search(keyword, startPrice, endPrice, orderType, pageable));
		render("/shop/${theme}/goods/search.ftl");
	}

	/**
	 * 对比
	 */
	public void compare() {
		Long[] goodsIds = getParaValuesToLong("goodsIds");
		if (ArrayUtils.isEmpty(goodsIds) || goodsIds.length > MAX_COMPARE_GOODS_COUNT) {
			redirect(ERROR_VIEW);
			return;
		}

		List<Goods> goodsList = goodsService.findList(goodsIds);
		if (CollectionUtils.isEmpty(goodsList)) {
			redirect(ERROR_VIEW);
			return;
		}

		setAttr("goodsList", goodsList);
		render("/shop/${theme}/goods/compare.ftl");
	}

	/**
	 * 点击数
	 */
	public void hits() {
		Long goodsId = getParaToLong(0);
		if (goodsId == null) {
			renderJson(0L);
		}

		renderJson(goodsService.viewHits(goodsId));
	}

}