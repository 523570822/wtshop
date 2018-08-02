package com.wtshop.api.common.result;

import com.jfinal.plugin.activerecord.Page;
import com.wtshop.model.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by sq on 2017/5/16.
 */
@ApiModel(value = "货物详情", description = "GoodsMessageResult")
public class GoodsMessageResult  implements Serializable {

    private Integer sotck ;

    @ApiModelProperty(value = "货物", dataType = "Goods")
    private Goods goods;

    @ApiModelProperty(value = "名称", dataType = "String")
    private String title;

    @ApiModelProperty(value = "是否关注", dataType = "Boolean")
    private Boolean favorite;

    @ApiModelProperty(value = "咨询分页", dataType = "Consultation")
    private Page<Consultation> consultationPages;

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

    private List<Product> productList;

    private String settingShoppingCopyUrl;

    private String certifiedCopyUrl;

    private String taxExplainUrl;

    private Receiver aDefault;

    private String receiveTime;
    //喵币
    private BigDecimal miaobi;
    //钱
    private BigDecimal price;
    //包邮
    private String freeMoney;

    public String getFreeMoney() {
        return freeMoney;
    }

    public void setFreeMoney(String freeMoney) {
        this.freeMoney = freeMoney;
    }

    public GoodsMessageResult(Integer sotck, Goods goods, String title, Boolean favorite, Page<Consultation> consultationPages, Page<Review> review, Long reviewCount, Long positiveCount, Long moderateCount, Long negativeCount, Long imagesCount, List<Tag> tag, List<Product> productList, String settingShoppingCopyUrl, String certifiedCopyUrl, String taxExplainUrl, Receiver aDefault, String receiveTime, String freeMoney) {
        this.sotck = sotck;
        this.goods = goods;
        this.title = title;
        this.favorite = favorite;
        this.consultationPages = consultationPages;
        this.review = review;
        this.reviewCount = reviewCount;
        this.positiveCount = positiveCount;
        this.moderateCount = moderateCount;
        this.negativeCount = negativeCount;
        this.imagesCount = imagesCount;
        this.tag = tag;
        this.productList = productList;
        this.settingShoppingCopyUrl = settingShoppingCopyUrl;
        this.certifiedCopyUrl = certifiedCopyUrl;
        this.taxExplainUrl = taxExplainUrl;
        this.aDefault = aDefault;
        this.receiveTime = receiveTime;
        this.freeMoney = freeMoney;
    }

    public BigDecimal getMiaobi() {
        return miaobi;
    }

    public void setMiaobi(BigDecimal miaobi) {
        this.miaobi = miaobi;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getSotck() {
        return sotck;
    }

    public void setSotck(Integer sotck) {
        this.sotck = sotck;
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

    public String getCertifiedCopyUrl() {
        return certifiedCopyUrl;
    }

    public void setCertifiedCopyUrl(String certifiedCopyUrl) {
        this.certifiedCopyUrl = certifiedCopyUrl;
    }

    public String getTaxExplainUrl() {
        return taxExplainUrl;
    }

    public void setTaxExplainUrl(String taxExplainUrl) {
        this.taxExplainUrl = taxExplainUrl;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

    public Page<Consultation> getConsultationPages() {
        return consultationPages;
    }

    public void setConsultationPages(Page<Consultation> consultationPages) {
        this.consultationPages = consultationPages;
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

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }
}
