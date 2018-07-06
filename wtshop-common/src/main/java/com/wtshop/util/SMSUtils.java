package com.wtshop.util;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;
import com.wtshop.CommonAttributes;



/**
 * 短信相关的工具类
 * 
 * @author malongbo
 */
public class SMSUtils {

    public static ApiResult send(String phone, String templateId, Map<String, String> params) {
        ApiResult apiResult = ApiResult.success();
        ArrayList phones = new ArrayList();
        phones.add(phone);
        send(phones, templateId, params);
        return apiResult;
    }

    public static Object send(List<String> phones, String templateId, Map<String, String> params) {
        ApiResult apiResult = ApiResult.success();
        Prop prop = PropKit.use(CommonAttributes.wtshop_PROPERTIES_PATH);
        String url = prop.get("sms.url");
        String appkey = prop.get("sms.appkey");
        String secret = prop.get("sms.secret");
        String sign = prop.get("sms.signName");
        DefaultTaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
        AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
        req.setSmsType("normal");
        req.setSmsFreeSignName(sign);
        if(params != null && params.size() > 0) {
            req.setSmsParamString(JsonUtils.toJson(params));
        }

        StringBuilder sb = new StringBuilder();
        Iterator rsp = phones.iterator();

        while(rsp.hasNext()) {
            String status = (String)rsp.next();
            sb.append(status).append(',');
        }

        sb.deleteCharAt(sb.length() - 1);
        req.setRecNum(sb.toString());
        req.setSmsTemplateCode(templateId);

        ApiResult status1;
        try {
            AlibabaAliqinFcSmsNumSendResponse rsp1 = (AlibabaAliqinFcSmsNumSendResponse)client.execute(req);
            if(rsp1.getErrorCode() == null) {
                status1 = new ApiResult();
            } else {
                status1 = new ApiResult(0, rsp1.getSubMsg());
            }
        } catch (ApiException var13) {
            status1 = new ApiResult(0, var13.getErrMsg());
        }
        return apiResult;
    }
    /**
     * 检测手机号有效性*
     * @param mobile 手机号码
     * @return 是否有效
     */
    public static boolean isMobileNo(String mobile){
        if (mobile.length() != 11)
        {
            return false;
        }else{
            /**
             * 正则表达式
             */
            String pat1 = "^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$";

            Pattern pattern1 = Pattern.compile(pat1);
            Matcher match1 = pattern1.matcher(mobile);
            boolean isMatch1 = match1.matches();
            if(isMatch1){
                return true;
            }
            return false;
        }
}
    
    /**
     * 生成短信验证码*
     * @param length 长度
     * @return 指定长度的随机短信验证码
     */
    public static String randomSMSCode(int length) {
        boolean numberFlag = true;
        String retStr = "";
        String strTable = numberFlag ? "1234567890" : "1234567890abcdefghijkmnpqrstuvwxyz";
        int len = strTable.length();
        boolean bDone = true;
        do {
            retStr = "";
            int count = 0;
            for (int i = 0; i < length; i++) {
                double dblR = Math.random() * len;
                int intR = (int) Math.floor(dblR);
                char c = strTable.charAt(intR);
                if (('0' <= c) && (c <= '9')) {
                    count++;
                }
                retStr += strTable.charAt(intR);
            }
            if (count >= 2) {
                bDone = false;
            }
        } while (bDone);
        return retStr;
    }

}
