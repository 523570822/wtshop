package com.wtshop.api.controller;

import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.wtshop.Pageable;
import com.wtshop.model.*;
import com.wtshop.util.ApiResult;
import com.wtshop.api.interceptor.ErrorInterceptor;
import com.wtshop.interceptor.WapInterceptor;
import com.wtshop.service.*;
import com.wtshop.util.DateUtils;
import com.wtshop.util.RandomUtils;
import com.wtshop.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by sq on 2017/6/8.
 */
@ControllerBind(controllerKey = "/api/activity")
@Before({WapInterceptor.class, ErrorInterceptor.class} )
public class ActivityAPIController extends  BaseAPIController{

    private GoodsService goodsService = enhance(GoodsService.class);
    private ActivityService activityService = enhance(ActivityService.class);
    private RaffleService raffleService = enhance(RaffleService.class);

    private FuDaiProductService productService = enhance(FuDaiProductService.class);
    private MemberService memberService = enhance(MemberService.class);
    private MiaobiLogService miaobiLogService = enhance(MiaobiLogService.class);
    private ActivityProductService activityProductService = enhance(ActivityProductService.class);
  private FullReductionService fullReductionService =enhance(FullReductionService.class);
    private FullAntiService fullAntiService=enhance(FullAntiService.class);
  public void fullReduction(){

      List<FullReduction> kkk = fullReductionService.findAll();
      renderJson(ApiResult.success(kkk));

  }

    /**
     * 线下满反
     */
    public void fullAnti(){

     //   List<FullAnti> kkk = fullAntiService.findAll();
        List<Record> totalMoneyList=fullAntiService.findTotalMoney();
        List<Map> list=new ArrayList<>();

        for (Record record : totalMoneyList) {
            Map<Object ,Object> kkk=new HashMap<>();
            Object  totalMoney= record.get("total_money");
            List<FullAnti> fullAntisList=fullAntiService.findTotalMoneyList(totalMoney);
            kkk.put("fullAntisList",fullAntisList);
            kkk.put("totalMoney","满"+totalMoney+"活动");
            list.add(kkk);
        }
        renderJson(ApiResult.success(list));

    }
    /**
     * 猜你喜欢
     */
    public void likeList(){

        List<Goods> likeList = goodsService.findLikeList();
        renderJson(ApiResult.success(likeList));
    }

    public void manActivity(){
        Integer pageNumber = getParaToInt("pageNumbers", 1);
        Integer pageSize =  999999 ;
        Pageable pageable = new Pageable(pageNumber, pageSize);
        Page<Goods> goodsByPromId = goodsService.findGoodsByPromId(5L,pageable);
        renderJson(ApiResult.success(goodsByPromId));
    }

    public void testAct(){

        productService.lotteryProduct(5,162,13);


    }
    /**
     *点击抽奖接口
     * status（0：成功，1：失败，没有次数）
     * Ranking（名次）
      */
    public void lottery() {
        Long id = getParaToLong("id");

        BigDecimal[] point={ new BigDecimal("50"), new BigDecimal("100"), new BigDecimal("200"), new BigDecimal("0"), new BigDecimal("200"), new BigDecimal("100"), new BigDecimal("50"), new BigDecimal("0")};
        Map<String, Object> map = new HashMap<String, Object>();
        Member member = memberService.getCurrent();





        Activity activity = activityService.find(id);

        Date  time=new Date();
        Date form=activity.getBeginDate();
        Date to = activity.getEndDate();
        if(form!=null&&to!=null&&activity.getStatus()==1){
            int state= DateUtils.belongCalendar(time,form,to);
            if(state==-1){
                map.put("Ranking","");
                map.put("status","3");
                String msg="活动未开始";
                renderJson(ApiResult.success(map,msg));
                return;
            }else if(state==1){
                map.put("Ranking","");
                map.put("status","2");
                String msg="活动已结束";
                renderJson(ApiResult.success(map,msg));
                return;
            }



        }else if (activity.getStatus()==0){
            map.put("Ranking","");
            map.put("status","4");
            String msg="活动以关闭";
            renderJson(ApiResult.success(map,msg));
            return;

        }


        int num = raffleService.findByActivityIdAndMem(activity.getId(), member.getId());
                if(num>0){
                    map.put("Ranking","");
                    map.put("status","0");
                    String msg="已经抽取过了";
                    renderJson(ApiResult.success(map,msg));
                    return;
                }
        int max = 7;
        int min = 0;
        int s=0;
        Random random = new Random();
        //获取现在抽奖人数
        int sm=activity.getNowNumber()+1;
        //获取幸运数字
        String lucky = activity.getLuckyNumber();
        String[] strs=lucky.split("\\*");
        String[] serialNumber=lucky.split("\\*");

        //判斷是否是大獎
        Boolean isZ=StringUtils.useSet(strs,sm+"");
        List<ActivityProduct> activityProductL = activity.getActivityProduct();


       ActivityProduct activityProductN = new ActivityProduct();
       Raffle  raffle=new Raffle();
        //随机获取的奖品
        if(isZ&&activityProductL!=null&&activityProductL.size()>0){
             activityProductN= (ActivityProduct) RandomUtils.createRandomList(activityProductL,1).get(0);
             s=activityProductN.getSerialNumber();
            raffle.setIsReal(Raffle.IsReal.yes.ordinal());
            raffle.setIssue(Raffle.IsReal.no.ordinal());
            raffle.setActivityProductId(activityProductN.getId());

            activityProductN.setPtNum(activityProductN.getPtNum()+1);
            activityProductService.update(activityProductN);

        }else{
            int i=0;
            for (ActivityProduct activityProduct:activityProductL) {
                if(activityProduct.getSerialNumber()!=null){
                    serialNumber[i]=activityProduct.getSerialNumber()+"";
                    i++;
                }
            }
           s = random.nextInt(max)%(max-min+1) + min;

            Boolean serialNumberB=StringUtils.useSet(serialNumber,s+"");
            while (serialNumberB){
               s = random.nextInt(max)%(max-min+1) + min;

                 serialNumberB=StringUtils.useSet(serialNumber,s+"");

                 }

            raffle.setIsReal(Raffle.IsReal.no.ordinal());
            raffle.setIssue(Raffle.IsReal.yes.ordinal());

        }
        raffle.setPoint(point[s]);
        raffle.setActivityId(activity.getId());
        raffle.setMemberId(member.getId());

        raffleService.save(raffle);

        //增加抽奖人数
        activity.setNowNumber(activity.getNowNumber()+1);
        activityService.update(activity);

        //判断喵币如果不是0 增加喵币记录
        if(point[s]!=BigDecimal.ZERO){

            BigDecimal sendMiaoBi =point[s];
            MiaobiLog miaobiLog = new MiaobiLog();
            miaobiLog.setMemberId(member.getId());
            miaobiLog.setCredit(sendMiaoBi);
            miaobiLog.setDebit(BigDecimal.ZERO);
            miaobiLog.setType(0);
            miaobiLog.setMemo("订单赠送");
            miaobiLog.setBalance(member.getPoint().add(sendMiaoBi).setScale(2, BigDecimal.ROUND_HALF_UP));
            miaobiLogService.save(miaobiLog);
            //更新用户喵币
            member.setPoint(member.getPoint().add(sendMiaoBi).setScale(2, BigDecimal.ROUND_HALF_UP));
            memberService.update(member);
        }
         if(isZ){
                map.put("PName",activityProductN.getProduct().getName());
           }else {
                map.put("PName","");
             }

        map.put("isz",isZ);
        map.put("Point",point[s]);
        map.put("Ranking",s);
        map.put("status","1");
        String msg="抽取成功";
        renderJson(ApiResult.success(map,msg));


    }


    /**
     *获取活动状态
     *
     */
    public void isActivityStatus() {
        Long id = getParaToLong("id");
        Map<String, String> map = new HashMap<String, String>();
        Activity activity = activityService.find(id);

        Date  time=new Date();
        Date form=activity.getBeginDate();
        Date to = activity.getEndDate();
        if(form!=null&&to!=null&&activity.getStatus()==1){
            int state= DateUtils.belongCalendar(time,form,to);
            if(state==-1){

                map.put("status","3");
                String msg="活动未开始";
                renderJson(ApiResult.success(map,msg));
                return;
            }else if(state==1){

                map.put("status","2");
                String msg="活动已结束";
                renderJson(ApiResult.success(map,msg));
                return;
            }



        }else if (activity.getStatus()==0){
            map.put("Ranking","");
            map.put("status","4");
            String msg="活动已关闭";
            renderJson(ApiResult.success(map,msg));
            return;

        }else{
            map.put("Ranking","");
            map.put("status","1");
            String msg="活动正常";
            renderJson(ApiResult.success(map,msg));
        }
    }
    /**
     *获取活动状态
     *
     */
    public void findRaffle() {
        Long id = getParaToLong("id");
        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();


        List<Raffle> raffleList = raffleService.findByActivityIdList(id);

        for (Raffle raffle:raffleList) {
            Map<String, Object> map1 = new HashMap<String, Object>();

            if(raffle.getMember().getUsername()==null||"".equals(raffle.getMember().getUsername())){
                map1.put("phone",raffle.getMember().getNickname());
                if(raffle.getMember().getNickname()==null||"".equals(raffle.getMember().getNickname())){
                    map1.put("phone",raffle.getMember().getPhone());

                }
            }else{
                map1.put("phone",raffle.getMember().getUsername());
            }

            if(raffle.getIsReal()==1){
                map1.put("prizeName",raffle.getActivityProduct().getProduct().getName());
                map1.put("SerialNumber",raffle.getActivityProduct().getSerialNumber());
            }else{
                map1.put("prizeName",Double.parseDouble(raffle.getPoint().toString())+"喵币");
            }
            map1.put("date",raffle.getCreateDate());


            mapList.add(map1);
        }
        Map<String, Object> map = new HashMap<String, Object>();
            map.put("raffleList",mapList);
            map.put("status","1");
            String msg="请求成功";
            renderJson(ApiResult.success(map,msg));

    }
}
