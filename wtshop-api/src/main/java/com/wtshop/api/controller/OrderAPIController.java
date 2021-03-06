package com.wtshop.api.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.aop.Enhancer;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.i18n.I18n;
import com.jfinal.i18n.Res;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;
import com.wtshop.Pageable;
import com.wtshop.api.common.result.*;
import com.wtshop.api.interceptor.ErrorInterceptor;
import com.wtshop.entity.SpecificationValue;
import com.wtshop.interceptor.WapInterceptor;
import com.wtshop.model.*;
import com.wtshop.service.*;
import com.wtshop.util.RedisUtil;
import com.wtshop.util.ApiResult;
import com.wtshop.util.MathUtil;
import com.wtshop.util.RedisUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.nlpcn.commons.lang.util.StringUtil;

import java.math.BigDecimal;
import java.util.*;


/**
 * Controller - 订单
 * 
 * 
 */
@ControllerBind(controllerKey = "/api/order")
@Before({WapInterceptor.class, ErrorInterceptor.class})
public class OrderAPIController extends BaseAPIController {

	private CartService cartService = enhance(CartService.class);
	private GoodsService goodsService = enhance(GoodsService.class);
	private ProductService productService = enhance(ProductService.class);
	private MemberService memberService = enhance(MemberService.class);
	private ReceiverService receiverService = enhance(ReceiverService.class);
	private OrderService orderService = enhance(OrderService.class);
	private CouponService couponService = enhance(CouponService.class);
	private ShippingMethodService shippingMethodService = enhance(ShippingMethodService.class);
	private CertificatesService certificatesService= enhance(CertificatesService.class);
	private AreaDescribeService areaDescribeService = enhance(AreaDescribeService.class);
	private MiaoBiGoodsService miaoBiGoodsService = enhance(MiaoBiGoodsService.class);
	private AreaService areaService = enhance(AreaService.class);
	private PromotionService promotionService = enhance(PromotionService.class);
	private GoodsPromotionService goodsPromotionService = enhance(GoodsPromotionService.class);
	private  FightGroupService fightGroupService= Enhancer.enhance(FightGroupService.class);
	private CartItemService cartItemService =Enhancer.enhance(CartItemService.class);
	private IdentifierService identifierService =Enhancer.enhance(IdentifierService.class);
	private Res resZh = I18n.use();
	private SpecialCouponService specialCouponService=Enhancer.enhance(SpecialCouponService.class);

	private GroupBuyService groupBuyService = enhance(GroupBuyService.class);
	/** 每页记录数 */
	private static final int PAGE_SIZE = 10;


	/**
	 * 立即购买接口
	 */
	@Before(Tx.class)
	public void buyNow() {

		Member member = memberService.getCurrent();


		if(com.wtshop.util.StringUtils.isEmpty(member.getOnShareCode())){
			renderJson(ApiResult.fail(7,"请填写邀请码"));
			return;
		}
		Boolean isUseMiao = getParaToBoolean("isUseMiao",false);
		Boolean isIntegral = getParaToBoolean("isIntegral",false);
		//获取商品和数量
		Long productId = getParaToLong("productId");
		Long sPecialIds = getParaToLong("sPecialIds",0l);
		String  sPecialCoupId = getPara("sPecialCoupId","0");

		Identifier identifier=new Identifier();
		if(sPecialIds!=0){
			List<Identifier> dddd = identifierService.findByMemberId(member.getId());
			for (Identifier identifier1:dddd) {
				if(identifier1.getStatus()==1){
					List<Member> m = memberService.findByShareCode(identifier1.getShareCode());
					identifier=identifier1;
					identifier.put("store",m.get(0).getStore());
					break;
				}
			}


		}


		Long quantity = getParaToLong("quantity",1L);
		Product product = productService.find(productId);
		Goods goods = goodsService.findGoodsByPro(productId);

		//product.setAllocatedStock();
				if (  quantity > product.getAvailableStock()) {
					renderJson(ApiResult.fail(resZh.format("shop.cart.productLowStock")));
					return;
				}




		if (!product.getIsMarketable()) {
			renderJson(ApiResult.fail(resZh.format("shop.cart.productNotMarketable")));
			return;
		}



		if(sPecialIds!=0){
			isUseMiao=false;
		}
	//	product.setAllocatedStock();

		//获取商品价格
		Double price = MathUtil.multiply(product.getPrice(), quantity);

		//判断商品是否是促销商品
		Boolean is_promotion = false;
		if(product !=null ){
			GoodsPromotion goodsPromotion = goodsPromotionService.findPromitByGoodsId(product.getGoodsId());
			if(goodsPromotion != null){
				is_promotion = true;
			}
		}

		//获取默认的收货地址
		Receiver defaultReceiver = receiverService.findDefault(member);

		//商品配送
		String receiveTime = "";
		if(defaultReceiver != null){
			AreaDescribe areaDescribe = areaDescribeService.findByAreaId(goods.getAreaId());

			//判断本级地区是否填写
			if(areaDescribe != null && areaDescribe.getReceivingBegintime() != null){
				receiveTime = areaDescribe.getReceivingBegintime();
			}else {
				AreaDescribe areaDescribes = areaDescribeService.findByAreaId(areaService.find(goods.getAreaId()).getParentId());
				if(areaDescribes != null){
					receiveTime = areaDescribes.getReceivingBegintime();
				}

			}
		}
		//商品运费
		List<ShippingMethod> shippingMethods = shippingMethodService.findMethodList();
		double value = shippingMethodService.calculateFreight(shippingMethods.get(0), defaultReceiver, goods.getWeight() * 1).doubleValue();
		Delivery delivery = new Delivery(shippingMethods.get(0).getId(), shippingMethods.get(0).getName(), value);


		JSONObject redisSetting = JSONObject.parseObject(RedisUtil.getString("redisSetting"));
/*
		//是否包邮
		Boolean is_freeMoney = redisSetting.getBoolean("isFreeMoney") || price >= redisSetting.getDouble("freeMoney") ? true : false;

		Double deliver = 0d;
		deliver = delivery.getPrice();
		//运费优惠金额
		Double couponYunfei =0d;
		PriceResult newDeliveryPrice = new PriceResult("运费优惠金额","0" );
		if(is_freeMoney){
			 //运费
			couponYunfei = delivery.getPrice();
			newDeliveryPrice = new PriceResult("运费优惠金额","-¥"+MathUtil.getInt(delivery.getPrice().toString()));
		}*/
		//包税 地址
		String taxUrl = "http://shop.rxmao.cn/rxm/goods/tax.html";

		//是否可以使用喵币
		Boolean is_useMiaobi = redisSetting.getBoolean("isUseMiaoBi") ? true : false;
		//我的喵币个数
		Double myMiaoBi =  member.getPoint().doubleValue();



		//可用喵币
		Double useMiaoBi = 0d;
		//喵币抵扣金额
		Double miaoBiPrice = 0d;

		//代金券抵扣抵扣金额
		Double specialCouponPrice = 0d;
		String miaoBiDesc = "";
		//我的积分个数
		Double myIntegral =  member.getIntegral().doubleValue();
		//可用积分
		Double useIntegral = 0d;
		//积分抵扣金额
		Double integralPrice = 0d;
		String	integralDesc= "";
		Double integralScale =  redisSetting.getDouble("integralScale");
		Double integralLimit =  redisSetting.getDouble("integralLimit");
		Double scale =  redisSetting.getDouble("scale");
		Double miaoBiLimit =  redisSetting.getDouble("miaoBiLimit");


		//计算可以积分和喵币
		Double	useMiaoBi1= myMiaoBi;
		Double	miaoBiPrice1= new BigDecimal(useMiaoBi1).divide(new BigDecimal(scale)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		double multiply1 = new BigDecimal(price).multiply(new BigDecimal(miaoBiLimit)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		if(miaoBiPrice1 > multiply1){
			useMiaoBi1 = new BigDecimal(multiply1).multiply(new BigDecimal(scale)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			miaoBiPrice1= new BigDecimal(useMiaoBi1).divide(new BigDecimal(scale)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		}

		if(is_useMiaobi &&isUseMiao &&myMiaoBi >= 0){
			useMiaoBi = myMiaoBi;

			miaoBiPrice= new BigDecimal(useMiaoBi).divide(new BigDecimal(scale)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			double multiply = new BigDecimal(price).multiply(new BigDecimal(miaoBiLimit)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			//重新计算可用喵币
			if(miaoBiPrice > multiply){
				useMiaoBi = new BigDecimal(multiply).multiply(new BigDecimal(scale)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				miaoBiPrice= new BigDecimal(useMiaoBi).divide(new BigDecimal(scale)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			}

		}





		Double	useIntegral1 = myIntegral;
		Double	integralPrice1= new BigDecimal(useIntegral1).divide(new BigDecimal(integralScale)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		double multiply2 = new BigDecimal(price).multiply(new BigDecimal(integralLimit)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		if(integralPrice1 > multiply2){
			useIntegral1 = new BigDecimal(multiply2).multiply(new BigDecimal(integralScale)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			integralPrice1= new BigDecimal(useIntegral1).divide(new BigDecimal(integralScale)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		}
		if(isIntegral&&myIntegral>=0){
			useIntegral = myIntegral;
			integralPrice= new BigDecimal(useIntegral).divide(new BigDecimal(integralScale)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			double multiply = new BigDecimal(price).multiply(new BigDecimal(integralLimit)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			//重新计算可用喵币
			if(integralPrice > multiply){
				useIntegral = new BigDecimal(multiply).multiply(new BigDecimal(integralScale)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				integralPrice= new BigDecimal(useIntegral).divide(new BigDecimal(integralScale)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			}

		}
		PriceResult integralPriceP = null;
		Double integral = 0d;
		//代金卡信息暂存到sPecialResultList
		List<PriceResult> sPecialResultList = new ArrayList<>();
		if(!sPecialCoupId.equals(0)){
			List<SpecialCoupon> sPecialCouponList = specialCouponService.findBySpecialCids(sPecialCoupId);
			if(sPecialCouponList!=null){
				for (SpecialCoupon sPecialCoupon:sPecialCouponList){
					if(sPecialCoupon.getMemberId().equals(member.getId())&&sPecialCoupon.getStatus()==1){

						specialCouponPrice=specialCouponPrice+sPecialCoupon.getMoney().doubleValue();


					}else {
						renderJson(ApiResult.fail(9,"代金卡异常"));
						return;
					}
					PriceResult specialcouponPrice = new PriceResult("代金卡("+sPecialCoupon.getCode()+")", "-¥ " + MathUtil.getInt(sPecialCoupon.getMoney().toString()));
					sPecialResultList.add(specialcouponPrice);
				}
			}



		}
		PriceResult miaobiPrice = null;

		String realPrice = null;
		String favoritePrice = "0";

		//商品金额
		Double realPriced =price;
		Double favoreatePriced = 0d;
		Double miaobi = 0d;

		Double returns = 0d;



		//计算是否包邮金额
		Double dashabi = MathUtil.subtract(MathUtil.subtract(realPriced, miaoBiPrice), integralPrice);

		//是否包邮
		Boolean is_freeMoney = redisSetting.getBoolean("isFreeMoney") || dashabi >= redisSetting.getDouble("freeMoney")|| specialCouponPrice>0? true : false;

		Double deliver = 0d;
		deliver = delivery.getPrice();
		//运费优惠金额
		Double couponYunfei =0d;
		PriceResult newDeliveryPrice = new PriceResult("运费优惠金额","-¥ 0" );
		if(is_freeMoney|| specialCouponPrice>0){
			//运费优惠运费
			couponYunfei = delivery.getPrice();
			newDeliveryPrice = new PriceResult("运费优惠金额","-¥"+MathUtil.getInt(delivery.getPrice().toString()));
		}




		// 代金券提示及优惠增加
	//	specialcouponPrice = new PriceResult("代金卡","-¥ "+MathUtil.getInt(specialCouponPrice.toString()));
		favoreatePriced = MathUtil.add(specialCouponPrice,couponYunfei);

		if(isUseMiao){
			miaobiPrice = new PriceResult("喵币","-¥ "+MathUtil.getInt(miaoBiPrice.toString()));
			//优惠钱数
			favoreatePriced = MathUtil.add(miaoBiPrice,favoreatePriced);
			miaobi = miaoBiPrice;
		}else {
			miaobiPrice = new PriceResult("喵币","-¥ "+0);
			favoreatePriced = MathUtil.add(miaoBiPrice,favoreatePriced);
			miaobi = 0d;
		}
		if(isIntegral){
			integralPriceP = new PriceResult("积分","-¥ "+MathUtil.getInt(integralPrice.toString()));
			//优惠钱数
			favoreatePriced = MathUtil.add(integralPrice,favoreatePriced);
			favoritePrice = MathUtil.getInt(favoreatePriced.toString());

			integral = integralPrice;
		}else {
			integralPriceP = new PriceResult("积分","-¥ "+0);
			favoreatePriced = MathUtil.add(integralPrice,favoreatePriced);
			favoritePrice =  MathUtil.getInt(favoreatePriced.toString());
			integral = 0d;
		}









		PriceResult manjianPrice = null;
		Promotion promotion = promotionService.find(5L);
		Double manJianPrices = 0d;
		if(is_promotion){
			if ( price >= promotion.getTotalMoney().doubleValue()){
				manjianPrice = new PriceResult(promotion.getTitle(),"-¥ "+MathUtil.getInt(promotion.getMoney().toString()));
				favoritePrice = MathUtil.getInt(new BigDecimal(favoritePrice).add(promotion.getMoney()).toString());
				manJianPrices = promotion.getMoney().doubleValue();
			}
		}

		//优惠前总金额
		Double marketPrice = MathUtil.multiply(product.getMarketPrice(), quantity);
		PriceResult totalPrice = new PriceResult("商品总金额","¥ "+ MathUtil.getInt(price.toString()));
		PriceResult oldTotalPrice = new PriceResult("商品优惠前总金额","¥ "+ MathUtil.getInt(marketPrice.toString()));
		PriceResult deliveryPrice = new PriceResult("运费","¥ "+ MathUtil.getInt(delivery.getPrice().toString()));
		List<PriceResult> priceList = new ArrayList<>();
		priceList.add(oldTotalPrice);
		priceList.add(totalPrice);
		priceList.add(deliveryPrice);

		if(!is_promotion&&sPecialIds==0&&miaobi>0){
			priceList.add(miaobiPrice);
		}
		if(isIntegral){
			priceList.add(integralPriceP);
		}
		//是否显示代金券
		if(sPecialResultList.size()>0){
			priceList.addAll(sPecialResultList);
		}
		priceList.add(newDeliveryPrice);
		priceList.add(manjianPrice);

		//运费
		Double yunfei=  delivery.getPrice();
		/*//运费优惠金额
		Double couponYunfei = MathUtil.subtract(delivery.getPrice() ,deliver);*/
		//支付金额
		//Double amountpaid = MathUtil.subtract(MathUtil.subtract(realPriced ,couponYunfei),miaoBiPrice);


		Double amountpaid =MathUtil.add(MathUtil.subtract(MathUtil.subtract(realPriced ,miaoBiPrice),favoreatePriced),yunfei);
		if(amountpaid<0){
			amountpaid=0d;
		}
		//优惠总额
		String couponPrice = MathUtil.getInt(new BigDecimal(marketPrice).add(new BigDecimal(delivery.getPrice())).subtract(new BigDecimal(amountpaid)).setScale(2, BigDecimal.ROUND_HALF_UP).toString());

		/*if(amountpaid < 0){
			miaobi = miaobi + amountpaid;
			useMiaoBi = new BigDecimal(miaobi).multiply(new BigDecimal(scale)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			miaoBiPrice= new BigDecimal(useMiaoBi).divide(new BigDecimal(scale)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			amountpaid = 0d;
		}*/
		//喵币描述
		miaoBiDesc ="共" + MathUtil.getInt(member.getPoint().toString()) + "喵币,可用" + MathUtil.getInt(useMiaoBi1.toString()) + "喵币,抵扣¥" + MathUtil.getInt(miaoBiPrice1.toString()) ;
		integralDesc ="共" + MathUtil.getInt(member.getIntegral().toString()) + "积分,可用" + MathUtil.getInt(useIntegral1.toString()) + "积分,抵扣¥" + MathUtil.getInt(integralPrice1.toString()) ;

		Double[] param = {deliver, miaobi, amountpaid ,couponYunfei ,manJianPrices,specialCouponPrice,useIntegral,integralPrice};
		String params = deliver.toString() + "," + miaobi.toString() + "," +amountpaid.toString() + ","
				+couponYunfei.toString() + "," +manJianPrices.toString()+","+specialCouponPrice.toString()+","+useIntegral.toString() +","+integralPrice;

			realPrice =  MathUtil.getInt(amountpaid.toString());
		goods.setPrice(product.getPrice());


		goods.put("special_id",sPecialIds);
		List<String> specifications = product.getSpecifications();


		OrderBuyNowResult orderBuyNowResult = new OrderBuyNowResult(taxUrl, yunfei, member, defaultReceiver, goods, Integer.valueOf(quantity+""), receiveTime, is_freeMoney, is_useMiaobi,isUseMiao, miaoBiDesc, priceList,
			realPrice, couponPrice, param, is_promotion, amountpaid,specifications,identifier,isIntegral,integralDesc);

		RedisUtil.setString("ORDERPARAM:"+member.getId(), params);
		renderJson(ApiResult.success(orderBuyNowResult));

	}


	/**
	 *
	 * 立即购买后 创建订单并支付商品-创建
	 */
	@Before(Tx.class)
	public void createByNowOrder() {
		Member member = memberService.getCurrent();
		Long receiverId = getParaToLong("receiverId"); //收货人
		Long quantity  = getParaToLong("quantity",1L); //收货人
		Boolean isPersonal=getParaToBoolean("isPersonal");  //

		//特殊商品
		Long sPecialIds = getParaToLong("sPecialIds",0l);

		String identifierIdString = getPara("identifierId","0");
		if("undefined".equals(identifierIdString)){
			renderJson(new ApiResult(0,"请选择门店!"));
			return;
		}
		Long identifierId =Long.parseLong(identifierIdString);
				//Long identifierId = getParaToLong("identifierId",0l);

		//代金券id
		String  sPecialCoupId = getPara("sPecialCoupId","0");




		//1是 ，0否  是否開發票
		Boolean isInvoice=getParaToBoolean("isInvoice");
		//1是 ，0否  是否是個人发票还是单位发票

		String taxNumber = getPara("taxNumber"); 	//單位名稱
		String companyName = getPara("companyName"); 	//稅號


		Receiver receiver = receiverService.find(receiverId);
		Long goodsId = getParaToLong("goodsId");//商品

		Goods goods = goodsService.find(goodsId);
		String memo = getPara("memo"); 	//备注

		//是否实名认证
		Certificates certificates = certificatesService.queryByMemberId(member.getId());
		if(certificates != null && certificates.getState() != 1){
			renderJson(new ApiResult(101,"请先进行实名认证!"));
			return;
		}
		Double price =0d;

	 price = MathUtil.multiply(goods.getPrice(), quantity);


		String[] values = StringUtils.split(RedisUtil.getString("ORDERPARAM:" + member.getId()), ",");
		Double[] skuids = values == null ? null :convertToDouble(values);

		//快递费用
		Double deliveryMoney = skuids[0];

		//喵币支付
		Double miaobiMoney = skuids[1];

		//总支付金额
		Double amountMoney = skuids[2];

		//运费优惠金额
		Double couponYunfei = skuids[3];

		//满减金额
		Double manjianPrice = skuids[4];

		//代金卡
		//代金券抵扣抵扣金额

		Double specialCouponPrice = skuids[5];
		Double integral= skuids[6];
		Double integralMoney= skuids[7];
		Order.Type ss = Order.Type.general;
if(!"0".equals(sPecialIds)){
	if(identifierId==0&&"0".equals(sPecialCoupId)&&integralMoney>0){
		ss = Order.Type.particular;
	}else if(identifierId!=0&&"0".equals(sPecialCoupId)&&integralMoney==0){



		ss = Order.Type.special;
	}else if(identifierId==0&&!"0".equals(sPecialCoupId)&&integralMoney==0){
		ss = Order.Type.coupon;
	}else if(identifierId==0&&"0".equals(sPecialCoupId)&&integralMoney==0){
		ss = Order.Type.particular;
	}else {
		renderJson(ApiResult.fail("订单异常"));
	}
}


		//佣金比例
		Double rate = goods.getCommissionRate();

		Order order = orderService.createBuyNow(ss, member, goods, price, Integer.valueOf(quantity+""), manjianPrice, receiver, amountMoney, deliveryMoney , miaobiMoney, memo, couponYunfei,isInvoice,isPersonal,taxNumber,companyName,null,0,0,rate,sPecialIds,identifierId,specialCouponPrice,sPecialCoupId,integralMoney,integral);


		renderJson(ApiResult.success(order.getId()));
	}

	/**
	 * 团购立即购买 调用接口
	 */
	@Before(Tx.class)
	public void buyNowTuangou() {
		Member member = memberService.getCurrent();
		//是否是单购  1是  0否
		boolean isSinglepurchase= getParaToBoolean("isSinglepurchase");
		//是否是团长
		Long fightGroupId = getParaToLong("fightGroupId");
        Long tuanGouId = getParaToLong("tuanGouId");
		GroupBuy groupBuy = groupBuyService.find(tuanGouId);
		if(com.wtshop.util.StringUtils.isEmpty(member.getOnShareCode())){
			renderJson(ApiResult.fail(7,"请填写邀请码"));
			return;
		}
		if(com.wtshop.util.StringUtils.isEmpty(member.getShareCode())){
		List<Order> order = orderService.findBytuanGouIdmemberId(tuanGouId, member.getId());

		if(!isSinglepurchase&&(order.size()>0&&order.get(0).getFightgroupId()!=null)){
			FightGroup fightGroup = fightGroupService.find(order.get(0).getFightgroupId());
			//	List<Order> order = orderService.findByfightgroupIdmemberId(tuanGouId, member.getId());

			if(order.size()>=2){
				if(fightGroupId==0){

						renderJson(ApiResult.fail("非掌柜只能有一次发团的机会 请看团购玩法"));
						return;

				}else {
						renderJson(ApiResult.fail("非掌柜只能有一次参团的机会 请看团购玩法"));
						return;
					}


			}else if(order.size()==1){
				if(fightGroupId==0){
					if(fightGroup.getMemberId().longValue()==member.getId().longValue()){
						renderJson(ApiResult.fail("非掌柜只能有一次发团的机会 请看团购玩法"));
						return;
					}
				}else {
					if(!fightGroup.getMemberId().equals(member.getId())){
						renderJson(ApiResult.fail("非掌柜只能有一次参团的机会 请看团购玩法"));
						return;
					}
				}



			}
			if(groupBuy.getNum()!=0&&order.size()>=groupBuy.getNum()){
				renderJson(ApiResult.fail("此活动每人只限购买"+groupBuy.getNum()+"次"));
				return;
			}
		}
	}







		Product product = groupBuy.getProduct();

        PriceResult totalPrice = new PriceResult("商品总金额","¥ "+ MathUtil.getInt(groupBuy.getUniprice().toString()));
		Double price = groupBuy.getUniprice();
        if(isSinglepurchase){
            totalPrice = new PriceResult("商品总金额","¥ "+ MathUtil.getInt(groupBuy.getUniprice().toString()));
			price=groupBuy.getUniprice();
        }else {
            totalPrice = new PriceResult("商品总金额","¥ "+ MathUtil.getInt(groupBuy.getPrice().toString()));
			price=groupBuy.getPrice();
        }

		Goods goods = goodsService.findGoodsByPro(product.getId());
		if (!product.getIsMarketable()) {
			renderJson(ApiResult.fail(resZh.format("shop.cart.productNotMarketable")));
			return;
		}


		Boolean isUseMiao = getParaToBoolean("isUseMiao",false);

		//获取商品价格
	//	Double price = MathUtil.multiply(goods.getPrice(), 1);


		//判断商品是否是促销商品
		Boolean is_promotion = false;


		//获取默认的收货地址
		Receiver defaultReceiver = receiverService.findDefault(member);

		//商品配送
		String receiveTime = "";
		if(defaultReceiver != null){
			AreaDescribe areaDescribe = areaDescribeService.findByAreaId(goods.getAreaId());
			//判断本级地区是否填写
			if(areaDescribe != null && areaDescribe.getReceivingBegintime() != null){
				receiveTime = areaDescribe.getReceivingBegintime();
			}else {
				AreaDescribe areaDescribes = areaDescribeService.findByAreaId(areaService.find(goods.getAreaId()).getParentId());
				if(areaDescribes != null){
					receiveTime = areaDescribes.getReceivingBegintime();
				}

			}
		}

		//商品运费
		List<ShippingMethod> shippingMethods = shippingMethodService.findMethodList();
		double value =groupBuy.getDispatchprice()==null?0:groupBuy.getDispatchprice();
		Delivery delivery = new Delivery(shippingMethods.get(0).getId(), shippingMethods.get(0).getName(), value);


		JSONObject redisSetting = JSONObject.parseObject(RedisUtil.getString("redisSetting"));




		//是否包邮
		Boolean is_freeMoney = (groupBuy.getDispatchprice()==null||groupBuy.getDispatchprice()==0) ? true : false;

		Double deliver = 0d;
		deliver = delivery.getPrice();
		//运费优惠金额

		Double couponYunfei =0d;
		PriceResult newDeliveryPrice = new PriceResult("运费优惠金额","0" );
		if(is_freeMoney){
			//运费
			couponYunfei = delivery.getPrice();
			newDeliveryPrice = new PriceResult("运费优惠金额",MathUtil.getInt(delivery.getPrice().toString()));
		}
		//包税 地址
		String taxUrl = "http://shop.rxmao.cn/rxm/goods/tax.html";

		//是否可以使用喵币
		Boolean is_useMiaobi =  false;
		//我的喵币个数
		Double myMiaoBi =  member.getPoint().doubleValue();
		//可用喵币
		Double useMiaoBi = 0d;
		//抵扣金额
		Double miaoBiPrice = 0d;
		String miaoBiDesc = "";
		Double scale =  redisSetting.getDouble("scale");


		PriceResult miaobiPrice = null;
		String realPrice = null;
		String favoritePrice = "0";
		Double realPriced = MathUtil.add(price, delivery.getPrice());
		Double favoreatePriced = 0d;
		Double miaobi = 0d;
		Double returns = 0d;


			miaobiPrice = new PriceResult("喵币","-¥ "+0);
			favoritePrice =  MathUtil.getInt(newDeliveryPrice.getPrice());
			miaobi = 0d;

		PriceResult manjianPrice = null;
		Promotion promotion = promotionService.find(5L);
		Double manJianPrices = 0d;


		//优惠前总金额
		Double marketPrice = MathUtil.multiply(goods.getMarketPrice(), 1);


		PriceResult oldTotalPrice = new PriceResult("商品优惠前总金额","¥ "+MathUtil.getInt(price.toString()));
		PriceResult deliveryPrice = new PriceResult("运费","¥ "+ MathUtil.getInt(delivery.getPrice().toString()));
		List<PriceResult> priceList = new ArrayList<>();
		priceList.add(oldTotalPrice);
		priceList.add(totalPrice);
		priceList.add(deliveryPrice);


		priceList.add(newDeliveryPrice);
		priceList.add(manjianPrice);

		//运费
		Double yunfei=  delivery.getPrice();
		/*//运费优惠金额
		Double couponYunfei = MathUtil.subtract(delivery.getPrice() ,deliver);*/
		//支付金额
		Double amountpaid = MathUtil.subtract(realPriced ,favoritePrice);
		//优惠总额
		favoritePrice = MathUtil.getInt(new BigDecimal(favoritePrice).add(new BigDecimal(marketPrice)).subtract(new BigDecimal(price)).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
		//喵币描述

		if(amountpaid < 0){
			miaobi = miaobi + amountpaid;
			useMiaoBi = new BigDecimal(miaobi).multiply(new BigDecimal(scale)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			miaoBiPrice= new BigDecimal(useMiaoBi).divide(new BigDecimal(scale)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			amountpaid = 0d;
		}

		miaoBiDesc ="共" + MathUtil.getInt(member.getPoint().toString()) + "喵币,可用" + MathUtil.getInt(useMiaoBi.toString()) + "喵币,抵扣¥" + MathUtil.getInt(miaoBiPrice.toString()) ;

		Double[] param = {deliver, miaobi, amountpaid ,couponYunfei ,manJianPrices};
		String params = deliver.toString() + "," + miaobi.toString() + "," +amountpaid.toString() + "," +couponYunfei.toString() + "," +manJianPrices.toString() ;

		realPrice =  MathUtil.getInt(amountpaid.toString());
		OrderBuyNowTuanGouResult orderBuyNowResult = new OrderBuyNowTuanGouResult(taxUrl, yunfei, member, defaultReceiver, goods, 1, receiveTime, is_freeMoney, is_useMiaobi, miaoBiDesc, priceList,
				realPrice, favoritePrice, param, is_promotion, amountpaid,isSinglepurchase,fightGroupId,tuanGouId);
		RedisUtil.setString("ORDERPARAM:"+member.getId(), params);

 		renderJson(ApiResult.success(orderBuyNowResult));

	}
	/**
	 *
	 * 立即购买后 创建订单并支付商品-创建
	 */
	@Before(Tx.class)
	public void createByNowTuangouOrder() {

		Member member = memberService.getCurrent();
		Long receiverId = getParaToLong("receiverId"); //收货人

        //是否是单购  1是  0否
        boolean isSinglepurchase= getParaToBoolean("isSinglepurchase");
        //是否是团长
        Long fightGroupId = getParaToLong("fightGroupId");
        Long tuanGouId = getParaToLong("tuanGouId");



        if(fightGroupId!=0){
			FightGroup fightGroup = fightGroupService.find(fightGroupId);

			if(fightGroup.getCount()>=fightGroup.getGroupnum()){
				renderJson(ApiResult.fail("抱歉该团已经完成"));
				return;
			}
		}
		GroupBuy groupBuy = groupBuyService.find(tuanGouId);
		Double price = MathUtil.multiply(groupBuy.getUniprice(), 1);
if(!isSinglepurchase){
	 price = MathUtil.multiply(groupBuy.getPrice(), 1);
}


        Product product = groupBuy.getProduct();



		//1是 ，0否  是否開發票
		Boolean isInvoice=getParaToBoolean("isInvoice");
		//1是 ，0否  是否是個人
		Boolean isPersonal=getParaToBoolean("isPersonal");
		String taxNumber = getPara("taxNumber"); 	//單位名稱
		String companyName = getPara("companyName"); 	//稅號


		Receiver receiver = receiverService.find(receiverId);
		Long goodsId = getParaToLong("goodsId");//商品
		Goods goods = goodsService.find(goodsId);
		String memo = getPara("memo"); 	//备注

		//是否实名认证
		Certificates certificates = certificatesService.queryByMemberId(member.getId());

		if(certificates != null && certificates.getState() != 1){
			renderJson(new ApiResult(101,"请先进行实名认证!"));
			return;
		}


		String[] values = StringUtils.split(RedisUtil.getString("ORDERPARAM:" + member.getId()), ",");
		Double[] skuids = values == null ? null :convertToDouble(values);

		//快递费用
		Double deliveryMoney = skuids[0];

		//喵币
		Double miaobiMoney = skuids[1];

		//总支付金额
		Double amountMoney = skuids[2];

		//运费优惠金额
		Double couponYunfei = skuids[3];

		//满减金额
		Double manjianPrice = skuids[4];

		//佣金比例
		Double rate =groupBuy.getGroupRate();


		Order order = orderService.createBuyNow(Order.Type.group, member, goods, price, 1, manjianPrice, receiver, amountMoney, deliveryMoney , miaobiMoney
				, memo, couponYunfei,isInvoice,isPersonal,taxNumber,companyName,isSinglepurchase,fightGroupId,tuanGouId,rate,0l,0l,0d,"",0d,0d);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("order",order.getId()+"");
		map.put("sn",order.getSn()+"");
		map.put("rate",rate);
		map.put("amountMoney",amountMoney);
		renderJson(ApiResult.success(map));
	}
	/**
	 * 购物车结算接口
	 * 购物车普通订单-结算
	 * {"msg":"","code":1,"data":{"receiver":{"address":"中南海1号","area_id":12,"area_name":"北京市昌平区","consignee":"史强","create_date":"2017-05-24 14:42:02","id":14,"is_default":true,"member_id":18,"modify_date":"2017-05-24 14:42:02","phone":"13581856711","version":0,"zip_code":"100000"},"member":{"address":null,"amount":0E-12,"area_id":null,"attribute_value0":null,"attribute_value1":null,"attribute_value2":null,"attribute_value3":null,"attribute_value4":null,"attribute_value5":null,"attribute_value6":null,"attribute_value7":null,"attribute_value8":null,"attribute_value9":null,"avatar":null,"balance":0E-12,"birth":null,"create_date":"2017-05-09 15:40:29","email":"sq@163.com","gender":null,"id":18,"is_enabled":true,"is_locked":false,"lock_key":"b16f3025929c413e27fb4fd39a4f46ee","locked_date":null,"login_date":"2017-05-25 11:09:12","login_failure_count":0,"login_ip":"127.0.0.1","login_plugin_id":null,"member_rank_id":1,"mobile":null,"modify_date":"2017-05-25 11:09:12","name":null,"nickname":"sq","open_id":null,"password":"e10adc3949ba59abbe56e057f20f883e","phone":null,"point":0,"register_ip":"127.0.0.1","safe_key_expire":null,"safe_key_value":null,"username":"sq123","version":14,"zip_code":null},"paymentMethod":{"content":null,"create_date":"2015-10-19 00:58:54","description":"支持支付宝、财付通、快钱以及大多数网上银行支付","icon":"/4.0/201501/b0b6da31-6abf-4824-8dfa-c1f251732e20.gif","id":1,"method":0,"modify_date":"2015-10-19 00:58:54","name":"网上支付","orders":1,"timeout":1440,"type":0,"version":0},"invoice":{"title":"","content":"不开发票"},"title":"核对订单信息","url":"/api/order/checkout.jhtml?skuids=86","cart":{"cart_key":"8041fb81fccd462825b9b9a94f32c35f","create_date":"2017-05-23 15:58:57","expire":"2017-06-01","id":120,"member_id":18,"modify_date":"2017-05-24 14:42:25","version":1},"order":{"address":"中南海1号","amount":70.00,"amount_paid":0,"area_name":"北京市昌平区","consignee":"史强","coupon_discount":0,"exchange_point":0,"fee":0,"freight":0,"is_allocated_stock":false,"is_exchange_point":false,"is_use_coupon_code":false,"memo":null,"offset_amount":0,"payment_method_id":null,"phone":"13581856711","price":70.00,"promotion_discount":0.00,"promotion_names":"[]","quantity":1,"refund_amount":0,"returned_quantity":0,"reward_point":10,"shipped_quantity":0,"tax":0,"type":0,"weight":1000,"zip_code":"100000"},"token":"dda9efa110d25d8548b7c3ccdcb78cdb"}}
	 */
	@Before(Tx.class)
	public void checkout() {

		Member member = memberService.getCurrent();
		//product 的数组
		String[] values = StringUtils.split(getPara("cartTokens"), ",");
		String sPecialCoupId = getPara("sPecialCoupId","0");
		Boolean isIntegral = getParaToBoolean("isIntegral",false);
		Boolean isSPecialIds = getParaToBoolean("isSPecialIds",false);
		Identifier identifier=new Identifier();

		Long[] skuids = values == null ? null : convertToLong(values);
		/**
		 * 判断是否是特殊商品
		 */
		if(isSPecialIds){
			List<Identifier> dddd = identifierService.findByMemberId(member.getId());
	/*		if(dddd.size()==0){
				//if(member==null ||StringUtils.isEmpty(member.getOnShareCode())){
				renderJson(ApiResult.fail(8,"请绑定识别码"));
				return;
				//}
			}*/

			for (Identifier identifier1:dddd) {
				if(identifier1.getStatus()==1){
					List<Member> m = memberService.findByShareCode(identifier1.getShareCode());
					identifier=identifier1;
					identifier.put("store",m.get(0).getStore());
					break;
				}
			}
			Boolean isUseMiao =false;
			Boolean isReturnInsurans = false;
			List<CartItem> cartItemList = cartItemService.findList(skuids);
			Cart cart = cartService.getCurrent();

			//	List<Product> products = productService.findList(skuids);
			//更新购物车信息
			List<CartItem> cartItems = cart.getCartItems();
			if ((CollectionUtils.isNotEmpty(cartItems))) {
				List<CartItem> selectedItems = new ArrayList<CartItem>();
				for (CartItem cartItem : cartItems) {
					if (cartItemList.contains(cartItem)) {
						selectedItems.add(cartItem);
					}
				}
				cart.setCartItems(selectedItems);
			}
			List<CartItem> cartItems1 = cart.getCartItems();
			//返回商品信息
			List<Goods> goodsList = new ArrayList<Goods>();

			//现价支付总金额 购物项分类 用来筛选优惠券
			Double price = 0d;

			//原价支付总金额
			Double oldPrice = 0d;
			//购物项分类的id集合
			List<Long> categoryList = new ArrayList<>();

			//购物项产品的id集合
			List<Long> productList = new ArrayList<>();
			for (CartItem cartItem : cartItems1) {
				Goods goods = goodsService.findGoodsByCartItemId(cartItem.getId());
				Product product = productService.find(cartItem.getProductId());
				price += product.getPrice().multiply(new BigDecimal(cartItem.getQuantity())).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				oldPrice += product.getMarketPrice().multiply(new BigDecimal(cartItem.getQuantity())).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				if (!categoryList.contains(goods.getProductCategoryId())) {
					categoryList.add(goods.getProductCategoryId());
				}
				if (!productList.contains(product.getId())) {
					productList.add(product.getId());
				}
				goods.setAttributeValue10(product.getSpecificationValues());

				String sss = goods.getAttributeValue10();

				if (StringUtil.isNotBlank(sss)) {
					JSONArray specificationValueArrays = JSONArray.parseArray(sss);
					String dsb = "";
					for (int i = 0; i < specificationValueArrays.size(); i++) {
						SpecificationValue fffff = JSONObject.parseObject(specificationValueArrays.getString(i), SpecificationValue.class);
						if (i == 0) {
							dsb = dsb + fffff.getValue();
						} else {
							dsb = dsb + "," + fffff.getValue();
						}

					}
					goods.setAttributeValue11(dsb);

				}
				goods.put("special_id",cartItem.getSpecialId());

				goodsList.add(goods);
			}

			//筛选商品可用的优惠券
			//查询符合商品分类的优惠券
			List<Coupon> codeList =null;



			//获取默认的收货地址
			Receiver defaultReceiver = receiverService.findDefault(member);
			//	System.out.println("goodsList.size():" + goodsList.size());
			Goods goods = areaService.findMaxLong(goodsList);

			//商品配送
			String receiveTime = null;
			if (defaultReceiver != null) {
				AreaDescribe areaDescribe = areaDescribeService.findByAreaId(goods.getAreaId());
				//判断本级地区是否填写
				if (areaDescribe != null && areaDescribe.getReceivingBegintime() != null) {
					receiveTime = areaDescribe.getReceivingBegintime();
				} else {
					AreaDescribe areaDescribes = areaDescribeService.findByAreaId(areaService.find(goods.getAreaId()).getParentId());
					if (areaDescribes != null) {
						receiveTime = areaDescribes.getReceivingBegintime();
					}

				}
			}
			List<ShippingMethod> shippingMethods = shippingMethodService.findMethodList();
			double value = shippingMethodService.calculateFreight(shippingMethods.get(0), defaultReceiver, cart.getWeight()).doubleValue();
			Delivery delivery = new Delivery(shippingMethods.get(0).getId(), shippingMethods.get(0).getName(), value);


			JSONObject redisSetting = JSONObject.parseObject(RedisUtil.getString("redisSetting"));
			//是否包邮



			//包税 计算税金
			Double taxRate = redisSetting.getDouble("taxRate");
			Double tax = 0.0d;
			String taxUrl = "http://shop.rxmao.cn/rxm/goods/tax.html";


			//我的喵币个数
			//Double myMiaoBi = member.getPoint().doubleValue();



			//可用喵币
			Double useMiaoBi = 0d;
			//抵扣金额
			Double miaoBiPrice = 0d;
			String miaoBiDesc = "";
			Double scale = redisSetting.getDouble("scale");
			//退货保险
			Boolean isReturnInsurance = redisSetting.getBoolean("isReturnInsurance");
			Double returnMoney = 0d;
			String returnCopy = "";

			if (isReturnInsurance) {
				returnMoney = redisSetting.getDouble("returnMoney");
				returnCopy = redisSetting.getString("returnCopyUrl");
			}

			Cache actCache = Redis.use();
			actCache.set("CART:" + member.getId(), cart);


			String realPrice = null;
			String favoritePrice = null;
			Double favoreatePriced = 0d;
			Double realPriced = 0d;
			Double miaobi = 0d;
			Double returns = 0d;

			//我的积分个数
			Double myIntegral =  member.getIntegral().doubleValue();
			//可用积分
			Double useIntegral = 0d;
			//积分抵扣金额
			Double integralPrice = 0d;
			String	integralDesc= "";
			Double integralScale =  redisSetting.getDouble("integralScale");
			Double integralLimit =  redisSetting.getDouble("integralLimit");
			Double	useIntegral1 = myIntegral;
			Double	integralPrice1= new BigDecimal(useIntegral1).divide(new BigDecimal(integralScale)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			double multiply2 = new BigDecimal(price).multiply(new BigDecimal(integralLimit)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			if(integralPrice1 > multiply2){
				useIntegral1 = new BigDecimal(multiply2).multiply(new BigDecimal(integralScale)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				integralPrice1= new BigDecimal(useIntegral1).divide(new BigDecimal(integralScale)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			}
			if(isIntegral&&myIntegral>=0){
				useIntegral = myIntegral;
				integralPrice= new BigDecimal(useIntegral).divide(new BigDecimal(integralScale)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				double multiply = new BigDecimal(price).multiply(new BigDecimal(integralLimit)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				//重新计算可用喵币
				if(integralPrice > multiply){
					useIntegral = new BigDecimal(multiply).multiply(new BigDecimal(integralScale)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
					integralPrice= new BigDecimal(useIntegral).divide(new BigDecimal(integralScale)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				}

			}
			PriceResult integralPriceP = null;
			Double integral = 0d;
			if(isIntegral){
				integralPriceP = new PriceResult("积分","-¥ "+MathUtil.getInt(integralPrice.toString()));
				//优惠钱数
				favoreatePriced = MathUtil.add(integralPrice,favoreatePriced);
				//favoritePrice = MathUtil.getInt(favoreatePriced.toString());

				integral = integralPrice;
			}else {
				integralPriceP = new PriceResult("积分","-¥ "+0);
				favoreatePriced = MathUtil.add(integralPrice,favoreatePriced);
			// favoritePrice =  MathUtil.getInt(favoreatePriced.toString());
				integral = 0d;
			}





			realPriced = MathUtil.add(price, delivery.getPrice());
			realPrice = MathUtil.getInt(realPriced.toString());

			returns = 0d;

			miaobi = 0d;

			PriceResult manjianPrice = null;

			Double manJianPrices = 0d;

			PriceResult	specialcouponPrice = null;
			PriceResult totalPrice = new PriceResult("商品总金额", "¥ " + MathUtil.getInt(price.toString()));
			PriceResult oldTotalPrice = new PriceResult("商品优惠前总金额", "¥ " + MathUtil.getInt(oldPrice.toString()));
			PriceResult deliveryPrice = new PriceResult("运费", "¥ " + MathUtil.getInt(delivery.getPrice().toString()));
			Double specialCouponPrice=0d;
			//代金卡信息暂存到sPecialResultList
			List<PriceResult> sPecialResultList = new ArrayList<>();
			if(!sPecialCoupId.equals(0)){
				List<SpecialCoupon> sPecialCouponList = specialCouponService.findBySpecialCids(sPecialCoupId);
				if(sPecialCouponList!=null){
					for (SpecialCoupon sPecialCoupon:sPecialCouponList){
						if(sPecialCoupon.getMemberId().equals(member.getId())&&sPecialCoupon.getStatus()==1){

							specialCouponPrice=specialCouponPrice+sPecialCoupon.getMoney().doubleValue();


						}else {
							renderJson(ApiResult.fail(9,"代金卡异常"));
							return;
						}
						PriceResult specialcouponPrice1 = new PriceResult("代金卡("+sPecialCoupon.getCode()+")", "-¥ " + MathUtil.getInt(sPecialCoupon.getMoney().toString()));
						sPecialResultList.add(specialcouponPrice1);
					}
				}



			}
			//计算是否包邮金额
			Double dashabi = MathUtil.subtract(MathUtil.subtract(realPriced, miaoBiPrice), integralPrice);

			//是否包邮
			Boolean is_freeMoney = redisSetting.getBoolean("isFreeMoney") || dashabi >= redisSetting.getDouble("freeMoney")|| specialCouponPrice>0? true : false;

			Double couponYunfei =0d;
			PriceResult newDeliveryPrice = new PriceResult("运费优惠金额", "-¥ " + 0);
			//运费
			Double deliver = delivery.getPrice();

			if (is_freeMoney) {
				//deliver = delivery.getPrice();
				//运费
				couponYunfei = delivery.getPrice();
				newDeliveryPrice = new PriceResult("运费优惠金额", "-¥ " + MathUtil.getInt(delivery.getPrice().toString()));
			}
			List<PriceResult> priceList = new ArrayList<>();

			//旧购物车金额
			priceList.add(oldTotalPrice);
			//新购物车金额
			priceList.add(totalPrice);
			//运费
			priceList.add(deliveryPrice);
			//优惠运费
			priceList.add(newDeliveryPrice);
			if(isIntegral){
				priceList.add(integralPriceP);
			}
			//代金卡
			//是否显示代金券
			if(sPecialResultList.size()>0){
				priceList.addAll(sPecialResultList);
			}
			priceList.add(specialcouponPrice);
			priceList.add(manjianPrice);
			//优惠运费金额
		//	Double couponYunfei = MathUtil.subtract(delivery.getPrice(), deliver);

			favoreatePriced =MathUtil.add(favoreatePriced,couponYunfei);
			favoritePrice = MathUtil.getInt(favoreatePriced.toString());

			Double amountpaid =  MathUtil.subtract(MathUtil.subtract(realPrice, favoritePrice),specialCouponPrice);
			if (amountpaid < 0) {
				miaobi = miaobi + amountpaid;
				useMiaoBi = new BigDecimal(miaobi).multiply(new BigDecimal(scale)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				miaoBiPrice = new BigDecimal(useMiaoBi).divide(new BigDecimal(scale)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				amountpaid = 0d;
			}
			miaoBiDesc = "共" + MathUtil.getInt(member.getPoint().toString()) + "喵币,可用" + MathUtil.getInt(useMiaoBi.toString()) + "喵币,抵扣¥" + MathUtil.getInt(miaoBiPrice.toString());
			integralDesc ="共" + MathUtil.getInt(member.getIntegral().toString()) + "积分,可用" + MathUtil.getInt(useIntegral1.toString()) + "积分,抵扣¥" + MathUtil.getInt(integralPrice1.toString()) ;
			//优惠总额
			String couponPrice = MathUtil.getInt(new BigDecimal(oldPrice).add(new BigDecimal(delivery.getPrice())).subtract(new BigDecimal(amountpaid)).setScale(2, BigDecimal.ROUND_HALF_UP).toString());

				Double[] param = {deliver, miaobi, returns, amountpaid, couponYunfei, manJianPrices,specialCouponPrice,useIntegral,integralPrice,};
			Double yunfei = delivery.getPrice();
			Boolean is_useMiaobi =false;
			Boolean is_promotion =false;
		//	favoritePrice = MathUtil.getInt(new BigDecimal(favoritePrice).add(new BigDecimal(oldPrice)).subtract(new BigDecimal(price)).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
			OrderDetailsResult orderDetailsResult = new OrderDetailsResult(taxUrl, couponYunfei, skuids, yunfei, member, cart.getToken(), defaultReceiver, codeList, goodsList,
					receiveTime, is_freeMoney, is_useMiaobi,isUseMiao, miaoBiDesc, isReturnInsurance, returns, returnCopy, priceList, amountpaid.toString(), couponPrice, param, is_promotion,identifier,isIntegral,integralDesc);
			renderJson(ApiResult.success(orderDetailsResult));











		}else {




	Boolean isUseMiao = getParaToBoolean("isUseMiao", false);
	Boolean isReturnInsurans = false;

	List<Product> products = productService.findList(skuids);

	//判断商品是否是促销商品
	Boolean is_promotion = false;
	if (products != null && products.size() > 0) {
		for (Product product : products) {
			GoodsPromotion goods = goodsPromotionService.findPromitByGoodsId(product.getGoodsId());
			if (goods != null) {
				is_promotion = true;
				break;
			}
		}
	}

	Cart cart = cartService.getCurrent();

//		if (cart == null || cart.isEmpty()) {
//			Cache actCache = Redis.use();
//			cart = actCache.get("CART:"+ member.getId());
//		}

	//更新购物车信息
	List<CartItem> cartItems = cart.getCartItems();
	if (CollectionUtils.isNotEmpty(products) && (CollectionUtils.isNotEmpty(cartItems))) {
		List<CartItem> selectedItems = new ArrayList<CartItem>();
		for (CartItem cartItem : cartItems) {
			if (products.contains(cartItem.getProduct())) {
				if(cartItem.getSpecialId()==0){
					selectedItems.add(cartItem);
				}

			}
		}
		cart.setCartItems(selectedItems);
	}
	List<CartItem> cartItems1 = cart.getCartItems();


	//返回商品信息
	List<Goods> goodsList = new ArrayList<Goods>();

	//现价支付总金额 购物项分类 用来筛选优惠券
	Double price = 0d;

	//原价支付总金额
	Double oldPrice = 0d;
	//购物项分类的id集合
	List<Long> categoryList = new ArrayList<>();
	//购物项产品的id集合
	List<Long> productList = new ArrayList<>();
	for (CartItem cartItem : cartItems1) {
		Goods goods = goodsService.findGoodsByCartItemId(cartItem.getId());
		Product product = productService.find(cartItem.getProductId());
		price += product.getPrice().multiply(new BigDecimal(cartItem.getQuantity())).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		oldPrice += product.getMarketPrice().multiply(new BigDecimal(cartItem.getQuantity())).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		if (!categoryList.contains(goods.getProductCategoryId())) {
			categoryList.add(goods.getProductCategoryId());
		}
		if (!productList.contains(product.getId())) {
			productList.add(product.getId());
		}
		goods.setAttributeValue10(product.getSpecificationValues());

		String sss = goods.getAttributeValue10();

		if (StringUtil.isNotBlank(sss)) {
			JSONArray specificationValueArrays = JSONArray.parseArray(sss);
			String dsb = "";
			for (int i = 0; i < specificationValueArrays.size(); i++) {
				SpecificationValue fffff = JSONObject.parseObject(specificationValueArrays.getString(i), SpecificationValue.class);
				if (i == 0) {
					dsb = dsb + fffff.getValue();
				} else {
					dsb = dsb + "," + fffff.getValue();
				}

			}
			goods.setAttributeValue11(dsb);

		}
		goods.put("special_id",cartItem.getSpecialId());

		goodsList.add(goods);
	}

	//筛选商品可用的优惠券
	//查询符合商品分类的优惠券
	List<Coupon> codeList = couponService.findCodeList(member.getId(), price, categoryList);
	if (productList != null && productList.size() == 1) {
		//查询符合专属商品的优惠券
		List<Coupon> codeLists = couponService.findCodeLists(member.getId(), price, productList);
		if (codeLists != null && codeLists.size() > 0) {
			for (Coupon coupon : codeLists) {
				codeList.add(coupon);
			}
		}
	}


	//获取默认的收货地址
	Receiver defaultReceiver = receiverService.findDefault(member);
	System.out.println("goodsList.size():" + goodsList.size());
	Goods goods = areaService.findMaxLong(goodsList);

	//商品配送
	String receiveTime = null;
	if (defaultReceiver != null) {
		AreaDescribe areaDescribe = areaDescribeService.findByAreaId(goods.getAreaId());
		//判断本级地区是否填写
		if (areaDescribe != null && areaDescribe.getReceivingBegintime() != null) {
			receiveTime = areaDescribe.getReceivingBegintime();
		} else {
			AreaDescribe areaDescribes = areaDescribeService.findByAreaId(areaService.find(goods.getAreaId()).getParentId());
			if (areaDescribes != null) {
				receiveTime = areaDescribes.getReceivingBegintime();
			}

		}
	}
	List<ShippingMethod> shippingMethods = shippingMethodService.findMethodList();
	double value = shippingMethodService.calculateFreight(shippingMethods.get(0), defaultReceiver, cart.getWeight()).doubleValue();
	Delivery delivery = new Delivery(shippingMethods.get(0).getId(), shippingMethods.get(0).getName(), value);


	JSONObject redisSetting = JSONObject.parseObject(RedisUtil.getString("redisSetting"));
	//是否包邮

	Boolean is_freeMoney = redisSetting.getBoolean("isFreeMoney") || price >= redisSetting.getDouble("freeMoney") ? true : false;
	//运费优惠金额
	Double deliver = 0d;
	PriceResult newDeliveryPrice = new PriceResult("运费优惠金额", "-¥ " + 0);
	if (is_freeMoney) {
		deliver = delivery.getPrice();
		newDeliveryPrice = new PriceResult("运费优惠金额", "-¥ " + MathUtil.getInt(delivery.getPrice().toString()));
	}
	//包税 计算税金
	Double taxRate = redisSetting.getDouble("taxRate");
	Double tax = 0.0d;
	String taxUrl = "http://shop.rxmao.cn/rxm/goods/tax.html";

	//是否可以使用喵币
	Boolean is_useMiaobi = redisSetting.getBoolean("isUseMiaoBi") ? true : false;
	//我的喵币个数
	Double myMiaoBi = member.getPoint().doubleValue();
	//可用喵币
	Double useMiaoBi = 0d;
	//抵扣金额
	Double miaoBiPrice = 0d;
	String miaoBiDesc = "";
	Double scale = redisSetting.getDouble("scale");
	if (is_useMiaobi && myMiaoBi >= 0) {
		useMiaoBi = myMiaoBi;
		Double limit = redisSetting.getDouble("miaoBiLimit");
		miaoBiPrice = new BigDecimal(useMiaoBi).divide(new BigDecimal(scale)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		double multiply = new BigDecimal(price).multiply(new BigDecimal(limit)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		//重新计算可用喵币
		if (miaoBiPrice > multiply) {
			useMiaoBi = new BigDecimal(multiply).multiply(new BigDecimal(scale)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			miaoBiPrice = new BigDecimal(useMiaoBi).divide(new BigDecimal(scale)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		}

	}

	//退货保险
	Boolean isReturnInsurance = redisSetting.getBoolean("isReturnInsurance");
	Double returnMoney = 0d;
	String returnCopy = "";

	if (isReturnInsurance) {
		returnMoney = redisSetting.getDouble("returnMoney");
		returnCopy = redisSetting.getString("returnCopyUrl");
	}

	Cache actCache = Redis.use();
	actCache.set("CART:" + member.getId(), cart);

	PriceResult returnPrice = null;
	PriceResult miaobiPrice = null;
	String realPrice = null;
	String favoritePrice = null;
	Double realPriced = 0d;
	Double favoreatePriced = 0d;
	Double miaobi = 0d;
	Double returns = 0d;

	if (isUseMiao) {
		if (isReturnInsurans) {
			returnPrice = new PriceResult("退货保险", "¥ " + MathUtil.getInt(returnMoney.toString()));
			realPriced = MathUtil.add(price, delivery.getPrice());
			realPriced = MathUtil.add(realPriced, returnMoney);
			realPrice = MathUtil.getInt(realPriced.toString());

			miaobiPrice = new PriceResult("喵币", "-¥ " + MathUtil.getInt(miaoBiPrice.toString()));
			favoreatePriced = MathUtil.add(miaoBiPrice, deliver);
			favoritePrice = MathUtil.getInt(favoreatePriced.toString());
			returns = returnMoney;
		} else {
			returnPrice = new PriceResult("退货保险", "¥ " + 0);
			realPriced = MathUtil.add(price, delivery.getPrice());
			realPrice = MathUtil.getInt(realPriced.toString());

			miaobiPrice = new PriceResult("喵币", "-¥ " + MathUtil.getInt(miaoBiPrice.toString()));
			favoreatePriced = MathUtil.add(miaoBiPrice, deliver);
			favoritePrice = MathUtil.getInt(favoreatePriced.toString());
			returns = 0d;
		}
		miaobi = miaoBiPrice;
	} else {
		if (isReturnInsurans) {
			returnPrice = new PriceResult("退货保险", "¥ " + MathUtil.getInt(returnMoney.toString()));
			realPriced = MathUtil.add(price, delivery.getPrice());
			realPriced = MathUtil.add(realPriced, returnMoney);
			realPrice = MathUtil.getInt(realPriced.toString());

			miaobiPrice = new PriceResult("喵币", "-¥ " + 0);
			favoritePrice = MathUtil.getInt(deliver.toString());
			returns = returnMoney;
		} else {
			returnPrice = new PriceResult("退货保险", "¥ " + 0);
			realPriced = MathUtil.add(price, delivery.getPrice());
			realPrice = MathUtil.getInt(realPriced.toString());

			miaobiPrice = new PriceResult("喵币", "-¥ " + 0);
			favoritePrice = MathUtil.getInt(deliver.toString());
			returns = 0d;
		}
		miaobi = 0d;
	}
	PriceResult manjianPrice = null;
	Promotion promotion = promotionService.find(5L);
	Double prom = cartService.findPriceByCartItem(cartItems1);
	Double manJianPrices = 0d;
	if (prom >= promotion.getTotalMoney().doubleValue()) {
		manjianPrice = new PriceResult(promotion.getTitle(), "-¥ " + MathUtil.getInt(promotion.getMoney().toString()));
		favoritePrice = MathUtil.getInt(new BigDecimal(favoritePrice).add(promotion.getMoney()).toString());
		manJianPrices = promotion.getMoney().doubleValue();
	}
	PriceResult totalPrice = new PriceResult("商品总金额", "¥ " + MathUtil.getInt(price.toString()));
	PriceResult oldTotalPrice = new PriceResult("商品优惠前总金额", "¥ " + MathUtil.getInt(oldPrice.toString()));
	PriceResult deliveryPrice = new PriceResult("运费", "¥ " + MathUtil.getInt(delivery.getPrice().toString()));
	List<PriceResult> priceList = new ArrayList<>();
	priceList.add(oldTotalPrice);
	priceList.add(totalPrice);
	priceList.add(deliveryPrice);
	if (!is_promotion&&miaobi>0) {
		priceList.add(miaobiPrice);
	}

	priceList.add(newDeliveryPrice);
	priceList.add(manjianPrice);


	Double couponYunfei = MathUtil.subtract(delivery.getPrice(), deliver);
	Double amountpaid = MathUtil.subtract(realPrice, favoritePrice);

	if (amountpaid < 0) {
		miaobi = miaobi + amountpaid;
		useMiaoBi = new BigDecimal(miaobi).multiply(new BigDecimal(scale)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		miaoBiPrice = new BigDecimal(useMiaoBi).divide(new BigDecimal(scale)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		amountpaid = 0d;
	}
	miaoBiDesc = "共" + MathUtil.getInt(member.getPoint().toString()) + "喵币,可用" + MathUtil.getInt(useMiaoBi.toString()) + "喵币,抵扣¥" + MathUtil.getInt(miaoBiPrice.toString());

	Double[] param = {deliver, miaobi, returns, amountpaid, couponYunfei, manJianPrices,0d,0d,0d};
	Double yunfei = delivery.getPrice();
	favoritePrice = MathUtil.getInt(new BigDecimal(favoritePrice).add(new BigDecimal(oldPrice)).subtract(new BigDecimal(price)).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
	OrderDetailsResult orderDetailsResult = new OrderDetailsResult(taxUrl, couponYunfei, skuids, yunfei, member, cart.getToken(), defaultReceiver, codeList, goodsList,
			receiveTime, is_freeMoney, is_useMiaobi,isUseMiao, miaoBiDesc, isReturnInsurance, returns, returnCopy, priceList, amountpaid.toString(), favoritePrice, param, is_promotion,identifier,false,"");
	renderJson(ApiResult.success(orderDetailsResult));

}
	}

	/**
	 * 收货地址
	 */
	@Before(Tx.class)
	public void address() {
		Integer pageNumber = getParaToInt("pageNumbers");
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		
		Long receiverId = getParaToLong("receiverIds");
		String redirectUrl = getPara("url_forward");
		
		Member member = memberService.getCurrent();
		String curRedirectUrl = getRequest().getQueryString() != null ? getRequest().getRequestURI() + "?" + getRequest().getQueryString() : getRequest().getRequestURI();
		Page<Receiver> page = receiverService.findPage(member, pageable);

		OrderAddressResult orderAddressResult = new OrderAddressResult(curRedirectUrl, redirectUrl, receiverId, "选择收货地址", page);
		renderJson(ApiResult.success(orderAddressResult));

	}
	

	
	/**
	 * 购物车
	 */
	@Before(Tx.class)
	public void create() {
		Cache actCache = Redis.use();
		Member member = memberService.getCurrent();

		String identifierIdString = getPara("identifierId","0");
		if("undefined".equals(identifierIdString)){
			renderJson(new ApiResult(0,"请选择门店!"));
			return;
		}
		Boolean isSPecialIds = getParaToBoolean("isSPecialIds",false);
		Long identifierId =Long.parseLong(identifierIdString);


	//	Long identifierId = getParaToLong("identifierId",0l);

		//代金券id
		String  sPecialCoupId = getPara("sPecialCoupId","0");

		//是否实名认证
		Certificates certificates = certificatesService.queryByMemberId(member.getId());
		if(certificates != null && certificates.getState() != 1){
			renderJson(new ApiResult(101,"请先进行实名认证!"));
			return;
		}



		Cart cart = actCache.get("CART:"+member.getId());
		String cartToken = getPara("cartToken"); //防止购物车重复提交
		Long receiverId = getParaToLong("receiverIds"); //收货人
		String memo = getPara("memo"); 		//备注

        String[] values = StringUtils.split(getPara("param"), ",");
        Double[] skuids = values == null ? null :convertToDouble(values);

		//快递费用
		Double deliveryMoney = skuids[0];

		//喵币
        Double miaobiMoney = skuids[1];

        //退货保险
		Double returnMoney = skuids[2];

		//总支付金额
		Double amountMoney = skuids[3];

		//运费优惠金额
		Double couponYunfei = skuids[4];

		//满减金额
		Double manjianPrice = skuids[5];

		Double	specialCouponPrice= skuids[6];
		Double integral= skuids[7];
		Double integralMoney= skuids[8];

		Order.Type ss = Order.Type.general;

		if(!"0".equals(isSPecialIds)){
			if(identifierId==0&&"0".equals(sPecialCoupId)&&integralMoney>0){
				ss = Order.Type.particular;
			}else if(identifierId!=0&&"0".equals(sPecialCoupId)&&integralMoney==0){
				ss = Order.Type.special;
			}else if(identifierId==0&&!"0".equals(sPecialCoupId)&&integralMoney==0){
				ss = Order.Type.coupon;
			}else if(identifierId==0&&"0".equals(sPecialCoupId)&&integralMoney==0){
				ss = Order.Type.particular;
			}else {
				renderJson(ApiResult.fail("订单异常"));
			}
		}
		Receiver receiver = null;

		if (cart.getIsDelivery()) {
			receiver = receiverService.find(receiverId);
			if (receiver == null || !member.equals(receiver.getMember())) {
				renderJson(ApiResult.fail("收货地址不能为空"));
				return;
			}
		}

		if (cart == null || cart.isEmpty()) {
			renderJson(ApiResult.fail("购物车不能为空!"));
			return;
		}
		if (!StringUtils.equals(cart.getToken(), cartToken)) {
			renderJson(ApiResult.fail(resZh.format("shop.order.cartHasChanged")));
			return;
		}

		if (cart.hasNotMarketable()) {
			renderJson(ApiResult.fail(resZh.format("shop.order.hasNotMarketable")));
			return;
		}
		if (cart.getIsLowStock()) {
			renderJson(ApiResult.fail(resZh.format("shop.order.cartLowStock")));
			return;
		}

		//1是 ，0否  是否開發票
		Boolean isInvoice=getParaToBoolean("isInvoice");
		//1是 ，0否  是否是個人
		Boolean isPersonal=getParaToBoolean("isPersonal");
		String taxNumber = getPara("taxNumber"); 		// 單位名稱
		String companyName = getPara("companyName");   //稅號

		  Order order = orderService.create(ss, cart, manjianPrice, receiver, amountMoney, returnMoney,
				  deliveryMoney , miaobiMoney, memo, couponYunfei,isInvoice,isPersonal,taxNumber,companyName,identifierId,sPecialCoupId,specialCouponPrice,integral,integralMoney);

		  renderJson(ApiResult.success(order.getId()));



	}

	/**
	 * 喵币商品-创建
	 */
	@Before(Tx.class)
	public void createMiaoBiOrder() {
		Member member = memberService.getCurrent();
		Long receiverId = getParaToLong("receiverIds"); //收货人
		Long miaobiGoodsId = getParaToLong("miaobiGoodsId");
		int goodsNum = getParaToInt("num");
		Receiver receiver = receiverService.find(receiverId);
		MiaobiGoods miaobiGoods = (MiaobiGoods) miaoBiGoodsService.find(miaobiGoodsId);
		Goods goods = miaobiGoods.getGoods();
		Double miaobiMoney = miaobiGoods.getPriceMiaobi().doubleValue()*goodsNum;
		Double amountMoney = miaobiGoods.getPrice().doubleValue()*goodsNum;


		//1是 ，0否  是否開發票
		Boolean isInvoice=getParaToBoolean("isInvoice");
		//1是 ，0否  是否是個人
		Boolean isPersonal=getParaToBoolean("isPersonal");
		String taxNumber = getPara("taxNumber"); 		//稅號
		String companyName = getPara("companyName"); //單位名稱


		Order order = orderService.createMiaoBi(Order.Type.miaobi,receiver,member,amountMoney,miaobiMoney,null,goods.getPrice(),goodsNum,miaobiGoods.getGoodsId(),1*goodsNum,isInvoice,isPersonal,taxNumber,companyName);
		renderJson(new ApiResult(1,"",order.getId()));
	}
	/**
	 * 支付
	 */
	@Before(Tx.class)
	public void payment() {
		Long id = getParaToLong("id");
		Order order = orderService.find(id);
		Member member = memberService.getCurrent();
		if(StringUtils.isEmpty(member.getOnShareCode())){
			renderJson(ApiResult.fail(7,"请填写邀请码"));
			return;
		}

		if(StringUtils.isEmpty(member.getOnShareCode())){
			renderJson(ApiResult.fail(7,"请填写邀请码"));
			return;
		}
		//支付倒计时 获取到期时间 支付金额 余额
		Date expire = order.getExpire();
		Date date = new Date();
		if(expire != null && !expire.after(date)){
			renderJson(ApiResult.fail("订单已过期,请重新下单购买"));
			return;
		}


		double price = order.getAmount().doubleValue();
		double balance = member.getBalance().doubleValue();

		//计算支付金额
		double payMoney = 0d;
		double amountPaid = 0d;
		Boolean isBalance = getParaToBoolean("isBalance", false);
		if (isBalance) {
			payMoney = MathUtil.subtract(price, balance);
			amountPaid = balance;
			if (payMoney < 0) {
				payMoney = 0;
				amountPaid = price;
			}
			order.setAmountPaid(new BigDecimal(amountPaid));
			orderService.update(order);
		} else {

			payMoney = price;
			order.setAmountPaid(BigDecimal.ZERO);
			orderService.update(order);

		}

		OrderMoneyResult orderMoneyResult = new OrderMoneyResult(price, balance, payMoney, MathUtil.subtract(expire.getTime()/1000,date.getTime()/1000), id);
		renderJson(ApiResult.success(orderMoneyResult));

	}
	
}
