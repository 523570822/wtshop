package com.wtshop.controller.shop.member;

import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.wtshop.Pageable;
import com.wtshop.controller.shop.BaseController;
import com.wtshop.interceptor.MemberInterceptor;
import com.wtshop.model.Member;
import com.wtshop.model.ProductNotify;
import com.wtshop.service.MemberService;
import com.wtshop.service.ProductNotifyService;

/**
 * Controller - 会员中心 - 到货通知
 * 
 * 
 */
@ControllerBind(controllerKey = "/member/product_notify")
@Before(MemberInterceptor.class)
public class ProductNotifyController extends BaseController {

	/** 每页记录数 */
	private static final int PAGE_SIZE = 10;

	private ProductNotifyService productNotifyService = enhance(ProductNotifyService.class);
	private MemberService memberService = enhance(MemberService.class);

	/**
	 * 列表
	 */
	public void list() {
		Integer pageNumber = getParaToInt("pageNumber");
		Member member = memberService.getCurrent();
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		setAttr("page", productNotifyService.findPage(member, null, null, null, pageable));
		setAttr("member", member);
		render("/shop/${theme}/member/product_notify/list.ftl");
	}

	/**
	 * 删除
	 */
	public void delete() {
		Long id = getParaToLong("id");
		ProductNotify productNotify = productNotifyService.find(id);
		if (productNotify == null) {
			renderJson(ERROR_MESSAGE);
			return;
		}
		Member member = memberService.getCurrent();
		if (!member.getProductNotifies().contains(productNotify)) {
			renderJson(ERROR_MESSAGE);
			return;
		}
		productNotifyService.delete(productNotify);
		renderJson(SUCCESS_MESSAGE);
	}

}