package com.wtshop.controller.admin;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.wtshop.Message;
import com.wtshop.Pageable;
import com.wtshop.Setting;
import com.wtshop.model.*;
import com.wtshop.service.*;
import com.wtshop.shiro.hasher.Hasher;
import com.wtshop.shiro.hasher.HasherInfo;
import com.wtshop.shiro.hasher.HasherKit;
import com.wtshop.util.SystemUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Controller - 积分
 * 合伙人分配
 * 
 */
@ControllerBind(controllerKey = "/admin/partner")
public class PartnerController extends BaseController {
	private PartnerService partnerService = enhance(PartnerService.class);



	/**
	 * 查看
	 */
	public void view() {
		Long id = getParaToLong("id");
		Partner partner =partnerService.findFirst(id);


		//setAttr("genders", Partner.Gender.values());
	//	setAttr("memberAttributes", memberAttributeService.findList(true, true));

		setAttr("partner", partner);
	//	setAttr("loginPlugin", pluginService.getLoginPlugin(member.getLoginPluginId()));
		render("/admin/partner/view.ftl");
	}

	/**
	 * 添加
	 */
	public void add() {
	//	setAttr("genders", Partner.Gender.values());
	//	setAttr("memberRanks", memberRankService.findAll());
		//setAttr("memberAttributes", memberAttributeService.findList(true, true));
		render("/admin/partner/add.ftl");
	}

	/**
	 * 保存
	 */
	public void save() {
		Partner member = getModel(Partner.class);
		Long memberRankId = getParaToLong("memberRankId");
	//	member.setMemberRankId(memberRankService.find(memberRankId).getId());

		Boolean isEnabled = getParaToBoolean("isEnabled", false);
	//	member.setIsEnabled(isEnabled);

		Setting setting = SystemUtils.getSetting();
		/*if (member.getUsername().length() < setting.getUsernameMinLength() || member.getUsername().length() > setting.getUsernameMaxLength()) {
			addFlashMessage(new com.wtshop.Message(com.wtshop.Message.Type.error,"用户名长度不符,请重新输入!"));
			redirect("list.jhtml");
			return;
		}
		if (member.getPassword().length() < setting.getPasswordMinLength() || member.getPassword().length() > setting.getPasswordMaxLength()) {
			addFlashMessage(new com.wtshop.Message(com.wtshop.Message.Type.error,"密码长度不符,请重新输入!"));
			redirect("list.jhtml");
			return;
		}
		if (partnerService.usernameDisabled(member.getUsername()) || partnerService.usernameExists(member.getUsername())) {
			addFlashMessage(new com.wtshop.Message(com.wtshop.Message.Type.error,"您输入的用户名已存在,请重新输入!"));
			redirect("list.jhtml");
			return;
		}*/
	/*	if (!setting.getIsDuplicateEmail() && partnerService.emailExists(member.getEmail())) {
			redirect(ERROR_VIEW);
			return;
		}*/
	//	member.removeAttributeValue();
/*		for (MemberAttribute memberAttribute : memberAttributeService.findList(true, true)) {
			String[] values = getRequest().getParameterValues("memberAttribute_" + memberAttribute.getId());
			if (!memberAttributeService.isValid(memberAttribute, values)) {
				redirect(ERROR_VIEW);
				return;
			}
			Object memberAttributeValue = memberAttributeService.toMemberAttributeValue(memberAttribute, values);
			if (StrKit.notNull(memberAttributeValue)) {
				member.setAttributeValue(memberAttribute, memberAttributeValue);
			}
		}*/
	/*	member.setPhone(member.getUsername());
		member.setPassword(DigestUtils.md5Hex(member.getPassword()));
		member.setIsDelete(false);
		member.setPoint(BigDecimal.ZERO);
		member.setBalance(BigDecimal.ZERO);
		member.setAmount(BigDecimal.ZERO);
		member.setIsLocked(false);
		member.setLoginFailureCount(0);
		member.setLockedDate(null);
		member.setRegisterIp(getRequest().getRemoteAddr());
		member.setLoginIp(null);
		member.setLoginDate(null);
		member.setLoginPluginId(null);
		member.setOpenId(null);
		member.setLockKey(null);
		member.setCart(null);
		member.setOrders(null);
		member.setPaymentLogs(null);
		member.setDepositLogs(null);
		member.setCouponCodes(null);
		member.setReceivers(null);
		member.setReviews(null);
		member.setConsultations(null);
		member.setFavoriteGoods(null);
		member.setProductNotifies(null);
		member.setInMessages(null);
		member.setOutMessages(null);
		member.setPointLogs(null);
		member.setUsername(StringUtils.lowerCase(member.getUsername()));
		member.setEmail(StringUtils.lowerCase(member.getEmail()));
		member.setLockKey(DigestUtils.md5Hex(UUID.randomUUID() + RandomStringUtils.randomAlphabetic(30)));*/
		partnerService.save(member);
		addFlashMessage(SUCCESS_MESSAGE);
		redirect("list.jhtml");
	}

	/**
	 * 编辑
	 */
	public void edit() {
		Long id = getParaToLong("id");
		Partner partner = partnerService.find(id);
	//	setAttr("genders", Partner.Gender.values());
	//	setAttr("memberRanks", memberRankService.findAll());
	//	setAttr("memberAttributes", memberAttributeService.findList(true, true));
		setAttr("partner", partner);
	//	setAttr("loginPlugin", pluginService.getLoginPlugin(member.getLoginPluginId()));
		render("/admin/member/edit.ftl");
	}

	/**
	 * 更新
	 */
	public void update() {
		Partner member = getModel(Partner.class);
		Long memberRankId = getParaToLong("memberRankId");
		Boolean isEnabled = getParaToBoolean("isEnabled");
	//	member.setIsEnabled(isEnabled);
	//	member.setMemberRank(memberRankService.find(memberRankId));

		Setting setting = SystemUtils.getSetting();
/*		if (member.getPassword() != null && (member.getPassword().length() < setting.getPasswordMinLength() || member.getPassword().length() > setting.getPasswordMaxLength())) {
			redirect(ERROR_VIEW);
			return;
		}
		Partner pMember = partnerService.find(member.getId());
		if (pMember == null) {
			redirect(ERROR_VIEW);
			return;
		}*/
	/*	if (!setting.getIsDuplicateEmail() && !partnerService.emailUnique(pMember.getEmail(), member.getEmail())) {
			redirect(ERROR_VIEW);
			return;
		}
		member.removeAttributeValue();*/
	/*	for (MemberAttribute memberAttribute : memberAttributeService.findList(true, true)) {
			String[] values = getRequest().getParameterValues("memberAttribute_" + memberAttribute.getId());
			if (!memberAttributeService.isValid(memberAttribute, values)) {
				redirect(ERROR_VIEW);
				return;
			}
			Object memberAttributeValue = memberAttributeService.toMemberAttributeValue(memberAttribute, values);
			if (StrKit.notNull(memberAttributeValue)) {
				member.setAttributeValue(memberAttribute, memberAttributeValue);
			}
		}*/
	/*	if (StringUtils.isEmpty(member.getPassword())) {
			member.setPassword(pMember.getPassword());
		} else {
			member.setPassword(DigestUtils.md5Hex(member.getPassword()));
		}
		if (pMember.getIsLocked() && !member.getIsLocked()) {
			member.setLoginFailureCount(0);
			member.setLockedDate(null);
		} else {
			member.setIsLocked(pMember.getIsLocked());
			member.setLoginFailureCount(pMember.getLoginFailureCount());
			member.setLockedDate(pMember.getLockedDate());
		}
		member.setPhone(pMember.getPhone());*/
		member.remove("username");
		partnerService.update(member);
		addFlashMessage(SUCCESS_MESSAGE);
		redirect("list.jhtml");
	}

	/**
	 * 列表
	 */
	public void list() {
		Pageable pageable = getBean(Pageable.class);
		Page<Partner> pages = partnerService.findPages(pageable, null);


	//	setAttr("memberRanks", memberRankService.findAll());
	//	setAttr("memberAttributes", memberAttributeService.findAll());
		Partner partner = pages.getList().get(0);
		setAttr("pageable", pageable);
		setAttr("page", pages );
		render("/admin/partner/list.ftl");
	}


	/**
	 * 删除
	 */
	public void delete() {
		Long[] ids = getParaValuesToLong("ids");
		for(Long id : ids){
			Partner member = partnerService.find(id);
			member.setIsDelete(true);
			partnerService.update(member);
		}
		renderJson(SUCCESS_MESSAGE);
	}



}