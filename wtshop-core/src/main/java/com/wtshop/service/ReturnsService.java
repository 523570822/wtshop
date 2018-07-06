package com.wtshop.service;

import com.jfinal.aop.Enhancer;
import com.jfinal.plugin.activerecord.Page;
import com.wtshop.Pageable;
import com.wtshop.dao.ReturnsDao;
import com.wtshop.dao.SnDao;
import com.wtshop.model.Member;
import com.wtshop.model.Returns;
import com.wtshop.model.ReturnsItem;
import com.wtshop.model.Sn;
import com.wtshop.util.Assert;

import java.util.List;

/**
 * Service - 退货单
 * 
 * 
 */
public class ReturnsService extends BaseService<Returns> {

	/**
	 * 构造方法
	 */
	public ReturnsService() {
		super(Returns.class);
	}
	
	private SnDao snDao = Enhancer.enhance(SnDao.class);

	private ReturnsDao returnsDao = Enhancer.enhance(ReturnsDao.class);

	public Returns save(Returns returns) {
		Assert.notNull(returns);

		returns.setSn(snDao.generate(Sn.Type.returns));

		return super.save(returns);
	}

	public Returns findReturnsBySn(Long sn){
		return returnsDao.findReturnsBySn(sn);
	}

	public Returns findReturnsByOrderId(Long orderId){
		return returnsDao.findReturnsByOrderId(orderId);
	}


	public Page<Returns> findPages(Pageable pageable){
		return returnsDao.findPages(pageable);
	}

	/**
	 * 根据sn 和 订单项id 查找
	 */
	public List<Returns> findReturnsList(String name, String sn , Member member){
		return returnsDao.findReturnsList(name , sn ,member);

	}

	/**
	 * 根据 member 查找
	 */
	public List<Returns> findByMember( Member member, Integer status){
		return returnsDao.findByMember(member, status);

	}


}