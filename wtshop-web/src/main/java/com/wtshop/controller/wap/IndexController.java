package com.wtshop.controller.wap;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.ext.route.ControllerBind;
import com.wtshop.Order;
import com.wtshop.model.AdPosition;
import com.wtshop.model.Goods;
import com.wtshop.service.AdPositionService;
import com.wtshop.service.GoodsService;

@ControllerBind(controllerKey = "/wap")
public class IndexController extends BaseController {

	private AdPositionService adPositionService = enhance(AdPositionService.class);
	private GoodsService goodsService = enhance(GoodsService.class);
	
	
	/**
	 * 首页
	 */
	public void index() {
		List<Order> orders = new ArrayList<Order>();
		AdPosition adPosition = adPositionService.find(1L);
		List<Goods> goodsList = goodsService.findList(Goods.Type.general, null, null, null, null, null, null, null, true, true, null, null, null, null, null, null, null, orders, false);
		setAttr("adPosition", adPosition);
		setAttr("goodsList", goodsList);
		render("/wap/index.ftl");
	}
	
}
