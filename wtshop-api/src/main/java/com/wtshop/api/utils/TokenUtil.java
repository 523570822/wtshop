package com.wtshop.api.utils;

import com.jfinal.weixin.sdk.kit.PaymentKit;
import com.jfinal.weixin.sdk.utils.HttpUtils;
import com.wangtiansoft.common.utils.RandomUtil;
import com.wtshop.util.RandomUtils;

import java.util.HashMap;
import java.util.Map;


public class TokenUtil {
    /**
     * 生成token号码
     * @return token号码
     */
    public static String generateToken() {
        return RandomUtils.randomCustomUUID().concat(RandomUtils.randomString(6));
    }

    public static void main(String[] args) {

        Map<String, String> params = new HashMap<>();
        params.put("mch_id","1486268352");
        String random = RandomUtil.getRandom(32);
        params.put("nonce_str", random);
        String sign = PaymentKit.createSign(params, "qCw9W1RQoZhfdALP79bKF7TBVQdlj8JZ");
        System.out.println(random);
        System.out.println(sign);
        params.put("sign", sign);

        String post = HttpUtils.post("https://apitest.mch.weixin.qq.com/sandboxnew/pay/getsignkey", PaymentKit.toXml(params));
        Map<String, String> result = PaymentKit.xmlToMap(post);
        System.out.println(result.get("sandbox_signkey"));
    }

}
