package com.wtshop.controller.admin;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Page;
import com.wtshop.Filter;
import com.wtshop.Pageable;
import com.wtshop.model.Goods;
import com.wtshop.model.ProductCategory;
import com.wtshop.model.Ticket;
import com.wtshop.service.*;
import com.wtshop.util.MathUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Controller - 优惠券
 * 
 * 
 */
@ControllerBind(controllerKey = "/admin/ticketpool")
public class TicketController extends BaseController {

	private TicketService couponService = enhance(TicketService.class);
	private CouponCodeService couponCodeService = enhance(CouponCodeService.class);
	private ProductCategoryService productCategoryService = enhance(ProductCategoryService.class);
	private AdminService adminService = enhance(AdminService.class);
	private ProductService productService = enhance(ProductService.class);
	private GoodsService goodsService = enhance(GoodsService.class);

	/**
	 * 检查价格运算表达式是否正确
	 */
	public void checkPriceExpression() {
		String priceExpression = getPara("coupon.price_expression");
		if (StringUtils.isEmpty(priceExpression)) {
			renderJson(false);
			return;
		}
		renderJson(couponService.isValidPriceExpression(priceExpression));
	}

	/**
	 * 添加
	 */
	public void add() {
		List<ProductCategory> tree = productCategoryService.findTree();
		ProductCategory productCategory = new ProductCategory();
		productCategory.setId(242L);
		productCategory.setName("通用");
		productCategory.setGrade(0);
		tree.add(productCategory);
		setAttr("productCategoryTree",tree);
		setAttr("configId",getPara("configId"));
		render("/admin/ticket/add.ftl");
	}

	/**
	 * 保存
	 */
	public void save() {
		Ticket coupon = getModel(Ticket.class);
		Boolean isEnabled = getParaToBoolean("isEnabled", false);
		Boolean isExchange = getParaToBoolean("isExchange", false);
		coupon.setIsEnabled(isEnabled);
		coupon.setIsExchange(isExchange);

		//优惠券种类 1 分类优惠券 2商品优惠券
		Integer type = 0;

		//优惠券状态 0未开始 1正在使用 2已过期
		Integer status = null;
		if(coupon.getProductId() != null && coupon.getProductCategoryId() != null ){
			addFlashMessage(com.wtshop.Message.errMsg("分类和商品id不能同时选择"));
			redirect("list.jhtml?configId="+coupon.getConfigId());
			return;
		}

		if(coupon.getProductId() == null && coupon.getProductCategoryId() == null ){
			addFlashMessage(com.wtshop.Message.errMsg("分类和商品id不能都不选择"));
			redirect("list.jhtml?configId="+coupon.getConfigId());
			return;
		}
		//如果添加的商品 找到分类(手机端只能显示护肤 美发)
		if(coupon.getProductId() != null){
			type  = 2;
			Goods goods = goodsService.find(productService.find(coupon.getProductId()).getGoodsId());
			coupon.setProductCategoryId(goods.getProductCategoryId());
		}

		if(coupon.getProductCategoryId() != null){
			type  = 1;
		}

		if (coupon.getBeginDate() != null && coupon.getEndDate() != null && coupon.getBeginDate().after(coupon.getEndDate())) {
			addFlashMessage(com.wtshop.Message.errMsg("开始时间必须小于结束时间"));
			redirect("list.jhtml?configId="+coupon.getConfigId());
			return;
		}
		if (coupon.getMinimumQuantity() != null && coupon.getMaximumQuantity() != null && coupon.getMinimumQuantity() > coupon.getMaximumQuantity()) {
			addFlashMessage(com.wtshop.Message.errMsg("最大商品数量应大于最小商品数量"));
			redirect("list.jhtml?configId="+coupon.getConfigId());
			return;
		}
		if (coupon.getMinimumPrice() != null && coupon.getMaximumPrice() != null && coupon.getMinimumPrice().compareTo(coupon.getMaximumPrice()) > 0) {
			addFlashMessage(com.wtshop.Message.errMsg("最大商品单价应大于最小商品单价"));
			redirect("list.jhtml");
			return;
		}
		if (StringUtils.isNotEmpty(coupon.getPriceExpression()) && !couponService.isValidPriceExpression(coupon.getPriceExpression())) {
			addFlashMessage(com.wtshop.Message.errMsg("表达式无效"));
			redirect("list.jhtml?configId="+coupon.getConfigId());
			return;
		}
		if (coupon.getIsExchange() && coupon.getPoint() == null) {
			addFlashMessage(com.wtshop.Message.errMsg("兑换积分不能为空"));
			redirect("list.jhtml?configId="+coupon.getConfigId());
			return;
		}
		if (!coupon.getIsExchange()) {
			coupon.setPoint(null);
		}
		//已经过期
		if(coupon.hasExpired()){
			status = 2;
		}
		//还未开始
		if(!coupon.hasBegun()){
			status = 0;
		}
		//还未开始
		if(coupon.hasBegun() && !coupon.hasExpired()){
			status = 1;
		}
		coupon.setType(type);
		coupon.setStatus(status);
		coupon.setCouponCodes(null);
		coupon.setPromotions(null);
		coupon.setOrders(null);
		couponService.save(coupon);
		addFlashMessage(SUCCESS_MESSAGE);
        redirect("list.jhtml?configId="+coupon.getConfigId());
	}

	/**
	 * 编辑
	 */
	public void edit() {

		List<ProductCategory> tree = productCategoryService.findTree();
		ProductCategory productCategory = new ProductCategory();
		productCategory.setId(242L);
		productCategory.setName("通用");
		productCategory.setGrade(0);
		tree.add(productCategory);
		setAttr("productCategoryTree", tree);
		Long id = getParaToLong("id");
		setAttr("coupon", couponService.find(id));
		render("/admin/ticket/edit.ftl");
	}

	/**
	 * 更新
	 */
	public void update() {
		Ticket coupon = getModel(Ticket.class);
		Boolean isEnabled = getParaToBoolean("isEnabled", false);
		Boolean isExchange = getParaToBoolean("isExchange", false);
		coupon.setIsEnabled(isEnabled);
		coupon.setIsExchange(isExchange);

		Integer type = 0;
        if(coupon.getProductId() != null && coupon.getProductCategoryId() != null ){
            addFlashMessage(com.wtshop.Message.errMsg("分类和商品id不能同时选择"));
			redirect("list.jhtml?configId="+coupon.getConfigId());
            return;
        }

        if(coupon.getProductId() == null && coupon.getProductCategoryId() == null ){
            addFlashMessage(com.wtshop.Message.errMsg("分类和商品id不能都不选择"));
			redirect("list.jhtml?configId="+coupon.getConfigId());
            return;
        }
		if(coupon.getProductId() != null){
			type  = 2;
		}

		if(coupon.getProductCategoryId() != null){
			type  = 1;
		}
		if (coupon.getBeginDate() != null && coupon.getEndDate() != null && coupon.getBeginDate().after(coupon.getEndDate())) {
			addFlashMessage(com.wtshop.Message.errMsg("开始时间必须小于结束时间"));
			redirect("list.jhtml?configId="+coupon.getConfigId());
			return;
		}
		if (coupon.getMinimumQuantity() != null && coupon.getMaximumQuantity() != null && coupon.getMinimumQuantity() > coupon.getMaximumQuantity()) {
			addFlashMessage(com.wtshop.Message.errMsg("最大商品数量应大于最小商品数量"));
			redirect("list.jhtml?configId="+coupon.getConfigId());
			return;
		}
		if (coupon.getMinimumPrice() != null && coupon.getMaximumPrice() != null && coupon.getMinimumPrice().compareTo(coupon.getMaximumPrice()) > 0) {
			addFlashMessage(com.wtshop.Message.errMsg("最大商品单价应大于最小商品单价"));
			redirect("list.jhtml?configId="+coupon.getConfigId());
			return;
		}
		if (StringUtils.isNotEmpty(coupon.getPriceExpression()) && !couponService.isValidPriceExpression(coupon.getPriceExpression())) {
			addFlashMessage(com.wtshop.Message.errMsg("表达式无效"));
			redirect("list.jhtml?configId="+coupon.getConfigId());
			return;
		}
		if (coupon.getIsExchange() && coupon.getPoint() == null) {
			addFlashMessage(com.wtshop.Message.errMsg("兑换积分不能为空"));
			redirect("list.jhtml?configId="+coupon.getConfigId());
			return;
		}
		if (!coupon.getIsExchange()) {
			coupon.setPoint(null);
		}
		coupon.setType(type);
		couponService.update(coupon);
		addFlashMessage(SUCCESS_MESSAGE);
		redirect("list.jhtml?configId="+coupon.getConfigId());
	}

	/**
	 * 列表
	 */
	public void list() {
		Pageable pageable = getBean(Pageable.class);
		Filter filter=Filter.eq("config_id",getPara("configId"));
		pageable.setFilter(filter);
		Page<Ticket> page = couponService.findPage(pageable);

		for(Ticket coupon : page.getList()){
			long count = couponCodeService.count(coupon, null, true, false, null);
			Double aDouble = MathUtil.subtract(coupon.getCount(), count);
			coupon.setCount( (new Double(aDouble)).intValue());
		}
		setAttr("pageable", pageable);
		setAttr("page", page);
		setAttr("configId", getPara("configId"));
		render("/admin/ticket/list.ftl");
	}

	/**
	 * 删除
	 */
	public void delete() {
		Long[] ids = getParaValuesToLong("ids");
		couponService.delete(ids);
		renderJson(SUCCESS_MESSAGE);
	}

	/**
	 * 生成优惠码
	 */
	public void generate() {
		Long id = getParaToLong("id");
		Ticket coupon = couponService.find(id);
		setAttr("coupon", coupon);
		setAttr("totalCount", couponCodeService.count(coupon, null, null, null, null));
		setAttr("usedCount", couponCodeService.count(coupon, null, null, null, true));
		render("/admin/ticket/generate.ftl");
	}



}