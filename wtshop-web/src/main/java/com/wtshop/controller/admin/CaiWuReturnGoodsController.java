package com.wtshop.controller.admin;

import com.jfinal.ext.render.excel.PoiRender;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.render.Render;
import com.wtshop.Pageable;
import com.wtshop.model.Statistic;
import com.wtshop.service.StatisticService;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;
import java.util.List;


/**
 * Controller - 会员统计
 * 
 * 
 */
@ControllerBind(controllerKey = "/admin/caiwu/returnGoods")
public class CaiWuReturnGoodsController extends BaseController {

	private StatisticService statisticService = enhance(StatisticService.class);

	/**
	 * 列表
	 */
	public void list() {
				String periodName = getPara("period");
		Statistic.Period period = StrKit.notBlank(periodName) ? Statistic.Period.valueOf(periodName) : null;

		Date beginDate = getParaToDate("beginDate", null);
		Date endDate = getParaToDate("endDate", null);
		if (beginDate == null) {
					beginDate = DateUtils.addMonths(new Date(), -1);
			}

		if (endDate == null) {
			endDate = new Date();
		}
		setAttr("beginDate", beginDate);
		setAttr("endDate", endDate);
		Pageable pageable = getBean(Pageable.class);
		setAttr("pageable", pageable);
	//	renderJson(statisticService.getCaiWuList(pageable, com.wtshop.util.DateUtils.formatDate(beginDate),com.wtshop.util.DateUtils.formatDate(endDate)));
		setAttr("page", statisticService.caiWuReturnGoods(pageable, com.wtshop.util.DateUtils.formatDate(beginDate),com.wtshop.util.DateUtils.formatDate(endDate)));
		render("/admin/caiwu/returnlist.ftl");
	}

	public  void   excel(){
		String beginDate = getPara("beginDate", null);
		String endDate = getPara("endDate", null);
		List<Record>  list= statisticService.caiWuReturnGoodsExcel(beginDate,endDate);

		String[] header={"订单编号","退货编号","会员昵称","退货数量","退货金额","退货日期","退货状态"};
		String[] columns={"orderid","returnid","nickname","quantity","amount","create_date","statusStr"};
		Render poirender = PoiRender.me(list).fileName("tuihuo.xls").headers(header).sheetName("退货订单").columns(columns);
		render(poirender);


	}


}