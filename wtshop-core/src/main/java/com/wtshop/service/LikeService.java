package com.wtshop.service;

import com.jfinal.aop.Enhancer;
import com.jfinal.plugin.activerecord.Page;
import com.wtshop.Pageable;
import com.wtshop.dao.CharacterDao;
import com.wtshop.dao.LikeDao;
import com.wtshop.model.CharacterCommend;
import com.wtshop.model.LikeCommend;

/**
 * Created by sq on 2017/9/11.
 */
public class LikeService extends BaseService<LikeCommend>{

    public LikeService(){
        super(LikeCommend.class);
    }

    private LikeDao likeDao = Enhancer.enhance(LikeDao.class);

    /**
     * 分页
     */
    public Page<LikeCommend> findPages(Pageable pageable){

        String searchProperty = pageable.getSearchProperty();
        String searchValue = pageable.getSearchValue();

        String name = null ;
        String sn = null;
        if("name".equals(searchProperty)){
            name = searchValue;
        }else if("sn".equals(searchProperty)){
            sn = searchValue;
        }

        return likeDao.findPages(pageable, name, sn);
    }

}
