package com.wtshop.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseFreeUse<M extends BaseFreeUse<M>> extends Model<M> implements IBean {

	public void setId(Long id) {
		set("id", id);
	}

	public Long getId() {
		return get("id");
	}

	public void setTitle(String title) {
		set("title", title);
	}

	public String getTitle() {
		return get("title");
	}

	public void setIntroduction(String introduction) {
		set("introduction", introduction);
	}

	public String getIntroduction() {
		return get("introduction");
	}

	public void setJpg(String jpg) {
		set("jpg", jpg);
	}

	public String getJpg() {
		return get("jpg");
	}

	public void setEndDate(java.util.Date endDate) {
		set("end_date", endDate);
	}

	public java.util.Date getEndDate() {
		return get("end_date");
	}

	public void setProductId(Long productId) {
		set("product_id", productId);
	}

	public Long getProductId() {
		return get("product_id");
	}

	public void setNum(Integer num) {
		set("num", num);
	}

	public Integer getNum() {
		return get("num");
	}

	public void setApplyNum(Integer applyNum) {
		set("apply_num", applyNum);
	}

	public Integer getApplyNum() {
		return get("apply_num");
	}

	public void setModifyDate(java.util.Date modifyDate) {
		set("modify_date", modifyDate);
	}

	public java.util.Date getModifyDate() {
		return get("modify_date");
	}

	public void setCreateDate(java.util.Date createDate) {
		set("create_date", createDate);
	}

	public java.util.Date getCreateDate() {
		return get("create_date");
	}

	public void setVersion(Long version) {
		set("version", version);
	}

	public Long getVersion() {
		return get("version");
	}

	public void setOrders(Integer orders) {
		set("orders", orders);
	}

	public Integer getOrders() {
		return get("orders");
	}

}
