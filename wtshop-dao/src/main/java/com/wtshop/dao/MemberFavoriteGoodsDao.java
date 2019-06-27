
package com.wtshop.dao;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.wtshop.Pageable;
import com.wtshop.model.MemberFavoriteGoods;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/7/24.
 */
public class MemberFavoriteGoodsDao  extends BaseDao<MemberFavoriteGoods>  {
    public MemberFavoriteGoodsDao() {
        super(MemberFavoriteGoods.class);
    }


    /**
     *查询会员是否已经关注过此商品
     */
    public MemberFavoriteGoods findGoods(Long memberId, Long gooosId,Long sPecialId){
        String sql = " SELECT * FROM member_favorite_goods WHERE 1 = 1  ";
        if(memberId != null){
            sql += " and favorite_members =" + memberId;
        }
        if(gooosId != null){
            sql += " and favorite_goods =" + gooosId;
        }
        if(sPecialId != null){
            sql += " and favorite_special =" + sPecialId;
        }
        return modelManager.findFirst(sql);
    }


    /**
     * 根据商品 id 获取会员
     */

    public List<Long> findMemberList(Long goodsId){
        if(goodsId == null){
            return null;
        }else {
            String sql = "SELECT favorite_members FROM member_favorite_goods WHERE 1 = 1 AND favorite_goods = " + goodsId;
            return Db.query(sql);
        }
    }


    /**
     * 根据会员id 获取对象
     * @param memberId
     * @return
     */
    public List<Long> findGoodsByMemberId(Long memberId){
        if(memberId == null){
            return null;
        }else {
            String sql = "SELECT favorite_goods FROM member_favorite_goods WHERE 1 = 1 AND favorite_members = " + memberId;
            return Db.query(sql);
        }
    }

    /**
     *根据会员出收藏商品
     * @param pageable
     * @return
     */
    public Page findFavoriteGoodsByMember(Pageable pageable,Long memberId){
        String select = "SELECT mfg.create_date,g.`name`,p.stock,g.sn,g.id";
        String from = "FROM member_favorite_goods mfg LEFT JOIN goods g ON mfg.favorite_goods=g.id LEFT JOIN (SELECT pp.* FROM product pp WHERE pp.goods_id=goods_id ) p ON p.goods_id=g.id";
        from+=" WHERE mfg.favorite_members="+memberId;
        return super.findPages(select,from,pageable);
    }
    /**
     *显示商品 对应用户的关注数量
     * @param pageable
     * @return
     */
    public Page queryGoodsOfMemberNum(Pageable pageable){
        String select = "SELECT g.name,g.id,tt.favoriteNum,p.stock ";
        String from = "FROM goods g LEFT JOIN (SELECT count(*) AS favoriteNum,member_favorite_goods.favorite_goods FROM member_favorite_goods WHERE 1=1 GROUP BY member_favorite_goods.favorite_goods) tt ON\n" +
                "g.id=tt.favorite_goods LEFT JOIN (SELECT pp.* FROM product pp WHERE pp.goods_id=goods_id ) p ON g.id=p.goods_id";
        from+=" WHERE 1=1";
        String order = pageable.getOrderProperty();
        if(StringUtils.isEmpty(order)){
            pageable.setOrderProperty("favoriteNum");
            pageable.setOrderDirection("desc");
        }
        return super.findPages(select,from,pageable);
    }
    /**
     *根据商品出对应关注的会员
     * @param pageable
     * @return
     */
    public Page findGoodsByMember(Pageable pageable,Long goodsId){
        String select = "SELECT m.id,m.phone,m.nickname,mfg.create_date,mfg.favorite_members";
        String from = "from member_favorite_goods mfg LEFT JOIN goods g ON mfg.favorite_goods=g.id LEFT JOIN member m ON m.id=mfg.favorite_members";
        from+=" WHERE g.id="+goodsId;
        return super.findPages(select,from,pageable);
    }
    /**
     * 根据会员id 获取对象
     * @param memberId
     * @return
     */
    public MemberFavoriteGoods findGoodsByGoodsId(Long goodsId ,Long memberId){
        if(memberId == null && goodsId == null){
            return null;
        }else {
            String sql = "SELECT * FROM member_favorite_goods WHERE 1 = 1 AND favorite_members = " + memberId;
            if(goodsId != null){
                sql += " AND favorite_goods = "+ goodsId;
            }
            return modelManager.findFirst(sql);
        }
    }
}
