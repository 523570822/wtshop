package com.wtshop.controller.admin;

import com.jfinal.ext.route.ControllerBind;
import com.wtshop.Pageable;
import com.wtshop.service.RefundsService;

/**
 * Controller - 退款单
 * 
 * 
 */
@ControllerBind(controllerKey = "/admin/refunds")
public class RefundsController extends BaseController {

	private RefundsService refundsService = enhance(RefundsService.class);

	/**
	 * 查看
	 */
	public void view() {
		Long id = getParaToLong("id");
		setAttr("refunds", refundsService.find(id));
		render("/admin/refunds/view.ftl");
	}

	/**
	 * 列表
	 */
	public void list() {
		Pageable pageable = getBean(Pageable.class);
		setAttr("pageable", pageable);
		setAttr("page", refundsService.findPages(pageable));
		render("/admin/refunds/list.ftl");
	}

	/**
	 * 删除
	 */
	public void delete() {
		Long[] ids = getParaValuesToLong("ids");
		refundsService.delete(ids);
		renderJson(SUCCESS_MESSAGE);
	}

}