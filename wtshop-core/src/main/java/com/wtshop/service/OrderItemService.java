package com.wtshop.service;

import com.jfinal.aop.Enhancer;
import com.wtshop.dao.OrderItemDao;
import com.wtshop.model.Order;
import com.wtshop.model.OrderItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Service - 订单项
 * 
 * 
 */
public class OrderItemService extends BaseService<OrderItem> {

	/**
	 * 构造方法
	 */
	public OrderItemService() {
		super(OrderItem.class);
	}

	private OrderItemDao orderItemDao = Enhancer.enhance(OrderItemDao.class);

	/**
	 * 根据订单查找订单项
	 *
	 * @param id
	 *
	 * @return 订单，若不存在则返回null
	 */
	public List<OrderItem> findOrderItemList(Long id) {
		return orderItemDao.findOrderItemList(id);
	}

	/**
	 * 根据订单id删除订单项
	 */
	public Boolean deleteByOrderId(Long orderId){
		return orderItemDao.delete(orderId);
	}


	/**
	 * 根据订单项的id 判断该订单是否已全部评价
	 */
	public List<OrderItem> findIsReview(Long orderItemId){
		List<OrderItem> orderItemList = orderItemDao.findOrderItemList(orderItemId);
		List<OrderItem> orderItemIsReview = new ArrayList<>();
		for(OrderItem orderItem : orderItemList){
			if(!orderItem.getIsReview()){
				orderItemIsReview.add(orderItem);
			}
		}
		return orderItemIsReview;
	}


}