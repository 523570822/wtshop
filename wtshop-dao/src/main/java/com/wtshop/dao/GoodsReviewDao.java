package com.wtshop.dao;

import com.jfinal.plugin.activerecord.Page;
import com.wtshop.Pageable;
import com.wtshop.model.GoodsReview;

/**
 * Created by Administrator on 2017/8/30.
 */
public class GoodsReviewDao extends BaseDao<GoodsReview> {
    public GoodsReviewDao() {
        super(GoodsReview.class);
    }

    public Page<GoodsReview> findGoodsReviewPage(Pageable pageable) {
        return null;
    }
}
