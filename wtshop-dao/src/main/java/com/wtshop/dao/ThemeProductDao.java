package com.wtshop.dao;

import com.wtshop.Pageable;
import com.wtshop.model.GoodsTheme;
import com.wtshop.model.ThemeProduct;
//import com.sun.tools.internal.ws.resources.ModelMessages;

import java.util.List;

/**
 * Created by sq on 2017/5/16.
 */
public class ThemeProductDao extends  BaseDao<ThemeProduct> {
    public ThemeProductDao (){super(ThemeProduct.class);}
    /**
     * 根据主题查对应商品
     */
    public List<ThemeProduct> queryListByGoodsThemeId(Long id){
        String sql = "select * from theme_product where goodsTheme_id = " + id;
        return ThemeProduct.dao.find(sql);
    }
    public List queryListByGoodsThemeIdForApi(Long id){
        String sql = "SELECT * from theme_product t LEFT JOIN product p ON t.product_id=p.id WHERE t.id=14";
        //List list = ThemeProduct.dao.find(sql);
        List list = modelManager.find(sql);
        return list;
    }
}
