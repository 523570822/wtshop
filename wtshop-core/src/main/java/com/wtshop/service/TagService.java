package com.wtshop.service;

import java.util.List;

import com.jfinal.aop.Enhancer;
import com.wtshop.Filter;
import com.wtshop.Order;
import com.wtshop.dao.TagDao;
import com.wtshop.model.Tag;

/**
 * Service - 标签
 * 
 * 
 */
public class TagService extends BaseService<Tag> {

	/**
	 * 构造方法
	 */
	public TagService() {
		super(Tag.class);
	}
	
	private TagDao tagDao = Enhancer.enhance(TagDao.class);
	
	/**
	 * 查找标签
	 * 
	 * @param type
	 *            类型
	 * @return 标签
	 */
	public List<Tag> findList(Tag.Type type) {
		return tagDao.findList(type);
	}

	/**
	 * 查找标签
	 * 
	 * @param count
	 *            数量
	 * @param filters
	 *            筛选
	 * @param orders
	 *            排序
	 * @param useCache
	 *            是否使用缓存
	 * @return 标签
	 */
	public List<Tag> findList(Integer count, List<Filter> filters, List<Order> orders, boolean useCache) {
		return tagDao.findList(null, count, filters, orders);
	}

	public Tag save(Tag tag) {
		return super.save(tag);
	}

	public Tag update(Tag tag) {
		return super.update(tag);
	}

//	public Tag update(Tag tag, String... ignoreProperties) {
//		return super.update(tag, ignoreProperties);
//	}

	public void delete(Long id) {
		super.delete(id);
	}

	public void delete(Long... ids) {
		super.delete(ids);
	}

	public void delete(Tag tag) {
		super.delete(tag);
	}
}