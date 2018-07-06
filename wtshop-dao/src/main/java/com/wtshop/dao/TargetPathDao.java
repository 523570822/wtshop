package com.wtshop.dao;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.wtshop.Pageable;
import com.wtshop.model.TargetPath;

import java.util.List;

/**
 * Created by admin on 2017/6/21.
 */
public class TargetPathDao extends BaseDao<TargetPath>{
    /**
     * 构造方法
     *
     * @param entityClass
     */
    public TargetPathDao(Class<TargetPath> entityClass) {
        super(entityClass);
    }

    public TargetPathDao() {
        super(TargetPath.class);
    }

    /**
     * 查找一级标题对应的二级标题
     * @param urltype
     * @return
     */
    public List<TargetPath> findByUrltype(int urltype) {
        String sql="select * from target_path where urltype="+urltype+" AND level_state=2";
        return super.findList(sql, null, null, null, null);
    }

    /**
     * 根据id，查找一级标题下的所有的二级标题
     * @param tarId
     * @param pageable
     * @return
     */
    public Page<TargetPath> findById(Integer tarId, Pageable pageable) {
        Long t=tarId.longValue();
        TargetPath targetPath=super.find(t);
        String sql="from target_path where urltype="+targetPath.getUrltype()+" AND level_state=2";
        return super.findPage(sql,pageable);
    }
    public Page<TargetPath> findLevel(Pageable pageable) {
        String sql="from target_path where level_state=2";
        return super.findPage(sql,pageable);
    }

    public List<TargetPath> findAll() {
        String sql="select * from target_path";
        return modelManager.find(sql);
    }
    public TargetPath findMaxUrlType(){
        String sql = "SELECT MAX(urltype) AS urltype FROM target_path WHERE level_state=1";
        return modelManager.findFirst(sql);
    }
}
