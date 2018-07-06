package com.wtshop.controller.admin;

import com.jfinal.ext.render.excel.PoiRender;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.wtshop.model.Goods;
import com.wtshop.service.GoodsService;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Controller - 商品排名
 */
@ControllerBind(controllerKey = "/admin/goods_ranking")
public class GoodsRankingController extends BaseController {

    private GoodsService goodsService = enhance(GoodsService.class);

    /**
     * 列表
     */
    public void list() {
        String rankingTypeName = getPara("rankingType");
        Goods.RankingType rankingType = StrKit.notBlank(rankingTypeName) ? Goods.RankingType.valueOf(rankingTypeName) : null;
        if (rankingType == null) {
            rankingType = Goods.RankingType.sales;
        }
        setAttr("rankingTypes", Goods.RankingType.values());
        setAttr("rankingType", rankingType);

        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.HOUR, 0);
        calendar.setTime(today);
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        Date lastWeekDay = calendar.getTime();
        Date beginDate = getParaToDate("beginDate", lastWeekDay);
        calendar.setTime(today);
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date nextDay = calendar.getTime();
        Date endDate = getParaToDate("endDate", nextDay);

        List dataList = new ArrayList();
        switch (rankingType) {
            case score:
                break;
            case scoreCount:
                break;
            case weekHits:
                break;
            case monthHits:
                break;
            case hits:
                break;
            case weekSales:
                break;
            case monthSales:
                break;
            case sales:
                dataList = Db.find("SELECT t.id , t.sn, t.name,  COUNT(1) count FROM\n" +
                        "(SELECT  o.`sn` order_sn, g.name, p.sn , p.`id`\n" +
                        " FROM `order` o , order_item oi, product p, goods g  " +
                        " WHERE o.`id` = oi.`order_id` AND p.goods_id = g.id  AND p.id = oi.product_id " +
                        "   AND  o.`status` IN (5,9,10) AND o.`create_date` >= ? AND ? >= o.`create_date` \n" +
                        ") t GROUP BY t.id", beginDate, endDate);
                break;
            default:
                break;
        }

        setAttr("dataList", dataList);
        setAttr("beginDate", beginDate);
        setAttr("endDate", endDate);
        render("/admin/goods_ranking/list.ftl");
    }

    /**
     * 导出
     */
    public void download() {
        String rankingTypeName = getPara("rankingType");
        Goods.RankingType rankingType = StrKit.notBlank(rankingTypeName) ? Goods.RankingType.valueOf(rankingTypeName) : null;
        if (rankingType == null) {
            rankingType = Goods.RankingType.sales;
        }
        setAttr("rankingTypes", Goods.RankingType.values());
        setAttr("rankingType", rankingType);

        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.HOUR, 0);
        calendar.setTime(today);
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        Date lastWeekDay = calendar.getTime();
        Date beginDate = getParaToDate("beginDate", lastWeekDay);
        calendar.setTime(today);
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date nextDay = calendar.getTime();
        Date endDate = getParaToDate("endDate", nextDay);

        List dataList = new ArrayList();
        switch (rankingType) {
            case score:
                break;
            case scoreCount:
                break;
            case weekHits:
                break;
            case monthHits:
                break;
            case hits:
                break;
            case weekSales:
                break;
            case monthSales:
                break;
            case sales:
                dataList = Db.find("SELECT t.id , t.sn, t.name,  COUNT(1) count FROM\n" +
                        "(SELECT  o.`sn` order_sn, g.name, p.sn , p.`id`\n" +
                        " FROM `order` o , order_item oi, product p, goods g  " +
                        " WHERE o.`id` = oi.`order_id` AND p.goods_id = g.id  AND p.id = oi.product_id " +
                        "   AND  o.`status` IN (5,9,10) AND o.`create_date` >= ? AND ? >= o.`create_date` \n" +
                        ") t GROUP BY t.id", beginDate, endDate);
                break;
            default:
                break;
        }
        String[] headers = new String[]{"商品编码", "商品名称", "销量"};
        String[] columns = new String[]{"sn", "name", "count"};
        String fileName = "" + DateFormatUtils.format(beginDate, "yyyy_MM_dd") + "-" + DateFormatUtils.format(endDate, "yyyy_MM_dd") + ".xlsx";
        render(PoiRender.me(dataList).fileName(fileName).sheetName("商品排名").headers(headers).columns(columns).cellWidth(9000));
    }


}