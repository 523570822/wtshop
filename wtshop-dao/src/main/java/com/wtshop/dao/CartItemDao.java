package com.wtshop.dao;

import com.jfinal.plugin.activerecord.Db;
import com.wtshop.model.CartItem;

import java.util.List;

/**
 * Dao - 购物车项
 * 
 * 
 */
public class CartItemDao extends BaseDao<CartItem> {
	
	/**
	 * 构造方法
	 */
	public CartItemDao() {
		super(CartItem.class);
	}

	/**
	 * 获取满减商品的总价格
	 */
	public List<CartItem> findPriceByCartId(Long cartId){
		if(cartId == null){
			return null;
		}else {
			String sql = " SELECT i.* from cart_item i LEFT JOIN product p ON i.product_id = p.id LEFT JOIN goods_promotion g ON p.goods_id = g.goods WHERE g.goods is not null and g.promotions = 5 and i.cart_id = " +cartId;
			return modelManager.find(sql);
		}
	}

	/**
	 * 获取满减商品的总价格
	 */
	public CartItem findPriceByCartItem(Long cartId){
		if(cartId == null){
			return null;
		}else {
			String sql = " SELECT i.* from cart_item i LEFT JOIN product p ON i.product_id = p.id LEFT JOIN goods_promotion g ON p.goods_id = g.goods WHERE g.goods is not null and g.promotions = 5 and i.id = " +cartId;
			return modelManager.findFirst(sql);
		}
	}
	
	/**
	 * 根据购物车Id删除
	 * @param cartId
	 * @return
	 */
	public boolean delete(Long cartId) {
		return Db.deleteById("cart_item", "cart_id", cartId);
	}

	/**
	 * 根据购物项Id删除
	 * @param cartItemId
	 * @return
	 */
	public boolean deleteByItemId(Long cartItemId) {
		return Db.deleteById("cart_item", "id", cartItemId);
	}

	/**
	 * 根据用户id查找购物项
	 *
	 * @param id
	 *            购物项id
	 * @return 购物车，若不存在则返回null
	 */
	public List<CartItem> findCartItemList(Long id) {
		String sql = "SELECT c.id item_id ,p.id product_id, g.id goodsId ,g.area_id, g.image,g.`name`,g.caption,g.price,g.market_price ,c.quantity FROM cart_item c left join product p on c.product_id = p.id left join goods g on p.goods_id = g.id WHERE cart_id = " + id ;
		return modelManager.find(sql);

	}

//	/**
//	 * 根据用户id查找购物项
//	 *
//	 * @param id
//	 *            购物项id
//	 * @return 购物车，若不存在则返回null
//	 */
//	public List<CartItem> findCartItemList(Long id) {
//
//		String sql = "SELECT * FROM cart_item  WHERE cart_id = " + id ;
//		return modelManager.find(sql);
//
//	}


}