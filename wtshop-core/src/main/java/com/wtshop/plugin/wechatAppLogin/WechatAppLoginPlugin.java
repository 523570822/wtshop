package com.wtshop.plugin.wechatAppLogin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wtshop.model.PluginConfig;
import com.wtshop.plugin.LoginPlugin;
import com.wtshop.util.WebUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Plugin - 支付宝快捷登录
 * 
 * 
 */
public class WechatAppLoginPlugin extends LoginPlugin {

	@Override
	public String getName() {
		return "微信App登录";
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
		return "wechat_app_login/install.jhtml";
	}

	@Override
	public String getUninstallUrl() {
		return "wechat_app_login/uninstall.jhtml";
	}

	@Override
	public String getSettingUrl() {
		return "wechat_app_login/setting.jhtml";
	}

	@Override
	public String getRequestUrl() {
		return "https://mapi.alipay.com/gateway.do";
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
	public Map<String, Object> getParameterMap(HttpServletRequest request) {
		PluginConfig pluginConfig = getPluginConfig();
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("service", "alipay.auth.authorize");
		parameterMap.put("partner", pluginConfig.getAttribute("partner"));
		parameterMap.put("_input_charset", "utf-8");
		parameterMap.put("sign_type", "MD5");
		parameterMap.put("return_url", getNotifyUrl());
		parameterMap.put("target_service", "user.auth.quick.login");
		parameterMap.put("exter_invoke_ip", request.getLocalAddr());
		parameterMap.put("client_ip", request.getLocalAddr());
		parameterMap.put("sign", generateSign(parameterMap));
		return parameterMap;
	}

	@Override
	public boolean verifyNotify(HttpServletRequest request) {
		PluginConfig pluginConfig = getPluginConfig();
		if (generateSign(request.getParameterMap()).equals(request.getParameter("sign")) && "T".equals(request.getParameter("is_success"))) {
			Map<String, Object> parameterMap = new HashMap<String, Object>();
			parameterMap.put("service", "notify_verify");
			parameterMap.put("partner", pluginConfig.getAttribute("partner"));
			parameterMap.put("notify_id", request.getParameter("notify_id"));
			if ("true".equals(WebUtils.post("https://mapi.alipay.com/gateway.do", parameterMap))) {
				return true;
			}
		}
		return false;
	}

	//	https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
	@Override
	public String getOpenId(HttpServletRequest request) {
		PluginConfig pluginConfig = getPluginConfig();
		String code = request.getParameter("code");
		String urlString = String.format("https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code", pluginConfig.getAttribute("oauthKey"), pluginConfig.getAttribute("oauthSecret"), code);
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		String result = WebUtils.post(urlString, parameterMap);
		if (StringUtils.isNotEmpty(result)){
			JSONObject resultJson = JSONObject.parseObject(result);
			String unionid = resultJson.getString("unionid");
			return unionid;
		}
		return null;
	}

	public JSONObject getUserInfo(HttpServletRequest request) {
		PluginConfig pluginConfig = getPluginConfig();
		String code = request.getParameter("code");
		String urlString = String.format("https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code", pluginConfig.getAttribute("oauthKey"), pluginConfig.getAttribute("oauthSecret"), code);
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		String result = WebUtils.post(urlString, parameterMap);
		if (StringUtils.isNotEmpty(result)){
			JSONObject resultJson = JSONObject.parseObject(result);
			String unionid = resultJson.getString("unionid");
			return resultJson;
		}
		return null;
	}


	@Override
	public String getEmail(HttpServletRequest request) {
		String email = request.getParameter("email");
		if (StringUtils.isEmpty(email) || !email.contains("@")) {
			return null;
		}
		return email;
	}

	@Override
	public String getNickname(HttpServletRequest request) {
		return request.getParameter("real_name");
	}

	/**
	 * 生成签名
	 * 
	 * @param parameterMap
	 *            参数
	 * @return 签名
	 */
	private String generateSign(Map<String, ?> parameterMap) {
		PluginConfig pluginConfig = getPluginConfig();
		return DigestUtils.md5Hex(joinKeyValue(new TreeMap<String, Object>(parameterMap), null, pluginConfig.getAttribute("key"), "&", true, "sign_type", "sign"));
	}

}