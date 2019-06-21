package com.wtshop.service;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.wtshop.Filter;
import com.wtshop.Order;
import com.wtshop.Pageable;
import com.wtshop.dao.BaseDao;
import com.wtshop.util.Assert;

/**
 * Service - 基类
 * 
 * 
 */
public class BaseService<M extends Model<M>> {
	
	/** 更新忽略属性 */
	//private static final String[] UPDATE_IGNORE_PROPERTIES = new String[] { BaseEntity.CREATE_DATE_PROPERTY_NAME, BaseEntity.MODIFY_DATE_PROPERTY_NAME, BaseEntity.VERSION_PROPERTY_NAME };

	/** BaseDao */
	private BaseDao<M> baseDao;
	
	public BaseService(Class<M> modelClass) {
		this.baseDao = new BaseDao<M>(modelClass);
	}

	/**
	 * 查找实体对象
	 *
	 * @param id
	 *            ID
	 * @return 实体对象，若不存在则返回null
	 */
	public M find(Long id) {
		return baseDao.find(id);
	}



	/**
	 * 获取最后一条记录
	 */
	public M findByLast(){
		return  baseDao.findByLast();
	}
	/**
	 * 查找所有实体对象集合
	 * 
	 * @return 所有实体对象集合
	 */
	public List<M> findAll() {
		return findList(null, null, null, null);
	}

	/**
	 * 查找实体对象集合
	 * 
	 * @param ids
	 *            ID
	 * @return 实体对象集合
	 */
	public List<M> findList(Long... ids) {
		List<M> result = new ArrayList<M>();
		if (ids != null) {
			for (Long id : ids) {
				M entity = find(id);
				if (entity != null) {
					result.add(entity);
				}
			}
		}
		return result;
	}

	/**
	 * 查找实体对象集合
	 * 
	 * @param count
	 *            数量
	 * @param filters
	 *            筛选
	 * @param orders
	 *            排序
	 * @return 实体对象集合
	 */
	public List<M> findList(Integer count, List<Filter> filters, List<Order> orders) {
		return findList(null, count, filters, orders);
	}

	/**
	 * 查找实体对象集合
	 * 
	 * @param first
	 *            起始记录
	 * @param count
	 *            数量
	 * @param filters
	 *            筛选
	 * @param orders
	 *            排序
	 * @return 实体对象集合
	 */
	public List<M> findList(Integer first, Integer count, List<Filter> filters, List<Order> orders) {
		return baseDao.findList(first, count, filters, orders);
	}

	/**
	 * 查找实体对象分页
	 * 
	 * @param pageable
	 *            分页信息
	 * @return 实体对象分页
	 */
	public Page<M> findPage(Pageable pageable) {
		return baseDao.findPage(pageable);
	}

	/**
	 * 查询实体对象总数
	 * 
	 * @return 实体对象总数
	 */
	public long count() {
		return count(new Filter[] {});
	}

	/**
	 * 查询实体对象数量
	 * 
	 * @param filters
	 *            筛选
	 * @return 实体对象数量
	 */
	public long count(Filter... filters) {
		return baseDao.count(filters);
	}

	/**
	 * 判断实体对象是否存在
	 * 
	 * @param id
	 *            ID
	 * @return 实体对象是否存在
	 */
	public boolean exists(Long id) {
		return baseDao.find(id) != null;
	}

	/**
	 * 判断实体对象是否存在
	 * 
	 * @param filters
	 *            筛选
	 * @return 实体对象是否存在
	 */
	public boolean exists(Filter... filters) {
		return baseDao.count(filters) > 0;
	}

	/**
	 * 保存实体对象
	 * 
	 * @param model
	 *            实体对象
	 * @return 实体对象
	 */
	public M save(M model) {
		Assert.notNull(model);
		baseDao.save(model);
		return model;
	}

	/**
	 * 更新实体对象
	 * 
	 * @param model
	 *            实体对象
	 * @return 实体对象
	 */
	public M update(M model) {
		Assert.notNull(model);
		M persistant = baseDao.find(model.getLong("id"));
		if (persistant != null) {
			baseDao.update(model);
		}
		return model;
	}

	/**
	 * 更新实体对象
	 * 
	 * @param entity
	 *            实体对象
	 * @param ignoreProperties
	 *            忽略属性
	 * @return 实体对象
	 */
	/*public M update(M entity, String... ignoreProperties) {
		Assert.notNull(entity);
		Assert.isTrue(!entity.isNew());
		Assert.isTrue(!baseDao.isManaged(entity));

		M persistant = baseDao.find(baseDao.getIdentifier(entity));
		if (persistant != null) {
			copyProperties(entity, persistant, (String[]) ArrayUtils.addAll(ignoreProperties, UPDATE_IGNORE_PROPERTIES));
		}
		return update(persistant);
	}*/

	/**
	 * 删除实体对象
	 * 
	 * @param id
	 *            ID
	 */
	public void delete(Long id) {
		delete(baseDao.find(id));
	}

	/**
	 * 删除实体对象
	 * 
	 * @param ids
	 *            ID
	 */
	public void delete(Long... ids) {
		if (ids != null) {
			for (Long id : ids) {
				delete(baseDao.find(id));
			}
		}
	}

	/**
	 * 删除实体对象
	 * 
	 * @param entity
	 *            实体对象
	 */
	public void delete(M entity) {
		if (entity != null) {
			baseDao.remove(entity);
		}
	}
	
	/**
	 * 拷贝对象属性
	 * 
	 * @param source
	 *            源
	 * @param target
	 *            目标
	 * @param ignoreProperties
	 *            忽略属性
	 */
	protected void copyProperties(M source, M target, String... ignoreProperties) {
		Assert.notNull(source);
		Assert.notNull(target);

		PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(target);
		for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
			String propertyName = propertyDescriptor.getName();
			Method readMethod = propertyDescriptor.getReadMethod();
			Method writeMethod = propertyDescriptor.getWriteMethod();
			if (ArrayUtils.contains(ignoreProperties, propertyName) || readMethod == null || writeMethod == null) {
				continue;
			}
			try {
				Object sourceValue = readMethod.invoke(source);
				writeMethod.invoke(target, sourceValue);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e.getMessage(), e);
			} catch (IllegalArgumentException e) {
				throw new RuntimeException(e.getMessage(), e);
			} catch (InvocationTargetException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		}
	}

}