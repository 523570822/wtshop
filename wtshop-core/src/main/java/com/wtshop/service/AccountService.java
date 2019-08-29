package com.wtshop.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayFundTransOrderQueryModel;
import com.alipay.api.domain.AlipayFundTransToaccountTransferModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayFundTransOrderQueryRequest;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.request.AlipayUserUserinfoShareRequest;
import com.alipay.api.response.*;
import com.jfinal.aop.Enhancer;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.log.Logger;
import com.jfinal.weixin.sdk.utils.HttpUtils;
import com.jfinal.wxaapp.WxaConfig;
import com.jfinal.wxaapp.WxaConfigKit;
import com.jfinal.wxaapp.api.WxaAccessTokenApi;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.wtshop.CommonAttributes;
import com.wtshop.dao.AccountDao;
import com.wtshop.entity.WXPayReqData;
import com.wtshop.entity.WXPaymentReqData;
import com.wtshop.entity.WxaTemplate;
import com.wtshop.exception.AppRuntimeException;
import com.wtshop.model.Account;
import com.wtshop.util.AliPayUtil;
import com.wtshop.util.UUIDUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.StringReader;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.util.*;

import static com.jfinal.ext.kit.AliPayApi.transferToResponse;

/**
 * Created by sq on 2017/9/6.
 */
public class AccountService extends BaseService<Account> {

    Logger logger = Logger.getLogger(AccountService.class);

     public AccountService(){
         super(Account.class);
     }

     private AccountDao accountDao = Enhancer.enhance(AccountDao.class);

    /**
     * 根据用户id 获取用户账号绑定信息
     */
    public Account getUserInfo(Long memberId ,Integer type){
        return accountDao.getUserInfo(memberId, type);
    }

    /**
     * 根据用户id 获取用户账号绑定信息
     */
    public List<Account> getUserInfoList(Long memberId){
        return accountDao.getUserInfoList(memberId);
    }

    /**
     * 根据openId 获取用户信息
     */
    public Account findByAccount(String openId ,String unionid ,Integer type){
        return accountDao.findByAccount(openId,unionid, type);
    }
    private static String sendApiUrl = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token=";


    /**
     * 微信登录 获取token
     */
    public Map<String, Object> getAccess_token(String code){
        Prop prop = PropKit.use(CommonAttributes.wtshop_PROPERTIES_PATH);
        System.out.println("AppID 打印"+prop.get("AppID"));
        System.out.println("secret：====="+prop.get("SECRET"));

        StringBuilder requestUrl = new StringBuilder("https://api.weixin.qq.com/sns/oauth2/access_token?appid=")
                .append(prop.get("AppID")).append("&secret=").append(prop.get("SECRET")).append("&code=")
                .append(code).append("&grant_type=authorization_code");
        try {
            String results = HttpUtils.get(requestUrl.toString());

            Map<String, Object> resultMap= JSON.parseObject(results, HashMap.class);
            if(resultMap!=null){
                return resultMap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
/**
 * 快递公司列表
 */
public Map<String,Object> getall(Map<String,Object> access_token){

    Prop prop = PropKit.use(CommonAttributes.wtshop_PROPERTIES_PATH);

    StringBuilder requestUrl = new StringBuilder("https://api.weixin.qq.com/cgi-bin/express/business/delivery/getall?access_token=")
            .append(access_token.get("access_token").toString());
    try {
        String res = HttpUtils.get(requestUrl.toString());
       return JSON.parseObject(res, HashMap.class);
    } catch (Exception e) {

        e.printStackTrace();
    }
    return null;
}
    /**
     * 小程序 获取token
     */
    public Map<String, Object> getAccessXCX_token( ){
        Prop prop = PropKit.use(CommonAttributes.wtshop_PROPERTIES_PATH);
        System.out.println("AppID 打印"+prop.get("AppID"));
        System.out.println("secret：====="+prop.get("SECRET"));

        StringBuilder requestUrl = new StringBuilder("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=")
                .append(prop.get("XCX_APPID")).append("&secret=").append(prop.get("XCX_SECRET"));
        try {
            String results = HttpUtils.get(requestUrl.toString());

            Map<String, Object> resultMap= JSON.parseObject(results, HashMap.class);
            if(resultMap!=null){
                return resultMap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 小程序登录
     */
    public Map<String, Object> getXCXAccess_token(String code){
        Prop prop = PropKit.use(CommonAttributes.wtshop_PROPERTIES_PATH);


        StringBuilder requestUrl = new StringBuilder("https://api.weixin.qq.com/sns/jscode2session?appid=")
                .append(prop.get("XCX_APPID")).append("&secret=").append(prop.get("XCX_SECRET")).append("&js_code=")
                .append(code).append("&grant_type=authorization_code");
        try {
           String results = HttpUtils.get(requestUrl.toString());

           Map<String, Object> resultMap= JSON.parseObject(results, HashMap.class);
            if(resultMap!=null){
                return resultMap;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
    /**
     * 小程序消息推送
     */
    public Map<String, Object> getXCXSend(WxaTemplate template){
        Object token = WxaAccessTokenApi.getAccessTokenStr()+"1";
        String  build = template.build().toString();
        String jsonResult = HttpUtils.post(sendApiUrl + token,build);
        HashMap resultMap = JSON.parseObject(jsonResult, HashMap.class);
        Object dfsfds = resultMap.get("errcode");
       if("40001".equals(resultMap.get("errcode").toString())){
           WxaConfig wc = WxaConfigKit.getWxaConfig();
           Object   token1= WxaAccessTokenApi.refreshAccessToken();
            jsonResult = HttpUtils.post(sendApiUrl + token1,build);
            resultMap = JSON.parseObject(jsonResult, HashMap.class);
        }




       return resultMap;
       // return null;

    }
    /**
     * 刷新用户授权的token
     * @param access_token
     * @return
     */
    public Map<String,Object> refresh_token(Map<String,Object> access_token){

        Prop prop = PropKit.use(CommonAttributes.wtshop_PROPERTIES_PATH);

        StringBuilder requestUrl = new StringBuilder("https://api.weixin.qq.com/sns/auth?access_token=")
                .append(access_token.get("access_token").toString()).append("&openid=").append(access_token.get("access_token").toString());
        try {
            String res = HttpUtils.get(requestUrl.toString());
            Map<String, Object> resultMap=JSON.parseObject(res, HashMap.class);
            if(resultMap!=null){
                String errmsg=resultMap.get("errmsg").toString();
                if(!"ok".equals(errmsg)){

                    StringBuilder refresh_token_Url = new StringBuilder("https://api.weixin.qq.com/sns/oauth2/refresh_token?appid==")
                            .append(prop.get("AppID")).append("&grant_type=refresh_token&refresh_token=").append(access_token.get("refresh_token").toString());
                    resultMap=JSON.parseObject(res, HashMap.class);
                    if(resultMap!=null){
                        return resultMap;
                    }
                }else{
                    return resultMap;
                }
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
        return null;
    }
    /**
     * 获取用户基本信息
     * @param access_token 通过code获取的token
     * @return
     */
    public Map<String,Object> getUserInfoXCX(Map<String,Object> access_token){
        StringBuilder requestUrl = new StringBuilder("https://api.weixin.qq.com/wxa/getpaidunionid?access_token=").append(access_token.get("session_key").toString()).append("&openid=").append(access_token.get("openid").toString());
        try {
            //String res = HttpService.doGet(requestUrl.toString());
            String results = HttpUtils.get(requestUrl.toString());
            Map<String, Object> resultMap=JSON.parseObject(results, HashMap.class);
            if(resultMap!=null){
                return resultMap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取用户基本信息
     * @param access_token 通过code获取的token
     * @return
     */
    public Map<String,Object> getUserInfo(Map<String,Object> access_token){
        StringBuilder requestUrl = new StringBuilder("https://api.weixin.qq.com/sns/userinfo?access_token=").append(access_token.get("access_token").toString()).append("&openid=").append(access_token.get("openid").toString()).append("&lang=zh_CN");
        try {
            //String res = HttpService.doGet(requestUrl.toString());
            String results = HttpUtils.get(requestUrl.toString());
            Map<String, Object> resultMap=JSON.parseObject(results, HashMap.class);
            if(resultMap!=null){
                return resultMap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取用户基本信息
     * @param access_token 通过openid token
     * @return
     */
    public Map<String,Object> getUser(String access_token, String openid){
        StringBuilder requestUrl = new StringBuilder("https://api.weixin.qq.com/sns/userinfo?access_token=")
                .append(access_token).append("&openid=").append(openid).append("&lang=zh_CN");
        try {
            //String res = HttpService.doGet(requestUrl.toString());
            String results = HttpUtils.get(requestUrl.toString());
            Map<String, Object> resultMap=JSON.parseObject(results, HashMap.class);
            if(resultMap!=null){
                return resultMap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    /**
     * 微博登录 获取token
     */
    public Map<String, Object> getSina_Access_token(String code){
        Prop prop = PropKit.use(CommonAttributes.wtshop_PROPERTIES_PATH);

        JSONObject data = new JSONObject();
        data.put("client_id",prop.get("Sina_API_KEY"));
        data.put("client_secret",prop.get("Sina_SECRET"));
        data.put("code",prop.get("code"));
        data.put("redirect_uri",prop.get("Sina_Notify_url"));
        data.put("grant_type",prop.get("authorization_code"));

        try {
            String results = HttpUtils.post("https://api.weibo.com/oauth2/access_token",data.toString());

            Map<String, Object> resultMap= JSON.parseObject(results, HashMap.class);
            if(resultMap!=null){
                return resultMap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 微博登录 获取用户基本信息
     */
    public Map<String, Object> getSina_UserInfo(String access_token, String uid){


        StringBuilder requestUrl = new StringBuilder("https://api.weibo.com/2/users/show.json?access_token=")
                .append(access_token.toString()).append("&uid=").append(uid.toString());

        try {

            String results = HttpUtils.get(requestUrl.toString());
            Map<String, Object> resultMap=JSON.parseObject(results, HashMap.class);
            if(resultMap!=null){
                return resultMap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * QQ登录 获取token
     */
    public Map<String, Object> getQQ_Access_token(String code){
        Prop prop = PropKit.use(CommonAttributes.wtshop_PROPERTIES_PATH);

        StringBuilder requestUrl = new StringBuilder("https://graph.qq.com/oauth2.0/token?client_id=")
                .append(prop.get("QQ_APPID")).append("&client_secret=").append(prop.get("QQ_APP_KEY")).append("&code=")
                .append(code).append("&redirect_uri=").append(prop.get("QQ_Notify_url")).append("&grant_type=authorization_code");

        try {
            String results = HttpUtils.get(requestUrl.toString());

            Map<String, Object> resultMap= JSON.parseObject(results, HashMap.class);
            if(resultMap!=null){
                return resultMap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * QQ登录 获取用户OpenId
     */
    public Map<String, Object> getQQ_OpenId(Map<String, Object> access_token){

        StringBuilder requestUrl = new StringBuilder("https://graph.qq.com/oauth2.0/me?access_token=")
                .append(access_token.get("access_token").toString());
        try {
            String results = HttpUtils.get(requestUrl.toString());

            Map<String, Object> resultMap= JSON.parseObject(results, HashMap.class);
            if(resultMap!=null){
                return resultMap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * QQ登录 获取用户基本信息
     */
    public Map<String, Object> getQQ_UserInfo(String access_token,Map<String, Object> openId){
        Prop prop = PropKit.use(CommonAttributes.wtshop_PROPERTIES_PATH);
        StringBuilder requestUrl = new StringBuilder("https://graph.qq.com/user/get_user_info?access_token=")
                .append(access_token).append("&oauth_consumer_key=").append(prop.get("QQ_API_KEY")).append("&openid=").append(openId.get("openid"));
        try {
            String results = HttpUtils.get(requestUrl.toString());

            Map<String, Object> resultMap= JSON.parseObject(results, HashMap.class);
            if(resultMap!=null){
                return resultMap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * QQ登录 获取用户基本信息
     */
    public Map<String, Object> getqq_UserInfo(String access_token, String openId, String appId){
        Prop prop = PropKit.use(CommonAttributes.wtshop_PROPERTIES_PATH);
        StringBuilder requestUrl = new StringBuilder("https://graph.qq.com/user/get_user_info?access_token=")
                .append(access_token).append("&oauth_consumer_key=").append(appId).append("&openid=").append(openId);
        try {
            String results = HttpUtils.get(requestUrl.toString());

            Map<String, Object> resultMap= JSON.parseObject(results, HashMap.class);
            if(resultMap!=null){
                return resultMap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 支付宝 获取code
     */
    public String getAliMes(){
        Prop prop = PropKit.use(CommonAttributes.wtshop_PROPERTIES_PATH);
        StringBuilder requestUrl = new StringBuilder("https://openauth.alipay.com/oauth2/publicAppAuthorize.htm?app_id=")
                .append(prop.get("Ali_APPID")).append("&scope=").append("SCOPE").append("&redirect_uri=").append(prop.get("Ali_Notify_url"));
        try {
            String results = HttpUtils.get(requestUrl.toString());
            return results;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 支付宝 获取code
     */
    public String getAliMessage(){
        Prop prop = PropKit.use(CommonAttributes.wtshop_PROPERTIES_PATH);
        try{
            SortedMap<String,String > map = new TreeMap<>();
            map.put("apiname","com.alipay.account.auth");
            map.put("method","alipay.open.auth.sdk.code.get");
            map.put("app_id",prop.get("Ali_APPID")+"");
            map.put("app_name","mc");
            map.put("biz_type","openservice");
            map.put("pid","2088221943292400");
            map.put("product_id","APP_FAST_LOGIN");
            map.put("scope","kuaijie");
            map.put("target_id","20141225xxxx");
            map.put("auth_type","AUTHACCOUNT");
            map.put("sign_type","RSA2");
            String signStr = AlipaySignature.getSignContent(map);
            String select = AlipaySignature.rsa256Sign(signStr, AliPayUtil.getAppPrivateKey(), "utf-8");
            return (signStr + URLEncoder.encode(select,"utf-8"));
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 支付宝登录 获取user_id
     */
    public Map<String, String>  getAliPay_accessToken(String auth_code){
        Map<String, String> user = new HashMap<>();
        AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
        AlipayClient alipayClient = AliPayUtil.getAlipayClient();
        request.setCode(auth_code);
        request.setGrantType("authorization_code");
        try {
            AlipaySystemOauthTokenResponse oauthTokenResponse = alipayClient.execute(request);
            if(oauthTokenResponse.isSuccess()){
                user.put("accessToken", oauthTokenResponse.getAccessToken());
                user.put("uid", oauthTokenResponse.getUserId());
                return user;
            } else {
                return null;
            }
        } catch (AlipayApiException e) {
            //处理异常
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 支付宝 获取用户信息
     */
    public String getAliPau_userInfo(String accessToken){
        AlipayUserUserinfoShareRequest request = new AlipayUserUserinfoShareRequest();

        AlipayClient alipayClient = AliPayUtil.getAlipayClient();
        try {
            AlipayUserUserinfoShareResponse response = alipayClient.execute(request, accessToken);
            if(response.isSuccess()){
                return response.getNickName();
            } else {
            }
        } catch (AlipayApiException e) {
            //处理异常
            e.printStackTrace();
        }
        return null;

    }



    /**
     * 单笔转账到支付宝账户
     * https://doc.open.alipay.com/docs/doc.htm?spm=a219a.7629140.0.0.54Ty29&treeId=193&articleId=106236&docType=1
     * @return
     * @throws AlipayApiException
     */
    public static String transfer(AlipayFundTransToaccountTransferModel model) throws AlipayApiException {
        AlipayFundTransToaccountTransferResponse response = transferToResponse(model);
        String result = response.getBody();
        if (response.isSuccess()) {
            return result;
        } else {
            //调用查询接口查询数据
            JSONObject jsonObject = JSONObject.parseObject(result);
            String out_biz_no = jsonObject.getJSONObject("alipay_fund_trans_toaccount_transfer_response").getString("out_biz_no");
            AlipayFundTransOrderQueryModel queryModel = new AlipayFundTransOrderQueryModel();
            model.setOutBizNo(out_biz_no);
            String query = transferQuery(queryModel);
            if (StringUtils.isNotEmpty(query)) {
                return query;
            }
        }
        return null;
    }

    /**
     * 转账查询接口
     * @param model
     * @return {AlipayFundTransOrderQueryResponse}
     * @throws {AlipayApiException}
     */
    public static AlipayFundTransOrderQueryResponse transferQueryToResponse(AlipayFundTransOrderQueryModel model) throws AlipayApiException{
        AlipayFundTransOrderQueryRequest request = new AlipayFundTransOrderQueryRequest();
        request.setBizModel(model);
        return AliPayUtil.getAlipayClient().execute(request);
    }

    /**
     * 转账查询接口
     * @param model
     * @return {boolean}
     * @throws {AlipayApiException}
     */
    public static String transferQuery(AlipayFundTransOrderQueryModel model) throws AlipayApiException{
        AlipayFundTransOrderQueryResponse response = transferQueryToResponse(model);
        if(response.isSuccess()){
            return response.getBody();
        }
        return null;
    }

    /**
     * 支付宝提现
     */
    public JSONObject CashToAliPay(String openid, Double amount,String uu){
        Logger cashToAliPay = Logger.getLogger("CashToAliPay");
        JSONObject json = null;
        AlipayFundTransToaccountTransferModel model = new AlipayFundTransToaccountTransferModel();
        model.setOutBizNo(uu+UUIDUtils.getLongUUID());//生成订单号
        model.setPayeeType("ALIPAY_USERID");//固定值
        model.setPayeeAccount(openid);//转账收款账户
        model.setAmount(String.format("%.2f", amount));
        model.setRemark("任性猫支付宝提现");

        try {
            String transfer = transfer(model);
            JSONObject jsonObject = JSONObject.parseObject(transfer);
            String info = jsonObject.get("alipay_fund_trans_toaccount_transfer_response").toString();
            json= JSONObject.parseObject(info);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    /**
     * 支付宝退款
     */
    public double tradeRefund(String orderNo, Double price, Long userId) {
        AlipayTradeRefundModel model = new AlipayTradeRefundModel();
        model.setTradeNo(orderNo);
        model.setOutRequestNo(userId + UUIDUtils.getLongUUID());
        model.setRefundAmount(String.format("%.2f", price));
        try{
            AlipayTradeRefundResponse response = tradeRefundToResponse(model);
            JSONObject jsonObject = JSONObject.parseObject(response.getBody());
            String info = jsonObject.get("alipay_trade_refund_response").toString();
            jsonObject= JSONObject.parseObject(info);
            if( jsonObject != null) {
                if("40004".equals(jsonObject.get("code"))){
                    return price;
                }
                if (! "10000".equals(jsonObject.get("code"))) {
                    throw new AppRuntimeException("支付宝退款异常");
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
            throw new AppRuntimeException("支付宝退款异常");
        }
        return 0d;

    }

    public AlipayTradeRefundResponse tradeRefundToResponse(AlipayTradeRefundModel model) throws AlipayApiException{
        AlipayClient alipayClient = AliPayUtil.getAlipayClient();
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        request.setBizModel(model);
        return alipayClient.execute(request);
    }

    /**
     * 微信退款
     * @param transaction_id 订单号
     * @param total_fee	订单金额，(分)
     * @param refund_fee 退款金额，(分)
     * @param desc	退款描述
     * @return
     * @throws Exception
     */
    public  Map<String, String> BackToWeChat(String transaction_id, int total_fee, int refund_fee, String desc)throws Exception {

        Map<String, String> returnResult = null;
        KeyStore keyStore  = KeyStore.getInstance("PKCS12");

        FileInputStream instream = new FileInputStream(new File(AccountService.class.getClassLoader().getResource("1486268352.p12").getPath()));

        try {
            keyStore.load(instream, "1486268352".toCharArray());

        } finally {
            instream.close();

        }
        // Trust own CA and all self-signed certs
        SSLContext sslcontext = SSLContexts.custom()
            .loadKeyMaterial(keyStore, "1486268352".toCharArray())
            .build();

        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
            sslcontext,
            new String[] { "TLSv1" },
            null,
            SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);

        CloseableHttpClient httpClient = HttpClients.custom()
            .setSSLSocketFactory(sslsf)
            .build();

        WXPayReqData reqData = WXPayReqData.buildWXBackDataForMchPay( transaction_id, total_fee, refund_fee,desc);
        XStream xStreamForRequestPostData = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
        String postDataXML = xStreamForRequestPostData.toXML(reqData);
        try {
            HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/secapi/pay/refund");
            StringEntity postEntity = new StringEntity(postDataXML, "UTF-8");
            httpPost.addHeader("Content-Type", "text/xml");
            httpPost.setEntity(postEntity);
            try {
                CloseableHttpResponse response = httpClient.execute(httpPost);
                HttpEntity entity = response.getEntity();
                String result = EntityUtils.toString(entity, "UTF-8");
                returnResult = xmlToMap(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return returnResult;
        } finally {
            httpClient.close();
        }
    }



    /**
     * 微信提现
     * @param partner_trade_no
     * @param openid
     * @param amount
     * @param desc
     * @param spbill_create_ip
     * @return
     * @throws Exception
     */
    public  Map<String, String> CashToWeChat(String partner_trade_no,String openid,int amount,String desc,String spbill_create_ip )throws Exception {

        Map<String, String> returnResult = null;
        KeyStore keyStore  = KeyStore.getInstance("PKCS12");

        FileInputStream instream = new FileInputStream(new File(AccountService.class.getClassLoader().getResource("1486268352.p12").getPath()));

        try {
            keyStore.load(instream, "1486268352".toCharArray());

        } finally {
            instream.close();

        }
        // Trust own CA and all self-signed certs
        SSLContext sslcontext = SSLContexts.custom()
                .loadKeyMaterial(keyStore, "1486268352".toCharArray())
                .build();

        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                new String[] { "TLSv1" },
                null,
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);

        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .build();

        WXPaymentReqData reqData = WXPaymentReqData.buildWXPayReqDataForMchPay(partner_trade_no, openid, amount, desc, spbill_create_ip);
        XStream xStreamForRequestPostData = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
        String postDataXML = xStreamForRequestPostData.toXML(reqData);
        try {
            HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers");
            StringEntity postEntity = new StringEntity(postDataXML, "UTF-8");
            httpPost.addHeader("Content-Type", "text/xml");
            httpPost.setEntity(postEntity);
            try {
                CloseableHttpResponse response = httpClient.execute(httpPost);
                HttpEntity entity = response.getEntity();
                String result = EntityUtils.toString(entity, "UTF-8");
                returnResult = xmlToMap(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return returnResult;
        } finally {
            httpClient.close();
        }
    }

    public static Map<String,String> xmlToMap(String returnResult) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        SAXReader reader = new SAXReader();
        Document doc =  reader.read(new StringReader(returnResult));
        Element root = doc.getRootElement();
        List<Element> list = root.elements();
        for(Element e : list){
            map.put(e.getName(), e.getText());
        }
        return map;
    }


    public Account findByUnionid(String unionid, int i) {

            return accountDao.findByUnionid(unionid, i);
    }

    public Account findByOpenid(String openid) {
        return accountDao.findByOpenid(openid);
    }
    public Account findByMemberId(String memberId,String type) {
        return accountDao.findByMemberId(memberId,type);
    }

}
