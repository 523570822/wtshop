package com.wtshop.api.common.result.member;

import com.wtshop.model.ReverseAuctionDetail;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/9 0009.
 */
public class ReverseAuctionResult implements Serializable {

    private Long auctionId;
    private String title;
    private String downRule;
    private List<ReverseAuctionDetailResult> detailResultList;

    public ReverseAuctionResult() {
        this.detailResultList = new ArrayList<>();
    }

    public Long getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(Long auctionId) {
        this.auctionId = auctionId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDownRule() {
        return downRule;
    }

    public void setDownRule(String downRule) {
        this.downRule = downRule;
    }

    public List<ReverseAuctionDetailResult> getDetailResultList() {
        return detailResultList;
    }

    public void setDetailResultList(List<ReverseAuctionDetailResult> detailResultList) {
        this.detailResultList = detailResultList;
    }
}
