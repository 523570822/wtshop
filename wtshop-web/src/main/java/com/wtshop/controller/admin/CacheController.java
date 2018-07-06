package com.wtshop.controller.admin;

import com.jfinal.ext.route.ControllerBind;
import com.wtshop.service.CacheService;

/**
 * Controller - 缓存
 * 
 * 
 */
@ControllerBind(controllerKey = "/admin/cache")
public class CacheController extends BaseController {

	private CacheService cacheService = new CacheService();

	/**
	 * 缓存查看
	 */
	public void clear() {
		Long totalMemory = null;
		Long maxMemory = null;
		Long freeMemory = null;
		try {
			totalMemory = Runtime.getRuntime().totalMemory() / 1024 / 1024;
			maxMemory = Runtime.getRuntime().maxMemory() / 1024 / 1024;
			freeMemory = Runtime.getRuntime().freeMemory() / 1024 / 1024;
		} catch (Exception e) {
		}
		setAttr("totalMemory", totalMemory);
		setAttr("maxMemory", maxMemory);
		setAttr("freeMemory", freeMemory);
		setAttr("cacheSize", cacheService.getCacheSize());
		setAttr("diskStorePath", cacheService.getDiskStorePath());
		render("/admin/cache/clear.ftl");
	}

	/**
	 * 清除缓存
	 */
	public void clear1() {
		cacheService.clear();
		addFlashMessage(SUCCESS_MESSAGE);
		redirect("clear.jhtml");
	}
	
	//@RequestMapping(value = "/clear", method = RequestMethod.POST)
//	public String clear(RedirectAttributes redirectAttributes) {
//		cacheService.clear();
//		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
//		return "redirect:clear.jhtml";
//	}

}