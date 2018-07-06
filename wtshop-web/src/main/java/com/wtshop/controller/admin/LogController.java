package com.wtshop.controller.admin;

import com.jfinal.ext.route.ControllerBind;
import com.wtshop.Pageable;
import com.wtshop.service.LogService;

/**
 * Controller - 日志
 * 
 * 
 */
@ControllerBind(controllerKey = "/admin/log")
public class LogController extends BaseController {

	private LogService logService = enhance(LogService.class);

	/**
	 * 列表
	 */
	public void list() {
		Pageable pageable = getBean(Pageable.class);
		setAttr("page", logService.findPage(pageable));
		render("/admin/log/list.ftl");
	}

	/**
	 * 查看
	 */
	public void view() {
		Long id = getParaToLong("id");
		setAttr("log", logService.find(id));
		render("/admin/log/view.ftl");
	}

	/**
	 * 删除
	 */
	public void delete() {
		Long[] ids = getParaValuesToLong("ids");
		logService.delete(ids);
		renderJson(SUCCESS_MESSAGE);
	}

	/**
	 * 清空
	 */
	public void clear() {
		logService.clear();
		renderJson(SUCCESS_MESSAGE);
	}

}