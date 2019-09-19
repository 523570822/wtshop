package com.wtshop.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseActivityStore<M extends BaseActivityStore<M>> extends Model<M> implements IBean {

	public void setId(Long id) {
		set("id", id);
	}

	public Long getId() {
		return get("id");
	}

	public void setMemberId(Long memberId) {
		set("member_id", memberId);
	}

	public Long getMemberId() {
		return get("member_id");
	}

	public void setName(String name) {
		set("name", name);
	}

	public String getName() {
		return get("name");
	}

	public void setPhone(String phone) {
		set("phone", phone);
	}

	public String getPhone() {
		return get("phone");
	}

	public void setIdCard(String idCard) {
		set("id_card", idCard);
	}

	public String getIdCard() {
		return get("id_card");
	}

	public void setPositiveImg(String positiveImg) {
		set("positiveImg", positiveImg);
	}

	public String getPositiveImg() {
		return get("positiveImg");
	}

	public void setOppositeImg(String oppositeImg) {
		set("oppositeImg", oppositeImg);
	}

	public String getOppositeImg() {
		return get("oppositeImg");
	}

	public void setState(Integer state) {
		set("state", state);
	}

	public Integer getState() {
		return get("state");
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

	public void setModifyDate(java.util.Date modifyDate) {
		set("modify_date", modifyDate);
	}

	public java.util.Date getModifyDate() {
		return get("modify_date");
	}

	public void setOrders(Long orders) {
		set("orders", orders);
	}

	public Long getOrders() {
		return get("orders");
	}

	public void setType(String type) {
		set("type", type);
	}

	public String getType() {
		return get("type");
	}

	public void setMailbox(String mailbox) {
		set("mailbox", mailbox);
	}

	public String getMailbox() {
		return get("mailbox");
	}

	public void setAddress(String address) {
		set("address", address);
	}

	public String getAddress() {
		return get("address");
	}

	public void setStoreDiscount(String storeDiscount) {
		set("store_discount", storeDiscount);
	}

	public String getStoreDiscount() {
		return get("store_discount");
	}

	public void setRecentPhoto(String recentPhoto) {
		set("recent_photo", recentPhoto);
	}

	public String getRecentPhoto() {
		return get("recent_photo");
	}

	public void setBusinessLicense(String businessLicense) {
		set("business_license", businessLicense);
	}

	public String getBusinessLicense() {
		return get("business_license");
	}

	public void setFeedback(String feedback) {
		set("feedback", feedback);
	}

	public String getFeedback() {
		return get("feedback");
	}

	public void setStoreName(String storeName) {
		set("store_name", storeName);
	}

	public String getStoreName() {
		return get("store_name");
	}

}