package com.wtshop.service;

import com.wtshop.dao.ThemeProductDao;
import com.wtshop.model.ThemeProduct;
import com.wtshop.model.base.BaseThemeProduct;

import java.util.List;

import static com.jfinal.aop.Enhancer.enhance;

/**
 * Created by sq on 2017/5/16.
 */
public class ThemeProductService extends BaseService<ThemeProduct>{

    private ThemeProductDao themeProductDao = enhance(ThemeProductDao.class);

    public ThemeProductService(){super(ThemeProduct.class);}


    /**
     * 根据主题id查对应商品
     */
    public List<ThemeProduct> queryByGoodsThemeId(Long id){
        return themeProductDao.queryListByGoodsThemeId(id);
    }
}
