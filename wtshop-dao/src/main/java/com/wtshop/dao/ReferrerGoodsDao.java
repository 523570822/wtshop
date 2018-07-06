package com.wtshop.dao;

import com.jfinal.plugin.activerecord.Page;
import com.wtshop.Pageable;
import com.wtshop.model.ReferrerGoods;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by 蔺哲 on 2017/9/5.
 */
public class ReferrerGoodsDao extends BaseDao<ReferrerGoods> {
    public ReferrerGoodsDao (){super(ReferrerGoods.class);}

    /**
     * 分页查询单个商品统计记录
     * @param page
     * @return
     */
    public Page queryPageByGoodsId(Pageable page,Long goodsId){
        String select = "SELECT m.phone AS memberPhone,sf.phone as staffPhone,rg.create_date,m.id AS mid,m.`name` AS memberName,sf.id AS staffid,sf.`name` AS staffName," +
                "g.id AS goodsId,g.`name` AS goodsName";
        String from = "from referrer_goods rg\n" +
                "    LEFT JOIN member m ON m.id=rg.member_id\n" +
                "    LEFT JOIN member sf ON sf.id=rg.staff_id\n" +
                "    LEFT JOIN goods g ON g.id=rg.goods_id\n" +
                "\t\tWHERE  rg.goods_id="+goodsId;
        return super.findPages(select,from,page);
    }
    /**
     * 分页查询商品统计记录
     * @param
     * @return
     */
    public Page queryByPage(Pageable pageable){
        String select = "SELECT g.id AS id,nn.num,g.`name`,p.stock";
        String from = " FROM goods g " +
                "       LEFT JOIN (SELECT count(*) AS num,rg.goods_id FROM referrer_goods rg GROUP BY rg.goods_id) nn ON nn.goods_id=g.id"
              +"  LEFT JOIN (SELECT pp.* FROM product pp WHERE pp.goods_id=goods_id ) p ON p.goods_id=g.id WHERE 1=1";
        String order = pageable.getOrderProperty();
        if(StringUtils.isEmpty(order)){
            pageable.setOrderProperty("num");
            pageable.setOrderDirection("desc");
        }
        return super.findPages(select,from,pageable);
    }

    /**
     * 分页查询单个客户推荐记录
     * @param
     * @return
     */
    public Page queryReferrerGoods(int pageNum,int pageSize,Long memberId){
        String select = "SELECT g.id,r.create_date,g.image,g.`name`,g.price,g.market_price ";
        String from = "  FROM referrer_goods r LEFT JOIN goods g ON g.id=r.goods_id WHERE member_id="+memberId;
        from +=" ORDER BY r.create_date DESC";
        return modelManager.paginate(pageNum,pageSize,select,from);
    }

    /**
     * 查询单个记录
     * @param staffId
     * @param userId
     * @param goodsId
     * @return
     */
    public ReferrerGoods findOne(Long staffId,Long userId,Long goodsId){
        String sql = "SELECT * FROM referrer_goods r WHERE r.goods_id="+goodsId+" AND r.staff_id="+staffId+" AND r.member_id="+userId;
        sql +=" ORDER BY r.create_date DESC";
        return modelManager.findFirst(sql);
    }
}
