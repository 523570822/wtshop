package com.wtshop.controller.shop;

import java.util.HashMap;
import java.util.Map;

import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.wtshop.Message;
import com.wtshop.interceptor.ThemeInterceptor;
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

/**
 * Controller - 购物车
 * 
 * 
 */
@ControllerBind(controllerKey = "/cart")
@Before(ThemeInterceptor.class)
public class CartController extends BaseController {

	private MemberService memberService = enhance(MemberService.class);
	private ProductService productService = enhance(ProductService.class);
	private CartService cartService = enhance(CartService.class);
	private CartItemService cartItemService = enhance(CartItemService.class);

	/**
	 * 数量
	 */
	public void quantity() {
		Map<String, Integer> data = new HashMap<String, Integer>();
		Cart cart = cartService.getCurrent();
		data.put("quantity", cart != null ? cart.getProductQuantity() : 0);
		renderJson(data);
	}

	/**
	 * 添加
	 */
	public void add() {
		Long productId = getParaToLong("productId");
		Integer quantity = getParaToInt("quantity");
		if (quantity == null || quantity < 1) {
			renderJson(ERROR_MESSAGE);
			return;
		}
		Product product = productService.find(productId);
		if (product == null) {
			renderJson(Message.warn("shop.cart.productNotExist"));
			return;
		}
		if (!Goods.Type.general.equals(product.getType())) {
			renderJson(Message.warn("shop.cart.productNotForSale"));
			return;
		}
		if (!product.getIsMarketable()) {
			renderJson(Message.warn("shop.cart.productNotMarketable"));
			return;
		}

		Cart cart = cartService.getCurrent();
		if (cart != null) {
			if (cart.contains(product,0l)) {
				CartItem cartItem = cart.getCartItem(product,0l);
				if (CartItem.MAX_QUANTITY != null && cartItem.getQuantity() + quantity > CartItem.MAX_QUANTITY) {
					renderJson(Message.warn("shop.cart.addQuantityNotAllowed", CartItem.MAX_QUANTITY));
					return;
				}
				if (cartItem.getQuantity() + quantity > product.getAvailableStock()) {
					renderJson(Message.warn("shop.cart.productLowStock"));
					return;
				}
			} else {
				if (Cart.MAX_CART_ITEM_COUNT != null && cart.getCartItems().size() >= Cart.MAX_CART_ITEM_COUNT) {
					renderJson(Message.warn("shop.cart.addCartItemCountNotAllowed", Cart.MAX_CART_ITEM_COUNT));
					return;
				}
				if (CartItem.MAX_QUANTITY != null && quantity > CartItem.MAX_QUANTITY) {
					renderJson(Message.warn("shop.cart.addQuantityNotAllowed", CartItem.MAX_QUANTITY));
					return;
				}
				if (quantity > product.getAvailableStock()) {
					renderJson(Message.warn("shop.cart.productLowStock"));
					return;
				}
			}
		} else {
			if (CartItem.MAX_QUANTITY != null && quantity > CartItem.MAX_QUANTITY) {
				renderJson(Message.warn("shop.cart.addQuantityNotAllowed", CartItem.MAX_QUANTITY));
				return;
			}
			if (quantity > product.getAvailableStock()) {
				renderJson(Message.warn("shop.cart.productLowStock"));
				return;
			}
		}
		cart = cartService.add(product, quantity, false,0l);

		Member member = memberService.getCurrent();
		if (member == null) {
			WebUtils.addCookie(getRequest(), getResponse(), Cart.KEY_COOKIE_NAME, cart.getCartKey(), Cart.TIMEOUT);
		}
		renderJson(Message.success("shop.cart.addSuccess", cart.getProductQuantity(), currency(cart.getEffectivePrice(), true, false)));
	}

	/**
	 * 列表
	 */
	public void list() {
		setAttr("cart", cartService.getCurrent());
		render("/shop/${theme}/cart/list.ftl");
	}

	/**
	 * 编辑
	 */
	public void edit() {
		Long id = getParaToLong("id");
		Integer quantity = getParaToInt("quantity");
		Map<String, Object> data = new HashMap<String, Object>();
		if (quantity == null || quantity < 1) {
			data.put("message", ERROR_MESSAGE);
			renderJson(data);
			return;
		}
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
		if (CartItem.MAX_QUANTITY != null && quantity > CartItem.MAX_QUANTITY) {
			data.put("message", Message.warn("shop.cart.addQuantityNotAllowed", CartItem.MAX_QUANTITY));
			renderJson(data);
			return;
		}
		Product product = cartItem.getProduct();
		if (quantity > product.getAvailableStock()) {
			data.put("message", Message.warn("shop.cart.productLowStock"));
			renderJson(data);
			return;
		}
		cartItem.setQuantity(quantity);
		cartItemService.update(cartItem);
		cart = cartService.getCurrent();

		data.put("message", SUCCESS_MESSAGE);
		data.put("subtotal", cartItem.getSubtotal());
		data.put("isLowStock", cartItem.getIsLowStock());
		data.put("quantity", cart.getProductQuantity());
		data.put("effectiveRewardPoint", cart.getEffectiveRewardPoint());
		data.put("effectivePrice", cart.getEffectivePrice());
		data.put("giftNames", cart.getGiftNames());
		data.put("promotionNames", cart.getPromotionNames());
		renderJson(data);
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

		data.put("message", SUCCESS_MESSAGE);
		data.put("isLowStock", cart.getIsLowStock());
		data.put("quantity", cart.getProductQuantity());
		data.put("effectiveRewardPoint", cart.getEffectiveRewardPoint());
		data.put("effectivePrice", cart.getEffectivePrice());
		data.put("giftNames", cart.getGiftNames());
		data.put("promotionNames", cart.getPromotionNames());
		renderJson(data);
	}

	/**
	 * 清空
	 */
	public void clear() {
		Cart cart = cartService.getCurrent();
		cartService.delete(cart.getId());
		renderJson(SUCCESS_MESSAGE);
	}

}