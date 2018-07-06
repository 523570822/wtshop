package com.wtshop.service;

import com.jfinal.aop.Enhancer;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.wtshop.Pageable;
import com.wtshop.dao.NodifyGoodsSendDao;
import com.wtshop.model.NodifyGoodsSend;

/**
 * Created by sq on 2017/10/19.
 */
public class NodifyGoodsSendService extends BaseService<NodifyGoodsSend> {

    public NodifyGoodsSendService(){
        super(NodifyGoodsSend.class);
    }


    private NodifyGoodsSendDao nodifyGoodsSendDao = Enhancer.enhance(NodifyGoodsSendDao.class);

    /**
     * 分页查询
     */
    public Page<Record> findPageList(Pageable pageable){
        String searchProperty = pageable.getSearchProperty();
        String searchValue = pageable.getSearchValue();
        String sn = null;
        String nickname = null ;
        String phone = null;
        if("sn".equals(searchProperty)){
            sn = searchValue;
        }else if("nickname".equals(searchProperty)){
            nickname = searchValue;
        }else if("phone".equals(searchProperty)){
            phone = searchValue;
        }
        return  nodifyGoodsSendDao.findPageList(pageable, sn, nickname, phone);
    }

    /**
     * 根据orderId获取对象
     */
    public NodifyGoodsSend findByOrderId(Long orderId){
        return nodifyGoodsSendDao.findByOrderId(orderId);
    }

}
