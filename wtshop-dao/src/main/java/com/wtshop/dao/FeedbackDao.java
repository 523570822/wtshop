package com.wtshop.dao;

import com.jfinal.plugin.activerecord.Page;
import com.wtshop.Pageable;
import com.wtshop.model.Feedback;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by sq on 2017/6/23.
 */
public class FeedbackDao extends BaseDao<Feedback>{

    /**
     * 构造方法
     */
    public FeedbackDao() {
        super(Feedback.class);
    }


    public Page<Feedback> feedbackPage(String memberName, Pageable pageable){

        String sql = "From feedback f LEFT JOIN member m on f.member_id = m.id where 1 = 1";
        if(!StringUtils.isEmpty(memberName)){
            sql +=" AND m.username LIKE " + "'%" +memberName+"%'";
        }
        String select = "select f.* , m.username";
        sql += " order by create_date desc ";
        return modelManager.paginate(pageable.getPageNumber(), pageable.getPageSize(), select, sql);

    }

}
