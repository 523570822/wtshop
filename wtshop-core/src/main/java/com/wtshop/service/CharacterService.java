package com.wtshop.service;

import com.jfinal.aop.Enhancer;
import com.jfinal.plugin.activerecord.Page;
import com.wtshop.Pageable;
import com.wtshop.dao.CharacterDao;
import com.wtshop.model.CharacterCommend;

/**
 * Created by sq on 2017/9/11.
 */
public class CharacterService extends BaseService<CharacterCommend>{

    public CharacterService(){
        super(CharacterCommend.class);
    }

    private CharacterDao characterDao = Enhancer.enhance(CharacterDao.class);

    /**
     * 分页
     */
    public Page<CharacterCommend> findPages(Pageable pageable){

        String searchProperty = pageable.getSearchProperty();
        String searchValue = pageable.getSearchValue();

        String name = null ;
        String sn = null;
        if("name".equals(searchProperty)){
            name = searchValue;
        }else if("sn".equals(searchProperty)){
            sn = searchValue;
        }

        return characterDao.findPages(pageable, name, sn);
    }

}
