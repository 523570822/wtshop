package com.wtshop.service;

import com.jfinal.aop.Enhancer;
import com.jfinal.plugin.activerecord.Record;
import com.wtshop.Filter;
import com.wtshop.Order;
import com.wtshop.dao.FullAntiDao;
import com.wtshop.dao.MemberRankDao;
import com.wtshop.dao.ProductCategoryDao;
import com.wtshop.dao.PromotionDao;
import com.wtshop.model.*;
import com.wtshop.util.Assert;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.math.BigDecimal;
import java.util.*;

/**
 * Service - 促销
 * 
 * 
 */
public class FullAntiService extends BaseService<FullAnti> {

	/**
	 * 构造方法
	 */
	public FullAntiService() {
		super(FullAnti.class);
	}
	
	/** 价格表达式变量 */
	private static final List<Map<String, Object>> PRICE_EXPRESSION_VARIABLES = new ArrayList<Map<String, Object>>();

	/** 积分表达式变量 */
	private static final List<Map<String, Object>> POINT_EXPRESSION_VARIABLES = new ArrayList<Map<String, Object>>();

	private PromotionDao promotionDao = Enhancer.enhance(PromotionDao.class);
	private MemberRankDao memberRankDao = Enhancer.enhance(MemberRankDao.class);
	private ProductCategoryDao productCategoryDao = Enhancer.enhance(ProductCategoryDao.class);
	private FullAntiDao fullAntiDao = Enhancer.enhance(FullAntiDao.class);


	static {
		Map<String, Object> variable0 = new HashMap<String, Object>();
		Map<String, Object> variable1 = new HashMap<String, Object>();
		Map<String, Object> variable2 = new HashMap<String, Object>();
		Map<String, Object> variable3 = new HashMap<String, Object>();
		variable0.put("quantity", 99);
		variable0.put("price", new BigDecimal("99"));
		variable1.put("quantity", 99);
		variable1.put("price", new BigDecimal("9.9"));
		variable2.put("quantity", 99);
		variable2.put("price", new BigDecimal("0.99"));
		variable3.put("quantity", 99);
		variable3.put("point", 99L);
		PRICE_EXPRESSION_VARIABLES.add(variable0);
		PRICE_EXPRESSION_VARIABLES.add(variable1);
		PRICE_EXPRESSION_VARIABLES.add(variable2);
		POINT_EXPRESSION_VARIABLES.add(variable3);
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
	 * 验证积分运算表达式
	 * 
	 * @param pointExpression
	 *            积分运算表达式
	 * @return 验证结果
	 */
	public boolean isValidPointExpression(String pointExpression) {
		Assert.hasText(pointExpression);

		for (Map<String, Object> variable : POINT_EXPRESSION_VARIABLES) {
			try {
				Binding binding = new Binding();
				for (Map.Entry<String, Object> entry : variable.entrySet()) {
					binding.setVariable(entry.getKey(), entry.getValue());
				}
				GroovyShell groovyShell = new GroovyShell(binding);
				Object result = groovyShell.evaluate(pointExpression);
				Long.valueOf(result.toString());
			} catch (Exception e) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 查找促销
	 * 
	 * @param memberRank
	 *            会员等级
	 * @param productCategory
	 *            商品分类
	 * @param hasBegun
	 *            是否已开始
	 * @param hasEnded
	 *            是否已结束
	 * @param count
	 *            数量
	 * @param filters
	 *            筛选
	 * @param orders
	 *            排序
	 * @return 促销
	 */
	public List<Promotion> findList(MemberRank memberRank, ProductCategory productCategory, Boolean hasBegun, Boolean hasEnded, Integer count, List<Filter> filters, List<Order> orders) {
		return promotionDao.findList(memberRank, productCategory, hasBegun, hasEnded, count, filters, orders);
	}

	/**
	 * 查找促销
	 * 
	 * @param memberRankId
	 *            会员等级ID
	 * @param productCategoryId
	 *            商品分类ID
	 * @param hasBegun
	 *            是否已开始
	 * @param hasEnded
	 *            是否已结束
	 * @param count
	 *            数量
	 * @param filters
	 *            筛选
	 * @param orders
	 *            排序
	 * @param useCache
	 *            是否使用缓存
	 * @return 促销
	 */
	public List<Promotion> findList(Long memberRankId, Long productCategoryId, Boolean hasBegun, Boolean hasEnded, Integer count, List<Filter> filters, List<Order> orders, boolean useCache) {
		MemberRank memberRank = memberRankDao.find(memberRankId);
		if (memberRankId != null && memberRank == null) {
			return Collections.emptyList();
		}
		ProductCategory productCategory = productCategoryDao.find(productCategoryId);
		if (productCategoryId != null && productCategory == null) {
			return Collections.emptyList();
		}
		return promotionDao.findList(memberRank, productCategory, hasBegun, hasEnded, count, filters, orders);
	}
	
	/**
	 * 保存
	 * 
	 */
	public FullAnti save(FullAnti promotion) {
		super.save(promotion);


		return promotion;
	}

	/**
	 * 更新
	 * 
	 */
	public FullAnti update(FullAnti promotion) {
		super.update(promotion);
		// 允许参加会员等级
		return promotion;
	}

	public void delete(Long id) {
		PromotionMemberRank.dao.delete(id);
		PromotionCoupon.dao.delete(id);
		PromotionGift.dao.delete(id);
		super.delete(id);
	}

	public void delete(Long... ids) {
		if (ids != null) {
			for (Long id : ids) {
				PromotionMemberRank.dao.delete(id);
				PromotionCoupon.dao.delete(id);
				PromotionGift.dao.delete(id);
			}
		}
		super.delete(ids);
	}

	public void delete(FullAnti promotion) {
		super.delete(promotion);
	}
	public    List<Record> findTotalMoney(){
	    return  fullAntiDao.findTotalMoney();
	}
	public    List<FullAnti> findTotalMoneyList(Object totalMoey){
		return  fullAntiDao.findTotalMoneyList(totalMoey);
	}
}