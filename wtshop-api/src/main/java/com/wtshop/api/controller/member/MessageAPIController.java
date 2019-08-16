package com.wtshop.api.controller.member;

import com.alibaba.common.logging.LoggerFactory;
import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;
import com.wtshop.Pageable;
import com.wtshop.RequestContextHolder;
import com.wtshop.api.common.result.member.MemberGoodsResult;
import com.wtshop.api.common.result.member.MyMessageResult;
import com.wtshop.api.interceptor.TokenInterceptor;
import com.wtshop.model.Goods;
import com.wtshop.service.*;
import com.wtshop.util.ApiResult;
import com.wtshop.api.controller.BaseAPIController;
import com.wtshop.api.interceptor.ErrorInterceptor;
import com.wtshop.interceptor.WapInterceptor;
import com.wtshop.model.Information;
import com.wtshop.model.Member;
import com.wtshop.util.IpUtil;
import com.wtshop.util.RedisUtil;
import freemarker.log.Logger;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;


/**
 * Controller - 会员中心 - 消息
 * 
 * 
 */
@ControllerBind(controllerKey = "/api/member/message")
@Before({WapInterceptor.class, ErrorInterceptor.class})
public class MessageAPIController extends BaseAPIController {
	com.jfinal.log.Logger logger = com.jfinal.log.Logger.getLogger(MessageAPIController.class);
	/** 每页记录数 */
	private static final int PAGE_SIZE = 10;

	private MemberService memberService = enhance(MemberService.class);
	private InformationService informationService = enhance(InformationService.class);
	private GoodsService goodsService = enhance(GoodsService.class);

	/**
	 * 列表
	 * type = 0 通知  type = 1  互动  ,type =2.物流
	 */
	public void list() {
		Integer pageNumber = getParaToInt("pageNumber");
		Integer type = getParaToInt("type", 0);
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		Member member = memberService.getCurrent();
		Page<Information> page = informationService.findPages(member, pageable, type);

		renderJson(ApiResult.success(page));
	}

	/**
	 * 删除
	 */
	public void delete(){
		String[] values = StringUtils.split(getPara("id"), ",");
		Long[] ids = values == null ? null :convertToLong(values);

		for(Long id : ids){
			Information information = informationService.find(id);
			information.setIsDelete(true);
			informationService.update(information);
		}

		renderJson(ApiResult.success());
	}

	/**
	 * 删除
	 */
	public void deleteAll(){
		Member member = memberService.getCurrent();

		int aBoolean = informationService.updateMessage(member);
		if(aBoolean > 0){
			renderJson(ApiResult.success());
		}else {
			renderJson(ApiResult.fail());
		}

	}

	/**
	 * 消息接口
	 */
	public void count(){
		com.alibaba.common.logging.Logger logger = LoggerFactory.getLogger("fff");
		//我的消息

		String token = getHeader("token");
		logger.info("++++++++++++++token:" + token + " ++++++++++++++++++++++");
		Long messageNoReadCount = 0L;
		logger.info("没有登录++++++++++++++messageNoReadCount:" + messageNoReadCount.toString() + " ++++++++++++++++++++++");
		if( !"0".equals(token) && StringUtils.isNotEmpty(token)){
			messageNoReadCount = informationService.findMessageNoReadCount(memberService.getCurrent(), false);
			logger.info("登录++++++++++++++messageNoReadCount:" + messageNoReadCount.toString() + " ++++++++++++++++++++++");

		}

		renderJson(ApiResult.success(messageNoReadCount.toString()));
	}
	
	/**
	 * 设置消息已读
	 */
	public void read() {
		String[] values = StringUtils.split(getPara("id"), ",");
		Long[] ids = values == null ? null :convertToLong(values);
		for(Long id : ids){
			Information information = informationService.find(id);
			information.setStatus(true);
			informationService.update(information);
		}
		renderJson(ApiResult.success("消息已读!"));
	}

	/**
	 * 设置消息全部已读
	 */
	public void readall(){
		Member member = memberService.getCurrent();
		List<Information> messageNoRead = informationService.findMessageNoRead(member, false);
		for(Information information : messageNoRead){
			information.setStatus(true);
			informationService.update(information);
		}
		renderJson(ApiResult.success());
	}

	/**
	 * 获取appid接口
	 * 添加推送消息
	 */
	public void addAppid() throws IOException {

		logger.info("开始调用获取appid接口");
		Member member = memberService.getCurrent();
		String ip = IpUtil.getIpAddress(getRequest());
		logger.info("访问ip为："+ip+"");
		String appid = getPara("appid");
		if( member==null||member.getId()==null){
			renderJson(ApiResult.fail("没有登录"));
			return;
		}else{
			String key = "MEMBER:" + member.getId();
			RedisUtil.setString(key, appid);
			renderJson(ApiResult.success());
			return;
		}

	}

	/**
	 * 删除appid
	 */
	public void deleteAppid(){
		Member member = memberService.getCurrent();
		String key = "MEMBER:" + member.getId().toString();
		Cache actCache = Redis.use();
		actCache.del(key);
		renderJson(ApiResult.success());
	}


	/**
	 * 消息状态
	 *  系统消息 订单消息  技师推荐消息
	 */

	public void status(){
		Member member = memberService.getCurrent();
		Cache actCache = Redis.use();
		Boolean systemMessage = actCache.get("SYSTEMMESSAGR_SWITCH:" + member.getId());
        Boolean orderMessage = actCache.get("ORDERMMESSAGR_SWITCH:" + member.getId());
		Boolean staffMessage = actCache.get("STAFFMESSAGR_SWITCH:" + member.getId());
		MyMessageResult myMessageResult = new MyMessageResult(systemMessage, orderMessage, staffMessage);
		renderJson(ApiResult.success(myMessageResult));

	}

	/**
	 * 消息开关
	 * 系统消息 订单消息  技师推荐消息
	 */
	public void design(){
		Member member = memberService.getCurrent();
		Boolean systemMessage = getParaToBoolean("systemMessage",true);
		Boolean orderMessage = getParaToBoolean("orderMessage",true);
		Boolean staffMessage = getParaToBoolean("staffMessage",true);
		Cache actCache = Redis.use();
		actCache.set("SYSTEMMESSAGR_SWITCH:" + member.getId(),systemMessage);
		actCache.set("ORDERMMESSAGR_SWITCH:" + member.getId(),orderMessage);
		actCache.set("STAFFMESSAGR_SWITCH:" + member.getId(),staffMessage);
		renderJson(ApiResult.success());
	}

	/**
	 * 商品推荐信息
	 */
	public void goods(){
		Long messageId = getParaToLong("messageId");
		Long goodsId = getParaToLong("goodsId");
		Information information = informationService.findMember(messageId);
		Goods goods = goodsService.find(goodsId);
		MemberGoodsResult memberGoodsResult = new MemberGoodsResult(information, goods);
		renderJson(ApiResult.success(memberGoodsResult));

	}


}
