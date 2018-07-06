package com.wtshop.service;

import org.apache.commons.lang3.BooleanUtils;

import com.jfinal.aop.Enhancer;
import com.wtshop.dao.DeliveryTemplateDao;
import com.wtshop.model.DeliveryTemplate;
import com.wtshop.util.Assert;

/**
 * Service - 快递单模板
 * 
 * 
 */
public class DeliveryTemplateService extends BaseService<DeliveryTemplate> {

	/**
	 * 构造方法
	 */
	public DeliveryTemplateService() {
		super(DeliveryTemplate.class);
	}
	
	private DeliveryTemplateDao deliveryTemplateDao = Enhancer.enhance(DeliveryTemplateDao.class);
	
	/**
	 * 查找默认快递单模板
	 * 
	 * @return 默认快递单模板，若不存在则返回null
	 */
	public DeliveryTemplate findDefault() {
		return deliveryTemplateDao.findDefault();
	}

	public DeliveryTemplate save(DeliveryTemplate deliveryTemplate) {
		Assert.notNull(deliveryTemplate);

		if (BooleanUtils.isTrue(deliveryTemplate.getIsDefault())) {
			deliveryTemplateDao.setDefault(deliveryTemplate);
		}
		return super.save(deliveryTemplate);
	}

	public DeliveryTemplate update(DeliveryTemplate deliveryTemplate) {
		Assert.notNull(deliveryTemplate);

		if (BooleanUtils.isTrue(deliveryTemplate.getIsDefault())) {
			deliveryTemplateDao.setDefault(deliveryTemplate);
		}
		return super.update(deliveryTemplate);
	}
}