package com.wtshop.api.controller;

import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Page;
import com.wtshop.Pageable;
import com.wtshop.api.interceptor.TokenInterceptor;
import com.wtshop.util.ApiResult;
import com.wtshop.api.common.result.GoodsThemeResult;
import com.wtshop.api.common.result.Theme;
import com.wtshop.api.common.result.ThemeResult;
import com.wtshop.api.interceptor.ErrorInterceptor;
import com.wtshop.interceptor.WapInterceptor;
import com.wtshop.model.Ad;
import com.wtshop.model.Goods;
import com.wtshop.model.GoodsTheme;
import com.wtshop.model.ThemeProduct;
import com.wtshop.service.AdService;
import com.wtshop.service.GoodsService;
import com.wtshop.service.GoodsThemeService;
import com.wtshop.service.ThemeProductService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sq on 2017/5/24.
 */
@ControllerBind(controllerKey = "/api/theme")
@Before({WapInterceptor.class, ErrorInterceptor.class, TokenInterceptor.class})
public class GoodsThemeAPIController extends BaseAPIController  {

    /** 每页记录数 */
    private static final int PAGE_SIZE = 10;

    private GoodsThemeService goodsThemeService = enhance(GoodsThemeService.class);
    private ThemeProductService themeProductService = enhance(ThemeProductService.class);
    private GoodsService goodsService = enhance(GoodsService.class);
    private AdService adService = enhance(AdService.class);

    /**
     * 主页
     */
    public void index(){
        Integer pageNumber = getParaToInt("pageNumbers");
        // id=243 美容   id=244 护发
        Long id = getParaToLong("id");
        Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
        Page<GoodsTheme> page = goodsThemeService.findPages(pageable,id);
        List<Theme> lists = new ArrayList<Theme>();
        List<GoodsTheme> goodsThemes = page.getList();
        for(GoodsTheme goodsTheme : goodsThemes){
            List<Goods> list = new ArrayList<Goods>();
            List<ThemeProduct> themeProductList = themeProductService.queryByGoodsThemeId(goodsTheme.getId());
            for(ThemeProduct themeProduct:themeProductList){
                Goods goods = goodsService.findGoodsByThemeId(themeProduct.getId());
                list.add(goods);
            }
            //
            Theme theme = new Theme(goodsTheme,list);
            lists.add(theme);
        }
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("PageNumber",page.getPageNumber());
        hashMap.put("TotalPage",page.getTotalPage());
        hashMap.put("TotalRow",page.getTotalRow());
        hashMap.put("PageSize",page.getPageSize());
        hashMap.put("lists",lists);
        List<Ad> adList =new ArrayList<>();
        if( 244 == id){
            adList = adService.findAdList(10L);
        }else{
            adList = adService.findAdList(9L);
        }
        GoodsThemeResult goodsThemeResult = new GoodsThemeResult(adList,hashMap );
        renderJson(ApiResult.success(goodsThemeResult));
    }

    /**
     * 根据专题获取商品信息
     * {"goodsTheme":{"create_date":"2017-05-23 14:43:10","id":9,"imgPath":null,"modify_date":"2017-05-24 10:15:35","orders":1,"product_category_id":243,"title":"天然的才是最好的","version":1},"themeProductList":[{"create_date":"2017-05-24 10:17:13","goodsTheme_id":9,"id":14,"modify_date":"2017-05-24 10:17:13","orders":1,"product_id":88,"version":0},{"create_date":"2017-05-24 10:17:13","goodsTheme_id":9,"id":15,"modify_date":"2017-05-24 10:17:13","orders":2,"product_id":87,"version":0}]}
     */
    public void list(){
        Long themeId = getParaToLong("themeId");
        GoodsTheme goodsTheme = goodsThemeService.find(themeId);
        List<Goods> goods = goodsService.findGoodsByGoodsThemeId(themeId);

        ThemeResult goodsThemeResult = new ThemeResult(goodsTheme, goods);
        renderJson(ApiResult.success(goodsThemeResult));

    }





}
