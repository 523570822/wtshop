package com.wtshop.service;

import com.jfinal.aop.Before;
import com.jfinal.aop.Enhancer;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.wtshop.dao.ProductCategoryDao;
import com.wtshop.model.*;
import com.wtshop.util.Assert;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Service - 商品分类
 * 
 * 
 */
public class ProductCategoryService extends BaseService<ProductCategory> {

	/**
	 * 构造方法
	 */
	public ProductCategoryService() {
		super(ProductCategory.class);
	}
	
	private ProductCategoryDao productCategoryDao = Enhancer.enhance(ProductCategoryDao.class);
	
	/**
	 * 查找顶级商品分类
	 * 
	 * @return 顶级商品分类
	 */
	public List<ProductCategory> findRoots() {
		return productCategoryDao.findRoots(null);
	}

	/**
	 * 查找一级和二级商品分类
	 *
	 * @return 查找一级和二级商品分类
	 */
	public List<ProductCategory> findOneAndTwoRoots() {
		return productCategoryDao.findOneAndTwoRoots(null);
	}

	/**
	 * 查找一级和二级商品分类
	 *
	 * @return 查找一级和二级商品分类
	 */
	public List<ProductCategory> TwoRoots(Long id) {
		return productCategoryDao.TwoRoots(id);
	}


	/**
	 * 查找顶级商品分类
	 * 
	 * @param count
	 *            数量
	 * @return 顶级商品分类
	 */
	public List<ProductCategory> findRoots(Integer count) {
		return productCategoryDao.findRoots(count);
	}

	/**
	 * 查找顶级商品分类
	 * 
	 * @param count
	 *            数量
	 * @param useCache
	 *            是否使用缓存
	 * @return 顶级商品分类
	 */
	public List<ProductCategory> findRoots(Integer count, boolean useCache) {
		return productCategoryDao.findRoots(count);
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
		return productCategoryDao.findParents(productCategory, recursive, count);
	}

	/**
	 * 查找上级商品分类
	 * 
	 * @param productCategoryId
	 *            商品分类ID
	 * @param recursive
	 *            是否递归
	 * @param count
	 *            数量
	 * @param useCache
	 *            是否使用缓存
	 * @return 上级商品分类
	 */
	public List<ProductCategory> findParents(Long productCategoryId, boolean recursive, Integer count, boolean useCache) {
		ProductCategory productCategory = productCategoryDao.find(productCategoryId);
		if (productCategoryId != null && productCategory == null) {
			return Collections.emptyList();
		}
		return productCategoryDao.findParents(productCategory, recursive, count);
	}

	/**
	 * 查找商品分类树
	 * 
	 * @return 商品分类树
	 */
	public List<ProductCategory> findTree() {
		return productCategoryDao.findChildren(null, true, null);
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
		return productCategoryDao.findChildren(productCategory, recursive, count);
	}

	/**
	 * 查找下级商品分类
	 * 
	 * @param productCategoryId
	 *            商品分类ID
	 * @param recursive
	 *            是否递归
	 * @param count
	 *            数量
	 * @param useCache
	 *            是否使用缓存
	 * @return 下级商品分类
	 */
	public List<ProductCategory> findChildren(Long productCategoryId, boolean recursive, Integer count, boolean useCache) {
		ProductCategory productCategory = productCategoryDao.find(productCategoryId);
		if (productCategoryId != null && productCategory == null) {
			return Collections.emptyList();
		}
		return productCategoryDao.findChildren(productCategory, recursive, count);
	}

	/**
	 * 保存
	 * 
	 */
	@Before(Tx.class)
	public ProductCategory save(ProductCategory productCategory) {
		Assert.notNull(productCategory);
		setValue(productCategory);
		super.save(productCategory);
		List<Brand> brands = productCategory.getBrands();
		if (CollectionUtils.isNotEmpty(brands)) {
			for (Brand brand : brands) {
				ProductCategoryBrand productCategoryBrand = new ProductCategoryBrand();
				productCategoryBrand.setBrands(brand.getId());
				productCategoryBrand.setProductCategories(productCategory.getId());
				productCategoryBrand.save();
			}
				
		}
		List<Promotion> promotions = productCategory.getPromotions();
		if (CollectionUtils.isNotEmpty(promotions)) {
			for (Promotion promotion : promotions) {
				ProductCategoryPromotion productCategoryPromotion = new ProductCategoryPromotion();
				productCategoryPromotion.setPromotions(promotion.getId());
				productCategoryPromotion.setProductCategories(productCategory.getId());
				productCategoryPromotion.save();
			}
		}
		return productCategory;
	}

	/**
	 * 更新
	 * 
	 */
	@Before(Tx.class)
	public ProductCategory update(ProductCategory productCategory) {
		Assert.notNull(productCategory);

		setValue(productCategory);
		for (ProductCategory children : productCategoryDao.findChildren(productCategory, true, null)) {
			setValue(children);
		}
		super.update(productCategory);
		
		List<Brand> brands = productCategory.getBrands();
		if (CollectionUtils.isNotEmpty(brands)) {
			ProductCategoryBrand.dao.delete(productCategory.getId());
			for (Brand brand : brands) {
				ProductCategoryBrand productCategoryBrand = new ProductCategoryBrand();
				productCategoryBrand.setBrands(brand.getId());
				productCategoryBrand.setProductCategories(productCategory.getId());
				productCategoryBrand.save();
			}
				
		}
		List<Promotion> promotions = productCategory.getPromotions();
		if (CollectionUtils.isNotEmpty(promotions)) {
			ProductCategoryPromotion.dao.delete(productCategory.getId());
			for (Promotion promotion : promotions) {
				ProductCategoryPromotion productCategoryPromotion = new ProductCategoryPromotion();
				productCategoryPromotion.setPromotions(promotion.getId());
				productCategoryPromotion.setProductCategories(productCategory.getId());
				productCategoryPromotion.save();
			}
		}
		return productCategory;
	}

	@Before(Tx.class)
	public void delete(Long id) {
		ProductCategoryBrand.dao.delete(id);
		ProductCategoryPromotion.dao.delete(id);
		super.delete(id);
	}

	public void delete(Long... ids) {
		super.delete(ids);
	}

	public void delete(ProductCategory productCategory) {
		super.delete(productCategory);
	}

	public List<ProductCategory> queryByGrade(Integer grade){
		return productCategoryDao.queryByGrade(grade);
	}

	/**
	 * 设置值
	 * 
	 * @param productCategory
	 *            商品分类
	 */
	private void setValue(ProductCategory productCategory) {
		if (productCategory == null) {
			return;
		}
		ProductCategory parent = productCategory.getParent();
		if (parent != null) {
			productCategory.setTreePath(parent.getTreePath() + parent.getId() + ProductCategory.TREE_PATH_SEPARATOR);
		} else {
			productCategory.setTreePath(ProductCategory.TREE_PATH_SEPARATOR);
		}
		productCategory.setGrade(productCategory.getParentIds().length);
	}

	/**
	 * 获取当前分类下所有的子元素
	 * @param productCategoryId
	 * @return
	 */
	public List<Long> getProductCategory(Long productCategoryId){
		List<Long> ProductCategorys = new ArrayList<>();
		ProductCategorys.add(productCategoryId);
		List<ProductCategory> thirdRoots =findChildren(productCategoryId, true, null, false);
		for(ProductCategory productCategory : thirdRoots){
			ProductCategorys.add(productCategory.getId());
		}
		return ProductCategorys;
	}

}