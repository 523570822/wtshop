package com.wtshop.api.common.result.member;

import com.wtshop.model.Review;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created by sq on 2017/5/17.
 */

@ApiModel(value = "评论查询", description = "ReviewFindResult")
public class ReviewFindResult implements Serializable{

    @ApiModelProperty(value = "评论", dataType = "Review")
    private Review review;

    public ReviewFindResult(Review review) {
        this.review = review;
    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }
}
