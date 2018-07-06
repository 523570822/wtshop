package com.wtshop.model;

import com.wtshop.model.base.BaseProductNotify;
import com.wtshop.util.ObjectUtils;

/**
 * Model - 到货通知
 * 
 * 
 */
public class ProductNotify extends BaseProductNotify<ProductNotify> {
	private static final long serialVersionUID = -6220032110888351694L;
	public static final ProductNotify dao = new ProductNotify();
	
	/** 会员 */
	private Member member;

	
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
