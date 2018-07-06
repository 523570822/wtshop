package com.wtshop.service;

import com.jfinal.aop.Before;
import com.jfinal.aop.Enhancer;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.wtshop.constants.Code;
import com.wtshop.dao.*;
import com.wtshop.model.*;
import com.wtshop.util.ApiResult;
import com.wtshop.util.DateUtils;
import com.wtshop.util.StringUtils;
import com.wtshop.util.UUIDUtils;
import org.apache.commons.collections.map.HashedMap;

import java.util.*;

/**
 * Created by Administrator on 2017/8/10.
 */
@Before(Tx.class)
public class TicketSnService extends BaseService<Ticketsn>  {
    private TicketSnDao ticketSnDao=	 Enhancer.enhance(TicketSnDao.class);
    private TicketconfigDao configDao=	 Enhancer.enhance(TicketconfigDao.class);
    private TicketSnRecordDao ticketSnRecordDao=	 Enhancer.enhance(TicketSnRecordDao.class);
    private MemberService memberService = Enhancer.enhance(MemberService.class);
    private TicketService ticketService =	 Enhancer.enhance(TicketService.class);
    private TicketReceiveDao ticketReceiveDao =	 Enhancer.enhance(TicketReceiveDao.class);
    private MemberDao memberDao=Enhancer.enhance(MemberDao.class);
    private CouponCodeService css = Enhancer.enhance(CouponCodeService.class);
    private   static Object sharelock =new Object();
    public TicketSnService() {
        super(Ticketsn.class);
    }

    public  Ticketsn getTicketsnBySn(String sn){
        return  ticketSnDao.getTicketsnBySn(sn);
    }


    //生成sn码
    public ApiResult generateLink(String orderId){
        Ticketsn ticketsn=getTicketSnByOrderId(orderId);
        if (ticketsn!=null){
            return  ApiResult.fail("该订单已被分享,无法再次分享");
        }
        ticketsn =new Ticketsn();
        Ticketconfig  ticketconfig=  getCurrentConfig();
        if (ticketconfig==null){
            return  ApiResult.fail("当前没有分享券的活动");
        }
        ticketsn.setConfigId(ticketconfig.getLong("id"));
        Member m=memberService.getCurrent();
        ticketsn.setMemberId(m.getId());
        ticketsn.setOrderId(Long.parseLong(orderId));
        ticketsn.setSn( UUIDUtils.getLongUUID());
        ticketsn.setMaxShareNum(ticketconfig.getShareLimit());
        ticketSnDao.save(ticketsn);
        return ApiResult.success(ticketsn.getSn());
    }

    public  Ticketsn getTicketSnByOrderId(String orderId){
        String sql="SELECT * from  ticketsn WHERE orderId="+orderId;
        Ticketsn t=ticketSnDao.findBySql(sql);
        return  t;

    };




    public Ticketconfig getCurrentConfig(){

        long curr=System.currentTimeMillis()/1000;
        String sql="SELECT * FROM ticketconfig WHERE    '"+curr+"'  >=unix_timestamp(IFNULL(beginTime,0)) AND  '" +
                curr+"'  <=unix_timestamp(IFNULL(endTime,0))    AND state=0 ";

        return  (Ticketconfig)configDao.findBySql(sql) ;

    }

    //获取分享详情
    public ApiResult checkSn(String sn){
        Ticketsn ticketsn=ticketSnDao.getTicketsnBySn(sn);
        if (ticketsn==null){
            return       ApiResult.fail("分享snid不存在");
        }
        Ticketconfig ticketconfig=configDao.find(ticketsn.getConfigId());

        if (ticketconfig==null){
            return       ApiResult.fail("活动不存在");
        }

        if (ticketconfig.getState()!=0){
            return       ApiResult.fail("活动已停止");
        }

        Date now=new Date();
        if (ticketconfig.getBeginTime().getTime()>now.getTime()){
            return       ApiResult.fail("活动未开始");
        }

        if (ticketconfig.getEndTime().getTime()<now.getTime()){
            return       ApiResult.fail("活动已结束");
        }
        Map<String,Object> d=new HashedMap();
        d.put("ticketconfig",ticketconfig);
        d.put("ticketsn",ticketsn);
        return   ApiResult.success(d);
    }





//获取抽券信息
    @Before(Tx.class)
    public ApiResult extractTicket(String phone,String sn,String ip){

        ApiResult r=checkSn(sn);
        if (r.getCode()== Code.FAIL){
            return  r;
        }



       Map map=(Map)r.getData();
        Ticketsn ticketsn=(Ticketsn)map.get("ticketsn");
        Ticketconfig ticketconfig=(Ticketconfig)map.get("ticketconfig");
        Member member=null;
        List<Record> RticketList= null;

            synchronized (sharelock){
                ticketsn=ticketSnDao.find(ticketsn.getId());
                if (ticketsn.getCurrentNum()>ticketsn.getMaxShareNum()){
                    return  ApiResult.fail("当前分享活动已达到最大限制");
                }

                member=memberDao.findByUsername(phone);
                if (member !=null){
                    long c=ticketReceiveDao.chcekRepeat(member.getId(),sn);
                    if (c>0){
                        return ApiResult.fail("该会员已经领取次券,不能重复领取");
                    }
                }

                ticketsn.setCurrentNum(ticketsn.getCurrentNum()+1);
                ticketsn.update();
            }
            if (member==null){
                memberService.register(phone, System.currentTimeMillis()+"" , ip);
                member=memberDao.findByUsername(phone);
            }

            //生成抽券表
            List<Ticket> ticketList=ticketService.getListByConfigId(ticketsn.getConfigId());
            if (ticketList==null || ticketList.size()<1){
                throw new  RuntimeException("当前活动没有设置优惠券");
            }


            List<Ticketsnrecord> listR=new ArrayList<>();
            List<Ticket> newTicketList=new ArrayList<>();
            int num=ticketList.size();
            if (ticketconfig.getNum()>num){
                ticketconfig.setNum(num);
            }

            for (int i = 0; i < ticketconfig.getNum(); i++) {
                Random random=new Random();
                int index=random.nextInt(num);
                newTicketList.add(ticketList.get(index));
            }

            for (int i = 0; i <newTicketList.size() ; i++) {
                Ticket e=newTicketList.get(i);
                Ticketsnrecord record=new Ticketsnrecord();
                record.setTicketSnId(ticketsn.getId());
                record.setTicketId(e.getLong("id"));
                record.setTicketName(e.getStr("name"));
                record.setOrderId(ticketsn.getOrderId());
                record.setModifyDate(new Date());
                record.setCreateDate(new Date());
                record.setVersion(1L);
                record.setMemId(member.getId());
                listR.add(record);
            }
            Db.batchSave(listR,listR.size());


            List<Ticketsnrecord> ticketsnrecords =listR;//ticketSnRecordDao.getListBySnId(ticketsn.getId());

            String ids="";
            for (int i = 0; i <ticketsnrecords.size() ; i++) {
                    ids+=ticketsnrecords.get(i).getTicketId()+",";
            }
            if (!StringUtils.isEmpty(ids)){
                ids=ids.substring(0,ids.length()-1);
            }

            //   List<Ticket> ticketList=ticketService.findListByIds(ids);
            RticketList = new ArrayList<>();
            for (int i = 0; i <ticketsnrecords.size() ; i++) {
                Record re=Db.findById("ticket",ticketsnrecords.get(i).getTicketId());
                Ticketsnrecord te=   ticketsnrecords.get(i);
                re.set("ticketsnrecordId",te.getId());
                re.set("currentNum",te.getCurrentNum());
                //插入领取表和我的优惠券表
                Ticketreceive ticketreceive=new   Ticketreceive();
                ticketreceive.setMemberId(member.getId());
                ticketreceive.setTicketsnrecordId(te.getId());
                //领取后有效天数,与开始结束时间不能同时存在
                if (  re.get("effectiveDay")!=null&& re.getInt("effectiveDay")>0){
                    Date now=new Date();
                    ticketreceive.setRealSTime(now);
                    ticketreceive.setRealETime(DateUtils.addDate(now,Calendar.DATE,re.getInt("effectiveDay")));
                }else {
                    ticketreceive.setRealSTime(re.getDate("begin_date"));
                    ticketreceive.setRealETime(re.getDate("end_date"));
                }
                ticketreceive.setModulus(re.getStr("modulus"));
                ticketreceive.setMoney(re.getStr("money"));
                ticketreceive.setCondition(re.getStr("condition"));
                ticketreceive.setType(re.getInt("type"));
                ticketreceive.setName(re.getStr("name"));
                ticketreceive.setProductId(re.getLong("product_id"));
                ticketreceive.setProductCategoryId(re.getLong("product_category_id"));
                ticketreceive.setImage(re.getStr("image"));
                ticketreceive.setStatus(re.get("status"));
                ticketreceive.setIsEnabled(re.get("is_enabled"));
                ticketreceive.setIsExchange(re.get("is_exchange"));

                if( !re.getStr("money").equals("0") && re.getStr("modulus").equals("10")){
                    ticketreceive.setDesc("满" + re.getStr("condition") +"元减" + re.getStr("money")+"元");
                }else if( re.getStr("money").equals("0") && !re.getStr("modulus").equals("10")){
                    ticketreceive.setDesc("满" + re.getStr("condition") +"元打" +re.getStr("modulus")+"折");
                }else if( !re.getStr("money").equals("0") && !re.getStr("modulus").equals("10")){
                    ticketreceive.setDesc("满" + re.getStr("condition") +"元打" + re.getStr("modulus")+"折"+"再减"+ re.getStr("money")+"元");
                }
                ticketReceiveDao.save(ticketreceive);
                css.generate(re,member,re.getLong("id"),ticketreceive.getId());
                re.set("begin_date",ticketreceive.getRealSTime());
                re.set("end_date",ticketreceive.getRealETime());
                RticketList.add(re);
            }



        Map<String,Object> d=new HashedMap();
        d.put("ticketList",RticketList);
        d.put("ticketconfig",ticketconfig);

         return  ApiResult.success(d);

    }


}
