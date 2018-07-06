package com.wtshop.controller.admin;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;
import com.wtshop.Pageable;
import com.wtshop.model.Navigation;
import com.wtshop.service.ArticleCategoryService;
import com.wtshop.service.NavigationService;
import com.wtshop.service.ProductCategoryService;

/**
 * Controller - 导航
 * 
 * 
 */
@ControllerBind(controllerKey = "/admin/navigation")
public class NavigationController extends BaseController {

	private NavigationService navigationService = enhance(NavigationService.class);
	private ArticleCategoryService articleCategoryService = enhance(ArticleCategoryService.class);
	private ProductCategoryService productCategoryService = enhance(ProductCategoryService.class);

	/**
	 * 添加
	 */
	public void add() {
		setAttr("positions", Navigation.Position.values());
		setAttr("articleCategoryTree", articleCategoryService.findTree());
		setAttr("productCategoryTree", productCategoryService.findTree());
		render("/admin/navigation/add.ftl");
	}

	/**
	 * 保存
	 */
	public void save() {
		Navigation navigation = getModel(Navigation.class);

		Boolean isBlankTarget = getParaToBoolean("isBlankTarget", false);
		navigation.setIsBlankTarget(isBlankTarget);
		
		String positionName = getPara("position", null);
		Navigation.Position position = StrKit.notBlank(positionName) ? Navigation.Position.valueOf(positionName) : null;
		navigation.setPosition(position.ordinal());
		
		navigationService.save(navigation);
		addFlashMessage(SUCCESS_MESSAGE);
		redirect("list.jhtml");
	}

	/**
	 * 编辑
	 */
	public void edit() {
		Long id = getParaToLong("id");
		setAttr("positions", Navigation.Position.values());
		setAttr("articleCategoryTree", articleCategoryService.findTree());
		setAttr("productCategoryTree", productCategoryService.findTree());
		setAttr("navigation", navigationService.find(id));
		render("/admin/navigation/edit.ftl");
	}

	/**
	 * 更新
	 */
	public void update() {
		Navigation navigation = getModel(Navigation.class);
		
		Boolean isBlankTarget = getParaToBoolean("isBlankTarget", false);
		navigation.setIsBlankTarget(isBlankTarget);
		
		String positionName = getPara("position", null);
		Navigation.Position position = StrKit.notBlank(positionName) ? Navigation.Position.valueOf(positionName) : null;
		navigation.setPosition(position.ordinal());
		
		navigationService.update(navigation);
		addFlashMessage(SUCCESS_MESSAGE);
		redirect("list.jhtml");
	}

	/**
	 * 列表
	 */
	public void list() {
		Pageable pageable = getBean(Pageable.class);
		setAttr("pageable", pageable);
		setAttr("topNavigations", navigationService.findList(Navigation.Position.top));
		setAttr("middleNavigations", navigationService.findList(Navigation.Position.middle));
		setAttr("bottomNavigations", navigationService.findList(Navigation.Position.bottom));
		render("/admin/navigation/list.ftl");
	}

	/**
	 * 删除
	 */
	public void delete() {
		Long[] ids = getParaValuesToLong("ids");
		navigationService.delete(ids);
		renderJson(SUCCESS_MESSAGE);
	}

}