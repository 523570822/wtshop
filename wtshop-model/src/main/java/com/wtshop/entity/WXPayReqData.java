package com.wtshop.entity;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.wtshop.CommonAttributes;
import com.wtshop.util.MD5Utils;
import com.wtshop.util.UUIDUtils;
import org.apache.commons.lang3.RandomStringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sq on 2017/9/11.
 */
public class WXPayReqData {



    private String mch_appid;
    private String appid;
    private String mchid;
    private String mch_id;
    private String device_info;
    private String nonce_str;
    private String sign;
    private String partner_trade_no;
    private String openid;
    private String check_name;
    private String re_user_name;
    private String amount;
    private String desc;
    private String refund_desc;
    private String spbill_create_ip;
    private String transaction_id;
    private String out_refund_no;
    private int total_fee;
    private int refund_fee;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getMch_appid() {
        return mch_appid;
    }

    public void setMch_appid(String mch_appid) {
        this.mch_appid = mch_appid;
    }

    public String getMchid() {
        return mchid;
    }

    public void setMchid(String mchid) {
        this.mchid = mchid;
    }

    public String getDevice_info() {
        return device_info;
    }

    public void setDevice_info(String device_info) {
        this.device_info = device_info;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getPartner_trade_no() {
        return partner_trade_no;
    }

    public void setPartner_trade_no(String partner_trade_no) {
        this.partner_trade_no = partner_trade_no;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getCheck_name() {
        return check_name;
    }

    public void setCheck_name(String check_name) {
        this.check_name = check_name;
    }

    public String getRe_user_name() {
        return re_user_name;
    }

    public void setRe_user_name(String re_user_name) {
        this.re_user_name = re_user_name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getSpbill_create_ip() {
        return spbill_create_ip;
    }

    public void setSpbill_create_ip(String spbill_create_ip) {
        this.spbill_create_ip = spbill_create_ip;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getOut_refund_no() {
        return out_refund_no;
    }

    public void setOut_refund_no(String out_refund_no) {
        this.out_refund_no = out_refund_no;
    }

    public int getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(int total_fee) {
        this.total_fee = total_fee;
    }

    public int getRefund_fee() {
        return refund_fee;
    }

    public void setRefund_fee(int refund_fee) {
        this.refund_fee = refund_fee;
    }

    public String getRefund_desc() {
        return refund_desc;
    }

    public void setRefund_desc(String refund_desc) {
        this.refund_desc = refund_desc;
    }

    /**
     * 生成企业付款请求
     * @param partner_trade_no	订单号
     * @param openid	收款用户openid
     * @param amount	付款金额，(分)
     * @param desc	付款描述
     * @param spbill_create_ip	调用接口的机器Ip地址
     * @return
     */
    public static WXPayReqData buildWXPayReqDataForMchPay(String partner_trade_no,String openid,int amount,String desc,String spbill_create_ip){
        Prop prop = PropKit.use(CommonAttributes.wtshop_PROPERTIES_PATH);
        WXPayReqData reqData = new WXPayReqData();
        reqData.setMch_appid(prop.get("AppID"));
        reqData.setMchid(prop.get("MCH_ID"));
        reqData.setCheck_name("NO_CHECK");
        reqData.setPartner_trade_no(partner_trade_no);
        reqData.setOpenid(openid);
        reqData.setAmount(String.valueOf(amount));
        reqData.setDesc(desc);
        reqData.setSpbill_create_ip(spbill_create_ip);
        char[] RANDOM_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        String nonce_str = RandomStringUtils.random(32,RANDOM_CHARS).toUpperCase();
        reqData.setNonce_str(nonce_str);
        String sign = reqData.signReqData(reqData.toMap());
        reqData.setSign(sign);
        return reqData;
    }


    /**
     * 微信退款
     * @param transaction_id 订单号
     * @param total_fee	订单金额，(分)
     * @param refund_fee 退款金额，(分)
     * @param desc	退款描述
     * @return
     */
    public static WXPayReqData buildWXBackDataForMchPay(String transaction_id, int total_fee, int refund_fee, String desc){
        Prop prop = PropKit.use(CommonAttributes.wtshop_PROPERTIES_PATH);
        WXPayReqData reqData = new WXPayReqData();
        reqData.setAppid(prop.get("AppID"));
        reqData.setMch_id(prop.get("MCH_ID"));
        reqData.setOut_refund_no(UUIDUtils.getLongUUID());
        reqData.setTransaction_id(transaction_id);
        reqData.setTotal_fee(total_fee);
        reqData.setRefund_fee(refund_fee);
        reqData.setRefund_desc(desc);
        char[] RANDOM_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        String nonce_str = RandomStringUtils.random(32,RANDOM_CHARS).toUpperCase();
        reqData.setNonce_str(nonce_str);
        String sign = reqData.signReqData(reqData.toMap());
        reqData.setSign(sign);
        return reqData;
    }

    private Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            Object obj;
            try {
                obj = field.get(this);
                if (obj != null) {
                    map.put(field.getName(), obj);
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    private String signReqData(Map<String, Object> map) {
        Prop prop = PropKit.use(CommonAttributes.wtshop_PROPERTIES_PATH);
        ArrayList<String> list = new ArrayList<String>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getValue() != "") {
                list.add(entry.getKey() + "=" + entry.getValue() + "&");
            }
        }
        int size = list.size();
        String[] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(arrayToSort[i]);
        }
        String result = sb.toString();
        result += "key=" + prop.get("API_KEY");
        System.out.println("result : " + result);
        result = MD5Utils.MD5Encode(result, "utf-8").toUpperCase();
        return result;
    }


}
