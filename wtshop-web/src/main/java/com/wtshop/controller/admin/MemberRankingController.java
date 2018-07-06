package com.wtshop.controller.admin;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;
import com.wtshop.Pageable;
import com.wtshop.model.Member;
import com.wtshop.service.MemberService;

/**
 * Controller - 会员排名
 * 
 * 
 */
@ControllerBind(controllerKey = "/admin/member_ranking")
public class MemberRankingController extends BaseController {

	private MemberService memberService = enhance(MemberService.class);

	/**
	 * 列表
	 */
	public void list() {
		String rankingTypeName = getPara("rankingType", null);
		Member.RankingType rankingType = StrKit.notBlank(rankingTypeName) ? Member.RankingType.valueOf(rankingTypeName) : null;
		
		Pageable pageable = getBean(Pageable.class);
		
		if (rankingType == null) {
			rankingType = Member.RankingType.amount;
		}
		setAttr("rankingTypes", Member.RankingType.values());
		setAttr("rankingType", rankingType);
		setAttr("pageable", pageable);
		setAttr("page", memberService.findPage(rankingType, pageable));
		render("/admin/member_ranking/list.ftl");
	}

}