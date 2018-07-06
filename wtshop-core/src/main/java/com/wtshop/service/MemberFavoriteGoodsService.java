package com.wtshop.service;

import com.jfinal.aop.Enhancer;
import com.jfinal.plugin.activerecord.Page;
import com.wtshop.Pageable;
import com.wtshop.dao.MemberFavoriteGoodsDao;
import com.wtshop.model.Goods;
import com.wtshop.model.Member;
import com.wtshop.model.MemberFavoriteGoods;

/**
 * Created by 蔺哲 on 2017/8/17.
 */
public class MemberFavoriteGoodsService extends BaseService<MemberFavoriteGoods> {
    public MemberFavoriteGoodsService(){super(MemberFavoriteGoods.class);}
    private MemberFavoriteGoodsDao memberFavoriteGoodsDao = Enhancer.enhance(MemberFavoriteGoodsDao.class);

    public Page queryGoodsOfMemberNum(Pageable pageable){
        return memberFavoriteGoodsDao.queryGoodsOfMemberNum(pageable);
    }
    //分页查询会员相关收藏商品
    public Page<MemberFavoriteGoods> findFavoriteGoodsByMember(Pageable pageable,Long memberId){
        return memberFavoriteGoodsDao.findFavoriteGoodsByMember(pageable,memberId);
    }
    //分页查询商品被关注的会员
    public Page<MemberFavoriteGoods> findGoodsByMember(Pageable pageable, Long goodsId){
        return memberFavoriteGoodsDao.findGoodsByMember(pageable,goodsId);
    }

    /**
     * 查询会员是否已经关注过此商品
     */

    public MemberFavoriteGoods findGoods(Long memberId, Long goodsId){
        return memberFavoriteGoodsDao.findGoods(memberId, goodsId);
    }

}
