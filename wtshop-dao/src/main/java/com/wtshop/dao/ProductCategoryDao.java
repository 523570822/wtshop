package com.wtshop.dao;

import com.wtshop.model.ProductCategory;
import com.wtshop.util.SqlUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;

import java.util.*;

/**
 * Dao - 商品分类
 * 
 * 
 */
public class ProductCategoryDao extends OrderEntity<ProductCategory> {
	
	/**
	 * 构造方法
	 */
	public ProductCategoryDao() {
		super(ProductCategory.class);
	}
	
	/**
	 * 查找顶级商品分类
	 * 
	 * @param count
	 *            数量
	 * @return 顶级商品分类
	 */
	public List<ProductCategory> findRoots(Integer count) {
		String sql = "SELECT * FROM product_category WHERE parent_id IS NULL ORDER BY orders ASC ";
		if (count != null) {
			sql += "LIMIT 0, " + count;
		}
		return modelManager.find(sql);
	}

	/**
	 * 查找一级和二级商品分类
	 *
	 * @param count
	 *            数量
	 * @return一级和二级商品分类
	 */
	public List<ProductCategory> findOneAndTwoRoots(Integer count) {
		String sql = "SELECT * FROM product_category WHERE GRADE = 0 ORDER BY orders ASC ";
		if (count != null) {
			sql += "LIMIT 0, " + count;
		}
		return modelManager.find(sql);
	}

	/**
	 * 查找一级和二级商品分类
	 *
	 *            数量
	 * @return一级和二级商品分类
	 */
	public List<ProductCategory> TwoRoots(Long id) {
		String sql = "SELECT * FROM product_category WHERE parent_id =" +id;

		return modelManager.find(sql);
	}

	/**
	 * 查找上级商品分类
	 * 
	 * @param productCategory
	 *            商品分类
	 * @param recursive
	 *            是否递归
	 * @param count
	 *            数量
	 * @return 上级商品分类
	 */
	public List<ProductCategory> findParents(ProductCategory productCategory, boolean recursive, Integer count) {
		if (productCategory == null || productCategory.getParent() == null) {
			return Collections.emptyList();
		}
		String sql = "";
		if (recursive) {
			List<Long> ids = Arrays.asList(productCategory.getParentIds());
			sql = "SELECT * FROM product_category WHERE id IN " + SqlUtils.getSQLIn(ids) + " ORDER BY grade ASC ";
			
		} else {
			sql = "SELECT * FROM product_category WHERE id = " + productCategory.getParent();
		}
		if (count != null) {
			sql += "LIMIT 0, " + count;
		}
		return modelManager.find(sql);
	}

	
	/**
	 * 查找下级商品分类
	 * 
	 * @param productCategory
	 *            商品分类
	 * @param recursive
	 *            是否递归
	 * @param count
	 *            数量
	 * @return 下级商品分类
	 */
	public List<ProductCategory> findChildren(ProductCategory productCategory, boolean recursive, Integer count) {
		String sql = "";
		if (recursive) {
			if (productCategory != null) {
				sql = "SELECT * FROM product_category WHERE tree_path LIKE '%" + ProductCategory.TREE_PATH_SEPARATOR + productCategory.getId() + ProductCategory.TREE_PATH_SEPARATOR + "%' ORDER BY grade ASC, orders ASC ";
			} else {
				sql = "SELECT * FROM product_category ORDER BY grade ASC, orders ASC ";
			}
			if (count != null) {
				sql += "LIMIT 0, " + count;
			}
			List<ProductCategory> result = modelManager.find(sql);
			sort(result);
			return result;
		} else {
			if (productCategory != null) {
				sql = "SELECT * FROM product_category WHERE parent_id = " + productCategory.getId() + " ORDER BY orders ASC ";
			} else {
				sql = "SELECT * FROM product_category WHERE parent_id IS NULL ORDER BY orders ASC ";
			}
			if (count != null) {
				sql += " LIMIT 0, " + count;
			}
			return modelManager.find(sql);
		}
	}
	
	/**
	 * 排序商品分类
	 * 
	 * @param productCategories
	 *            商品分类
	 */
	private void sort(List<ProductCategory> productCategories) {
		if (CollectionUtils.isEmpty(productCategories)) {
			return;
		}
		final Map<Long, Integer> orderMap = new HashMap<Long, Integer>();
		for (ProductCategory productCategory : productCategories) {
			orderMap.put(productCategory.getId(), productCategory.getOrders());
		}
		Collections.sort(productCategories, new Comparator<ProductCategory>() {
			@Override
			public int compare(ProductCategory productCategory1, ProductCategory productCategory2) {
				Long[] ids1 = (Long[]) ArrayUtils.add(productCategory1.getParentIds(), productCategory1.getId());
				Long[] ids2 = (Long[]) ArrayUtils.add(productCategory2.getParentIds(), productCategory2.getId());
				Iterator<Long> iterator1 = Arrays.asList(ids1).iterator();
				Iterator<Long> iterator2 = Arrays.asList(ids2).iterator();
				CompareToBuilder compareToBuilder = new CompareToBuilder();
				while (iterator1.hasNext() && iterator2.hasNext()) {
					Long id1 = iterator1.next();
					Long id2 = iterator2.next();
					Integer order1 = orderMap.get(id1);
					Integer order2 = orderMap.get(id2);
					compareToBuilder.append(order1, order2).append(id1, id2);
					if (!iterator1.hasNext() || !iterator2.hasNext()) {
						compareToBuilder.append(productCategory1.getGrade(), productCategory2.getGrade());
					}
				}
				return compareToBuilder.toComparison();
			}
		});
	}
	public List<ProductCategory> queryByGrade(Integer grade){
		String sql = "select * from product_category where grade = "+grade;
		return modelManager.find(sql);
	}

}