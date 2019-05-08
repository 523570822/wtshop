package com.wtshop.cron;

import com.jfinal.aop.Enhancer;
import com.jfinal.log.Logger;
import com.jfinal.plugin.cron4j.ITask;
import com.wtshop.dao.GroupRemindDao;
import com.wtshop.service.InformationService;
import com.wtshop.service.OrderService;

import java.util.Date;

import static com.jfinal.aop.Enhancer.enhance;

/**
 * Created by sq on 2017/8/4.
 */
public class YueDuTimeCronManager implements ITask {

    private OrderService orderService = enhance(OrderService.class);
    private GroupRemindDao groupRemindDao = Enhancer.enhance(GroupRemindDao.class);
    private InformationService informationService = Enhancer.enhance(InformationService.class);
    Logger logger = Logger.getLogger(YueDuTimeCronManager.class);

    public void run() {
        new Date();
        logger.info("月度奖励测试"+new Date());
   }

    public void stop() {

    }
}
