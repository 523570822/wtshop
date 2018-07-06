package com.wtshop.controller.admin;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;
import com.wtshop.Pageable;
import com.wtshop.model.Effect;
import com.wtshop.model.Tag;
import com.wtshop.service.EffectService;
import com.wtshop.service.TagService;

/**
 * Controller - 标签
 * 
 * 
 */
@ControllerBind(controllerKey = "/admin/effect")
public class EffectController extends BaseController {

	private EffectService effectService = enhance(EffectService.class);

	/**
	 * 添加
	 */
	public void add() {
		render("/admin/effect/add.ftl");
	}

	/**
	 * 保存
	 */
	public void save() {
		Effect effect = getModel(Effect.class);

		effectService.save(effect);
		addFlashMessage(SUCCESS_MESSAGE);
		redirect("list.jhtml");
	}

	/**
	 * 编辑
	 */
	public void edit() {
		Long id = getParaToLong("id");
		setAttr("effect", effectService.find(id));
		render("/admin/effect/edit.ftl");
	}

	/**
	 * 更新
	 */
	public void update() {
		Effect effect = getModel(Effect.class);
		
		effectService.update(effect);
		addFlashMessage(SUCCESS_MESSAGE);
		redirect("list.jhtml");
	}

	/**
	 * 列表
	 */
	public void list() {
		Pageable pageable = getBean(Pageable.class);
		setAttr("pageable", pageable);
		setAttr("page", effectService.findPage(pageable));
		render("/admin/effect/list.ftl");
	}

	/**
	 * 删除
	 */
	public void delete() {
		Long[] ids = getParaValuesToLong("ids");
		effectService.delete(ids);
		renderJson(SUCCESS_MESSAGE);
	}

}