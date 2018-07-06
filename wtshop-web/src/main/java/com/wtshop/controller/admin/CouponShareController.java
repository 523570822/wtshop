package com.wtshop.controller.admin;

import com.jfinal.ext.route.ControllerBind;
import com.wtshop.Pageable;
import com.wtshop.dao.CouponDao;
import com.wtshop.model.Coupon;
import com.wtshop.model.CouponShare;
import com.wtshop.service.CouponShareService;

/**
 * Controller - 优惠券
 * 
 * 
 */
@ControllerBind(controllerKey = "/admin/couponShare")
public class CouponShareController extends BaseController {

	private CouponShareService cservice= enhance(CouponShareService.class);
	private CouponDao couponDao=enhance(CouponDao.class);

	/**
	 * 添加
	 */
	public void add() {
		render("/admin/couponShare/add.ftl");
	}

	/**
	 * 保存
	 */
	public void save() {
		CouponShare cs = getModel(CouponShare.class);
		cservice.save(cs);
		addFlashMessage(SUCCESS_MESSAGE);
		redirect("list.jhtml");
	}

	/**
	 * 编辑
	 */
	public void edit() {
		Long id = getParaToLong("id");
		CouponShare cs=cservice.find(id);
		Coupon c=couponDao.find(cs.getCouponId());
		setAttr("couponShare", cs);
		setAttr("coupon", c);
		render("/admin/couponShare/edit.ftl");
	}

	/**
	 * 更新
	 */
	public void update() {
		CouponShare coupon = getModel(CouponShare.class);
		CouponShare  cs=cservice.find(coupon.getId());
		coupon.setCurrentNum(cs.getCurrentNum());
		cservice.update(coupon);
		addFlashMessage(SUCCESS_MESSAGE);
		redirect("list.jhtml");
	}

	/**
	 * 列表
	 */
	public void list() {
		Pageable pageable = getBean(Pageable.class);
		setAttr("pageable", pageable);
		setAttr("page", cservice.getlist(pageable));
		render("/admin/couponShare/list.ftl");
	}

	/**
	 * 删除
	 */
	public void delete() {
		Long[] ids = getParaValuesToLong("ids");
		cservice.delete(ids);
		renderJson(SUCCESS_MESSAGE);
	}



	//获取优惠券

public void getCouponList(){

		renderJson(   couponDao.getallEnable());
}






}