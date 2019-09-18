package com.wtshop.service;

import com.jfinal.aop.Before;
import com.jfinal.aop.Enhancer;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.wtshop.Pageable;
import com.wtshop.dao.ActivityStoreDao;
import com.wtshop.dao.MemberDao;
import com.wtshop.model.ActivityStore;
import com.wtshop.util.ObjectUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 蔺哲 on 2017/5/25.
 */
public class ActivityStoreService extends BaseService<ActivityStore> {

    private ActivityStoreDao certificatesDao = Enhancer.enhance(ActivityStoreDao.class);

    private MemberDao memberDao = Enhancer.enhance(MemberDao.class);
    public ActivityStoreService(){
        super(ActivityStore.class);
    }
    public ActivityStore queryByMemberId(Long memberId){
        return certificatesDao.queryByMemberId(memberId);
    }
    @Before(Tx.class)
    public Map updateCertificates(ActivityStore certificates,Integer type){
        Map result = new HashMap();
        if(ObjectUtils.isEmpty(certificates)){
            result.put("result",false);
            result.put("msg","审核信息不存在");
           return result;
        }

        certificates.setState(type);
        certificatesDao.update(certificates);
        result.put("result",true);
        result.put("msg","审核成功");
        return result;
    }


    /**
     *
     */
    public Page<ActivityStore> findShenHePage( Pageable pageable, Integer type){
        String username = null ;
        String create_date = null;
        String searchProperty = pageable.getSearchProperty();
        String searchValue = pageable.getSearchValue();
        if("name".equals(searchProperty)){
            username =searchValue;
        }else{
            create_date=searchValue;
        }


        return certificatesDao.findShenHePage(username, create_date, pageable, type);


    }
}
