package com.wtshop.controller.admin;

import com.aliyun.common.utils.DateUtil;
import com.jfinal.ext.render.excel.PoiRender;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.render.Render;
import com.wtshop.Pageable;
import com.wtshop.dao.OrderItemDao;
import com.wtshop.model.OrderItem;
import com.wtshop.model.Statistic;
import com.wtshop.service.StatisticService;
import org.apache.commons.lang3.time.DateUtils;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;




/**
 * Controller - 会员统计
 * 
 * 
 */
@ControllerBind(controllerKey = "/admin/caiwu")
public class CaiWuController extends BaseController {

	private StatisticService statisticService = enhance(StatisticService.class);
	private OrderItemDao orderItemDao = enhance(OrderItemDao.class);

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
		setAttr("page", statisticService.getCaiWuList(pageable, com.wtshop.util.DateUtils.formatDate(beginDate),com.wtshop.util.DateUtils.formatDate(endDate)));
		render("/admin/caiwu/list.ftl");
	}


	/**
	 * 查看退货统计
	 */
	public void returnList(){
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
		setAttr("page", statisticService.caiWuReturnGoods(pageable, com.wtshop.util.DateUtils.formatDate(beginDate),com.wtshop.util.DateUtils.formatDate(endDate)));
		render("/admin/caiwu/returnlist.ftl");
	}

	public  void   getCaiwuListExcel(){
		String beginDate = getPara("beginDate", null);
		String endDate = getPara("endDate", null);
		List<Record>  list= statisticService.getcaiwuExcelList(beginDate,endDate);
		if (list != null && list.size() > 0) {
			for (Record r : list) {
				long id = r.getLong("id");
				List<OrderItem> itemList = orderItemDao.findOrderItemList(id);
				r.set("orderItem", itemList);
				//计算成本
				BigDecimal cost = new BigDecimal(0);
				for (OrderItem oi : itemList) {
					cost = cost.add(oi.getBigDecimal("cost") == null ? new BigDecimal(0) : oi.getBigDecimal("cost"));
				}
				r.set("cost", cost);
				BigDecimal profit = r.getBigDecimal("amount").subtract(cost);
				r.set("profit", profit);

				Timestamp create_date = r.getTimestamp("create_date");
				String select = com.wtshop.util.DateUtils.dateFormat(create_date);
				r.set("create_date",select);

			}

		}
		String[] header={"订单类型","订单编号","创建日期","会员名称","状态","商品名称","数量","商品单价","使用喵币","消费额","微信支付金额","支付宝支付金额","交易单号","产品成本","产品利润"};
		String[] columns={"type","sn","create_date","name","status","goodsName","quantity","price","miaobi_paid","amount","weiXin_paid","aLi_paid","order_no","cost","profit"};
		Render poirender = PoiRender.me(list).fileName("caiwuOrder.xls").headers(header).sheetName("财务订单").columns(columns);
		render(poirender);

	}

	/**
	 * 退货导出
	 */
	public  void   getReturnListExcel(){
		String beginDate = getPara("beginDate", null);
		String endDate = getPara("endDate", null);
		List<Record>  list= statisticService.caiWuReturnGoodsExcel(beginDate,endDate);


		String[] header={"订单类型","订单编号","退货单编号","会员昵称","退货日期","退货数量","退货金额","退货状态","商品名称"};
		String[] columns={"type","sn","return_sn","nickname","modify_date","quantity","amount","return_type","goodsName"};
		Render poirender = PoiRender.me(list).fileName("caiwuOrder.xls").headers(header).sheetName("财务订单").columns(columns);
		render(poirender);


	}


}