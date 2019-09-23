package com.wtshop.controller.admin;

import com.jfinal.ext.route.ControllerBind;
import com.wtshop.Pageable;
import com.wtshop.model.FullAnti;
import com.wtshop.model.Goods;
import com.wtshop.model.Product;
import com.wtshop.service.*;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

import static com.wtshop.controller.wap.BaseController.convertToLong;

/**
 * Controller - 促销
 * 
 * 
 */
@ControllerBind(controllerKey = "/admin/fullAnti")
public class FullAntiController extends BaseController {

	private FullAntiService promotionService = enhance(FullAntiService.class);
	private MemberRankService memberRankService = enhance(MemberRankService.class);
	private ProductService productService = enhance(ProductService.class);
	private CouponService couponService = enhance(CouponService.class);
	private GoodsService goodsService = enhance(GoodsService.class);

	/**
	 * 检查价格运算表达式是否正确
	 */
	public void checkPriceExpression() {
		String priceExpression = getPara("promotion.price_expression");
		if (StringUtils.isEmpty(priceExpression)) {
			renderJson(false);
			return;
		}
		renderJson(promotionService.isValidPriceExpression(priceExpression));
	}

	/**
	 * 检查积分运算表达式是否正确
	 */
	public void checkPointExpression() {
		String pointExpression = getPara("promotion.point_expression");
		if (StringUtils.isEmpty(pointExpression)) {
			renderJson(false);
			return;
		}
		renderJson(promotionService.isValidPointExpression(pointExpression));
	}

	/**
	 * 赠品选择
	 */
	public void giftSelect() {
		String keyword = getPara("q");
		Long[] excludeIds = getParaValuesToLong("excludeIds");
		Integer count = getParaToInt("limit");
		
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		if (StringUtils.isEmpty(keyword)) {
			renderJson(data);
			return;
		}
		Set<Product> excludes = new HashSet<Product>(productService.findList(excludeIds));
		List<Product> products = productService.search(Goods.Type.gift, keyword, excludes, count);
		for (Product product : products) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("id", product.getId());
			item.put("sn", product.getSn());
			item.put("name", product.getName());
			item.put("specifications", product.getSpecifications());
			item.put("url", product.getUrl());
			data.add(item);
		}
		renderJson(data);
	}

	/**
	 * 添加
	 */
	public void add() {
		setAttr("memberRanks", memberRankService.findAll());
		setAttr("coupons", couponService.findAll());
		render("/admin/full_anti/add.ftl");
	}

	/**
	 * 保存
	 */
	public void save() {
		FullAnti promotion = getModel(FullAnti.class);
		promotionService.save(promotion);
		addFlashMessage(SUCCESS_MESSAGE);
		redirect("/admin/fullAnti/list.jhtml");
	}

	/**
	 * 编辑
	 */
	public void edit() {
		Long id = getParaToLong("id");
		setAttr("fullAnti", promotionService.find(id));
		/*setAttr("memberRanks", memberRankService.findAll());
		setAttr("coupons", couponService.findAll());*/
		render("/admin/full_anti/edit.ftl");
	}

	/**
	 * 更新
	 */
	public void update() {
		FullAnti promotion = getModel(FullAnti.class);
		//Long[] memberRankIds = getParaValuesToLong("memberRankIds");
	//	Long[] couponIds = getParaValuesToLong("couponIds");
		Long[] giftIds = getParaValuesToLong("giftIds");
	//	Boolean isFreeShipping = getParaToBoolean("isFreeShipping", false);
	//	Boolean isCouponAllowed = getParaToBoolean("isCouponAllowed", false);

		//promotion.remove("goods", "productCategories");
		promotionService.update(promotion);
		addFlashMessage(SUCCESS_MESSAGE);
		redirect("/admin/fullAnti/list.jhtml");
	}

	/**
	 * 列表
	 */
	public void list() {
		Pageable pageable = getBean(Pageable.class);
		setAttr("pageable", pageable);
		setAttr("page", promotionService.findPage(pageable));
		render("/admin/full_anti/list.ftl");
	}

	/**
	 * 删除
	 */
	public void delete() {
		String[] values = StringUtils.split(getPara("ids"), ",");
		Long[] ids = values == null ? null :convertToLong(values);
		promotionService.delete(ids);
		renderJson(SUCCESS_MESSAGE);
	}

	/**
	 * 查看
	 */
	public void view(){
		Long id = getParaToLong("id");
		setAttr("promotion", goodsService.findGoodsByPromId(id));
		render("/admin/full_anti/goodsList.ftl");
	}

}