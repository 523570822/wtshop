package com.wtshop.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseGroupsCreate<M extends BaseGroupsCreate<M>> extends Model<M> implements IBean {

	public void setId(Long id) {
		set("id", id);
	}

	public Long getId() {
		return get("id");
	}

	public void setUserId(Integer userId) {
		set("user_id", userId);
	}

	public Integer getUserId() {
		return get("user_id");
	}

	public void setGroupId(Integer groupId) {
		set("group_id", groupId);
	}

	public Integer getGroupId() {
		return get("group_id");
	}

	public void setGroupType(Integer groupType) {
		set("group_type", groupType);
	}

	public Integer getGroupType() {
		return get("group_type");
	}

	public void setNum(Integer num) {
		set("num", num);
	}

	public Integer getNum() {
		return get("num");
	}

	public void setJoinNum(Integer joinNum) {
		set("join_num", joinNum);
	}

	public Integer getJoinNum() {
		return get("join_num");
	}

	public void setBeginTime(Integer beginTime) {
		set("begin_time", beginTime);
	}

	public Integer getBeginTime() {
		return get("begin_time");
	}

	public void setEndTime(Integer endTime) {
		set("end_time", endTime);
	}

	public Integer getEndTime() {
		return get("end_time");
	}

	public void setIsValid(Integer isValid) {
		set("is_valid", isValid);
	}

	public Integer getIsValid() {
		return get("is_valid");
	}

	public void setIsSuccess(Integer isSuccess) {
		set("is_success", isSuccess);
	}

	public Integer getIsSuccess() {
		return get("is_success");
	}

}
