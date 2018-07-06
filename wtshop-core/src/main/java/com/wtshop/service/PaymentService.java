package com.wtshop.service;

import com.wtshop.dao.PaymentDao;
import com.wtshop.dao.SnDao;
import com.wtshop.model.Payment;
import com.wtshop.model.Sn;
import com.wtshop.util.Assert;

/**
 * Service - 收款单
 * 
 * 
 */
public class PaymentService extends BaseService<Payment> {

	/**
	 * 构造方法
	 */
	public PaymentService() {
		super(Payment.class);
	}
	
	private PaymentDao paymentDao;
	private SnDao snDao;
	
	/**
	 * 根据编号查找收款单
	 * 
	 * @param sn
	 *            编号(忽略大小写)
	 * @return 收款单，若不存在则返回null
	 */
	public Payment findBySn(String sn) {
		return paymentDao.findBySn(sn);
	}

	public Payment save(Payment payment) {
		Assert.notNull(payment);


		return super.save(payment);
	}
}