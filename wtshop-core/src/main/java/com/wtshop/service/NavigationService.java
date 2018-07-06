package com.wtshop.service;

import java.util.List;

import com.jfinal.aop.Enhancer;
import com.wtshop.Filter;
import com.wtshop.Order;
import com.wtshop.dao.NavigationDao;
import com.wtshop.model.Navigation;

/**
 * Service - 导航
 * 
 * 
 */
public class NavigationService extends BaseService<Navigation> {

	/**
	 * 构造方法
	 */
	public NavigationService() {
		super(Navigation.class);
	}
	
	private NavigationDao navigationDao = Enhancer.enhance(NavigationDao.class);
	
	/**
	 * 查找导航
	 * 
	 * @param position
	 *            位置
	 * @return 导航
	 */
	public List<Navigation> findList(Navigation.Position position) {
		return navigationDao.findList(position);
	}

	/**
	 * 查找导航
	 * 
	 * @param count
	 *            数量
	 * @param filters
	 *            筛选
	 * @param orders
	 *            排序
	 * @param useCache
	 *            是否使用缓存
	 * @return 导航
	 */
	public List<Navigation> findList(Integer count, List<Filter> filters, List<Order> orders, boolean useCache) {
		return navigationDao.findList(null, count, filters, orders);
	}

	public Navigation save(Navigation navigation) {
		return super.save(navigation);
	}

	public Navigation update(Navigation navigation) {
		return super.update(navigation);
	}

//	public Navigation update(Navigation navigation, String... ignoreProperties) {
//		return super.update(navigation, ignoreProperties);
//	}

	public void delete(Long id) {
		super.delete(id);
	}

	public void delete(Long... ids) {
		super.delete(ids);
	}

	public void delete(Navigation navigation) {
		super.delete(navigation);
	}

}