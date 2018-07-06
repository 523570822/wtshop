package com.wtshop.controller.admin;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Page;
import com.wtshop.Pageable;
import com.wtshop.model.DeliveryCorp;
import com.wtshop.service.DeliveryCorpService;

/**
 * Controller - 物流公司
 * 
 * 
 */
@ControllerBind(controllerKey = "/admin/delivery_corp")
public class DeliveryCorpController extends BaseController {

	private DeliveryCorpService deliveryCorpService = enhance(DeliveryCorpService.class);

	/**
	 * 添加
	 */
	public void add() {
		render("/admin/delivery_corp/add.ftl");
	}

	/**
	 * 保存
	 */
	public void save() {
		DeliveryCorp deliveryCorp = getModel(DeliveryCorp.class);
		
		deliveryCorp.setShippingMethods(null);
		deliveryCorpService.save(deliveryCorp);
		addFlashMessage(SUCCESS_MESSAGE);
		redirect("list.jhtml");
	}

	/**
	 * 编辑
	 */
	public void edit() {
		Long id = getParaToLong("id");
		setAttr("deliveryCorp", deliveryCorpService.find(id));
		render("/admin/delivery_corp/edit.ftl");
	}

	/**
	 * 更新
	 */
	public void update() {
		DeliveryCorp deliveryCorp = getModel(DeliveryCorp.class);
		
		deliveryCorp.remove("shippingMethods");
		deliveryCorpService.update(deliveryCorp);
		addFlashMessage(SUCCESS_MESSAGE);
		redirect("list.jhtml");
	}

	/**
	 * 列表
	 */
	public void list() {
		Pageable pageable = getBean(Pageable.class);
		Page<DeliveryCorp> search = deliveryCorpService.findPageBySearch(pageable);
		setAttr("page",search);
		setAttr("pageable", pageable);
		render("/admin/delivery_corp/list.ftl");
	}

	/**
	 * 删除
	 */
	public void delete() {
		Long[] ids = getParaValuesToLong("ids");
		deliveryCorpService.delete(ids);
		renderJson(SUCCESS_MESSAGE);
	}

}