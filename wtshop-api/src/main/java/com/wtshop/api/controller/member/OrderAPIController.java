package com.wtshop.api.controller.member;

import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.i18n.I18n;
import com.jfinal.i18n.Res;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.wtshop.Pageable;
import com.wtshop.Setting;
import com.wtshop.api.common.result.PriceResult;
import com.wtshop.api.common.result.member.OrderFindResult;
import com.wtshop.api.interceptor.TokenInterceptor;
import com.wtshop.util.ApiResult;
import com.wtshop.api.common.result.OrderGoods;
import com.wtshop.api.common.result.TrackResult;
import com.wtshop.api.common.result.member.OrderTrackResult;
import com.wtshop.api.controller.BaseAPIController;
import com.wtshop.api.interceptor.ErrorInterceptor;
import com.wtshop.interceptor.WapInterceptor;
import com.wtshop.model.*;
import com.wtshop.service.*;
import com.wtshop.util.MathUtil;
import com.wtshop.util.SystemUtils;

import java.math.BigDecimal;
import java.util.*;


/**
 * Controller - 会员中心 - 订单
 * 
 * 
 */
@ControllerBind(controllerKey = "/api/member/order")
@Before({WapInterceptor.class, ErrorInterceptor.class})
public class OrderAPIController extends BaseAPIController {

	/** 每页记录数 */
	private static final int PAGE_SIZE = 10;

	private MemberService memberService = enhance(MemberService.class);
	private GoodsService goodsService = enhance(GoodsService.class);
	private OrderItemService orderItemService = enhance(OrderItemService.class);
	private ShippingService shippingService = enhance(ShippingService.class);
	private OrderService orderService = enhance(OrderService.class);
	private AreaDescribeService areaDescribeService = enhance(AreaDescribeService.class);
	private AreaService areaService = enhance(AreaService.class);
	private PromotionService promotionService = enhance(PromotionService.class);
	private ReturnsService returnsService = enhance(ReturnsService.class);
	private GoodsPromotionService goodsPromotionService = enhance(GoodsPromotionService.class);
	private NodifyGoodsSendService nodifyGoodsSendService = enhance(NodifyGoodsSendService.class);
	private Res resZh = I18n.use();
	
	/**
	 * 列表
	 * {"msg":"","code":1,"data":{"page":{"totalRow":1,"pageNumber":1,"firstPage":true,"lastPage":true,"totalPage":1,"pageSize":10,"list":[{"address":"中南海1号","amount":70.000000,"amount_paid":0.000000,"area_id":null,"area_name":"北京市昌平区","complete_date":null,"consignee":"史强","coupon_code_id":null,"coupon_discount":0.000000,"create_date":"2017-05-24 14:42:25","exchange_point":0,"expire":"2017-05-25 14:42:25","fee":0.000000,"freight":0.000000,"id":82,"invoice_content":null,"invoice_title":null,"is_allocated_stock":false,"is_exchange_point":false,"is_use_coupon_code":false,"lock_expire":"2017-05-24 14:43:25","lock_key":"b16f3025929c413e27fb4fd39a4f46ee","member_id":18,"memo":"","modify_date":"2017-05-24 14:42:25","offset_amount":0.000000,"payment_method_id":1,"payment_method_name":"网上支付","payment_method_type":0,"phone":"13581856711","price":70.000000,"promotion_discount":0.000000,"promotion_names":"[]","quantity":1,"refund_amount":0.000000,"returned_quantity":0,"reward_point":10,"shipped_quantity":0,"shipping_method_id":1,"shipping_method_name":"普通快递","sn":"20170524404","status":0,"tax":0.000000,"type":0,"version":0,"weight":1000,"zip_code":"100000"}]},"status":"all"}}
	 */
	public void list() {
		Integer pageNumber = getParaToInt("pageNumbers");
		String statusName = getPara("statuss");
		Integer type = getParaToInt("type");
		Order.Status status = StrKit.notBlank(statusName) ? Order.Status.valueOf(statusName) : null;
		Member member = memberService.getCurrent();
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		//更新过期订单
		orderService.updateExperce(member);
		Page<Order> page = orderService.findPages( status, member ,pageable ,type);

		List<Order> orderList = page.getList();
		List<OrderGoods> orderGoodss = new ArrayList<>();
		for(Order order : orderList){
			//todo 退款订单显示问题 暂时这样修改
			if(order.getStatus() == 11){
				order.setStatus(110);
			}

			List<Goods> goodsList = goodsService.findGoodsByItemId(order.getId());
			if (order.getType()==Order.Type.daopai.ordinal()){
					//获取倒拍单价

			}
			OrderGoods orderGoods = new OrderGoods(goodsList, order);
			orderGoodss.add(orderGoods);
		}

		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("PageNumber",page.getPageNumber());
		hashMap.put("TotalPage",page.getTotalPage());
		hashMap.put("TotalRow",page.getTotalRow());
		hashMap.put("PageSize",page.getPageSize());
		hashMap.put("orderGoodss",orderGoodss);
		renderJson(ApiResult.success(hashMap));

	}


	/**
	 * 提醒发货
	 */
	public void send(){
		String sn = getPara("sns");
		Order order = orderService.findBySn(sn);
		NodifyGoodsSend nodifyGoodsSends = nodifyGoodsSendService.findByOrderId(order.getId());
		if(nodifyGoodsSends != null){
			nodifyGoodsSendService.update(nodifyGoodsSends);
		}else {
			NodifyGoodsSend nodifyGoodsSend = new NodifyGoodsSend();
			nodifyGoodsSend.setOrderId(order.getId());
			nodifyGoodsSend.setStatus(false);
			nodifyGoodsSend.setMemberId(memberService.getCurrent().getId());
			nodifyGoodsSendService.save(nodifyGoodsSend);
		}

		renderJson(ApiResult.success("提醒成功!"));
	}

	/**
	 * 查看
	 * {"msg":"","code":1,"data":{"key":false,"order":{"address":"中南海1号","amount":70.000000,"amount_paid":0.000000,"area_id":null,"area_name":"北京市昌平区","complete_date":null,"consignee":"史强","coupon_code_id":null,"coupon_discount":0.000000,"create_date":"2017-05-24 14:42:25","exchange_point":0,"expire":"2017-05-25 14:42:25","fee":0.000000,"freight":0.000000,"id":82,"invoice_content":null,"invoice_title":null,"is_allocated_stock":false,"is_exchange_point":false,"is_use_coupon_code":false,"lock_expire":"2017-05-24 14:43:25","lock_key":"b16f3025929c413e27fb4fd39a4f46ee","member_id":18,"memo":"","modify_date":"2017-05-24 14:42:25","offset_amount":0.000000,"payment_method_id":1,"payment_method_name":"网上支付","payment_method_type":0,"phone":"13581856711","price":70.000000,"promotion_discount":0.000000,"promotion_names":"[]","quantity":1,"refund_amount":0.000000,"returned_quantity":0,"reward_point":10,"shipped_quantity":0,"shipping_method_id":1,"shipping_method_name":"普通快递","sn":"20170524404","status":0,"tax":0.000000,"type":0,"version":0,"weight":1000,"zip_code":"100000"}}}
	 */
	public void view() {

		String sn = getPara("sns");
		Order order = orderService.findBySn(sn);
		if (order == null) {
            renderJson(ApiResult.fail("请输入正确的订单号!"));
            return;
		}
		Member member = memberService.getCurrent();
		if (!member.getId().equals(order.getMemberId()) ) {
            renderJson(ApiResult.fail("当前用户与订单用户不符!"));
            return;
		}

		List<Goods> goodsList = goodsService.findGoodsByOrderId(order.getId());
		Boolean is_promotion = false;
		for(Goods goods : goodsList){
			GoodsPromotion goodsId = goodsPromotionService.findPromitByGoodsId(goods.getId());
			if(goodsId != null){
				is_promotion = true;
				break;
			}
		}

		//查询是否可以退换货
		Returns returnsBySn = returnsService.findReturnsByOrderId(order.getId());
		Boolean returns = true;
		if( returnsBySn != null){
			returns = false;
		}

		List<OrderItem> orderItemList = orderItemService.findOrderItemList(order.getId());
		Long time = 0L;
		Long expire = 0L;
		if(order.getStatus() == 0){
			time = Calendar.getInstance().getTimeInMillis();
			expire = order.getExpire().getTime();
		}
		Shipping shipping = shippingService.findBySn(sn);
		Receiver receiver = new Receiver();
		receiver.setAddress(order.getAddress());
		receiver.setAreaName(order.getAreaName());
		receiver.setConsignee(order.getConsignee());
		receiver.setPhone(order.getPhone());
		receiver.setConsignee(receiver.getConsignee());

        Setting setting = SystemUtils.getSetting();
        String taxUrl = setting.getTaxExplainUrl();
        String returnUrl = setting.getReturnCopyUrl();

		//商品配送
		String receiveTime = null;

		AreaDescribe areaDescribe = areaDescribeService.findByAreaId(order.getAreaId());
		//判断本级地区是否填写
		if(areaDescribe != null && areaDescribe.getReceivingBegintime() != null){
			receiveTime = areaDescribe.getReceivingBegintime();
		}else {
			AreaDescribe areaDescribes = areaDescribeService.findByAreaId(areaService.find(order.getAreaId()).getParentId());
			if(areaDescribes != null){
				receiveTime = areaDescribes.getReceivingBegintime();
			}

		}
		Double oldPrice = orderService.getOldPrice(order);
		PriceResult oldTotalPrice = new PriceResult("商品优惠前总金额","¥ "+ MathUtil.getInt(oldPrice.toString()));
		PriceResult deliveryPrice = new PriceResult("运费","¥ "+ MathUtil.getInt( order.getFee().toString()));
		String couponMoney = MathUtil.getInt(order.getFreight().add(order.getMiaobiPaid()).add(new BigDecimal(oldPrice).subtract(order.getPrice())).toString());

		PriceResult newDeliveryPrice = new PriceResult("运费优惠金额","-¥ "+  MathUtil.getInt( order.getFreight().toString()));

		PriceResult totalPrice = new PriceResult("商品总金额","¥ "+ MathUtil.getInt(order.getPrice().toString()));

		PriceResult miaobiPrice = new PriceResult("喵币","-¥ "+ MathUtil.getInt(order.getMiaobiPaid().toString()));
		if(order.getType() == Order.Type.miaobi.ordinal()){
			couponMoney = "0";
			miaobiPrice = new PriceResult("喵币","-¥ " + couponMoney);
			oldTotalPrice = new PriceResult("商品优惠前总金额","¥ "+ MathUtil.getInt(order.getAmount().toString()));
			totalPrice = new PriceResult("商品优惠前总金额","¥ "+ MathUtil.getInt(order.getAmount().toString()));
		}
		PriceResult manjianPrice =null;
		double v = order.getPromotionDiscount().doubleValue();
		Promotion promotion = promotionService.find(5L);
		if(v > 0){
			manjianPrice = new PriceResult(promotion.getTitle(),"-"+MathUtil.getInt(promotion.getMoney().toString()));
			couponMoney = MathUtil.getInt(new BigDecimal(couponMoney).add(promotion.getMoney()).toString());
		}

		List<PriceResult> priceList = new ArrayList<>();
		priceList.add(oldTotalPrice);
		priceList.add(totalPrice);
		priceList.add(deliveryPrice);
		if(!is_promotion){
			priceList.add(miaobiPrice);
		}

		priceList.add(newDeliveryPrice);
		priceList.add(manjianPrice);

        String realMoney = MathUtil.getInt(order.getAmount().toString());
		OrderFindResult orderFindResult = new OrderFindResult(taxUrl, returnUrl, time, expire, order, shipping, orderItemList, member, receiver, receiveTime, priceList, realMoney, couponMoney, returns);
		renderJson(ApiResult.success(orderFindResult));
	}
	
	/**
	 * 取消
	 * {"msg":"请求成功","code":1,"data":null}
	 */
	public void cancel() {
		String sn = getPara("sns");
		String cause= getPara("cause");
		String desc = getPara("desc");
		String url = getPara("url");

		Order order = orderService.findBySn(sn);
		if (order == null) {
			renderJson(ApiResult.fail("订单未找到!"));
			return;
		}
		Member member = memberService.getCurrent();
		if (!member.equals(order.getMember())) {
			renderJson(ApiResult.fail("订单创建人与当前用户不同!"));
			return;
		}
		if (order.hasExpired() || ((!Order.Status.pendingPayment.equals(order.getStatusName()) && !Order.Status.pendingReview.equals(order.getStatusName())) && !Order.Status.pendingShipment.equals(order.getStatusName()) && !Order.Status.shipped.equals(order.getStatusName()))) {
			renderJson(ApiResult.fail("订单过期或状态非等待付款!"));
			return;
		}
		if (orderService.isLocked(order, member, true)) {
			renderJson(ApiResult.fail(resZh.format("shop.member.order.locked")));

		}

			orderService.cancel(order ,cause, desc, url);





		renderJson(ApiResult.success());
	}
	
	/**
	 * 物流跟踪
	 */
	public void track() {
		String sn = getPara("sns");
		Shipping shipping = shippingService.findBySn(sn);
		renderJson(ApiResult.success(shipping));
	}
	
	/**
	 * 确认收货
	 * {"msg":"请求成功","code":1,"data":null}
	 */
	public void receive() {
		String sn = getPara("sns");
		Order order = orderService.findBySn(sn);
		if (order == null) {
			renderJson(ApiResult.fail("订单未找到!"));
			return;
		}
		Member member = memberService.getCurrent();
		if (!member.equals(order.getMember())) {
			renderJson(ApiResult.fail("订单创建人与当前用户不同!"));
			return;
		}
		if (order.hasExpired() || !Order.Status.shipped.equals(order.getStatusName())) {
			renderJson(ApiResult.fail("订单过期或状态非已发货!"));
			return;
		}
		if (orderService.isLocked(order, member, true)) {
			renderJson(ApiResult.fail( resZh.format("shop.member.order.locked")));

		}
		orderService.receive(order, null);
		renderJson(ApiResult.success());
	}
	/**
	 * 删除订单
	 */
	public void delete(){
		String sn = getPara("sns");
		Order order = orderService.findBySn(sn);
		if (order == null) {
			renderJson(ApiResult.fail("订单未找到!"));
			return;
		}
		Member member = memberService.getCurrent();
		if (!member.equals(order.getMember())) {
			renderJson(ApiResult.fail("订单创建人与当前用户不同!"));
			return;
		}
		if (order.hasExpired() || (!Order.Status.completed.equals(order.getStatusName()) && !Order.Status.failed.equals(order.getStatusName()) && !Order.Status.canceled.equals(order.getStatusName()) && !Order.Status.denied.equals(order.getStatusName()))){
			renderJson(ApiResult.fail("订单不允许删除"));
			return;
		}
		order.setIsDelete(true);
		orderService.update(order);
		renderJson(ApiResult.success("订单删除成功!"));
	}
}
