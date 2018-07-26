package com.wtshop.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseReview<M extends BaseReview<M>> extends Model<M> implements IBean {

	public void setId(Long id) {
		set("id", id);
	}

	public Long getId() {
		return get("id");
	}
	public void setName(String name) {
		set("name", name);
	}

	public Long getName() {
		return get("name");
	}

	public void setCreateDate(java.util.Date createDate) {
		set("create_date", createDate);
	}

	public java.util.Date getCreateDate() {
		return get("create_date");
	}

	public void setModifyDate(java.util.Date modifyDate) {
		set("modify_date", modifyDate);
	}

	public java.util.Date getModifyDate() {
		return get("modify_date");
	}

	public void setVersion(Long version) {
		set("version", version);
	}

	public Long getVersion() {
		return get("version");
	}

	public void setOrderContent(String orderContent) {
		set("order_content", orderContent);
	}

	public String getOrderContent() {
		return get("order_content");
	}

	public void setContent(String content) {
		set("content", content);
	}

	public String getContent() {
		return get("content");
	}

	public void setIp(String ip) {
		set("ip", ip);
	}

	public String getIp() {
		return get("ip");
	}

	public void setIsShow(Boolean isShow) {
		set("is_show", isShow);
	}

	public Boolean getIsShow() {
		return get("is_show");
	}

	public void setOrderScore(Integer orderScore) {
		set("order_score", orderScore);
	}

	public Integer getOrderScore() {
		return get("order_score");
	}

	public void setScore(Integer score) {
		set("score", score);
	}

	public Integer getScore() {
		return get("score");
	}

	public void setOrderImages(String orderImages) {
		set("order_images", orderImages);
	}

	public String getOrderImages() {
		return get("order_images");
	}

	public void setImages(String images) {
		set("images", images);
	}

	public String getImages() {
		return get("images");
	}

	public void setGoodsId(Long goodsId) {
		set("goods_id", goodsId);
	}

	public Long getGoodsId() {
		return get("goods_id");
	}

	public void setOrderId(Long orderId) {
		set("order_id", orderId);
	}

	public Long getOrderId() {
		return get("order_id");
	}

	public void setOrderItemId(Long orderItemId) {
		set("order_item_id", orderItemId);
	}

	public Long getOrderItemId() {
		return get("order_item_id");
	}

	public void setForReviewId(Long forReviewId) {
		set("for_review_id", forReviewId);
	}

	public Long getForReviewId() {
		return get("for_review_id");
	}

	public void setProductId(Long productId) {
		set("product_id", productId);
	}

	public Long getProductId() {
		return get("product_id");
	}

	public void setMemberId(Long memberId) {
		set("member_id", memberId);
	}

	public Long getMemberId() {
		return get("member_id");
	}

	public void setIsAnonymous(Boolean isAnonymous) {
		set("is_anonymous", isAnonymous);
	}

	public Boolean getIsAnonymous() {
		return get("is_anonymous");
	}

	public void setStatus(Boolean status) {
		set("status", status);
	}

	public Boolean getStatus() {
		return get("status");
	}

	public void setIsDelete(Boolean isDelete) {
		set("is_delete", isDelete);
	}

	public Boolean getIsDelete() {
		return get("is_delete");
	}

}
