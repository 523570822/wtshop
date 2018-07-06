package com.wtshop.service;

import com.jfinal.plugin.activerecord.Page;
import com.wtshop.Pageable;
import com.wtshop.dao.GoodsThemeDao;
import com.wtshop.dao.ThemeProductDao;
import com.wtshop.model.GoodsTheme;
import com.wtshop.model.ThemeProduct;
import com.wtshop.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static com.jfinal.aop.Enhancer.enhance;
import static com.xiaoleilu.hutool.json.XMLTokener.entity;

/**
 * Created by 蔺哲 on 2017/5/16.
 */
public class GoodsThemeService extends BaseService<GoodsTheme>{


    private ThemeProductDao themeProductDao = enhance(ThemeProductDao.class);

    private GoodsThemeDao goodsThemeDao = enhance(GoodsThemeDao.class);

    public GoodsThemeService(){super(GoodsTheme.class);}

    /**
     * 修改专题下产品
     * @param newList
     * @param goodsThemeId
     * @return
     */
    public boolean saveOrupdate(List<ThemeProduct> newList, Long goodsThemeId){
        //得到数据库当前产品
        List<ThemeProduct> oldList = themeProductDao.queryListByGoodsThemeId(goodsThemeId);
        List<Long> updateIdList = new ArrayList<Long>();
        if(oldList.size()>0){//需要判断 前端用户哪些是新增，哪些是保存，哪些是修改
            for (ThemeProduct product:newList){//保存新增产品
                if(!StringUtils.isEmpty(product.getId())){
                    updateIdList.add(product.getId());
                }else {
                    themeProductDao.save(product);
                }
            }

            for(ThemeProduct product:oldList){//删除前端删除产品
                if(!updateIdList.contains(product.getId())){
                    themeProductDao.remove(product);
                }
            }

            for(ThemeProduct updateDetail:oldList){//修改前段修改产品
                for(ThemeProduct detail:newList){
                    if(detail.getId()==updateDetail.getId()){
                        themeProductDao.update(updateDetail);
                    }
                }
            }
        }else {//保存
            for (ThemeProduct themeProduct:newList){
                themeProductDao.save(themeProduct);
            }
        }
        return true;
    }

    public Page<GoodsTheme> findPages(Pageable pageable ,Long id){
        return goodsThemeDao.findPages(pageable ,id);
    }

    public List<GoodsTheme> findGoodsThemeByTarId(Long[] targetId) {
        List<GoodsTheme> result = new ArrayList<GoodsTheme>();
        if (targetId != null) {
            for (Long id : targetId) {
                List<GoodsTheme> re=goodsThemeDao.findByTarId(id);
                if(re.size()>0){
                    for(GoodsTheme r:re){
                        result.add(r);
                    }
                }

            }
        }
        return result;
    }


}
