package com.wtshop.api.controller.member;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.i18n.I18n;
import com.jfinal.i18n.Res;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.mongodb.DB;
import com.wtshop.Pageable;
import com.wtshop.RequestContextHolder;
import com.wtshop.api.interceptor.TokenInterceptor;
import com.wtshop.service.MemberFavoriteGoodsService;
import com.wtshop.util.ApiResult;
import com.wtshop.api.common.result.member.FavoriteListResult;
import com.wtshop.api.controller.BaseAPIController;
import com.wtshop.api.interceptor.ErrorInterceptor;
import com.wtshop.dao.MemberFavoriteGoodsDao;
import com.wtshop.interceptor.WapInterceptor;
import com.wtshop.model.Goods;
import com.wtshop.model.Member;
import com.wtshop.model.MemberFavoriteGoods;
import com.wtshop.service.GoodsService;
import com.wtshop.service.MemberService;
import com.wtshop.util.DateUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;


/**
 * Controller - 会员中心 - 商品收藏
 * 
 * 
 */
@ControllerBind(controllerKey = "/api/member/favorite")
@Before({WapInterceptor.class, ErrorInterceptor.class, TokenInterceptor.class})
public class FavoriteAPIController extends BaseAPIController {

	/** 每页记录数 */
	private static final int PAGE_SIZE = 10;

	private MemberService memberService = enhance(MemberService.class);
	private GoodsService goodsService = enhance(GoodsService.class);

	private MemberFavoriteGoodsService mfgGoodsService=enhance(MemberFavoriteGoodsService.class);
	private MemberFavoriteGoodsDao mfgGoodsDaoe=enhance(MemberFavoriteGoodsDao.class);

	/**
	 * 添加
	 * {"msg":"","code":1,"data":"商品收藏成功"}
	 */

	public void add() {

		String[] values = StringUtils.split(getPara("sku_id"), ",");
		Long sPecialId = getParaToLong("sPecialIds",0l);
		Long[] goodsLists = values == null ? null :convertToLong(values);
		Long goodsId = goodsLists[0];
		Res resZh = I18n.use();

	//	for(Long goodsId : goodsLists){
			Goods goods = goodsService.find(goodsId);
			JSONObject object = new JSONObject();

			RequestContextHolder.setRequestAttributes(getRequest());
			Member member = memberService.getCurrent();
			if (member == null) {
				renderJson(ApiResult.fail("请登录后操作!"));
				return;
			}
		MemberFavoriteGoods ss = mfgGoodsService.findGoods(member.getId(), goods.getId(), sPecialId);
			if (ss!=null) {
				renderJson(ApiResult.fail(resZh.format("shop.member.favorite.exist"),object));
				return;
			}


			if(ss!=null){
				MemberFavoriteGoods goodsByGoodsId = mfgGoodsService.findGoodsByGoodsId(goodsId, member.getId());
				goodsByGoodsId.setCreateDate(new Date());
				goodsByGoodsId.setFavoriteSpecial(sPecialId);
				goodsByGoodsId.update();
			}else {
				MemberFavoriteGoods memberFavoriteGood = new MemberFavoriteGoods();
				memberFavoriteGood.setFavoriteGoods(goodsId);
				memberFavoriteGood.setFavoriteMembers(member.getId());
				memberFavoriteGood.setFavoriteSpecial(sPecialId);
				mfgGoodsService.save(memberFavoriteGood);
			}

		//}

		renderJson(ApiResult.success(resZh.format("shop.member.favorite.success")));
	}
	
	/**
	 * 列表
	 * {"msg":"","code":1,"data":{"period":"week","goods":{"totalRow":1,"pageNumber":1,"firstPage":true,"lastPage":true,"totalPage":1,"pageSize":10,"list":[{"attribute_value0":null,"attribute_value1":null,"attribute_value10":null,"attribute_value11":null,"attribute_value12":null,"attribute_value13":null,"attribute_value14":null,"attribute_value15":null,"attribute_value16":null,"attribute_value17":null,"attribute_value18":null,"attribute_value19":null,"attribute_value2":null,"attribute_value3":null,"attribute_value4":null,"attribute_value5":null,"attribute_value6":null,"attribute_value7":null,"attribute_value8":null,"attribute_value9":null,"brand_id":50,"caption":"qwe","create_date":"2017-05-23 11:26:57","favorite_goods":66,"favorite_members":18,"generate_method":1,"hits":0,"id":66,"image":null,"introduction":null,"is_delivery":true,"is_list":true,"is_marketable":true,"is_top":false,"keyword":null,"market_price":155.000000,"memo":null,"modify_date":"2017-05-23 11:26:57","month_hits":0,"month_hits_date":"2017-05-23 11:26:57","month_sales":0,"month_sales_date":"2017-05-23 11:26:57","name":"qwe","parameter_values":null,"price":111.000000,"product_category_id":243,"product_images":null,"sales":0,"score":0.0,"score_count":0,"seo_description":null,"seo_keywords":null,"seo_title":null,"sn":"201705231313","specification_items":null,"total_score":0,"type":0,"unit":null,"version":0,"week_hits":0,"week_hits_date":"2017-05-23 11:26:57","week_sales":0,"week_sales_date":"2017-05-23 11:26:57","weight":null}]}}}
	 */

	public void list() {
		String keyword = getPara("keyword");


		Integer pageNumber = getParaToInt("pageNumbers");
		Member member = memberService.getCurrent();


		Pageable pageable = new Pageable(pageNumber, 1000);
		Page<Goods> page = goodsService.findPage(member, pageable, keyword);

		FavoriteListResult favoriteListResult1 = new FavoriteListResult(page, keyword);
		renderJson(ApiResult.success(favoriteListResult1));

	}
	
	/**
	 * 删除
	 * {"msg":"请求成功","code":1,"data":null}
	 */

	public void delete() {
		Long id = getParaToLong("sku_id");
		Long sPecialId = getParaToLong("sPecialIds",0l);
		Goods goods = goodsService.find(id);
		if (goods == null) {
			renderJson(ApiResult.fail());
			return;
		}

		Member member = memberService.getCurrent();
		MemberFavoriteGoods ss = mfgGoodsService.findGoods(member.getId(), id, sPecialId);
		if (ss==null) {
			renderJson(ApiResult.fail());;
			return;
		}

		Db.update("delete from `member_favorite_goods` where `favorite_goods` = "+id+" and `favorite_members` = "+member.getId()+" and `favorite_special` = "+sPecialId+"");
		//mfgGoodsService.delete(ss);
		//mfgGoodsDaoe.findBySql("delete from `member_favorite_goods` where `favorite_goods` = "+id+" and `favorite_members` = "+member.getId()+" and `favorite_special` = "+sPecialId+"");
		renderJson(ApiResult.success());
	}

	/**
	 * 清空记录
	 * {"msg":"清空成功","code":1,"data":null}
	 */

	public void deleteAll() {

		String[] values = StringUtils.split(getPara("id"), ",");
		Long[] goods = values == null ? null :convertToLong(values);

		for(Long good : goods){
			Db.deleteById("member_favorite_goods", "favorite_goods", good);
		}
		renderJson(ApiResult.success());

	}


}
