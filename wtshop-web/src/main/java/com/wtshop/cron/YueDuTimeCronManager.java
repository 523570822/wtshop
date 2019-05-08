package com.wtshop.cron;

import com.jfinal.aop.Enhancer;
import com.jfinal.log.Logger;
import com.jfinal.plugin.cron4j.ITask;
import com.wtshop.dao.DepositLogDao;
import com.wtshop.dao.GroupRemindDao;
import com.wtshop.model.DepositLog;
import com.wtshop.model.Member;
import com.wtshop.service.InformationService;
import com.wtshop.service.MemberService;
import com.wtshop.service.OrderService;
import com.wtshop.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static com.jfinal.aop.Enhancer.enhance;

/**
 * Created by sq on 2017/8/4.
 */
public class YueDuTimeCronManager implements ITask {
    Logger logger = Logger.getLogger(YueDuTimeCronManager.class);
    private MemberService memberService = Enhancer.enhance(MemberService.class);
    private DepositLogDao depositLogDao = Enhancer.enhance(DepositLogDao.class);
    public void run() {

//计算金牌的奖励
      Long  housekeeperId=3l;
       List<Member> memberList=memberService.findMemberByHousekeeperId(housekeeperId);
for (Member member:memberList){
    if(!StringUtils.isEmpty(member.get("zprice"))){
       int jinglijin=0;
  //计算月度奖励
        if(Double.parseDouble(member.get("zprice")+"")>6000&&Double.parseDouble(member.get("zprice")+"")<10000){
            jinglijin=100;
        }else if(Double.parseDouble(member.get("zprice")+"")>10000&&Double.parseDouble(member.get("zprice")+"")<30000){
            jinglijin=200;
        }else if(Double.parseDouble(member.get("zprice")+"")>30000&&Double.parseDouble(member.get("zprice")+"")<50000){
            jinglijin=600;
        }else if(Double.parseDouble(member.get("zprice")+"")>50000&&Double.parseDouble(member.get("zprice")+"")<100000){
            jinglijin=1000;
        }else if(Double.parseDouble(member.get("zprice")+"")>100000){
            jinglijin=2500;
        }
if (jinglijin>0){
        DepositLog depositLog5 = new DepositLog();
        depositLog5.setCredit(BigDecimal.valueOf(jinglijin));
        member.setBalance(BigDecimal.valueOf(jinglijin).add(member.getBalance()));
        depositLog5.setBalance(member.getBalance());
        depositLog5.setDebit(BigDecimal.ZERO);
        depositLog5.setStatus(1);
    depositLog5.setPerformance(BigDecimal.valueOf(Double.parseDouble(member.get("zprice")+"")));
        depositLog5.setMemo("月度奖励奖励");
        depositLog5.setType(DepositLog.Type.yuedu.ordinal());
        depositLog5.setMemberId(member.getId());
        memberService.update(member);
        depositLogDao.save(depositLog5);
}
    };
}
//计算白金的奖励
         housekeeperId=4l;
        memberList=memberService.findMemberByHousekeeperId(housekeeperId);
        for (Member member:memberList){
            double jinglijin=0;
            if(!StringUtils.isEmpty(member.get("zcommission"))){

                if(Double.parseDouble(member.get("zcommission")+"")>0){
                    jinglijin=Double.parseDouble(member.get("zcommission")+"")*0.1;
                }
                if (jinglijin>0){
                    DepositLog depositLog5 = new DepositLog();
                    depositLog5.setCredit(BigDecimal.valueOf(jinglijin));
                    member.setBalance(BigDecimal.valueOf(jinglijin).add(member.getBalance()));
                    depositLog5.setBalance(member.getBalance());
                    depositLog5.setDebit(BigDecimal.ZERO);
                    depositLog5.setPerformance(BigDecimal.valueOf(Double.parseDouble(member.get("zcommission")+"")));
                    depositLog5.setStatus(1);
                    depositLog5.setMemo("月度奖励奖励");
                    depositLog5.setType(DepositLog.Type.yuedu.ordinal());
                    depositLog5.setMemberId(member.getId());
                    memberService.update(member);
                    depositLogDao.save(depositLog5);
                }
            };
        }
        housekeeperId=5l;
        memberList=memberService.findMemberByHousekeeperId(housekeeperId);
        for (Member member:memberList){
            double jinglijin=0;
            if(!StringUtils.isEmpty(member.get("zcommission"))){

                if(Double.parseDouble(member.get("zcommission")+"")>0){
                    jinglijin=Double.parseDouble(member.get("zcommission")+"")*0.1;
                }
                if (jinglijin>0){
                    DepositLog depositLog5 = new DepositLog();
                    depositLog5.setCredit(BigDecimal.valueOf(jinglijin));
                    member.setBalance(BigDecimal.valueOf(jinglijin).add(member.getBalance()));
                    depositLog5.setBalance(member.getBalance());
                    depositLog5.setPerformance(BigDecimal.valueOf(Double.parseDouble(member.get("zcommission")+"")));
                    depositLog5.setDebit(BigDecimal.ZERO);
                    depositLog5.setStatus(1);
                    depositLog5.setMemo("月度奖励奖励");
                    depositLog5.setType(DepositLog.Type.yuedu.ordinal());
                    depositLog5.setMemberId(member.getId());
                    memberService.update(member);
                    depositLogDao.save(depositLog5);
                }
            };
        }
        logger.info("月度奖励测试"+new Date());
   }

    public void stop() {

    }
}
