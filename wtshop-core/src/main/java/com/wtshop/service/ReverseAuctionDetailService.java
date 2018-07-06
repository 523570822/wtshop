package com.wtshop.service;

import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;
import com.wtshop.dao.ReverseAuctionDetailDao;
import com.wtshop.model.ReverseAuctionDetail;
import com.wtshop.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static com.jfinal.aop.Enhancer.enhance;


/**
 * Created by Lin on 2017/5/14.
 */
public class ReverseAuctionDetailService extends BaseService<ReverseAuctionDetail> {

    public ReverseAuctionDetailService() {
        super(ReverseAuctionDetail.class);
    }

    private ReverseAuctionDetailDao reverseAuctionDetailDao = enhance(ReverseAuctionDetailDao.class);

    /**
     * 保存和修改
     *
     * @param newList
     * @param reverseauctionId
     */
    public void updateList(List<ReverseAuctionDetail> newList, Long reverseauctionId) {
        List<ReverseAuctionDetail> oldList = reverseAuctionDetailDao.queryByReverseauctionId(reverseauctionId);
        List<Long> updateIdList = new ArrayList<Long>();
        //保存新增
        for (ReverseAuctionDetail detail : newList) {
            if (!StringUtils.isEmpty(detail.getId())) {
                updateIdList.add(detail.getId());
            } else {
                save(detail);
            }
        }
        //删除前台删除
        for (ReverseAuctionDetail detail : oldList) {
            if (!updateIdList.contains(detail.getId())) {
                delete(detail.getId());
            }
        }
        //修改前台修改
        for (ReverseAuctionDetail updateDetail : oldList) {
            for (ReverseAuctionDetail detail : newList) {
                if (detail.getId() == updateDetail.getId()) {
                    updateDetail.setCompleteNum(detail.getCompleteNum());
                    updateDetail.setAuctionOriginalPrice(detail.getAuctionOriginalPrice());
                    updateDetail.setAuctionStartPrice(detail.getAuctionStartPrice());
                    updateDetail.setAuctionLimitUpPrice(detail.getAuctionLimitUpPrice());
                    updateDetail.setAuctionLimitDownPrice(detail.getAuctionLimitDownPrice());
                    updateDetail.setTotalNum(detail.getTotalNum());
                    update(updateDetail);
                }
            }
        }
    }


    /**
     * 根据倒拍的id 获取商品信息
     */
    public List<ReverseAuctionDetail> findProductList(Long id) {
        return reverseAuctionDetailDao.queryByReverseauctionId(id);
    }
}
