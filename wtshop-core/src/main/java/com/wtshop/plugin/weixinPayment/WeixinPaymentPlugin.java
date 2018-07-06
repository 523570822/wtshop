package com.wtshop.plugin.weixinPayment;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.jfinal.kit.StrKit;
import com.jfinal.weixin.sdk.api.PaymentApi.TradeType;
import com.wtshop.Setting;
import com.wtshop.model.PaymentLog;
import com.wtshop.model.PluginConfig;
import com.wtshop.plugin.PaymentPlugin;
import com.wtshop.util.IpUtil;
import com.wtshop.util.RandomUtils;
import com.wtshop.util.SystemUtils;
import com.xiaoleilu.hutool.util.MathUtil;

/**
 * Plugin - 微信支付
 * 
 * 
 */
public class WeixinPaymentPlugin extends PaymentPlugin {

	@Override
	public String getName() {
		return "微信支付";
	}

	@Override
	public String getVersion() {
		return "1.0";
	}

	@Override
	public String getAuthor() {
		return "Wangtiansoft";
	}

	@Override
	public String getSiteUrl() {
		return "http://www.wangtiansoft.com";
	}

	@Override
	public String getInstallUrl() {
		return "weixin_payment/install.jhtml";
	}

	@Override
	public String getUninstallUrl() {
		return "weixin_payment/uninstall.jhtml";
	}

	@Override
	public String getSettingUrl() {
		return "weixin_payment/setting.jhtml";
	}

	@Override
	public String getRequestUrl() {
		return "https://api.mch.weixin.qq.com/pay/unifiedorder";
	}

	@Override
	public RequestMethod getRequestMethod() {
		return RequestMethod.get;
	}

	@Override
	public String getRequestCharset() {
		return "UTF-8";
	}

	@Override
	public Map<String, Object> getParameterMap(String sn, String description, HttpServletRequest request) {
		Setting setting = SystemUtils.getSetting();
		PluginConfig pluginConfig = getPluginConfig();
		PaymentLog paymentLog = getPaymentLog(sn);
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("appid", pluginConfig.getAttribute("appid")); // 公众账号ID
		parameterMap.put("mch_id", pluginConfig.getAttribute("mch_id")); // 商户号
		parameterMap.put("nonce_str", RandomUtils.randomUpperWords(32)); // 随机字符串
		parameterMap.put("body", StringUtils.abbreviate(description.replaceAll("[^0-9a-zA-Z\\u4e00-\\u9fa5 ]", ""), 600)); // 商品描述
		parameterMap.put("out_trade_no", sn); // 商户订单号
		parameterMap.put("total_fee", MathUtil.roundStr(paymentLog.getAmount().doubleValue() * 100, 0));// 订单总金额
		String ip = IpUtil.getIpAddr(request);
		if (StrKit.isBlank(ip) || ip.equals("0:0:0:0:0:0:0:1")) {
			ip = "127.0.0.1";
		}
		parameterMap.put("spbill_create_ip", ip); // 终端IP
		parameterMap.put("notify_url", setting.getSiteUrl() + "/wap/payment/paymentNotify.jhtml"); // 通知地址
		parameterMap.put("trade_type", TradeType.JSAPI.name()); // 交易类型
		parameterMap.put("attach", getId()); // 插件id
		return parameterMap;
	}

	@Override
	public boolean verifyNotify(NotifyMethod notifyMethod, HttpServletRequest request) {
		return false;
	}

	@Override
	public String getSn(HttpServletRequest request) {
		return null;
	}

	@Override
	public String getNotifyMessage(NotifyMethod notifyMethod, HttpServletRequest request) {
		return null;
	}

}
