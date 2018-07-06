package com.wtshop.controller.admin;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Page;
import com.wtshop.Pageable;
import com.wtshop.model.Goods;
import com.wtshop.model.Member;
import com.wtshop.model.MemberFavoriteGoods;
import com.wtshop.service.*;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static com.wtshop.controller.wap.BaseController.convertToLong;

/**
 * Created by 蔺哲 on 2017/8/17.
 */
@ControllerBind(controllerKey = "/admin/userFollow")
public class UserFollowController extends BaseController{
    private MemberService memberService = enhance(MemberService.class);
    private MemberAttributeService memberAttributeService = enhance(MemberAttributeService.class);
    private MemberFavoriteGoodsService memberFavoriteGoodsService = enhance(MemberFavoriteGoodsService.class);
    private GoodsService goodsService = enhance(GoodsService.class);
    /**
     * 列表
     */
    public void list() {
        Pageable pageable = getBean(Pageable.class);
        Page<Member> pages = memberService.findPage(pageable);
        setAttr("memberAttributes", memberAttributeService.findAll());
        setAttr("pageable", pageable);
        setAttr("page", pages );
        render("/admin/userFollow/list.ftl");
    }

    /**
     * 用户相关收藏商品
     */
    public void detailsList(){
        Pageable pageable = getBean(Pageable.class);
        Long memberId = getParaToLong("memberId");
        setAttr("memberId",memberId);
        setAttr("page",memberFavoriteGoodsService.findFavoriteGoodsByMember(pageable,memberId));
        setAttr("pageable", pageable );
        render("/admin/userFollow/detailsList.ftl");
    }

    /**
     * 商品维度列表
     */
    public  void goodsDimension(){
        Pageable pageable = getBean(Pageable.class);
        Page<Goods> pages = memberFavoriteGoodsService.queryGoodsOfMemberNum(pageable);
        setAttr("pageable", pageable);
        setAttr("page", pages );
        render("/admin/userFollow/goodsList.ftl");
    }

    /**
     * 商品对应用户关注详情
     */
    public void goodsDetailsList(){
        Pageable pageable = getBean(Pageable.class);
        Long goodsId = getParaToLong("goodsId");
        List<Long> goodsList = new ArrayList<Long>();
        Page<MemberFavoriteGoods> pages = memberFavoriteGoodsService.findGoodsByMember(pageable,goodsId);
        for(MemberFavoriteGoods MemberFavoriteGoods : pages.getList()){
            goodsList.add(MemberFavoriteGoods.getFavoriteMembers());
        }
        setAttr("goodsList",goodsList);
        setAttr("goodsId",goodsId);
        setAttr("pageable", pageable);
        setAttr("page", pages );
        render("/admin/userFollow/goodsDetailsList.ftl");
    }

}
