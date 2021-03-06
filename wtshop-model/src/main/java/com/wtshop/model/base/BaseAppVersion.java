package com.wtshop.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseAppVersion<M extends BaseAppVersion<M>> extends Model<M> implements IBean {

	public void setId(Long id) {
		set("id", id);
	}

	public Long getId() {
		return get("id");
	}

	public void setCreateDate(java.util.Date createDate) {
		set("create_date", createDate);
	}

	public java.util.Date getCreateDate() {
		return get("create_date");
	}

	public void setModifyDate(java.util.Date modifyDate) {
		set("modify_date", modifyDate);
	}

	public java.util.Date getModifyDate() {
		return get("modify_date");
	}

	public void setVersion(Long version) {
		set("version", version);
	}

	public Long getVersion() {
		return get("version");
	}

	public void setTitle(String title) {
		set("title", title);
	}

	public String getTitle() {
		return get("title");
	}

	public void setAppVersion(Double appVersion) {
		set("app_version", appVersion);
	}

	public Double getAppVersion() {
		return get("app_version");
	}

	public void setDesc(String desc) {
		set("desc", desc);
	}

	public String getDesc() {
		return get("desc");
	}

	public void setUrl(String url) {
		set("url", url);
	}

	public String getUrl() {
		return get("url");
	}

	public void setIsForceUpdate(Boolean isForceUpdate) {
		set("is_force_update", isForceUpdate);
	}

	public Boolean getIsForceUpdate() {
		return get("is_force_update");
	}

	public void setAppFlat(String appFlat) {
		set("app_flat", appFlat);
	}

	public String getAppFlat() {
		return get("app_flat");
	}

}
