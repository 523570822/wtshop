package com.wtshop.controller.admin;

import com.jfinal.ext.route.ControllerBind;
import com.wtshop.TemplateConfig;
import com.wtshop.service.StaticService;
import com.wtshop.util.SystemUtils;

/**
 * Controller - Sitemap
 * 
 * 
 */
@ControllerBind(controllerKey = "/admin/sitemap")
public class SitemapController extends BaseController {

	private StaticService staticService = enhance(StaticService.class);

	/**
	 * 生成Sitemap
	 */
	public void generate() {
		TemplateConfig templateConfig = SystemUtils.getTemplateConfig("sitemapIndex");
		setAttr("sitemapIndexPath", templateConfig.getRealStaticPath());
		render("/admin/sitemap/generate.ftl");
	}

	/**
	 * 生成Sitemap
	 */
	public void generate1() {
		staticService.generateSitemap();
		addFlashMessage(SUCCESS_MESSAGE);
		redirect("generate.jhtml");
	}

}