package com.wtshop.service;

import com.jfinal.aop.Enhancer;
import com.wtshop.dao.HouserkeeperGradeDao;
import com.wtshop.model.Houserkeeper;
import com.wtshop.util.Assert;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

/**
 * Service - 会员等级
 * 
 * 
 */
public class HouserkeeperGradeService extends BaseService<Houserkeeper> {

	/**
	 * 构造方法
	 */
	public HouserkeeperGradeService() {
		super(Houserkeeper.class);
	}
	
	private HouserkeeperGradeDao memberRankDao = Enhancer.enhance(HouserkeeperGradeDao.class);
	
	/**
	 * 判断名称是否存在
	 * 
	 * @param name
	 *            名称(忽略大小写)
	 * @return 名称是否存在
	 */
	public boolean nameExists(String name) {
		return memberRankDao.nameExists(name);
	}

	/**
	 * 判断名称是否唯一
	 * 
	 * @param previousName
	 *            修改前名称(忽略大小写)
	 * @param currentName
	 *            当前名称(忽略大小写)
	 * @return 名称是否唯一
	 */
	public boolean nameUnique(String previousName, String currentName) {
		return StringUtils.equalsIgnoreCase(previousName, currentName) || !memberRankDao.nameExists(currentName);
	}

	/**
	 * 判断消费金额是否存在
	 * 
	 * @param amount
	 *            消费金额
	 * @return 消费金额是否存在
	 */
	public boolean amountExists(BigDecimal amount) {
		return memberRankDao.amountExists(amount);
	}

	/**
	 * 判断消费金额是否唯一
	 * 
	 * @param previousAmount
	 *            修改前消费金额
	 * @param currentAmount
	 *            当前消费金额
	 * @return 消费金额是否唯一
	 */
	public boolean amountUnique(BigDecimal previousAmount, BigDecimal currentAmount) {
		return (previousAmount != null && previousAmount.compareTo(currentAmount) == 0) || !memberRankDao.amountExists(currentAmount);
	}

	/**
	 * 查找默认会员等级
	 * 
	 * @return 默认会员等级，若不存在则返回null
	 */
	public Houserkeeper findDefault() {
		return memberRankDao.findDefault();
	}

	/**
	 * 根据消费金额查找符合此条件的最高会员等级
	 * 
	 * @param amount
	 *            消费金额
	 * @return 会员等级，不包含特殊会员等级，若不存在则返回null
	 */
	public Houserkeeper findByAmount(BigDecimal amount) {
		return memberRankDao.findByAmount(amount);
	}

	public Houserkeeper save(Houserkeeper memberRank) {
		Assert.notNull(memberRank);

		if (BooleanUtils.isTrue(memberRank.getIsDefault())) {
			memberRankDao.setDefault(memberRank);
		}
		return super.save(memberRank);
	}

	public Houserkeeper update(Houserkeeper memberRank) {
		Assert.notNull(memberRank);

		if (BooleanUtils.isTrue(memberRank.getIsDefault())) {
			memberRankDao.setDefault(memberRank);
		}
		return super.update(memberRank);
	}
}