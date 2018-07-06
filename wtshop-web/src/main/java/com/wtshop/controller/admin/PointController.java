package com.wtshop.controller.admin;

import java.util.HashMap;
import java.util.Map;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.wtshop.Message;
import com.wtshop.Pageable;
import com.wtshop.model.Admin;
import com.wtshop.model.Member;
import com.wtshop.model.PointLog;
import com.wtshop.service.AdminService;
import com.wtshop.service.MemberService;
import com.wtshop.service.MiaobiLogService;
import com.wtshop.service.PointLogService;

/**
 * Controller - 积分
 * 
 * 
 */
@ControllerBind(controllerKey = "/admin/point")
public class PointController extends BaseController {

	private PointLogService pointLogService = enhance(PointLogService.class);
	private MemberService memberService = enhance(MemberService.class);
	private MiaobiLogService miaobiLogService = enhance(MiaobiLogService.class);

	/**
	 * 检查会员
	 */
	public void checkMember() {
		String username = getPara("username");
		Map<String, Object> data = new HashMap<String, Object>();
		Member member = memberService.findByUsername(username);
		if (member == null) {
			data.put("message", Message.warn("admin.point.memberNotExist"));
			renderJson(data);
			return;
		}
		data.put("message", SUCCESS_MESSAGE);
		data.put("point", member.getPoint());
		renderJson(data);
	}

	/**
	 * 调整
	 */
	public void adjust() {
		render("/admin/point/adjust.ftl");
	}



	/**
	 * 记录
	 */
	public void log() {

		Pageable pageable = getBean(Pageable.class);
		Integer type = getParaToInt("typeName");
		Page<Record> pages = miaobiLogService.findPages(pageable, type);

		setAttr("page", pages);
		setAttr("pageable", pageable);
		render("/admin/point/log.ftl");
	}

}