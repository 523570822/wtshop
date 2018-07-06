package com.wtshop.model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wtshop.entity.ProductImage;
import com.wtshop.model.base.BaseReview;
import com.wtshop.util.ObjectUtils;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Model - 评论
 * 
 * 
 */
public class Review extends BaseReview<Review> {
	private static final long serialVersionUID = -7786965922193880425L;
	public static final Review dao = new Review();
	
	/** 路径前缀 */
	private static final String PATH_PREFIX = "/review/content";

	/** 路径后缀 */
	private static final String PATH_SUFFIX = ".jhtml";


	/**
	 * 类型
	 */
	public enum Type {

		/** 差评 */
		negative,

		/** 中评 */
		moderate,

		/** 好评 */
		positive
	}
	
	/** 会员 */
	private Member member;

	/** 产品 */
	private Product product;
	
	/** 货品 */
	private Goods goods;
	
	/** 回复 */
	private List<Review> replyReviews = new ArrayList<Review>();
	
	
	/**
	 * 获取回复
	 * 
	 * @return 回复
	 */
	public List<Review> getReplyReviews() {
		if (CollectionUtils.isEmpty(replyReviews)) {
			String sql = "SELECT * FROM `review` WHERE for_review_id = ?";
			replyReviews = Review.dao.find(sql, getId());
		}
		return replyReviews;
	}

	/**
	 * 设置回复
	 *
	 */
	public void setReplyReviews(List<Review> replyReviews) {
		this.replyReviews = replyReviews;
	}
	private List<ProductImage> productImages = new ArrayList<ProductImage>();

	public List<ProductImage> getProductImages() {
		JSONArray imagesArrays = JSONArray.parseArray(getImages());
		if (CollectionUtils.isNotEmpty(imagesArrays)) {
			for (int i = 0; i < imagesArrays.size(); i++) {
				productImages.add(JSONObject.parseObject(imagesArrays.getString(i), ProductImage.class));
			}
		}
		return productImages;
	}

	public void setProductImages(List<ProductImage> productImages) {
		this.productImages = productImages;
	}

	/**
	 * 获取评价图片
	 * 
	 * @return 评价图片
	 */
	public List<String> getImagesConverter() {
		List<String> images = new ArrayList<String>();
		String images1 = getImages();
		if(images1.contains("/")){
			JSONArray imagesArrays = JSONArray.parseArray(images1);
			if (CollectionUtils.isNotEmpty(imagesArrays)) {
				for (int i = 0; i < imagesArrays.size(); i++) {
					images.add(imagesArrays.getString(i));
				}
			}
		}
		return images;
	}



	/**
	 * 获取产品
	 * 
	 * @return 产品
	 */
	public Product getProduct() {
		if (ObjectUtils.isEmpty(product)) {
			product = Product.dao.findById(getProductId());
		}
		return product;
	}

	/**
	 * 设置产品
	 * 
	 * @param product
	 *            产品
	 */
	public void setProduct(Product product) {
		this.product = product;
	}

	/**
	 * 获取货品
	 * 
	 * @return 货品
	 */
	public Goods getGoods() {
		if (ObjectUtils.isEmpty(goods)) {
			goods = Goods.dao.findById(getGoodsId());
		}
		return goods;
	}
	public Member getMember() {
		return Member.dao.findById(getMemberId());
	}

	public void setMember(Member member) {
		this.member = member;
	}

	/**
	 * 设置货品
	 * 
	 * @param goods
	 *            货品
	 */
	public void setGoods(Goods goods) {
		this.goods = goods;
	}

	/**
	 * 获取路径
	 * 
	 * @return 路径
	 */
	public String getPath() {
		return getGoods() != null && getGoods().getId() != null ? PATH_PREFIX + "/" + getGoods().getId() + PATH_SUFFIX : null;
	}
	
}
