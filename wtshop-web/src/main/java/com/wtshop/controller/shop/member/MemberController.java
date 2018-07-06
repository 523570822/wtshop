package com.wtshop.controller.shop.member;

import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.wtshop.controller.shop.BaseController;
import com.wtshop.interceptor.MemberInterceptor;
import com.wtshop.model.Coupon;
import com.wtshop.model.Member;
import com.wtshop.model.Order;
import com.wtshop.service.ConsultationService;
import com.wtshop.service.CouponCodeService;
import com.wtshop.service.GoodsService;
import com.wtshop.service.MemberService;
import com.wtshop.service.MessageService;
import com.wtshop.service.OrderService;
import com.wtshop.service.ProductNotifyService;
import com.wtshop.service.ReviewService;

/**
 * Controller - 会员中心
 * 
 * 
 */
@ControllerBind(controllerKey = "/member")
@Before(MemberInterceptor.class)
public class MemberController extends BaseController {

	/** 最新订单数 */
	private static final int NEW_ORDER_COUNT = 6;

	private MemberService memberService = new MemberService();
	private OrderService orderService = new OrderService();
	private CouponCodeService couponCodeService = new CouponCodeService();
	private MessageService messageService = new MessageService();
	private GoodsService goodsService = new GoodsService();
	private ProductNotifyService productNotifyService = new ProductNotifyService();
	private ReviewService reviewService = new ReviewService();
	private ConsultationService consultationService = new ConsultationService();

	/**
	 * 首页
	 */
	public void index() {
		//Integer pageNumber = getParaToInt("pageNumber");
		Member member = memberService.getCurrent();
		setAttr("pendingPaymentOrderCount", orderService.count(null, Order.Status.pendingPayment, member, null, null, null, null, null, null, false));
		setAttr("pendingShipmentOrderCount", orderService.count(null, Order.Status.pendingShipment, member, null, null, null, null, null, null, null));
		setAttr("messageCount", messageService.count(member, false));
		setAttr("couponCodeCount", couponCodeService.count(new Coupon(), member, null, false, false));
		setAttr("favoriteCount", goodsService.count(null, member, null, null, null, null, null));
		setAttr("productNotifyCount", productNotifyService.count(member, null, null, null));
		setAttr("reviewCount", reviewService.count(member, null, null, null));
		setAttr("consultationCount", consultationService.count(member, null, null));
		setAttr("newOrders", orderService.findList(null, null, member, null, null, null, null, null, null, null, NEW_ORDER_COUNT, null, null));
		setAttr("member", member);
		render("/shop/${theme}/member/index.ftl");
	}

}