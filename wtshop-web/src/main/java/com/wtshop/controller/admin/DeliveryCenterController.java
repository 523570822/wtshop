package com.wtshop.controller.admin;

import org.apache.commons.lang3.StringUtils;

import com.jfinal.ext.route.ControllerBind;
import com.wtshop.Pageable;
import com.wtshop.model.DeliveryCenter;
import com.wtshop.service.AreaService;
import com.wtshop.service.DeliveryCenterService;

/**
 * Controller - 发货点
 * 
 * 
 */
@ControllerBind(controllerKey = "/admin/delivery_center")
public class DeliveryCenterController extends BaseController {

	private DeliveryCenterService deliveryCenterService = enhance(DeliveryCenterService.class);
	private AreaService areaService = enhance(AreaService.class);

	/**
	 * 添加
	 */
	public void add() {
		render("/admin/delivery_center/add.ftl");
	}

	/**
	 * 保存
	 */
	public void save() {
		DeliveryCenter deliveryCenter = getModel(DeliveryCenter.class);
		Long areaId = getParaToLong("areaId");
		deliveryCenter.setArea(areaService.find(areaId));
		
		Boolean isDefault = StringUtils.equals(getPara("isDefault", ""), "on") ? true : false;
		deliveryCenter.setIsDefault(isDefault);
		
		deliveryCenter.setAreaName(null);
		deliveryCenterService.save(deliveryCenter);
		addFlashMessage(SUCCESS_MESSAGE);
		redirect("list.jhtml");
	}

	/**
	 * 编辑
	 */
	public void edit() {
		Long id = getParaToLong("id");
		setAttr("deliveryCenter", deliveryCenterService.find(id));
		render("/admin/delivery_center/edit.ftl");
	}

	/**
	 * 更新
	 */
	public void update() {
		DeliveryCenter deliveryCenter = getModel(DeliveryCenter.class);
		Long areaId = getParaToLong("areaId");
		deliveryCenter.setArea(areaService.find(areaId));
		
		Boolean isDefault = StringUtils.equals(getPara("isDefault", ""), "on") ? true : false;
		deliveryCenter.setIsDefault(isDefault);
		
		deliveryCenter.remove("area_name");
		deliveryCenterService.update(deliveryCenter);
		addFlashMessage(SUCCESS_MESSAGE);
		redirect("list.jhtml");
	}

	/**
	 * 列表
	 */
	public void list() {
		Pageable pageable = getBean(Pageable.class);
		setAttr("pageable", pageable);
		setAttr("page", deliveryCenterService.findPage(pageable));
		render("/admin/delivery_center/list.ftl");
	}

	/**
	 * 删除
	 */
	public void delete() {
		Long[] ids = getParaValuesToLong("ids");
		deliveryCenterService.delete(ids);
		renderJson(SUCCESS_MESSAGE);
	}

}