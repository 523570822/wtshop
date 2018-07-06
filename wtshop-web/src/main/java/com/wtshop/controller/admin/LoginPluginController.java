package com.wtshop.controller.admin;

import com.jfinal.ext.route.ControllerBind;
import com.wtshop.service.PluginService;

/**
 * Controller - 登录插件
 * 
 * 
 */
@ControllerBind(controllerKey = "/admin/login_plugin")
public class LoginPluginController extends BaseController {

	private PluginService pluginService = enhance(PluginService.class);

	/**
	 * 列表
	 */
	public void list() {
		setAttr("loginPlugins", pluginService.getLoginPlugins());
		render("/admin/login_plugin/list.ftl");
	}

}