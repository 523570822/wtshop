package com.wtshop.api.controller.member;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Page;
import com.wtshop.Pageable;
import com.wtshop.util.ApiResult;
import com.wtshop.api.common.result.member.ConsultionAddResult;
import com.wtshop.api.common.result.member.ConsultionListResult;
import com.wtshop.api.controller.BaseAPIController;
import com.wtshop.api.interceptor.ErrorInterceptor;
import com.wtshop.interceptor.WapInterceptor;
import com.wtshop.model.Consultation;
import com.wtshop.model.Goods;
import com.wtshop.model.Member;
import com.wtshop.service.ConsultationService;
import com.wtshop.service.GoodsService;
import com.wtshop.service.MemberService;


/**
 * Controller - 会员中心 - 咨询
 * 
 * 
 */
@ControllerBind(controllerKey = "/api/member/consultation")
@Before({WapInterceptor.class, ErrorInterceptor.class})
public class ConsultationAPIController extends BaseAPIController {

	/** 每页记录数 */
	private static final int PAGE_SIZE = 10;
	
	private MemberService memberService = new MemberService();
	private ConsultationService consultationService = new ConsultationService();
	private GoodsService goodsService = enhance(GoodsService.class);
	
	/**
	 * 列表
	 * {"msg":"","code":1,"data":{"consultation":{"totalRow":1,"pageNumber":1,"firstPage":true,"lastPage":true,"totalPage":1,"pageSize":10,"list":[{"content":"\"你好啊\"","create_date":"2017-05-23 17:33:27","for_consultation_id":null,"goods_id":65,"id":19,"ip":"127.0.0.1","is_show":true,"member_id":18,"modify_date":"2017-05-23 17:33:27","version":0}]}}}
	 */

	public void list() {
		Integer pageNumber = getParaToInt("pageNumbers");
		Member member = memberService.getCurrent();
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		Page<Consultation> page = consultationService.findPage(member, null, null, pageable);
		ConsultionListResult consultionListResult = new ConsultionListResult(page);
		renderJson(ApiResult.success(consultionListResult));
	}
	
	
	/**
	 * 增加咨询
	 * {"msg":"","code":1,"data":{"goods":{"attribute_value0":null,"attribute_value1":null,"attribute_value10":null,"attribute_value11":null,"attribute_value12":null,"attribute_value13":null,"attribute_value14":null,"attribute_value15":null,"attribute_value16":null,"attribute_value17":null,"attribute_value18":null,"attribute_value19":null,"attribute_value2":null,"attribute_value3":null,"attribute_value4":null,"attribute_value5":null,"attribute_value6":null,"attribute_value7":null,"attribute_value8":null,"attribute_value9":null,"brand_id":50,"caption":"控油去油 长效保湿 调节水油平衡","create_date":"2017-05-22 14:19:21","generate_method":1,"hits":0,"id":64,"image":"/upload/image/201705/ba801bbb-37be-45e1-a576-e59df54753e5.jpg","introduction":"<p><img src=\"/upload/image/201705/e13f093e-0f6c-4771-88cb-be595666c409.png\"/></p>","is_delivery":true,"is_list":true,"is_marketable":true,"is_top":false,"keyword":null,"market_price":298.800000,"memo":null,"modify_date":"2017-05-22 17:50:27","month_hits":0,"month_hits_date":"2017-05-22 14:19:21","month_sales":0,"month_sales_date":"2017-05-22 14:19:21","name":"新品上市 欧莱雅男士洗面奶矿漠泥长效控油保湿洁面护肤品套装","parameter_values":"[{\"group\":\"产品参数\",\"entries\":[{\"name\":\"化妆品净含量\",\"value\":\"套装容量\"},{\"name\":\"产地\",\"value\":\"中国\"},{\"name\":\"功效\",\"value\":\"补水\"},{\"name\":\"规格类型\",\"value\":\"正常规格\"},{\"name\":\"化妆品保质期\",\"value\":\"3年\"},{\"name\":\"适合肤质\",\"value\":\"油性肤质\"}]}]","price":249.000000,"product_category_id":245,"product_images":"[{\"source\":\"/upload/image/201705/73e96e1e-2a89-46e9-a11b-5f942d0553ff-source.png\",\"large\":\"/upload/image/201705/73e96e1e-2a89-46e9-a11b-5f942d0553ff-large.jpg\",\"medium\":\"/upload/image/201705/73e96e1e-2a89-46e9-a11b-5f942d0553ff-medium.jpg\",\"thumbnail\":\"/upload/image/201705/73e96e1e-2a89-46e9-a11b-5f942d0553ff-thumbnail.jpg\"},{\"source\":\"/upload/image/201705/bb23ff84-b8f9-4258-a7f8-f481057991a3-source.jpg\",\"large\":\"/upload/image/201705/bb23ff84-b8f9-4258-a7f8-f481057991a3-large.jpg\",\"medium\":\"/upload/image/201705/bb23ff84-b8f9-4258-a7f8-f481057991a3-medium.jpg\",\"thumbnail\":\"/upload/image/201705/bb23ff84-b8f9-4258-a7f8-f481057991a3-thumbnail.jpg\"}]","sales":0,"score":0.0,"score_count":0,"seo_description":null,"seo_keywords":null,"seo_title":null,"sn":"201705221111","specification_items":null,"total_score":0,"type":0,"unit":"套","version":8,"week_hits":0,"week_hits_date":"2017-05-22 14:19:21","week_sales":0,"week_sales_date":"2017-05-22 14:19:21","weight":1000}}}
	 */
	public void add() {
		Long id = getParaToLong("ids");
		Goods goods = goodsService.find(id);
		ConsultionAddResult consultionAddResult = new ConsultionAddResult(goods);
		renderJson(ApiResult.success(consultionAddResult));
	}
	
	/**
	 * 保存
	 * {"msg":"","code":1,"data":{"referer":"/api/goods/detail.jhtml?id=65"}}
	 */
	public void save() {
		Long id = getParaToLong("ids");
		String content = getPara("questions");

		Member member = memberService.getCurrent();
		if (member == null) {
			renderJson(ApiResult.fail("当前用户不能是空!"));
			return;
		}
		
		Goods goods = goodsService.find(id);
		if (goods == null) {
			renderJson(ApiResult.fail("当前商品不能是空!"));
			return;
		}
		Consultation consultation = new Consultation();
		consultation.setGoodsId(goods.getId());
		consultation.setContent(content);
		consultation.setMemberId(member.getId());
		consultation.setIsShow(true);
		consultation.setIp(getRequest().getRemoteHost());
		consultationService.save(consultation);

		JSONObject object = new JSONObject();
		object.put("referer","/api/goods/detail.jhtml?id=" + goods.getId());
		renderJson(ApiResult.success(object));
	}
	

}
