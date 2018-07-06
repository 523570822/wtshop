package com.wtshop.model;

import com.jfinal.kit.StrKit;
import com.wtshop.model.base.BaseGoodsTheme;
import com.wtshop.util.CollectionUtils;
import com.wtshop.util.ObjectUtils;

import java.util.List;


public class GoodsTheme extends BaseGoodsTheme<GoodsTheme> {

	public static final GoodsTheme dao = new GoodsTheme().dao();
	/**
	 * 专题对应类别
	 */
	private ProductCategory productCategory;

	/**
	 * 转向地址类型
	 */
	private TargetPath urlType;

	/**
	 * 专题对应的商品
	 */
	private List<Product> products;
	/**
	 * 目标路径专向的页面标题
	 */
	private List<TargetPath> targetTitle;
	/**
	 * 转向图片地址
	 */
	private String targetPath;

	public List<TargetPath> getTargetTitle() {
		if(CollectionUtils.isEmpty(targetTitle)){
			String sql="select * from target_path";
			targetTitle=TargetPath.dao.find(sql);
		}
		return targetTitle;
	}

	public void setTargetTitle(List<TargetPath> targetTitle) {
		this.targetTitle = targetTitle;
	}

	public TargetPath getUrlType() {
		if(ObjectUtils.isEmpty(urlType)){
			String sql="select * from target_path where id="+getTargetId();
			urlType=TargetPath.dao.find(sql).get(0);
		}
		return urlType;
	}

	public void setUrlType(TargetPath urlType) {
		this.urlType = urlType;
	}

	public List<Product> getProducts() {
		if(CollectionUtils.isEmpty(products)){
			products = Product.dao.find("SELECT p.* FROM product p LEFT JOIN theme_product t ON p.id=t.product_id WHERE t.goodsTheme_id="+getId());
		}
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public ProductCategory getProductCategory() {
		if(ObjectUtils.isEmpty(productCategory)){
			productCategory = ProductCategory.dao.findById(getProductCategoryId());
		}
		return productCategory;
	}

	public void setProductCategory(ProductCategory productCategory) {
		this.productCategory = productCategory;
	}

	public String getTargetPath() {
		String sql="select * from target_path where id="+getTargetId();
		targetPath=TargetPath.dao.find(sql).get(0).getTargetPath();
		return targetPath;
	}

	public void setTargetPath(String targetPath) {
		this.targetPath = targetPath;
	}
}
