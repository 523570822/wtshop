package com.wtshop.dao;

import com.wtshop.Pageable;
import com.wtshop.model.ReverseAuction;

import java.util.List;
import java.util.Map;

/**
 * Created by sq on 2017/6/14.
 */
public class ReverseAuctionDao extends BaseDao<ReverseAuction> {

    public ReverseAuctionDao() {
        super(ReverseAuction.class);
    }


    public List<Map> queryReverseAuctionDetailMapList(Long auctionId){
        String sql = "select d.*, g.`name`, g.`image` from `reverse_auction_detail` d , product p, goods g  where d.`reverse_auction_id` = 1  and d.`product_id` = p.`id`  and p.`goods_id` = g.id";
        return null;
    }

}
