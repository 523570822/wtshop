package com.wtshop.service;

import com.jfinal.aop.Enhancer;
import com.wtshop.dao.OrderDao;
import com.wtshop.dao.OrderItemDao;
import com.wtshop.dao.ReturnsItemDao;
import com.wtshop.model.Member;
import com.wtshop.model.Order;
import com.wtshop.model.OrderItem;
import com.wtshop.model.ReturnsItem;

import java.math.BigDecimal;
import java.util.List;

public class ReturnsItemService extends BaseService<ReturnsItem> {

	/**
	 * 构造方法
	 */
	public ReturnsItemService() {
		super(ReturnsItem.class);
	}
	
	private ReturnsItemDao returnsItemDao = Enhancer.enhance(ReturnsItemDao.class);
	private OrderDao orderDao = Enhancer.enhance(OrderDao.class);
	private OrderItemDao orderItemDao = Enhancer.enhance(OrderItemDao.class);

	/**
	 * 根据sn 和 订单项id 查找
	 */
	public List<OrderItem> findOrder(String name, String sn ,Member member){
		return orderItemDao.findOrderByName(name , sn ,member);

	}

	/**
	 * 根据sn 和 订单项id 查找
	 */
	public List<ReturnsItem> findReturnsItems(String name, String sn ,Member member){
		return returnsItemDao.findReturnsItems(name , sn ,member);

	}

	/**
	 * 根据return_id 查找
	 */
	public List<ReturnsItem> findByReturnId(Long retrun_id){
		return returnsItemDao.findByReturnId(retrun_id);
	}

	/**
	 * 根据return_id 查找
	 */
	public ReturnsItem findMoneyById(Long retrun_id){
		return returnsItemDao.findMoneyById(retrun_id);
	}



	/**
	 * 根据退货id 获取退货项信息
	 */
	public List<ReturnsItem> findReturnsItems(Long id){
		return returnsItemDao.findReturnsItems(id);

	}
	
	/**
	 * 根据会员查找退货申请
	 * 
	 * @param member
	 *            用户名(忽略大小写)
	 * @return 退货项，若不存在则返回null
	 */
	public List<ReturnsItem> findReturnsItems(Member member, Integer status) {
		return returnsItemDao.findReturnsItems(member, status);
	}


	/**
	 * 根据sn查找退货项
	 */
	public ReturnsItem findBySn(String sn){
		return returnsItemDao.findBySn(sn);
	}

}
