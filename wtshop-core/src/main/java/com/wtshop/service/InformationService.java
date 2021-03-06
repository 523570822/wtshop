package com.wtshop.service;

import com.jfinal.aop.Before;
import com.jfinal.aop.Enhancer;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.wtshop.Pageable;
import com.wtshop.dao.InformationDao;
import com.wtshop.dao.MemberFavoriteGoodsDao;
import com.wtshop.model.*;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;
import com.wtshop.util.JPush;
import com.wtshop.util.MathUtil;
import com.wtshop.util.RedisUtil;
import freemarker.log.Logger;

import java.util.List;

/**
 * Created by sq on 2017/8/1.
 */
public class InformationService extends BaseService<Information> {

    public InformationService() {
        super(Information.class);
    }

    private InformationDao informationDao = Enhancer.enhance(InformationDao.class);
    private MemberFavoriteGoodsDao memberFavoriteGoodsDao = Enhancer.enhance(MemberFavoriteGoodsDao.class);


    /**
     * 删除消息
     */
    public int updateMessage(Member member) {
        return informationDao.updateMessage(member);
    }


    /**
     * 技师信息
     */
    public Information findMember(Long id) {
        return informationDao.findMember(id);
    }


    /**
     * 消息列表
     */

    public Page<Record> findPageRecord(Pageable pageable, Integer type) {

        String searchProperty = pageable.getSearchProperty();
        String searchValue = pageable.getSearchValue();

        String title = null;
        String name = null;
        if ("title".equals(searchProperty)) {
            title = searchValue;
        } else if ("name".equals(searchProperty)) {
            name = searchValue;
        }
        return informationDao.findPageRecord(pageable, type, title, name);
    }

    /**
     * 消息分頁
     */
    public Page<Information> findPages(Member member, Pageable pageable, Integer type) {
        return informationDao.findPage(member, pageable, type);
    }

    /**
     * 全部未读的消息
     */
    public List<Information> findMessageNoRead(Member member, Boolean read) {
        return informationDao.findMessageNoRead(member, read);
    }

    /**
     * 未读消息个数
     */
    public Long findMessageNoReadCount(Member member, Boolean read) {
        return informationDao.findMessageNoReadCount(member, read);
    }

    /**
     * 系统消息
     */
    public void systemMessage() {
    }

    /**
     *
     * 赠送积分成功消息
     * @param integralLog
     */

    public void intergraSuccessMessage(IntegralLog integralLog) {
        final Logger logger = Logger.getLogger("paySuccessMessage");
        //添加消息记录表
        Information information = new Information();
        String typeS="";
        if(integralLog.getType()==0){
            typeS="钜惠卡";
        }else if(integralLog.getType()==1){
            typeS="代金卡";
        }

        information.setContent("绑定"+typeS+"成功");
        information.setIsDelete(false);
        information.setStatus(false);
        information.setMemberId(integralLog.getMemberId());
        information.setTitle("绑定"+typeS+"成功");
        information.setAction(Information.Action.none.ordinal());
        information.setLink(integralLog.getId()+"");
        information.setType(Information.Type.none.ordinal());
        informationDao.save(information);

        Cache actCache = Redis.use();
        Boolean isMyMessage = actCache.get("ORDERMMESSAGR_SWITCH:" + information.getMemberId());
        String sound = "default";
        Object o = actCache.get("SOUND:" + information.getMemberId());
        if (o != null) {
            sound = o.toString();
        }
        if (isMyMessage != null && isMyMessage) {
            String key = "MEMBER:" + information.getMemberId().toString();
            String appid = RedisUtil.getString(key);
            if (appid != null) {
                logger.info("开始调用极光推送方法——————————————————————; appid=" + appid);
                JPush.sendPushById(appid, "系统消息", typeS+"绑定成功", "送您的"+ MathUtil.getInt(integralLog.getCredit().toString())+"积分已到账", sound, null);

                logger.info("成功调用极光推送方法——————————————————————");
            }
        }
    }

    /**
     *
     * 赠送积分成功消息
     * @param integralLog
     */

    public void intergraRregisterMessage(IntegralLog integralLog) {
        final Logger logger = Logger.getLogger("paySuccessMessage");
        //添加消息记录表
        Information information = new Information();


        information.setContent("注册成功");
        information.setIsDelete(false);
        information.setStatus(false);
        information.setMemberId(integralLog.getMemberId());
        information.setTitle("注册成功");
        information.setAction(Information.Action.none.ordinal());
        information.setLink(integralLog.getId()+"");
        information.setType(Information.Type.none.ordinal());
        informationDao.save(information);

        Cache actCache = Redis.use();
        Boolean isMyMessage = actCache.get("ORDERMMESSAGR_SWITCH:" + information.getMemberId());
        String sound = "default";
        Object o = actCache.get("SOUND:" + information.getMemberId());
        if (o != null) {
            sound = o.toString();
        }
        if (isMyMessage != null && isMyMessage) {
            String key = "MEMBER:" + information.getMemberId().toString();
            String appid = RedisUtil.getString(key);
            if (appid != null) {
                logger.info("开始调用极光推送方法——————————————————————; appid=" + appid);
                JPush.sendPushById(appid, "系统消息", "注册成功", "送您的"+ MathUtil.getInt(integralLog.getCredit().toString())+"积分已到账", sound, null);

                logger.info("成功调用极光推送方法——————————————————————");
            }
        }
    }
    /**
     *支付成功消息
     *
     * @param order
     */

    public void paySuccessMessage(Order order) {
        final Logger logger = Logger.getLogger("paySuccessMessage");
        //添加消息记录表
        Information information = new Information();
        information.setContent("订单(" + order.getSn() + ")已完成付款");
        information.setIsDelete(false);
        information.setStatus(false);
        information.setMemberId(order.getMemberId());
        information.setTitle("订单(" + order.getSn() + ")已完成付款");
        information.setAction(Information.Action.order.ordinal());
        information.setLink(order.getSn().toString());
        information.setType(Information.Type.order.ordinal());
        informationDao.save(information);

        Cache actCache = Redis.use();
        Boolean isMyMessage = actCache.get("ORDERMMESSAGR_SWITCH:" + order.getMemberId());
        String sound = "default";
        Object o = actCache.get("SOUND:" + order.getMemberId());
        if (o != null) {
            sound = o.toString();
        }
        if (isMyMessage != null && isMyMessage) {
            String key = "MEMBER:" + order.getMemberId().toString();
            String appid = RedisUtil.getString(key);
            if (appid != null) {
                logger.info("开始调用极光推送方法——————————————————————; appid=" + appid);
                JPush.sendPushById(appid, "订单消息", "订单(" + order.getSn() + ")已完成付款", "订单(" + order.getSn() + ")已完成付款", sound, null);
                logger.info("成功调用极光推送方法——————————————————————");
            }
        }
    }
    /**
     * 团购提醒推送
     *
     * @param
     */

    public void groupRmindMessage(GroupRemind groupRemind,long num) {
        final Logger logger = Logger.getLogger("paySuccessMessage");
        //添加消息记录表
        Information information = new Information();
        information.setContent("团购提醒推送");
        information.setIsDelete(false);
        information.setStatus(false);
        information.setMemberId(groupRemind.getMemberId());
        information.setTitle("团购提醒推送");
        information.setAction(Information.Action.none.ordinal());
        information.setLink(groupRemind.getGroupId()+"");
        information.setType(Information.Type.none.ordinal());
        informationDao.save(information);

        Cache actCache = Redis.use();
        String sound = "default";
        Object o = actCache.get("SOUND:" + groupRemind.getMemberId());
        if (o != null) {
            sound = o.toString();
        }



            String key = "MEMBER:" + groupRemind.getMemberId().toString();
            String appid = RedisUtil.getString(key);
            if (appid != null) {
                logger.info("开始调用极光推送方法——————————————————————; appid=" + appid);
                JPush.sendPushById(appid, "团购消息", "您有", ""+num+"个团购提醒,活动即将开始，快去看看吧！", sound, null);
                logger.info("成功调用极光推送方法——————————————————————");
            }

    }
    /**
     * 待付款消息
     *
     * @param order
     */
    @Before(Tx.class)
    public void noPayMessage(Order order) {
        Cache actCache = Redis.use();
        //添加消息记录表
        Information information = new Information();
        information.setContent("订单(" + order.getSn() + ")待付款");
        information.setIsDelete(false);
        information.setStatus(false);
        information.setMemberId(order.getMemberId());
        information.setTitle("您有一条待付款订单");
        information.setAction(Information.Action.order.ordinal());
        information.setLink(order.getSn().toString());
        information.setType(Information.Type.order.ordinal());
        informationDao.save(information);

        Boolean isMyMessage = actCache.get("ORDERMMESSAGR_SWITCH:" + order.getMemberId());
        String sound = "default";
        Object o = actCache.get("SOUND:" + order.getMemberId());
        if (o != null) {
            sound = o.toString();
        }
        if (isMyMessage != null && isMyMessage) {
            String key = "MEMBER:" + order.getMemberId().toString();
            String appid = RedisUtil.getString(key);
            if (appid != null) {
                JPush.sendPushById(appid, "订单消息", "您有一条待付款订单", "订单(" + order.getSn() + ")待付款", sound, null);
            }

        }

    }

    /**
     * 发货消息
     *
     * @param order
     */

    public void sendPeoductMessage(Order order) {
        Cache actCache = Redis.use();
        //添加消息记录表
        Information information = new Information();
        information.setContent("订单(" + order.getSn() + ")已发货");
        information.setIsDelete(false);
        information.setStatus(false);
        information.setMemberId(order.getMemberId());
        information.setTitle("订单(" + order.getSn() + ")已发货");
        information.setAction(Information.Action.order.ordinal());
        information.setLink(order.getSn().toString());
        information.setType(Information.Type.order.ordinal());
        informationDao.save(information);

        Boolean isMyMessage = actCache.get("ORDERMMESSAGR_SWITCH:" + order.getMemberId());
        String sound = "default";
        Object o = actCache.get("SOUND:" + order.getMemberId());
        if (o != null) {
            sound = o.toString();
        }
        if (isMyMessage != null && isMyMessage) {
            String key = "MEMBER:" + order.getMemberId().toString();
            String appid = RedisUtil.getString(key);
            if (appid != null) {
                JPush.sendPushById(appid, "订单消息", "订单(" + order.getSn() + ")已发货", "订单(" + order.getSn() + ")已发货", sound, null);
                return;
            }

        }
    }
    /**
     * 发货消息
     *
     * @param order
     */

    public void sendPeoductMessage(Identifier order) {
        Cache actCache = Redis.use();
        //添加消息记录表
        Information information = new Information();
        information.setContent("运单号(" + order.getTrackingNo() + ")已发货");
        information.setIsDelete(false);
        information.setStatus(false);
        information.setMemberId(order.getMemberId());
        information.setTitle("运单号(" + order.getTrackingNo() + ")已发货");
        information.setAction(Information.Action.order.ordinal());
        information.setLink(order.toString());
        information.setType(Information.Type.order.ordinal());
        informationDao.save(information);

        Boolean isMyMessage = actCache.get("ORDERMMESSAGR_SWITCH:" + order.getMemberId());
        String sound = "default";
        Object o = actCache.get("SOUND:" + order.getMemberId());
        if (o != null) {
            sound = o.toString();
        }
        if (isMyMessage != null && isMyMessage) {
            String key = "MEMBER:" + order.getMemberId().toString();
            String appid = RedisUtil.getString(key);
            if (appid != null) {
                JPush.sendPushById(appid, "订单消息", "运单号(" + order.getTrackingNo() + ")已发货", "运单号(" + order.getTrackingNo() + ")已发货", sound, null);
                return;
            }

        }
    }
    /**
     * 关注到货消息
     *
     * @param goods
     */

    public void myFavoriteMessage(Goods goods) {
        List<Long> memberList = memberFavoriteGoodsDao.findMemberList(goods.getId());

        Cache actCache = Redis.use();
        for (Long memberId : memberList) {
            //添加消息记录表
            Information information = new Information();
            information.setContent("商品(" + goods.getName() + ")已到货");
            information.setIsDelete(false);
            information.setStatus(false);
            information.setMemberId(memberId);
            information.setTitle("商品(" + goods.getName() + ")已到货");
            information.setAction(Information.Action.goods.ordinal());
            information.setLink(goods.getId().toString());
            information.setType(Information.Type.myFavorite.ordinal());
            informationDao.save(information);

            Boolean isMyMessage = actCache.get("SYSTEMMESSAGR_SWITCH:" + memberId);
            String sound = "default";
            Object o = actCache.get("SOUND:" + memberId);
            if (o != null) {
                sound = o.toString();
            }
            if (isMyMessage != null && isMyMessage) {
                String key = "MEMBER:" + memberId.toString();
                String appid = RedisUtil.getString(key);
                if (appid != null) {
                    JPush.sendPushById(appid, "关注商品到货通知", "您关注的商品(" + goods.getName() + ")已到货", "您关注的商品(" + goods.getName() + ")已到货", sound, null);

                }
            }
        }

    }

    /**
     * 技师推荐消息
     *
     * @param goods
     */

    public void staffMessage(Goods goods, Member staff, Member member) {
        Cache actCache = Redis.use();
        //添加消息记录表
        Information information = new Information();
        information.setContent("技师:" + staff.getNickname() + " 给您推荐最新产品," + goods.getName() + ",请您查看");
        information.setIsDelete(false);
        information.setStatus(false);
        information.setMemberId(member.getId());
        information.setTitle("技师:" + staff.getNickname() + " 给您推荐最新产品," + goods.getName() + ",请您查看");
        information.setAction(Information.Action.goods.ordinal());
        information.setLink(goods.getId().toString());
        information.setType(Information.Type.staff.ordinal());
        information.setStaffId(staff.getId());
        informationDao.save(information);

        Boolean isMyMessage = actCache.get("STAFFMESSAGR_SWITCH:" + member.getId());
        String sound = "default";
        Object o = actCache.get("SOUND:" + member.getId());
        if (o != null) {
            sound = o.toString();
        }
        if (isMyMessage != null && isMyMessage) {
            String key = "MEMBER:" + member.getId().toString();
            String appid = RedisUtil.getString(key);
            if (appid != null) {
                JPush.sendPushById(appid, "技师消息", "技师消息", "技师:" + staff.getNickname() + " 给您推荐最新产品," + goods.getName(), sound, null);
            }

        }
    }


}
