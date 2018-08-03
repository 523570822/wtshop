package com.jfinalshop;

import com.wtshop.dao.ActivityDao;
import com.wtshop.model.Activity;
import com.wtshop.service.ActivityService;
import org.junit.Test;

import static com.jfinal.aop.Enhancer.enhance;

public class FCommentTest extends JFinalModelCase {
    Activity dao = Activity.dao;
   private ActivityDao activityService = enhance(ActivityDao.class);


    @Test
    public void testGetFCommentsByUserName(){
        Activity ss= activityService.find(1l);


    //    long ss = activityService.count();
            System.out.println(ss);

    }

    @Test
    public void testGetFcommentByContentId(){
      //  dao.getFcommentByContentId(12);
    }

}
