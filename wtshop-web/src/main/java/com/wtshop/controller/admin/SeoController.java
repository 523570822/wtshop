package com.wtshop.controller.admin;

import com.jfinal.ext.route.ControllerBind;
import com.wtshop.Pageable;
import com.wtshop.model.Seo;
import com.wtshop.service.SeoService;

/**
 * Controller - SEO设置
 * 
 * 
 */
@ControllerBind(controllerKey = "/admin/seo")
public class SeoController extends BaseController {

	private SeoService seoService = enhance(SeoService.class);

	/**
	 * 编辑
	 */
	public void edit() {
		Long id = getParaToLong("id");
		setAttr("seo", seoService.find(id));
		render("/admin/seo/edit.ftl");
	}

	/**
	 * 更新
	 */
	public void update() {
		Seo seo = getModel(Seo.class);
		seo.remove("type");
		seoService.update(seo);
		addFlashMessage(SUCCESS_MESSAGE);
		redirect("list.jhtml");
	}

	/**
	 * 列表
	 */
	public void list() {
		Pageable pageable = getBean(Pageable.class);
		setAttr("pageable", pageable);
		setAttr("page", seoService.findPage(pageable));
		render("/admin/seo/list.ftl");
	}

}