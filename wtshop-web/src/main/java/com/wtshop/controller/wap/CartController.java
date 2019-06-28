package com.wtshop.controller.wap;

import java.util.HashMap;
import java.util.Map;

import com.wtshop.Message;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.i18n.I18n;
import com.jfinal.i18n.Res;
import com.wtshop.interceptor.WapInterceptor;
import com.wtshop.model.Cart;
import com.wtshop.model.CartItem;
import com.wtshop.model.Goods;
import com.wtshop.model.Member;
import com.wtshop.model.Product;
import com.wtshop.service.CartItemService;
import com.wtshop.service.CartService;
import com.wtshop.service.MemberService;
import com.wtshop.service.ProductService;
import com.wtshop.util.WebUtils;

@ControllerBind(controllerKey = "/wap/cart")
@Before(WapInterceptor.class)
public class CartController extends BaseController {

	private CartService cartService = enhance(CartService.class);
	private MemberService memberService = enhance(MemberService.class);
	private ProductService productService = enhance(ProductService.class);
	private CartItemService cartItemService = enhance(CartItemService.class);
	private Res resZh = I18n.use();
	
	/**
	 * 添加
	 */
	public void add() {
		Long productId = getParaToLong("productId");
		Integer quantity = getParaToInt("quantity");
		Boolean buyNow = getParaToBoolean("buy_now");
		
		Map<String, String> map = new HashMap<String, String>();
		if (quantity == null || quantity < 1) {
			map.put(STATUS, ERROR);
			map.put(MESSAGE, "数量不能为空!");
			renderJson(map);
			return;
		}
		Product product = productService.find(productId);
		if (product == null) {
			map.put(STATUS, ERROR);
			map.put(MESSAGE, resZh.format("shop.cart.productNotExist"));
			renderJson(map);
			return;
		}
		if (!Goods.Type.general.equals(product.getType())) {
			map.put(STATUS, ERROR);
			map.put(MESSAGE, resZh.format("shop.cart.productNotForSale"));
			renderJson(map);
			return;
		}
		if (!product.getIsMarketable()) {
			map.put(STATUS, ERROR);
			map.put(MESSAGE, resZh.format("shop.cart.productNotMarketable"));
			renderJson(map);
			return;
		}

		Cart cart = cartService.getCurrent();
		if (cart != null) {
			if (cart.contains(product,0l)) {
				CartItem cartItem = cart.getCartItem(product,0l);
				if (CartItem.MAX_QUANTITY != null && cartItem.getQuantity() + quantity > CartItem.MAX_QUANTITY) {
					map.put(STATUS, ERROR);
					map.put(MESSAGE, resZh.format("shop.cart.addQuantityNotAllowed", CartItem.MAX_QUANTITY));
					renderJson(map);
					return;
				}
				if (cartItem.getQuantity() + quantity > product.getAvailableStock()) {
					map.put(STATUS, ERROR);
					map.put(MESSAGE, resZh.format("shop.cart.productLowStock"));
					renderJson(map);
					return;
				}
			} else {
				if (Cart.MAX_CART_ITEM_COUNT != null && cart.getCartItems().size() >= Cart.MAX_CART_ITEM_COUNT) {
					map.put(STATUS, ERROR);
					map.put(MESSAGE, resZh.format("shop.cart.addCartItemCountNotAllowed", Cart.MAX_CART_ITEM_COUNT));
					renderJson(map);
					return;
				}
				if (CartItem.MAX_QUANTITY != null && quantity > CartItem.MAX_QUANTITY) {
					map.put(STATUS, ERROR);
					map.put(MESSAGE, resZh.format("shop.cart.addQuantityNotAllowed", CartItem.MAX_QUANTITY));
					renderJson(map);
					return;
				}
				if (quantity > product.getAvailableStock()) {
					map.put(STATUS, ERROR);
					map.put(MESSAGE, resZh.format("shop.cart.productLowStock"));
					renderJson(map);
					return;
				}
			}
		} else {
			if (CartItem.MAX_QUANTITY != null && quantity > CartItem.MAX_QUANTITY) {
				map.put(STATUS, ERROR);
				map.put(MESSAGE, resZh.format("shop.cart.addQuantityNotAllowed", CartItem.MAX_QUANTITY));
				renderJson(map);
				return;
			}
			if (quantity > product.getAvailableStock()) {
				map.put(STATUS, ERROR);
				map.put(MESSAGE, resZh.format("shop.cart.productLowStock"));
				renderJson(map);
				return;
			}
		}
		cart = cartService.add(product, quantity, buyNow,0l);

		Member member = memberService.getCurrent();
		if (member == null) {
			WebUtils.addCookie(getRequest(), getResponse(), Cart.KEY_COOKIE_NAME, cart.getCartKey(), Cart.TIMEOUT);
		}
		map.put(STATUS, SUCCESS);
		renderJson(map);
	}
	
	/**
	 * 再次购买
	 */
	public void again() {
		String params = getPara("params");
		JSONArray arr = JSONArray.parseArray(params);
		
		Map<String, String> map = new HashMap<String, String>();
		// 定义组数存放产品id
		String ids = "";
		for (int i = 0; i < arr.size(); i++) {
			JSONObject obj = arr.getJSONObject(i);
			Long productId = obj.getLong("productId");
			Integer quantity = obj.getInteger("quantity");
			
			if (quantity == null || quantity < 1) {
				map.put(STATUS, ERROR);
				map.put(MESSAGE, "数量不能为空!");
				renderJson(map);
				return;
			}
			Product product = productService.find(productId);
			if (product == null) {
				map.put(STATUS, ERROR);
				map.put(MESSAGE, resZh.format("shop.cart.productNotExist"));
				renderJson(map);
				return;
			}
			if (!Goods.Type.general.equals(product.getType())) {
				map.put(STATUS, ERROR);
				map.put(MESSAGE, resZh.format("shop.cart.productNotForSale"));
				renderJson(map);
				return;
			}
			if (!product.getIsMarketable()) {
				map.put(STATUS, ERROR);
				map.put(MESSAGE, resZh.format("shop.cart.productNotMarketable"));
				renderJson(map);
				return;
			}
			
			Cart cart = cartService.getCurrent();
			if (cart != null) {
				if (cart.contains(product,0l)) {
					CartItem cartItem = cart.getCartItem(product,0l);
					if (CartItem.MAX_QUANTITY != null && cartItem.getQuantity() + quantity > CartItem.MAX_QUANTITY) {
						map.put(STATUS, ERROR);
						map.put(MESSAGE, resZh.format("shop.cart.addQuantityNotAllowed", CartItem.MAX_QUANTITY));
						renderJson(map);
						return;
					}
					if (cartItem.getQuantity() + quantity > product.getAvailableStock()) {
						map.put(STATUS, ERROR);
						map.put(MESSAGE, resZh.format("shop.cart.productLowStock"));
						renderJson(map);
						return;
					}
				} else {
					if (Cart.MAX_CART_ITEM_COUNT != null && cart.getCartItems().size() >= Cart.MAX_CART_ITEM_COUNT) {
						map.put(STATUS, ERROR);
						map.put(MESSAGE, resZh.format("shop.cart.addCartItemCountNotAllowed", Cart.MAX_CART_ITEM_COUNT));
						renderJson(map);
						return;
					}
					if (CartItem.MAX_QUANTITY != null && quantity > CartItem.MAX_QUANTITY) {
						map.put(STATUS, ERROR);
						map.put(MESSAGE, resZh.format("shop.cart.addQuantityNotAllowed", CartItem.MAX_QUANTITY));
						renderJson(map);
						return;
					}
					if (quantity > product.getAvailableStock()) {
						map.put(STATUS, ERROR);
						map.put(MESSAGE, resZh.format("shop.cart.productLowStock"));
						renderJson(map);
						return;
					}
				}
			} else {
				if (CartItem.MAX_QUANTITY != null && quantity > CartItem.MAX_QUANTITY) {
					map.put(STATUS, ERROR);
					map.put(MESSAGE, resZh.format("shop.cart.addQuantityNotAllowed", CartItem.MAX_QUANTITY));
					renderJson(map);
					return;
				}
				if (quantity > product.getAvailableStock()) {
					map.put(STATUS, ERROR);
					map.put(MESSAGE, resZh.format("shop.cart.productLowStock"));
					renderJson(map);
					return;
				}
			}
			ids += productId + ",";
			cart = cartService.add(product, quantity, false,0l);
		}
		map.put("referer", "/wap/order/checkout.jhtml?skuids=" + StringUtils.substringBeforeLast(ids, ","));
		map.put(STATUS, SUCCESS);
		renderJson(map);
	}
	
	/**
	 * 列表
	 */
	public void list() {
		setAttr("title" , "购物车信息");
		setAttr("cart", cartService.getCurrent());
		render("/wap/cart/list.ftl");
	}
	
	
	/**
	 * 删除
	 */
	public void delete() {
		Long id = getParaToLong("id");
		Map<String, Object> data = new HashMap<String, Object>();
		Cart cart = cartService.getCurrent();
		if (cart == null || cart.isEmpty()) {
			data.put("message", Message.error("shop.cart.notEmpty"));
			renderJson(data);
			return;
		}
		CartItem cartItem = cartItemService.find(id);
		if (!cart.contains(cartItem)) {
			data.put("message", Message.error("shop.cart.cartItemNotExist"));
			renderJson(data);
			return;
		}
		cartItemService.delete(cartItem);
		cart.getCartItems().remove(cartItem);

		data.put(STATUS, SUCCESS);
		renderJson(data);
	}
	
	/**
	 * 获取购物车总数
	 * 
	 */
	public void getCartCount() {
		Map<String, Integer> map = new HashMap<String, Integer>();
		Cart cart = cartService.getCurrent();
		map.put("sku_counts", cart != null ? cart.getQuantity() : 0);
		renderJson(map);
	}
	
	/**
	 * 更新购物车数量
	 * 改变数量 加减按钮
	 * 
	 */
	public void setNums() {
		Long productId = getParaToLong("productId", null);
		Integer quantity = getParaToInt("quantity", null);
		
		Map<String, String> map = new HashMap<String, String>();
		if (quantity == null) {
			map.put(STATUS, ERROR);
			map.put(MESSAGE, "数量不能为空!");
			renderJson(map);
			return;
		}
		
		Product product = productService.find(productId);
		if (product == null) {
			map.put(STATUS, ERROR);
			map.put(MESSAGE, resZh.format("shop.cart.productNotExist"));
			renderJson(map);
			return;
		}
		
		Cart cart = cartService.getCurrent();
		if (cart == null) {
			map.put(STATUS, ERROR);
			map.put(MESSAGE, resZh.format("shop.cart.notEmpty"));
			renderJson(map);
			return;
		}
		CartItem cartItem = cart.getCartItem(product,0l);
		if (cartItem == null) {
			map.put(STATUS, ERROR);
			map.put(MESSAGE, resZh.format("shop.cart.cartItemNotExist"));
			renderJson(map);
			return;
		}
		cartItem.setQuantity(quantity);
		cartItem.update();
		map.put(STATUS, SUCCESS);
		renderJson(map);
	}
}
