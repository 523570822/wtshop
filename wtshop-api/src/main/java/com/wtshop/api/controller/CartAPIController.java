package com.wtshop.api.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.i18n.I18n;
import com.jfinal.i18n.Res;
import com.jfinal.plugin.activerecord.Page;
import com.wtshop.Pageable;
import com.wtshop.Setting;
import com.wtshop.api.common.result.CartGoodsResult;
import com.wtshop.api.common.result.CartListResult;
import com.wtshop.api.interceptor.ErrorInterceptor;
import com.wtshop.interceptor.WapInterceptor;
import com.wtshop.model.*;
import com.wtshop.service.*;
import com.wtshop.util.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerBind(controllerKey = "/api/cart")
@Before({WapInterceptor.class, ErrorInterceptor.class} )
public class CartAPIController extends BaseAPIController {

	private CartService cartService = enhance(CartService.class);
	private MemberService memberService = enhance(MemberService.class);
	private ProductService productService = enhance(ProductService.class);
	private CartItemService cartItemService = enhance(CartItemService.class);
	private GoodsService goodsService = enhance(GoodsService.class);
	private AreaService areaService = enhance(AreaService.class);
	private PromotionService promotionService = enhance(PromotionService.class);
	private GoodsPromotionService goodsPromotionService = enhance(GoodsPromotionService.class);
	private CertificatesService certificatesService= enhance(CertificatesService.class);
	private MemberFavoriteGoodsService favoriteGoodsService = enhance(MemberFavoriteGoodsService.class);
	private Res resZh = I18n.use();

	/**
	 * 添加
	 * http://localhost/api/cart/add.jhtml?productIds=88&quantitys=1&buy_nows=0
	 * {"msg":"请求成功","code":1,"data":{}}
	 */
	public void add() {
		Long productId = getParaToLong("productIds");
		Integer quantity = getParaToInt("quantitys");
		Boolean buyNow = getParaToBoolean("buy_nows");

		Member member = memberService.getCurrent();

		if(StringUtils.isEmpty(member.getOnShareCode())){
			renderJson(ApiResult.fail(7,"请填写邀请码"));
			return;
		}


		if (quantity == null || quantity < 1) {
			renderJson(ApiResult.fail("数量不能为空!"));
			return ;
		}
		Product product = productService.find(productId);
		if (product == null) {
			renderJson(ApiResult.fail(resZh.format("shop.cart.productNotExist")));
			return;
		}
		if (!product.getIsMarketable()) {
			renderJson(ApiResult.fail(resZh.format("shop.cart.productNotMarketable")));
			return;
		}


		Cart cart = cartService.getCurrent();
		if (cart != null) {
			if (cart.contains(product)) {
				CartItem cartItem = cart.getCartItem(product);
				if (CartItem.MAX_QUANTITY != null && cartItem.getQuantity() + quantity > CartItem.MAX_QUANTITY) {
					renderJson(ApiResult.fail(resZh.format("shop.cart.addQuantityNotAllowed", CartItem.MAX_QUANTITY)));
					return;
				}
				if (cartItem.getQuantity() + quantity > product.getAvailableStock()) {
					renderJson(ApiResult.fail(resZh.format("shop.cart.productLowStock")));
					return;
				}
			} else {
				if (Cart.MAX_CART_ITEM_COUNT != null && cart.getCartItems().size() >= Cart.MAX_CART_ITEM_COUNT) {
					renderJson(ApiResult.fail(resZh.format("shop.cart.addCartItemCountNotAllowed", Cart.MAX_CART_ITEM_COUNT)));
					return;
				}
				if (CartItem.MAX_QUANTITY != null && quantity > CartItem.MAX_QUANTITY) {
					renderJson(ApiResult.fail(resZh.format("shop.cart.addQuantityNotAllowed", CartItem.MAX_QUANTITY)));
					return;
				}
				if (quantity > product.getAvailableStock()) {
					renderJson(ApiResult.fail(resZh.format("shop.cart.productLowStock")));
					return;
				}
			}
		} else {
			if (CartItem.MAX_QUANTITY != null && quantity > CartItem.MAX_QUANTITY) {
				renderJson(ApiResult.fail(resZh.format("shop.cart.addQuantityNotAllowed", CartItem.MAX_QUANTITY)));
				return;
			}
			if (quantity > product.getAvailableStock()) {
				renderJson(ApiResult.fail(resZh.format("shop.cart.productLowStock")));
				return;
			}
		}
		cart = cartService.add(product, quantity, buyNow);
		if (member == null) {
			WebUtils.addCookie(getRequest(), getResponse(), Cart.KEY_COOKIE_NAME, cart.getCartKey(), Cart.TIMEOUT);
		}
		renderJson(ApiResult.success());
	}


	/**
	 * 凑单商品
	 */

	public void findGoods(){
		String price = getPara("price");
		Integer activityId = getParaToInt("activityId");
		Integer pageNumber = getParaToInt("pageNumbers", 1);
		Integer pageSize =  999999 ;
		if(1 == activityId){
			Pageable pageable = new Pageable(pageNumber, pageSize);
			Page<Goods> goods = goodsService.findGoods(price, pageable);
			renderJson(ApiResult.success(goods));
		}else {
			Pageable pageable = new Pageable(pageNumber, pageSize);
			Page<Goods> goodsByPromId = goodsService.findGoodsByPromId(5L,pageable);
			renderJson(ApiResult.success(goodsByPromId));
		}

	}



	/**
	 * 再次购买
	 * http://localhost/api/cart/again.jhtml?param=[{productId:88,quantity:1}]
	 */
	public void again() {
		String params = getPara("param");
		JSONArray arr = JSONArray.parseArray(params);

		// 定义组数存放产品id
		String ids = "";
		for (int i = 0; i < arr.size(); i++) {
			JSONObject obj = arr.getJSONObject(i);
			Long productId = obj.getLong("productId");
			Integer quantity = obj.getInteger("quantity");
			
			if (quantity == null || quantity < 1) {
				renderJson(ApiResult.fail("数量不能为空!"));
				return;
			}
			Product product = productService.find(productId);
			if (product == null) {
				renderJson(ApiResult.fail(resZh.format("shop.cart.productNotExist")));
				return;
			}
			if (!Goods.Type.general.equals(product.getType())) {
				renderJson(ApiResult.fail(resZh.format("shop.cart.productNotForSale")));
				return;
			}
			if (!product.getIsMarketable()) {
				renderJson(ApiResult.fail(resZh.format("shop.cart.productNotMarketable")));
				return;
			}
			
			Cart cart = cartService.getCurrent();
			if (cart != null) {
				if (cart.contains(product)) {
					CartItem cartItem = cart.getCartItem(product);
					if (CartItem.MAX_QUANTITY != null && cartItem.getQuantity() + quantity > CartItem.MAX_QUANTITY) {
						renderJson(ApiResult.fail(resZh.format("shop.cart.addQuantityNotAllowed", CartItem.MAX_QUANTITY)));
						return;
					}
					if (cartItem.getQuantity() + quantity > product.getAvailableStock()) {
						renderJson(ApiResult.fail(resZh.format("shop.cart.productLowStock")));
						return;
					}
				} else {
					if (Cart.MAX_CART_ITEM_COUNT != null && cart.getCartItems().size() >= Cart.MAX_CART_ITEM_COUNT) {
						renderJson(ApiResult.fail(resZh.format("shop.cart.addCartItemCountNotAllowed", Cart.MAX_CART_ITEM_COUNT)));
						return;
					}
					if (CartItem.MAX_QUANTITY != null && quantity > CartItem.MAX_QUANTITY) {
						renderJson(ApiResult.fail(resZh.format("shop.cart.addQuantityNotAllowed", CartItem.MAX_QUANTITY)));
						return;
					}
					if (quantity > product.getAvailableStock()) {
						renderJson(ApiResult.fail(resZh.format("shop.cart.productLowStock")));
						return;
					}
				}
			} else {
				if (CartItem.MAX_QUANTITY != null && quantity > CartItem.MAX_QUANTITY) {
					renderJson(ApiResult.fail(resZh.format("shop.cart.addQuantityNotAllowed", CartItem.MAX_QUANTITY)));
					return;
				}
				if (quantity > product.getAvailableStock()) {
					renderJson(ApiResult.fail(resZh.format("shop.cart.productLowStock")));
					return;
				}
			}
			ids += productId + ",";
			cart = cartService.add(product, quantity, false);
		}
		renderJson(ApiResult.success());
	}
	
	/**
	 * 列表
	 * {"msg":"","code":1,"data":{"current":{"cart_key":"8041fb81fccd462825b9b9a94f32c35f","create_date":"2017-05-23 15:58:57","expire":"2017-05-31","id":120,"member_id":null,"modify_date":"2017-05-23 15:58:57","version":0}}}
	 */
	public void list() {
		Cart cart = cartService.getCurrent();
		Setting setting = SystemUtils.getSetting();
		String message = "";
		Double subtract = 0d;

		String promMessage = "";
		Double promSubtract = 0d;

		List<Goods> cartList = new ArrayList<>();
		List<Goods> promGoodsList = new ArrayList<>();
		List<Goods> goodsList = new ArrayList<>();
		if(cart != null){
			cartList = goodsService.findCartItemList(cart.getId());
			if(cartList != null && cartList.size() > 0){
				for(Goods goods : cartList){
					Long areaId = goods.getAreaId();
					List<Area> areas = areaService.findParents(areaService.find(areaId), true, null);
					String name = areas.get(0).getName();
					goods.setCaption(name);

					GoodsPromotion goodsPromotion = goodsPromotionService.findByGoodsId(goods.getId());
					if(goodsPromotion != null){
						promGoodsList.add(goods);
					}else {
						goodsList.add(goods);
					}

				}
			}
		}
		if(!setting.getIsFreeMoney() && cart != null && cartList != null && cartList.size() >0 ){
			//获取购物车商品 计算价格
			Double price = cartService.getPrice(cart);
			Double freeMoney = setting.getFreeMoney().doubleValue();

			subtract = MathUtil.subtract(freeMoney, price);
			if(subtract > 0){
				message = "满"+ MathUtil.getInt(freeMoney.toString())+"元减免运费,还差"+ MathUtil.getInt(subtract.toString())+"元";
			}else {
				subtract = 0d;
			}

		}
		if(cart != null && cartList != null && cartList.size() >0){
			Promotion promotion = promotionService.find(5L);
			Double prom = cartService.findPriceByCartId(cart);

			promSubtract = MathUtil.subtract(promotion.getTotalMoney(), prom);
			if(promSubtract > 0){
				promMessage = "满"+ MathUtil.getInt(promotion.getTotalMoney().toString())+"元减"+ MathUtil.getInt(promotion.getMoney().toString())+"元,还差"+ MathUtil.getInt(promSubtract.toString())+"元";
			}else {
				promSubtract = 0d;
			}
		}
		List<CartGoodsResult> cartGoodsList = new ArrayList<>();
		CartGoodsResult cartGoodsResult = null;
		CartGoodsResult cartListResult1 = null;
		if(goodsList != null && goodsList.size() > 0){
		//	cartGoodsResult = new CartGoodsResult(message, goodsList,1);
		}
		if(promGoodsList != null && promGoodsList.size() > 0){
		//	cartListResult1 = new CartGoodsResult(promMessage, promGoodsList, 5);
		}
		Product product = productService.findByCartId(cart.getId());
		if(product != null){
			GoodsPromotion goodsPromotion = goodsPromotionService.findByGoodsId(product.getGoodsId());
			if(goodsPromotion == null){
				if( cartGoodsResult != null){
					cartGoodsList.add(cartGoodsResult);
				}
				if( cartListResult1 != null ){
					cartGoodsList.add(cartListResult1);
				}

			}else {
				if( cartListResult1 != null){
					cartGoodsList.add(cartListResult1);
				}
				if( cartGoodsResult != null ){
					cartGoodsList.add(cartGoodsResult);
				}
			}
		}



		CartListResult cartListResult = new CartListResult(cartGoodsList,null, subtract, promSubtract);
		renderJson(ApiResult.success(cartListResult));
	}

	public void cartList(){
		Cart cart = cartService.getCurrent();

		String message = "";
		Double subtract = 0d;
		JSONObject redisSetting = JSONObject.parseObject(RedisUtil.getString("redisSetting"));
		String promMessage = "";
		Double promSubtract = 0d;
		List<CartGoodsResult> messageList = new ArrayList<>();

		List<Goods> cartList = new ArrayList<>();
		List<Map> cartListMap = new ArrayList<>();

		if(cart != null){
			cartList = goodsService.findCartItemList(cart.getId());
			if(cartList != null && cartList.size() > 0){
				for(Goods goods : cartList){
					Product product = productService.find(goods.get("product_id"));
					List<String> specifications = product.getSpecifications();
					Map<String,Object> sb= new HashMap<>();

					List<Area> areas = areaService.findParents(areaService.find(goods.getAreaId()), true, null);
					goods.setCaption(areas.get(0).getName());

					GoodsPromotion goodsPromotion = goodsPromotionService.findByGoodsId(goods.getId());
					if(goodsPromotion != null){
						goods.setAttributeValue0("1");
					}else {
						goods.setAttributeValue0("0");
					}
					sb.put("goods",goods);
					sb.put("specifications",specifications);
					cartListMap.add(sb);
				}
			}
		}
		if(! redisSetting.getBoolean("isFreeMoney") && cart != null && cartList != null && cartList.size() >0 ){
			//获取购物车商品 计算价格
			Double price = cartService.getPrice(cart);
			Double freeMoney = redisSetting.getDouble("freeMoney");
			subtract = MathUtil.subtract(freeMoney, price);
			if(subtract > 0){
				message = "满"+ MathUtil.getInt(freeMoney.toString())+"元减免运费,还差"+ MathUtil.getInt(subtract.toString())+"元";
				messageList.add (new CartGoodsResult(message, 1, "去凑单"));
			}else {
				subtract = 0d;
			}

		}
		if(cart != null && cartList != null && cartList.size() >0){
			Promotion promotion = promotionService.find(5L);
			Double prom = cartService.findPriceByCartId(cart);

			promSubtract = MathUtil.subtract(promotion.getTotalMoney(), prom);
			if(promSubtract > 0 && prom != 0){
				promMessage = "满"+ MathUtil.getInt(promotion.getTotalMoney().toString())+"元减"+ MathUtil.getInt(promotion.getMoney().toString())+"元,还差"+ MathUtil.getInt(promSubtract.toString())+"元";
				messageList.add (new CartGoodsResult(promMessage, 5, "去凑单"));
			}else {
				promSubtract = 0d;
			}
		}
		if(messageList.size() == 0 ){
			messageList = null;
		}
		CartListResult cartListResult = new CartListResult(messageList, cartListMap, subtract, promSubtract);
		renderJson(ApiResult.success(cartListResult));


	}
	
	
	/**
	 * 删除
	 * {"msg":"购物车项不存在","code":0,"data":null}
	 */
	public void delete() {
		String id = getPara("id");

		String[] cartItemIds = id.split(",");
		Member member = memberService.getCurrent();
		Cart cart = cartService.getCurrent();
		if (cart == null || cart.isEmpty()) {
			renderJson(ApiResult.fail(resZh.format("shop.cart.notEmpty")));
			return;
		}

		List<Long> goodsLists = new ArrayList<>();
		for(String cartItemId :cartItemIds){
			CartItem cartItem = cartItemService.find(Long.parseLong(cartItemId));
			if (!cart.contains(cartItem)) {
				renderJson(ApiResult.fail(resZh.format("shop.cart.cartItemNotExist")));
				return;
			}
			cartItemService.delete(cartItem);
			cart.getCartItems().remove(cartItem);
			MemberFavoriteGoods goods = favoriteGoodsService.findGoods(member.getId(), cartItem.getProduct().getGoodsId(),0l);
			if( goods == null ){
				goodsLists.add(cartItem.getProduct().getGoodsId());
			}

		}
		renderJson(ApiResult.success(goodsLists));
	}
	
	/**
	 * 获取购物车总数
	 * {"sku_counts":3}
	 */
	public void getCartCount() {
		JSONObject object = new JSONObject();
		Cart cart = cartService.getCurrent();
		object.put("sku_counts", cart != null ? cart.getQuantity() : 0);
		renderJson(object);
	}
	
	/**
	 * 更新购物车数量
	 * 改变数量 加减按钮
	 * {"msg":"请求成功","code":1,"data":null}
	 */
	public void setNums(){
		Long productId = getParaToLong("productIds", null);
		Integer quantity = getParaToInt("quantitys", null);

		if (quantity == null) {
			renderJson(ApiResult.fail("数量不能为空!"));
			return;
		}
		
		Product product = productService.find(productId);
		if (product == null) {
			renderJson(ApiResult.fail(resZh.format("shop.cart.productNotExist")));
			return;
		}
		
		Cart cart = cartService.getCurrent();
		if (cart == null) {
			renderJson(ApiResult.fail(resZh.format("shop.cart.notEmpty")));
			return;
		}
		CartItem cartItem = cart.getCartItem(product);
		if (cartItem == null) {
			renderJson(ApiResult.fail(resZh.format("shop.cart.cartItemNotExist")));
			return;
		}
		cartItem.setQuantity(quantity);
		cartItem.update();
		renderJson(ApiResult.success());
	}
}
