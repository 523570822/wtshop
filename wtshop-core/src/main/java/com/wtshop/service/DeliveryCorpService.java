package com.wtshop.service;

import com.jfinal.aop.Enhancer;
import com.jfinal.plugin.activerecord.Page;
import com.wtshop.Pageable;
import com.wtshop.dao.DeliveryCorpDao;
import com.wtshop.dao.MemberDao;
import com.wtshop.model.DeliveryCorp;

/**
 * Service - 物流公司
 * 
 * 
 */
public class DeliveryCorpService extends BaseService<DeliveryCorp> {

	private DeliveryCorpDao deliveryCorpDao= Enhancer.enhance(DeliveryCorpDao.class);

	/**
	 * 构造方法
	 */
	public DeliveryCorpService() {
		super(DeliveryCorp.class);
	}


	public Page<DeliveryCorp> findPageBySearch(Pageable pageable){


		return deliveryCorpDao.findPageBySearch(pageable);
	}
	
}