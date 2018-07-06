package com.wtshop.controller.admin.plugin;

import com.alibaba.fastjson.JSON;
import com.jfinal.ext.route.ControllerBind;
import com.wtshop.controller.admin.BaseController;
import com.wtshop.model.PluginConfig;
import com.wtshop.plugin.LoginPlugin;
import com.wtshop.plugin.PaymentPlugin;
import com.wtshop.plugin.qqLogin.QqLoginPlugin;
import com.wtshop.plugin.wechatAppLogin.WechatAppLoginPlugin;
import com.wtshop.service.PluginConfigService;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller - QQ登录
 * 
 * 
 */
@ControllerBind(controllerKey = "/admin/login_plugin/wechat_app_login")
public class WechatAppLoginController extends BaseController {

	private WechatAppLoginPlugin wechatAppLoginPlugin = new WechatAppLoginPlugin();
	private PluginConfigService pluginConfigService = new PluginConfigService();

	/**
	 * 安装
	 */
	public void install() {
		if (!wechatAppLoginPlugin.getIsInstalled()) {
			PluginConfig pluginConfig = new PluginConfig();
			pluginConfig.setPluginId(wechatAppLoginPlugin.getId());
			pluginConfig.setIsEnabled(false);
			//pluginConfig.setAttributes(null);
			pluginConfigService.save(pluginConfig);
		}
		renderJson(SUCCESS_MESSAGE);
	}

	/**
	 * 卸载
	 */
	public void uninstall() {
		if (wechatAppLoginPlugin.getIsInstalled()) {
			pluginConfigService.deleteByPluginId(wechatAppLoginPlugin.getId());
		}
		renderJson(SUCCESS_MESSAGE);
	}

	/**
	 * 设置
	 */
	public void setting() {
		PluginConfig pluginConfig = wechatAppLoginPlugin.getPluginConfig();
		setAttr("pluginConfig", pluginConfig);
		render("/admin/plugin/wechatAppLogin/setting.ftl");
	}

	/**
	 * 更新
	 */
	public void update() {
		String loginMethodName = getPara("loginMethodName");
		String oauthKey = getPara("oauthKey");
		String oauthSecret = getPara("oauthSecret");
		String logo = getPara("logo");
		String description = getPara("description");
		Boolean isEnabled = getParaToBoolean("isEnabled", false);
		Integer orders = getParaToInt("orders");
		
		PluginConfig pluginConfig = wechatAppLoginPlugin.getPluginConfig();
		Map<String, String> attributes = new HashMap<String, String>();
		attributes.put(LoginPlugin.LOGIN_METHOD_NAME_ATTRIBUTE_NAME, loginMethodName);
		attributes.put("oauthKey", oauthKey);
		attributes.put("oauthSecret", oauthSecret);
		attributes.put(PaymentPlugin.LOGO_ATTRIBUTE_NAME, logo);
		attributes.put(PaymentPlugin.DESCRIPTION_ATTRIBUTE_NAME, description);
		pluginConfig.setAttributes(JSON.toJSONString(attributes));
		pluginConfig.setIsEnabled(isEnabled);
		pluginConfig.setOrders(orders);
		pluginConfigService.update(pluginConfig);
		addFlashMessage(SUCCESS_MESSAGE);
		redirect("/admin/login_plugin/list.jhtml");
	}

}