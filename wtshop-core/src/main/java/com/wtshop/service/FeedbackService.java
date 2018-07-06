package com.wtshop.service;

import com.jfinal.aop.Enhancer;
import com.jfinal.plugin.activerecord.Page;
import com.wtshop.Pageable;
import com.wtshop.dao.FeedbackDao;
import com.wtshop.model.Feedback;

/**
 * Created by sq on 2017/6/22.
 */
public class FeedbackService extends BaseService<Feedback>{


    public FeedbackService() {
        super(Feedback.class);
    }

    private FeedbackDao feedbackDao = Enhancer.enhance(FeedbackDao.class);

    /**
     * 列表
     */
    public Page<Feedback> feedbackPage(String memberName,Pageable pageable){

        return feedbackDao.feedbackPage(memberName,pageable);

    }

}
