package com.wtshop.service;

import com.jfinal.aop.Enhancer;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import com.wtshop.Pageable;
import com.wtshop.dao.MemberDao;
import com.wtshop.dao.OrderDao;
import com.wtshop.dao.OrderItemDao;
import com.wtshop.dao.StatisticDao;
import com.wtshop.model.OrderItem;
import com.wtshop.model.Statistic;
import com.wtshop.util.Assert;
import com.wtshop.util.DateUtils;
import org.apache.commons.collections.map.HashedMap;

import java.math.BigDecimal;
import java.util.*;

/**
 * Service - 统计
 * 
 * 
 */
public class StatisticService extends BaseService<Statistic> {

	/**
	 * 构造方法
	 */
	public StatisticService() {
		super(Statistic.class);
	}

	private StatisticDao statisticDao = Enhancer.enhance(StatisticDao.class);
	private MemberDao memberDao = Enhancer.enhance(MemberDao.class);
	private OrderDao orderDao = Enhancer.enhance(OrderDao.class);
	private OrderItemDao orderItemDao = Enhancer.enhance(OrderItemDao.class);

	/**
	 * 判断统计是否存在
	 *
	 * @param year  年
	 * @param month 月
	 * @param day   日
	 * @return 统计是否存在
	 */
	public boolean exists(int year, int month, int day) {
		return statisticDao.exists(year, month, day);
	}

	/**
	 * 收集
	 *
	 * @param year  年
	 * @param month 月
	 * @param day   日
	 * @return 统计
	 */
	public Statistic collect(int year, int month, int day) {
		Assert.state(month >= 0);
		Assert.state(day >= 0);

		Calendar beginCalendar = Calendar.getInstance();
		beginCalendar.set(year, month, day);
		beginCalendar.set(Calendar.HOUR_OF_DAY, beginCalendar.getActualMinimum(Calendar.HOUR_OF_DAY));
		beginCalendar.set(Calendar.MINUTE, beginCalendar.getActualMinimum(Calendar.MINUTE));
		beginCalendar.set(Calendar.SECOND, beginCalendar.getActualMinimum(Calendar.SECOND));
		Date beginDate = beginCalendar.getTime();

		Calendar endCalendar = Calendar.getInstance();
		endCalendar.set(year, month, day);
		endCalendar.set(Calendar.HOUR_OF_DAY, beginCalendar.getActualMaximum(Calendar.HOUR_OF_DAY));
		endCalendar.set(Calendar.MINUTE, beginCalendar.getActualMaximum(Calendar.MINUTE));
		endCalendar.set(Calendar.SECOND, beginCalendar.getActualMaximum(Calendar.SECOND));
		Date endDate = endCalendar.getTime();

		Statistic statistics = new Statistic();
		statistics.setYear(year);
		statistics.setMonth(month);
		statistics.setDay(day);
		statistics.setRegisterMemberCount(memberDao.registerMemberCount(beginDate, endDate));
		statistics.setCreateOrderCount(orderDao.createOrderCount(beginDate, endDate));
		statistics.setCompleteOrderCount(orderDao.completeOrderCount(beginDate, endDate));
		statistics.setCreateOrderAmount(orderDao.createOrderAmount(beginDate, endDate));
		statistics.setCompleteOrderAmount(orderDao.completeOrderAmount(beginDate, endDate));

		return statistics;
	}

	/**
	 * 分析
	 *
	 * @param period    周期
	 * @param beginDate 起始日期
	 * @param endDate   结束日期
	 * @return 统计
	 */
	public List<Statistic> analyze(Statistic.Period period, Date beginDate, Date endDate, int type) {
		//	return statisticDao.analyze(period, beginDate, endDate);

//		Statistic s1=new  Statistic();
//		s1.setYear(2017); s1.setMonth(8); s1.setDay(9); s1.setRegisterMemberCount(100L); s1.setCreateOrderCount(200L);
//		s1.setCompleteOrderAmount(new BigDecimal(1000.0));
//		s1.setCompleteOrderCount(50L);
//
//		Statistic s2=new  Statistic();
//		s2.setYear(2017); s2.setMonth(1); s2.setDay(11); s2.setRegisterMemberCount(1000L); s2.setCreateOrderCount(200L);
//		s2.setCompleteOrderAmount(new BigDecimal(1000.0));
//		s2.setCompleteOrderCount(50L);
//		list.add(s1);
//		list.add(s2);
//		return  list;

		List rlist = new ArrayList();
		Kv cond = null;

		SqlPara sqlPara = null;

//会员

		if (type == 0) {

			cond = Kv.by("timeType", period.ordinal()).set("beginDate", DateUtils.formatDate(beginDate)).set("endDate", DateUtils.formatDate(endDate));
			sqlPara = Db.getSqlPara("getVIPAdd", cond);
			List<Record> list = Db.find(sqlPara);
			if (list != null && list.size() > 0) {
				for (Record r : list) {
					Statistic s = new Statistic();
					s.setDay(r.getInt("day"));
					s.setMonth(r.getInt("month"));
					s.setYear(r.getInt("year"));
					s.setRegisterMemberCount(r.getBigDecimal("registerMemberCount").longValue());
					rlist.add(s);
				}

			}
		}


		return rlist;

	}

	public List<Record> getAddvipList(String beginDate, String endDate) {
		Kv cond = null;
		SqlPara sqlPara = null;
		cond = Kv.by("beginDate", beginDate).set("endDate", endDate);
		sqlPara = Db.getSqlPara("getVIPAddAll", cond);
		List<Record> list = Db.find(sqlPara);
		return list;
	}


	public Map<String, Object> getGzXf(Statistic.Period period, Date beginDate, Date endDate) {
		Map<String, Object> d = new HashedMap();
		Kv cond = Kv.by("timeType", period.ordinal()).set("beginDate", DateUtils.formatDate(beginDate)).set("endDate", DateUtils.formatDate(endDate));
		SqlPara sqlPara = Db.getSqlPara("getGuanzhuVip", cond);
		List<Record> listgz = Db.find(sqlPara);  //关注用户
		sqlPara = Db.getSqlPara("getXiaoFeiVip", cond);
		List<Record> listxf = Db.find(sqlPara);  //消费用户
		List<String> timeList = new ArrayList<>();
		//获取time列表
		getTimeStr(period, listgz, timeList);
		getTimeStr(period, listxf, timeList);

		List<String> timeListDISTINCT = new ArrayList<String>(new HashSet<String>(timeList));
		Collections.sort(timeListDISTINCT, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				int i1 = Integer.parseInt(o1);
				int i2 = Integer.parseInt(o2);
				return i1 - i2;
			}
		});

		if (timeListDISTINCT.size() > 0) {
			d.put("xTitle", timeListDISTINCT);
			int[] gzResult = new int[timeListDISTINCT.size()];
			int[] xfResult = new int[timeListDISTINCT.size()];
			if (listgz != null && listgz.size() > 0) {
				for (Record r : listgz) {
					int index = getTimeStrIndex(period, r, timeListDISTINCT);
					if (index >= 0) {
						gzResult[index] = r.getLong("num").intValue();
					}
				}
			}

			if (listxf != null && listxf.size() > 0) {
				for (Record r : listxf) {
					int index = getTimeStrIndex(period, r, timeListDISTINCT);
					if (index >= 0) {
						xfResult[index] = r.getLong("num").intValue();
					}
				}
			}

			d.put("gzResult", gzResult);
			d.put("xfResult", xfResult);
		} else {
			d.put("gzResult", new int[0]);
			d.put("xfResult", new int[0]);
			d.put("xTitle", new String[0]);
		}


		return d;
	}

	;


	//抽取list中的时间拼接字段
	private void getTimeStr(Statistic.Period period, List<Record> list, List<String> timeList) {
		if (list != null && list.size() > 0) {
			for (Record r : list) {
				switch (period.ordinal()) {
					case 0: //年
						timeList.add(r.getInt("year") + "");
						break;
					case 1: //月
						timeList.add(r.getInt("year") + buquan(r.getInt("month")));
						break;
					case 2:  //日
						timeList.add(r.getInt("year") + buquan(r.getInt("month")) + buquan(r.getInt("day")));
						break;
				}
			}
		}
	}

	//获取时间在timelist的index
	private int getTimeStrIndex(Statistic.Period period, Record r, List<String> timeList) {
		String timeStr = "";
		switch (period.ordinal()) {
			case 0: //年
				timeStr = Integer.toString(r.getInt("year"));
				break;
			case 1: //月
				timeStr = r.getInt("year") + buquan(r.getInt("month"));
				break;
			case 2:  //日
				timeStr = r.getInt("year") + buquan(r.getInt("month")) + buquan(r.getInt("day"));
				break;
		}

		int index = timeList.indexOf(timeStr);
		return index;
	}

	//补全2位
	private String buquan(Integer s) {
		String rs = Integer.toString(s);
		if (rs.length() == 1) {
			rs = "0" + rs;
		}
		return rs;
	}

	//导出关注用户
	public List<Record> getGzVipListExcel(String beginDate, String endDate) {
		Kv cond = null;
		SqlPara sqlPara = null;
		cond = Kv.by("beginDate", beginDate).set("endDate", endDate);
		sqlPara = Db.getSqlPara("getGzVipListExcel", cond);
		List<Record> list = Db.find(sqlPara);
		return list;
	}


	//导出消费用户
	public List<Record> getXfVipListExcel(String beginDate, String endDate) {
		Kv cond = null;
		SqlPara sqlPara = null;
		cond = Kv.by("beginDate", beginDate).set("endDate", endDate);
		sqlPara = Db.getSqlPara("getXfVipListExcel", cond);
		List<Record> list = Db.find(sqlPara);
		return list;
	}


	//获取订单
	public Map<String, Object> getOrderStatistic(Statistic.Period period, Date beginDate, Date endDate) {
		//获取创建订单数量
		{
			Map<String, Object> d = new HashedMap();
			Kv cond = Kv.by("timeType", period.ordinal()).set("beginDate", DateUtils.formatDate(beginDate)).set("endDate", DateUtils.formatDate(endDate));
			SqlPara sqlPara = Db.getSqlPara("createOrderNum", cond);
			List<Record> createOrderNumList = Db.find(sqlPara);  //创建订单数量
			sqlPara = Db.getSqlPara("completeOrderNum", cond);
			List<Record> completeOrderNumList = Db.find(sqlPara);  //完成订单数量

			sqlPara = Db.getSqlPara("createOrderMoney", cond);
			List<Record> createOrderMoneyList = Db.find(sqlPara);  //创建订单金额

			sqlPara = Db.getSqlPara("completeOrderMoney", cond);
			List<Record> completeOrderMoneyList = Db.find(sqlPara);  //完成订单金额
			List<String> timeList = new ArrayList<>();
			//获取time列表
			getTimeStr(period, createOrderNumList, timeList);
			getTimeStr(period, completeOrderNumList, timeList);
			getTimeStr(period, createOrderMoneyList, timeList);
			getTimeStr(period, completeOrderMoneyList, timeList);

			List<String> timeListDISTINCT = new ArrayList<String>(new HashSet<String>(timeList));
			Collections.sort(timeListDISTINCT, new Comparator<String>() {
				@Override
				public int compare(String o1, String o2) {
					int i1 = Integer.parseInt(o1);
					int i2 = Integer.parseInt(o2);
					return i1 - i2;
				}
			});

			if (timeListDISTINCT.size() > 0) {
				d.put("xTitle", timeListDISTINCT);
				int[] result1 = new int[timeListDISTINCT.size()];
				int[] result2 = new int[timeListDISTINCT.size()];
				double[] result3 = new double[timeListDISTINCT.size()];
				double[] result4 = new double[timeListDISTINCT.size()];
				if (createOrderNumList != null && createOrderNumList.size() > 0) {
					for (Record r : createOrderNumList) {
						int index = getTimeStrIndex(period, r, timeListDISTINCT);
						if (index >= 0) {
							result1[index] = r.getBigDecimal("num").intValue();
						}
					}
				}

				if (completeOrderNumList != null && completeOrderNumList.size() > 0) {
					for (Record r : completeOrderNumList) {
						int index = getTimeStrIndex(period, r, timeListDISTINCT);
						if (index >= 0) {
							result2[index] = r.getBigDecimal("num").intValue();
						}
					}
				}


				if (createOrderMoneyList != null && createOrderMoneyList.size() > 0) {
					for (Record r : createOrderMoneyList) {
						int index = getTimeStrIndex(period, r, timeListDISTINCT);
						if (index >= 0) {
							result3[index] = r.getBigDecimal("amount").doubleValue();
						}
					}
				}

				if (completeOrderMoneyList != null && completeOrderMoneyList.size() > 0) {
					for (Record r : completeOrderMoneyList) {
						int index = getTimeStrIndex(period, r, timeListDISTINCT);
						if (index >= 0) {
							result4[index] = r.getBigDecimal("amount").doubleValue();
						}
					}
				}

				d.put("result1", result1);
				d.put("result2", result2);
				d.put("result3", result3);
				d.put("result4", result4);
			} else {
				d.put("result1", new int[0]);
				d.put("result2", new int[0]);
				d.put("result3", new double[0]);
				d.put("result4", new double[0]);
				d.put("xTitle", new String[0]);
			}


			return d;
		}
	}


	//财务订单统计

	public Page getCaiWuList(Pageable pageable, String beginTime, String endTime) {
		Kv cond = Kv.by("beginDate", beginTime).set("endDate", endTime);
		SqlPara sqlPara = Db.getSqlPara("getCaiWuListOrder", cond);
		Page page = Db.paginate(pageable.getPageNumber(), pageable.getPageSize(), sqlPara);
		List<Record> list = page.getList();
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

			}

		}
		return page;
	}

	//财务导出

	public List<Record> getcaiwuExcelList(String beginTime, String endTime) {
		Kv cond = Kv.by("beginDate", beginTime).set("endDate", endTime);
		SqlPara sqlPara = Db.getSqlPara("getCaiwuListExcel", cond);
		List<Record> list = Db.find(sqlPara);
		return list;

	}

	//财务退货统计

	public Page caiWuReturnGoods(Pageable pageable, String beginTime, String endTime) {
		Kv cond = Kv.by("beginDate", beginTime).set("endDate", endTime);
		SqlPara sqlPara = Db.getSqlPara("caiWuReturnGoods", cond);
		Page page = Db.paginate(pageable.getPageNumber(), pageable.getPageSize(), sqlPara);
		List<Record> list = page.getList();
		return page;
	}

	//导出财务退货统计

	private  void setcaiWuReturnGoodsList(List<Record> list ){
		if (list !=null && list.size()>0){
			for (Record r:list){
				r.set("return_type","已完成");
			}
		}
	}
	public List<Record> caiWuReturnGoodsExcel(String beginTime, String endTime) {
		Kv cond = Kv.by("beginDate", beginTime).set("endDate", endTime);
		SqlPara sqlPara = Db.getSqlPara("caiWuReturnGoodsExcel", cond);
		List<Record> list = Db.find(sqlPara);
		setcaiWuReturnGoodsList(list);
		return list;
	}


	}