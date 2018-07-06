package com.wtshop.controller.admin;

import java.util.Date;
import java.util.List;

import com.jfinal.plugin.activerecord.Page;
import com.wtshop.*;
import com.wtshop.model.*;
import com.wtshop.model.Message;
import com.wtshop.service.*;
import com.wtshop.util.MathUtil;
import com.sun.tools.corba.se.idl.InterfaceGen;
import io.swagger.models.auth.In;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import com.jfinal.ext.render.excel.PoiRender;
import com.jfinal.ext.route.ControllerBind;

/**
 * Controller - 优惠券
 * 
 * 
 */
@ControllerBind(controllerKey = "/admin/coupon")
public class CouponController extends BaseController {

	private CouponService couponService = enhance(CouponService.class);
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
		render("/admin/coupon/add.ftl");
	}

	/**
	 * 保存
	 */
	public void save() {
		Coupon coupon = getModel(Coupon.class);
		Boolean isEnabled = getParaToBoolean("isEnabled", false);
		Boolean isExchange = getParaToBoolean("isExchange", false);
		coupon.setIsEnabled(isEnabled);
		coupon.setIsExchange(isExchange);

		//优惠券种类 1 分类优惠券 2商品优惠券
		Integer type = 0;

		//优惠券状态 0未开始 1正在使用 2已过期
		Integer status = null;
		if(coupon.getProductId() != null && coupon.getProductCategoryId() != null ){
			addFlashMessage(new com.wtshop.Message(com.wtshop.Message.Type.error,"只能选择一类优惠券!"));
			redirect("list.jhtml");
			return;
		}

		if(coupon.getProductId() == null && coupon.getProductCategoryId() == null ){
			addFlashMessage(new com.wtshop.Message(com.wtshop.Message.Type.error,"请选择一类优惠券!"));
			redirect("list.jhtml");
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
			redirect(ERROR_VIEW);
			return;
		}
		if (coupon.getMinimumQuantity() != null && coupon.getMaximumQuantity() != null && coupon.getMinimumQuantity() > coupon.getMaximumQuantity()) {
			redirect(ERROR_VIEW);
			return;
		}
		if (coupon.getMinimumPrice() != null && coupon.getMaximumPrice() != null && coupon.getMinimumPrice().compareTo(coupon.getMaximumPrice()) > 0) {
			redirect(ERROR_VIEW);
			return;
		}
		if (StringUtils.isNotEmpty(coupon.getPriceExpression()) && !couponService.isValidPriceExpression(coupon.getPriceExpression())) {
			redirect(ERROR_VIEW);
			return;
		}
		if (coupon.getIsExchange() && coupon.getPoint() == null) {
			redirect(ERROR_VIEW);
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

		if( !coupon.getMoney().equals("0") && coupon.getModulus().equals("10")){
			coupon.setDesc("满" + coupon.getCondition() +"元减" + coupon.getMoney()+"元");
		}else if( coupon.getMoney().equals("0") && !coupon.getModulus().equals("10")){
			coupon.setDesc("满" + coupon.getCondition() +"元打" + coupon.getModulus()+"折");
		}else if( !coupon.getMoney().equals("0") && !coupon.getModulus().equals("10")){
			coupon.setDesc("满" + coupon.getCondition() +"元打" + coupon.getModulus()+"折"+"再减"+ coupon.getMoney()+"元");
		}
		coupon.setType(type);
		coupon.setStatus(status);
		coupon.setCouponCodes(null);
		coupon.setPromotions(null);
		coupon.setOrders(null);
		couponService.save(coupon);
		addFlashMessage(SUCCESS_MESSAGE);
		redirect("list.jhtml");
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
		render("/admin/coupon/edit.ftl");
	}

	/**
	 * 更新
	 */
	public void update() {
		Coupon coupon = getModel(Coupon.class);
		Boolean isEnabled = getParaToBoolean("isEnabled", false);
		Boolean isExchange = getParaToBoolean("isExchange", false);
		coupon.setIsEnabled(isEnabled);
		coupon.setIsExchange(isExchange);

		Integer type = 0;
		if(coupon.getProductId() != null && coupon.getProductCategoryId() != null ){
			addFlashMessage(new com.wtshop.Message(com.wtshop.Message.Type.error,"只能选择一类优惠券!"));
			redirect("list.jhtml");
			return;
		}

		if(coupon.getProductId() == null && coupon.getProductCategoryId() == null ){
			addFlashMessage(new com.wtshop.Message(com.wtshop.Message.Type.error,"请选择一类优惠券!"));
			redirect("list.jhtml");
			return;
		}
		if(coupon.getProductId() != null){
			type  = 2;
			Goods goods = goodsService.find(productService.find(coupon.getProductId()).getGoodsId());
			coupon.setProductCategoryId(goods.getProductCategoryId());
		}

		if(coupon.getProductCategoryId() != null){
			type  = 1;
		}
		if(coupon.getProductId() != null ){
			Goods goods = goodsService.find(productService.find(coupon.getProductId()).getGoodsId());
			coupon.setProductCategoryId( goods.getProductCategoryId());
		}
		
		if (coupon.getBeginDate() != null && coupon.getEndDate() != null && coupon.getBeginDate().after(coupon.getEndDate())) {
			redirect(ERROR_VIEW);
			return;
		}
		if (coupon.getMinimumQuantity() != null && coupon.getMaximumQuantity() != null && coupon.getMinimumQuantity() > coupon.getMaximumQuantity()) {
			redirect(ERROR_VIEW);
			return;
		}
		if (coupon.getMinimumPrice() != null && coupon.getMaximumPrice() != null && coupon.getMinimumPrice().compareTo(coupon.getMaximumPrice()) > 0) {
			redirect(ERROR_VIEW);
			return;
		}
		if (StringUtils.isNotEmpty(coupon.getPriceExpression()) && !couponService.isValidPriceExpression(coupon.getPriceExpression())) {
			redirect(ERROR_VIEW);
			return;
		}
		if (coupon.getIsExchange() && coupon.getPoint() == null) {
			redirect(ERROR_VIEW);
			return;
		}
		if (!coupon.getIsExchange()) {
			coupon.setPoint(null);
		}
		if( !coupon.getMoney().equals("0") && coupon.getModulus().equals("10")){
			coupon.setDesc("满" + coupon.getCondition() +"元减" + coupon.getMoney()+"元");
		}else if( coupon.getMoney().equals("0") && !coupon.getModulus().equals("10")){
			coupon.setDesc("满" + coupon.getCondition() +"元打" + coupon.getModulus()+"折");
		}else if( !coupon.getMoney().equals("0") && !coupon.getModulus().equals("10")){
			coupon.setDesc("满" + coupon.getCondition() +"元打" + coupon.getModulus()+"折"+"再减"+ coupon.getMoney()+"元");
		}
		coupon.setType(type);
		couponService.update(coupon);
		addFlashMessage(SUCCESS_MESSAGE);
		redirect("list.jhtml");
	}

	/**
	 * 列表
	 */
	public void list() {
		Pageable pageable = getBean(Pageable.class);
		Page<Coupon> page = couponService.findPage(pageable);
		for(Coupon coupon : page.getList()){
			long count = couponCodeService.count(coupon, null, true, false, null);
			Double aDouble = MathUtil.subtract(coupon.getCount(), count);
			coupon.setCount( (new Double(aDouble)).intValue());
		}
		setAttr("pageable", pageable);
		setAttr("page", page);
		render("/admin/coupon/list.ftl");
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
		Coupon coupon = couponService.find(id);
		setAttr("coupon", coupon);
		setAttr("totalCount", couponCodeService.count(coupon, null, null, null, null));
		setAttr("usedCount", couponCodeService.count(coupon, null, null, null, true));
		render("/admin/coupon/generate.ftl");
	}

	/**
	 * 下载优惠码
	 */
	public void download() {
		Long id = getParaToLong("id");
		Integer count = getParaToInt("count");
		if (count == null || count <= 0) {
			count = 100;
		}
		Coupon coupon = couponService.find(id);
		List<CouponCode> data = couponCodeService.generate(coupon, null, count);
		String filename = "coupon_code_" + DateFormatUtils.format(new Date(), "yyyyMMdd") + ".xls";
		String[] contents = new String[4];
		contents[0] = message("admin.coupon.type") + ": " + coupon.getName();
		contents[1] = message("admin.coupon.count") + ": " + count;
		contents[2] = message("admin.coupon.operator") + ": " + adminService.getCurrentUsername();
		contents[3] = message("admin.coupon.date") + ": " + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
		String[] headers = new String[]{message("admin.coupon.title")};
		String[] columns = new String[]{"code"};
	    render(PoiRender.me(data).fileName(filename).sheetName(filename).headers(headers).columns(columns).cellWidth(9000));
	}

}