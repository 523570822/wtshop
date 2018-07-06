package com.wtshop.service;

import com.wtshop.dao.InterestCategoryDao;
import com.wtshop.model.InterestCategory;

import static com.jfinal.aop.Enhancer.enhance;

/**
 * Created by sq on 2017/8/21.
 */
public class InterestCategoryService extends BaseService<InterestCategory>{


    public InterestCategoryService() {
        super(InterestCategory.class);
    }

    private InterestCategoryDao interestCategoryDao = enhance(InterestCategoryDao.class);


}
