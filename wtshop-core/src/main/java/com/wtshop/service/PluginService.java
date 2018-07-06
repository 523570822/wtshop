package com.wtshop.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wtshop.plugin.wechatAppLogin.WechatAppLoginPlugin;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import com.wtshop.plugin.LoginPlugin;
import com.wtshop.plugin.PaymentPlugin;
import com.wtshop.plugin.StoragePlugin;
import com.wtshop.plugin.alipayBankPayment.AlipayBankPaymentPlugin;
import com.wtshop.plugin.alipayDirectPayment.AlipayDirectPaymentPlugin;
import com.wtshop.plugin.alipayDualPayment.AlipayDualPaymentPlugin;
import com.wtshop.plugin.alipayEscowPayment.AlipayEscowPaymentPlugin;
import com.wtshop.plugin.alipayLogin.AlipayLoginPlugin;
import com.wtshop.plugin.ccbPayment.CcbPaymentPlugin;
import com.wtshop.plugin.ftpStorage.FtpStoragePlugin;
import com.wtshop.plugin.localStorage.LocalStoragePlugin;
import com.wtshop.plugin.ossStorage.OssStoragePlugin;
import com.wtshop.plugin.pay99billBankPayment.Pay99billBankPaymentPlugin;
import com.wtshop.plugin.pay99billPayment.Pay99billPaymentPlugin;
import com.wtshop.plugin.paypalPayment.PaypalPaymentPlugin;
import com.wtshop.plugin.qqLogin.QqLoginPlugin;
import com.wtshop.plugin.tenpayBankPayment.TenpayBankPaymentPlugin;
import com.wtshop.plugin.tenpayDirectPayment.TenpayDirectPaymentPlugin;
import com.wtshop.plugin.tenpayEscowPayment.TenpayEscowPaymentPlugin;
import com.wtshop.plugin.unionpayPayment.UnionpayPaymentPlugin;
import com.wtshop.plugin.weiboLogin.WeiboLoginPlugin;
import com.wtshop.plugin.weixinPayment.WeixinPaymentPlugin;
import com.wtshop.plugin.yeepayPayment.YeepayPaymentPlugin;

/**
 * Service - 插件
 * 
 * 
 */
public class PluginService {

	private static final List<PaymentPlugin>		paymentPlugins		= new ArrayList<PaymentPlugin>();
	private static final List<StoragePlugin>		storagePlugins		= new ArrayList<StoragePlugin>();
	private static final List<LoginPlugin>			loginPlugins		= new ArrayList<LoginPlugin>();
	
	private static final Map<String, PaymentPlugin>	paymentPluginMap	= new HashMap<String, PaymentPlugin>();
	private static final Map<String, StoragePlugin>	storagePluginMap	= new HashMap<String, StoragePlugin>();
	private static final Map<String, LoginPlugin>	loginPluginMap		= new HashMap<String, LoginPlugin>();

	static {
		AlipayBankPaymentPlugin		alipayBankPaymentPlugin		= new AlipayBankPaymentPlugin();
		AlipayDirectPaymentPlugin	alipayDirectPaymentPlugin	= new AlipayDirectPaymentPlugin();
		AlipayDualPaymentPlugin		alipayDualPaymentPlugin		= new AlipayDualPaymentPlugin();
		AlipayEscowPaymentPlugin	alipayEscowPaymentPlugin	= new AlipayEscowPaymentPlugin();
		CcbPaymentPlugin			ccbPaymentPlugin			= new CcbPaymentPlugin();
		Pay99billBankPaymentPlugin	pay99billBankPaymentPlugin	= new Pay99billBankPaymentPlugin();
		Pay99billPaymentPlugin		pay99billPaymentPlugin		= new Pay99billPaymentPlugin();
		PaypalPaymentPlugin			paypalPaymentPlugin			= new PaypalPaymentPlugin();
		TenpayBankPaymentPlugin		tenpayBankPaymentPlugin		= new TenpayBankPaymentPlugin();
		TenpayDirectPaymentPlugin	tenpayDirectPaymentPlugin	= new TenpayDirectPaymentPlugin();
		TenpayEscowPaymentPlugin	tenpayEscowPaymentPlugin	= new TenpayEscowPaymentPlugin();
		UnionpayPaymentPlugin		unionpayPaymentPlugin		= new UnionpayPaymentPlugin();
		YeepayPaymentPlugin			yeepayPaymentPlugin			= new YeepayPaymentPlugin();
		WeixinPaymentPlugin			weixinPaymentPlugin			= new WeixinPaymentPlugin();
		
		QqLoginPlugin				qqLoginPlugin				= new QqLoginPlugin();
		WeiboLoginPlugin			weiboLoginPlugin			= new WeiboLoginPlugin();
		AlipayLoginPlugin			alipayLoginPlugin			= new AlipayLoginPlugin();
		WechatAppLoginPlugin 		wechatAppLoginPlugin		= new WechatAppLoginPlugin();

		LocalStoragePlugin			filePlugin					= new LocalStoragePlugin();
		FtpStoragePlugin			ftpPlugin					= new FtpStoragePlugin();
		OssStoragePlugin			ossPlugin					= new OssStoragePlugin();

		paymentPlugins.add(alipayBankPaymentPlugin);
		paymentPlugins.add(alipayDirectPaymentPlugin);
		paymentPlugins.add(alipayDualPaymentPlugin);
		paymentPlugins.add(alipayEscowPaymentPlugin);
		paymentPlugins.add(ccbPaymentPlugin);
		paymentPlugins.add(pay99billBankPaymentPlugin);
		paymentPlugins.add(pay99billPaymentPlugin);
		paymentPlugins.add(paypalPaymentPlugin);
		paymentPlugins.add(tenpayBankPaymentPlugin);
		paymentPlugins.add(tenpayDirectPaymentPlugin);
		paymentPlugins.add(tenpayEscowPaymentPlugin);
		paymentPlugins.add(unionpayPaymentPlugin);
		paymentPlugins.add(yeepayPaymentPlugin);
		paymentPlugins.add(weixinPaymentPlugin);
		
		paymentPluginMap.put(alipayBankPaymentPlugin.getId(), alipayBankPaymentPlugin);
		paymentPluginMap.put(alipayDirectPaymentPlugin.getId(), alipayDirectPaymentPlugin);
		paymentPluginMap.put(alipayDualPaymentPlugin.getId(), alipayDualPaymentPlugin);
		paymentPluginMap.put(alipayEscowPaymentPlugin.getId(), alipayEscowPaymentPlugin);
		paymentPluginMap.put(ccbPaymentPlugin.getId(), ccbPaymentPlugin);
		paymentPluginMap.put(pay99billBankPaymentPlugin.getId(), pay99billBankPaymentPlugin);
		paymentPluginMap.put(pay99billPaymentPlugin.getId(),pay99billPaymentPlugin);
		paymentPluginMap.put(paypalPaymentPlugin.getId(), paypalPaymentPlugin);
		paymentPluginMap.put(tenpayBankPaymentPlugin.getId(), tenpayBankPaymentPlugin);
		paymentPluginMap.put(tenpayDirectPaymentPlugin.getId(), tenpayDirectPaymentPlugin);
		paymentPluginMap.put(tenpayEscowPaymentPlugin.getId(), tenpayEscowPaymentPlugin);
		paymentPluginMap.put(unionpayPaymentPlugin.getId(), unionpayPaymentPlugin);
		paymentPluginMap.put(yeepayPaymentPlugin.getId(), yeepayPaymentPlugin);
		paymentPluginMap.put(weixinPaymentPlugin.getId(), weixinPaymentPlugin);
		
		loginPlugins.add(weiboLoginPlugin);
		loginPlugins.add(qqLoginPlugin);
		loginPlugins.add(alipayLoginPlugin);
		loginPlugins.add(wechatAppLoginPlugin);
		
		loginPluginMap.put(weiboLoginPlugin.getId(), weiboLoginPlugin);
		loginPluginMap.put(qqLoginPlugin.getId(), qqLoginPlugin);
		loginPluginMap.put(alipayLoginPlugin.getId(), alipayLoginPlugin);
		loginPluginMap.put(wechatAppLoginPlugin.getId(), wechatAppLoginPlugin);
		
		storagePlugins.add(filePlugin);
		storagePlugins.add(ftpPlugin);
		storagePlugins.add(ossPlugin);
		
		storagePluginMap.put(filePlugin.getId(), filePlugin);
		storagePluginMap.put(ftpPlugin.getId(), ftpPlugin);
		storagePluginMap.put(ossPlugin.getId(), ossPlugin);
	}

	/**
	 * 获取支付插件
	 * 
	 * @return 支付插件
	 */
	public List<PaymentPlugin> getPaymentPlugins() {
		Collections.sort(paymentPlugins);
		return paymentPlugins;
	}

	/**
	 * 获取存储插件
	 * 
	 * @return 存储插件
	 */
	public List<StoragePlugin> getStoragePlugins() {
		Collections.sort(storagePlugins);
		return storagePlugins;
	}

	/**
	 * 获取登录插件
	 * 
	 * @return 登录插件
	 */
	public List<LoginPlugin> getLoginPlugins() {
		Collections.sort(loginPlugins);
		return loginPlugins;
	}

	/**
	 * 获取支付插件
	 * 
	 * @param isEnabled
	 *            是否启用
	 * @return 支付插件
	 */
	public List<PaymentPlugin> getPaymentPlugins(final boolean isEnabled) {
		List<PaymentPlugin> result = new ArrayList<PaymentPlugin>();
		CollectionUtils.select(paymentPlugins, new Predicate() {
			public boolean evaluate(Object object) {
				PaymentPlugin paymentPlugin = (PaymentPlugin) object;
				return paymentPlugin.getIsEnabled() == isEnabled;
			}
		}, result);
		Collections.sort(result);
		return result;
	}

	/**
	 * 获取存储插件
	 * 
	 * @param isEnabled
	 *            是否启用
	 * @return 存储插件
	 */
	public List<StoragePlugin> getStoragePlugins(final boolean isEnabled) {
		List<StoragePlugin> result = new ArrayList<StoragePlugin>();
		CollectionUtils.select(storagePlugins, new Predicate() {
			public boolean evaluate(Object object) {
				StoragePlugin storagePlugin = (StoragePlugin) object;
				return storagePlugin.getIsEnabled() == isEnabled;
			}
		}, result);
		Collections.sort(result);
		return result;
	}

	/**
	 * 获取登录插件
	 * 
	 * @param isEnabled
	 *            是否启用
	 * @return 登录插件
	 */
	public List<LoginPlugin> getLoginPlugins(final boolean isEnabled) {
		List<LoginPlugin> result = new ArrayList<LoginPlugin>();
		CollectionUtils.select(loginPlugins, new Predicate() {
			public boolean evaluate(Object object) {
				LoginPlugin loginPlugin = (LoginPlugin) object;
				return loginPlugin.getIsEnabled() == isEnabled;
			}
		}, result);
		Collections.sort(result);
		return result;
	}

	/**
	 * 获取支付插件
	 * 
	 * @param id
	 *            ID
	 * @return 支付插件
	 */
	public PaymentPlugin getPaymentPlugin(String id) {
		return paymentPluginMap.get(id);
	}

	/**
	 * 获取存储插件
	 * 
	 * @param id
	 *            ID
	 * @return 存储插件
	 */
	public StoragePlugin getStoragePlugin(String id) {
		return storagePluginMap.get(id);
	}

	/**
	 * 获取登录插件
	 * 
	 * @param id
	 *            ID
	 * @return 登录插件
	 */
	public LoginPlugin getLoginPlugin(String id) {
		return loginPluginMap.get(id);
	}

}