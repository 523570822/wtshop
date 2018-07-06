package com.wtshop.util;

import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConstants;
import com.alipay.api.DefaultAlipayClient;
import com.aliyun.openservices.oss.internal.SignUtils;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.PropKit;
import com.wtshop.CommonAttributes;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Created by sq on 2017/10/30.
 */
public class AliPayUtil {


    public static final String ALIPAY_APPID = PropKit.use(CommonAttributes.wtshop_PROPERTIES_PATH).get("Ali_APPID"); // appid

    public static String getAppPrivateKey(){
        String APP_PRIVATE_KEY = null;
        try{
            Resource resources = new ClassPathResource("private_key.zip");
            final ZipFile zipFile = new ZipFile(resources.getFile()); // 根据路径取得需要解压的Zip文件
            if (zipFile.isEncrypted()) { // 判断文件是否加码
                zipFile.setPassword("rxm123457"); //
            }
            zipFile.extractAll(PathKit.getRootClassPath()); // 压缩包文件解压路径

            Resource resource = new ClassPathResource("rsa_private_key_pkcs8.pem");
            InputStream inputStream = resource.getInputStream();
            APP_PRIVATE_KEY = StringUtils.inputStream2String(inputStream);

            if(inputStream != null) {
                inputStream.close();
            }
            File file = new File(String.valueOf(resource.getFile()));
            if(file.exists()){
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return APP_PRIVATE_KEY;
    }

    public static String getAppPublicKey(){
        String ALIPAY_PUBLIC_KEY = null;
        try{
            Resource resources = new ClassPathResource("public_key.zip");
            final ZipFile zipFile = new ZipFile(resources.getFile()); // 根据路径取得需要解压的Zip文件
            if (zipFile.isEncrypted()) { // 判断文件是否加码
                zipFile.setPassword("rxm123457"); //
            }
            zipFile.extractAll(PathKit.getRootClassPath()); // 压缩包文件解压路径

            Resource resource = new ClassPathResource("rsa_public_key.pem");
            InputStream inputStream = resource.getInputStream();
            ALIPAY_PUBLIC_KEY = StringUtils.inputStream2String(inputStream);

            if(inputStream != null) {
                inputStream.close();
            }
            File file = new File(String.valueOf(resource.getFile()));
            if(file.exists()){
                file.delete();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ALIPAY_PUBLIC_KEY;
    }


    // 统一收单交易创建接口
    private static AlipayClient alipayClient = null;

    public static AlipayClient getAlipayClient() {
        try{
            String APP_PRIVATE_KEY = getAppPrivateKey();
            String ALIPAY_PUBLIC_KEY = getAppPublicKey();

            if (alipayClient == null) {
                synchronized (AliPayUtil.class) {
                    if (null == alipayClient) {
                        alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", ALIPAY_APPID,
                                APP_PRIVATE_KEY, AlipayConstants.FORMAT_JSON, "utf-8",
                                ALIPAY_PUBLIC_KEY,AlipayConstants.SIGN_TYPE_RSA2);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return alipayClient;
    }



    /**
     * 构造支付订单参数信息
     *
     * @param map
     * 支付订单参数
     * @return
     */
    public static String buildOrderParam(Map<String, String> map) {
        List<String> keys = new ArrayList<String>(map.keySet());

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keys.size() - 1; i++) {
            String key = keys.get(i);
            String value = map.get(key);
            sb.append(buildKeyValue(key, value, true));
            sb.append("&");
        }

        String tailKey = keys.get(keys.size() - 1);
        String tailValue = map.get(tailKey);
        sb.append(buildKeyValue(tailKey, tailValue, true));

        return sb.toString();
    }

    /**
     * 拼接键值对
     *
     * @param key
     * @param value
     * @param isEncode
     * @return
     */
    private static String buildKeyValue(String key, String value, boolean isEncode) {
        StringBuilder sb = new StringBuilder();
        sb.append(key);
        sb.append("=");
        if (isEncode) {
            try {
                sb.append(URLEncoder.encode(value, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                sb.append(value);
            }
        } else {
            sb.append(value);
        }
        return sb.toString();
    }

    /**
     * 对支付参数信息进行签名
     *
     * @param map
     *            待签名授权信息
     *
     * @return
     */
    public static String getSign(Map<String, String> map, String rsaKey) {
        List<String> keys = new ArrayList<String>(map.keySet());
        // key排序
        Collections.sort(keys);

        StringBuilder authInfo = new StringBuilder();
        for (int i = 0; i < keys.size() - 1; i++) {
            String key = keys.get(i);
            String value = map.get(key);
            authInfo.append(buildKeyValue(key, value, false));
            authInfo.append("&");
        }

        String tailKey = keys.get(keys.size() - 1);
        String tailValue = map.get(tailKey);
        authInfo.append(buildKeyValue(tailKey, tailValue, false));

        String oriSign = AliSignUtils.sign (authInfo.toString(), rsaKey);
        String encodedSign = "";

        try {
            encodedSign = URLEncoder.encode(oriSign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "sign=" + encodedSign;
    }

    /**
     * 要求外部订单号必须唯一。
     * @return
     */
    private static String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }


}
