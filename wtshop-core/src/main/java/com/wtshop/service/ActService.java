package com.wtshop.service;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.wtshop.exception.AppRuntimeException;
import com.wtshop.util.ApiResult;
import com.wtshop.util.DateUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/10.
 */

//通用活动服务
public class ActService {

    //新增检测活动时间
    public static ApiResult addCheckActTime(Date begin, Date end, Map<String, String> sqlmap) {
        if (new Date().getTime() > begin.getTime()) {
            throw new AppRuntimeException("开始时间应大于当前时间");
//            return ApiResult.fail("开始时间应大于当前时间");
        }
        if (begin.getTime() >= end.getTime()) {
            return ApiResult.fail("结束时间应大于开始时间");
        } else {
            String beginDateS = DateUtils.dateFormat(begin);
            //设置字段
            String eTime = sqlmap.get("eTime");
            String condition = sqlmap.get("condition");
            String table = sqlmap.get("table");
            String beginsql = " SELECT  unix_timestamp('" + beginDateS + "')  >unix_timestamp(IFNULL(MAX(" + eTime + "),0)) flag ,    unix_timestamp(IFNULL(MAX(" + eTime + "),0)) endtime  " +
                    " FROM " + table + " WHERE  1=1 " + condition;
            List<Record> list = Db.find(beginsql);
            if (list.get(0).getLong("flag") == 0) {
                return ApiResult.fail("开始时间必须大于" + DateUtils.getTime(list.get(0).getLong("endtime")));
            }
        }
        return ApiResult.success("ok");
    }

    //更新时检测
    public static ApiResult updateCheckActTime(Date begin, Date end, long id, Map<String, String> sqlmap) {

        if (new Date().getTime() > begin.getTime()) {
            return ApiResult.fail("开始时间应大于当前时间");
        }

        if (begin.getTime() >= end.getTime()) {
            return ApiResult.fail("结束时间应大于开始时间");
        } else {
            //设置字段
            String sTime = (String) sqlmap.get("sTime");
            String eTime = (String) sqlmap.get("eTime");
            String condition = (String) sqlmap.get("condition");
            String table = (String) sqlmap.get("table");
            String sql = "SELECT    unix_timestamp(IFNULL(MAX(" + eTime + "),0))   FROM  " + table + " where id=( SELECT id FROM " + table + " WHERE 1=1 " + condition + "   and id >" + id + "  LIMIT 1 )";
            long endTime = Db.queryLong(sql);
            if (endTime > 0 && begin.getTime() > endTime) {
                return ApiResult.fail("开始时间不能大于" + DateUtils.getTime(endTime));
            }

        }


        return ApiResult.success("ok");
    }


}
