package com.wtshop.controller.admin;

import com.jfinal.ext.route.ControllerBind;
import com.wtshop.Pageable;
import com.wtshop.service.PaymentService;

/**
 * Controller - 收款单
 * 
 * 
 */
@ControllerBind(controllerKey = "/admin/payment")
public class PaymentController extends BaseController {

	private PaymentService paymentService = enhance(PaymentService.class);

	/**
	 * 查看
	 */
	public void view() {
		Long id = getParaToLong("id");
		setAttr("payment", paymentService.find(id));
		render("/admin/payment/view.ftl");
	}

	/**
	 * 列表
	 */
	public void list() {
		Pageable pageable = getBean(Pageable.class);
		setAttr("pageable" ,pageable);
		setAttr("page", paymentService.findPage(pageable));
		render("/admin/payment/list.ftl");
	}

	/**
	 * 删除
	 */
	public void delete() {
		Long[] ids = getParaValuesToLong("ids");
		paymentService.delete(ids);
		renderJson(SUCCESS_MESSAGE);
	}

}