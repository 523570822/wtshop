package com.wtshop.controller.shop;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.wtshop.Message;
import com.wtshop.Pageable;
import com.wtshop.Setting;
import com.wtshop.exception.ResourceNotFoundException;
import com.wtshop.interceptor.ThemeInterceptor;
import com.wtshop.model.Goods;
import com.wtshop.model.Member;
import com.wtshop.model.Review;
import com.wtshop.service.GoodsService;
import com.wtshop.service.MemberService;
import com.wtshop.service.ReviewService;
import com.wtshop.shiro.core.SubjectKit;
import com.wtshop.util.SystemUtils;

/**
 * Controller - 评论
 * 
 *
 */
@ControllerBind(controllerKey = "/review")
@Before(ThemeInterceptor.class)
public class ReviewController extends BaseController {

	/** 每页记录数 */
	private static final int PAGE_SIZE = 10;

	private ReviewService reviewService = enhance(ReviewService.class);
	private GoodsService goodsService = enhance(GoodsService.class);
	private MemberService memberService = enhance(MemberService.class);

	/**
	 * 发表
	 */
	public void add() {
		Long goodsId = getParaToLong(0);
		Setting setting = SystemUtils.getSetting();
		if (!setting.getIsReviewEnabled()) {
			throw new ResourceNotFoundException();
		}
		Goods goods = goodsService.find(goodsId);
		if (goods == null) {
			throw new ResourceNotFoundException();
		}

		setAttr("goods", goods);
		setAttr("captchaId", UUID.randomUUID().toString());
		render("/shop/${theme}/review/add.ftl");
	}

	/**
	 * 内容
	 */
	public void content() {
		Long goodsId = getParaToLong(0);
		Integer pageNumber = getParaToInt("pageNumber");
		
		Setting setting = SystemUtils.getSetting();
		if (!setting.getIsReviewEnabled()) {
			throw new ResourceNotFoundException();
		}
		Goods goods = goodsService.find(goodsId);
		if (goods == null) {
			throw new ResourceNotFoundException();
		}

		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		setAttr("goods", goods);
		setAttr("page", reviewService.findPage(null, goods, null, true, pageable));
		render("/shop/${theme}/review/content.ftl");
	}

	/**
	 * 保存
	 */
	public void save() {
		String captcha = getPara("captcha");
		Long goodsId = getParaToLong("goodsId");
		Integer score = getParaToInt("score");
		String content = getPara("content");
		HttpServletRequest request = getRequest();
		
		if (!SubjectKit.doCaptcha("captcha", captcha)) {
			renderJson(Message.error("shop.captcha.invalid"));
			return;
		}
		
		Setting setting = SystemUtils.getSetting();
		if (!setting.getIsReviewEnabled()) {
			renderJson(Message.error("shop.review.disabled"));
			return;
		}
		Goods goods = goodsService.find(goodsId);
		if (goods == null) {
			renderJson(ERROR_MESSAGE);
			return;
		}

		Member member = memberService.getCurrent();
		if (!Setting.ReviewAuthority.anyone.equals(setting.getReviewAuthority()) && member == null) {
			renderJson(Message.error("shop.review.accessDenied"));
			return;
		}
		if (member != null && !reviewService.hasPermission(member, goods)) {
			renderJson(Message.error("shop.review.noPermission"));
			return;
		}

		Review review = new Review();
		review.setScore(score);
		review.setContent(content);
		review.setIp(request.getRemoteAddr());
		review.setMemberId(member.getId());
		review.setProductId(goods.getProducts().get(0).getId());
		review.setGoodsId(goodsId);
		if (setting.getIsReviewCheck()) {
			review.setIsShow(false);
			reviewService.save(review);
			renderJson(Message.success("shop.review.check"));
		} else {
			review.setIsShow(true);
			reviewService.save(review);
			renderJson(Message.success("shop.review.success"));
		}
	}

}