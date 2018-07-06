package com.wtshop.controller.shop;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;
import com.wtshop.Setting;
import com.wtshop.interceptor.ThemeInterceptor;
import com.wtshop.model.Member;
import com.wtshop.model.Order;
import com.wtshop.model.PaymentLog;
import com.wtshop.model.PaymentMethod;
import com.wtshop.plugin.PaymentPlugin;
import com.wtshop.service.MemberService;
import com.wtshop.service.OrderService;
import com.wtshop.service.PaymentLogService;
import com.wtshop.service.PluginService;
import com.wtshop.util.SystemUtils;

/**
 * Controller - 支付
 * 
 * 
 */
@ControllerBind(controllerKey = "/payment")
@Before(ThemeInterceptor.class)
public class PaymentController extends BaseController {

	private OrderService orderService = enhance(OrderService.class);
	private MemberService memberService = enhance(MemberService.class);
	private PluginService pluginService = enhance(PluginService.class);
	private PaymentLogService paymentLogService = enhance(PaymentLogService.class);

	/**
	 * 插件提交
	 */
	public void pluginSubmit() {
		String typeName = getPara("type", "");
		PaymentLog.Type type = StrKit.notBlank(typeName) ? PaymentLog.Type.valueOf(typeName) : null;
		String paymentPluginId = getPara("paymentPluginId");
		String sn = getPara("sn");
		BigDecimal amount = new BigDecimal(getPara("amount", "0"));
		
		if (type == null) {
			redirect(ERROR_VIEW);
			return;
		}
		Member member = memberService.getCurrent();
		if (member == null) {
			redirect(ERROR_VIEW);
			return;
		}
		PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(paymentPluginId);
		if (paymentPlugin == null || !paymentPlugin.getIsEnabled()) {
			redirect(ERROR_VIEW);
			return;
		}
		Setting setting = SystemUtils.getSetting();
		switch (type) {
			case recharge: {
				if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0 || amount.precision() > 15 || amount.scale() > setting.getPriceScale()) {
					redirect(ERROR_VIEW);
					return;
				}
				PaymentLog paymentLog = new PaymentLog();
				paymentLog.setSn(null);
				paymentLog.setType(type.ordinal());
				paymentLog.setStatus(PaymentLog.Status.wait.ordinal());
				paymentLog.setFee(paymentPlugin.calculateFee(amount));
				paymentLog.setAmount(paymentPlugin.calculateAmount(amount));
				paymentLog.setPaymentPluginId(paymentPluginId);
				paymentLog.setPaymentPluginName(paymentPlugin.getName());
				paymentLog.setMemberId(member.getId());
				paymentLog.setOrder(null);
				paymentLogService.save(paymentLog);
				setAttr("parameterMap", paymentPlugin.getParameterMap(paymentLog.getSn(), message("shop.payment.rechargeDescription"), getRequest()));
				break;
			}
			case payment: {
				Order order = orderService.findBySn(sn);
				if (order == null || !member.equals(order.getMember()) || orderService.isLocked(order, member, true)) {
					redirect(ERROR_VIEW);
					return;
				}
				if (order.getPaymentMethod() == null || !PaymentMethod.Method.online.equals(order.getPaymentMethod().getMethodName())) {
					redirect(ERROR_VIEW);
					return;
				}
				if (order.getAmountPayable().compareTo(BigDecimal.ZERO) <= 0) {
					redirect(ERROR_VIEW);
					return;
				}
				PaymentLog paymentLog = new PaymentLog();
				paymentLog.setSn(null);
				paymentLog.setType(type.ordinal());
				paymentLog.setStatus(PaymentLog.Status.wait.ordinal());
				paymentLog.setFee(paymentPlugin.calculateFee(order.getAmount()));
				paymentLog.setAmount(paymentPlugin.calculateAmount(order.getAmount()));
				paymentLog.setPaymentPluginId(paymentPluginId);
				paymentLog.setPaymentPluginName(paymentPlugin.getName());
				paymentLog.setOrderId(order.getId());
				paymentLog.setMember(null);
				paymentLogService.save(paymentLog);
				setAttr("parameterMap", paymentPlugin.getParameterMap(paymentLog.getSn(), message("shop.payment.paymentDescription", order.getSn()), getRequest()));
				break;
			}
		}
		setAttr("requestUrl", paymentPlugin.getRequestUrl());
		setAttr("requestMethod", paymentPlugin.getRequestMethod());
		setAttr("requestCharset", paymentPlugin.getRequestCharset());
		if (StringUtils.isNotEmpty(paymentPlugin.getRequestCharset())) {
			getResponse().setContentType("text/html; charset=" + paymentPlugin.getRequestCharset());
		}
		render("/shop/${theme}/payment/plugin_submit.ftl");
	}

	/**
	 * 插件通知
	 */
	//@RequestMapping("/plugin_notify/{pluginId}/{notifyMethod}")
	public void pluginNotify() {
		String pluginId = getPara("pluginId");
		String notifyMethodName = getPara("notifyMethod");
		PaymentPlugin.NotifyMethod notifyMethod = notifyMethodName != null ? PaymentPlugin.NotifyMethod.valueOf(notifyMethodName) : null;

		PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(pluginId);
		if (paymentPlugin != null && paymentPlugin.verifyNotify(notifyMethod, getRequest())) {
			String sn = paymentPlugin.getSn(getRequest());
			PaymentLog paymentLog = paymentLogService.findBySn(sn);
			if (paymentLog != null) {
				paymentLogService.handle(paymentLog);
				setAttr("paymentLog", paymentLog);
				setAttr("notifyMessage", paymentPlugin.getNotifyMessage(notifyMethod, getRequest()));
			}
		}
		render("/shop/${theme}/payment/plugin_notify.ftl");
	}

}