package com.wtshop.cron;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Enhancer;
import com.jfinal.log.Logger;
import com.wtshop.dao.GroupRemindDao;
import com.wtshop.model.GroupRemind;
import com.wtshop.service.InformationService;
import com.wtshop.service.OrderService;
import com.wtshop.util.RedisUtil;


import java.util.List;

import static com.jfinal.aop.Enhancer.enhance;

/**
 * Created by sq on 2017/8/4.
 */
public class DoTimeCronManager implements Runnable {

    private OrderService orderService = enhance(OrderService.class);
    private GroupRemindDao groupRemindDao = Enhancer.enhance(GroupRemindDao.class);
    private InformationService informationService = Enhancer.enhance(InformationService.class);
    Logger logger = Logger.getLogger(DoTimeCronManager.class);

    public void run() {
        /**
         * 用户推送团购定时
         */
        JSONObject redisSetting = JSONObject.parseObject(RedisUtil.getString("redisSetting"));
        Double hour = redisSetting.getDouble("hour")==null?0.00:redisSetting.getDouble("hour");
        List<GroupRemind> groupReminlist1= groupRemindDao.findListNum(hour);
        List<GroupRemind> groupReminlist = groupRemindDao.findListRe(hour);

        for (GroupRemind groupRemind:groupReminlist) {
            groupRemind.setStatus(1);
            try {

                groupRemindDao.update(groupRemind);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }


        logger.info("开始开团前"+hour+"小时极光团购推送服务 ——————————推动了"+groupReminlist1.size()+"个——————————————");
        for (GroupRemind groupRemind:groupReminlist1) {
            long dss=groupRemind.get("number");
            try {
                informationService.groupRmindMessage(groupRemind,dss);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
   }
}
