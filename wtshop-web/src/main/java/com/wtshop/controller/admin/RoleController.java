package com.wtshop.controller.admin;

import java.util.List;

import com.jfinal.ext.route.ControllerBind;
import com.wtshop.Message;
import com.wtshop.Pageable;
import com.wtshop.model.Permission;
import com.wtshop.model.Role;
import com.wtshop.service.PermissionService;
import com.wtshop.service.RoleService;

/**
 * Controller - 角色
 * 
 * 
 */
@ControllerBind(controllerKey = "/admin/role")
public class RoleController extends BaseController {

	private RoleService roleService = enhance(RoleService.class);
	private PermissionService permissionService = enhance(PermissionService.class);

	/**
	 * 添加
	 */
	public void add() {
		setAttr("modules", permissionService.getModules());
		setAttr("permissions", permissionService.findAll());
		render("/admin/role/add.ftl");
	}

	/**
	 * 保存
	 */
	public void save() {
		Role role = getModel(Role.class);
		Boolean isEnabled = getParaToBoolean("isEnabled", false);
		role.setIsEnabled(isEnabled);

		Long[] ids = getParaValuesToLong("permissions");
		List<Permission> permissions = permissionService.findList(ids);
		role.setPermissions(permissions);
		role.setIsSystem(false);
		role.setAdmins(null);
		roleService.save(role);
		addFlashMessage(SUCCESS_MESSAGE);
		redirect("list.jhtml");
	}

	/**
	 * 编辑
	 */
	public void edit() {
		Long id = getParaToLong("id");
		
		setAttr("role", roleService.find(id));
		setAttr("modules", permissionService.getModules());
		setAttr("permissions", permissionService.findAll());
		render("/admin/role/edit.ftl");
	}

	/**
	 * 更新
	 */
	public void update() {
		Role role = getModel(Role.class);
		Boolean isEnabled = getParaToBoolean("isEnabled", false);
		role.setIsEnabled(isEnabled);
		
		Role pRole = roleService.find(role.getId());
		if (pRole == null || pRole.getIsSystem()) {
			redirect(ERROR_VIEW);
			return;
		}
		
		Long[] ids = getParaValuesToLong("permissions");
		List<Permission> permissions = permissionService.findList(ids);
		role.setPermissions(permissions);
		role.remove("is_system");
		roleService.update(role);
		addFlashMessage(SUCCESS_MESSAGE);
		redirect("/admin/role/list.jhtml");
	}

	/**
	 * 列表
	 */
	public void list() {
		Pageable pageable = getBean(Pageable.class);
		setAttr("pageable", pageable);
		setAttr("page", roleService.findPage(pageable));
		render("/admin/role/list.ftl");
	}

	/**
	 * 删除
	 */
	public void delete() {
		Long[] ids = getParaValuesToLong("ids");
		if (ids != null) {
			for (Long id : ids) {
				Role role = roleService.find(id);
				if (role != null && (role.getIsSystem() || (role.getAdmins() != null && !role.getAdmins().isEmpty()))) {
					renderJson(Message.error("admin.role.deleteExistNotAllowed", role.getName()));
					return;
				}
			}
			roleService.delete(ids);
		}
		renderJson(SUCCESS_MESSAGE);
	}

}