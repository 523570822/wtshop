package com.wtshop.controller.admin;

import com.jfinal.ext.route.ControllerBind;
import com.wtshop.model.DeliveryCenter;
import com.wtshop.model.DeliveryTemplate;
import com.wtshop.service.DeliveryCenterService;
import com.wtshop.service.DeliveryTemplateService;
import com.wtshop.service.OrderService;

/**
 * Controller - 打印
 * 
 * 
 */
@ControllerBind(controllerKey = "/admin/print")
public class PrintController extends BaseController {

	private OrderService orderService = enhance(OrderService.class);
	private DeliveryTemplateService deliveryTemplateService = enhance(DeliveryTemplateService.class);
	private DeliveryCenterService deliveryCenterService = enhance(DeliveryCenterService.class);

	/**
	 * 订单打印
	 */
	public void order() {
		Long id = getParaToLong("id");
		setAttr("order", orderService.find(id));
		render("/admin/print/order.ftl");
	}

	/**
	 * 购物单打印
	 */
	public void product() {
		Long id = getParaToLong("id");
		setAttr("order", orderService.find(id));
		render("/admin/print/product.ftl");
	}

	/**
	 * 发货单打印
	 */
	public void shipping() {
		Long id = getParaToLong("id");
		setAttr("order", orderService.find(id));
		render("/admin/print/shipping.ftl");
	}

	/**
	 * 快递单打印
	 */
	public void delivery() {
		Long orderId = getParaToLong("orderId");
		Long deliveryTemplateId = getParaToLong("deliveryTemplateId");
		Long deliveryCenterId = getParaToLong("deliveryCenterId");
		
		DeliveryTemplate deliveryTemplate = deliveryTemplateService.find(deliveryTemplateId);
		DeliveryCenter deliveryCenter = deliveryCenterService.find(deliveryCenterId);
		if (deliveryTemplate == null) {
			deliveryTemplate = deliveryTemplateService.findDefault();
		}
		if (deliveryCenter == null) {
			deliveryCenter = deliveryCenterService.findDefault();
		}
		setAttr("deliveryTemplates", deliveryTemplateService.findAll());
		setAttr("deliveryCenters", deliveryCenterService.findAll());
		setAttr("order", orderService.find(orderId));
		setAttr("deliveryTemplate", deliveryTemplate);
		setAttr("deliveryCenter", deliveryCenter);
		render("/admin/print/delivery.ftl");
	}

}