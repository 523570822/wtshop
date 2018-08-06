package com.wtshop.api.controller;

import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Page;
import com.wtshop.Pageable;
import com.wtshop.api.interceptor.TokenInterceptor;
import com.wtshop.model.*;
import com.wtshop.util.ApiResult;
import com.wtshop.api.interceptor.ErrorInterceptor;
import com.wtshop.interceptor.WapInterceptor;
import com.wtshop.service.*;

import java.util.*;

/**
 * Created by sq on 2017/6/8.
 */
@ControllerBind(controllerKey = "/api/activity")
@Before({WapInterceptor.class, ErrorInterceptor.class} )
public class ActivityAPIController extends  BaseAPIController{

    private GoodsService goodsService = enhance(GoodsService.class);

    private FuDaiProductService productService = enhance(FuDaiProductService.class);

    /**
     * 猜你喜欢
     */
    public void likeList(){

        List<Goods> likeList = goodsService.findLikeList();
        renderJson(ApiResult.success(likeList));
    }

    public void manActivity(){
        Integer pageNumber = getParaToInt("pageNumbers", 1);
        Integer pageSize =  999999 ;
        Pageable pageable = new Pageable(pageNumber, pageSize);
        Page<Goods> goodsByPromId = goodsService.findGoodsByPromId(5L,pageable);
        renderJson(ApiResult.success(goodsByPromId));
    }

    public void testAct(){

        productService.lotteryProduct(5,162,13);


    }
    /**
     *点击抽奖接口
     * status（0：成功，1：失败，没有次数）
     * Ranking（名次）
      */
    public void lottery() {
        Map<String, String> map = new HashMap<String, String>();

        int max = 8;
        int min = 1;
        Random random = new Random();
        int s = random.nextInt(max)%(max-min+1) + min;
        map.put("Ranking",s+"");
        map.put("status","0");
        String msg="抽取成功";
        renderJson(ApiResult.success(map,msg));


    }
}
