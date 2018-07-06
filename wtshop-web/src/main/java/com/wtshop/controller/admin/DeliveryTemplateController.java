package com.wtshop.controller.admin;

import org.apache.commons.lang3.StringUtils;

import com.jfinal.ext.route.ControllerBind;
import com.wtshop.Pageable;
import com.wtshop.model.DeliveryTemplate;
import com.wtshop.service.DeliveryTemplateService;

/**
 * Controller - 快递单模板
 * 
 * 
 */
@ControllerBind(controllerKey = "/admin/delivery_template")
public class DeliveryTemplateController extends BaseController {

	private DeliveryTemplateService deliveryTemplateService = enhance(DeliveryTemplateService.class);

	/**
	 * 添加
	 */
	public void add() {
		render("/admin/delivery_template/add.ftl");
	}

	/**
	 * 保存
	 */
	public void save() {
		DeliveryTemplate deliveryTemplate = getModel(DeliveryTemplate.class);
		
		Boolean isDefault = StringUtils.equals(getPara("isDefault", ""), "on") ? true : false;
		deliveryTemplate.setIsDefault(isDefault);
		
		deliveryTemplateService.save(deliveryTemplate);
		addFlashMessage(SUCCESS_MESSAGE);
		redirect("list.jhtml");
	}

	/**
	 * 编辑
	 */
	public void edit() {
		Long id = getParaToLong("id");
		setAttr("deliveryTemplate", deliveryTemplateService.find(id));
		render("/admin/delivery_template/edit.ftl");
	}

	/**
	 * 更新
	 */
	public void update() {
		DeliveryTemplate deliveryTemplate = getModel(DeliveryTemplate.class);
		
		Boolean isDefault = StringUtils.equals(getPara("isDefault", ""), "on") ? true : false;
		deliveryTemplate.setIsDefault(isDefault);
		
		deliveryTemplateService.update(deliveryTemplate);
		addFlashMessage(SUCCESS_MESSAGE);
		redirect("list.jhtml");
	}

	/**
	 * 列表
	 */
	public void list() {
		Pageable pageable = getBean(Pageable.class);
		setAttr("page", deliveryTemplateService.findPage(pageable));
		render("/admin/delivery_template/list.ftl");
	}

	/**
	 * 删除
	 */
	public void delete() {
		Long[] ids = getParaValuesToLong("ids");
		deliveryTemplateService.delete(ids);
		renderJson(SUCCESS_MESSAGE);
	}
}