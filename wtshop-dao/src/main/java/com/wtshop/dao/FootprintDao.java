package com.wtshop.dao;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.wtshop.Pageable;
import com.wtshop.model.Footprint;
import com.wtshop.util.DateUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/8/17.
 */
public class FootprintDao extends BaseDao<Footprint> {
    public FootprintDao() {
        super(Footprint.class);
    }

    /**
     * 查询我的足迹
     * 时间排序
     *
     * @param memberId
     * @return
     */
    public List<Footprint> queryMyFootPrint(Long memberId) {
        String sql = "SELECT g.image,f.id,f.goodsId,f.create_date,g.price,g.market_price,g.`name`";
        sql += " FROM footprint f LEFT JOIN goods g ON f.goodsId=g.id WHERE memberId=" + memberId;
        sql += " ORDER BY f.create_date DESC";
        return modelManager.find(sql);
    }

    public Long findByTime(Date endTime, Long goodsId, Long memberId) {
        String sql = "SELECT count(*) FROM footprint WHERE goodsId=" + goodsId + " AND memberId=" + memberId + " AND create_date BETWEEN '" + DateUtils.getDateTime(endTime) + "' AND '" + DateUtils.getDateTime(new Date()) + "'";
        return Db.queryLong(sql);
    }

    /**
     * 用户足迹详情
     */
    public Page<Footprint> getUserDetailsFootPrint(String startTime, String endTime, Pageable pageable, Long uid) {

        String select = " select g.name, f.create_date, f.goodsId, m.`username`, m.`nickname`, m.`phone` ";
        String sql = " FROM footprint f , member m , goods g WHERE f.`memberId` = m.id AND f.goodsId = g.id ";
        if (startTime != null) {
            sql += " and f.create_date >= '" + startTime + "'";
        }
        if (endTime != null) {
            sql += " and f.create_date <= '" + endTime + "'";
        }
        if (uid != null) {
            sql += " and f.memberId = " + uid;
        }
        return modelManager.paginate(pageable.getPageNumber(), pageable.getPageSize(), select, sql);

    }


    /**
     * 商品详情
     */
    public Page<Footprint> getGoodsrDetailsFootPrint(String startTime, String endTime, Pageable pageable, Long goodsId, String phone, String nickname) {

        String select = " select m.nickname, m.phone,m.username, f.create_date, f.memberId ";
        String sql = " from footprint f left join member m on f.memberId = m.id where 1 = 1";
        if (startTime != null) {
            sql += " and f.create_date >= '" + startTime + "'";
        }
        if (endTime != null) {
            sql += " and f.create_date <= '" + endTime + "'";
        }
        if (goodsId != null) {
            sql += " and f.goodsid = " + goodsId;
        }
        if (phone != null) {
            sql += " and m.phone like '%" + phone + "%'";
        }
        if (nickname != null) {
            sql += " and m.nickname like '%" + nickname + "%'";
        }

        return modelManager.paginate(pageable.getPageNumber(), pageable.getPageSize(), select, sql);

    }
}
