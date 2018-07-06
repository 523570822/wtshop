package com.wtshop.service;

import com.jfinal.aop.Enhancer;
import com.wtshop.dao.FlashsaleDao;
import com.wtshop.dao.FlashsaleDetailDao;
import com.wtshop.model.Flashsale;
import com.wtshop.model.FlashsaleDetail;
import com.wtshop.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 蔺哲 on 2017/5/17.
 */
public class FlashsaleService extends BaseService<Flashsale>{
    public FlashsaleService(){super(Flashsale.class);}
    private FlashsaleDao flashsaleDao = Enhancer.enhance(FlashsaleDao.class);
    private FlashsaleDetailDao flashsaleDetailDao = Enhancer.enhance(FlashsaleDetailDao.class);


    public List<FlashsaleDetail> queryListByFlashsaleId(Long id){
        return flashsaleDao.queryDetailById(id);
    }

    /**
     * 保存和修改抢购商品的方法
     * @param newList
     * @param flashsaleId
     */
    public void saveOrUpdate(List<FlashsaleDetail> newList,Long flashsaleId){
        List<FlashsaleDetail> oldList = queryListByFlashsaleId(flashsaleId);
        List<Long> updateIdList = new ArrayList<Long>();
        if(oldList.size()>0){//修改保存删除
            for (FlashsaleDetail detail:newList){//保存新增产品
                if(!StringUtils.isEmpty(detail.getId())){
                    updateIdList.add(detail.getId());
                }else {
                    flashsaleDetailDao.save(detail);
                }
            }

            for(FlashsaleDetail detail:oldList){//删除前端删除产品
                if(!updateIdList.contains(detail.getId())){
                    flashsaleDetailDao.remove(detail);
                }
            }

            for(FlashsaleDetail updateDetail:oldList){//修改前段修改产品
                for(FlashsaleDetail detail:newList){
                    if(detail.getId()==updateDetail.getId()){
                        flashsaleDetailDao.update(detail);
                    }
                }
            }
        }else {//保存
            for(FlashsaleDetail detail:newList){
                flashsaleDetailDao.save(detail);
            }
        }
    }
}
