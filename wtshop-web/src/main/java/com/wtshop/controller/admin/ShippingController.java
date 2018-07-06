package com.wtshop.controller.admin;

import com.jfinal.ext.route.ControllerBind;
import com.wtshop.Pageable;
import com.wtshop.model.Shipping;
import com.wtshop.service.ShippingService;

/**
 * Controller - 发货单
 * 
 * 
 */
@ControllerBind(controllerKey = "/admin/shipping")
public class ShippingController extends BaseController {

	private ShippingService shippingService = enhance(ShippingService.class);

	/**
	 * 查看
	 */
	public void view() {
		Long id = getParaToLong("id");
		setAttr("shipping", shippingService.find(id));
		render("/admin/shipping/view.ftl");
	}

	/**
	 * 列表
	 */
	public void list() {
		Pageable pageable = getBean(Pageable.class);
		setAttr("pageable", pageable);
		setAttr("page", shippingService.findPage(pageable));
		render("/admin/shipping/list.ftl");
	}


	public void edit(){
		Long id1 = getParaToLong("id");
		String  trackingNo1 = getPara("trackingNo");
		Shipping shipping = shippingService.find(id1);
		shipping.setTrackingNo(trackingNo1);
		shippingService.update(shipping);

		renderJson(SUCCESS_MESSAGE);
	}
	/**
	 * 删除
	 */
	public void delete() {
		Long[] ids = getParaValuesToLong("ids");
		shippingService.delete(ids);
		renderJson(SUCCESS_MESSAGE);
	}

}