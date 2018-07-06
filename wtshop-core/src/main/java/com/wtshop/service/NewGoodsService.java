package com.wtshop.service;

import com.jfinal.aop.Enhancer;
import com.jfinal.plugin.activerecord.Page;
import com.wtshop.Pageable;
import com.wtshop.dao.NewGoodsDao;
import com.wtshop.model.NewgoodsCommend;

/**
 * Created by sq on 2017/9/13.
 */
public class NewGoodsService extends BaseService<NewgoodsCommend>{

    public NewGoodsService(){
        super(NewgoodsCommend.class);
    }
    private NewGoodsDao newGoodsDao = Enhancer.enhance(NewGoodsDao.class);

    /**
     * 分页
     */
    public Page<NewgoodsCommend> findPages(Pageable pageable){
        String searchProperty = pageable.getSearchProperty();
        String searchValue = pageable.getSearchValue();

        String name = null ;
        String sn = null;
        if("name".equals(searchProperty)){
            name = searchValue;
        }else if("sn".equals(searchProperty)){
            sn = searchValue;
        }
        return newGoodsDao.findPages(pageable, name, sn);
    }

}
