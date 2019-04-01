package com.wtshop.dao;

import com.wtshop.model.InterestCategory;
import com.wtshop.model.base.BaseDeliveryCenter;

import java.util.List;

/**
 * Created by sq on 2017/8/21.
 */
public class InterestCategoryDao extends BaseDao<InterestCategory>{

    public InterestCategoryDao(){super (InterestCategory.class);}

    /**
     * 查找未使用的属性序号
     *
     * @return 未使用的属性序号，若不存在则返回null
     */
    public List<InterestCategory> findByMember(Long id) {

            String sql = "select * from  interest_category i LEFT JOIN  (select * from member_interest_category m where m.members="+id+" ) m on  i.id=m.interest_category ";

        return modelManager.find(sql);
        };



}
