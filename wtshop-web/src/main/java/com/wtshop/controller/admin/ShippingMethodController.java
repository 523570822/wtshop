package com.wtshop.controller.admin;

import java.util.ArrayList;

import com.jfinal.ext.route.ControllerBind;
import com.wtshop.Message;
import com.wtshop.Pageable;
import com.wtshop.model.PaymentMethod;
import com.wtshop.model.ShippingMethod;
import com.wtshop.service.DeliveryCorpService;
import com.wtshop.service.PaymentMethodService;
import com.wtshop.service.ShippingMethodService;

/**
 * Controller - 配送方式
 * 
 * 
 */
@ControllerBind(controllerKey = "/admin/shipping_method")
public class ShippingMethodController extends BaseController {

	private ShippingMethodService shippingMethodService = enhance(ShippingMethodService.class);
	private PaymentMethodService paymentMethodService = enhance(PaymentMethodService.class);
	private DeliveryCorpService deliveryCorpService = enhance(DeliveryCorpService.class);

	/**
	 * 添加
	 */
	public void add() {
		setAttr("deliveryCorps", deliveryCorpService.findAll());
		setAttr("paymentMethods", paymentMethodService.findAll());
		render("/admin/shipping_method/add.ftl");
	}

	/**
	 * 保存
	 */
	public void save() {
		ShippingMethod shippingMethod = getModel(ShippingMethod.class);
		Long defaultDeliveryCorpId = getParaToLong("defaultDeliveryCorpId");
		Long[] paymentMethodIds = getParaValuesToLong("paymentMethodIds");
		
		shippingMethod.setDefaultDeliveryCorpId(deliveryCorpService.find(defaultDeliveryCorpId).getId());
		shippingMethod.setPaymentMethods(new ArrayList<PaymentMethod>(paymentMethodService.findList(paymentMethodIds)));
		
		shippingMethod.setFreightConfigs(null);
		shippingMethod.setOrder(null);
		shippingMethodService.save(shippingMethod);
		addFlashMessage(SUCCESS_MESSAGE);
		redirect("list.jhtml");
	}

	/**
	 * 编辑
	 */
	public void edit() {
		Long id = getParaToLong("id");
		setAttr("deliveryCorps", deliveryCorpService.findAll());
		setAttr("paymentMethods", paymentMethodService.findAll());
		setAttr("shippingMethod", shippingMethodService.find(id));
		render("/admin/shipping_method/edit.ftl");
	}

	/**
	 * 更新
	 */
	public void update() {
		ShippingMethod shippingMethod = getModel(ShippingMethod.class);
		Long defaultDeliveryCorpId = getParaToLong("defaultDeliveryCorpId");
		Long[] paymentMethodIds = getParaValuesToLong("paymentMethodIds");
		
		shippingMethod.setDefaultDeliveryCorpId(deliveryCorpService.find(defaultDeliveryCorpId).getId());
		shippingMethod.setPaymentMethods(new ArrayList<PaymentMethod>(paymentMethodService.findList(paymentMethodIds)));
		
		shippingMethodService.update(shippingMethod);
		addFlashMessage(SUCCESS_MESSAGE);
		redirect("list.jhtml");
	}

	/**
	 * 列表
	 */
	public void list() {
		Pageable pageable = getBean(Pageable.class);
		setAttr("pageable", pageable);
		setAttr("page", shippingMethodService.findPage(pageable));
		render("/admin/shipping_method/list.ftl");
	}

	/**
	 * 删除
	 */
	public void delete() {
		Long[] ids = getParaValuesToLong("ids");
		if (ids.length >= shippingMethodService.count()) {
			renderJson(Message.error("admin.common.deleteAllNotAllowed"));
			return;
		}
		shippingMethodService.delete(ids);
		renderJson(SUCCESS_MESSAGE);
	}

}