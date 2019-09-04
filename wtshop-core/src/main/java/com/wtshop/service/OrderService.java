package com.wtshop.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.aop.Enhancer;
import com.jfinal.kit.LogKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;
import com.wtshop.Filter;
import com.wtshop.Pageable;
import com.wtshop.Setting;
import com.wtshop.constants.Code;
import com.wtshop.dao.*;
import com.wtshop.entity.Invoice;
import com.wtshop.entity.OrderGoods;
import com.wtshop.exception.AppRuntimeException;
import com.wtshop.model.*;
import com.wtshop.util.*;
import freemarker.log.Logger;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.math.BigDecimal.ROUND_HALF_DOWN;
import static java.math.BigDecimal.ROUND_HALF_UP;

/**
 * Service - 订单
 */
public class OrderService extends BaseService<Order> {

    /**
     * 构造方法
     */
    public OrderService() {
        super(Order.class);
    }

    private OrderDao orderDao = Enhancer.enhance(OrderDao.class);
    private OrderItemDao orderItemDao = Enhancer.enhance(OrderItemDao.class);
    private OrderLogDao orderLogDao = Enhancer.enhance(OrderLogDao.class);
    private CartItemDao cartItemDao = Enhancer.enhance(CartItemDao.class);
    private DepositLogDao depositLogDao = Enhancer.enhance(DepositLogDao.class);
    private CommissionLogDao commissionDao = Enhancer.enhance(CommissionLogDao.class);
    private SnDao snDao = Enhancer.enhance(SnDao.class);
    private PaymentDao paymentDao = Enhancer.enhance(PaymentDao.class);
    private ShippingDao shippingDao = Enhancer.enhance(ShippingDao.class);
    private ShippingItemDao shippingItemDao = Enhancer.enhance(ShippingItemDao.class);
    private ReturnsDao returnsDao = Enhancer.enhance(ReturnsDao.class);
    private ReturnsItemDao returnsItemDao = Enhancer.enhance(ReturnsItemDao.class);
    private MemberService memberService = Enhancer.enhance(MemberService.class);
    private CouponCodeService couponCodeService = Enhancer.enhance(CouponCodeService.class);
    private GoodsService goodsService = Enhancer.enhance(GoodsService.class);
    private ProductService productService = Enhancer.enhance(ProductService.class);
    private ShippingMethodService shippingMethodService = Enhancer.enhance(ShippingMethodService.class);
    private FuDaiProductService fuDaiProductService = Enhancer.enhance(FuDaiProductService.class);
    private PaymentLogDao paymentLogDao = Enhancer.enhance(PaymentLogDao.class);
    private ReturnsItemProgressDao returnsItemProgressDao = Enhancer.enhance(ReturnsItemProgressDao.class);
    private MailService mailService = new MailService();
    private SmsService smsService = new SmsService();
    private InformationService informationService = Enhancer.enhance(InformationService.class);
    private FuDaiService fuDaiService = Enhancer.enhance(FuDaiService.class);
    private MiaobiLogService miaobiLogService = Enhancer.enhance(MiaobiLogService.class);
    private PaymentMethodService paymentMethodService = Enhancer.enhance(PaymentMethodService.class);
    private CencelService cencelService = Enhancer.enhance(CencelService.class);
    private PromotionDao promotionDao = Enhancer.enhance(PromotionDao.class);
    private PaymentService paymentService = Enhancer.enhance(PaymentService.class);
    private DepositLogService depositLogService = Enhancer.enhance(DepositLogService.class);
    private ExchangeLogService exchangeLogService = Enhancer.enhance(ExchangeLogService.class);
    private AccountService accountService = Enhancer.enhance(AccountService.class);
    private FightGroupService fightGroupService = Enhancer.enhance(FightGroupService.class);
    private GroupBuyService groupBuyService = Enhancer.enhance(GroupBuyService.class);
    private  IdentifierService identifierService =Enhancer.enhance(IdentifierService.class);
    private  SpecialCouponService specialCouponService =Enhancer.enhance(SpecialCouponService.class);
    private  IntegralLogService integralLogService =Enhancer.enhance(IntegralLogService.class);
    private IntegralStoreService integralStoreService =Enhancer.enhance(IntegralStoreService.class);
    private  IntegralStoreLogService integralStoreLogService=Enhancer.enhance(IntegralStoreLogService.class);
    com.jfinal.log.Logger logger = com.jfinal.log.Logger.getLogger(OrderService.class);

    /**
     * 根据编号查找订单
     *
     * @param sn 编号(忽略大小写)
     * @return 订单，若不存在则返回null
     */
    public Order findBySn(String sn) {
        return orderDao.findBySn(sn);
    }

    /**
     * 根据编号查找订单
     *
     * @param sn 编号(忽略大小写)
     * @return 订单，若不存在则返回null
     */
    public List<Order> findByfightgroupId(Long sn) {
        return orderDao.findByfightgroupId(sn);
    }
    /**
     * 根据编号查找订单
     *
     * @param sn 编号(忽略大小写)
     * @return 订单，若不存在则返回null
     */
    public Page<Order> findByfightgroupId(Long sn,Pageable pageable) {
        return orderDao.findByfightgroupId(sn,pageable);
    }

    /**
     * 根据编号查找订单
     *
     * @param sn 编号(忽略大小写)
     * @return 订单，若不存在则返回null
     */
    public List<Order> findByfightgroupIdmemberId(Long sn, long memberId) {
        return orderDao.findByfightgroupIdmemberId(sn, memberId);
    }
    /**
     * 根据编号查找订单
     *
     * @param sn 编号(忽略大小写)
     * @return 订单，若不存在则返回null
     */
    public List<Order> findBytuanGouIdmemberId(Long sn, long memberId) {
        return orderDao.findBytuanGouIdmemberId(sn, memberId);
    }

    /**
     * 获取订单商品市场价格
     */
    public Double getOldPrice(Order order) {
        List<Goods> goodsList = goodsService.findGoodsByPt(order.getId());
        Double oldPrice = 0d;
        for (Goods goods : goodsList) {
            oldPrice = MathUtil.add(oldPrice, MathUtil.multiply(goods.getMarketPrice(), goods.getCheck()));
        }
        return oldPrice;
    }

    /**
     * 获取订单商品市场价格
     */
    public Double getPrice(Order order) {
        List<Goods> goodsList = goodsService.findGoodsByPt(order.getId());
        Double oldPrice = 0d;
        for (Goods goods : goodsList) {
            oldPrice = MathUtil.add(oldPrice, MathUtil.multiply(goods.getPrice(), goods.getCheck()));
        }
        return oldPrice;
    }


    /**
     * 充值成功
     */
    @Before(Tx.class)
    public ApiResult CZpaySuccess(String id, String amount, String type) {

        com.jfinal.log.Logger logger = com.jfinal.log.Logger.getLogger("CZpaySuccess");
        logger.info("@@@@@@@@@@@ amount++++++++++++++" + amount);
        ApiResult returnStatus = ApiResult.fail();
        ExchangeLog exchangeLog = exchangeLogService.findByOrderNo(id);
        Member member = memberService.find(exchangeLog.getMemberId());
        BigDecimal balance = member.getBalance();
        Double price = exchangeLog.getPrice().doubleValue();
        Double multiply = price;
        logger.info("转化之前 multiply++++++++++++++" + multiply);
        if ("0".equals(type)) {
            multiply = MathUtil.multiply(multiply, 100);
        }
        logger.info("转化之后 multiply++++++++++++++" + multiply);
        if (MathUtil.getInt(multiply.toString()).equals(MathUtil.getInt(amount.toString()))) {
            //判断是否已经充值完成
            if (1 == exchangeLog.getStatus()) {
                return ApiResult.success("已经充值成功");
            }

            //获取充值金额 跟新余额
            BigDecimal money = new BigDecimal(price);
            BigDecimal bigDecimal = balance.add(money).setScale(2, BigDecimal.ROUND_HALF_DOWN);
            BigDecimal recharge = member.getRecharge().add(money).setScale(2, BigDecimal.ROUND_HALF_DOWN);
            member.setBalance(bigDecimal);
            member.setRecharge(recharge);
            memberService.update(member);

            //交易记录
            DepositLog depositLog = new DepositLog();
            depositLog.setMemberId(member.getId());
            depositLog.setType(DepositLog.Type.recharge.ordinal());
            depositLog.setCredit(money);
            depositLog.setDebit(new BigDecimal(0));
            depositLog.setMemo("充值收入");
            depositLog.setBalance(member.getBalance());
            depositLogService.save(depositLog);

            exchangeLog.setStatus(1);
            exchangeLogService.update(exchangeLog);

            returnStatus = ApiResult.success();
            return returnStatus;
        } else {
            return returnStatus;
        }

    }

    /**
     * 支付失败
     */
    @Before(Tx.class)
    public ApiResult payFaile(String id) {
        Order order = orderDao.find(Long.parseLong(id));
        order.setAmountPaid(BigDecimal.ZERO);
        orderDao.update(order);
        return ApiResult.success();
    }

    /**
     * 微信支付成功 订单处理
     */
    @Before(Tx.class)
    public ApiResult paySuccess(String sn, String money, String weiXinNo, String aliNo) {

        final Logger logger = Logger.getLogger("paySuccess");
        ApiResult returnStatus = ApiResult.fail();
        Setting setting = SystemUtils.getSetting();
        JSONObject redisSetting = JSONObject.parseObject(RedisUtil.getString("redisSetting"));
        Order order = orderDao.findBySn(sn);
        Long memberId = order.getMemberId();

        Member member = memberService.find(memberId);
        Long dds = ShareCodeUtils.codeToId(member.getOnShareCode());
        Member member1 = memberService.find(dds);
        BigDecimal amount = order.getAmount().subtract(order.getAmountPaid()).setScale(2, BigDecimal.ROUND_HALF_UP);
        order.setExpire(null);
        order.setLockExpire(null);
        order.setLockKey(null);
        orderDao.update(order);
        if (order.getStatus() == Order.Status.pendingShipment.ordinal()) {
            logger.info("订单已完成支付,无需再次支付 " );
            return ApiResult.fail("订单已完成支付,无需再次支付");
        }
        order.setStatus(Order.Status.pendingShipment.ordinal());




        logger.info("测试支付宝应保存金额   :  " + amount);
        if (StringUtils.isNotBlank(weiXinNo)) {
            order.setWeixinPaid(amount);
            order.setAliPaid(BigDecimal.ZERO);
            order.setOrderNo(weiXinNo);
        } else if (StringUtils.isNotBlank(aliNo)) {
            logger.info("测试支付宝实际保存金额  :  " + amount);
            order.setWeixinPaid(BigDecimal.ZERO);
            order.setAliPaid(amount);
            order.setOrderNo(aliNo);
        } else {
            order.setWeixinPaid(BigDecimal.ZERO);
            order.setAliPaid(BigDecimal.ZERO);
            order.setOrderNo(null);
        }


        //支付金额
        memberService.addAmount(member, order.getAmount());
        //有余额支付

        if (order.getAmountPaid().doubleValue() > 0) {

            //更新用户余额
            BigDecimal decimal = member.getBalance().subtract(order.getAmountPaid()).setScale(2, RoundingMode.UP);
            if (decimal.doubleValue() < 0) {
                decimal = BigDecimal.ZERO;
            }
            member.setBalance(decimal);
            memberService.update(member);
            //添加余额消费记录
            DepositLog depositLog = new DepositLog();
            depositLog.setBalance(decimal);
            depositLog.setCredit(new BigDecimal("0"));
            depositLog.setDebit(order.getAmountPaid());
            depositLog.setMemo("订单支付");
            depositLog.setType(DepositLog.Type.payment.ordinal());
            depositLog.setOrderId(order.getId());
            depositLog.setMemberId(member.getId());
            depositLogDao.save(depositLog);

        }

        //添加支付记录
        Payment payment = new Payment();
        payment.setAmount(order.getAmount());
        payment.setSn(snDao.generate(Sn.Type.paymentLog));
        payment.setAmount(order.getAmount());
        payment.setBalanceAmount(order.getAmountPaid());
        payment.setPayAmount(amount);
        if (amount.doubleValue() != 0) {
            payment.setPaymentMethod("在线支付");
        } else {
            payment.setPaymentMethod("余额支付");
        }
        payment.setOrderId(order.getId());
        payment.setPayer(member.getNickname());
        payment.setMethod(0);
        paymentService.save(payment);

        PaymentLog paymentLog = new PaymentLog();
        paymentLog.setSn(order.getSn());
        paymentLog.setType(PaymentLog.Type.payment.ordinal());
        paymentLog.setStatus(PaymentLog.Status.wait.ordinal());
        paymentLog.setFee(new BigDecimal("0"));
        paymentLog.setAmount(amount);
        paymentLog.setPaymentPluginId("1");
        if (StringUtils.isNotBlank(weiXinNo))
            paymentLog.setPaymentPluginName("微信支付");
        else
            paymentLog.setPaymentPluginName("支付宝支付");
        paymentLog.setMemberId(member.getId());
        paymentLog.setOrderId(order.getId());
        paymentLog.setStatus(PaymentLog.Status.success.ordinal());
        paymentLogDao.save(paymentLog);


        //喵币比例
        Double scale = redisSetting.getDouble("scale");
        //喵币限制
        Double sendMiaoBiLimit = redisSetting.getDouble("sendMiaoBiLimit");
        //判断是否有喵币支付，添加喵币支付记录
        if (order.getMiaobiPaid() != null && order.getMiaobiPaid().doubleValue() > 0) {
            MiaobiLog miaobiLog = new MiaobiLog();
            miaobiLog.setMemberId(order.getMemberId());
            miaobiLog.setCredit(BigDecimal.ZERO);


            miaobiLog.setDebit(order.getMiaobiPaid().multiply(new BigDecimal(scale).setScale(2, BigDecimal.ROUND_HALF_UP)));
            miaobiLog.setBalance(member.getPoint().subtract((order.getMiaobiPaid().multiply(new BigDecimal(scale).setScale(2, BigDecimal.ROUND_HALF_UP)))));


            miaobiLog.setType(1);
            miaobiLog.setMemo("订单支付");
            miaobiLogService.save(miaobiLog);
            //更新用户喵币余额
            member.setPoint(member.getPoint().subtract((order.getMiaobiPaid().multiply(new BigDecimal(scale).setScale(2, BigDecimal.ROUND_HALF_UP)))));

            memberService.update(member);
        }
        //赠送喵币逻辑
        boolean isSendMiaoBi = redisSetting.getBoolean("isSendMiaoBi") ? true : false;
        if (isSendMiaoBi && order.getType() == 0 && order.getCouponCode() == null && order.getMiaobiPaid().doubleValue() == 0) {

            Double price = order.getAmount().doubleValue();
            //喵币赠送金额
            BigDecimal sendMiaoBi = new BigDecimal(price).multiply(new BigDecimal(sendMiaoBiLimit)).setScale(2, BigDecimal.ROUND_HALF_UP);
            MiaobiLog miaobiLog = new MiaobiLog();
            miaobiLog.setMemberId(order.getMemberId());
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

        //  佣金计算
       /* if ( Order.Type.general.ordinal()== order.getType() ) {
            List<Goods> goodsLists = goodsService.findGoodsByPt(order.getId());
            List<Long> goodsList = new ArrayList<>();
            if (goodsLists != null && goodsLists.size() > 0) {
                for (Goods goods : goodsLists) {
                    if (!goodsList.contains(goods.getId())) {
                        goodsList.add(goods.getId());
                    }

                }
                StaffMember staffMember = staffMemberDao.queryByMemberId(order.getMemberId());
                if (staffMember != null) {
                    for (Long goodId : goodsList) {
                        //购买成功的技师
                        Member staff = memberService.find(staffMember.getStaffId());
                        if (staff != null) {
                            List<Product> productList = productService.findProductList(goodId);
                            if (productList != null && productList.size() > 0 && productList.get(0) != null) {
                                order.setIsCommission(true);
                            }
                        }

                    }
                }

            }

        }*/
        if (Order.Type.general.ordinal() == order.getType()) {
            order.setOnShareCode(member.getOnShareCode());
            //商品返现
            List<Goods> goodList = goodsService.findGoodsByOrderItemId(order.getId());
            if (goodList != null && goodList.size() > 0) {
                for (Goods goods : goodList) {
                    if (goods.getSales() == null) {
                        goods.setSales(0L);
                    } else {
                        if (goods.get("quantity") == null || goods.get("quantity").equals("null")) {

                        } else {
                            goods.setSales(goods.getSales() + Long.valueOf(goods.get("quantity") + ""));
                        }


                    }

                    goodsService.update(goods);
                    Promotion prom = promotionDao.findProm(goods.getId());
                    if (prom != null) {
                        member.setBalance(member.getBalance().add(prom.getMoney()).setScale(2, BigDecimal.ROUND_HALF_UP));
                        memberService.update(member);


                        //插入钱包变动记录
                        DepositLog depositLog = new DepositLog();
                        depositLog.setBalance(member.getBalance());
                        depositLog.setCredit(prom.getMoney());
                        depositLog.setDebit(BigDecimal.ZERO);
                        depositLog.setMemo("返现活动");
                        depositLog.setType(DepositLog.Type.adjustment.ordinal());
                        depositLog.setOrderId(order.getId());
                        depositLog.setMemberId(member.getId());
                        depositLogDao.save(depositLog);


                    }

                }
            }
            double dd = order.getCommissionRate() * order.getPrice().doubleValue() / 100;

            logger.info("佣金金额————————————————————————" + dd);
            if(dd > 0){
                logger.info("开始计算佣金————————————————————————");
                BigDecimal b1 = new BigDecimal(dd);
                if (member.getCommissionUnarrived() == null) {
                    member.setCommissionUnarrived(BigDecimal.ZERO);
                }
                //member.setCommissionUnarrived(b1.add(member.getCommissionUnarrived()));
                //判断是否是管家
                if (StringUtils.isNotEmpty(member.getShareCode())) {
                    order.setIsCommission(true);
                    member.getOnShareCode();
                    order.setIsCommission(true);
                    //插入佣金变动记录
                    CommissionLog depositLog = new CommissionLog();
                    depositLog.setBalance(member.getBalance());
                    depositLog.setCredit(b1);
                    depositLog.setDebit(BigDecimal.ZERO);
                    depositLog.setMemo("佣金回馈自己");
                    depositLog.setType(CommissionLog.Type.adjustment.ordinal());
                    /**
                     * 待定
                     */
                    depositLog.setStatus(2);
                    depositLog.setOrderId(order.getId());
                    depositLog.setMemberId(member.getId());
                    member.setCommissionUnarrived(b1.add(member.getCommissionUnarrived()));
                    commissionDao.save(depositLog);

                    memberService.update(member);


                }
                order.setOnShareCode(member.getOnShareCode());

                CommissionLog depositLog1 = new CommissionLog();
                depositLog1.setBalance(member1.getBalance());
                depositLog1.setCredit(b1);
                depositLog1.setDebit(BigDecimal.ZERO);
                depositLog1.setStatus(2);
                depositLog1.setMemo("佣金回馈上级");
                depositLog1.setType(CommissionLog.Type.adjustment.ordinal());
                depositLog1.setOrderId(order.getId());

                depositLog1.setMemberId(dds);
                if (member1.getCommissionUnarrived() == null) {
                    member1.setCommissionUnarrived(BigDecimal.ZERO);
                }
                member1.setCommissionUnarrived(b1.add(member1.getCommissionUnarrived()));
                memberService.update(member1);
                commissionDao.save(depositLog1);

            }


        }

        // 生成会员激活码，福袋
        if (order.getType() == Order.Type.fudai.ordinal()) {

            CommissionLog depositLog1 = new CommissionLog();
            CommissionLog depositLog2 = new CommissionLog();
            logger.info("福袋相关技术————————————————————————");
            if (StringUtils.isEmpty(member.getShareCode()) || member.getShareCode() == null) {
                logger.info("生成邀请码————————————————————————");
                // 生成邀请码
                String code = ShareCodeUtils.idToCode(member.getId());
                member.setShareCode(code);
                if (member.getHousekeeperId() == null || member.getHousekeeperId() <= 1) {
                    member.setHousekeeperId(2L);
                }

                member.setLinkShareCode(member1.getLinkShareCode() + "_" + member.getOnShareCode());

              /*  if(mmss.size()>=15){
                    member1.setHousekeeperId(3l);
                }*/
                if(member1.getHousekeeperId()==2||member1.getHousekeeperId()==1){
                    depositLog1.setCredit(BigDecimal.valueOf(100l));
                    member1.setCommission(BigDecimal.valueOf(100L).add(member1.getCommission()));
                }else if(member1.getHousekeeperId()==3){
                    depositLog1.setCredit(BigDecimal.valueOf(150l));
                    member1.setCommission(BigDecimal.valueOf(150L).add(member1.getCommission()));
                }else if(member1.getHousekeeperId()>=4){
                    depositLog1.setCredit(BigDecimal.valueOf(230l));
                    member1.setCommission(BigDecimal.valueOf(230L).add(member1.getCommission()));
                }
                depositLog1.setBalance(member1.getBalance());
                depositLog1.setDebit(BigDecimal.ZERO);
                depositLog1.setStatus(1);
                depositLog1.setMemo("福袋上级返现");
                depositLog1.setType(CommissionLog.Type.fudan.ordinal());

                depositLog1.setOrderId(order.getId());
                depositLog1.setMemberId(member1.getId());
                 boolean xunHuan=true;
                 boolean jinPai=false;
                 boolean baiJin=false;
                 boolean zhuanShi=false;
                    int i=0;
                //计算新增银牌会员50奖励
                if(member1.getHousekeeperId()==2){
                    String onShareCode = member1.getOnShareCode();
                while(xunHuan){
                    logger.info("开始循环计算新增掌柜奖励50元—————————————第"+i+++"次———————————");
                        Long dds1 = ShareCodeUtils.codeToId(onShareCode);
                        Member member2 = memberService.find(dds1);
                    if(member2.getOnShareCode()==null||member2.getShareCode()==null||member2.getOnShareCode().equals("")||member2.getShareCode().equals("")||member2.getOnShareCode().equals(member2.getShareCode())){
                        break;
                    //判断初始邀请码直接跳过
                    } else  if(member2.getHousekeeperId()==1){
                        //判断上级身份 白银直接跳到下一循环
                        onShareCode=member2.getOnShareCode();
                      //  continue;
                    }else if (member2.getHousekeeperId()==3&&!jinPai){
                        DepositLog depositLog5 = new DepositLog();
                        depositLog5.setCredit(BigDecimal.valueOf(50l));
                        member2.setBalance(BigDecimal.valueOf(50L).add(member2.getBalance()));
                        depositLog5.setBalance(member2.getBalance());
                        depositLog5.setDebit(BigDecimal.ZERO);
                        depositLog5.setStatus(1);
                        depositLog5.setMemo("扶持奖励");
                        depositLog5.setType(DepositLog.Type.fuchi.ordinal());
                        depositLog5.setOrderId(order.getId());
                        depositLog5.setMemberId(member2.getId());
                        memberService.update(member2);
                        depositLogDao.save(depositLog5);
                        //判断上级身份 黄金并且没有发过奖金 开始发奖金
                        onShareCode=member2.getOnShareCode();
                        jinPai=true;
                    }else if (member2.getHousekeeperId()==4&&!baiJin){
                        //判断上级身份白金并且没有发过奖金 开始发奖金
                        DepositLog depositLog5 = new DepositLog();
                        depositLog5.setCredit(BigDecimal.valueOf(50l));
                        member2.setBalance(BigDecimal.valueOf(50L).add(member2.getBalance()));
                        depositLog5.setBalance(member2.getBalance());
                        depositLog5.setDebit(BigDecimal.ZERO);
                        depositLog5.setStatus(1);
                        depositLog5.setMemo("扶持奖励");
                        depositLog5.setType(DepositLog.Type.fuchi.ordinal());
                        depositLog5.setOrderId(order.getId());
                        depositLog5.setMemberId(member2.getId());
                        memberService.update(member2);
                        depositLogDao.save(depositLog5);
                        onShareCode=member2.getOnShareCode();
                        baiJin=true;
                    }else if((member2.getHousekeeperId()==5&&!baiJin&&!zhuanShi)){
                        //判断上级身份砖石 并且上级没有白金 开始发奖金
                        DepositLog depositLog5 = new DepositLog();
                        depositLog5.setCredit(BigDecimal.valueOf(50l));
                        member2.setBalance(BigDecimal.valueOf(50L).add(member2.getBalance()));
                        depositLog5.setBalance(member2.getBalance());
                        depositLog5.setDebit(BigDecimal.ZERO);
                        depositLog5.setStatus(1);
                        depositLog5.setMemo("扶持奖励50元");
                        depositLog5.setType(DepositLog.Type.fuchi.ordinal());
                        depositLog5.setOrderId(order.getId());
                        depositLog5.setMemberId(member2.getId());
                        memberService.update(member2);
                        depositLogDao.save(depositLog5);
                        onShareCode=member2.getOnShareCode();
                        zhuanShi=true;
                    }else if((member2.getHousekeeperId()==5&&baiJin&&!zhuanShi)){
                        //判断上级身份砖石 并且上级有白金 开始发奖金 （这个根据比例算）
                        List<Member> mm = memberService.findMemberByLinkShare(member2.getShareCode(), 4L);

                        Long jinglijin=20L;
                        if(mm.size()>3&&mm.size()<6){
                            jinglijin=40l;
                        }else  if(mm.size()>=6){
                            jinglijin=60l;
                        }
                        DepositLog depositLog5 = new DepositLog();
                        depositLog5.setCredit(BigDecimal.valueOf(jinglijin));
                        member2.setBalance(BigDecimal.valueOf(jinglijin).add(member2.getBalance()));
                        depositLog5.setBalance(member2.getBalance());
                        depositLog5.setDebit(BigDecimal.ZERO);
                        depositLog5.setStatus(1);
                        depositLog5.setMemo("扶持奖励"+jinglijin+"元");
                        depositLog5.setType(DepositLog.Type.fuchi.ordinal());
                        depositLog5.setOrderId(order.getId());
                        depositLog5.setMemberId(member2.getId());
                        memberService.update(member2);
                        depositLogDao.save(depositLog5);
                        onShareCode=member2.getOnShareCode();
                        zhuanShi=true;
                    }else{
                        onShareCode=member2.getOnShareCode();
                    }
                    }
                }




            } else {
                //判断身份是银牌级以上自己赠送100元
                member1.setCommission(BigDecimal.valueOf(100L).add(member1.getCommission()));
                depositLog1.setBalance(member1.getBalance());
                depositLog1.setDebit(BigDecimal.ZERO);
                depositLog1.setCredit(BigDecimal.valueOf(100l));
                depositLog1.setStatus(1);
                depositLog1.setMemo("福袋上级返现(复购)");
                depositLog1.setType(CommissionLog.Type.fudan.ordinal());
                depositLog1.setOrderId(order.getId());
                depositLog1.setMemberId(member1.getId());

                    depositLog2.setBalance(member.getBalance());
                    depositLog2.setDebit(BigDecimal.ZERO);
                    depositLog2.setStatus(1);
                    depositLog2.setCredit(BigDecimal.valueOf(100l));
                    depositLog2.setMemo("福袋自购返现");
                    depositLog2.setType(CommissionLog.Type.fudan.ordinal());
                    depositLog2.setOrderId(order.getId());
                    depositLog2.setMemberId(member.getId());
                    member.setCommission(BigDecimal.valueOf(100L).add(member.getCommission()));
                commissionDao.save(depositLog2);


                logger.info("存在邀请码没有生成邀请码————————————————————————");
            }


            memberService.update(member1);
            memberService.update(member);
            commissionDao.save(depositLog1);


            order.setOnShareCode(member.getOnShareCode());
          /*  List<Member> mmss = memberService.findMemberByOnShare(member1.getShareCode());*/

            //调用推送

            order.setOnShareCode(member.getOnShareCode());
            List<Map<String, Object>> list = fuDaiService.luckDraw(order);
        }
        //  logger.info("测试团购getFightgroupId   :  " + order.getFightgroupId());
        //  logger.info("测试团购order.getIsSinglepurchase()   :  " +order.getIsSinglepurchase());
        //  logger.info("order.getType()  :  " +order.getType());
        //  logger.info("Order.Type.group.ordinal()  :  " +Order.Type.group.ordinal());
        //团购
        if (order.getType() == Order.Type.group.ordinal()) {
            FightGroup fightGroup = new FightGroup();
            GroupBuy groupBuy = groupBuyService.find(order.getGroupbuyId());
            //判断有没有拼团id 并且判断是不是单购
      /*   if(order.getFightgroupId()==0&&order.getIsSinglepurchase()){
                //单购
                fightGroup.setTitle(groupBuy.getTitle());
                fightGroup.setPrice(groupBuy.getPrice());
                fightGroup.setUniprice(groupBuy.getUniprice());

                //拼图状态  拼图中
                fightGroup.setStatus(1);
                fightGroup.setRule(groupBuy.getRule());
                fightGroup.setExplain(groupBuy.getExplain());
                fightGroup.setProductId(groupBuy.getProductId());
                //已经参团人数
                fightGroup.setCount(1);
                fightGroup.setDispatchprice(groupBuy.getDispatchprice());
                fightGroup.setGroupnum(1);
                fightGroup.setMemberId(order.getMemberId());



                fightGroup.setBeginDate(groupBuy.getBeginDate());
                fightGroup.setEndDate(groupBuy.getEndDate());
              //  fightGroup.set
                fightGroup.setTuangouId(order.getGroupbuyId());

                fightGroup = fightGroupService.save(fightGroup);
                order.setFightgroupId(fightGroup.getId());

            }else*/
            if (order.getFightgroupId() == 0 && !order.getIsSinglepurchase()) {
                //自己租的团

                //  fightGroup.
                fightGroup.setTitle(groupBuy.getTitle());
                fightGroup.setPrice(groupBuy.getPrice());
                fightGroup.setUniprice(groupBuy.getUniprice());

                //拼图状态  拼图中
                fightGroup.setStatus(2);
                fightGroup.setRule(groupBuy.getRule());
                fightGroup.setExplain(groupBuy.getExplain());
                fightGroup.setProductId(groupBuy.getProductId());
                //已经参团人数
                fightGroup.setCount(1);
                fightGroup.setDispatchprice(groupBuy.getDispatchprice());
                fightGroup.setGroupnum(groupBuy.getGroupnum());

                fightGroup.setMemberId(order.getMemberId());


/*
                Date nowDate = new Date();
                Calendar cal = Calendar.getInstance();
                cal.setTime(nowDate);
                cal.add(Calendar.HOUR, groupBuy.getEndtime());// 24小时制
                Date time = cal.getTime();
*/

                fightGroup.setBeginDate(groupBuy.getBeginDate());
                fightGroup.setEndDate(groupBuy.getEndDate());

                fightGroup.setTuangouId(order.getGroupbuyId());

                fightGroup = fightGroupService.save(fightGroup);
                order.setFightgroupId(fightGroup.getId());

            } else {

                fightGroup = fightGroupService.find(order.getFightgroupId());

                fightGroup.setCount(fightGroup.getCount() + 1);


                if (fightGroup.getCount() >= fightGroup.getGroupnum()) {
                    fightGroup.setStatus(1);
                }
                BigDecimal ddd = order.getPrice().multiply(BigDecimal.valueOf(groupBuy.getGroupRate() == null ? 0 : groupBuy.getGroupRate())).divide(BigDecimal.valueOf(100));
                if(ddd.compareTo(BigDecimal.ZERO)!=0){
                    DepositLog depositLog1 = new DepositLog();
                    depositLog1.setBalance(member1.getBalance());
                    depositLog1.setCredit(ddd);
                    depositLog1.setDebit(BigDecimal.ZERO);
                    depositLog1.setStatus(2);
                    depositLog1.setMemo("团购上级返现");
                    depositLog1.setType(DepositLog.Type.tuangou.ordinal());
                    depositLog1.setOrderId(order.getId());
                    depositLog1.setMemberId(member1.getId());
                    member1.setTuangouUnarrived(ddd.add(member1.getTuangouUnarrived()));
                    depositLogService.save(depositLog1);
                    memberService.update(member1);
                }



                fightGroupService.update(fightGroup);
                //跟人家拼团

            }

            logger.info("开始调用团购————————————————————————");


//			reverseExService.paySuccess(order.getActOrderId());
        }
        //特殊商品购买
        if (order.getType() == Order.Type.special.ordinal()) {
            logger.info("特殊商品购买————————————————————————");
           // order.setOnShareCode(member.getOnShareCode());
            List<Goods> goodList = goodsService.findGoodsByOrderItemId(order.getId());
            if (goodList != null && goodList.size() > 0) {
                for (Goods goods : goodList) {
                  Long  itemid=goods.get("order_itemId");
                OrderItem itemids= orderItemDao.find(itemid);
                    Product product = productService.find(itemids.getProductId());
                    if (goods.getSales() == null) {
                        goods.setSales(0L);
                    } else {
                        if (goods.get("quantity") == null || goods.get("quantity").equals("null")) {

                        } else {
                         //   Integer stock = product.getStock();
                           Object quantity=  goods.get("quantity");
                      //     System.out.println("stock=========="+stock);
                            System.out.println("quantity=========="+quantity);
                          //  int ff = stock - Integer.parseInt(quantity+"");
                          //  product.setStock(ff);
                        //    productService.update(product);
                            goods.setSales(goods.getSales() + Long.valueOf(goods.get("quantity") + ""));
                        }


                    }
                    goodsService.update(goods);
                }
            }
            Identifier identifier = identifierService.find(order.getIdentifierId());

            if(identifier.getPrice()==null) {
                identifier.setPrice(order.getAmount());
            }else {
                identifier.setPrice(order.getAmount().add(identifier.getPrice()));
            }


            //满足 返现条件
            if(identifier.getPrice().compareTo(identifier.getTotalMoney())!=-1){
                identifier.setStatus(3);


                Member member2 = memberService.findByShareCode(identifier.getShareCode()).get(0);
                identifier.setCompleteDate(new Date());
                DepositLog depositLog1 = new DepositLog();
                depositLog1.setBalance(member2.getBalance());
                depositLog1.setCredit(identifier.getMoney());
                depositLog1.setDebit(BigDecimal.ZERO);
                depositLog1.setStatus(1);
                depositLog1.setMemo("门店合作收益");
                depositLog1.setType(DepositLog.Type.ident.ordinal());
                depositLog1.setOrderId(order.getId());
                depositLog1.setOperator(""+member.getNickname()+"  "+member.getPhone());
                depositLog1.setMemberId(member2.getId());
                member2.setBalance(identifier.getMoney().add(member2.getBalance()));
                depositLogService.save(depositLog1);
                memberService.update(member2);


                Cache sm = Redis.use();
                String name=identifier.getOnMember().getStore();
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("name", name );
                String mobile=member.getPhone();
                //检查手机号码有效性
                if (!SMSUtils.isMobileNo(mobile)) {
                    logger.error("请检查用户"+identifier.getMemberId()+"手机号是否正确!——————————————————————");
                    //    renderJson(ApiResult.fail("请检查手机号是否正确!"));
                }
                ApiResult result = SMSUtils.send(mobile,"SMS_171116042", params);
                //ApiResult result = SMSUtils.send("", "", params);
                if(result.resultSuccess()) {
                  //  sm.setex("PONHE:"+mobile,120,"1");
                    Sms sms = new Sms();
                    sms.setMobile(mobile);
                    sms.setSmsCode("您的"+name+"钜惠卡已完成消费金额的累计，可以到线下门店兑换优惠啦");
                    sms.setSmsType(Setting.SmsType.other.ordinal());
                    smsService.saveOrUpdate(sms);
                    logger.info("短信发送成功！【您的"+name+"钜惠卡已完成消费金额的累计，可以到线下门店兑换优惠啦】");
                }else {
                    logger.info("您发送的过于频繁,请稍后再试!");
                }


            }else{
               // Cache sm = Redis.use();
              //  double price=identifier.getTotalMoney().doubleValue()-(identifier.getPrice().doubleValue());
                BigDecimal price = identifier.getTotalMoney().subtract(identifier.getPrice());
                String sprice = MathUtil.getInt(price.toString());
                String smoney = MathUtil.getInt(money);
                String name=identifier.getOnMember().getStore();
                Date day = identifier.getEndDate();
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("name", name );
                params.put("price",sprice);
                params.put("money",smoney);


                SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
                String dateString = formatter.format(day.getTime());
                params.put("day",dateString);


                String mobile=member.getPhone();
                //检查手机号码有效性
                if (!SMSUtils.isMobileNo(mobile)) {
                    logger.error("请检查用户"+identifier.getMemberId()+"手机号是否正确!——————————————————————");
                    //    renderJson(ApiResult.fail("请检查手机号是否正确!"));
                }
                ApiResult result = SMSUtils.send(mobile,"SMS_171188377", params);
                //ApiResult result = SMSUtils.send("", "", params);
                if(result.resultSuccess()) {
                  //  sm.setex("PONHE:"+mobile,120,"1");
                    Sms sms = new Sms();
                    sms.setMobile(mobile);
                    sms.setSmsCode("您的"+name+"钜惠卡本单消费"+money+"元,待消费金额为"+sprice+"元有效期至"+day+"，请及时使用哦~");
                    sms.setSmsType(Setting.SmsType.other.ordinal());
                    smsService.saveOrUpdate(sms);
                    logger.info("短信发送成功！【您的"+name+"钜惠卡本单消费"+money+"元,待消费金额为"+price+"元有效期至"+day+"，请及时使用哦~】");
                }else {
                    logger.info("您发送的过于频繁,请稍后再试!");
                }
            }
            identifierService.update(identifier);
        }
        if (order.getType() == Order.Type.coupon.ordinal()) {
            logger.info("特殊商品代金券购买————————————————————————");
            // order.setOnShareCode(member.getOnShareCode());
            List<Goods> goodList = goodsService.findGoodsByOrderItemId(order.getId());
            if (goodList != null && goodList.size() > 0) {
                for (Goods goods : goodList) {
                    Long  itemid=goods.get("order_itemId");
                    OrderItem itemids= orderItemDao.find(itemid);
                    Product product = productService.find(itemids.getProductId());
                    if (goods.getSales() == null) {
                        goods.setSales(0L);
                    } else {
                        if (goods.get("quantity") == null || goods.get("quantity").equals("null")) {

                        } else {
                            Integer stock = product.getStock();
                            Object quantity=  goods.get("quantity");
                            goods.setSales(goods.getSales() + Long.valueOf(goods.get("quantity") + ""));
                        }


                    }
                    goodsService.update(goods);
                }
            }
          /*  SpecialCoupon specialCoupon = specialCouponService.find(order.getSpecialcoupId());

            if(specialCoupon.getPrice()==null) {
                specialCoupon.setPrice(order.getAmount());
            }else {
                specialCoupon.setPrice(order.getAmount().add(specialCoupon.getPrice()));
            }
            //满足 返现条件
            specialCoupon.setStatus(3);
            specialCoupon.setShareCode(order.getSn());
            specialCouponService.update(specialCoupon);*/

            if(!order.getSpecialcoupId().equals(0)){
                List<SpecialCoupon> sPecialCouponList = specialCouponService.findBySpecialCids(order.getSpecialcoupId());
                if(sPecialCouponList!=null){
                    for (SpecialCoupon sPecialCoupon:sPecialCouponList){
                        sPecialCoupon.setPrice(order.getAmount());
                        sPecialCoupon.setStatus(3);
                        sPecialCoupon.setOrderId(order.getId());
                        sPecialCoupon.setOrderNo(order.getOrderNo());
                        sPecialCoupon.setCompleteDate(new Date());
                        specialCouponService.update(sPecialCoupon);
                    }
                }



            }

        }
        if (order.getType() == Order.Type.particular.ordinal()) {
            logger.info("特殊商品积分购买————————————————————————");
            // order.setOnShareCode(member.getOnShareCode());
            List<Goods> goodList = goodsService.findGoodsByOrderItemId(order.getId());
            if (goodList != null && goodList.size() > 0) {
                for (Goods goods : goodList) {
                    Long  itemid=goods.get("order_itemId");
                    OrderItem itemids= orderItemDao.find(itemid);
                    Product product = productService.find(itemids.getProductId());
                    if (goods.getSales() == null) {
                        goods.setSales(0L);
                    } else {
                        if (goods.get("quantity") == null || goods.get("quantity").equals("null")) {

                        } else {
                            Integer stock = product.getStock();
                            Object quantity=  goods.get("quantity");
                            goods.setSales(goods.getSales() + Long.valueOf(goods.get("quantity") + ""));
                        }


                    }
                    goodsService.update(goods);
                }
            }
             //开始扣积分

                if(order.getIntegralPaid().compareTo(BigDecimal.ZERO)==1){

                    IntegralLog integralLog=new IntegralLog();
                    integralLog.setDebit(order.getIntegralPaid());
                    integralLog.setOrderId(order.getId());
                    integralLog.setOrderSn(order.getSn());
                    integralLog.setCredit(BigDecimal.ZERO);
                    integralLog.setMemberId(order.getMemberId());
                    integralLog.setMemo("订单支付成功扣除积分");
                    integralLog.setType(2);
                    integralLog.setBalance(member.getIntegral());
                    member.setIntegral(member.getIntegral().subtract(order.getIntegralPaid()));
                    integralLogService.save(integralLog);

                    memberService.update(member);

                }

            //开始加积分

                if(order.getIntegralGift().compareTo(BigDecimal.ZERO)==1){

                    IntegralLog integralLog=new IntegralLog();
                    integralLog.setDebit(BigDecimal.ZERO);
                   integralLog.setOrderId(order.getId());
                   integralLog.setOrderSn(order.getSn());
                    integralLog.setCredit(order.getIntegralGift());
                    integralLog.setMemberId(order.getMemberId());
                    integralLog.setType(3);
                    integralLog.setMemo("订单支付成功增加积分");
                    integralLog.setBalance(member.getIntegral());
                    member.setIntegral(member.getIntegral().add(order.getIntegralGift()));
                    integralLogService.save(integralLog);

                    memberService.update(member);



            //反佣金和扣除对应的佣金比例还有赠送积分

            //1赠送积分推送
     /*     if(StringUtils.isNotEmpty(order.getPrepayId())){

                    WxaTemplate template=new WxaTemplate();
                    template.setTouser(order.getAccount().getAccount());
                    //	template.setEmphasis_keyword("给力");
                    template.setForm_id(order.getPrepayId());
                    template.setPage("pages/main/main");
                    template.setTemplate_id("sK2pxYoo46AY-ijs_f_cfSsMG91Rn-TzHAmeZmcUYFI");
                    template.add("keyword1",order.getSn());
                    SimpleDateFormat sdf =new SimpleDateFormat("yyyy年MM月dd HH:mm:ss SSS" );
                    Date d= new Date();
                    String str = sdf.format(d);
                    template.add("keyword2",str);
                    template.add("keyword3",MathUtil.getInt(order.getAmount().toString())+"元");
                    template.add("keyword3",MathUtil.getInt(order.getIntegralGift().toString()));
                logger.info("微信推送开始"+template.build().toString());
                    Map<String, Object> ddd123 = accountService.getXCXSend(template);
                    logger.info("微信推送结束"+ddd123.toString());
            }*/
            //根据部门增加积分.

                    //总积分
                    BigDecimal zongIntegral=BigDecimal.ZERO;
                    //反现比例
                    Double zhiFuFanBi =  redisSetting.getDouble("zhiFuFanBi");
                    if(zhiFuFanBi==null){
                        zhiFuFanBi=0d;
                    }
                    //返现金额
                    BigDecimal fanXianMoney=order.getAmount().multiply(BigDecimal.valueOf(zhiFuFanBi));

                    //待分配的积分
                    BigDecimal fengPeiIntegral=order.getIntegralGift();

            List<IntegralStore> integralStoreList=integralStoreService.findLogByMemberId(order.getMemberId());
                    logger.info("开始计算反现:"+integralStoreList.size());

                    for (IntegralStore integralStore : integralStoreList) {
                    //返钱
                        Double bili=Double.valueOf(integralStore.get("scale").toString());
                        MathContext mc=new MathContext(3);
                        BigDecimal money111=fanXianMoney.multiply(BigDecimal.valueOf(bili),mc);
                        logger.info("返现金额:"+money111);
                        logger.info("返现比例:"+bili);
                        if(money111.compareTo(BigDecimal.ZERO)==1){
                        Member member2 = memberService.find(integralStore.getStoreMemberId());
                        DepositLog depositLog1 = new DepositLog();
                        depositLog1.setBalance(member2.getBalance());
                        depositLog1.setCredit(money111);
                        depositLog1.setDebit(BigDecimal.ZERO);
                        depositLog1.setStatus(1);
                        depositLog1.setMemo("积分抵扣收益 ");
                        depositLog1.setType(DepositLog.Type.ident.ordinal());
                        depositLog1.setOrderId(order.getId());
                        depositLog1.setOperator(""+member.getNickname()+"  "+member.getPhone());
                        depositLog1.setMemberId(member2.getId());
                        member2.setBalance(money111.add(member2.getBalance()));
                        depositLogService.save(depositLog1);
                        memberService.update(member2);

                            /**
                             *反现短信提醒
                             */
                            Map<String, Object> params = new HashMap<String, Object>();

                            params.put("name",member2.getNickname());
                            params.put("zmoney",order.getAmount() );
                            params.put("store",bili*100d) ;
                            params.put("money",fanXianMoney );
                            ApiResult result = SMSUtils.send(member2.getPhone(),"SMS_173405304", params);
                            //ApiResult result = SMSUtils.send("", "", params);
                            if(result.resultSuccess()) {
                                // sm.setex("PONHE:"+mobile,120,"1");
                                Sms sms = new Sms();
                                sms.setMobile(member2.getPhone());
                                sms.setSmsCode(member2.getNickname()+"用户完成了一笔"+order.getAmount()+"元的订单，您的门店积分占该用户总积分的"+bili*100d+"%，因此获得"+fanXianMoney+"元积分抵扣收益，详情请查看钱包—使用明细。");
                                sms.setSmsType(Setting.SmsType.other.ordinal());
                                smsService.saveOrUpdate(sms);
                                logger.info(member2.getNickname()+"用户完成了一笔"+order.getAmount()+"元的订单，您的门店积分占该用户总积分的"+bili*100d+"%，因此获得"+fanXianMoney+"元积分抵扣收益，详情请查看钱包—使用明细。");
                            }else {
                                logger.info("您发送的过于频繁,请稍后再试!");
                            }

                            //增加门店比例
                            IntegralStoreLog integralStoreLog=new IntegralStoreLog();
                            BigDecimal meige = fengPeiIntegral.multiply(BigDecimal.valueOf(bili), mc);
                            integralStore.setBalance(integralStore.getBalance().add(meige));
                            integralStoreService.update(integralStore);
                        // 增加积分占比
                            integralStoreLog.setBalance(integralStore.getBalance());
                            integralStoreLog.setCredit(meige);
                            integralStoreLog.setDebit(BigDecimal.ZERO);
                            integralStoreLog.setMemberId(member.getId());
                            integralStoreLog.setType(1);
                            integralStoreLog.setStoreMemberId(integralStore.getStoreMemberId());
                            integralStoreLog.setMemo("绑定代金卡获取积分增加相应门店权重");
                            integralStoreLogService.save(integralStoreLog);



                            //扣除积分占比

                            IntegralStoreLog integralStoreLog1=new IntegralStoreLog();
                            BigDecimal meige1 = order.getIntegralPaid().multiply(BigDecimal.valueOf(bili), mc);
                            integralStore.setBalance(integralStore.getBalance().subtract(meige1));
                            integralStoreService.update(integralStore);
                            // 增加积分占比
                            integralStoreLog1.setBalance(integralStore.getBalance());
                            integralStoreLog1.setCredit(BigDecimal.ZERO);
                            integralStoreLog1.setDebit(meige1);
                            integralStoreLog1.setMemberId(member.getId());
                            integralStoreLog1.setType(1);
                            integralStoreLog1.setStoreMemberId(integralStore.getStoreMemberId());
                            integralStoreLog1.setMemo("绑定代金卡获取积分扣除相应门店权重");
                            integralStoreLogService.save(integralStoreLog1);






                        }



                    }


            }

        }
        //倒拍
        if (order.getType() == Order.Type.daopai.ordinal()) {
//			reverseExService.paySuccess(order.getActOrderId());
        }



        orderDao.update(order);


        logger.info("开始极光推送服务————————————————————————");
        try {
            informationService.paySuccessMessage(order);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        logger.info("结束极光推送服务————————————————————————");

        returnStatus = ApiResult.success();
        return returnStatus;
    }



    /**
     * 查找过期订单
     */
    public List<Order> findExpriceOrder() {
        return orderDao.findExpriceOrder();
    }


    /**
     * 查找过期订单
     */
    @Before(Tx.class)
    public void updateExperce(Member member) {
        List<Order> orders = orderDao.updateExperce(member);
        if (orders != null && orders.size() > 0) {
            for (Order order : orders) {

                order.setStatus(Order.Status.canceled.ordinal());
                order.setExpire(null);
                releaseAllocatedStock(order);
                super.update(order);
            }
        }

    }


    /**
     * 创建福袋订单
     */
    @Before(Tx.class)
    public Order createFudai(Order.Type type, FuDai fuDai, Receiver receiver, Boolean isInvoice, Boolean isPersonal, String taxNumber, String companyName) {

        List<ShippingMethod> shippingMethods = shippingMethodService.findMethodList();
        // 默认网上支付
        PaymentMethod paymentMethod = paymentMethodService.find(1L);

        //快递公司
        Member member = memberService.getCurrent();

        Order order = new Order();

        order.setIsInvoice(isInvoice);
        order.setIsPersonal(isPersonal);
        order.setTaxNumber(taxNumber);
        order.setCompanyName(companyName);

        order.setSn(snDao.generate(Sn.Type.order));
        order.setType(type.ordinal());
        order.setPaymentMethodId(1L);
        order.setPrice(new BigDecimal(fuDai.getPrice()));
        order.setFee(BigDecimal.ZERO);
        order.setFreight(BigDecimal.ZERO); //福袋运费是0
        order.setPromotionDiscount(BigDecimal.ZERO);
        order.setMiaobiPaid(BigDecimal.ZERO);
        order.setReturnCopyPaid(BigDecimal.ZERO);
        order.setOffsetAmount(BigDecimal.ZERO);
        order.setAmountPaid(BigDecimal.ZERO);
        order.setRefundAmount(BigDecimal.ZERO);
        order.setAreaId(receiver.getAreaId());
        order.setRewardPoint(0L);
        order.setExchangePoint(0L);
        order.setWeight(0);
        order.setQuantity(1);
        order.setShippedQuantity(0);
        order.setReturnedQuantity(0);
        order.setIsDelete(false);
        order.setConsignee(receiver.getConsignee());
        order.setAreaName(receiver.getAreaName());
        order.setAddress(receiver.getAddress());
        order.setZipCode(receiver.getZipCode());
        order.setPhone(receiver.getPhone());
        order.setArea(receiver.getArea());
        order.setMemo(null);
        order.setIsUseCouponCode(false);
        order.setIsExchangePoint(false);
        order.setIsAllocatedStock(false);
        // order.setInvoice(null);
        order.setShippingMethodId(shippingMethods.get(0).getId());
        order.setMemberId(member.getId());
        order.setCouponDiscount(BigDecimal.ZERO);
        order.setTax(calculateTax(order));
        order.setAmount(calculateAmount(order));
        order.setStatus(Order.Status.pendingPayment.ordinal());
        order.setPaymentMethod(null);
        order.setActOrderId(fuDai.getId().toString());
        Setting setting = SystemUtils.getSetting();
        BigDecimal amountPayable = order.getAmount();

        order.setTax(BigDecimal.ZERO);
        order.setStatus(Order.Status.pendingPayment.ordinal());
        order.setPaymentMethod(null);
        order.setPaymentMethodId(1L);
        order.setPaymentMethodName("网上支付");
        order.setExpire(DateUtils.addMinutes(new Date(), setting.getCommomPayTime()));
        lock(order, member);

        if (order.getArea() != null) {
            order.setAreaName(order.getArea().getFullName());
        }
        if (order.getPaymentMethod() != null) {
            order.setPaymentMethodName(order.getPaymentMethod().getName());
            order.setPaymentMethodType(order.getPaymentMethod().getType());
            order.setPaymentMethodId(paymentMethod.getId());
        }
        if (order.getShippingMethod() != null) {
            order.setShippingMethodName(order.getShippingMethod().getName());
        }

        orderDao.save(order);

        //先保存主产品,等付款后再抽取副产品

        List<Long> fudaiIdList = new ArrayList<>();//fuDaiProductService.lotteryProduct(fuDai.getId(),member.getId());
        FudaiProduct primary = fuDaiProductService.findPrimary(fuDai.getId());
        fudaiIdList.add(primary.getId());
        for (Long fudaiId : fudaiIdList) {
            Product product1 = productService.findProductByFudaiId(fudaiId);
            FudaiProduct fudaiProduct = fuDaiProductService.find(fudaiId);
            OrderItem orderItem = new OrderItem();
            orderItem.setSn(product1.getSn());
            orderItem.setName(product1.getName());
            orderItem.setType(product1.getType().ordinal());
            orderItem.setPrice(product1.getPrice());
            orderItem.setWeight(product1.getWeight());
            orderItem.setIsDelivery(product1.getIsDelivery());
            orderItem.setThumbnail(product1.getThumbnail());
            orderItem.setQuantity(1);
            orderItem.setShippedQuantity(0);
            orderItem.setThumbnail(product1.getThumbnail());
            orderItem.setReturnedQuantity(0);
            orderItem.setProductId(product1.getId());
            orderItem.setSpecifications(JSON.toJSONString(product1.getSpecifications()));
            orderItem.setOrderId(order.getId());
            orderItem.setActitemId(0l);
            orderItemDao.save(orderItem);
        }

        OrderLog orderLog = new OrderLog();
        orderLog.setType(OrderLog.Type.create.ordinal());
        orderLog.setOrderId(order.getId());
        orderLogDao.save(orderLog);

        Order order1 = orderDao.findOrderByStatus(member.getId(), "0");
        try {
            informationService.noPayMessage(order1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mailService.sendCreateOrderMail(order);
        smsService.sendCreateOrderSms(order);

        return order;

    }
    /**
     * 创建特殊订单
     */
    @Before(Tx.class)
    public Order createSpecial(Order.Type type, SpecialGoods fuDai, Receiver receiver, Boolean isInvoice, Boolean isPersonal, String taxNumber, String companyName) {

        List<ShippingMethod> shippingMethods = shippingMethodService.findMethodList();
        // 默认网上支付
        PaymentMethod paymentMethod = paymentMethodService.find(1L);

        //快递公司
        Member member = memberService.getCurrent();

        Order order = new Order();

        order.setIsInvoice(isInvoice);
        order.setIsPersonal(isPersonal);
        order.setTaxNumber(taxNumber);
        order.setCompanyName(companyName);

        order.setSn(snDao.generate(Sn.Type.order));
        order.setType(type.ordinal());
        order.setPaymentMethodId(1L);
        order.setPrice(new BigDecimal(fuDai.getPrice()));
        order.setFee(BigDecimal.ZERO);
        order.setFreight(BigDecimal.ZERO); //福袋运费是0
        order.setPromotionDiscount(BigDecimal.ZERO);
        order.setMiaobiPaid(BigDecimal.ZERO);
        order.setReturnCopyPaid(BigDecimal.ZERO);
        order.setOffsetAmount(BigDecimal.ZERO);
        order.setAmountPaid(BigDecimal.ZERO);
        order.setRefundAmount(BigDecimal.ZERO);
        order.setAreaId(receiver.getAreaId());
        order.setRewardPoint(0L);
        order.setExchangePoint(0L);
        order.setWeight(0);
        order.setQuantity(1);
        order.setShippedQuantity(0);
        order.setReturnedQuantity(0);
        order.setIsDelete(false);
        order.setConsignee(receiver.getConsignee());
        order.setAreaName(receiver.getAreaName());
        order.setAddress(receiver.getAddress());
        order.setZipCode(receiver.getZipCode());
        order.setPhone(receiver.getPhone());
        order.setArea(receiver.getArea());
        order.setMemo(null);
        order.setIsUseCouponCode(false);
        order.setIsExchangePoint(false);
        order.setIsAllocatedStock(false);
        // order.setInvoice(null);
        order.setShippingMethodId(shippingMethods.get(0).getId());
        order.setMemberId(member.getId());
        order.setCouponDiscount(BigDecimal.ZERO);
        order.setTax(calculateTax(order));
        order.setAmount(calculateAmount(order));
        order.setStatus(Order.Status.pendingPayment.ordinal());
        order.setPaymentMethod(null);
        order.setActOrderId(fuDai.getId().toString());
        Setting setting = SystemUtils.getSetting();
        BigDecimal amountPayable = order.getAmount();

        order.setTax(BigDecimal.ZERO);
        order.setStatus(Order.Status.pendingPayment.ordinal());
        order.setPaymentMethod(null);
        order.setPaymentMethodId(1L);
        order.setPaymentMethodName("网上支付");
        order.setExpire(DateUtils.addMinutes(new Date(), setting.getCommomPayTime()));
        lock(order, member);

        if (order.getArea() != null) {
            order.setAreaName(order.getArea().getFullName());
        }
        if (order.getPaymentMethod() != null) {
            order.setPaymentMethodName(order.getPaymentMethod().getName());
            order.setPaymentMethodType(order.getPaymentMethod().getType());
            order.setPaymentMethodId(paymentMethod.getId());
        }
        if (order.getShippingMethod() != null) {
            order.setShippingMethodName(order.getShippingMethod().getName());
        }

        orderDao.save(order);

        //先保存主产品,等付款后再抽取副产品

        List<Long> fudaiIdList = new ArrayList<>();//fuDaiProductService.lotteryProduct(fuDai.getId(),member.getId());
        FudaiProduct primary = fuDaiProductService.findPrimary(fuDai.getId());
        fudaiIdList.add(primary.getId());
        for (Long fudaiId : fudaiIdList) {
            Product product1 = productService.findProductByFudaiId(fudaiId);
            FudaiProduct fudaiProduct = fuDaiProductService.find(fudaiId);
            OrderItem orderItem = new OrderItem();
            orderItem.setSn(product1.getSn());
            orderItem.setName(product1.getName());
            orderItem.setType(product1.getType().ordinal());
            orderItem.setPrice(product1.getPrice());
            orderItem.setWeight(product1.getWeight());
            orderItem.setIsDelivery(product1.getIsDelivery());
            orderItem.setThumbnail(product1.getThumbnail());
            orderItem.setQuantity(1);
            orderItem.setShippedQuantity(0);
            orderItem.setThumbnail(product1.getThumbnail());
            orderItem.setReturnedQuantity(0);
            orderItem.setProductId(product1.getId());
            orderItem.setSpecifications(JSON.toJSONString(product1.getSpecifications()));
            orderItem.setOrderId(order.getId());
            orderItem.setActitemId(0l);
            orderItemDao.save(orderItem);
        }

        OrderLog orderLog = new OrderLog();
        orderLog.setType(OrderLog.Type.create.ordinal());
        orderLog.setOrderId(order.getId());
        orderLogDao.save(orderLog);

        Order order1 = orderDao.findOrderByStatus(member.getId(), "0");
        try {
            informationService.noPayMessage(order1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mailService.sendCreateOrderMail(order);
        smsService.sendCreateOrderSms(order);

        return order;

    }
    /**
     * 查找订单
     *
     * @param type             类型
     * @param status           状态
     * @param member           会员
     * @param goods            货品
     * @param isPendingReceive 是否等待收款
     * @param isPendingRefunds 是否等待退款
     * @param isUseCouponCode  是否已使用优惠码
     * @param isExchangePoint  是否已兑换积分
     * @param isAllocatedStock 是否已分配库存
     * @param hasExpired       是否已过期
     * @param count            数量
     * @param filters          筛选
     * @param orders           排序
     * @return 订单
     */
    public List<Order> findList(Order.Type type, Order.Status status, Member member, Goods goods, Boolean isPendingReceive, Boolean isPendingRefunds, Boolean isUseCouponCode, Boolean isExchangePoint, Boolean isAllocatedStock, Boolean hasExpired, Integer count, List<Filter> filters,
                                List<com.wtshop.Order> orders) {
        return orderDao.findList(type, status, member, goods, isPendingReceive, isPendingRefunds, isUseCouponCode, isExchangePoint, isAllocatedStock, hasExpired, count, filters, orders);
    }

    /**
     * 查找订单分页
     *
     * @param type             类型
     * @param status           状态
     * @param member           会员
     * @param goods            货品
     * @param isPendingReceive 是否等待收款
     * @param isPendingRefunds 是否等待退款
     * @param isUseCouponCode  是否已使用优惠码
     * @param isExchangePoint  是否已兑换积分
     * @param isAllocatedStock 是否已分配库存
     * @param hasExpired       是否已过期
     * @param pageable         分页信息
     * @return 订单分页
     */
    public Page<Order> findPage(Order.Type type, Order.Status status, Member member, Goods goods, Boolean isPendingReceive, Boolean isPendingRefunds, Boolean isUseCouponCode, Boolean isExchangePoint, Boolean isAllocatedStock, Boolean hasExpired, Pageable pageable) {
        if (status != null && status.ordinal() == 10) {
            status = Order.Status.completed;
        }
        return orderDao.findPage(type, status, member, goods, isPendingReceive, isPendingRefunds, isUseCouponCode, isExchangePoint, isAllocatedStock, hasExpired, pageable);
    }
    public Page<Order> findGoodsPage(Boolean isEcel,String adminId,String beginDate,String endDate,Order.Type type, Order.Status status, Member member, Goods goods, Boolean isPendingReceive, Boolean isPendingRefunds, Boolean isUseCouponCode, Boolean isExchangePoint, Boolean isAllocatedStock, Boolean hasExpired, Pageable pageable) {
        if (status != null && status.ordinal() == 10) {
            status = Order.Status.completed;
        }
        return orderDao.findGoodsPage(isEcel,adminId,beginDate,endDate,type, status, member, goods, isPendingReceive, isPendingRefunds, isUseCouponCode, isExchangePoint, isAllocatedStock, hasExpired, pageable);
    }

    public Page<Order> findPages(Order.Status status, Member member, Pageable pageable, Integer type) {
        return orderDao.findPages(status, member, pageable, type);
    }

    public Page<Order> findTuanGouPages(String status, Member member, Pageable pageable) {
        return orderDao.findTuanGouPages(status, member, pageable);
    }

    public Page<Order> findYongJinPages(String status, Member member, Pageable pageable) {
        return orderDao.findYongJinPages(status, member, pageable);
    }    public Page<Order> findWuXiaoYongJinPages(String status, Member member, Pageable pageable) {
        return orderDao.findWuXiaoYongJinPages(status, member, pageable);
    }

    public Page<Order> findYongJinXiaPages(Integer memberId, Member member, Pageable pageable) {
        return orderDao.findYongJinXiaPages(memberId, member, pageable);
    }

    /**
     * 查询已完成的订单 且退货单中不存在
     */
    public List<OrderGoods> findPages(Member member) {
        List<Order> orderList = orderDao.findPages(member);
        List<OrderGoods> orderGoodss = new ArrayList<>();
        OrderGoods orderGoods = new OrderGoods();
        for (Order order : orderList) {
            List<Goods> goods = goodsService.findGoodsByOrderId(order.getId());

            if (goods != null && goods.size() > 0) {
                orderGoods = new OrderGoods(goods, order);
                if (orderGoods.getOrder() != null) {
                    orderGoodss.add(orderGoods);
                }
            }
        }
        return orderGoodss;
    }

    /**
     * 查询订单数量
     *
     * @param type             类型
     * @param status           状态
     * @param member           会员
     * @param goods            货品
     * @param isPendingReceive 是否等待收款
     * @param isPendingRefunds 是否等待退款
     * @param isUseCouponCode  是否已使用优惠码
     * @param isExchangePoint  是否已兑换积分
     * @param isAllocatedStock 是否已分配库存
     * @param hasExpired       是否已过期
     * @return 订单数量
     */
    public Long count(Order.Type type, Order.Status status, Member member, Goods goods, Boolean isPendingReceive, Boolean isPendingRefunds, Boolean isUseCouponCode, Boolean isExchangePoint, Boolean isAllocatedStock, Boolean hasExpired) {
        return orderDao.count(type, status, member, goods, isPendingReceive, isPendingRefunds, isUseCouponCode, isExchangePoint, isAllocatedStock, hasExpired);
    }

    /**
     * 计算税金
     *
     * @param price             商品价格
     * @param promotionDiscount 促销折扣
     * @param couponDiscount    优惠券折扣
     * @param offsetAmount      调整金额
     * @return 税金
     */
    public BigDecimal calculateTax(BigDecimal price, BigDecimal promotionDiscount, BigDecimal couponDiscount, BigDecimal offsetAmount) {
        Assert.notNull(price);
        Assert.state(price.compareTo(BigDecimal.ZERO) >= 0);
        Assert.state(promotionDiscount == null || promotionDiscount.compareTo(BigDecimal.ZERO) >= 0);
        Assert.state(couponDiscount == null || couponDiscount.compareTo(BigDecimal.ZERO) >= 0);

        Setting setting = SystemUtils.getSetting();
        if (!setting.getIsTaxPriceEnabled()) {
            return BigDecimal.ZERO;
        }
        BigDecimal amount = price;
        if (promotionDiscount != null) {
            amount = amount.subtract(promotionDiscount);
        }
        if (couponDiscount != null) {
            amount = amount.subtract(couponDiscount);
        }
        if (offsetAmount != null) {
            amount = amount.add(offsetAmount);
        }
        BigDecimal tax = amount.multiply(new BigDecimal(String.valueOf(setting.getTaxRate())));
        return tax.compareTo(BigDecimal.ZERO) >= 0 ? setting.setScale(tax) : BigDecimal.ZERO;
    }

    /**
     * 计算税金
     *
     * @param order 订单
     * @return 税金
     */
    public BigDecimal calculateTax(Order order) {
        Assert.notNull(order);

        if (order.getInvoice() == null) {
            return BigDecimal.ZERO;
        }
        return calculateTax(order.getPrice(), order.getPromotionDiscount(), order.getCouponDiscount(), order.getOffsetAmount());
    }

    /**
     * 计算订单金额
     *
     * @param price             商品价格
     * @param fee               支付手续费
     * @param freight           运费
     * @param tax               税金
     * @param promotionDiscount 促销折扣
     * @param couponDiscount    优惠券折扣
     * @param offsetAmount      调整金额
     * @return 订单金额
     */
    public BigDecimal calculateAmount(BigDecimal price, BigDecimal fee, BigDecimal freight, BigDecimal tax, BigDecimal promotionDiscount, BigDecimal couponDiscount, BigDecimal offsetAmount) {
        Assert.notNull(price);
        Assert.state(price.compareTo(BigDecimal.ZERO) >= 0);
        Assert.state(fee == null || fee.compareTo(BigDecimal.ZERO) >= 0);
        Assert.state(freight == null || freight.compareTo(BigDecimal.ZERO) >= 0);
        Assert.state(tax == null || tax.compareTo(BigDecimal.ZERO) >= 0);
        Assert.state(promotionDiscount == null || promotionDiscount.compareTo(BigDecimal.ZERO) >= 0);
        Assert.state(couponDiscount == null || couponDiscount.compareTo(BigDecimal.ZERO) >= 0);

        Setting setting = SystemUtils.getSetting();
        BigDecimal amount = price;
        if (fee != null) {
            amount = amount.add(fee);
        }
        if (freight != null) {
            amount = amount.add(freight);
        }
        if (tax != null) {
            amount = amount.add(tax);
        }
        if (promotionDiscount != null) {
            amount = amount.subtract(promotionDiscount);
        }
        if (couponDiscount != null) {
            amount = amount.subtract(couponDiscount);
        }
        if (offsetAmount != null) {
            amount = amount.add(offsetAmount);
        }
        return amount.compareTo(BigDecimal.ZERO) >= 0 ? setting.setScale(amount) : BigDecimal.ZERO;
    }

    /**
     * 计算订单金额
     *
     * @param order 订单
     * @return 订单金额
     */
    public BigDecimal calculateAmount(Order order) {
        Assert.notNull(order);

        return calculateAmount(order.getPrice(), order.getFee(), order.getFreight(), order.getTax(), order.getPromotionDiscount(), order.getCouponDiscount(), order.getOffsetAmount());
    }

    /**
     * 判断订单是否锁定
     *
     * @param order    订单
     * @param admin    管理员
     * @param autoLock 是否自动加锁
     * @return 订单是否锁定
     */
    public boolean isLocked(Order order, Admin admin, boolean autoLock) {
        Assert.notNull(order);
        Assert.notNull(admin);

        boolean isLocked = order.getLockExpire() != null && order.getLockExpire().after(new Date()) && StringUtils.isNotEmpty(order.getLockKey()) && !StringUtils.equals(order.getLockKey(), admin.getLockKey());
        if (autoLock && !isLocked && StringUtils.isNotEmpty(admin.getLockKey())) {
            order.setLockKey(admin.getLockKey());
            order.setLockExpire(DateUtils.addSeconds(new Date(), Order.LOCK_EXPIRE));
        }
        return isLocked;
    }

    /**
     * 判断订单是否锁定
     *
     * @param order    订单
     * @param member   会员
     * @param autoLock 是否自动加锁
     * @return 订单是否锁定
     */
    public boolean isLocked(Order order, Member member, boolean autoLock) {
        Assert.notNull(order);
        Assert.notNull(member);

        boolean isLocked = order.getLockExpire() != null && order.getLockExpire().after(new Date()) && StringUtils.isNotEmpty(order.getLockKey()) && !StringUtils.equals(order.getLockKey(), member.getLockKey());
        if (autoLock && !isLocked && StringUtils.isNotEmpty(member.getLockKey())) {
            order.setLockKey(member.getLockKey());
            order.setLockExpire(DateUtils.addSeconds(new Date(), Order.LOCK_EXPIRE));
        }
        return isLocked;
    }

    /**
     * 订单锁定
     *
     * @param order 订单
     * @param admin 管理员
     */
    public void lock(Order order, Admin admin) {
        Assert.notNull(order);
        Assert.notNull(admin);

        boolean isLocked = order.getLockExpire() != null && order.getLockExpire().after(new Date()) && StringUtils.isNotEmpty(order.getLockKey()) && !StringUtils.equals(order.getLockKey(), admin.getLockKey());
        if (!isLocked && StringUtils.isNotEmpty(admin.getLockKey())) {
            order.setLockKey(admin.getLockKey());
            order.setLockExpire(DateUtils.addSeconds(new Date(), Order.LOCK_EXPIRE));
        }
    }

    /**
     * 订单锁定
     *
     * @param order  订单
     * @param member 会员
     */
    public void lock(Order order, Member member) {
        Setting setting = SystemUtils.getSetting();
        Assert.notNull(order);
        Assert.notNull(member);

        boolean isLocked = order.getLockExpire() != null && order.getLockExpire().after(new Date()) && StringUtils.isNotEmpty(order.getLockKey()) && !StringUtils.equals(order.getLockKey(), member.getLockKey());
        if (!isLocked && StringUtils.isNotEmpty(member.getLockKey())) {
            order.setLockKey(member.getLockKey());
            order.setLockExpire(DateUtils.addSeconds(new Date(), setting.getCommomPayTime()));
        }
    }

    /**
     * 过期订单优惠码使用撤销
     */
    public void undoExpiredUseCouponCode() {
        while (true) {
            List<Order> orders = orderDao.findList(null, null, null, null, null, null, true, null, null, true, 100, null, null);
            if (CollectionUtils.isNotEmpty(orders)) {
                for (Order order : orders) {
                    undoUseCouponCode(order);
                }
//				orderDao.flush();
//				orderDao.clear();
            }
            if (orders.size() < 100) {
                break;
            }
        }
    }

//	/**
//	 * 过期订单积分兑换撤销
//	 */
//	public void undoExpiredExchangePoint() {
//		while (true) {
//			List<Order> orders = orderDao.findList(null, null, null, null, null, null, null, true, null, true, 100, null, null);
//			if (CollectionUtils.isNotEmpty(orders)) {
//				for (Order order : orders) {
//					undoExchangePoint(order);
//				}
////				orderDao.flush();
////				orderDao.clear();
//			}
//			if (orders.size() < 100) {
//				break;
//			}
//		}
//	}

    /**
     * 释放过期订单已分配库存
     */
    public void releaseExpiredAllocatedStock() {
        while (true) {
            List<Order> orders = orderDao.findList(null, null, null, null, null, null, null, null, true, true, 100, null, null);
            if (CollectionUtils.isNotEmpty(orders)) {
                for (Order order : orders) {
                    releaseAllocatedStock(order);
                }
//				orderDao.flush();
//				orderDao.clear();
            }
            if (orders.size() < 100) {
                break;
            }
        }
    }

    /**
     * 订单生成
     *
     * @param type           类型
     * @param cart           购物车
     * @param receiver       收货地址
     * @param paymentMethod  支付方式
     * @param shippingMethod 配送方式
     * @param couponCode     优惠码
     * @param invoice        发票
     * @param balance        使用余额
     * @param memo           附言
     * @return 订单
     */
    public Order generate(Order.Type type, Cart cart, Receiver receiver, PaymentMethod paymentMethod, ShippingMethod shippingMethod, CouponCode couponCode, Invoice invoice, BigDecimal balance, String memo) {
        Assert.notNull(type);
        Assert.notNull(cart);
        Assert.notNull(cart.getMember());
        Assert.state(!cart.isEmpty());

        Setting setting = SystemUtils.getSetting();
        Member member = cart.getMember();

        Order order = new Order();
        order.setType(type.ordinal());
        order.setPrice(cart.getPrice());
        order.setFee(BigDecimal.ZERO);
        order.setPromotionDiscount(cart.getDiscount());
        order.setOffsetAmount(BigDecimal.ZERO);
        order.setRefundAmount(BigDecimal.ZERO);
        order.setRewardPoint(cart.getEffectiveRewardPoint());
        order.setExchangePoint(cart.getExchangePoint());
        order.setWeight(cart.getWeight());
        order.setQuantity(cart.getQuantity());
        order.setShippedQuantity(0);
        order.setReturnedQuantity(0);
        order.setMemo(memo);
        order.setIsUseCouponCode(false);
        order.setIsExchangePoint(false);
        order.setIsAllocatedStock(false);
        order.setInvoice(setting.getIsInvoiceEnabled() ? invoice : null);
        order.setPaymentMethodId(paymentMethod != null ? paymentMethod.getId() : null);
        order.setMember(member);
        order.setPromotionNames(JSON.toJSONString(cart.getPromotionNames()));
        order.setCoupons(new ArrayList<Coupon>(cart.getCoupons()));
        order.setIsDelete(false);
        if (shippingMethod != null && shippingMethod.isSupported(paymentMethod) && cart.getIsDelivery()) {
            order.setFreight(!cart.isFreeShipping() ? shippingMethodService.calculateFreight(shippingMethod, receiver, cart.getWeight()) : BigDecimal.ZERO);
            order.setShippingMethodId(shippingMethod.getId());
        } else {
            order.setFreight(BigDecimal.ZERO);
            order.setShippingMethod(null);
        }

        if (couponCode != null && cart.isCouponAllowed() && cart.isValid(couponCode)) {
            BigDecimal couponDiscount = cart.getEffectivePrice().subtract(couponCode.getCoupon().calculatePrice(cart.getEffectivePrice(), cart.getProductQuantity()));
            order.setCouponDiscount(couponDiscount.compareTo(BigDecimal.ZERO) >= 0 ? couponDiscount : BigDecimal.ZERO);
            order.setCouponCode(couponCode);
        } else {
            order.setCouponDiscount(BigDecimal.ZERO);
            order.setCouponCode(null);
        }

        order.setTax(calculateTax(order));
        order.setAmount(calculateAmount(order));

        if (balance != null && balance.compareTo(BigDecimal.ZERO) > 0 && balance.compareTo(member.getBalance()) <= 0 && balance.compareTo(order.getAmount()) <= 0) {
            order.setAmountPaid(balance);
        } else {
            order.setAmountPaid(BigDecimal.ZERO);
        }

        if (cart.getIsDelivery() && receiver != null) {
            order.setConsignee(receiver.getConsignee());
            order.setAreaName(receiver.getAreaName());
            order.setAddress(receiver.getAddress());
            order.setZipCode(receiver.getZipCode());
            order.setPhone(receiver.getPhone());
            order.setArea(receiver.getArea());
            order.setAreaId(receiver.getAreaId());
        }

        List<OrderItem> orderItems = order.getOrderItems();
        for (CartItem cartItem : cart.getCartItems()) {
            Product product = cartItem.getProduct();
            if (product != null) {
                OrderItem orderItem = new OrderItem();
                orderItem.setSn(product.getSn());
                orderItem.setName(product.getName());
                orderItem.setType(product.getType().ordinal());
                orderItem.setPrice(cartItem.getPrice());
                orderItem.setWeight(product.getWeight());
                orderItem.setIsDelivery(product.getIsDelivery());
                orderItem.setThumbnail(product.getThumbnail());
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setShippedQuantity(0);
                orderItem.setReturnedQuantity(0);
                orderItem.setProduct(cartItem.getProduct());
                orderItem.setOrder(order);
                orderItem.setSpecifications(JSON.toJSONString(product.getSpecifications()));
                orderItems.add(orderItem);
            }
        }

        for (Product gift : cart.getGifts()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setSn(gift.getSn());
            orderItem.setName(gift.getName());
            orderItem.setType(gift.getType().ordinal());
            orderItem.setPrice(BigDecimal.ZERO);
            orderItem.setWeight(gift.getWeight());
            orderItem.setIsDelivery(gift.getIsDelivery());
            orderItem.setThumbnail(gift.getThumbnail());
            orderItem.setQuantity(1);
            orderItem.setShippedQuantity(0);
            orderItem.setReturnedQuantity(0);
            orderItem.setProduct(gift);
            orderItem.setOrder(order);
            orderItem.setSpecifications(JSON.toJSONString(gift.getSpecifications()));
            orderItems.add(orderItem);
        }

        return order;
    }

    /**
     * 订单创建
     *
     * @param type     类型
     * @param cart     购物车
     * @param receiver 收货地址
     * @param memo     附言
     * @return 订单
     */

    public Order create(Order.Type type, Cart cart, Double manjianPrice, Receiver receiver, Double amountMoney, Double returnMoney, Double deliveryMoney, Double miaobiMoney, String memo, Double couponYunfei,
                        Boolean isInvoice, Boolean isPersonal, String taxNumber, String companyName,Long identifierId,String sPecialCoupId,Double specialCouponPrice,Double integral,Double integralMoney) {
        Assert.notNull(type);
        Assert.notNull(cart);
        Assert.notNull(cart.getMember());
        Assert.state(!cart.isEmpty());
        if (cart.getIsDelivery()) {
            Assert.notNull(receiver);
        } else {
            Assert.isNull(receiver);
        }

        for (CartItem cartItem : cart.getCartItems()) {
            Product product = cartItem.getProduct();
            if (product == null || !product.getIsMarketable() || cartItem.getQuantity() > product.getAvailableStock()) {
                throw new IllegalArgumentException();
            }
        }

        JSONObject redisSetting = JSONObject.parseObject(RedisUtil.getString("redisSetting"));
        Member member = cart.getMember();

        Order order = new Order();

        order.setIsInvoice(isInvoice);
        order.setIsPersonal(isPersonal);
        order.setTaxNumber(taxNumber);
        order.setCompanyName(companyName);
        order.setIntegral(BigDecimal.valueOf(integral));
        order.setIntegralPaid(BigDecimal.valueOf(integralMoney));



        if(order.getType()==10){
            order.setIntegralGift(BigDecimal.valueOf(amountMoney) );
        }else {
            order.setIntegralGift(BigDecimal.ZERO );
        }

        order.setIdentifierId(identifierId);
        order.setSn(snDao.generate(Sn.Type.order));
        order.setType(type.ordinal());
        order.setPrice(cart.getPrice());
        order.setFee(new BigDecimal(deliveryMoney));
        order.setFreight(new BigDecimal(couponYunfei));
        order.setMiaobiPaid(new BigDecimal(miaobiMoney));
        order.setWeixinPaid(BigDecimal.ZERO);
        order.setAliPaid(BigDecimal.ZERO);
        order.setOrderNo(null);
        order.setPromotionDiscount(cart.getDiscount());
        order.setOffsetAmount(BigDecimal.ZERO);
        order.setAmountPaid(BigDecimal.ZERO);
        order.setRefundAmount(BigDecimal.ZERO);
        order.setCouponDiscount(BigDecimal.ZERO);
        order.setRewardPoint(0L);
        order.setExchangePoint(cart.getExchangePoint());
        order.setWeight(cart.getWeight());
        order.setQuantity(cart.getQuantity());
        order.setAmount(new BigDecimal(amountMoney));
        order.setReturnCopyPaid(new BigDecimal(returnMoney));
        order.setShippedQuantity(0);
        order.setReturnedQuantity(0);
        order.setSpecialcoupId(sPecialCoupId);
        order.setSpecialCouponPrice(BigDecimal.valueOf(specialCouponPrice));
        order.setIsDelete(false);
        if (cart.getIsDelivery()) {
            order.setConsignee(receiver.getConsignee());
            order.setAreaName(receiver.getAreaName());
            order.setAddress(receiver.getAddress());
            order.setZipCode(receiver.getZipCode());
            order.setPhone(receiver.getPhone());
            order.setArea(receiver.getArea());
            order.setAreaId(receiver.getAreaId());
        }
        order.setMemo(memo);
        order.setIsUseCouponCode(false);
        order.setIsExchangePoint(false);
        order.setIsAllocatedStock(false);
        // order.setInvoice(null);
        order.setShippingMethodId(1L);
        order.setMemberId(member.getId());
        order.setPromotionNames(JSON.toJSONString(cart.getPromotionNames()));
        order.setCoupons(new ArrayList<Coupon>(cart.getCoupons()));

        if (manjianPrice > 0) {
            order.setType(Order.Type.manjian.ordinal());
            order.setPromotionDiscount(new BigDecimal(manjianPrice));
            order.setPromotionNames("满减促销");
        }

//		if (couponCode != null) {
//			order.setCouponCodeId(couponCode.getId());
//			BigDecimal couponDiscount = new BigDecimal(couponCode.getCoupon().getMoney());
//			order.setCouponDiscount(couponDiscount.compareTo(BigDecimal.ZERO) >= 0 ? couponDiscount : BigDecimal.ZERO);
//			order.setCouponCode(couponCode);
//			useCouponCode(order);
//		} else {
//			order.setCouponDiscount(BigDecimal.ZERO);
//		}

        order.setTax(BigDecimal.ZERO);
        order.setStatus(Order.Status.pendingPayment.ordinal());
        order.setPaymentMethod(null);
        order.setPaymentMethodId(1L);
        order.setPaymentMethodName("网上支付");
        order.setExpire(DateUtils.addMinutes(new Date(), redisSetting.getInteger("commomPayTime")));
        lock(order, member);

//		//-1表示小于，0是等于，1是大于
//		if (balance != null && (balance.compareTo(BigDecimal.ZERO) < 0 || balance.compareTo(member.getBalance()) > 0 /*|| balance.compareTo(order.getAmount()) > 0*/)) {
//			throw new IllegalArgumentException();
//		}
//		BigDecimal amountPayable = balance != null ? order.getAmount().subtract(balance) : order.getAmount();
//		if (amountPayable.compareTo(BigDecimal.ZERO) > 0) {
//			if (paymentMethod == null) {
//				throw new IllegalArgumentException();
//			}
//			order.setStatus(PaymentMethod.Type.deliveryAgainstPayment.equals(paymentMethod.getTypeName())  ? Order.Status.pendingPayment.ordinal() : Order.Status.pendingReview.ordinal());
//
//			if (paymentMethod.getTimeout() != null && Order.Status.pendingPayment.equals(order.getStatusName())) {
//				order.setExpire(DateUtils.addMinutes(new Date(),setting.getCommomPayTime()));
//			}
//			if (PaymentMethod.Method.online.ordinal() == paymentMethod.getMethod()) {
//				lock(order, member);
//			}
//		}

        if (order.getArea() != null) {
            order.setAreaName(order.getArea().getFullName());
        }
        if (order.getPaymentMethod() != null) {
            order.setPaymentMethodName(order.getPaymentMethod().getName());
            order.setPaymentMethodType(order.getPaymentMethod().getType());
        }
        if (order.getShippingMethod() != null) {
            order.setShippingMethodName(order.getShippingMethod().getName());
        }

        List<OrderItem> orderItems = order.getOrderItems();
        for (CartItem cartItem : cart.getCartItems()) {
            Product product = cartItem.getProduct();
            OrderItem orderItem = new OrderItem();
            orderItem.setSn(product.getSn());
            orderItem.setName(product.getName());
            orderItem.setType(product.getType().ordinal());
            orderItem.setPrice(cartItem.getPrice());
            orderItem.setWeight(product.getWeight());
            orderItem.setIsDelivery(product.getIsDelivery());
            orderItem.setThumbnail(product.getThumbnail());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setShippedQuantity(0);
            orderItem.setReturnedQuantity(0);
            orderItem.setSpecialGoodsId(cartItem.getSpecialId());
            orderItem.setProductId(cartItem.getProduct().getId());
            orderItem.setSpecifications(JSON.toJSONString(product.getSpecifications()));
            orderItems.add(orderItem);
        }

        orderDao.save(order);
        if (CollectionUtils.isNotEmpty(orderItems)) {
            for (OrderItem orderItem : orderItems) {
                orderItem.setOrderId(order.getId());
                orderItemDao.save(orderItem);
            }
        }

        OrderLog orderLog = new OrderLog();
        orderLog.setType(OrderLog.Type.create.ordinal());
        orderLog.setOrderId(order.getId());
        orderLogDao.save(orderLog);


        if ( Setting.StockAllocationTime.order.name().equals(redisSetting.get("stockAllocationTime")) || (Setting.StockAllocationTime.payment.name().equals(redisSetting.get("stockAllocationTime")) && (order.getAmountPaid().compareTo(BigDecimal.ZERO) > 0 || order.getExchangePoint() > 0 || order.getAmountPayable().compareTo(BigDecimal.ZERO) <= 0))) {
            allocateStock(order);
        }


        mailService.sendCreateOrderMail(order);
        smsService.sendCreateOrderSms(order);

        if (!cart.isNew()) {
            for (CartItem cartItem : cart.getCartItems()) {
                cartItemDao.deleteByItemId(cartItem.getId());
            }
        }
        try {
            informationService.noPayMessage(order);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        Cache actcache = Redis.use();
//        actcache.del("CART:"+member.getId());

        return order;
    }


    /**
     * 订单创建
     *
     * @param type     类型
     * @param receiver 收货地址
     * @param memo     附言
     * @return 订单
     */

    public Order createBuyNow(Order.Type type, Member member, Goods goods, Double price, int quantity, Double manjianPrice, Receiver receiver, Double amountMoney, Double deliveryMoney, Double
            miaobiMoney, String memo, Double couponYunfei, Boolean isInvoice, Boolean isPersonal, String taxNumber
            , String companyName, Boolean isSinglepurchase, long fightGroupId, long tuanGouId, Double rate,Long sPecialIds,Long identifierId,Double specialCouponPrice,String sPecialCoupId,Double integralMoney,Double integral) {


        JSONObject redisSetting = JSONObject.parseObject(RedisUtil.getString("redisSetting"));
        Order order = new Order();
        order.setSn(snDao.generate(Sn.Type.order));
        order.setType(type.ordinal());
        order.setIsSinglepurchase(isSinglepurchase);
        order.setFightgroupId(fightGroupId);
        order.setGroupbuyId(tuanGouId);
        order.setIsInvoice(isInvoice);
        order.setIsPersonal(isPersonal);
        order.setTaxNumber(taxNumber);
        order.setCompanyName(companyName);
        order.setCommissionRate(rate);
        order.setOnShareCode(member.getOnShareCode());
        order.setShareCode(member.getShareCode());
        order.setPrice(new BigDecimal(price));
        order.setFee(new BigDecimal(deliveryMoney));
        order.setFreight(new BigDecimal(couponYunfei));
        order.setMiaobiPaid(new BigDecimal(miaobiMoney));
        order.setWeixinPaid(BigDecimal.ZERO);
        order.setAliPaid(BigDecimal.ZERO);
        order.setOrderNo(null);
        order.setPromotionDiscount(BigDecimal.ZERO);
        order.setOffsetAmount(BigDecimal.ZERO);
        order.setAmountPaid(BigDecimal.ZERO);
        order.setRefundAmount(BigDecimal.ZERO);
        order.setIntegralPaid(new BigDecimal(integralMoney));
        order.setIntegral(new BigDecimal(integral));
        order.setIntegralGift(new BigDecimal(amountMoney));
        order.setCouponDiscount(BigDecimal.ZERO);
        order.setRewardPoint(0L);
        order.setExchangePoint(0L);
        order.setWeight(goods.getWeight());
        order.setQuantity(quantity);
        order.setAmount(new BigDecimal(amountMoney));
        order.setReturnCopyPaid(BigDecimal.ZERO);
        order.setShippedQuantity(0);
        order.setReturnedQuantity(0);
        order.setIsDelete(false);
        order.setSpecialcoupId(sPecialCoupId);
        order.setSpecialCouponPrice(BigDecimal.valueOf(specialCouponPrice));

        order.setConsignee(receiver.getConsignee());
        order.setAreaName(receiver.getAreaName());
        order.setAddress(receiver.getAddress());
        order.setZipCode(receiver.getZipCode());
        order.setPhone(receiver.getPhone());
        order.setArea(receiver.getArea());
        order.setAreaId(receiver.getAreaId());

        order.setMemo(memo);
        order.setIsUseCouponCode(false);
        order.setIsExchangePoint(false);
        order.setIsAllocatedStock(false);
        //  order.setInvoice(null);
        order.setShippingMethodId(1L);
        order.setMemberId(member.getId());
        order.setPromotionNames(null);
        order.setCoupons(null);

        if (manjianPrice > 0) {
            order.setType(Order.Type.manjian.ordinal());
            order.setPromotionDiscount(new BigDecimal(manjianPrice));
            order.setPromotionNames("满减促销");
        }

        order.setTax(BigDecimal.ZERO);
        order.setStatus(Order.Status.pendingPayment.ordinal());
        order.setPaymentMethod(null);
        order.setIdentifierId(identifierId);
        order.setPaymentMethodId(1L);
        order.setPaymentMethodName("网上支付");
        order.setExpire(DateUtils.addMinutes(new Date(), redisSetting.getInteger("commomPayTime")));
        lock(order, member);


        if (order.getArea() != null) {
            order.setAreaName(order.getArea().getFullName());
        }
        if (order.getPaymentMethod() != null) {
            order.setPaymentMethodName(order.getPaymentMethod().getName());
            order.setPaymentMethodType(order.getPaymentMethod().getType());
        }
        if (order.getShippingMethod() != null) {
            order.setShippingMethodName(order.getShippingMethod().getName());
        }
        orderDao.save(order);

        Product product = productService.findByGoodsId(goods.getId());

        OrderItem orderItem = new OrderItem();
        orderItem.setSn(product.getSn());
        orderItem.setName(product.getName());
        orderItem.setType(product.getType().ordinal());
        orderItem.setPrice(goods.getPrice());
        orderItem.setWeight(product.getWeight());
        orderItem.setIsDelivery(product.getIsDelivery());
        orderItem.setSpecialGoodsId(sPecialIds);
        orderItem.setThumbnail(product.getThumbnail());
        orderItem.setQuantity(quantity);
        orderItem.setShippedQuantity(0);
        orderItem.setReturnedQuantity(0);
        orderItem.setProductId(product.getId());
        orderItem.setSpecifications(JSON.toJSONString(product.getSpecifications()));
        orderItem.setOrderId(order.getId());
        orderItemDao.save(orderItem);


        OrderLog orderLog = new OrderLog();
        orderLog.setType(OrderLog.Type.create.ordinal());
        orderLog.setOrderId(order.getId());
        orderLogDao.save(orderLog);


        if (Setting.StockAllocationTime.order.name().equals(redisSetting.get("stockAllocationTime")) || (Setting.StockAllocationTime.payment.name().equals(redisSetting.get("stockAllocationTime")) && (order.getAmountPaid().compareTo(BigDecimal.ZERO) > 0 || order.getExchangePoint() > 0 || order.getAmountPayable().compareTo(BigDecimal.ZERO) <= 0))) {
            allocateStock(order);
        }


        mailService.sendCreateOrderMail(order);
        smsService.sendCreateOrderSms(order);

        try {
            informationService.noPayMessage(order);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return order;
    }


    //创建倒拍的普通订单
    @Before(Tx.class)
    public Order createReverseauction(Order.Type type, Cart cart, Receiver receiver, PaymentMethod paymentMethod, ShippingMethod shippingMethod,
                                      CouponCode couponCode, Invoice invoice, BigDecimal balance, String memo, String actOrderId) {
        Setting setting = SystemUtils.getSetting();


        Cache actcache = Redis.use();
        Map<String, Object> map = (Map<String, Object>) actcache.get("reverseauctionHistroy-" + actOrderId);
        if (map == null) {
            return null;
        }

        BigDecimal priceD = new BigDecimal((String) map.get("price"));

        Member member = cart.getMember();
        Order order = new Order();
        order.setSn(snDao.generate(Sn.Type.order));
        order.setType(type.ordinal());
        order.setPrice(priceD);
        order.setFee(BigDecimal.ZERO);
        order.setFreight(cart.getIsDelivery() && !cart.isFreeShipping() ? shippingMethodService.calculateFreight(shippingMethod, receiver, cart.getWeight()) : BigDecimal.ZERO);
        order.setPromotionDiscount(cart.getDiscount());
        order.setOffsetAmount(BigDecimal.ZERO);
        order.setAmountPaid(BigDecimal.ZERO);
        order.setRefundAmount(BigDecimal.ZERO);
        order.setRewardPoint(cart.getEffectiveRewardPoint());
        order.setExchangePoint(cart.getExchangePoint());
        order.setWeight(cart.getWeight());
        order.setQuantity(cart.getQuantity());
        order.setShippedQuantity(0);
        order.setReturnedQuantity(0);
        order.setIsDelete(false);
        order.setActOrderId(actOrderId);
        if (cart.getIsDelivery()) {
            order.setConsignee(receiver.getConsignee());
            order.setAreaName(receiver.getAreaName());
            order.setAddress(receiver.getAddress());
            order.setZipCode(receiver.getZipCode());
            order.setPhone(receiver.getPhone());
            order.setArea(receiver.getArea());
            order.setAreaId(receiver.getAreaId());
        }
        order.setMemo(memo);
        order.setIsUseCouponCode(false);
        order.setIsExchangePoint(false);
        order.setIsAllocatedStock(false);
        order.setInvoice(setting.getIsInvoiceEnabled() ? invoice : null);
        order.setShippingMethodId(shippingMethod.getId());
        order.setMemberId(member.getId());
        order.setPromotionNames(JSON.toJSONString(cart.getPromotionNames()));
        order.setCoupons(new ArrayList<Coupon>(cart.getCoupons()));

        if (couponCode != null) {
            if (!cart.isCouponAllowed() || !cart.isValid(couponCode)) {
                throw new IllegalArgumentException();
            }
            order.setCouponCodeId(couponCode.getId());
            BigDecimal couponDiscount = cart.getEffectivePrice().subtract(couponCode.getCoupon().calculatePrice(cart.getEffectivePrice(), cart.getProductQuantity()));
            order.setCouponDiscount(couponDiscount.compareTo(BigDecimal.ZERO) >= 0 ? couponDiscount : BigDecimal.ZERO);
            order.setCouponCode(couponCode);
            useCouponCode(order);
        } else {
            order.setCouponDiscount(BigDecimal.ZERO);
        }

        order.setTax(calculateTax(order));
        order.setAmount(calculateAmount(order));
        order.setStatus(Order.Status.pendingShipment.ordinal());
        order.setPaymentMethod(null);
        //-1表示小于，0是等于，1是大于
        if (balance != null && (balance.compareTo(BigDecimal.ZERO) < 0 || balance.compareTo(member.getBalance()) > 0 /*|| balance.compareTo(order.getAmount()) > 0*/)) {
            throw new IllegalArgumentException();
        }
        BigDecimal amountPayable = balance != null ? order.getAmount().subtract(balance) : order.getAmount();
        if (amountPayable.compareTo(BigDecimal.ZERO) > 0) {
            if (paymentMethod == null) {
                throw new IllegalArgumentException();
            }
            order.setStatus(PaymentMethod.Type.deliveryAgainstPayment.equals(paymentMethod.getTypeName()) ? Order.Status.pendingPayment.ordinal() : Order.Status.pendingShipment.ordinal());
            order.setPaymentMethodId(paymentMethod.getId());
            if (paymentMethod.getTimeout() != null && Order.Status.pendingPayment.equals(order.getStatusName())) {
                order.setExpire(DateUtils.addMinutes(new Date(), setting.getCommomPayTime()));
            }
            if (PaymentMethod.Method.online.ordinal() == paymentMethod.getMethod()) {
                lock(order, member);
            }
        } else {
            order.setStatus(Order.Status.pendingShipment.ordinal());
            order.setPaymentMethod(null);
        }

        if (order.getArea() != null) {
            order.setAreaName(order.getArea().getFullName());
        }
        if (order.getPaymentMethod() != null) {
            order.setPaymentMethodName(order.getPaymentMethod().getName());
            order.setPaymentMethodType(order.getPaymentMethod().getType());
        }
        if (order.getShippingMethod() != null) {
            order.setShippingMethodName(order.getShippingMethod().getName());
        }

        List<OrderItem> orderItems = order.getOrderItems();
        for (CartItem cartItem : cart.getCartItems()) {
            Product product = cartItem.getProduct();
            OrderItem orderItem = new OrderItem();
            orderItem.setSn(product.getSn());
            orderItem.setName(product.getName());
            orderItem.setType(product.getType().ordinal());
            orderItem.setPrice(priceD);
            orderItem.setWeight(product.getWeight());
            orderItem.setIsDelivery(product.getIsDelivery());
            orderItem.setThumbnail(product.getThumbnail());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setShippedQuantity(0);
            orderItem.setReturnedQuantity(0);
            orderItem.setProductId(cartItem.getProduct().getId());
            //orderItem.setOrderId(order.getId());
            orderItem.setSpecifications(JSON.toJSONString(product.getSpecifications()));
            orderItems.add(orderItem);
        }

        for (Product gift : cart.getGifts()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setSn(gift.getSn());
            orderItem.setName(gift.getName());
            orderItem.setType(gift.getType().ordinal());
            orderItem.setPrice(BigDecimal.ZERO);
            orderItem.setWeight(gift.getWeight());
            orderItem.setIsDelivery(gift.getIsDelivery());
            orderItem.setThumbnail(gift.getThumbnail());
            orderItem.setQuantity(1);
            orderItem.setShippedQuantity(0);
            orderItem.setReturnedQuantity(0);
            orderItem.setProduct(gift);
            //orderItem.setOrder(order);
            orderItem.setSpecifications(JSON.toJSONString(gift.getSpecifications()));
            orderItems.add(orderItem);
        }

        orderDao.save(order);
        if (CollectionUtils.isNotEmpty(orderItems)) {
            for (OrderItem orderItem : orderItems) {
                orderItem.setOrderId(order.getId());
                orderItemDao.save(orderItem);
            }
        }

        OrderLog orderLog = new OrderLog();
        orderLog.setType(OrderLog.Type.create.ordinal());
        orderLog.setOrderId(order.getId());
        orderLogDao.save(orderLog);

//		exchangePoint(order);
        if (Setting.StockAllocationTime.order.name().equals(setting.getStockAllocationTime()) || (Setting.StockAllocationTime.payment.name().equals(setting.getStockAllocationTime()) && (order.getAmountPaid().compareTo(BigDecimal.ZERO) > 0 || order.getExchangePoint() > 0 || order.getAmountPayable().compareTo(BigDecimal.ZERO) <= 0))) {
            allocateStock(order);
        }

        if (balance != null && balance.compareTo(BigDecimal.ZERO) > 0) {
            Payment payment = new Payment();
            payment.setMethod(Payment.Method.deposit.ordinal());
            if (amountPayable.compareTo(BigDecimal.ZERO) > 0) {
                payment.setAmount(balance);
            } else {
                payment.setAmount(order.getAmount());
            }
            payment.setOrderId(order.getId());
            payment(order, payment, null);
        }

        mailService.sendCreateOrderMail(order);
        smsService.sendCreateOrderSms(order);

        if (!cart.isNew()) {
            //cartItemDao.delete(cart.getId());
            //cartDao.remove(cart);
            for (CartItem cartItem : cart.getCartItems()) {
                cartItemDao.deleteByItemId(cartItem.getId());
            }
        }

        try {
            informationService.noPayMessage(order);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return order;
    }


    /**
     * 订单更新
     *
     * @param order    订单
     * @param operator 操作员
     */
    public void update(Order order, Admin operator) {
        Assert.notNull(order);
        Assert.isTrue(!order.isNew());
        Assert.state(!order.hasExpired() && (Order.Status.pendingPayment.equals(order.getStatusName()) || Order.Status.pendingShipment.equals(order.getStatusName())));

        order.setAmount(calculateAmount(order));
        if (order.getAmountPayable().compareTo(BigDecimal.ZERO) <= 0) {
            order.setStatus(Order.Status.pendingShipment.ordinal());
            order.setExpire(null);
        } else {
            if (order.getPaymentMethod() != null && PaymentMethod.Type.deliveryAgainstPayment.equals(order.getPaymentMethod().getTypeName())) {
                order.setStatus(Order.Status.pendingPayment.ordinal());
            } else {
                order.setStatus(Order.Status.pendingShipment.ordinal());
                order.setExpire(null);
            }
        }

        if (order.getArea() != null) {
            order.setAreaName(order.getArea().getFullName());
        }
        if (order.getPaymentMethod() != null) {
            order.setPaymentMethodName(order.getPaymentMethod().getName());
            order.setPaymentMethodType(order.getPaymentMethod().getType());
        }
        if (order.getShippingMethod() != null) {
            order.setShippingMethodName(order.getShippingMethod().getName());
        }

        orderDao.update(order);

        OrderLog orderLog = new OrderLog();
        orderLog.setType(OrderLog.Type.update.ordinal());
        orderLog.setOperator(operator);
        orderLog.setOrderId(order.getId());
        orderLogDao.save(orderLog);

        mailService.sendUpdateOrderMail(order);
        smsService.sendUpdateOrderSms(order);
    }


    /**
     * 订单取消
     *
     * @param order 订单
     */
    @Before(Tx.class)
    public void cancel(Order order, String cause, String desc, String url) {
        Assert.notNull(order);
        Assert.isTrue(!order.isNew());
        Assert.state(!order.hasExpired() && (Order.Status.pendingPayment.equals(order.getStatusName()) || Order.Status.pendingShipment.equals(order.getStatusName())));
        JSONObject redisSetting = JSONObject.parseObject(RedisUtil.getString("redisSetting"));
        if (Order.Status.pendingShipment.ordinal() == order.getStatus()) {
            //支付单号
            String orderNo = "";
            //订单金额
            Double orderPrice = 0D;
            //退款金额
            Double price = 0D;
            //更新用户余额
            Member member = memberService.find(order.getMemberId());
            //退喵币
            if (order.getMiaobiPaid().doubleValue() > 0) {
                Double scale = redisSetting.getDouble("scale");
                //  换算喵币
                BigDecimal miaobiScaled = order.getMiaobiPaid().multiply(new BigDecimal(scale)).setScale(2, BigDecimal.ROUND_HALF_UP);
                member.setPoint(member.getPoint().add(miaobiScaled).setScale(2, ROUND_HALF_DOWN));
                //  添加喵币记录
                MiaobiLog pointLog = new MiaobiLog();
                pointLog.setType(3);
                pointLog.setCredit(miaobiScaled);
                pointLog.setDebit(BigDecimal.ZERO);
                pointLog.setBalance(member.getPoint());
                pointLog.setMemo("订单退款");
                pointLog.setMemberId(member.getId());
                miaobiLogService.save(pointLog);
            }

            // 全部使用余额支付
            if (order.getAmountPaid().doubleValue() > 0 && order.getAmountPaid().doubleValue() == order.getAmount().doubleValue()) {
                member.setBalance(member.getBalance().add(order.getAmountPaid()).setScale(2, ROUND_HALF_UP));
                //  添加余额消费记录
                DepositLog depositLog = new DepositLog();
                depositLog.setBalance(member.getBalance());
                depositLog.setCredit(order.getAmountPaid());
                depositLog.setDebit(BigDecimal.ZERO);
                depositLog.setMemo("订单退款");
                depositLog.setType(DepositLog.Type.refunds.ordinal());
                depositLog.setOrderId(order.getId());
                depositLog.setMemberId(member.getId());
                depositLogDao.save(depositLog);
                memberService.update(member);
            } else if (order.getAmountPaid().doubleValue() > 0) {
                //  部分使用余额支付
                BigDecimal balace = null;
                //  微信退部分+余额退部分
                if (order.getWeixinPaid().doubleValue() > 0) {
                    orderNo = order.getOrderNo();
                    try {
                        //  总订单金额
                        Integer totalPrice = Integer.parseInt(String.format("%.0f", order.getWeixinPaid().doubleValue() * 100));
                        //余额退款
                        Double balaceReturnPrice = order.getAmountPaid().setScale(2, ROUND_HALF_DOWN).doubleValue();
                        balace = new BigDecimal(balaceReturnPrice).setScale(2, ROUND_HALF_DOWN);
                        //微信退款
                        Double wechatReturnPrice = order.getWeixinPaid().setScale(2, ROUND_HALF_DOWN).doubleValue();
                        Integer wechatReturnPriceIntValue = Integer.parseInt(String.format("%.0f", wechatReturnPrice * 100));
                        Map<String, String> map = accountService.BackToWeChat(orderNo, totalPrice, wechatReturnPriceIntValue, "任性猫退款");
                        if (map == null || !"SUCCESS".equals(map.get("result_code"))) {

                            //  renderJson(ApiResult.fail("余额不足"));

                            throw new AppRuntimeException(Code.FAIL, "微信退款失败");
                        }
                    } catch (Exception e) {
                        logger.error("微信退款失败: " + e.getCause());
                        logger.error("微信退款失败详情: " + orderNo);
                        throw new AppRuntimeException(Code.FAIL, "微信退款失败");
                    }

                }
                // 支付宝退部分+余额退部分
                if (order.getAliPaid().doubleValue() > 0) {
                    orderNo = order.getOrderNo();
                    try {
                        //余额退款
                        Double balaceReturnPrice = order.getAmountPaid().setScale(2, ROUND_HALF_DOWN).doubleValue();
                        balace = new BigDecimal(balaceReturnPrice).setScale(2, ROUND_HALF_DOWN);
                        //支付宝退款
                        Double alipayReturnPrice = order.getAliPaid().setScale(2, ROUND_HALF_DOWN).doubleValue();
                        accountService.tradeRefund(orderNo, alipayReturnPrice, member.getId());
                    } catch (Exception e) {
                        logger.error("支付宝退款失败: " + e.getCause());
                        logger.error("支付宝退款失败详情: " + orderNo);
                        throw new AppRuntimeException(Code.FAIL, "支付宝退款失败");
                    }
                }
                if (balace != null)
                    member.setBalance(member.getBalance().add(balace).setScale(2, ROUND_HALF_DOWN));

                //添加余额消费记录
                DepositLog depositLog = new DepositLog();
                depositLog.setBalance(member.getBalance());
                depositLog.setCredit(balace == null ? BigDecimal.ZERO : balace);
                depositLog.setDebit(BigDecimal.ZERO);
                depositLog.setMemo("订单退款");
                depositLog.setType(DepositLog.Type.refunds.ordinal());
                depositLog.setOrderId(order.getId());
                depositLog.setMemberId(member.getId());
                depositLogDao.save(depositLog);

                memberService.update(member);
            } else {
                orderNo = order.getOrderNo();
                //  没有使用余额支付
                //  支付宝退
                if (order.getAliPaid().doubleValue() > 0) {
                    try {
                        //支付宝退款
                        Double alipayReturnPrice = order.getAliPaid().setScale(2, ROUND_HALF_DOWN).doubleValue();
                        accountService.tradeRefund(orderNo, alipayReturnPrice, member.getId());
                    } catch (Exception e) {
                        logger.error("支付宝退款失败: " + e.getCause());
                        logger.error("支付宝退款失败详情: " + orderNo);
                        throw new AppRuntimeException(Code.FAIL, "支付宝退款失败");
                    }
                }
                //  微信退
                if (order.getWeixinPaid().doubleValue() > 0) {
                    try {
                        //  总订单金额
                        Integer totalPrice = Integer.parseInt(String.format("%.0f", order.getWeixinPaid().doubleValue() * 100));
                        //微信退款
                        Double wechatReturnPrice = order.getWeixinPaid().setScale(2, ROUND_HALF_DOWN).doubleValue();
                        Integer wechatReturnPriceIntValue = Integer.parseInt(String.format("%.0f", wechatReturnPrice * 100));
                        Map<String, String> map = accountService.BackToWeChat(orderNo, totalPrice, wechatReturnPriceIntValue, "任性猫退款");
                        if (map == null || !"SUCCESS".equals(map.get("result_code"))) {
                            throw new AppRuntimeException(Code.FAIL, "微信退款失败");
                        }
                    } catch (Exception e) {
                        logger.error("微信退款失败: " + e.getCause());
                        logger.error("微信退款失败详情: " + orderNo);
                        throw new AppRuntimeException(Code.FAIL, "微信退款失败");
                    }
                }
            }

        }
        order.setStatus(Order.Status.canceled.ordinal());
        order.setExpire(null);
        undoUseCouponCode(order);
//		undoExchangePoint(order);


        releaseAllocatedStock(order);
        super.update(order);

        OrderCancel orderCancel = new OrderCancel();
        orderCancel.setReason(cause);
        orderCancel.setDesc(desc);
        orderCancel.setImage(url);
        orderCancel.setOrderId(order.getId());
        cencelService.save(orderCancel);

        OrderLog orderLog = new OrderLog();
        orderLog.setType(OrderLog.Type.cancel.ordinal());
        orderLog.setOrderId(order.getId());
        orderLogDao.save(orderLog);
        mailService.sendCancelOrderMail(order);
        smsService.sendCancelOrderSms(order);
    }

    /**
     * 订单审核
     *
     * @param order    订单
     * @param passed   是否审核通过
     * @param operator 操作员
     */
    public void review(Order order, boolean passed, Admin operator) {
        Assert.notNull(order);
        Assert.isTrue(!order.isNew());
        Assert.state(!order.hasExpired() && Order.Status.pendingShipment.equals(order.getStatusName()));

        if (passed) {
            order.setStatus(Order.Status.pendingShipment.ordinal());
        } else {
            order.setStatus(Order.Status.denied.ordinal());

            undoUseCouponCode(order);
//			undoExchangePoint(order);
            releaseAllocatedStock(order);
        }
        super.update(order);

        OrderLog orderLog = new OrderLog();
        orderLog.setType(OrderLog.Type.review.ordinal());
        orderLog.setOperator(operator);
        orderLog.setOrderId(order.getId());
        orderLogDao.save(orderLog);

        mailService.sendReviewOrderMail(order);
        smsService.sendReviewOrderSms(order);
    }

    /**
     * 订单收款
     *
     * @param order    订单
     * @param payment  收款单
     * @param operator 操作员
     */
    public void payment(Order order, Payment payment, Admin operator) {
        Assert.notNull(order);
        Assert.isTrue(!order.isNew());
        Assert.notNull(payment);
        Assert.isTrue(payment.isNew());
        Assert.notNull(payment.getAmount());
        Assert.state(payment.getAmount().compareTo(BigDecimal.ZERO) > 0);

        payment.setSn(snDao.generate(Sn.Type.payment));
        payment.setOrderId(order.getId());
        paymentDao.save(payment);

//		if (order.getMember() != null && Payment.Method.deposit.equals(payment.getMethodName())) {
//			memberService.addBalance(order.getMember(), payment.getEffectiveAmount().negate(), DepositLog.Type.payment, operator,"订单支付");
//		}

        Setting setting = SystemUtils.getSetting();
        if (Setting.StockAllocationTime.payment.equals(setting.getStockAllocationTime())) {
            allocateStock(order);
        }

//		order.setAmountPaid(order.getAmountPaid().add(payment.getEffectiveAmount()));
//		if (!order.hasExpired() && Order.Status.pendingPayment.equals(order.getStatusName()) && order.getAmountPayable().compareTo(BigDecimal.ZERO) <= 0) {
//			order.setStatus(Order.Status.pendingShipment.ordinal());
//			order.setExpire(null);
//		}
        super.update(order);
        LogKit.info("订单收款成功更新订单：" + order.getSn());

        OrderLog orderLog = new OrderLog();
        orderLog.setType(OrderLog.Type.payment.ordinal());
        orderLog.setOperator(operator);
        orderLog.setOrderId(order.getId());
        orderLogDao.save(orderLog);

        mailService.sendPaymentOrderMail(order);
        smsService.sendPaymentOrderSms(order);
    }


    /**
     * 订单发货
     *
     * @param order    订单
     * @param shipping 发货单
     * @param operator 操作员
     */
    @Before(Tx.class)
    public void shipping(Order order, Shipping shipping, Admin operator) {
        Assert.notNull(order);
        Assert.isTrue(!order.isNew());
        Assert.state(order.getShippableQuantity() > 0);
        Assert.notNull(shipping);
        Assert.isTrue(shipping.isNew());
        Assert.notEmpty(shipping.getShippingItems());

        shipping.setSn(snDao.generate(Sn.Type.shipping));
        shipping.setOrderId(order.getId());
        shippingDao.save(shipping);

        List<ShippingItem> shippingItems = shipping.getShippingItems();
        if (CollectionUtils.isNotEmpty(shippingItems)) {
            for (ShippingItem shippingItem : shippingItems) {
                shippingItem.setShippingId(shipping.getId());
                shippingItemDao.save(shippingItem);
            }
        }

        Setting setting = SystemUtils.getSetting();
        if (Setting.StockAllocationTime.ship.name().equals(setting.getStockAllocationTime())) {
            allocateStock(order);
        }

        for (ShippingItem shippingItem : shipping.getShippingItems()) {
            OrderItem orderItem = order.getOrderItem(shippingItem.getSn());

            orderItem.setShippedQuantity(orderItem.getShippedQuantity() + shippingItem.getQuantity());
            orderItem.update();
            Product product = shippingItem.getProduct();
            if (product != null) {
                if (shippingItem.getQuantity() > product.getStock()) {
                    throw new IllegalArgumentException();
                }
                productService.addStock(product, -shippingItem.getQuantity(), StockLog.Type.stockOut, operator, null);
                if (BooleanUtils.isTrue(order.getIsAllocatedStock())) {
                    productService.addAllocatedStock(product, -shippingItem.getQuantity());
                }
            }
        }

        order.setShippedQuantity(order.getShippedQuantity() + shipping.getQuantity());
        order.setFee(shipping.getFreight());
        if (order.getShippedQuantity() >= order.getQuantity()) {
            order.setStatus(Order.Status.shipped.ordinal());
            order.setIsAllocatedStock(false);
        }
        super.update(order);

        OrderLog orderLog = new OrderLog();
        orderLog.setType(OrderLog.Type.shipping.ordinal());
        orderLog.setOperator(operator);
        orderLog.setOrderId(order.getId());
        orderLogDao.save(orderLog);

        try {
            informationService.sendPeoductMessage(order);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mailService.sendShippingOrderMail(order);
        smsService.sendShippingOrderSms(order);
    }

    /**
     * 发货订单退货
     *
     * @param order    订单
     * @param returns  退货单
     * @param operator 操作员
     */
    @Before(Tx.class)
    public void returns(Order order, Returns returns, Admin operator) {
        Assert.notNull(order);
        Assert.isTrue(!order.isNew());
        Assert.state(order.getReturnableQuantity() > 0);
        Assert.notNull(returns);
        Assert.isTrue(returns.isNew());
        Assert.notEmpty(returns.getReturnsItems());

        returns.setSn(snDao.generate(Sn.Type.returns));
        returns.setOrderId(order.getId());
        returns.setPhone(order.getPhone());
        returns.setType(0);
        returns.setStatus(2);
        returnsDao.save(returns);

        for (ReturnsItem returnsItem : returns.getReturnsItems()) {
            OrderItem orderItem = order.getOrderItem(returnsItem.getSn());
            if (orderItem == null || returnsItem.getQuantity() > orderItem.getReturnableQuantity()) {
                throw new IllegalArgumentException();
            }
            returnsItem.setReturnId(returns.getId());
            returnsItem.setMemberId(order.getMemberId());
            returnsItem.setStatus(ReturnsItem.Status.pendingReview.ordinal());
            returnsItemDao.save(returnsItem);

            orderItem.setReturnedQuantity(orderItem.getReturnedQuantity() + returnsItem.getQuantity());
            orderItem.update();
        }

        ReturnsItemProgress returnsItemProgress = new ReturnsItemProgress();
        returnsItemProgress.setDesc("您的售后申请已提交，待售后审核中");
        returnsItemProgress.setReturnsId(returns.getId());
        returnsItemProgress.setStatus(1);
        returnsItemProgress.setType(0);
        returnsItemProgressDao.save(returnsItemProgress);

        order.setReturnedQuantity(order.getReturnedQuantity() + returns.getQuantity());
        super.update(order);


        OrderLog orderLog = new OrderLog();
        orderLog.setType(OrderLog.Type.returns.ordinal());
        orderLog.setOperator(operator);
        orderLog.setOrderId(order.getId());
        orderLogDao.save(orderLog);

        mailService.sendReturnsOrderMail(order);
        smsService.sendReturnsOrderSms(order);
    }


    /**
     * 未发货订单退货
     *
     * @param order    订单
     * @param returns  退货单
     * @param operator 操作员
     */
    @Before(Tx.class)
    public void returnd(Order order, Returns returns, Admin operator) {
        Assert.notNull(order);
        Assert.isTrue(!order.isNew());
        Assert.notNull(returns);
        Assert.isTrue(returns.isNew());
        Assert.notEmpty(returns.getReturnsItems());
        if (order.getStatus() == 2) {
            returns.setStatus(0);
        } else if (order.getStatus() == 3) {
            returns.setStatus(1);
        }
        returns.setSn(snDao.generate(Sn.Type.returns));
        returns.setOrderId(order.getId());
        returns.setPhone(order.getPhone());
        if (3 == order.getType()) {
            returns.setStatus(0);
        } else if (4 == order.getType()) {
            returns.setStatus(1);
        }

        returnsDao.save(returns);

        for (ReturnsItem returnsItem : returns.getReturnsItems()) {
            OrderItem orderItem = order.getOrderItem(returnsItem.getSn());

            returnsItem.setReturnId(returns.getId());
            returnsItem.setMemberId(order.getMemberId());
            returnsItem.setStatus(ReturnsItem.Status.pendingReview.ordinal());
            returnsItemDao.save(returnsItem);

            orderItem.setReturnedQuantity(orderItem.getReturnedQuantity() + returnsItem.getQuantity());
            orderItem.update();
        }


        order.setReturnedQuantity(order.getReturnedQuantity() + returns.getQuantity());
        super.update(order);


        ReturnsItemProgress returnsItemProgress = new ReturnsItemProgress();
        returnsItemProgress.setDesc("您的售后申请已提交，待售后审核中");
        returnsItemProgress.setReturnsId(returns.getId());
        returnsItemProgress.setStatus(1);
        returnsItemProgress.setType(1);
        returnsItemProgressDao.save(returnsItemProgress);

        OrderLog orderLog = new OrderLog();
        orderLog.setType(OrderLog.Type.returns.ordinal());
        orderLog.setOperator(operator);
        orderLog.setOrderId(order.getId());
        orderLogDao.save(orderLog);

        mailService.sendReturnsOrderMail(order);
        smsService.sendReturnsOrderSms(order);
    }

    /**
     * 订单收货
     *
     * @param order    订单
     * @param operator 操作员
     */
    public void receive(Order order, Admin operator) {
        Assert.notNull(order);
        Assert.isTrue(!order.isNew());
        Assert.state(!order.hasExpired() && Order.Status.shipped.equals(order.getStatusName()));

        order.setStatus(Order.Status.noReview.ordinal());
        super.update(order);
        Setting setting = SystemUtils.getSetting();
        Member member = order.getMember();

        if (setting.getIsSendMiaoBi() && order.getCouponDiscount().doubleValue() == 0 && order.getType() == Order.Type.general.ordinal()) {
            Double sendMiaoBiLimit = setting.getSendMiaoBiLimit();
            BigDecimal bigDecimal = new BigDecimal(sendMiaoBiLimit).multiply(order.getPrice()).setScale(2, BigDecimal.ROUND_DOWN);
            member.setPoint(member.getPoint().add(bigDecimal));
            memberService.update(member);
            //添加赠送记录
        }

        OrderLog orderLog = new OrderLog();
        orderLog.setType(OrderLog.Type.receive.ordinal());
        orderLog.setOperator(operator);
        orderLog.setOrderId(order.getId());
        orderLogDao.save(orderLog);

        mailService.sendReceiveOrderMail(order);
        smsService.sendReceiveOrderSms(order);
    }


    /**
     * 订单完成
     *
     * @param order    订单
     * @param operator 操作员
     */
    public void complete(Order order, Admin operator) {
        Assert.notNull(order);
        Assert.isTrue(!order.isNew());
        Assert.state(!order.hasExpired() && Order.Status.noReview.equals(order.getStatusName()));

        Member member = order.getMember();
//		if (order.getRewardPoint() > 0) {
//			memberService.addPoint(member, order.getRewardPoint(), PointLog.Type.reward, operator, null);
//		}
        if (CollectionUtils.isNotEmpty(order.getCoupons())) {
            for (Coupon coupon : order.getCoupons()) {
                couponCodeService.generate(coupon, member, 0);
            }
        }
        if (order.getAmountPaid().compareTo(BigDecimal.ZERO) > 0) {
            memberService.addAmount(member, order.getAmountPaid());
        }
        for (OrderItem orderItem : order.getOrderItems()) {
            Product product = orderItem.getProduct();
            if (product != null && product.getGoods() != null) {
                goodsService.addSales(product.getGoods(), orderItem.getQuantity());
            }
        }

        order.setStatus(Order.Status.completed.ordinal());
        order.setCompleteDate(new Date());
        super.update(order);

        OrderLog orderLog = new OrderLog();
        orderLog.setType(OrderLog.Type.complete.ordinal());
        orderLog.setOperator(operator);
        orderLog.setOrderId(order.getId());
        orderLogDao.save(orderLog);

        mailService.sendCompleteOrderMail(order);
        smsService.sendCompleteOrderSms(order);
    }

    /**
     * 订单失败
     *
     * @param order    订单
     * @param operator 操作员
     */
    public void fail(Order order, Admin operator) {
        Assert.notNull(order);
        Assert.isTrue(!order.isNew());
        Assert.state(!order.hasExpired() && (Order.Status.pendingShipment.equals(order.getStatusName()) || Order.Status.shipped.equals(order.getStatusName()) || Order.Status.noReview.equals(order.getStatusName())));

        order.setStatus(Order.Status.failed.ordinal());

        undoUseCouponCode(order);
//		undoExchangePoint(order);
        releaseAllocatedStock(order);
        super.update(order);

        OrderLog orderLog = new OrderLog();
        orderLog.setType(OrderLog.Type.fail.ordinal());
        orderLog.setOperator(operator);
        orderLog.setOrderId(order.getId());
        orderLogDao.save(orderLog);

        mailService.sendFailOrderMail(order);
        smsService.sendFailOrderSms(order);
    }

    public void delete(Order order) {
        if (order != null && !Order.Status.completed.equals(order.getStatusName())) {
            undoUseCouponCode(order);
//			undoExchangePoint(order);
            releaseAllocatedStock(order);
            super.update(order);
        }

        super.delete(order);
    }

    /**
     * 优惠码使用
     *
     * @param order 订单
     */
    private void useCouponCode(Order order) {
        if (order == null || BooleanUtils.isNotFalse(order.getIsUseCouponCode()) || order.getCouponCode() == null) {
            return;
        }
        CouponCode couponCode = order.getCouponCode();
        couponCode.setIsUsed(true);
        couponCode.setUsedDate(new Date());
        order.setIsUseCouponCode(true);
        couponCode.update();
    }

    /**
     * 优惠码使用撤销
     *
     * @param order 订单
     */
    private void undoUseCouponCode(Order order) {
        if (order == null || BooleanUtils.isNotTrue(order.getIsUseCouponCode()) || order.getCouponCode() == null) {
            return;
        }
        CouponCode couponCode = order.getCouponCode();
        couponCode.setIsUsed(false);
        couponCode.setUsedDate(null);
        order.setIsUseCouponCode(false);
        order.setCouponCode(null);
        couponCode.update();
    }

//	/**
//	 * 积分兑换
//	 *
//	 * @param order
//	 *            订单
//	 */
//	private void exchangePoint(Order order) {
//		if (order == null || BooleanUtils.isNotFalse(order.getIsExchangePoint()) || order.getExchangePoint() <= 0 || order.getMember() == null) {
//			return;
//		}
//		memberService.addPoint(order.getMember(), -order.getExchangePoint(), PointLog.Type.exchange, null, null);
//		order.setIsExchangePoint(true);
//	}

//	/**
//	 * 积分兑换撤销
//	 *
//	 * @param order
//	 *            订单
//	 */
//	private void undoExchangePoint(Order order) {
//		if (order == null || BooleanUtils.isNotTrue(order.getIsExchangePoint()) || order.getExchangePoint() <= 0 || order.getMember() == null) {
//			return;
//		}
//		memberService.addPoint(order.getMember(), order.getExchangePoint(), PointLog.Type.undoExchange, null, null);
//		order.setIsExchangePoint(false);
//	}

    /**
     * 分配库存
     *
     * @param order 订单
     */
    private void allocateStock(Order order) {
        if (order == null || BooleanUtils.isNotFalse(order.getIsAllocatedStock())) {
            return;
        }
        if (order.getOrderItems() != null) {
            for (OrderItem orderItem : order.getOrderItems()) {
                Product product = orderItem.getProduct();
                if (product != null) {
                    productService.addAllocatedStock(product, orderItem.getQuantity() - orderItem.getShippedQuantity());
                }
            }
        }
        order.setIsAllocatedStock(true);
        update(order);
    }

    /**
     * 释放已分配库存
     *
     * @param order 订单
     */
    private void releaseAllocatedStock(Order order) {
        if (order == null || BooleanUtils.isNotTrue(order.getIsAllocatedStock())) {
            return;
        }
        if (order.getOrderItems() != null) {
            for (OrderItem orderItem : order.getOrderItems()) {
                Product product = orderItem.getProduct();
                if (product != null) {
                    productService.addAllocatedStock(product, -(orderItem.getQuantity() - orderItem.getShippedQuantity()));
                }
            }
        }
        order.setIsAllocatedStock(false);
    }

    public Page queryListByPage(int number, int pageSize, String name, String phone) {
        return orderDao.queryListByPage(number, pageSize, name, phone);
    }


    /**
     * 喵币商场订单创建
     *
     * @param type
     * @param receiver
     * @param member
     * @param amountMoney
     * @param miaobiMoney
     * @param memo
     * @param price
     * @param goodsNum
     * @param goodsId
     * @param weight
     * @return
     */
    @Before(Tx.class)
    public Order createMiaoBi(Order.Type type, Receiver receiver, Member member, Double amountMoney, Double miaobiMoney, String memo, BigDecimal price, Integer goodsNum, Long goodsId, Integer weight, Boolean isInvoice, Boolean isPersonal, String taxNumber, String companyName) {
        Assert.notNull(type);
        JSONObject redisSetting = JSONObject.parseObject(RedisUtil.getString("redisSetting"));
        Setting setting = SystemUtils.getSetting();
        Double scale = redisSetting.getDouble("scale");
        Order order = new Order();

        order.setIsInvoice(isInvoice);
        order.setIsPersonal(isPersonal);
        order.setTaxNumber(taxNumber);
        order.setCompanyName(companyName);

        order.setSn(snDao.generate(Sn.Type.order));//订单类型
        order.setType(type.ordinal());//枚举
        order.setPrice(price);//商品价格
        order.setFee(BigDecimal.ZERO);
        order.setFreight(BigDecimal.ZERO);
        order.setMiaobiPaid(new BigDecimal(MathUtil.divide(miaobiMoney, 10)));
        order.setPromotionDiscount(BigDecimal.ZERO);
        order.setOffsetAmount(BigDecimal.ZERO);
        order.setAmountPaid(price);
        order.setRefundAmount(BigDecimal.ZERO);
        order.setCouponDiscount(BigDecimal.ZERO);
        order.setRewardPoint(0L);//赠送积分
        order.setExchangePoint(0L);//兑换积分
        order.setWeight(weight);//不知道重量
        order.setQuantity(goodsNum);//不知道数量
        order.setAmount(new BigDecimal(amountMoney));
        order.setReturnCopyPaid(BigDecimal.ZERO);
        order.setShippedQuantity(0);
        order.setReturnedQuantity(0);
        order.setIsDelete(false);
        //默认物流
        order.setConsignee(receiver.getConsignee());
        order.setAreaName(receiver.getAreaName());
        order.setAddress(receiver.getAddress());
        order.setZipCode(receiver.getZipCode());
        order.setPhone(receiver.getPhone());
        order.setArea(receiver.getArea());
        order.setAreaId(receiver.getAreaId());
        //结束默认物流

        order.setMemo(memo);
        order.setIsUseCouponCode(false);
        order.setIsExchangePoint(false);
        order.setIsAllocatedStock(false);
        //order.setInvoice(null);
        order.setShippingMethodId(1L);
        order.setMemberId(member.getId());
        order.setPromotionNames(null);
        order.setCoupons(new ArrayList<Coupon>());


        order.setTax(BigDecimal.ZERO);
        order.setStatus(Order.Status.pendingPayment.ordinal());
        order.setPaymentMethod(null);
        order.setPaymentMethodId(1L);
        order.setPaymentMethodName("网上支付");
        order.setExpire(DateUtils.addMinutes(new Date(), setting.getCommomPayTime()));
        lock(order, member);


        if (order.getArea() != null) {
            order.setAreaName(order.getArea().getFullName());
        }
        if (order.getPaymentMethod() != null) {
            order.setPaymentMethodName(order.getPaymentMethod().getName());
            order.setPaymentMethodType(order.getPaymentMethod().getType());
        }
        if (order.getShippingMethod() != null) {
            order.setShippingMethodName(order.getShippingMethod().getName());
        }

        List<OrderItem> orderItems = new ArrayList<>();
        Product product = productService.findByGoodsId(goodsId);
        OrderItem orderItem = new OrderItem();
        orderItem.setSn(product.getSn());
        orderItem.setName(product.getName());
        orderItem.setType(product.getType().ordinal());
        orderItem.setPrice(product.getPrice());//单价
        orderItem.setWeight(product.getWeight());//全部重量
        orderItem.setIsDelivery(product.getIsDelivery());
        orderItem.setThumbnail(product.getThumbnail());
        orderItem.setQuantity(goodsNum);//个数
        orderItem.setShippedQuantity(0);
        orderItem.setReturnedQuantity(0);
        orderItem.setProductId(product.getId());//product  的id
        orderItem.setSpecifications(JSON.toJSONString(product.getSpecifications()));
        orderItems.add(orderItem);


        orderDao.save(order);
        if (CollectionUtils.isNotEmpty(orderItems)) {
            for (OrderItem orderItemm : orderItems) {
                orderItem.setOrderId(order.getId());
                orderItemDao.save(orderItemm);
            }
        }

        OrderLog orderLog = new OrderLog();
        orderLog.setType(OrderLog.Type.create.ordinal());
        orderLog.setOrderId(order.getId());
        orderLogDao.save(orderLog);


        if (Setting.StockAllocationTime.order.name().equals(setting.getStockAllocationTime()) || (Setting.StockAllocationTime.payment.name().equals(setting.getStockAllocationTime()) && (order.getAmountPaid().compareTo(BigDecimal.ZERO) > 0 || order.getExchangePoint() > 0 || order.getAmountPayable().compareTo(BigDecimal.ZERO) <= 0))) {
            allocateStock(order);
        }

        try {
            informationService.noPayMessage(order);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return order;
    }

    /**
     * 获取自己购买福袋
     * @param id
     * @return
     */
    public List<Order> findByMemberId(Long id) {
      return  orderDao.findByMemberId(id);
    }
}