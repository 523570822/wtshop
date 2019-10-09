package com.wtshop.dao;

import org.apache.commons.lang3.StringUtils;

import com.wtshop.model.Shipping;

/**
 * Dao - 发货单
 * 
 * 
 */
public class ShippingDao extends BaseDao<Shipping> {
	
	/**
	 * 构造方法
	 */
	public ShippingDao() {
		super(Shipping.class);
	}
	
	/**
	 * 根据编号查找发货单
	 * 
	 * @param sn
	 *            编号(忽略大小写)
	 * @return 发货单，若不存在则返回null
	 */
	public Shipping findBySn(String sn) {
		if (StringUtils.isEmpty(sn)) {
			return null;
		}
		String sql = "SELECT s.tracking_no FROM shipping s LEFT JOIN `order` o on s.order_id = o.id WHERE o.sn =" + sn;
		return modelManager.findFirst(sql);

	}
	/**
	 * 根据编号查找发货单
	 *
	 * @param sn
	 *            编号(忽略大小写)
	 * @return 发货单，若不存在则返回null
	 */
	public Shipping findByIdentifierId(Long sn) {
		if (StringUtils.isEmpty(sn+"")) {
			return null;
		}
		String sql = "SELECT * FROM shipping s  WHERE s.identifier_Id =" + sn;
		return modelManager.findFirst(sql);

	}
}