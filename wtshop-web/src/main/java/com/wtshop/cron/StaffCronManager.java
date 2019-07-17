package com.wtshop.cron;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Enhancer;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.log.Logger;
import com.jfinal.plugin.cron4j.ITask;
import com.sun.tools.corba.se.idl.constExpr.Or;
import com.wtshop.CommonAttributes;
import com.wtshop.Setting;
import com.wtshop.constants.Code;
import com.wtshop.dao.*;
import com.wtshop.model.*;
import com.wtshop.service.*;
import com.wtshop.util.DateUtils;
import com.wtshop.util.RedisUtil;
import com.wtshop.util.SystemUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 描述：
 *
 * @author Shi Qiang
 * @date 2018/4/11 9:48
 */
public class StaffCronManager implements ITask{
    Logger logger = Logger.getLogger(StaffCronManager.class);

    private MemberService memberService = Enhancer.enhance(MemberService.class);
    private CommissionLogService commissionLogService = Enhancer.enhance(CommissionLogService.class);
    private GroupRemindDao groupRemindDao = Enhancer.enhance(GroupRemindDao.class);
    public void stop() {
    }
    public void run() {
        logger.info("开始执行定时任务!!!!!!!!");
        Prop prop = PropKit.use(CommonAttributes.wtshop_PROPERTIES_PATH);
        if (!prop.getBoolean("commission.enable")){
            logger.info("当前服务配置为关闭分佣");
            return;
        }






        logger.info("开始极光推送服务————————————————————————");

        /**
         * 购买普通商品分佣
         */
        List<CommissionLog> commlogList=commissionLogService.findByStatus();
       for (CommissionLog commlog:commlogList){
            Member staff = memberService.find(commlog.getMemberId());
            staff.setCommission(staff.getCommission().add(commlog.getCredit()));
            staff.setCommissionUnarrived(staff.getCommissionUnarrived().subtract(commlog.getCredit()));
            commlog.setStatus(1);
            commissionLogService.update(commlog);
            memberService.update(staff);

        }
        logger.info("结束极光推送服务————————————————————————");

        return;


    }
}
