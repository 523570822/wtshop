package com.wtshop.model;

import com.wtshop.model.base.BaseStaffOrgan;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class StaffOrgan extends BaseStaffOrgan<StaffOrgan> {
	public static final StaffOrgan dao = new StaffOrgan().dao();
	public StaffOrgan(){}
	public StaffOrgan(Integer isLeave,Long memberId,String organId){
		this.setIsLeave(isLeave);
		this.setMemberId(memberId);
		this.setOrganId(organId);
	}
}
