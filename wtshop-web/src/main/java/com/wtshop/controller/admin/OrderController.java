package com.wtshop.controller.admin;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.wtshop.model.*;
import com.wtshop.service.*;
import org.apache.commons.lang3.StringUtils;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;
import com.wtshop.Message;
import com.wtshop.Pageable;
import com.wtshop.Setting;
import com.wtshop.entity.Invoice;
import com.wtshop.util.SystemUtils;

/**
 * Controller - 订单
 * 
 * 
 */
@ControllerBind(controllerKey = "/admin/order")
public class OrderController extends BaseController {

	private AdminService adminService = enhance(AdminService.class);
	private AreaService areaService = enhance(AreaService.class);
	private OrderService orderService = enhance(OrderService.class);
	private ShippingMethodService shippingMethodService = enhance(ShippingMethodService.class);
	private PaymentMethodService paymentMethodService = enhance(PaymentMethodService.class);
	private DeliveryCorpService deliveryCorpService = enhance(DeliveryCorpService.class);
	private ShippingService shippingService = enhance(ShippingService.class);
	private MemberService memberService = enhance(MemberService.class);
	private NodifyGoodsSendService nodifyGoodsSendService = enhance(NodifyGoodsSendService.class);

	/**
	 * 检查锁定
	 */
	public void checkLock() {
		Long id = getParaToLong("id");
		Order order = orderService.find(id);
		if (order == null) {
			renderJson(ERROR_MESSAGE);
			return;
		}
		Admin admin = adminService.getCurrent();
		if (orderService.isLocked(order, admin, true)) {
			renderJson(Message.warn("admin.order.locked"));
		}
		renderJson(SUCCESS_MESSAGE);
	}

	/**
	 * 计算
	 */
	public void calculate() {
		Long id = getParaToLong("id");
		BigDecimal freight = new BigDecimal(getPara("freight", "0"));
		BigDecimal tax = new BigDecimal(getPara("tax", "0"));
		BigDecimal offsetAmount = new BigDecimal(getPara("offsetAmount", "0"));
		Map<String, Object> data = new HashMap<String, Object>();
		Order order = orderService.find(id);
		if (order == null) {
			data.put("message", ERROR_MESSAGE);
			renderJson(data);
			return;
		}
		data.put("message", SUCCESS_MESSAGE);
		data.put("amount", orderService.calculateAmount(order.getPrice(), order.getFee(), freight, tax, order.getPromotionDiscount(), order.getCouponDiscount(), offsetAmount));
		renderJson(data);
	}

	/**
	 * 物流动态
	 */
	public void transitStep() {
		Long shippingId = getParaToLong("shippingId");
		Map<String, Object> data = new HashMap<String, Object>();
		Shipping shipping = shippingService.find(shippingId);
		if (shipping == null) {
			data.put("message", ERROR_MESSAGE);
			renderJson(data);
			return;
		}
		Setting setting = SystemUtils.getSetting();
		if (StringUtils.isEmpty(setting.getKuaidi100Key()) || StringUtils.isEmpty(shipping.getDeliveryCorpCode()) || StringUtils.isEmpty(shipping.getTrackingNo())) {
			data.put("message", ERROR_MESSAGE);
			renderJson(data);
			return;
		}
		data.put("message", SUCCESS_MESSAGE);
		data.put("transitSteps", shippingService.getTransitSteps(shipping));
		renderJson(data);
	}

	/**
	 * 编辑
	 */
	@Before(Tx.class)
	public void edit() {
		Long id = getParaToLong("id");
		Order order = orderService.find(id);
		if (order == null || order.hasExpired() || (!Order.Status.pendingPayment.equals(order.getStatusName()) && !Order.Status.pendingReview.equals(order.getStatusName()))) {
			redirect(ERROR_VIEW);
			return;
		}
		setAttr("paymentMethods", paymentMethodService.findAll());
		setAttr("shippingMethods", shippingMethodService.findAll());
		setAttr("order", order);
		render("/admin/order/edit.ftl");
	}

	/**
	 * 更新
	 */
	@Before(Tx.class)
	public void update() {
		Long id = getParaToLong("id");
		Long areaId = getParaToLong("areaId");
		Long paymentMethodId = getParaToLong("paymentMethodId");
		Long shippingMethodId = getParaToLong("shippingMethodId");
		BigDecimal freight = new BigDecimal(getPara("freight", "0"));
		BigDecimal tax = new BigDecimal(getPara("tax", "0"));
		BigDecimal offsetAmount = new BigDecimal(getPara("offsetAmount", "0"));
		Long rewardPoint = getParaToLong("rewardPoint");
		String consignee = getPara("consignee");
		String address = getPara("address");
		String zipCode = getPara("zipCode");
		String phone = getPara("phone");
		String invoiceTitle = getPara("invoiceTitle");
		String memo = getPara("memo");
		Area area = areaService.find(areaId);
		PaymentMethod paymentMethod = paymentMethodService.find(paymentMethodId);
		ShippingMethod shippingMethod = shippingMethodService.find(shippingMethodId);

		Order order = orderService.find(id);
		if (order == null || order.hasExpired() || (!Order.Status.pendingPayment.equals(order.getStatusName()) && !Order.Status.pendingReview.equals(order.getStatusName()))) {
			redirect("ERROR_VIEW");
		}
		Invoice invoice = StringUtils.isNotEmpty(invoiceTitle) ? new Invoice(invoiceTitle, null) : null;
		order.setTax(invoice != null ? tax : BigDecimal.ZERO);
		order.setOffsetAmount(offsetAmount);
		order.setRewardPoint(rewardPoint);
		order.setMemo(memo);
		order.setInvoice(invoice);
		order.setPaymentMethod(paymentMethod);
		if (order.getIsDelivery()) {
			order.setFreight(freight);
			order.setConsignee(consignee);
			order.setAddress(address);
			order.setZipCode(zipCode);
			order.setPhone(phone);
			order.setArea(area);
			order.setShippingMethod(shippingMethod);
		} else {
			order.setFreight(BigDecimal.ZERO);
			order.setConsignee(null);
			order.setAreaName(null);
			order.setAddress(null);
			order.setZipCode(null);
			order.setPhone(null);
			order.setShippingMethodName(null);
			order.setArea(null);
			order.setShippingMethod(null);
		}

		Admin admin = adminService.getCurrent();
		if (orderService.isLocked(order, admin, true)) {
			addFlashMessage(new com.wtshop.Message(com.wtshop.Message.Type.error,"该订单被锁定"));
			redirect("list.jhtml");
			return;
		}
		orderService.update(order, admin);

		addFlashMessage(SUCCESS_MESSAGE);
		redirect("list.jhtml");
	}

	/**
	 * 查看
	 */
	public void view() {
		Long id = getParaToLong("id");
		Setting setting = SystemUtils.getSetting();
		setAttr("methods", Payment.Method.values());
		setAttr("refundsMethods", Refunds.Method.values());
		setAttr("paymentMethods", paymentMethodService.findAll());
		setAttr("shippingMethods", shippingMethodService.findAll());
		setAttr("deliveryCorps", deliveryCorpService.findAll());
		setAttr("isKuaidi100Enabled", StringUtils.isNotEmpty(setting.getKuaidi100Key()));
		setAttr("area", areaService.findAll());
		Order order = orderService.find(id);
		setAttr("order", order);
		render("/admin/order/view.ftl");
	}

	/**
	 * 查看团购订单选择
	 */
	public void chooseOrder() {
		Pageable pageable = getBean(Pageable.class);
		Long id = getParaToLong("flag");
		setAttr("pageable", pageable);
		setAttr("page", orderService.findByfightgroupId(id, pageable));
		render("/admin/fightGroup/choose.ftl");
	}


	/**
	 * 审核
	 */
	public void review() {
		Long id = getParaToLong("id");
		Boolean passed = getParaToBoolean("passed", false);
		
		Order order = orderService.find(id);
		if (order == null || order.hasExpired() || !Order.Status.pendingReview.equals(order.getStatusName())) {
			addFlashMessage(new com.wtshop.Message(com.wtshop.Message.Type.error,"订单已过期"));
			redirect("list.jhtml");
			return;
		}
		Admin admin = adminService.getCurrent();
		if (orderService.isLocked(order, admin, true)) {
			addFlashMessage(new com.wtshop.Message(com.wtshop.Message.Type.error,"订单锁定中"));
			redirect("list.jhtml");
			return;
		}
		orderService.review(order, passed, admin);
		addFlashMessage(SUCCESS_MESSAGE);
		redirect("view.jhtml?id=" + id);
	}

	/**
	 * 收款
	 */
	public void payment() {
		Payment payment = getModel(Payment.class);
		Long orderId = getParaToLong("orderId");
		Long paymentMethodId = getParaToLong("paymentMethodId");
		
		String methodName = getPara("method");
		Payment.Method method = StrKit.notBlank(methodName) ? Payment.Method.valueOf(methodName) : null;
		if (method != null) {
			payment.setMethod(method.ordinal());
		}
		
		Order order = orderService.find(orderId);
		if (order == null) {
			addFlashMessage(new com.wtshop.Message(com.wtshop.Message.Type.error,"订单不存在"));
			redirect("list.jhtml");
			return;
		}
		payment.setOrderId(order.getId());
		payment.setPaymentMethod(paymentMethodService.find(paymentMethodId));
		
		Admin admin = adminService.getCurrent();
		if (orderService.isLocked(order, admin, true)) {
			addFlashMessage(new com.wtshop.Message(com.wtshop.Message.Type.error,"订单锁定中"));
			redirect("list.jhtml");
			return;
		}
		Member member = order.getMember();
		if (Payment.Method.deposit.equals(payment.getMethod()) && payment.getAmount().compareTo(member.getBalance()) > 0) {
			addFlashMessage(new com.wtshop.Message(com.wtshop.Message.Type.error,"收款金额有误"));
			redirect("list.jhtml");
			return;
		}
		payment.setOperator(admin);
		orderService.payment(order, payment, admin);
		addFlashMessage(SUCCESS_MESSAGE);
		redirect("view.jhtml?id=" + orderId);
	}

	/**
	 * 退款
	 */
	public void refunds() {
		Refunds refunds = getModel(Refunds.class);
		Long orderId = getParaToLong("orderId");
		Long paymentMethodId = getParaToLong("paymentMethodId");
		
		String methodName = getPara("method");
		Payment.Method method = StrKit.notBlank(methodName) ? Payment.Method.valueOf(methodName) : null;

		
		Order order = orderService.find(orderId);
		if (order == null || order.getRefundableAmount().compareTo(BigDecimal.ZERO) <= 0) {
			addFlashMessage(new com.wtshop.Message(com.wtshop.Message.Type.error,"退款金额有误"));
			redirect("list.jhtml");
			return;
		}
		refunds.setOrderId(order.getId());
		
		Admin admin = adminService.getCurrent();
		if (orderService.isLocked(order, admin, true)) {
			addFlashMessage(new com.wtshop.Message(com.wtshop.Message.Type.error,"订单锁定中"));
			redirect("list.jhtml");
			return;
		}
		refunds.setOperator(admin);
		addFlashMessage(SUCCESS_MESSAGE);
		redirect("view.jhtml?id=" + orderId);
	}

	/**
	 * 发货
	 */
	@Before(Tx.class)
	public void shipping() {
		Shipping shipping = getModel(Shipping.class);
		Long orderId = getParaToLong("orderId");
		Long shippingMethodId = getParaToLong("shippingMethodId");
		Long deliveryCorpId = getParaToLong("deliveryCorpId");
		Long areaId = getParaToLong("areaId");
		
		Order order = orderService.find(orderId);
		if (order == null || order.getShippableQuantity() <= 0) {
			addFlashMessage(new com.wtshop.Message(com.wtshop.Message.Type.error,"可发货数有误"));
			redirect("/admin/order/list.jhtml");
			return;
		}
		NodifyGoodsSend nodifyGoodsSend = nodifyGoodsSendService.findByOrderId(orderId);
		if(nodifyGoodsSend != null ){
			nodifyGoodsSend.setStatus(true);
			nodifyGoodsSend.update();
		}


		List<ShippingItem> shippingItems = getBeans(ShippingItem.class, "shippingItems");
		shipping.setShippingItems(shippingItems);
		
		boolean isDelivery = false; // 是否交货
		for (Iterator<ShippingItem> iterator = shipping.getShippingItems().iterator(); iterator.hasNext();) {
			ShippingItem shippingItem = iterator.next();
			if (shippingItem == null || StringUtils.isEmpty(shippingItem.getSn()) || shippingItem.getQuantity() == null || shippingItem.getQuantity() <= 0) {
				iterator.remove();
				continue;
			}
			OrderItem orderItem = order.getOrderItem(shippingItem.getSn());
			Product product = orderItem.getProduct();
			if (product != null && shippingItem.getQuantity() > product.getStock()) {
				redirect(ERROR_VIEW);
				return;
			}
			shippingItem.setName(orderItem.getName());
			shippingItem.setIsDelivery(orderItem.getIsDelivery());
			shippingItem.setProductId(product.getId());
			shippingItem.setShipping(shipping);
			shippingItem.setSpecifications(orderItem.getSpecifications());
			if (orderItem.getIsDelivery()) {
				isDelivery = true;
			}
		}
		shipping.setOrderId(order.getId());
		shipping.setShippingMethod(shippingMethodService.find(shippingMethodId));
		shipping.setDeliveryCorp(deliveryCorpService.find(deliveryCorpId));
		shipping.setArea(areaService.find(areaId));
		if (!isDelivery) {
			shipping.setShippingMethod((String) null);
			shipping.setDeliveryCorp((String) null);
			shipping.setDeliveryCorpUrl(null);
			shipping.setDeliveryCorpCode(null);
			shipping.setTrackingNo(null);
			shipping.setFreight(null);
			shipping.setConsignee(null);
			shipping.setArea((String) null);
			shipping.setAddress(null);
			shipping.setZipCode(null);
			shipping.setPhone(null);
		}

		Admin admin = adminService.getCurrent();
		if (orderService.isLocked(order, admin, true)) {
			addFlashMessage(new com.wtshop.Message(com.wtshop.Message.Type.error,"订单锁定中 "));
			redirect("/admin/order/list.jhtml");
			return;
		}
		shipping.setOperator(admin);
		orderService.shipping(order, shipping, admin);
		addFlashMessage(SUCCESS_MESSAGE);
		redirect("/admin/order/list.jhtml");
	}

	/**
	 * 退货
	 */
	public void returns() {
		Returns returns = getModel(Returns.class);
		Long orderId = getParaToLong("orderId");
		Long shippingMethodId = getParaToLong("shippingMethodId");
		Long deliveryCorpId = getParaToLong("deliveryCorpId");
		Long areaId = getParaToLong("areaId");
		
		Order order = orderService.find(orderId);
		if (order == null || order.getReturnableQuantity() <= 0) {
			addFlashMessage(new com.wtshop.Message(com.wtshop.Message.Type.error,"可退货数有误"));
			redirect("list.jhtml");
			return;
		}
		List<ReturnsItem> returnsItems = getBeans(ReturnsItem.class, "returnsItems");
		returns.setReturnsItems(returnsItems);
		
		for (Iterator<ReturnsItem> iterator = returns.getReturnsItems().iterator(); iterator.hasNext();) {
			ReturnsItem returnsItem = iterator.next();
			if (returnsItem == null || StringUtils.isEmpty(returnsItem.getSn()) || returnsItem.getQuantity() == null || returnsItem.getQuantity() <= 0) {
				iterator.remove();
				continue;
			}
			OrderItem orderItem = order.getOrderItem(returnsItem.getSn());
			if (orderItem == null || returnsItem.getQuantity() > orderItem.getReturnableQuantity()) {
				redirect(ERROR_VIEW);
				return;
			}
			returnsItem.setName(orderItem.getName());
			returnsItem.setReturns(returns);
			returnsItem.setSpecifications(orderItem.getSpecifications());
		}
		returns.setOrderId(order.getId());
		returns.setShippingMethod(shippingMethodService.find(shippingMethodId));
		returns.setDeliveryCorp(deliveryCorpService.find(deliveryCorpId));
		returns.setArea(areaService.find(areaId));
		
		Admin admin = adminService.getCurrent();
		if (orderService.isLocked(order, admin, true)) {
			addFlashMessage(new com.wtshop.Message(com.wtshop.Message.Type.error,"订单锁定中"));
			redirect("list.jhtml");
			return;
		}
		returns.setOperator(admin);
		orderService.returns(order, returns, admin);
		addFlashMessage(SUCCESS_MESSAGE);
		redirect("view.jhtml?id=" + orderId);
	}

	/**
	 * 收货
	 */
	public void receive() {
		Long id = getParaToLong("id");
		Order order = orderService.find(id);
		if (order == null || order.hasExpired() || !Order.Status.shipped.equals(order.getStatusName())) {
			addFlashMessage(new com.wtshop.Message(com.wtshop.Message.Type.error,"订单不存在或者已过期"));
			redirect("list.jhtml");
			return;
		}
		Admin admin = adminService.getCurrent();
		if (orderService.isLocked(order, admin, true)) {
			addFlashMessage(new com.wtshop.Message(com.wtshop.Message.Type.error,"订单锁定中"));
			redirect("list.jhtml");
			return;
		}
		orderService.receive(order, admin);
		addFlashMessage(SUCCESS_MESSAGE);
		redirect("view.jhtml?id=" + id);
	}

	/**
	 * 完成
	 */
	public void complete() {
		Long id = getParaToLong("id");
		Order order = orderService.find(id);
		if (order == null || order.hasExpired() || !Order.Status.received.equals(order.getStatusName())) {
			addFlashMessage(new com.wtshop.Message(com.wtshop.Message.Type.error,"订单不存在或者已过期"));
			redirect("list.jhtml");
			return;
		}
		Admin admin = adminService.getCurrent();
		if (orderService.isLocked(order, admin, true)) {
			addFlashMessage(new com.wtshop.Message(com.wtshop.Message.Type.error,"订单锁定中"));
			redirect("list.jhtml");
			return;
		}
		orderService.complete(order, admin);
		addFlashMessage(SUCCESS_MESSAGE);
		redirect("view.jhtml?id=" + id);
	}

	/**
	 * 失败
	 */
	public void fail() {
		Long id = getParaToLong("id");
		Order order = orderService.find(id);
		if (order == null || order.hasExpired() || (!Order.Status.pendingShipment.equals(order.getStatusName()) && !Order.Status.shipped.equals(order.getStatusName()) && !Order.Status.received.equals(order.getStatusName()))) {
			redirect(ERROR_VIEW);
			return;
		}
		Admin admin = adminService.getCurrent();
		if (orderService.isLocked(order, admin, true)) {
			addFlashMessage(new com.wtshop.Message(com.wtshop.Message.Type.error,"订单锁定中"));
			redirect("list.jhtml");
			return;
		}
		orderService.fail(order, admin);
		addFlashMessage(SUCCESS_MESSAGE);
		redirect("view.jhtml?id=" + id);
	}

	/**
	 * 列表
	 */
	public void list() {
		String typeName = getPara("type");
		Order.Type type = StrKit.notBlank(typeName) ? Order.Type.valueOf(typeName) : null;
		
		String statusName = getPara("status");
		Order.Status status = StrKit.notBlank(statusName) ? Order.Status.valueOf(statusName) : null;

		orderService.updateExperce(null);
		
		String memberUsername = getPara("memberUsername");
		Boolean isPendingReceive = getParaToBoolean("isPendingReceive");
		Boolean isPendingRefunds = getParaToBoolean("isPendingRefunds");
		Boolean isAllocatedStock = getParaToBoolean("isAllocatedStock");
		Boolean hasExpired = getParaToBoolean("hasExpired");
		Pageable pageable = getBean(Pageable.class);

		setAttr("types", Order.Type.values());
		setAttr("statuses", Order.Status.values());
		setAttr("type", type);
		setAttr("status", status);
		setAttr("memberUsername", memberUsername);
		setAttr("isPendingReceive", isPendingReceive);
		setAttr("isPendingRefunds", isPendingRefunds);
		setAttr("isAllocatedStock", isAllocatedStock);
		setAttr("hasExpired", hasExpired);
		setAttr("pageable", pageable);

		Member member = memberService.findByUsername(memberUsername);
		if (StringUtils.isNotEmpty(memberUsername) && member == null) {
			setAttr("page", "");
		} else {
			setAttr("page", orderService.findPage(type, status, member, null, isPendingReceive, isPendingRefunds, null, null, isAllocatedStock, hasExpired, pageable));
		}
		render("/admin/order/list.ftl");
	}

	/**
	 * 删除
	 */
	public void delete() {
		Long[] ids = getParaValuesToLong("ids");
//		if (ids != null) {
//			Admin admin = adminService.getCurrent();
//			for (Long id : ids) {
//				Order order = orderService.find(id);
//				if (order != null && orderService.isLocked(order, admin, true)) {
//					renderJson(Message.error("admin.order.deleteLockedNotAllowed", order.getSn()));
//					return;
//				}
//			}
//			orderService.delete(ids);
//		}
		for(Long id : ids){
			Order order = orderService.find(id);
			order.setIsDelete(true);
			orderService.update(order);
		}
		renderJson(SUCCESS_MESSAGE);
	}

}