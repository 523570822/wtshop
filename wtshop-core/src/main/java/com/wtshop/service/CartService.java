package com.wtshop.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.wtshop.Pageable;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.jfinal.aop.Enhancer;
import com.wtshop.RequestContextHolder;
import com.wtshop.dao.CartDao;
import com.wtshop.dao.CartItemDao;
import com.wtshop.model.Cart;
import com.wtshop.model.CartItem;
import com.wtshop.model.Member;
import com.wtshop.model.Product;
import com.wtshop.util.Assert;
import com.wtshop.util.WebUtils;

/**
 * Service - 购物车
 * 
 * 
 */
public class CartService extends BaseService<Cart> {

	/**
	 * 构造方法
	 */
	public CartService() {
		super(Cart.class);
	}
	
	private CartDao cartDao = Enhancer.enhance(CartDao.class);
	private CartItemDao cartItemDao = Enhancer.enhance(CartItemDao.class);
	private MemberService memberService = Enhancer.enhance(MemberService.class);


	/**
	 * 根据购物车获取满减商品价格
	 */
	public Double findPriceByCartId(Cart cart){

		List<CartItem> cartItems = cartItemDao.findPriceByCartId(cart.getId());
		Double price = 0d;
		for(CartItem cartItem : cartItems){
			price += cartItem.getPrice().multiply(new BigDecimal(cartItem.getQuantity())).doubleValue();
		}
		return price;
	}

	/**
	 * 根据购物车获取满减商品价格
	 */
	public Double findPriceByCartItem(List<CartItem> cartItems){

		Double price = 0d;
		for(CartItem cartItem : cartItems){
			CartItem cartItem1 = cartItemDao.findPriceByCartItem(cartItem.getId());
			if(cartItem1 != null){
				price += cartItem1.getPrice().multiply(new BigDecimal(cartItem1.getQuantity())).doubleValue();
			}
		}
		return price;
	}


	/**
	 * 获取购物商品价格
	 */

	public Double getPrice(Cart cart){
		List<CartItem> cartItems = cart.getCartItems();
		Double price = 0d;
		for(CartItem cartItem : cartItems){
			price += cartItem.getPrice().multiply(new BigDecimal(cartItem.getQuantity())).doubleValue();
		}
		return price;

	}

	/**
	 * 购物车商品列表
	 */
	public Page<Record> findCartGoods(Pageable pageable){
		String searchProperty = pageable.getSearchProperty();
		String searchValue = pageable.getSearchValue();

		String id = null;
		String name = null ;
		if("id".equals(searchProperty)){
			id = searchValue;
		}else if("name".equals(searchProperty)){
			name = searchValue;
		}

		return cartDao.findCartGoods(pageable, id, name);
	}



	/**
	 * 购物车中商品所在会员的详情
	 */
	public Page<Record> findCartGoodsMember(Pageable pageable ,Long productId){
		return cartDao.findCartGoodsMember(pageable ,productId);
	}


	/**
	 * 购物车情况列表
	 */
	public Page<Record>  findCartMember(Pageable pageable){
		String searchProperty = pageable.getSearchProperty();
		String searchValue = pageable.getSearchValue();

		String id = null;
		String name = null ;
		String phone = null;
		if("id".equals(searchProperty)){
			id = searchValue;
		}else if("name".equals(searchProperty)){
			name = searchValue;
		}else if("phone".equals(searchProperty)){
			phone = searchValue;
		}

		return cartDao.findCartMember(pageable, id, name ,phone);
	}

	/**
	 * 购物车详情
	 */
	public Page<Record> findCartMemberMessage(Pageable pageable ,Long memberId){
		return cartDao.findCartMemberMessage(pageable ,memberId);
	}


	/**
	 * 获取当前购物车
	 * 
	 * @return 当前购物车，若不存在则返回null
	 */
	public Cart getCurrent() {
		Cart cart;
		Member member = memberService.getCurrent(true);
		if (member != null) {
			Cart pCart = getRequestCart();
			cart = member.getCart();
			if (pCart != null && cart == null) {
				cart = pCart;
				cart.setMemberId(member.getId());
				cartDao.update(cart);
			}
		} else {
			cart = getRequestCart();
		}
		if (cart != null) {
			Date expire = DateUtils.addSeconds(new Date(), Cart.TIMEOUT);
			if (!DateUtils.isSameDay(cart.getExpire(), expire)) {
				cart.setExpire(expire);
			}
		}
		return cart;
	}
	
	/**
	 * 获取临时产生的购物车
	 * 
	 */
	public Cart getRequestCart() {
		HttpServletRequest request = RequestContextHolder.currentRequestAttributes();
		if (request == null) {
			return null;
		}
		String key = WebUtils.getCookie(request, Cart.KEY_COOKIE_NAME);
		return cartDao.findByKey(key);
	}
	/**
	 * 添加商品至当前购物车
	 * 
	 * @param product
	 *            商品
	 * @param quantity
	 *            数量
	 * @return 当前购物车
	 */
	public Cart add(Product product, int quantity, Boolean buyNow,Long sPecialIds) {
		Assert.notNull(product);
		Assert.state(quantity > 0);

		Cart cart = getCurrent();
		if (cart == null) {
			cart = new Cart();
			Member member = memberService.getCurrent();
			if (member != null && member.getCart() == null) {
				cart.setMemberId(member.getId());
			}
			cart.setCartKey((DigestUtils.md5Hex(UUID.randomUUID() + RandomStringUtils.randomAlphabetic(30))));
			cart.setExpire(DateUtils.addSeconds(new Date(), Cart.TIMEOUT));
			cartDao.save(cart);
		}
		
		if (cart.contains(product,sPecialIds)) {
			if (buyNow) {
				CartItem cartItem = cart.getCartItem(product,sPecialIds);
				cartItem.setQuantity(quantity);
				cartItemDao.update(cartItem);
			} else {
				CartItem cartItem = cart.getCartItem(product,sPecialIds);
				cartItem.add(quantity);
				cartItemDao.update(cartItem);
			}
		} else {
			CartItem cartItem = new CartItem();
			cartItem.setQuantity(quantity);
			cartItem.setProductId(product.getId());
			cartItem.setSpecialId(sPecialIds);
			cartItem.setCartId(cart.getId());
			cartItemDao.save(cartItem);
		}
		return cart;
	}

	/**
	 * 合并临时购物车至会员
	 * 
	 * @param member
	 *            会员
	 * @param cart
	 *            临时购物车
	 */
	public void merge(Member member, Cart cart) {
		if (member == null || cart == null || cart.getMember() != null) {
			return;
		}
		Cart memberCart = member.getCart();
		if (memberCart != null) {
			if (cart.getCartItems() != null) {
				for (CartItem cartItem : cart.getCartItems()) {
					Product product = cartItem.getProduct();
					if (memberCart.contains(product,0l)) {
						CartItem memberCartItem = memberCart.getCartItem(product,0l);
						if (CartItem.MAX_QUANTITY != null && memberCartItem.getQuantity() + cartItem.getQuantity() > CartItem.MAX_QUANTITY) {
							continue;
						}
						memberCartItem.add(cartItem.getQuantity());
						memberCartItem.update();
					} else {
						if (Cart.MAX_CART_ITEM_COUNT != null && memberCart.getCartItems().size() >= Cart.MAX_CART_ITEM_COUNT) {
							continue;
						}
						if (CartItem.MAX_QUANTITY != null && cartItem.getQuantity() > CartItem.MAX_QUANTITY) {
							continue;
						}
						CartItem item = new CartItem();
						item.setQuantity(cartItem.getQuantity());
						item.setProductId(cartItem.getProduct().getId());
						item.setCartId(memberCart.getId());
						cartItemDao.save(item);
						memberCart.getCartItems().add(cartItem);
					}
				}
			}
			delete(cart.getId());
		} else {
			cart.setMemberId(member.getId());
			cart.update();
			member.setCart(cart);
		}
	}

	/**
	 * 清除过期购物车
	 */
	public void evictExpired() {
		while (true) {
			List<Cart> carts = cartDao.findList(true, 100);
			if (CollectionUtils.isNotEmpty(carts)) {
				for (Cart cart : carts) {
					//cartDao.remove(cart);
					delete(cart.getId());
				}
			}
			if (carts.size() < 100) {
				break;
			}
		}
	}

	public void delete(Long id) {
		cartItemDao.delete(id);
		super.delete(id);
	}
	
}