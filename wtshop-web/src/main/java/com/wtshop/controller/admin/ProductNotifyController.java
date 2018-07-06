package com.wtshop.controller.admin;

import java.util.List;

import com.jfinal.ext.route.ControllerBind;
import com.wtshop.Message;
import com.wtshop.Pageable;
import com.wtshop.model.ProductNotify;
import com.wtshop.service.ProductNotifyService;

/**
 * Controller - 到货通知
 * 
 * 
 */
@ControllerBind(controllerKey = "/admin/product_notify")
public class ProductNotifyController extends BaseController {

	private ProductNotifyService productNotifyService = enhance(ProductNotifyService.class);

	/**
	 * 发送到货通知
	 */
	public void send() {

	}

	/**
	 * 列表
	 */
	public void list() {
		Boolean isMarketable = getParaToBoolean("isMarketable", false);
		Boolean isOutOfStock = getParaToBoolean("isOutOfStock", false);
		Boolean hasSent = getParaToBoolean("hasSent", false);
		Pageable pageable = getBean(Pageable.class);
		setAttr("isMarketable", isMarketable);
		setAttr("isOutOfStock", isOutOfStock);
		setAttr("hasSent", hasSent);
		setAttr("pageable", pageable);
		setAttr("page", productNotifyService.findPage(null, isMarketable, isOutOfStock, hasSent, pageable));
		render("/admin/product_notify/list.ftl");
	}

	/**
	 * 删除
	 */
	public void delete(Long[] ids) {
		productNotifyService.delete(ids);
		renderJson(SUCCESS_MESSAGE);
	}

}