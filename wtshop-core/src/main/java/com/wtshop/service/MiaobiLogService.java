package com.wtshop.service;

import com.jfinal.aop.Enhancer;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.wtshop.Pageable;
import com.wtshop.dao.MiaobiLogDao;
import com.wtshop.model.MiaobiLog;
import com.wtshop.model.PointLog;

import java.io.Serializable;

/**
 * Created by sq on 2017/9/7.
 */
public class MiaobiLogService extends BaseService<MiaobiLog>{

    public MiaobiLogService(){
        super(MiaobiLog.class);
    }

    private MiaobiLogDao miaobiLogDao = Enhancer.enhance(MiaobiLogDao.class);

    /**
     * 喵币分页
     * @param pageable
     * @param type
     * @return
     */
    public Page<Record> findPages(Pageable pageable , Integer type){
        String searchProperty = pageable.getSearchProperty();
        String searchValue = pageable.getSearchValue();
        String name = null;
         if("name".equals(searchProperty)){
            name = searchValue;
        }
        return miaobiLogDao.findPages(pageable, name ,type);
    }


    /**
     * 根据会员id 获取首次赠送记录
     */
    public MiaobiLog findLogByMemberId(Long memberId){
        return  miaobiLogDao.findLogByMemberId(memberId);
    }



}
