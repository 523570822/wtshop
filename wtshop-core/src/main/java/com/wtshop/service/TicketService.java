package com.wtshop.service;

import com.jfinal.aop.Enhancer;
import com.jfinal.plugin.activerecord.Page;
import com.wtshop.Pageable;
import com.wtshop.dao.TicketDao;
import com.wtshop.model.ProductCategory;
import com.wtshop.model.Ticket;
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
public class TicketService extends BaseService<Ticket> {


	/**
	 * 构造方法
	 */
	public TicketService() {
		super(Ticket.class);
	}
	
	/** 价格表达式变量 */
	private static final List<Map<String, Object>> PRICE_EXPRESSION_VARIABLES = new ArrayList<Map<String, Object>>();
	
	private TicketDao couponDao = Enhancer.enhance(TicketDao.class);;
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

	public List<Ticket> findCodeList(Long memberId ,Double price ,List<Long> categoryList){

		//可用优惠券的集合
		List<Long> code = new ArrayList<>();
		//分类优惠券
		if(categoryList != null && categoryList.size() > 0) {
			//先查我的所有可用优惠券
			List<Ticket> myCode = couponDao.findMyCode(memberId ,1);
			//判断其中所有满足条件的
			for (Ticket coupon : myCode) {
				//通用优惠券
				if( 242 == coupon.getProductCategoryId()){
					code.add(coupon.getId());
				}else {
					List<Long> productCategorys = productCategoryService.getProductCategory(coupon.getProductCategoryId());
					//如果我的优惠券中获取的分类子集包含传进来的集合 认为可用
					if (productCategorys.containsAll(categoryList)) {
						code.add(coupon.getId());
					}
				}

			}
			if(code.size() > 0){
				return couponDao.findCodeList(memberId ,price ,code);
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

	public List<Ticket> findCodeLists(Long memberId ,Double price ,List<Long> productList){

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
	public Page<Ticket> findPage(Boolean isEnabled, Boolean isExchange, Boolean hasExpired, Pageable pageable ,Integer type) {
		return couponDao.findPage(isEnabled, isExchange, hasExpired, pageable ,type);
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


	public List<Ticket> findCoupon() {
		return couponDao.findCoupon();
	}

	//通过活动配置id获取优惠券

	public  List<Ticket> getListByConfigId(long confId){
		String sql="SELECT * FROM ticket WHERE config_id="+confId;
	  return (List<Ticket>)couponDao.findListBySql(sql);
	}

	//通过ticket的ids获取优惠券
	public List<Ticket> findListByIds(String ids){
		String sql="SELECT * FROM ticket WHERE id in ("+ids+")";
		return (List<Ticket>)couponDao.findListBySql(sql);
	}



}