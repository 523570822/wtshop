package com.wtshop.controller.admin;

import com.jfinal.ext.route.ControllerBind;
import com.wtshop.service.PluginService;

/**
 * Controller - 存储插件
 * 
 * 
 */
@ControllerBind(controllerKey = "/admin/storage_plugin")
public class StoragePluginController extends BaseController {

	private PluginService pluginService = enhance(PluginService.class);

	/**
	 * 列表
	 */
	public void list() {
		setAttr("storagePlugins", pluginService.getStoragePlugins());
		render("/admin/storage_plugin/list.ftl");
	}

}