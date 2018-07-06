package com.wtshop.service;

import com.jfinal.aop.Enhancer;
import com.jfinal.plugin.activerecord.Page;
import com.wtshop.Pageable;
import com.wtshop.dao.FunctionDao;
import com.wtshop.model.Function;


import java.util.List;

/**
 * Created by 蔺哲 on 2017/7/21.
 */
public class OrganizationService extends BaseService<Function>{
    private FunctionDao functionDao = Enhancer.enhance(FunctionDao.class);
    public OrganizationService(){super(Function.class);}

    /**
     * 查询当前父节点数据
     * @param parentId
     * @return
     */
    public List<Function> queryByParentId(Long parentId){
        return functionDao.queryByParentId(parentId);
    }

    /**
     * 分页显示当前父节点下数据
     * @param parentId
     * @param pageable
     * @param name
     * @return
     */
    public Page<Function> queryByPage(Long parentId, Pageable pageable, String name){
        return functionDao.queryByPage(parentId,pageable,name);
    }

    /**
     * 查询树下所有节点
     * @return
     */
    public List<Function> findAllFunciton(){
        return functionDao.findAllFunciton();
    }
}
