package com.wtshop.api.controller.member;

import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Page;
import com.wtshop.Pageable;
import com.wtshop.api.interceptor.TokenInterceptor;
import com.wtshop.util.ApiResult;
import com.wtshop.api.common.result.member.ReceiverEditResult;
import com.wtshop.api.common.result.member.ReceiverListResult;
import com.wtshop.api.controller.BaseAPIController;
import com.wtshop.api.interceptor.ErrorInterceptor;
import com.wtshop.interceptor.WapInterceptor;
import com.wtshop.model.Area;
import com.wtshop.model.Member;
import com.wtshop.model.Receiver;
import com.wtshop.service.AreaService;
import com.wtshop.service.MemberService;
import com.wtshop.service.ReceiverService;
import com.wtshop.util.SMSUtils;

import java.util.List;


/**
 * Controller - 会员中心 - 收货地址
 * 
 * 
 */
@ControllerBind(controllerKey = "/api/member/receiver")
@Before({WapInterceptor.class, ErrorInterceptor.class, TokenInterceptor.class})
public class ReceiverAPIController extends BaseAPIController {
	
	/** 每页记录数 */
	private static final int PAGE_SIZE = 10;
	
	private MemberService memberService = enhance(MemberService.class);
	private ReceiverService receiverService = enhance(ReceiverService.class);
	private AreaService areaService = enhance(AreaService.class);
	
	/**
	 * 列表
	 * {"msg":"","code":1,"data":{"page":{"totalRow":1,"pageNumber":1,"firstPage":true,"lastPage":true,"totalPage":1,"pageSize":10,"list":[{"address":"中南海1号","area_id":12,"area_name":"北京市昌平区","consignee":"史强","create_date":"2017-05-24 14:42:02","id":14,"is_default":true,"member_id":18,"modify_date":"2017-05-24 14:42:02","phone":"13581856711","version":0,"zip_code":"100000"}]}}}
	 */
	public void list() {
		Integer pageNumber = getParaToInt("pageNumbers");
		Member member = memberService.getCurrent();
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);

		Page<Receiver> page = receiverService.findPage(member, pageable);

		ReceiverListResult receiverListResult = new ReceiverListResult(page);
		renderJson(ApiResult.success(receiverListResult));
	}

	/**
	 * 设为默认地址
	 */
	public void defaultd(){
		Receiver receiver = receiverService.findDefault(memberService.getCurrent());
		if(receiver != null){
			receiver.setIsDefault(false);
			receiverService.update(receiver);
		}
		Long id = getParaToLong("ids");
		Receiver receiver1 = receiverService.find(id);
		receiver1.setIsDefault(true);
		receiverService.update(receiver1);
		renderJson(ApiResult.success("修改默认地址成功"));
	}

	
	/**
	 * 保存
	 */
	public void save() {
		String name = getPara("name");
		String phone = getPara("phone");
		Long areaid = getParaToLong("areaid");
		String address = getPara("address");
		Receiver receivers = receiverService.findDefault(memberService.getCurrent());
		Receiver receiver = new Receiver();
		receiver.setAddress(address);
		receiver.setAreaId(areaid);
		receiver.setPhone(phone);
		receiver.setConsignee(name);
		receiver.setIsDefault(false);
		if(receivers == null){
			receiver.setIsDefault(true);
		}

		//检查手机号码有效性
		if (! SMSUtils.isMobileNo(phone)) {
			renderJson(ApiResult.fail("请检查手机号是否正确!"));
			return;
		}
		Area area = areaService.find(areaid);
		if (area == null) {
			renderJson(ApiResult.fail("地址不能为空!"));
			return;
		}
		receiver.setAreaName(area.getFullName());
		
		Member member = memberService.getCurrent();
		if (Receiver.MAX_RECEIVER_COUNT != null && member.getReceivers().size() >= Receiver.MAX_RECEIVER_COUNT) {
			renderJson(ApiResult.fail("收货地址超过最大保存数【" + Receiver.MAX_RECEIVER_COUNT + "】"));
			return;
		}
		
		receiver.setMemberId(member.getId());
		receiverService.save(receiver);

		renderJson(ApiResult.success("保存成功!"));
	}

	/**
	 * 更新
	 */
	public void update() {
		Long pReceiverId = getParaToLong("ids");
		String name = getPara("name");
		String phone = getPara("phone");
		Long areaid = getParaToLong("areaid");
		String address = getPara("address");
		Receiver receiver = receiverService.find(pReceiverId);

		//检查手机号码有效性
		if (! SMSUtils.isMobileNo(phone)) {
			renderJson(ApiResult.fail("请检查手机号是否正确!"));
			return;
		}
		receiver.setAddress(address);
		receiver.setAreaId(areaid);
		receiver.setPhone(phone);
		receiver.setConsignee(name);

		Area area = areaService.find(receiver.getAreaId());
		if (area != null) {
			receiver.setAreaId(area.getId());
			receiver.setAreaName(area.getFullName());
		}


		if (receiver == null) {
			renderJson(ApiResult.fail("当前地址为空!"));
			return;
		}
		Member member = memberService.getCurrent();
		if (!member.equals(receiver.getMember())) {
			renderJson(ApiResult.fail("非当前用户!"));
			return;
		}
		receiver.setMemberId(receiver.getMemberId());
		receiverService.update(receiver);
		renderJson(ApiResult.success("操作成功!"));
	}

	/**
	 * 删除
	 */
	public void delete() {
		Long id = getParaToLong("ids");
		Receiver receiver = receiverService.find(id);
		if (receiver == null) {
			renderJson(ApiResult.fail("当前地址为空!"));
			return;
		}
		if (receiver.getIsDefault()) {
			renderJson(ApiResult.fail("默认地址不能删除!"));
			return;
		}
		Member member = memberService.getCurrent();
		if (!member.equals(receiver.getMember())) {
			renderJson(ApiResult.fail("非当前用户!"));
			return;
		}
		receiverService.delete(id);

		renderJson(ApiResult.success("删除成功!"));
	}
	
	/**
	 * 地区
	 */
	public void area() {
		List<Area> areas = areaService.findAll();
		renderJson(ApiResult.success(areas));
	}
	
}
