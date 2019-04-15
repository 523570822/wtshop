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
    private InformationService informationService = Enhancer.enhance(InformationService.class);
    private OrderDao orderDao = Enhancer.enhance(OrderDao.class);
    private MemberService memberService = Enhancer.enhance(MemberService.class);
    private GoodsService goodsService = Enhancer.enhance(GoodsService.class);
    private ProductService productService = Enhancer.enhance(ProductService.class);
    private StaffMemberDao staffMemberDao = Enhancer.enhance(StaffMemberDao.class);
    private CommissionService commissionService = Enhancer.enhance(CommissionService.class);
    private MrmfShopDao mrmfShopDao = Enhancer.enhance(MrmfShopDao.class);
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

        /**
         * 用户推送团购定时
         */
        JSONObject redisSetting = JSONObject.parseObject(RedisUtil.getString("redisSetting"));
        Double hour = redisSetting.getDouble("")==null?0.00:redisSetting.getDouble("hour");

        List<GroupRemind> groupReminlist = groupRemindDao.findListRe(hour);

        for (GroupRemind groupRemind:groupReminlist) {
                             groupRemind.setStatus(1);
            try {

                groupRemindDao.update(groupRemind);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        logger.info("开始极光推送服务————————————————————————");
        List<GroupRemind> groupReminlist1= groupRemindDao.findListNum(hour);;
        for (GroupRemind groupRemind:groupReminlist1) {

            int dss=groupRemind.get("number");
            try {

                informationService.groupRmindMessage(groupRemind,dss);
            } catch (Exception ex) {
                ex.printStackTrace();
            }


        }

        logger.info("结束极光推送服务————————————————————————");

        /**
         * 购买普通商品分佣
         */






        //获取15天之前 需要分佣的订单
        Date date = DateUtils.getDateBefore(new Date(), Code.staff_day);
        List<Order> orders = orderDao.findStaffOrder(DateUtils.getDayStartTime(date), DateUtils.getDayEndTime(date));
        Double staffCommission = Double.parseDouble(prop.get("commission.staff")) ;
        Double organCommission = Double.parseDouble(prop.get("commission.organ")) ;

        //佣金计算
       if(orders != null && orders.size() > 0) {
           for(Order order :orders){
               List<Goods> goodsLists = goodsService.findGoodsByPt(order.getId());
               List<Long> goodsList = new ArrayList<Long>();
               if (goodsLists != null && goodsLists.size() > 0) {
                   for (Goods goods : goodsLists) {
                       if (! goodsList.contains(goods.getId())) {
                           goodsList.add(goods.getId());
                       }

                   }
                   StaffMember staffMember = staffMemberDao.queryByMemberId(order.getMemberId());
                   if (staffMember != null) {
                       for (Long goodId : goodsList) {
                           //购买成功的技师
                           Member staff = memberService.find(staffMember.getStaffId());
                           Goods goods = goodsService.find(goodId);
                           if (staff != null) {
                               List<Product> productList = productService.findProductList(goodId);
                               if (productList != null && productList.size() > 0 && productList.get(0) != null) {

                                   BigDecimal staffMoney = (goods.getPrice().subtract(productList.get(0).getCost())).multiply(new BigDecimal(staffCommission)).setScale(2, BigDecimal.ROUND_HALF_UP);
                                   logger.info("商品价格  " +goods.getPrice() + "    ,成本价格   " + productList.get(0).getCost() + " 分佣比例 "  +  staffCommission);


                                   MrmfShop mrmfShop = mrmfShopDao.findByMemberId(staff.getId());
                                   mrmfShop.setCommission(mrmfShop.getCommission().add(staffMoney).setScale(2, BigDecimal.ROUND_HALF_UP));
                                   mrmfShopDao.update(mrmfShop);

                                   //分佣记录
                                   CommissionHistory commissionHistory = new CommissionHistory();
                                   commissionHistory.setTyep(0);
                                   commissionHistory.setMoney(staffMoney);
                                   commissionHistory.setStaffId(staff.getId());
                                   commissionHistory.setMemberId(order.getMemberId());
                                   commissionHistory.setGoodsId(goodId);
                                   if (StringUtils.isNotBlank(staffMember.getOrganId())) {
                                       commissionHistory.setOrganId(staffMember.getOrganId());
                                   }
                                   commissionService.save(commissionHistory);
                               }

                               if (StringUtils.isNotBlank(staffMember.getOrganId())) {
                                   BigDecimal organMoney = (goods.getPrice().subtract(productService.findProductList(goodId).get(0).getCost())).multiply(new BigDecimal(organCommission)).setScale(2, BigDecimal.ROUND_HALF_UP);
                                   MrmfShop shop = mrmfShopDao.findByOrganId(staffMember.getOrganId());
                                   if (shop != null) {
                                       shop.setCommission(shop.getCommission().add(organMoney).setScale(2, BigDecimal.ROUND_HALF_UP));
                                       shop.update();
                                   } else {
                                       MrmfShop mrmfShop = new MrmfShop();
                                       mrmfShop.setCreateDate(new Date());
                                       mrmfShop.setModifyDate(new Date());
                                       mrmfShop.setVersion(0L);
                                       mrmfShop.setOrganId(staffMember.getOrganId());
                                       mrmfShop.setCommission(organMoney);
                                       mrmfShop.save();
                                   }
                                   //店铺
                                   CommissionHistory commission = new CommissionHistory();
                                   commission.setTyep(1);
                                   commission.setMoney(organMoney);
                                   commission.setStaffId(staff.getId());
                                   commission.setMemberId(order.getMemberId());
                                   commission.setOrganId(staffMember.getOrganId());
                                   commission.setGoodsId(goodId);
                                   commissionService.save(commission);

                               }

                           }

                       }
                   }

               }

           }

       }

    }
}
