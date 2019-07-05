package com.wtshop.api.controller.member;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.wtshop.Pageable;
import com.wtshop.api.common.result.member.CommissionLogResult;
import com.wtshop.api.common.result.member.DepositLogResult;
import com.wtshop.api.controller.BaseAPIController;
import com.wtshop.api.interceptor.ErrorInterceptor;
import com.wtshop.api.interceptor.TokenInterceptor;
import com.wtshop.interceptor.WapInterceptor;
import com.wtshop.model.*;
import com.wtshop.service.*;
import com.wtshop.util.ApiResult;
import com.wtshop.util.IpUtil;
import com.wtshop.util.UUIDUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Controller - 会员中心 - 预存款
 * 
 * 
 */
@ControllerBind(controllerKey = "/api/member/commission")
@Before({WapInterceptor.class, ErrorInterceptor.class, TokenInterceptor.class})
public class CommissionAPIController extends BaseAPIController {

	org.slf4j.Logger logger = LoggerFactory.getLogger(CommissionAPIController.class);


	/** 每页记录数 */
	private static final int PAGE_SIZE = 10;

	private MemberService memberService = enhance(MemberService.class);
	private CommissionLogService depositLogService = enhance(CommissionLogService.class);
	private UserPayService userPayService = enhance(UserPayService.class);
	private AccountService accountService = enhance(AccountService.class);
	private ExchangeService exchangeService = enhance(ExchangeService.class);
	private PrestoreService prestoreService = enhance(PrestoreService.class);
	private ExchangeLogService exchangeLogService = enhance(ExchangeLogService.class);
	private MrmfShopService mrmfShopService = enhance(MrmfShopService.class);
	private OrderService orderService = enhance(OrderService.class);

	/**
	 * 微信充值 0  支付宝充值 1
	 */
	@Before(Tx.class)
	public void recharge() {
        Logger logger = Logger.getLogger("recharge");
        String price = getPara("price");
		Integer type = getParaToInt("type");
		double value = Double.parseDouble(price);
        String ip = IpUtil.getIpAddr(getRequest());
		if (StrKit.isBlank(ip) || ip.equals("0:0:0:0:0:0:0:1")) {
			ip = "127.0.0.1";
		}
		Member member = memberService.getCurrent();
		ExchangeLog exchangeLog = new ExchangeLog();
		exchangeLog.setType(type);
		exchangeLog.setStatus(0);
		exchangeLog.setPrice(new BigDecimal(price));
		exchangeLog.setMemberId(member.getId());
		exchangeLogService.save(exchangeLog);
		Map<String, String> map = new HashMap<>();
		if(0 == type){
			map = userPayService.getRechargePrepayId(value, ip ,exchangeLog.getId());
		}
		if(1 == type){
			map = userPayService.aliRecharge(value, exchangeLog.getId());
		}
		renderJson(ApiResult.success(map));
	}


	/**
	 * 转账
	 */
	@Before(Tx.class)
	public void transfer(){
		String price = getPara("price");

		String phone = getPara("phone");
		Member member = memberService.findByPhone(phone);
		if(member == null){
			renderJson(ApiResult.fail("您输入的用户不存在,请重新输入!"));
			return;
		}
		Member current = memberService.getCurrent();
		BigDecimal money = new BigDecimal(price);
		BigDecimal scale = current.getBalance().subtract(money).setScale(2, BigDecimal.ROUND_HALF_DOWN);
		if(scale.doubleValue() >= 0.00){
			current.setBalance(scale);
		}else {
			renderJson(ApiResult.fail("您的余额不足,请重新输入!"));
			return;
		}
		member.setBalance(member.getBalance().add(money).setScale(2, BigDecimal.ROUND_HALF_DOWN));

		memberService.update(member);
		memberService.update(current);
		//交易记录
		CommissionLog depositLog = new CommissionLog();
		depositLog.setMemberId(member.getId());
		depositLog.setBalance(member.getBalance());
		depositLog.setType(DepositLog.Type.transfer.ordinal());
		depositLog.setCredit(money);
		depositLog.setDebit(new BigDecimal(0));
		depositLog.setMemo("转账收入");
		depositLogService.save(depositLog);

		CommissionLog depositLog1 = new CommissionLog();
		depositLog1.setMemberId(current.getId());
		depositLog1.setBalance(current.getBalance());
		depositLog1.setType(DepositLog.Type.transfer.ordinal());
		depositLog1.setMemo("转账支出");
		depositLog1.setCredit(new BigDecimal(0));
		depositLog1.setDebit(money);
		depositLogService.save(depositLog1);


		renderJson(ApiResult.success("转账成功!"));

	}

	/**
	 * 退款
	 */
	@Before(Tx.class)
	public void backMoney(){
		Long orderId = getParaToLong("orderId");
		Order order = orderService.find(orderId);
		if(order == null){
			renderJson(ApiResult.fail("错误的订单号!"));
			return;
		}

		//支付宝退款
		if(order.getAliPaid().doubleValue() > 0){

		}else if(order.getWeixinPaid().doubleValue() > 0){

		}

		renderJson(ApiResult.success("转账成功!"));

	}



	/**
	 * 提现
	 * 0 微信 1 支付宝 2余额提现
	 */
	@Before(Tx.class)
	public void exchange(){
		try {
			String price = getPara("price");
			Integer type = getParaToInt("type");
			//提现方式 余额1 /佣金 2
			Integer balanceType = getParaToInt("balanceType",2);
			String ip = IpUtil.getIpAddr(getRequest());
			if (StrKit.isBlank(ip) || ip.equals("0:0:0:0:0:0:0:1")) {
				ip = "127.0.0.1";
			}
			Member member = memberService.getCurrent();
			//获取用户佣金余额
			Double commission = member.getCommission().doubleValue();

			//微信绑定
			Account weiXinInfo = accountService.getUserInfo(member.getId(), 0);

			//支付宝绑定
			Account aLiInfo = accountService.getUserInfo(member.getId(), 3);

			Double amount =  Double.parseDouble( price);
			Integer money =Integer.parseInt( String.format("%.0f", amount * 100));

			//计算可提现金额
			BigDecimal km=depositLogService.findMoneyByMemId(member.getId());
			if(km.doubleValue()<amount){
				renderJson(ApiResult.fail("提现金额异常，请联系管理员!"));
				return;
			}

			//判断输入金额 和 余额
         /*    if(){

			 }*/

			BigDecimal scale = new BigDecimal(commission).subtract(new BigDecimal(price)).setScale(2, BigDecimal.ROUND_HALF_DOWN);
			if(scale.doubleValue() >= 0.00){
				if( 0 == type){
					if(weiXinInfo != null && weiXinInfo.getAccount() != null){
						//区分提现方式
						String str_no = "";
						if(1 == balanceType){
							str_no = "B" + UUIDUtils.getLongUUID();
						}else if(2 == balanceType){
							str_no = "Y" + UUIDUtils.getLongUUID();
						}
						Map<String, String> map = accountService.CashToWeChat(str_no, weiXinInfo.getAccount(), money, "安吃提现", ip);
						logger.info("安吃提现[微信] {}", map);
						if (map != null) {
							if (StringUtils.isNotEmpty(map.get("err_code"))) {
								renderJson(ApiResult.fail("系统错误,请稍后尝试!"));
								return;
							}else if (map.get("result_code").equalsIgnoreCase("SUCCESS")){
								String partner_trade_no = map.get("partner_trade_no");
								//更新信息
								depositLogService.exchengeUpdate(price, partner_trade_no);
								renderJson(ApiResult.success("提现成功!","提现成功!"));
								return;
							}
						}
					}else {
						renderJson(ApiResult.fail("提现请先绑定微信社交账号!"));
						return;
					}

				}else if(1 == type){
					if(aLiInfo != null && aLiInfo.getAccount() != null){
						JSONObject jsonObject = accountService.CashToAliPay(aLiInfo.getAccount(), amount,"Y");
						logger.info("安吃提现[支付宝] {}", jsonObject);
						if( jsonObject != null && "10000".equals(jsonObject.get("code"))){
							String out_biz_no = jsonObject.getString("out_biz_no");
							//更新信息
								depositLogService.exchengeUpdate(price, out_biz_no);
								renderJson(ApiResult.success("提现成功!","提现成功!"));
								return;
						}else {
								renderJson(ApiResult.fail("系统错误,请稍后尝试"));
								return;
						}


					}else {
						renderJson(ApiResult.fail("提现请先绑定支付宝社交账号!"));
						return;
					}
				}if(2 == type){
					depositLogService.exchengeUpdate(price);
					renderJson(ApiResult.success("提现成功!","提现成功!"));
					return;
				}
			}else {
				renderJson(ApiResult.fail("您的佣金余额不足,请重新输入!"));
				return;
			}

		} catch (Exception e) {
			renderJson(ApiResult.fail("提现失败!"));
		}
	}




	/**
	 * 记录
	 * {"msg":"","code":1,"data":{"page":{"totalRow":0,"pageNumber":1,"firstPage":true,"lastPage":true,"totalPage":0,"pageSize":10,"list":[]}}}
	 */
	public void log() {
		Integer pageNumber = getParaToInt("pageNumbers");
		//类型 使用明细：0    消费记录 1
		Integer type = getParaToInt("type");
		Integer status = getParaToInt("status");

		Member member = memberService.getCurrent();
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		Page<CommissionLog> page = depositLogService.findPage(member, pageable ,type,status);
		CommissionLogResult depositLogResult = new CommissionLogResult(page);
		renderJson(ApiResult.success(depositLogResult));
	}

	/**
	 * 提现进度
	 */
	public void progress(){
		Long progressId = getParaToLong("progressId");
		ExchangeProgress progress = exchangeService.findProgress(progressId);
		renderJson(ApiResult.success(progress));

	}

	/**
	 * 预存款记录
	 */
	public void prestore(){

		String organId = memberService.findOrganId(memberService.getCurrent());

		if(organId == null){
			renderJson(ApiResult.fail("您当前店铺身份有误,请联系后台管理员!"));
			return;
		}
		List<Prestore> list = prestoreService.findList(Long.parseLong(organId));
		renderJson(ApiResult.success(list));

	}

}
