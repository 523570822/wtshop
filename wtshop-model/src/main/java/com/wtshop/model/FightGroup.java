package com.wtshop.model;

import com.wtshop.model.base.BaseFightGroup;
import com.wtshop.util.ObjectUtils;

/**
 * 团购表
 */
@SuppressWarnings("serial")
public class FightGroup extends BaseFightGroup<FightGroup> {
	public static final FightGroup dao = new FightGroup().dao();
	//  帮抢状态    0/已发布,1/未发布
	public final static int State_Active = 0;
	public final static int State_UnActive = 1;

	private Product product;

	public Product getProduct() {
		if(ObjectUtils.isEmpty(product)){
			product = Product.dao.findById(getProductId());
		}
		return product;
	}

	public void setProduct(Product product) {

		if(ObjectUtils.isEmpty(product)){
			product = Product.dao.findById(getProductId());
		}
		this.product = product;
	}
}
