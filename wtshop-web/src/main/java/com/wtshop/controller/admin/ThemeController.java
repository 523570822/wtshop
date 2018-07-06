package com.wtshop.controller.admin;

import org.apache.commons.io.FilenameUtils;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.upload.UploadFile;
import com.wtshop.Message;
import com.wtshop.Setting;
import com.wtshop.Theme;
import com.wtshop.service.CacheService;
import com.wtshop.service.ThemeService;
import com.wtshop.util.SystemUtils;

/**
 * Controller - 主题
 * 
 * 
 */
@ControllerBind(controllerKey = "/admin/theme")
public class ThemeController extends BaseController {

	private ThemeService themeService = new ThemeService();
	private CacheService cacheService = new CacheService();

	/**
	 * 设置
	 */
	public void setting() {
		setAttr("themes", themeService.getAll());
		render("/admin/theme/setting.ftl");
	}

	/**
	 * 设置
	 */
	public void setting1() {
		UploadFile themeFile = getFile("themeFile");
		String id = getPara("id");
		
		if (themeFile != null && themeFile.getFile().length() <= 0) {
			if (!FilenameUtils.isExtension(themeFile.getOriginalFileName(), "zip")) {
				addFlashMessage(Message.error("admin.upload.invalid"));
				redirect("setting.jhtml");
			}
			if (!themeService.upload(themeFile)) {
				addFlashMessage(Message.error("admin.theme.uploadInvalid"));
				redirect("setting.jhtml");
			}
		}
		Theme theme = themeService.get(id);
		if (theme == null) {
			redirect(ERROR_VIEW);
			return;
		}
		Setting setting = SystemUtils.getSetting();
		setting.setTheme(theme.getName());
		SystemUtils.setSetting(setting);
		cacheService.clear();
		addFlashMessage(SUCCESS_MESSAGE);
		redirect("setting.jhtml");
	}

}