package com.wtshop.dao;

import com.wtshop.model.Ad;

import java.util.List;

/**
 * Dao - 广告
 */
public class AdDao extends OrderEntity<Ad> {

    /**
     * 构造方法
     */
    public AdDao() {
        super(Ad.class);
    }

    /**
     * 获取首页轮播图
     */

    public List<Ad> findAdList(Long id) {
        String sql = "SELECT t.target_path,a.target_id, t.urltype ,a.title ,a.path, a.param FROM ad a LEFT JOIN target_path t on a.target_id = t.id WHERE a.ad_position_id =" + id;
        List<Ad> ads = modelManager.find(sql);
        return ads;
    }

    /**
     * 根据目标页面id查找对应得主题
     *
     * @param id
     * @return
     */
    public List<Ad> findByTarId(Long id) {
        String sql = "select * from ad where target_id=" + id;
        return modelManager.find(sql);
    }
}