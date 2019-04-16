package com.wtshop.model;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wtshop.entity.SpecificationItem;
import com.wtshop.model.base.BaseReturnsItem;
import com.wtshop.util.ObjectUtils;
import org.apache.commons.collections.CollectionUtils;

/**
 * Model - 退货项
 * 
 * 
 */
public class ReturnsItem extends BaseReturnsItem<ReturnsItem> {
	private static final long serialVersionUID = -8362958048275729703L;
	public static final ReturnsItem dao = new ReturnsItem();
	
	/** 退货单 */
	private Returns returns;
	
	/** 规格 */
	private List<String> specifications = new ArrayList<String>();
	
	/**
	 * 状态
	 */
	public enum Status {

		/** 待审核 0*/
		pendingReview,

		/** 已取消 1*/
		canceled,

		/** 通过 2*/
		pass,

		/** 未通过 3*/
		denied,

		/** 已退货 4*/
		returned,

		/** 已处理 5*/
		handle,

		/** 已完成 6*/
		complete,

		/** 已退款 7*/
		refund
	}
	
	/**
	 * 会员
	 */
	public Member getMember() {
		return Member.dao.findById(getMemberId());
	}
	
	/**
	 * 产品
	 */
	public Product getProduct() {
		return Product.dao.findById(getProductId());
	}
	
	/**
	 * 状态
	 */
	public Status getStatusName() {
		return getStatus() != null ? Status.values()[getStatus()] : null;
	}

	private List<String> imageConverter = new ArrayList<String>();

	/**
	 * 获取规格项
	 *
	 * @return 规格项
	 */
	public List<String>  getImageConverter() {
		if (CollectionUtils.isEmpty(imageConverter)) {
			JSONArray imageJson = JSONArray.parseArray(getImages());
			if (CollectionUtils.isNotEmpty(imageJson)) {
				for (int i = 0; i < imageJson.size(); i++) {

					String[] sb = imageJson.getString(i).split(",");
					for (String sb1:sb
						 ) {
						imageConverter.add(sb1);
					}

				}

			}
		}
		return imageConverter;
	}
	/**
	 * 设置规格项
	 *
	 * @param imageConverter 规格项
	 */
	public void setImageConverter(List<String> imageConverter) {
		this.imageConverter = imageConverter;
	}

	/**
	 * 获取退货单
	 * 
	 * @return 退货单
	 */
	public Returns getReturns() {
		if (ObjectUtils.isEmpty(returns)) {
			returns = Returns.dao.findById(getReturnId());
		}
		return returns;
	}

	/**
	 * 设置退货单
	 * 
	 * @param returns
	 *            退货单
	 */
	public void setReturns(Returns returns) {
		this.returns = returns;
	}
	
	/**
	 * 获取规格
	 * 
	 * @return 规格
	 */
	public List<String> getSpecificationConverter() {
		return specifications;
	}

	/**
	 * 设置规格
	 * 
	 * @param specifications
	 *            规格
	 */
	public void setSpecificationConverter(List<String> specifications) {
		this.specifications = specifications;
	}

}
