package com.wtshop.service;

import com.jfinal.aop.Enhancer;
import com.wtshop.dao.CartItemDao;
import com.wtshop.model.CartItem;

import java.util.List;

/**
 * Service - 购物车项
 * 
 * 
 */
public class CartItemService extends BaseService<CartItem> {

	/**
	 * 构造方法
	 */
	public CartItemService() {
		super(CartItem.class);
	}

	private CartItemDao cartItemDao = Enhancer.enhance(CartItemDao.class);

	/**
	 * 购物车id获取商品信息
	 *
	 */
	public List<CartItem> findCartItemList(Long id) {

		return cartItemDao.findCartItemList(id);
	}

}