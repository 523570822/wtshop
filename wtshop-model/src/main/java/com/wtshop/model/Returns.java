package com.wtshop.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.wtshop.model.base.BaseReturns;
import com.wtshop.util.ObjectUtils;

/**
 * Model - 退货单
 * 
 * 
 */
public class Returns extends BaseReturns<Returns> {
	private static final long serialVersionUID = -6137699225065424914L;
	public static final Returns dao = new Returns();

	/**
	 * 状态
	 */
	public enum Type {

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
	 * 状态
	 */
	public Type getTypeName() {
		return getType() != null ? Type.values()[getType()] : null;
	}

	
	/** 订单 */
	private Order order;

	private Area area;

	/** 退货项 */
	private List<ReturnsItem> returnsItems = new ArrayList<ReturnsItem>();
	/**
	 * 获取地区
	 *
	 * @return 地区
	 */
	public Area getArea() {
		if (ObjectUtils.isEmpty(area)) {
			area = Area.dao.findById(getAreaId());
		}
		return area;
	}

	/**
	 * 设置地区
	 *
	 * @param area
	 *            地区
	 */
	public void setArea(Area area) {
		this.area = area;
	}

	
	/**
	 * 获取订单
	 * 
	 * @return 订单
	 */
	public Order getOrder() {
		if (ObjectUtils.isEmpty(order)) {
			order = Order.dao.findById(getOrderId());
		}
		return order;
	}

	/**
	 * 设置订单
	 * 
	 * @param order
	 *            订单
	 */
	public void setOrder(Order order) {
		this.order = order;
	}

	/**
	 * 获取退货项
	 * 
	 * @return 退货项
	 */
	public List<ReturnsItem> getReturnsItems() {
		if (CollectionUtils.isEmpty(returnsItems)) {
			String sql = "SELECT * FROM returns_item WHERE return_id = ?";
			returnsItems = ReturnsItem.dao.find(sql, getId());
		}
		return returnsItems;
	}

	/**
	 * 设置退货项
	 * 
	 * @param returnsItems
	 *            退货项
	 */
	public void setReturnsItems(List<ReturnsItem> returnsItems) {
		this.returnsItems = returnsItems;
	}

	/**
	 * 获取数量
	 * 
	 * @return 数量
	 */
	public int getQuantity() {
		int quantity = 0;
		if (getReturnsItems() != null) {
			for (ReturnsItem returnsItem : getReturnsItems()) {
				if (returnsItem != null && returnsItem.getQuantity() != null) {
					quantity += returnsItem.getQuantity();
				}
			}
		}
		return quantity;
	}

	/**
	 * 设置配送方式
	 * 
	 * @param shippingMethod
	 *            配送方式
	 */
	public void setShippingMethod(ShippingMethod shippingMethod) {
		setShippingMethod(shippingMethod != null ? shippingMethod.getName() : null);
	}

	/**
	 * 设置物流公司
	 * 
	 * @param deliveryCorp
	 *            物流公司
	 */
	public void setDeliveryCorp(DeliveryCorp deliveryCorp) {
		setDeliveryCorp(deliveryCorp != null ? deliveryCorp.getName() : null);
	}

	/**
	 * 设置地区
	 *
	 * @param area
	 *            地区
	 */
	public void setAreaName(Area area) {
		setAreaName(area != null ? area.getFullName() : null);
	}

	/**
	 * 设置操作员
	 * 
	 * @param operator
	 *            操作员
	 */
	public void setOperator(Admin operator) {
		setOperator(operator != null ? operator.getUsername() : null);
	}
	
	/**
	 * 判断是否为新建对象
	 * 
	 * @return 是否为新建对象
	 */
	public boolean isNew() {
		return getId() == null;
	}
}
