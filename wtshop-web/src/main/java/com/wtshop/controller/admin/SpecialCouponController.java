package com.wtshop.controller.admin;


import com.jfinal.ext.render.excel.PoiRender;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.LogKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.render.Render;
import com.wtshop.Message;
import com.wtshop.Pageable;
import com.wtshop.model.Brand.Type;
import com.wtshop.model.Goods;
import com.wtshop.model.Identifier;
import com.wtshop.model.Member;
import com.wtshop.model.SpecialCoupon;
import com.wtshop.service.GoodsService;
import com.wtshop.service.IdentifierService;
import com.wtshop.service.MemberService;
import com.wtshop.service.SpecialCouponService;
import com.wtshop.util.ShareCodeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static com.wtshop.api.controller.BaseAPIController.convertToLong;

/**
 * 特殊用户管理
 * 
 * 
 */
@ControllerBind(controllerKey = "/admin/specialCoupon")
public class SpecialCouponController extends BaseController {
	private SpecialCouponService identifierService = enhance(SpecialCouponService.class);
	private GoodsService goodsService = enhance(GoodsService.class);
	private MemberService memberService = enhance(MemberService.class);

	/**
	 * 添加
	 */
	public void add() {
		setAttr("types", Type.values());
		render("/admin/special_coupon/add.ftl");
	}

	/**
	 * 保存
	 */
	public void save(){

		Integer id = getParaToInt("number");
		Long money = getParaToLong("money");
		SpecialCoupon identifier=identifierService.findByLast();
		Long i=0l;
		String title="1";
		if(identifier==null){

			i++;
		}else{
			i=identifier.getId()+1;
			title=(Integer.parseInt(identifier.getTitle())+1)+"";
		}
		for (Long j=0l;j<id;j++){
			String code = ShareCodeUtils.idToCode(i,7);
			SpecialCoupon identifier1= new SpecialCoupon();
			identifier1.setCode(code);
			identifier1.setStatus(0);
			identifier1.setMoney(BigDecimal.valueOf(money));
			identifier1.setTitle(title);
				System.out.println("开始计数"+i+"验证码"+code);
			identifierService.save(identifier1);
				i++;

		}


	addFlashMessage(SUCCESS_MESSAGE);
	redirect("/admin/specialCoupon/list.jhtml");


	}

	/**
	 * 编辑
	 */
	public void edit() {
		Long id = getParaToLong("id");
		setAttr("types", Type.values());
		setAttr("specialPersonnel", identifierService.find(id));
		render("/admin/special_coupon/edit.ftl");
	}
	/**
	 * 更新
	 */
	public void update() {
		SpecialCoupon brand = getModel(SpecialCoupon.class);

		Member member = memberService.findByPhone(brand.getCode());
		if(member==null){
			addFlashMessage(Message.errMsg("手机号不存在"));

			redirect("/admin/specialCoupon/list.jhtml");
		}else{

			identifierService.update(brand);
			if(!StringUtils.isNotEmpty(member.getShareCode())){
				String shareCode = ShareCodeUtils.idToCode(member.getId());
				member.setShareCode(shareCode);
				member.setHousekeeperId(2l);
				memberService.update(member);

			}
			addFlashMessage(SUCCESS_MESSAGE);
			redirect("/admin/specialCoupon/list.jhtml");
		}

	}

	/**
	 * 列表
	 */
	public void list() {
		String titleB = getPara("titleB");
		String titleE = getPara("titleE");


		Date begin= getParaToDate("beginDate", null);
		Date end = getParaToDate("endDate", null);
		if (begin == null) {
			begin =new Date();
		}

		if (end == null) {
			end = new Date();
		}
		String beginDate = com.wtshop.util.DateUtils.formatDate(begin);
		Date ddd= DateUtils.addDays(end,1);
		String	 endDate=	com.wtshop.util.DateUtils.formatDate(ddd);

		//模糊
		String blurry = getPara("blurry");

		String select="select i.* ";
		String sql=" from special_coupon i LEFT JOIN member m on i.member_id=m.id  where 1=1 ";
		if(beginDate!=null){
			sql=sql+" and   i.create_date>='"+beginDate+"'";
		}
		if(endDate!=null){
			sql=sql+" and   i.create_date<='"+endDate+"'";
		}


		if(StringUtils.isNotEmpty(titleB)){
			sql=sql+" and   i.title>="+titleB;
		}
		if(StringUtils.isNotEmpty(titleE)){
			sql=sql+" and   i.title<="+titleE;
		}

		if(StringUtils.isNotEmpty(blurry)){
			blurry=blurry.trim();
			sql=sql+"and ( ";
			sql=sql+"  m.phone like '%"+blurry+"%' or m.nickname LIKE '%"+blurry+"%'  or i.`code` like '%"+blurry+"%' or i.title like '%"+blurry+"%' " ;
			if("现场兑换".contains(blurry)){
				sql=sql+"  or  i.`status`=5 " ;
			}
			if("已邮寄".contains(blurry)){
				sql=sql+"  or  i.`status`=4 " ;
			}
			if("已启用".contains(blurry)){
				sql=sql+"  or  i.`status`=1 " ;
			}
			if("已完成".contains(blurry)){
				sql=sql+"  or  i.`status`=3 " ;
			}
			if("未使用".contains(blurry)){
				sql=sql+"  or  i.`status`=0 " ;
			}
			sql=sql+" ) ";
		}
		Pageable pageable = getBean(Pageable.class);
		Object gg = identifierService.findPages(select, sql, pageable);
		setAttr("page",gg );
		LogKit.info(">" + pageable.getPageNumber());
		setAttr("pageable", pageable);
		setAttr("beginDate", begin);
		setAttr("endDate", end);
		setAttr("blurry", blurry);
		setAttr("titleB", titleB);
		setAttr("titleE", titleE);
		render("/admin/special_coupon/list.ftl");
	}

	/**
	 * 删除
	 */
	public void delete() {

		String[] values = StringUtils.split(getPara("ids"), ",");
		Long[] idList = values == null ? null :convertToLong(values);
		if (idList != null) {
			for (Long id : idList) {
				List<Goods> goodsList = goodsService.findByBrandId(id);
				if (goodsList != null &&  goodsList.size() > 0) {
					for(Goods goods : goodsList){
						renderJson(Message.error("admin.brand.deleteExistNotAllowed", goods.getName()));
						return;
					}

				}
			}
			identifierService.delete(idList);
		}
		renderJson(SUCCESS_MESSAGE);
	}


	/**
	 * 禁用福袋
	 */
	public void disabled() {
		Long id = getParaToLong("id");
		SpecialCoupon activity = identifierService.find(id);
		activity.setStatus(3);
		identifierService.update(activity);
		redirect("/admin/specialCoupon/list.jhtml");
	}


	/**
	 * 启用福袋
	 */
	public void publish() {
		Long id = getParaToLong("id");
		SpecialCoupon activity = identifierService.find(id);


		activity.setStatus(1);
		identifierService.update(activity);
		redirect("/admin/specialCoupon/list.jhtml");
	}
	//导出
	public  void   getExcel(){
		String titleB = getPara("titleB");

		String titleE = getPara("titleE");

		String sql=" from special_coupon i  where 1=1 ";
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


		Page<SpecialCoupon> identifier = identifierService.findPage(sql, pageable);

		List<SpecialCoupon> fff = identifier.getList();
		String[] header={"批次","邀请码","优惠金额","状态","创建时间"};
		String[] columns={"title","code","money","status","create_date"};
		Render poirender = PoiRender.me(fff).fileName("code"+titleB+"-"+""+titleE+".xls").headers(header).sheetName("识别码").columns(columns);
		render(poirender);

	}
}