package com.wtshop.dao;

import com.wtshop.model.ReverseAuctionHistroy;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by Administrator on 2017/6/5.
 */
public class ReverseAuctionHistroyDao extends BaseDao<ReverseAuctionHistroy> {
    /**
     * 构造方法
     *
     * @param entityClass
     */
    public ReverseAuctionHistroyDao(Class<ReverseAuctionHistroy> entityClass) {
        super(entityClass);
    }

    public ReverseAuctionHistroyDao() {
        super(ReverseAuctionHistroy.class);
    }


    public ReverseAuctionHistroy findByReverseAuctionDetailId(String reverse_auction_detail_id) {
        if (StringUtils.isEmpty(reverse_auction_detail_id)) {
            return null;
        }
        String sql = "SELECT * FROM `reverse_auction_histroy` WHERE reverse_auction_detail_id = ? order by create_date desc";
        try {
            return modelManager.findFirst(sql, reverse_auction_detail_id);
        } catch (Exception e) {
            return null;
        }
    }


}
