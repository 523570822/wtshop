package com.wtshop.service;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.wtshop.model.Permission;
import com.wtshop.model.PermissionRole;
import com.wtshop.model.Role;

/**
 * Service - 角色
 * 
 * 
 */
public class RoleService extends BaseService<Role> {

	/**
	 * 构造方法
	 */
	public RoleService() {
		super(Role.class);
	}
	
	/**
	 * 保存
	 * 
	 */
	public Role save(Role role) {
		super.save(role);
		List<Permission> permissions = role.getPermissions();
		if (CollectionUtils.isNotEmpty(permissions)) {
			for (Permission permission : role.getPermissions()) {
				PermissionRole permissionRole = new PermissionRole();
				permissionRole.setPermissions(permission.getId());
				permissionRole.setRoles(role.getId());
				permissionRole.save();
			}
		}
		return role;
	}

	/**
	 * 更新
	 * 
	 */
	public Role update(Role role) {
		super.update(role);
		List<Permission> permissions = role.getPermissions();
		if (CollectionUtils.isNotEmpty(permissions)) {
			PermissionRole.dao.delete(role.getId());
			for (Permission permission : role.getPermissions()) {
				PermissionRole permissionRole = new PermissionRole();
				permissionRole.setPermissions(permission.getId());
				permissionRole.setRoles(role.getId());
				permissionRole.save();
			}
		}
		return role;
	}

	public void delete(Long id) {
		super.delete(id);
	}

	public void delete(Long... ids) {
		super.delete(ids);
	}

	public void delete(Role role) {
		super.delete(role);
	}

}