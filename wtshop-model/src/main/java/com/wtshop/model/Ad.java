package com.wtshop.model;

import java.util.Date;
import java.util.List;

import com.wtshop.model.base.BaseAd;
import com.wtshop.util.CollectionUtils;
import com.wtshop.util.ObjectUtils;

/**
 * Model - 广告
 * 
 * 
 */
public class Ad extends BaseAd<Ad> {
	private static final long serialVersionUID = 5279484023423432835L;
	public static final Ad dao = new Ad();
	
	/**
	 * 类型
	 */
	public enum Type {

		/** 文本 */
		text,
		/** 图片 */
		image
	}
	
	/** 广告位 */
	private AdPosition adPosition;

	/**
	 * 目标路径专向的页面标题
	 */
	private List<TargetPath> targetTitle;
	/**
	 * 转向地址类型
	 */
	private TargetPath urlType;


	/**
	 * 类型名称
	 */
	public Type getTypeName() {
		return Type.values()[getType()];
	}
	/**
	 * 转向图片地址
	 */
	private String targetPath;
	/**
	 * 获取广告位
	 * 
	 * @return 广告位
	 */
	public AdPosition getAdPosition() {
		if (ObjectUtils.isEmpty(adPosition)) {
			adPosition = AdPosition.dao.findById(getAdPositionId());
		}
		return adPosition;
	}

	/**
	 * 设置广告位
	 * 
	 * @param adPosition
	 *            广告位
	 */
	public void setAdPosition(AdPosition adPosition) {
		this.adPosition = adPosition;
	}
	
	/**
	 * 判断是否已开始
	 * 
	 * @return 是否已开始
	 */
	public boolean hasBegun() {
		return getBeginDate() == null || !getBeginDate().after(new Date());
	}

	/**
	 * 判断是否已结束
	 * 
	 * @return 是否已结束
	 */
	public boolean hasEnded() {
		return getEndDate() != null && !getEndDate().after(new Date());
	}

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

	public String getTargetPath() {
		return targetPath;
	}

	public void setTargetPath(String targetPath) {
		this.targetPath = targetPath;
	}
}
