package com.wtshop.model;

import com.wtshop.model.base.BaseTicket;
import com.wtshop.util.ObjectUtils;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Model - 优惠券
 * 
 * 
 */
public class Ticket extends BaseTicket<Ticket> {
	private static final long serialVersionUID = -6063171273339816295L;
	public static final Ticket dao = new Ticket();
	
	/** 优惠码 */
	private List<CouponCode> couponCodes = new ArrayList<CouponCode>();

	/** 促销 */
	private List<Promotion> promotions = new ArrayList<Promotion>();

	/** 订单 */
	private List<Order> orders = new ArrayList<Order>();

	/**
	 * 优惠券状态
	 */
	public enum status{
		/**
		 * 未生效
		 */
		noUsing,

		/**
		 * 已生效
		 */
		using,

		/**
		 * 已过期
		 */
		expire
	}

	/**
	 * 专题对应类别
	 */
	private ProductCategory productCategory;

	public ProductCategory getProductCategory() {
		if(ObjectUtils.isEmpty(productCategory)){
			productCategory = ProductCategory.dao.findById(getProductCategoryId());
		}
		return productCategory;
	}

	/**
	 * 优惠商品
	 */
	private Product product;

	public void setProductCategory(ProductCategory productCategory) {
		this.productCategory = productCategory;
	}

	public Product getProduct() {
		if(ObjectUtils.isEmpty(product)){
			product = Product.dao.findById(getProductId());
		}
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
	/**
	 * 获取优惠码
	 * 
	 * @return 优惠码
	 */
	public List<CouponCode> getCouponCodes() {
		String sql = "SELECT * FROM coupon_code WHERE `ticket_id` = ?";
		if (couponCodes.isEmpty()) {
			couponCodes = CouponCode.dao.find(sql, getId());
		}
		return couponCodes;
	}
	
	/**
	 * 设置优惠码
	 * 
	 * @param couponCodes
	 *            优惠码
	 */
	public void setCouponCodes(List<CouponCode> couponCodes) {
		this.couponCodes = couponCodes;
	}
	
	/**
	 * 获取促销
	 * 
	 * @return 促销
	 */
	public List<Promotion> getPromotions() {
		String sql = "SELECT p.* FROM promotion_coupon pc INNER JOIN promotion p ON pc.`promotions` = p.`id` WHERE pc.`coupons` = ?";
		if (promotions.isEmpty()) {
			promotions = Promotion.dao.find(sql, getId());
		}
		return promotions;
	}
	
	/**
	 * 设置促销
	 * 
	 * @param promotions
	 *            促销
	 */
	public void setPromotions(List<Promotion> promotions) {
		this.promotions = promotions;
	}
	
	/**
	 * 获取订单
	 * 
	 * @return 订单
	 */
	public List<Order> getOrders() {
		String sql = "SELECT o.* FROM order_coupon oc INNER JOIN `order` o ON oc.`orders` = o.`id` WHERE oc.`coupons` = ?";
		if (orders.isEmpty()) {
			orders = Order.dao.find(sql, getId());
		}
		return orders;
	}
	
	/**
	 * 设置订单
	 * 
	 * @param orders
	 *            订单
	 */
	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}
	
	/**
	 * 判断是否已开始
	 * 
	 * @return 是否已开始
	 */
	public boolean hasBegun() {
		return getBeginDate() == null || !getBeginDate().after(new Date());
	}

	/**
	 * 判断是否已过期
	 * 
	 * @return 是否已过期
	 */
	public boolean hasExpired() {
		return getEndDate() != null && !getEndDate().after(new Date());
	}

	/**
	 * 计算优惠价格
	 * 
	 * @param price
	 *            商品价格
	 * @param quantity
	 *            商品数量
	 * @return 优惠价格
	 */
	public BigDecimal calculatePrice(BigDecimal price, Integer quantity) {
		if (price == null || quantity == null || StringUtils.isEmpty(getPriceExpression())) {
			return price;
		}
		BigDecimal result = BigDecimal.ZERO;
		try {
			Binding binding = new Binding();
			binding.setVariable("quantity", quantity);
			binding.setVariable("price", price);
			GroovyShell groovyShell = new GroovyShell(binding);
			result = new BigDecimal(groovyShell.evaluate(getPriceExpression()).toString());
		} catch (Exception e) {
			return price;
		}
		if (result.compareTo(price) > 0) {
			return price;
		}
		return result.compareTo(BigDecimal.ZERO) > 0 ? result : BigDecimal.ZERO;
	}

	/**
	 * 删除前处理
	 */
	public void preRemove() {
		List<Promotion> promotions = getPromotions();
		if (promotions != null) {
			for (Promotion promotion : promotions) {
				promotion.getCoupons().remove(this);
			}
		}
		List<Order> orders = getOrders();
		if (orders != null) {
			for (Order order : orders) {
				order.getCoupons().remove(this);
			}
		}
	}



}
