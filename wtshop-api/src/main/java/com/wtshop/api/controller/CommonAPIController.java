package com.wtshop.api.controller;


import com.jfinal.ext.plugin.monogodb.MongoKit;
import com.jfinal.ext.route.ControllerBind;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.wtshop.RequestContextHolder;
import com.wtshop.api.common.result.IndexResult;
import com.wtshop.model.Ad;
import com.wtshop.model.Goods;
import com.wtshop.model.Member;
import com.wtshop.service.AdService;
import com.wtshop.service.GoodsService;
import com.wtshop.service.MemberService;
import com.wtshop.util.ApiResult;
import com.wtshop.util.ObjectUtils;
import com.wtshop.util.RedisUtil;
import freemarker.log.Logger;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@ControllerBind(controllerKey = "/api")
public class CommonAPIController extends BaseAPIController {

    private AdService adService = enhance(AdService.class);
    private GoodsService goodsService = enhance(GoodsService.class);
    private MemberService memberService = enhance(MemberService.class);

    /**
     * 首页
     */
    public void index() {
        Logger logger = Logger.getLogger("index");
        List<Ad> vipList = new ArrayList<>();
        String token = getPara("token");
        if (StringUtils.isNotBlank(token)) {
            String userId = RedisUtil.getString(token);
            if (StringUtils.isNotEmpty(userId)) {
                Member member = memberService.find((long) Integer.parseInt(userId));
                if (member != null) {

                    /**
                     *  判断是否升级白金及以上
                     */
                    if(member.getHousekeeperId()==3){
                        List<Member> ddd = memberService.findMemberByLinkShare(member.getShareCode());
                        if(ddd.size()>=600){
                            member.setHousekeeperId(4l);
                            memberService.update(member);
                        }
                    }else if(member.getHousekeeperId()==4){
                        //升级钻石
                        List<Member> ddd = memberService.findMemberByLinkShare(member.getShareCode(),4l);
                        if(ddd.size()>=1){
                            member.setHousekeeperId(5l);
                            memberService.update(member);
                        }
                    }


                    BasicDBObject basicDBObject = new BasicDBObject();
                    basicDBObject.put("phone", member.getPhone());
                    basicDBObject.put("state", true);
                    DBObject vip = MongoKit.getCollection("vipMember").findOne(basicDBObject);
                    if (!ObjectUtils.isEmpty(vip)) {
                        vipList = adService.findAdList(11L);
                    }
                }
            }
        }

        //首页顶部广告位
        List<Ad> adList = adService.findAdList(7L);
        List<Ad> fudaiList = adService.findAdList(8L);
        List<Ad> shouYeList = adService.findAdList(16L);
        //商品倒拍
        Long id = null;
//        long currActId = ReverseScan.getCurrActId();
//        if((Long)currActId == null){
//            id = ReverseScan.getNextActId();
//        }else{
//            id = currActId;
//        }
        List<Goods> goodsList = goodsService.findGoodsByReverseId(id);

        //新品推荐
        List<Goods> newGoodsList = goodsService.findNewGoodsList();
        List<Ad> newGoodsAdList = adService.findAdList(13L);

        //个性推荐
        List<Goods> charactersList = goodsService.findCharactersList();
        List<Ad> charactersAdList = adService.findAdList(12L);


        IndexResult indexResult = new IndexResult(adList, fudaiList, vipList, goodsList, newGoodsList, charactersList, newGoodsAdList, charactersAdList,shouYeList);
        renderJson(ApiResult.success(indexResult));
    }

}
