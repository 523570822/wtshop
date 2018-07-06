package com.wtshop.controller.admin;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.jfinal.ext.route.ControllerBind;
import com.wtshop.Message;
import com.wtshop.Pageable;
import com.wtshop.model.Admin;
import com.wtshop.model.DepositLog;
import com.wtshop.model.Member;
import com.wtshop.service.AdminService;
import com.wtshop.service.DepositLogService;
import com.wtshop.service.MemberService;

/**
 * Controller - 预存款
 * 
 * 
 */
@ControllerBind(controllerKey = "/admin/deposit")
public class DepositController extends BaseController {

	private DepositLogService depositLogService = enhance(DepositLogService.class);
	private MemberService memberService = enhance(MemberService.class);
	private AdminService adminService = enhance(AdminService.class);

	/**
	 * 检查会员
	 */
	public void checkMember() {
		String username = getPara("username");
		Map<String, Object> data = new HashMap<String, Object>();
		Member member = memberService.findByUsername(username);
		if (member == null) {
			data.put("message", Message.warn("admin.deposit.memberNotExist"));
			renderJson(data);
			return;
		}
		data.put("message", SUCCESS_MESSAGE);
		data.put("balance", member.getBalance());
		renderJson(data);
	}

	/**
	 * 调整
	 */
	public void adjust() {
		render("/admin/deposit/adjust.ftl");
	}

	/**
	 * 调整
	 */
	public void adjustSubmit() {
		String username = getPara("username");
		BigDecimal amount = new BigDecimal(getPara("amount", "0"));
		String memo = getPara("memo");
		Member member = memberService.findByUsername(username);
		if (member == null) {
			redirect(ERROR_VIEW);
			return;
		}
		if (amount == null || amount.compareTo(BigDecimal.ZERO) == 0) {
			redirect(ERROR_VIEW);
			return;
		}
		if (member.getBalance() == null || member.getBalance().add(amount).compareTo(BigDecimal.ZERO) < 0) {
			redirect(ERROR_VIEW);
			return;
		}
		Admin admin = adminService.getCurrent();
		//memberService.addBalance(member, amount, DepositLog.Type.adjustment, admin, memo);
		addFlashMessage(SUCCESS_MESSAGE);
		redirect("log.jhtml");
	}

	/**
	 * 记录
	 */
	public void log() {
		Long memberId = getParaToLong("memberId");
		Pageable pageable = getBean(Pageable.class);
		Member member = memberService.find(memberId);
		if (member != null) {
			setAttr("member", member);
			setAttr("page", depositLogService.findPage(member, pageable,1));
		} else {
			setAttr("page", depositLogService.findPage(pageable));
		}
		setAttr("pageable", pageable);
		render("/admin/deposit/log.ftl");
	}

}