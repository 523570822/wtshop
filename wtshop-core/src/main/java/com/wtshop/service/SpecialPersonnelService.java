package com.wtshop.service;

import com.jfinal.aop.Enhancer;
import com.jfinal.plugin.activerecord.Record;
import com.wtshop.Filter;
import com.wtshop.Order;
import com.wtshop.dao.BrandDao;
import com.wtshop.dao.ProductCategoryDao;
import com.wtshop.dao.SpecialPersonnelDao;
import com.wtshop.model.Brand;
import com.wtshop.model.ProductCategory;
import com.wtshop.model.SpecialPersonnel;
import com.wtshop.util.PinYinUtil;
import com.wtshop.util.StringUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Service - 品牌
 * 
 * 
 */
public class SpecialPersonnelService extends BaseService<SpecialPersonnel> {

	public SpecialPersonnelService() {
		super(SpecialPersonnel.class);
	}
	
	private SpecialPersonnelDao brandDao = Enhancer.enhance(SpecialPersonnelDao.class);


	/**
	 * 查询品牌列表
	 */
	public List<SpecialPersonnel> findBrandList(){
		return brandDao.findBrandList();
	}


	public Map<String,List> findBrandSort(){
		List<Record> brandSort = brandDao.findBrandSort();
		Collections.sort(brandSort, new Comparator<Record>() {
			@Override
			public int compare(Record o1, Record o2) {
				String name1 = StringUtils.isEmpty(o1.get("name"))?o1.get("name").toString():o1.get("name").toString();
				String name2 = StringUtils.isEmpty(o2.get("name"))?o2.get("name").toString():o2.get("name").toString();
				String zm1 = PinYinUtil.getPinYinHeadChar(name1.substring(0,1)).toUpperCase();
				String zm2 = PinYinUtil.getPinYinHeadChar(name2.substring(0,1)).toUpperCase();
				return zm1.compareTo(zm2);
			}
		});
		Map<String, List> stringListMap = PinYinUtil.groupByUserName(brandSort);
		return stringListMap;
	}


	/**
	 * 查找品牌
	 * 
	 * @param productCategory
	 *            商品分类
	 * @param count
	 *            数量
	 * @param filters
	 *            筛选
	 * @param orders
	 *            排序
	 * @return 品牌
	 */
	public List<SpecialPersonnel> findList(ProductCategory productCategory, Integer count, List<Filter> filters, List<Order> orders) {
		return brandDao.findList(productCategory, count, filters, orders);
	}

	public SpecialPersonnel save(SpecialPersonnel brand) {
		return super.save(brand);
	}

	public SpecialPersonnel update(SpecialPersonnel brand) {
		return super.update(brand);
	}

//	public Brand update(Brand brand, String... ignoreProperties) {
//		return super.update(brand, ignoreProperties);
//	}

	public void delete(Long id) {
		super.delete(id);
	}

	public void delete(Long... ids) {
		super.delete(ids);
	}

	public void delete(SpecialPersonnel brand) {
		super.delete(brand);
	}

	public Boolean findSpByPhone(String phone) {

		return  brandDao.findSpByPhone(phone);
	}
}