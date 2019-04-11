package com.wtshop.service;

import com.jfinal.aop.Enhancer;
import com.wtshop.dao.AccountDao;
import com.wtshop.dao.SpecificationDao;
import com.wtshop.model.Specification;

import java.util.List;

/**
 * Service - 规格
 * 
 * 
 */
public class SpecificationService extends BaseService<Specification> {
	private SpecificationDao specificationDao = Enhancer.enhance(SpecificationDao.class);
	/**
	 * 构造方法
	 */
	public SpecificationService() {
		super(Specification.class);
	}

	public List<Specification> findByCategoryId(Long categoryId) {
	return 	specificationDao.findByCategoryId(categoryId);

	}
}