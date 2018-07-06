package com.wtshop.service;

import com.wtshop.dao.InterestCategoryDao;
import com.wtshop.model.InterestCategory;
import com.wtshop.model.SkinType;

import static com.jfinal.aop.Enhancer.enhance;

/**
 * Created by sq on 2017/8/21.
 */
public class SkinTypeService extends BaseService<SkinType>{


    public SkinTypeService() {
        super( SkinType.class);
    }



}
