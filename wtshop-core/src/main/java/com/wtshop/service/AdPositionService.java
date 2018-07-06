package com.wtshop.service;

import com.jfinal.aop.Enhancer;
import com.wtshop.dao.AdPositionDao;
import com.wtshop.model.AdPosition;

/**
 * Service - 广告位
 * 
 * 
 */
public class AdPositionService extends BaseService<AdPosition> {

	/**
	 * 构造方法
	 */
	public AdPositionService() {
		super(AdPosition.class);
	}
	
	private AdPositionDao adPositionDao = Enhancer.enhance(AdPositionDao.class);
	
	/**
	 * 查找广告位
	 * 
	 * @param id
	 *            ID
	 * @param useCache
	 *            是否使用缓存
	 * @return 广告位
	 */
	public AdPosition find(Long id, boolean useCache) {
		return adPositionDao.find(id);
	}
	
	public AdPosition save(AdPosition adPosition) {
		return super.save(adPosition);
	}

	public AdPosition update(AdPosition adPosition) {
		return super.update(adPosition);
	}

//	public AdPosition update(AdPosition adPosition, String... ignoreProperties) {
//		return super.update(adPosition, ignoreProperties);
//	}

	public void delete(Long id) {
		super.delete(id);
	}

	public void delete(Long... ids) {
		super.delete(ids);
	}

	public void delete(AdPosition adPosition) {
		super.delete(adPosition);
	}

}