package com.wtshop.dao;

import com.jfinal.plugin.activerecord.Page;
import com.wtshop.Pageable;
import com.wtshop.model.MiaobiGoods;
import com.wtshop.util.DateUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * Created by 蔺哲 on 2017/9/12.
 */
public class MiaoBiGoodsDao extends BaseDao<MiaobiGoods> {
    public MiaoBiGoodsDao(){super(MiaobiGoods.class);}

    public Page findByPage(int pageNumber,int pageSize){
        String select = "SELECT g.*,mb.price_miaobi,mb.price AS mbPrice,mb.id as miaobiGoodsId ";
        String from = " FROM miaobi_goods mb LEFT JOIN goods g ON g.id=mb.goods_id";

        return modelManager.paginate(pageNumber,pageSize,select,from);
    }
    public Page findList(Pageable pageable){
        String select = "SELECT mb.*,g.`name`";
        String from = " FROM miaobi_goods mb LEFT JOIN goods g ON mb.goods_id=g.id where 1=1";
        String order = pageable.getOrderProperty();
        if(StringUtils.isEmpty(order)){
            pageable.setOrderProperty("orders");
            pageable.setOrderDirection("asc");
        }
        return super.findPages(select,from,pageable);
    }
}
