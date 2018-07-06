package com.wtshop.controller.wap.member;

import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.upload.UploadFile;
import com.wtshop.FileType;
import com.wtshop.Message;
import com.wtshop.controller.wap.BaseController;
import com.wtshop.interceptor.WapMemberInterceptor;
import com.wtshop.model.Coupon;
import com.wtshop.model.Member;
import com.wtshop.model.Order;
import com.wtshop.service.*;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

@ControllerBind(controllerKey = "/wap/member")
@Before(WapMemberInterceptor.class)
public class MemberController extends BaseController {

	/** 最新订单数 */
	private static final int NEW_ORDER_COUNT = 6;

	private MemberService memberService = enhance(MemberService.class);
	private OrderService orderService = enhance(OrderService.class);
	private CouponCodeService couponCodeService = enhance(CouponCodeService.class);
	private MessageService messageService = enhance(MessageService.class);
	private GoodsService goodsService = enhance(GoodsService.class);
	private ProductNotifyService productNotifyService = enhance(ProductNotifyService.class);
	private ReviewService reviewService = enhance(ReviewService.class);
	private ConsultationService consultationService = enhance(ConsultationService.class);
	private FileService fileService = enhance(FileService.class);
	
	/**
	 * 首页
	 */
	public void index() {
		Member member = memberService.getCurrent();
		setAttr("pendingPaymentOrderCount", orderService.count(null, Order.Status.pendingPayment, member, null, null, null, null, null, null, false));
		setAttr("pendingShipmentOrderCount", orderService.count(null, Order.Status.pendingShipment, member, null, null, null, null, null, null, null));
		setAttr("messageCount", messageService.count(member, false));
		setAttr("couponCodeCount", couponCodeService.count(new Coupon(), member, null, null, false));
		setAttr("favoriteCount", goodsService.count(null, member, null, null, null, null, null));
		setAttr("productNotifyCount", productNotifyService.count(member, null, null, null));
		setAttr("reviewCount", reviewService.count(member, null, null, null));
		setAttr("consultationCount", consultationService.count(member, null, null));
		setAttr("newOrders", orderService.findList(null, null, member, null, null, null, null, null, null, null, NEW_ORDER_COUNT, null, null));
		// wap
		setAttr("memberPendingPaymentOrderCount", orderService.count(null, Order.Status.pendingPayment, member, null, null, null, null, null, null, null));
		setAttr("memberPendingShipmentOrderCount", orderService.count(null, Order.Status.pendingShipment, member, null, null, null, null, null, null, null));
		setAttr("memberReceivedOrderCount", orderService.count(null, Order.Status.received, member, null, null, null, null, null, null, null));
		setAttr("pendingReviewCount", reviewService.count(member, false));
		setAttr("member", member);
		setAttr("title" , "会员中心");
		render("/wap/member/index.ftl");
	}
	
	
	/**
	 * 上传
	 */
	public void upload() {
		UploadFile file = getFile();
		FileType fileType = FileType.valueOf(getPara("fileType", "image"));
		
		Member member = memberService.getCurrent();
		Map<String, Object> data = new HashMap<String, Object>();
		if (member == null) {
			data.put(MESSAGE, "当前用户不能为空!");
			data.put(STATUS, ERROR);
			renderJson(data);
			return;
		}
		if (fileType == null || file == null || file.getFile().length() <= 0) {
			data.put(MESSAGE, "请选择选图片");
			data.put(STATUS, ERROR);
			renderJson(data);
			return;
		}
		if (!fileService.isValid(fileType, file)) {
			data.put(MESSAGE, Message.warn("admin.upload.invalid"));
			data.put(STATUS, ERROR);
			renderJson(data);
			return;
		}
		String url = fileService.upload(fileType, file, false);
		if (StringUtils.isEmpty(url)) {
			data.put(MESSAGE, Message.warn("admin.upload.error"));
			data.put(STATUS, ERROR);
			renderJson(data);
			return;
		}
		member.setAvatar(url);
		memberService.update(member);
		data.put(MESSAGE, "上传成功!");
		data.put(STATUS, SUCCESS);
		renderJson(data);
	}
}
