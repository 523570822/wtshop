package com.wtshop.cron;

import com.jfinal.aop.Enhancer;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.log.Logger;
import com.jfinal.plugin.cron4j.ITask;
import com.sun.tools.corba.se.idl.constExpr.Or;
import com.wtshop.CommonAttributes;
import com.wtshop.Setting;
import com.wtshop.constants.Code;
import com.wtshop.dao.CartItemDao;
import com.wtshop.dao.DepositLogDao;
import com.wtshop.dao.MrmfShopDao;
import com.wtshop.dao.OrderDao;
import com.wtshop.dao.OrderItemDao;
import com.wtshop.dao.OrderLogDao;
import com.wtshop.dao.PaymentDao;
import com.wtshop.dao.PaymentLogDao;
import com.wtshop.dao.PromotionDao;
import com.wtshop.dao.ReturnsDao;
import com.wtshop.dao.ReturnsItemDao;
import com.wtshop.dao.ReturnsItemProgressDao;
import com.wtshop.dao.ShippingDao;
import com.wtshop.dao.ShippingItemDao;
import com.wtshop.dao.SnDao;
import com.wtshop.dao.StaffMemberDao;
import com.wtshop.model.CommissionHistory;
import com.wtshop.model.Goods;
import com.wtshop.model.Member;
import com.wtshop.model.MrmfShop;
import com.wtshop.model.Order;
import com.wtshop.model.Product;
import com.wtshop.model.StaffMember;
import com.wtshop.service.AccountService;
import com.wtshop.service.CencelService;
import com.wtshop.service.CommissionService;
import com.wtshop.service.CouponCodeService;
import com.wtshop.service.DepositLogService;
import com.wtshop.service.ExchangeLogService;
import com.wtshop.service.FuDaiProductService;
import com.wtshop.service.FuDaiService;
import com.wtshop.service.GoodsService;
import com.wtshop.service.InformationService;
import com.wtshop.service.MailService;
import com.wtshop.service.MemberService;
import com.wtshop.service.MiaobiLogService;
import com.wtshop.service.PaymentMethodService;
import com.wtshop.service.PaymentService;
import com.wtshop.service.ProductService;
import com.wtshop.service.ShippingMethodService;
import com.wtshop.service.SmsService;
import com.wtshop.util.DateUtils;
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

    private OrderDao orderDao = Enhancer.enhance(OrderDao.class);
    private MemberService memberService = Enhancer.enhance(MemberService.class);
    private GoodsService goodsService = Enhancer.enhance(GoodsService.class);
    private ProductService productService = Enhancer.enhance(ProductService.class);
    private StaffMemberDao staffMemberDao = Enhancer.enhance(StaffMemberDao.class);
    private CommissionService commissionService = Enhancer.enhance(CommissionService.class);
    private MrmfShopDao mrmfShopDao = Enhancer.enhance(MrmfShopDao.class);

    public void stop() {

    }

    public void run() {
        logger.info("开始执行定时任务!!!!!!!!");
        Prop prop = PropKit.use(CommonAttributes.wtshop_PROPERTIES_PATH);
        if (!prop.getBoolean("commission.enable")){
            logger.info("当前服务配置为关闭分佣");
            return;
        }
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
