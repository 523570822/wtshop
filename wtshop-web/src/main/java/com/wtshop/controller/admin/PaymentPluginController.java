package com.wtshop.controller.admin;

import com.jfinal.ext.route.ControllerBind;
import com.wtshop.service.PluginService;

/**
 * Controller - 支付插件
 * 
 * 
 */
@ControllerBind(controllerKey = "/admin/payment_plugin")
public class PaymentPluginController extends BaseController {

	private PluginService pluginService = enhance(PluginService.class);

	/**
	 * 列表
	 */
	public void list() {
		setAttr("paymentPlugins", pluginService.getPaymentPlugins());
		render("/admin/payment_plugin/list.ftl");
	}

}