package com.wtshop.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Enhancer;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;
import com.wtshop.Setting;
import com.wtshop.dao.OrderDao;
import com.wtshop.model.*;
import com.wtshop.service.*;
import com.wtshop.util.ApiResult;
import com.wtshop.util.RedisUtil;
import com.wtshop.util.SMSUtils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller - 索引
 * 
 * 
 */
@ControllerBind(controllerKey = "/admin/index")
public class IndexController extends BaseController {
	private MemberService memberService = Enhancer.enhance(MemberService.class);
	private ArticleService articleService = enhance(ArticleService.class);
	private GoodsService goodsService = enhance(GoodsService.class);
	private SearchService searchService = enhance(SearchService.class);
	private SpecialGoodsService specialGoodsService = enhance(SpecialGoodsService.class);
	private AdService adService = enhance(AdService.class);
	private FullReductionService fullReductionService =enhance(FullReductionService.class);
	private IntegralStoreService integralStoreService =Enhancer.enhance(IntegralStoreService.class);
	private AccountService accountService = Enhancer.enhance(AccountService.class);
	private OrderDao orderDao = Enhancer.enhance(OrderDao.class);

	com.jfinal.log.Logger logger = com.jfinal.log.Logger.getLogger(IndexController.class);
	/**
	 * 生成类型
	 */
	/**
	 *
	 */
	public void ceshi() {
		Order order = orderDao.findBySn("20190903155516492476");
		JSONObject redisSetting = JSONObject.parseObject(RedisUtil.getString("redisSetting"));
		//总积分
		BigDecimal zongIntegral=BigDecimal.ZERO;
		//反现比例
		Double zhiFuFanBi =  redisSetting.getDouble("zhiFuFanBi");
		if(zhiFuFanBi==null){
			zhiFuFanBi=0d;
		}
		//返现金额
		BigDecimal fanXianMoney=order.getAmount().multiply(BigDecimal.valueOf(zhiFuFanBi));

		//待分配的积分
		BigDecimal fengPeiIntegral=order.getIntegralGift();

		List<IntegralStore> integralStoreList=integralStoreService.findLogByMemberId(order.getMemberId());
		for (IntegralStore integralStore : integralStoreList) {
			//返钱
			MathContext mc=new MathContext(2);
			Double linshi = Double.valueOf(integralStore.get("scale").toString());
			BigDecimal money111=fanXianMoney.multiply(BigDecimal.valueOf(linshi),mc);

			if(money111.compareTo(BigDecimal.ZERO)==1){
				Member member2 = memberService.find(integralStore.getStoreMemberId());
				DepositLog depositLog1 = new DepositLog();
				depositLog1.setBalance(member2.getBalance());
				depositLog1.setCredit(money111);
				depositLog1.setDebit(BigDecimal.ZERO);
				depositLog1.setStatus(1);
				depositLog1.setMemo("商品返现,订单"+order.getSn()+",返现总金额"+fanXianMoney+",所占比例"+integralStore.get("scale"));
				depositLog1.setType(DepositLog.Type.ident.ordinal());
				depositLog1.setOrderId(order.getId());
				//depositLog1.setOperator(""+member.getNickname()+"  "+member.getPhone());
				depositLog1.setMemberId(member2.getId());
				member2.setBalance(money111.add(member2.getBalance()));
			//	depositLogService.save(depositLog1);
			//	memberService.update(member2);

				/**
				 *反现短信提醒
				 */
				Map<String, Object> params = new HashMap<String, Object>();

				params.put("name",member2.getNickname());
				params.put("zmoney",order.getAmount() );
				params.put("store",linshi*100d) ;
				params.put("money",fanXianMoney );
				ApiResult result = SMSUtils.send(member2.getPhone(),"SMS_173405304", params);
				//ApiResult result = SMSUtils.send("", "", params);
				if(result.resultSuccess()) {
					// sm.setex("PONHE:"+mobile,120,"1");
					Sms sms = new Sms();
					sms.setMobile(member2.getPhone());
					sms.setSmsCode(member2.getNickname()+"用户完成了一笔"+order.getAmount()+"元的订单，您的门店积分占该用户总积分的"+linshi*100d+"%，因此获得"+fanXianMoney+"元积分抵扣收益，详情请查看钱包—使用明细。");
					sms.setSmsType(Setting.SmsType.other.ordinal());
				//	smsService.saveOrUpdate(sms);
					logger.info(member2.getNickname()+"用户完成了一笔"+order.getAmount()+"元的订单，您的门店积分占该用户总积分的"+linshi*100d+"%，因此获得"+fanXianMoney+"元积分抵扣收益，详情请查看钱包—使用明细。");
				}else {
					logger.info("您发送的过于频繁,请稍后再试!");
				}

				//增加门店比例
				IntegralStoreLog integralStoreLog=new IntegralStoreLog();
				BigDecimal meige = fengPeiIntegral.multiply(BigDecimal.valueOf(linshi), mc);
				integralStore.setBalance(integralStore.getBalance().add(meige));
				integralStoreService.update(integralStore);
				// 增加积分占比
				integralStoreLog.setBalance(integralStore.getBalance());
				integralStoreLog.setCredit(meige);
				integralStoreLog.setDebit(BigDecimal.ZERO);
				integralStoreLog.setMemberId(1139l);
				integralStoreLog.setType(1);
				integralStoreLog.setStoreMemberId(integralStore.getStoreMemberId());
				integralStoreLog.setMemo("绑定代金卡获取积分增加相应门店权重");
			//	integralStoreLogService.save(integralStoreLog);



				//扣除积分占比

				IntegralStoreLog integralStoreLog1=new IntegralStoreLog();
				BigDecimal meige1 = order.getIntegralPaid().multiply(BigDecimal.valueOf(linshi), mc);
				integralStore.setBalance(integralStore.getBalance().subtract(meige1));
				integralStoreService.update(integralStore);
				// 增加积分占比
				integralStoreLog.setBalance(integralStore.getBalance());
				integralStoreLog.setCredit(BigDecimal.ZERO);
				integralStoreLog.setDebit(meige1);
				integralStoreLog.setMemberId(1139l);
				integralStoreLog.setType(1);
				integralStoreLog.setStoreMemberId(integralStore.getStoreMemberId());
				integralStoreLog.setMemo("绑定代金卡获取积分扣除相应门店权重");
			//	integralStoreLogService.save(integralStoreLog);






			}



		}
	}
	public enum GenerateType {
		/**
		 * 文章
		 */
		article,

		/**
		 * 商品
		 */
		goods
	}

	/**
	 * 生成索引
	 */
	public void generate() {
		setAttr("generateTypes", IndexController.GenerateType.values());
		render("/admin/index/generate.ftl");
	}

	/**
	 * 生成索引
	 */
	public void generateSubmit() {
		String generateTypeName = getPara("generateType");
		GenerateType generateType = StrKit.notBlank(generateTypeName) ? GenerateType.valueOf(generateTypeName) : null;
		Boolean isPurge = getParaToBoolean("isPurge");
		Integer first = getParaToInt("first");
		Integer count = getParaToInt("count");
		
		long startTime = System.currentTimeMillis();
		if (first == null || first < 0) {
			first = 0;
		}
		if (count == null || count <= 0) {
			count = 100;
		}
		int generateCount = 0;
		boolean isCompleted = true;
		switch (generateType) {
		case article:
			if (first == 0 && isPurge != null && isPurge) {
				searchService.purge(Article.class);
			}
			List<Article> articles = articleService.findList(first, count, null, null);
			for (Article article : articles) {
				searchService.index(article);
				generateCount++;
			}
			first += articles.size();
			if (articles.size() == count) {
				isCompleted = false;
			}
			break;
		case goods:
			if (first == 0 && isPurge != null && isPurge) {
				searchService.purge(Goods.class);
			}
			List<Goods> goodsList = goodsService.findList(first, count, null, null);
			for (Goods goods : goodsList) {
				searchService.index(goods);
				generateCount++;
			}
			first += goodsList.size();
			if (goodsList.size() == count) {
				isCompleted = false;
			}
			break;
		}
		long endTime = System.currentTimeMillis();
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("first", first);
		data.put("generateCount", generateCount);
		data.put("generateTime", endTime - startTime);
		data.put("isCompleted", isCompleted);
		renderJson(data);
	}

}