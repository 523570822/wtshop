package com.wtshop.model;

import com.wtshop.model.base.BaseOrderLog;
import com.wtshop.util.ObjectUtils;

/**
 * Model - 订单记录
 * 
 * 
 */
public class OrderLog extends BaseOrderLog<OrderLog> {
	private static final long serialVersionUID = 6707539448251320656L;
	public static final OrderLog dao = new OrderLog();
	
	/**
	 * 类型
	 */
	public enum Type {

		/** 订单创建 */
		create,

		/** 订单更新 */
		update,

		/** 订单取消 */
		cancel,

		/** 订单审核 */
		review,

		/** 订单收款 */
		payment,

		/** 订单退款 */
		refunds,

		/** 订单发货 */
		shipping,

		/** 订单退货 */
		returns,

		/** 订单收货 */
		receive,

		/** 订单完成 */
		complete,

		/** 订单失败 */
		fail
	}
	
	/** 订单 */
	private Order order;
	
	/**
	 * 类型
	 */
	public Type getTypeName() {
		return Type.values()[getType()];
	}
	
	/**
	 * 构造方法
	 */
	public OrderLog() {
	}
	
	/**
	 * 构造方法
	 * 
	 * @param type
	 *            类型
	 * @param operator
	 *            操作员
	 */
	public OrderLog(Type type, String operator) {
		this.setType(type.ordinal());
		this.setOperator(operator);
	}

	/**
	 * 构造方法
	 * 
	 * @param type
	 *            类型
	 * @param operator
	 *            操作员
	 * @param content
	 *            内容
	 */
	public OrderLog(Type type, String operator, String content) {
		this.setType(type.ordinal());
		this.setOperator(operator);
		this.setContent(content);
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
	 * 设置操作员
	 * 
	 * @param operator
	 *            操作员
	 */
	public void setOperator(Admin operator) {
		setOperator(operator != null ? operator.getUsername() : null);
	}
}
