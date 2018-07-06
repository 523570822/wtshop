package com.wtshop.controller.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.Kv;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import com.wtshop.Pageable;
import com.wtshop.model.Statistic;
import com.wtshop.util.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;
import java.util.List;


/**
 * Created by 蔺哲 on 2017/5/10.
 * 倒拍控制器
 */
@ControllerBind(controllerKey = "/admin/reverseOrder")
public class ReverseOrderController extends BaseController {
    /**
     * 列表
     */
    public void list(){
        Pageable pageable = getBean(Pageable.class);
        setAttr("pageable", pageable);
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
        setAttr("pageable", pageable);

        String id=getPara("orderid");
        setAttr("page", getpage( pageable,  id,  com.wtshop.util.DateUtils.formatDate(beginDate),  com.wtshop.util.DateUtils.formatDate(endDate)));
        setAttr("orderid",id);


        render("/admin/reverseAuction/orderlist.ftl");
     //   renderJson(getAttr("page"));
    }

    private  Page  getpage(Pageable pageable ,String id,String beginTime,String endTime){
        Kv cond = Kv.by("beginDate", beginTime).set("endDate", endTime).set("id",id);
        SqlPara sqlPara = Db.getSqlPara("daopaiOrder", cond);
        Page page = Db.paginate(pageable.getPageNumber(), pageable.getPageSize(), sqlPara);
        List<Record> list = page.getList();
        if (list != null && list.size() > 0) {
            for (Record r : list) {
                String gv=r.get("name"); //名称规格
                String spec=r.getStr("specification_values");
                if (!StringUtils.isEmpty(spec)){
                    List<String> sl= JSON.parseArray(spec,String.class);
                    for (String s:sl){
                        JSONObject j= (JSONObject) JSON.parse(s);
                        gv=gv+"      " +j.getString("value");
                    }
                }
                r.set("gv",gv);

            }

        }
        return  page;
    }
    

}
