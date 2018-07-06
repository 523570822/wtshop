package com.wtshop.service;

import com.wtshop.dao.SnDao;
import com.wtshop.model.Sn;

/**
 * Service - 序列号
 * 
 * 
 */
public class SnService {

	private SnDao snDao = new SnDao();
	
	/**
	 * 生成序列号
	 * 
	 * @param type
	 *            类型
	 * @return 序列号
	 */
	public String generate(Sn.Type type) {
		return snDao.generate(type);
	}

}