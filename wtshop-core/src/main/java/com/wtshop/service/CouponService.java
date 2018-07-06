package com.wtshop.service;

import com.jfinal.aop.Enhancer;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.wtshop.Pageable;
import com.wtshop.dao.CouponDao;
import com.wtshop.model.Coupon;
import com.wtshop.model.Member;
import com.wtshop.model.ProductCategory;
import com.wtshop.util.Assert;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service - 优惠券
 * 
 * 
 */
public class CouponService extends BaseService<Coupon> {
	
	/**
	 * 构造方法
	 */
	public CouponService() {
		super(Coupon.class);
	}
	
	/** 价格表达式变量 */
	private static final List<Map<String, Object>> PRICE_EXPRESSION_VARIABLES = new ArrayList<Map<String, Object>>();
	
	private CouponDao couponDao = Enhancer.enhance(CouponDao.class);;
	private ProductCategoryService productCategoryService = Enhancer.enhance(ProductCategoryService.class);
	
	static {
		Map<String, Object> variable0 = new HashMap<String, Object>();
		Map<String, Object> variable1 = new HashMap<String, Object>();
		Map<String, Object> variable2 = new HashMap<String, Object>();
		variable0.put("quantity", 99);
		variable0.put("price", new BigDecimal("99"));
		variable1.put("quantity", 99);
		variable1.put("price", new BigDecimal("9.9"));
		variable2.put("quantity", 99);
		variable2.put("price", new BigDecimal("0.99"));
		PRICE_EXPRESSION_VARIABLES.add(variable0);
		PRICE_EXPRESSION_VARIABLES.add(variable1);
		PRICE_EXPRESSION_VARIABLES.add(variable2);
	}

	/**
	 *
	 * @param memberId
	 * @param price
	 * @param categoryList 分类集合
	 * @return
	 */

	public List<Coupon> findCodeList(Long memberId ,Double price ,List<Long> categoryList){

		//可用普通优惠券的集合
		List<Long> code = new ArrayList<>();
		//可用分享优惠券的集合
		List<Long> codes = new ArrayList<>();
		//分类优惠券
		if(categoryList != null && categoryList.size() > 0) {
			//先查我的所有可用优惠券
			List<Record> myCode = couponDao.findMyCode(memberId, 1);
			//判断其中所有满足条件的
			for (Record coupon : myCode) {
				//普通优惠券
				if(0 == coupon.getLong("category")){
					//通用优惠券
					if( 242 == coupon.getLong("product_category_id")  ){
						code.add(coupon.getLong("pid"));
					}else {
						List<Long> productCategorys = productCategoryService.getProductCategory(coupon.getLong("product_category_id") );
						//如果我的优惠券中获取的分类子集包含传进来的集合 认为可用
						if (productCategorys.containsAll(categoryList)) {
							code.add(coupon.getLong("pid"));
						}
					}
					//分享优惠券
				}else if(1 == coupon.getLong("category")){
					//通用优惠券
					if( 242 == coupon.getLong("product_category_id")  ){
						codes.add(coupon.getLong("pid"));
					}else {
						List<Long> productCategorys = productCategoryService.getProductCategory(coupon.getLong("product_category_id") );
						//如果我的优惠券中获取的分类子集包含传进来的集合 认为可用
						if (productCategorys.containsAll(categoryList)) {
							codes.add(coupon.getLong("pid"));
						}
					}
				}

			}
			if(code.size() > 0 || codes.size() > 0){
				return couponDao.findCodeList(memberId ,price ,code ,codes);
			} else {
				return null;
			}

		}else {
			return null;
		}

	}


	/**
	 *
	 * @param memberId
	 * @param price
	 * @param productList 商品集合
	 * @return
	 */

	public List<Coupon> findCodeLists(Long memberId ,Double price ,List<Long> productList){

		return couponDao.findCodeLists(memberId ,price ,productList.get(0));

	}
	
	/**
	 * 验证价格运算表达式
	 * 
	 * @param priceExpression
	 *            价格运算表达式
	 * @return 验证结果
	 */
	public boolean isValidPriceExpression(String priceExpression) {
		Assert.hasText(priceExpression);

		for (Map<String, Object> variable : PRICE_EXPRESSION_VARIABLES) {
			try {
				Binding binding = new Binding();
				for (Map.Entry<String, Object> entry : variable.entrySet()) {
					binding.setVariable(entry.getKey(), entry.getValue());
				}
				GroovyShell groovyShell = new GroovyShell(binding);
				Object result = groovyShell.evaluate(priceExpression);
				new BigDecimal(result.toString());
			} catch (Exception e) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 查找优惠券分页
	 * 
	 * @param isEnabled
	 *            是否启用
	 * @param isExchange
	 *            是否允许积分兑换
	 * @param hasExpired
	 *            是否已过期
	 * @param pageable
	 *            分页信息
	 * @return 优惠券分页
	 */
	public Page<Coupon> findPage(Boolean isEnabled, Boolean isExchange, Boolean hasExpired, Pageable pageable ,List<Long> productCategory ) {
		return couponDao.findPage(isEnabled, isExchange, hasExpired, pageable ,productCategory);
	}

	/**
	 * 查找优惠码分页
	 *
	 * @param member
	 *            会员
	 * @param pageable
	 *            分页信息
	 * @return 优惠码分页
	 */
	public Page<Coupon> findPage(Member member, Pageable pageable, Boolean isUsed, List<Long> productCategory  , Boolean hasExpired, Boolean isEnabled) {
		return couponDao.findPage(member, pageable, isUsed , productCategory ,hasExpired, isEnabled);
	}

	/**
	 * 查找优惠码分页
	 *
	 * @param member
	 *            会员
	 * @param pageable
	 *            分页信息
	 * @return 优惠码分页
	 */
	public Page<Coupon> findSharePage(Member member, Pageable pageable, Boolean isUsed, List<Long> productCategory  , Boolean hasExpired, Boolean isEnabled) {
		return couponDao.findPage(member, pageable, isUsed , productCategory ,hasExpired, isEnabled);
	}

	/**
	 * 匹配分类信息 判断输入的集合是否在某一分类条件下  比如243护肤产品
	 * @param categoryList
	 * @return
	 */

	public Long getType(List<Long> categoryList){
		List<ProductCategory> all = productCategoryService.findAll();
		for(ProductCategory productCategory : all){
			List<Long> productCategory1 = productCategoryService.getProductCategory(productCategory.getId());
			if(productCategory1.containsAll(categoryList)){
				return productCategory.getId();
			}
		}
		return null;

	}


	public List<Coupon> findCoupon() {
		return couponDao.findCoupon();
	}

}