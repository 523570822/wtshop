package com.wtshop.api.common.token;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.wtshop.api.utils.TokenUtil;
import com.wtshop.model.Member;

public class TokenManager {
	
	private static TokenManager	me	= new TokenManager();

	private Map<String, Member>	tokens;
	private Map<String, String>	memberToken;

	public TokenManager() {
		tokens = new ConcurrentHashMap<String, Member>();
		memberToken = new ConcurrentHashMap<String, String>();
	}

	/**
	 * 获取单例对象
	 * 
	 * @return
	 */
	public static TokenManager getMe() {
		return me;
	}

	/**
	 * 验证token
	 * 
	 * @param token
	 * @return
	 */
	public Member validate(String token) {
		return tokens.get(token);
	}

	/**
	 * 生成token值
	 * 
	 * @param member
	 * @return
	 */
	public String generateToken(Member user) {
		String token = TokenUtil.generateToken();
		memberToken.put(String.valueOf(user.getId()), token);
		tokens.put(token, user);
		return token;
	}
}
