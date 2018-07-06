package com.wtshop.dao;

import com.wtshop.model.ReverseAuctionDetail;

import java.util.List;

/**
 * Created by 蔺哲 on 2017/5/15.
 */
public class ReverseAuctionDetailDao extends BaseDao<ReverseAuctionDetail> {
    public ReverseAuctionDetailDao(){super(ReverseAuctionDetail.class);}

    /**
     * 根据倒拍id 查 detail表
     * @param reverseauctionId
     * @return
     */
    public List<ReverseAuctionDetail> queryByReverseauctionId(Long reverseauctionId){
        String sql = "select * from reverse_auction_detail where reverse_auction_id = "+reverseauctionId;
        return modelManager.find(sql);
    }
}
