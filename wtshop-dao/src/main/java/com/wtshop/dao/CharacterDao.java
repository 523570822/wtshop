package com.wtshop.dao;

import com.jfinal.plugin.activerecord.Page;
import com.wtshop.Pageable;
import com.wtshop.model.CharacterCommend;

/**
 * Created by sq on 2017/9/11.
 */
public class CharacterDao extends BaseDao<CharacterCommend> {

    public CharacterDao(){
        super(CharacterCommend.class);
    }

    /**
     * 分页
     */
    public Page<CharacterCommend> findPages(Pageable pageable, String name, String sn){

        String sql = "from character_commend c left join goods g on c.goods_id = g.id LEFT JOIN product_category p on g.product_category_id = p.id where 1 = 1   ";

        String select = " select c.id cid, c.create_date, g.*, p.name cname ";

        if( name != null ){
            sql += "AND g.name LIKE '%" + name +"%' " ;
        }
        if( sn != null ){
            sql += "AND g.sn LIKE '%" + sn +"%' " ;
        }


        return modelManager.paginate(pageable.getPageNumber(), pageable.getPageSize(), select, sql);

    }

}
