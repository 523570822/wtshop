package com.wtshop.controller.shop.member;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.wtshop.Pageable;
import com.wtshop.controller.shop.BaseController;
import com.wtshop.interceptor.MemberInterceptor;
import com.wtshop.model.Member;
import com.wtshop.plugin.PaymentPlugin;
import com.wtshop.service.DepositLogService;
import com.wtshop.service.MemberService;
import com.wtshop.service.PluginService;

/**
 * Controller - 会员中心 - 预存款
 * 
 * 
 */
@ControllerBind(controllerKey = "/member/deposit")
@Before(MemberInterceptor.class)
public class DepositController extends BaseController {

	/** 每页记录数 */
	private static final int PAGE_SIZE = 10;

	private MemberService memberService = enhance(MemberService.class);
	private DepositLogService depositLogService = enhance(DepositLogService.class);
	private PluginService pluginService = enhance(PluginService.class);

	/**
	 * 计算支付手续费
	 */
	public void calculateFee() {
		String paymentPluginId = getPara("paymentPluginId");
		BigDecimal amount = new BigDecimal(getPara("amount"));
		
		Map<String, Object> data = new HashMap<String, Object>();
		PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(paymentPluginId);
		if (paymentPlugin == null || !paymentPlugin.getIsEnabled() || amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
			data.put("message", ERROR_MESSAGE);
			renderJson(data);
			return;
		}
		data.put("message", SUCCESS_MESSAGE);
		data.put("fee", paymentPlugin.calculateFee(amount));
		renderJson(data);
	}

	/**
	 * 检查余额
	 */
	public void checkBalance() {
		Map<String, Object> data = new HashMap<String, Object>();
		Member member = memberService.getCurrent();
		data.put("balance", member.getBalance());
		renderJson(data);
	}

	/**
	 * 充值
	 */
	public void recharge() {
		List<PaymentPlugin> paymentPlugins = pluginService.getPaymentPlugins(true);
		if (!paymentPlugins.isEmpty()) {
			setAttr("defaultPaymentPlugin", paymentPlugins.get(0));
			setAttr("paymentPlugins", paymentPlugins);
		}
		render("/shop/${theme}/member/deposit/recharge.ftl");
	}

	/**
	 * 记录
	 */
	public void log() {
		Integer pageNumber = getParaToInt("pageNumber");
		Member member = memberService.getCurrent();
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		setAttr("page", depositLogService.findPage(member, pageable,1));
		setAttr("member", member);
		render("/shop/${theme}/member/deposit/log.ftl");
	}

}