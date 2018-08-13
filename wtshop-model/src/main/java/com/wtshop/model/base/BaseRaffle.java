package com.wtshop.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseRaffle<M extends BaseRaffle<M>> extends Model<M> implements IBean {

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

	public void setNickname(String nickname) {
		set("nickname", nickname);
	}

	public String getNickname() {
		return get("nickname");
	}

	public void setPhone(String phone) {
		set("phone", phone);
	}

	public String getPhone() {
		return get("phone");
	}

	public void setActivityId(Long activityId) {
		set("activity_id", activityId);
	}

	public Long getActivityId() {
		return get("activity_id");
	}

	public void setOpporName(String OpporName) {
		set("OpporName", OpporName);
	}

	public String getOpporName() {
		return get("OpporName");
	}

}
