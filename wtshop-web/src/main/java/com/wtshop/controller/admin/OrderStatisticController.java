package com.wtshop.controller.admin;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;
import com.wtshop.model.Statistic;
import com.wtshop.service.StatisticService;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;

/**
 * Controller - 订单统计
 * 
 * 
 */
@ControllerBind(controllerKey = "/admin/order_statistic")
public class OrderStatisticController extends BaseController {

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
		setAttr("periods", Statistic.Period.values());
		setAttr("period", period);
		setAttr("beginDate", beginDate);
		setAttr("endDate", endDate);
		setAttr("d", statisticService.getOrderStatistic(period, beginDate, endDate));
		render("/admin/order_statistic/list.ftl");
	}

}