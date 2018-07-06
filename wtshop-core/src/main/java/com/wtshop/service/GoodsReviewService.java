package com.wtshop.service;

import com.jfinal.aop.Enhancer;
import com.jfinal.plugin.activerecord.Page;
import com.wtshop.Pageable;
import com.wtshop.dao.GoodsDao;
import com.wtshop.dao.GoodsReviewDao;
import com.wtshop.model.*;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by sq on 2017/9/5.
 */
public class GoodsReviewService extends BaseService<GoodsReview> {

    private GoodsReviewDao goodsReviewDao = Enhancer.enhance(GoodsReviewDao.class);

    public GoodsReviewService() {
        super(GoodsReview.class);
    }

    public Page<GoodsReview> findGoodsReviewPage(Pageable pageable) {
        return goodsReviewDao.findPage(pageable);
    }

}
