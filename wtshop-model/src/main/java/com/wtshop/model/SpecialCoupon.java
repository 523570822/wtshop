package com.wtshop.model;

import com.wtshop.model.base.BaseSpecialCoupon;
import com.wtshop.util.ObjectUtils;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class SpecialCoupon extends BaseSpecialCoupon<SpecialCoupon> {
	public static final SpecialCoupon dao = new SpecialCoupon().dao();
	public Member member;
	/**
	 * 获取会员
	 *
	 * @return 会员
	 */
	public Member getMember() {
		if (ObjectUtils.isEmpty(member)) {
			member = Member.dao.findById(getMemberId());
		}
		return member;
	}

	/**
	 * 设置会员
	 *
	 * @param member
	 *            会员
	 */
	public void setMember(Member member) {
		this.member = member;
	}

}
