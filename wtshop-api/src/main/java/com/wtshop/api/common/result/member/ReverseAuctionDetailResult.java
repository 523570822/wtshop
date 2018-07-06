package com.wtshop.api.common.result.member;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/9 0009.
 */
public class ReverseAuctionDetailResult implements Serializable {

    private String detailId;
    private String productId;
    private String title;
    private Long buyPrice;
    private Long auctionOriginalPrice;
    private String image;
    private String buyUsername;

    public ReverseAuctionDetailResult() {
    }

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getAuctionOriginalPrice() {
        return auctionOriginalPrice;
    }

    public void setAuctionOriginalPrice(Long auctionOriginalPrice) {
        this.auctionOriginalPrice = auctionOriginalPrice;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBuyUsername() {
        return buyUsername;
    }

    public void setBuyUsername(String buyUsername) {
        this.buyUsername = buyUsername;
    }

    public Long getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(Long buyPrice) {
        this.buyPrice = buyPrice;
    }
}
