package com.wtshop.api.controller.member;

import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Record;
import com.wtshop.api.interceptor.TokenInterceptor;
import com.wtshop.model.*;
import com.wtshop.service.*;
import com.wtshop.util.ApiResult;
import com.wtshop.api.common.result.member.MemberMessageResult;
import com.wtshop.api.controller.BaseAPIController;
import com.wtshop.api.interceptor.ErrorInterceptor;
import com.wtshop.interceptor.WapInterceptor;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;


/**
 * Controller - 会员中心 - 个人资料
 * 
 * 
 */
@ControllerBind(controllerKey = "/api/member/profile")
@Before({WapInterceptor.class, ErrorInterceptor.class, TokenInterceptor.class})
public class ProfileAPIController extends BaseAPIController {

	private MemberService memberService = enhance(MemberService.class);
	private MemberInterestService memberInterestService =enhance(MemberInterestService.class);
	private MemberSkinService memberSkinService = enhance(MemberSkinService.class);



	/**
	 * 编辑
	 */
	public void edit() {
		Member member = memberService.getCurrent();
		String nickname = getPara("nickname");
		String phone = getPara("phone");
		String attributeValue5 = getPara("attributeValue5");
		String attributeValue6 = getPara("attributeValue6");
		Date birth = getParaToDate("birth");
		Integer sex = getParaToInt("sex");
		String sign = getPara("sign");
		String[] interestids = StringUtils.split(getPara("interestid"), ",");
		Long[] interests = interestids == null ? null :convertToLong(interestids);

		String[] skinids = StringUtils.split(getPara("skinid"), ",");
		Long[] skins = skinids == null ? null :convertToLong(skinids);

		if( nickname != null){
			member.setNickname(nickname);
		}
		if( phone != null){
			member.setPhone(phone);
		}
		if( birth != null){
			member.setBirth(birth);
		}
		if( sex != null){
			member.setGender(sex);
		}
		if( attributeValue5 != null){
			member.setAttributeValue5(attributeValue5);
		}
		if( attributeValue6 != null){
			member.setAttributeValue6(attributeValue6);
		}



		if( interests != null && interests.length > 0){
			memberInterestService.deleteRecordList(getPara("interestid"),member.getId());
			for(Long inter : interests){
				MemberInterestCategory dddd = memberInterestService.findRecord(member.getId(), inter);

				if (dddd==null){
					MemberInterestCategory memberInterestCategory = new MemberInterestCategory();
					memberInterestCategory.setMembers(member.getId());
					memberInterestCategory.setInterestCategory(inter);
					memberInterestService.save(memberInterestCategory);
				}


			}
		}
		if( skins != null && skins.length > 0){
			memberSkinService.deleteRecord(member.getId());
			for(Long skin : skins){
				MemberSkinType memberSkinType = new MemberSkinType();
				memberSkinType.setMembers(member.getId());
				memberSkinType.setSkinType(skin);
				memberSkinService.save(memberSkinType);
			}
		}
		if( sign != null){
			member.setSign(sign);
		}

		memberService.update(member);

		renderJson(ApiResult.success("修改成功"));
	}

    /**
     *
     */


}
