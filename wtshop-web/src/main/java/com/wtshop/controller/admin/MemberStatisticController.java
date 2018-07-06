package com.wtshop.controller.admin;

import com.jfinal.ext.render.excel.PoiRender;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.render.Render;
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
@ControllerBind(controllerKey = "/admin/member_statistic")
public class MemberStatisticController extends BaseController {

	private StatisticService statisticService = enhance(StatisticService.class);

	/**
	 * 列表
	 */
	public void list() {
		String periodName = getPara("period");
		Statistic.Period period = StrKit.notBlank(periodName) ? Statistic.Period.valueOf(periodName) : null;
		
		Date beginDate = getParaToDate("beginDate", null);
		Date endDate = getParaToDate("endDate", null);
		if (period == null) {
			period = Statistic.Period.day;
		}
		if (beginDate == null) {
			switch (period) {
			case year:
				beginDate = DateUtils.addYears(new Date(), -10);

				break;
			case month:
				beginDate = DateUtils.addYears(new Date(), -1);
				break;
			case day:
				beginDate = DateUtils.addMonths(new Date(), -1);
				break;
			}
		}
		if (endDate == null) {
			endDate = new Date();
		}

		int type =getParaToInt("type");  //类型 int 0:
		setAttr("periods", Statistic.Period.values());
		setAttr("period", period);
		setAttr("beginDate", beginDate);
		setAttr("endDate", endDate);
		setAttr("statistics", statisticService.analyze(period, beginDate, endDate,type));
		render("/admin/member_statistic/list.ftl");
	}

	public  void   getAddvipListExcel(){
		String beginDate = getPara("beginDate", null);
		String endDate = getPara("endDate", null);
		List<Record>  list= statisticService.getAddvipList(beginDate,endDate);

		String[] header={"用户名称","电话","邮箱","生日","余额","总消费金额","创建时间"};
		String[] columns={"username","phone","email","birth","balance","amount","create_date"};


		Render poirender = PoiRender.me(list).fileName("vipadd.xls").headers(header).sheetName("会员表").columns(columns);
		render(poirender);


	}

}