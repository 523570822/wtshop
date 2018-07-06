package com.wtshop.service;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.jfinal.aop.Enhancer;
import com.jfinal.plugin.activerecord.Page;
//import com.wtshop.Page;
import com.wtshop.Pageable;
import com.wtshop.dao.ProductNotifyDao;
import com.wtshop.model.Member;
import com.wtshop.model.Product;
import com.wtshop.model.ProductNotify;

/**
 * Service - 到货通知
 * 
 * 
 */
public class ProductNotifyService extends BaseService<ProductNotify> {

	/**
	 * 构造方法
	 */
	public ProductNotifyService() {
		super(ProductNotify.class);
	}
	
	private ProductNotifyDao productNotifyDao = Enhancer.enhance(ProductNotifyDao.class);
	private MailService mailService = new MailService();
	
	/**
	 * 判断到货通知是否存在
	 * 
	 * @param product
	 *            商品
	 * @param email
	 *            E-mail(忽略大小写)
	 * @return 到货通知是否存在
	 */
	public boolean exists(Product product, String email) {
		return productNotifyDao.exists(product, email);
	}

	/**
	 * 查找到货通知分页
	 * 
	 * @param member
	 *            会员
	 * @param isMarketable
	 *            是否上架
	 * @param isOutOfStock
	 *            商品是否缺货
	 * @param hasSent
	 *            是否已发送.
	 * @param pageable
	 *            分页信息
	 * @return 到货通知分页
	 */
	public Page<ProductNotify> findPage(Member member, Boolean isMarketable, Boolean isOutOfStock, Boolean hasSent, Pageable pageable) {
		return productNotifyDao.findPage(member, isMarketable, isOutOfStock, hasSent, pageable);
	}

	/**
	 * 查找到货通知数量
	 * 
	 * @param member
	 *            会员
	 * @param isMarketable
	 *            是否上架
	 * @param isOutOfStock
	 *            商品是否缺货
	 * @param hasSent
	 *            是否已发送.
	 * @return 到货通知数量
	 */
	public Long count(Member member, Boolean isMarketable, Boolean isOutOfStock, Boolean hasSent) {
		return productNotifyDao.count(member, isMarketable, isOutOfStock, hasSent);
	}


}