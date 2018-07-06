package com.wtshop.dao;

import com.jfinal.plugin.activerecord.Page;
import com.wtshop.Pageable;
import com.wtshop.model.GoodsTheme;

import java.util.List;


/**
 * Created by sq on 2017/6/2.
 */
public class GoodsThemeDao extends  BaseDao<GoodsTheme>{

    /**
     * 构造方法

     */
    public GoodsThemeDao() {
        super(GoodsTheme.class);
    }

//    /**
//     * 根据主题查对应商品
//     */
//    public Page<GoodsTheme> findPages(Pageable pageable){
//        String sql = "FROM goods_theme G LEFT JOIN theme_product T ON G.id = T.goodsTheme_id LEFT JOIN product P ON T.product_id = P.id LEFT JOIN goods D ON P.goods_id = D.id ORDER BY G.orders ASC" ;
//        String select = "SELECT G.ID,G.imgPath,G.title,D.`name`,P.ID,D.caption,D.price,D.image ";
//        return modelManager.paginate(pageable.getPageNumber(), pageable.getPageSize(), select, sql);
//
//    }

    /**
     * 根据主题查对应商品
     */
    public Page<GoodsTheme> findPages(Pageable pageable, Long id){

        if(id == null){
            return null;
        }
        String sql = "from goods_theme g left join target_path t on g.target_id = t.id  where g.product_category_id = " + id + " order by g.orders asc";
        String select = "SELECT g.id, g.product_category_id, g.title, g.imgPath, t.target_path, t.urltype ,t.title";
        return modelManager.paginate(pageable.getPageNumber(), pageable.getPageSize(), select, sql);

    }

    /**
     * 根据目标页面id查找对应得主题
     * @param id
     * @return
     */
    public List<GoodsTheme> findByTarId(Long id) {
        String sql="select * from goods_theme where target_id="+id;
        return modelManager.find(sql);
    }
}
