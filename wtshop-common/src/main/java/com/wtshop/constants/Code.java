package com.wtshop.constants;

import java.io.Serializable;

public class Code implements Serializable {

    //  是否为开发者模式
    public static Boolean isDevMode = false;

    //  全局配置
    public static final int FALSE = 0;
    public static final int TRUE = 1;

    //技师分佣时间限制 15天后计算分佣金额
    public static final int staff_day = 15;
    /**
     * 成功
     */
    public static final int SUCCESS = 1;

    /**
     * 失败
     */
    public static final int FAIL = 0;

    /**
     * 参数错误: 一般是缺少或参数值不符合要求
     */
    public static final int ARGUMENT_ERROR = 2;

    /**
     * 服务器错误
     */
    public static final int ERROR = 500;

    /**
     * 接口不存在
     */
    public static final int NOT_FOUND = 404;
/**
 * 缺少邀请码
 */
public static final int NOT_SHARECODE = 7;
    /**
     * token无效
     */
    public static final int TOKEN_INVALID = 422;

    /**
     * 帐号已存在*
     */
    public static final int ACCOUNT_EXISTS = 3;
    /**
     * 特殊商品已经下架
     */
    public static final int SPECIAL_ERROR = 3;
    /**
     * 验证码错误
     */
    public static final int CODE_ERROR = 4;
    /**
     * 倒拍存在未支付订单
     */
    public static final int AUCTION_PENDING = 5;


    /**
     * 外部接口
     */
    public static final int API_SUCCESS = 0;                            //	成功
    public static final int API_ERROR_BUSS = 10000;                     //	业务错误
    public static final int API_ERROR_ARGUMENT_NOT_EXIST = 10001;       //	参数不存在
    public static final int API_ERROR_APPID = 20000;                    //	错误的appId
    public static final int API_ERROR_APPKEY = 20001;                   //	错误的appKey
    public static final int API_ERROR_MOBILE = 20002;                   //	错误的手机号码
    public static final int API_ERROR_IP = 30000;                       //	服务IP不在白名单列表中


    public final static String kAuctionSetting = "Auction:Setting";

    /**
     * 系统角色
     */
    public static final String R_ProductSpecialist  = "R_ProductSpecialist"; //  产品专员
    public static final String R_ProductManager     = "R_ProductManager"; //  产品主管
    public static final String R_Finance            = "R_Finance"; //  财务
    public static final String R_FinanceDirector    = "R_FinanceDirector"; //  财务主管

}
