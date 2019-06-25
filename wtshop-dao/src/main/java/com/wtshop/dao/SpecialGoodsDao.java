package com.wtshop.dao;


import com.wtshop.model.FuDai;
import com.wtshop.model.SpecialGoods;

import java.util.List;

/**
 * Created by sq on 2017/7/11.
 */
public class SpecialGoodsDao extends BaseDao<SpecialGoods>{

    public SpecialGoodsDao() {
        super(SpecialGoods.class);
    }

    /**
     * 获取当前正在使用的福袋
     */
    public List<SpecialGoods> findLists(){

        String sql = " SELECT f.* FROM special_goods f  \n" +
                " where 1 = 1 AND status = 0 order by price desc";
        return modelManager.find(sql);

    }


}
