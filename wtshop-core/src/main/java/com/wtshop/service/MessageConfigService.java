package com.wtshop.service;

import com.jfinal.aop.Enhancer;
import com.wtshop.dao.MessageConfigDao;
import com.wtshop.model.MessageConfig;

/**
 * Service - 消息配置
 * 
 * 
 */
public class MessageConfigService extends BaseService<MessageConfig> {

	/**
	 * 构造方法
	 */
	public MessageConfigService() {
		super(MessageConfig.class);
	}
	
	private MessageConfigDao messageConfigDao = Enhancer.enhance(MessageConfigDao.class);
	
	/**
	 * 查找消息配置
	 * 
	 * @param type
	 *            类型
	 * @return 消息配置
	 */
	public MessageConfig find(MessageConfig.Type type) {
		return messageConfigDao.find(type);
	}

	public MessageConfig save(MessageConfig messageConfig) {
		return super.save(messageConfig);
	}

	public MessageConfig update(MessageConfig messageConfig) {
		return super.update(messageConfig);
	}

//	public MessageConfig update(MessageConfig messageConfig, String... ignoreProperties) {
//		return super.update(messageConfig, ignoreProperties);
//	}

	public void delete(Long id) {
		super.delete(id);
	}

	public void delete(Long... ids) {
		super.delete(ids);
	}

	public void delete(MessageConfig messageConfig) {
		super.delete(messageConfig);
	}

}