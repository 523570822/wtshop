package com.wtshop.api.common.result;

import com.jfinal.plugin.activerecord.Page;
import com.wtshop.model.*;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sq on 2017/8/31.
 */
public class VipGoodsResult implements Serializable{

    @ApiModelProperty(value = "货物", dataType = "Goods")
    private Goods goods;

    @ApiModelProperty(value = "名称", dataType = "String")
    private String name;

    @ApiModelProperty(value = "是否关注", dataType = "Boolean")
    private Boolean favorite;

    @ApiModelProperty(value = "评论分页", dataType = "Review")
    private Page<Review> review;

    @ApiModelProperty(value = "评论总数", dataType = "Long")
    private Long reviewCount;

    @ApiModelProperty(value = "好评总数", dataType = "Long")
    private Long positiveCount;

    @ApiModelProperty(value = "中评总数", dataType = "Long")
    private Long moderateCount;

    @ApiModelProperty(value = "差评总数", dataType = "Long")
    private Long negativeCount;

    @ApiModelProperty(value = "晒图", dataType = "Long")
    private Long imagesCount;

    private List<Tag> tag;

    private Receiver aDefault;

    private String receiveTime;

    private String settingShoppingCopyUrl;

    private String shoppingCopy;

    private String certifiedCopyUrl;

    private String certifiedCopy;

    public VipGoodsResult(Goods goods, String name, Boolean favorite, Page<Review> review, Long reviewCount, Long positiveCount, Long moderateCount, Long negativeCount, Long imagesCount, List<Tag> tag, Receiver aDefault, String receiveTime, String settingShoppingCopyUrl, String shoppingCopy, String certifiedCopyUrl, String certifiedCopy) {
        this.goods = goods;
        this.name = name;
        this.favorite = favorite;
        this.review = review;
        this.reviewCount = reviewCount;
        this.positiveCount = positiveCount;
        this.moderateCount = moderateCount;
        this.negativeCount = negativeCount;
        this.imagesCount = imagesCount;
        this.tag = tag;
        this.aDefault = aDefault;
        this.receiveTime = receiveTime;
        this.settingShoppingCopyUrl = settingShoppingCopyUrl;
        this.shoppingCopy = shoppingCopy;
        this.certifiedCopyUrl = certifiedCopyUrl;
        this.certifiedCopy = certifiedCopy;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

    public Page<Review> getReview() {
        return review;
    }

    public void setReview(Page<Review> review) {
        this.review = review;
    }

    public Long getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(Long reviewCount) {
        this.reviewCount = reviewCount;
    }

    public Long getPositiveCount() {
        return positiveCount;
    }

    public void setPositiveCount(Long positiveCount) {
        this.positiveCount = positiveCount;
    }

    public Long getModerateCount() {
        return moderateCount;
    }

    public void setModerateCount(Long moderateCount) {
        this.moderateCount = moderateCount;
    }

    public Long getNegativeCount() {
        return negativeCount;
    }

    public void setNegativeCount(Long negativeCount) {
        this.negativeCount = negativeCount;
    }

    public Long getImagesCount() {
        return imagesCount;
    }

    public void setImagesCount(Long imagesCount) {
        this.imagesCount = imagesCount;
    }

    public List<Tag> getTag() {
        return tag;
    }

    public void setTag(List<Tag> tag) {
        this.tag = tag;
    }

    public Receiver getaDefault() {
        return aDefault;
    }

    public void setaDefault(Receiver aDefault) {
        this.aDefault = aDefault;
    }

    public String getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(String receiveTime) {
        this.receiveTime = receiveTime;
    }

    public String getSettingShoppingCopyUrl() {
        return settingShoppingCopyUrl;
    }

    public void setSettingShoppingCopyUrl(String settingShoppingCopyUrl) {
        this.settingShoppingCopyUrl = settingShoppingCopyUrl;
    }

    public String getShoppingCopy() {
        return shoppingCopy;
    }

    public void setShoppingCopy(String shoppingCopy) {
        this.shoppingCopy = shoppingCopy;
    }

    public String getCertifiedCopyUrl() {
        return certifiedCopyUrl;
    }

    public void setCertifiedCopyUrl(String certifiedCopyUrl) {
        this.certifiedCopyUrl = certifiedCopyUrl;
    }

    public String getCertifiedCopy() {
        return certifiedCopy;
    }

    public void setCertifiedCopy(String certifiedCopy) {
        this.certifiedCopy = certifiedCopy;
    }
}
