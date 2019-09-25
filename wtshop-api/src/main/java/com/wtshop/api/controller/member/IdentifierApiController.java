package com.wtshop.api.controller.member;


import com.jfinal.aop.Before;
import com.jfinal.ext.render.excel.PoiRender;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.render.Render;
import com.wtshop.Pageable;
import com.wtshop.api.controller.BaseAPIController;
import com.wtshop.api.interceptor.ErrorInterceptor;
import com.wtshop.interceptor.WapInterceptor;
import com.wtshop.model.Identifier;
import com.wtshop.service.GoodsService;
import com.wtshop.service.IdentifierService;
import com.wtshop.service.MemberService;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 特殊用户管理
 * 
 * 
 */
@ControllerBind(controllerKey = "/api/member/identifier")
@Before({WapInterceptor.class, ErrorInterceptor.class})
public class IdentifierApiController extends BaseAPIController {

	private IdentifierService identifierService = enhance(IdentifierService.class);
	private GoodsService goodsService = enhance(GoodsService.class);
	private MemberService memberService = enhance(MemberService.class);


	/**
	 * 线下兑换
	 */
	public void disabled() {
		Long id = getParaToLong("id");
		Integer status = getParaToInt("status",3);
		Identifier activity = identifierService.find(id);
if(status==1){
		if(activity.getShareCode()==null||"".equals(activity.getShareCode())){
			activity.setStatus(0);
		}else {
			activity.setStatus(status);
		}
}else {
	activity.setStatus(status);
}

		identifierService.update(activity);

	}


	/**
	 * 启用福袋
	 */
	public void publish() {
		Long id = getParaToLong("id");
		Identifier activity = identifierService.find(id);
if(activity.getShareCode()==null||"".equals(activity.getShareCode())){
	activity.setStatus(0);
}else {
	activity.setStatus(1);
}
		identifierService.update(activity);
		redirect("/admin/identifier/list.jhtml");
	}
	//导出
	public  void   getExcel(){
		String titleB = getPara("titleB");

		String titleE = getPara("titleE");

		String sql=" from identifier i  where 1=1 ";
if(StringUtils.isNotEmpty(titleB)){
	sql=sql+" and   i.title>="+titleB;
}
if(StringUtils.isNotEmpty(titleE)){
	sql=sql+" and   i.title<="+titleE;
}



		Pageable pageable = getBean(Pageable.class);

			pageable.setPageNumber(1);
			pageable.setPageSize(10000000);
			//Page<Order> oo = orderService.findPage(type, status, member, null, isPendingReceive, isPendingRefunds, null, null, isAllocatedStock, hasExpired, pageable);
			Boolean isEcel=true;


		Page<Identifier> identifier = identifierService.findPage(sql, pageable);

		List<Identifier> fff = identifier.getList();
		String[] header={"批次","邀请码","状态","创建时间"};
		String[] columns={"title","code","status","create_date"};
		Render poirender = PoiRender.me(fff).fileName("code"+titleB+"-"+""+titleE+".xls").headers(header).sheetName("识别码").columns(columns);
		render(poirender);

	}
}