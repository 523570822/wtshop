package com.wtshop.service;

import java.util.List;

import com.jfinal.aop.Enhancer;
import com.wtshop.Filter;
import com.wtshop.Order;
import com.wtshop.dao.FriendLinkDao;
import com.wtshop.model.FriendLink;

/**
 * Service - 友情链接
 * 
 * 
 */
public class FriendLinkService extends BaseService<FriendLink> {

	/**
	 * 构造方法
	 */
	public FriendLinkService() {
		super(FriendLink.class);
	}
	
	private FriendLinkDao friendLinkDao = Enhancer.enhance(FriendLinkDao.class);
	
	/**
	 * 查找友情链接
	 * 
	 * @param type
	 *            类型
	 * @return 友情链接
	 */
	public List<FriendLink> findList(FriendLink.Type type) {
		return friendLinkDao.findList(type);
	}

	/**
	 * 查找友情链接
	 * 
	 * @param count
	 *            数量
	 * @param filters
	 *            筛选
	 * @param orders
	 *            排序
	 * @param useCache
	 *            是否使用缓存
	 * @return 友情链接
	 */
	public List<FriendLink> findList(Integer count, List<Filter> filters, List<Order> orders, boolean useCache) {
		return friendLinkDao.findList(null, count, filters, orders);
	}

	public FriendLink save(FriendLink friendLink) {
		return super.save(friendLink);
	}

	public FriendLink update(FriendLink friendLink) {
		return super.update(friendLink);
	}

//	public FriendLink update(FriendLink friendLink, String... ignoreProperties) {
//		return super.update(friendLink, ignoreProperties);
//	}

	public void delete(Long id) {
		super.delete(id);
	}

	public void delete(Long... ids) {
		super.delete(ids);
	}

	public void delete(FriendLink friendLink) {
		super.delete(friendLink);
	}
}