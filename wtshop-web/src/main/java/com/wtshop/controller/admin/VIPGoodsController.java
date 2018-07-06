package com.wtshop.controller.admin;

import com.jfinal.aop.Enhancer;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Page;
import com.wtshop.Pageable;
import com.wtshop.service.GoodsService;
import com.wtshop.service.VipGoodsHistoryService;

/**
 * Controller - 货品
 * 
 * 
 */
@ControllerBind(controllerKey = "/admin/vipGoods")
public class VIPGoodsController extends BaseController {


	private GoodsService goodsService = Enhancer.enhance(GoodsService.class);
	private VipGoodsHistoryService vipGoodsHistoryService = Enhancer.enhance(VipGoodsHistoryService.class);

	/**
	 * 查询vip商品的排行
	 */

	public void list(){

		Pageable pageable = getBean(Pageable.class);
		setAttr("page", goodsService.findCount(pageable));
		setAttr("pageable", pageable);
		render("/admin/vipGoods_count/list.ftl");

	}


	/**
	 * 详情
	 */

	public void view(){
		Pageable pageable = getBean(Pageable.class);
		Long goodsId = getParaToLong("goodsId");
		Page pag= vipGoodsHistoryService.findMemList(pageable,goodsId);
		setAttr("page",pag);
		setAttr("pageable",pageable);
		render("/admin/vipGoods_count/view.ftl");
	}

}