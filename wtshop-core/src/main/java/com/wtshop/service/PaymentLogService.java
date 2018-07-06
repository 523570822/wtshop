package com.wtshop.service;

import com.jfinal.aop.Enhancer;
import com.wtshop.dao.PaymentLogDao;
import com.wtshop.dao.SnDao;
import com.wtshop.model.DepositLog;
import com.wtshop.model.Member;
import com.wtshop.model.Order;
import com.wtshop.model.Payment;
import com.wtshop.model.PaymentLog;
import com.wtshop.model.Sn;
import com.wtshop.util.Assert;

/**
 * Service - 支付记录
 * 
 * 
 */
public class PaymentLogService extends BaseService<PaymentLog> {

	/**
	 * 构造方法
	 */
	public PaymentLogService() {
		super(PaymentLog.class);
	}
	
	private PaymentLogDao paymentLogDao = Enhancer.enhance(PaymentLogDao.class);
	private SnDao snDao = Enhancer.enhance(SnDao.class);
	private MemberService memberService = Enhancer.enhance(MemberService.class);
	private OrderService orderService = Enhancer.enhance(OrderService.class);
	
	/**
	 * 根据编号查找支付记录
	 * 
	 * @param sn
	 *            编号(忽略大小写)
	 * @return 支付记录，若不存在则返回null
	 */
	public PaymentLog findBySn(String sn) {
		return paymentLogDao.findBySn(sn);
	}

	/**
	 * 支付处理
	 * 
	 * @param paymentLog
	 *            支付记录
	 */
	public void handle(PaymentLog paymentLog) {
		Assert.notNull(paymentLog);
		Assert.notNull(paymentLog.getType());

		if (!PaymentLog.Status.wait.equals(paymentLog.getStatusName())) {
			return;
		}

		switch (paymentLog.getTypeName()) {
		case recharge:
			Member member = paymentLog.getMember();
//			if (member != null) {
//				memberService.addBalance(member, paymentLog.getEffectiveAmount(), DepositLog.Type.recharge, null, null);
//			}
			break;
		case payment:
			Order order = paymentLog.getOrder();
			if (order != null) {
				Payment payment = new Payment();
				payment.setMethod(Payment.Method.online.ordinal());
				payment.setPaymentMethod(paymentLog.getPaymentPluginName());
				payment.setAmount(paymentLog.getAmount());
				payment.setOrderId(order.getId());
				orderService.payment(order, payment, null);
			}
			break;
		}
		paymentLog.setStatus(PaymentLog.Status.success.ordinal());
		super.update(paymentLog);
	}

	public PaymentLog save(PaymentLog paymentLog) {
		Assert.notNull(paymentLog);

		paymentLog.setSn(snDao.generate(Sn.Type.paymentLog));

		return super.save(paymentLog);
	}

}