package com.wtshop.dao;

import com.jfinal.plugin.activerecord.Page;
import com.wtshop.Pageable;
import com.wtshop.model.Function;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created by 蔺哲 on 2017/7/24.
 */
public class FunctionDao extends BaseDao<Function> {
    public FunctionDao(){super(Function.class);}

    /**
     * 根据父节点显示子节点
     * @param parentId
     * @return
     */
    public List<Function> queryByParentId(Long parentId){
        String sql = "SELECT * FROM function WHERE parentId =" + parentId;
        List<Function> functions=modelManager.find(sql);
        return functions;
    }

    /**
     * 分页查询父节点下子节点
     * @param parentId
     * @param pageable
     * @param name
     * @return
     */
    public Page<Function> queryByPage(Long parentId, Pageable pageable, String name){
        String sql = "FROM function WHERE parentId =" + parentId;
        if(StringUtils.isNotEmpty(name)){
            sql+=" And name like '%"+name+"%'";
        }
        pageable.setOrderDirection("asc");
        pageable.setOrderProperty("orders");
        return super.findPage(sql,pageable);
    }
    /**
     * 查询树下所有节点
     */
    public List<Function> findAllFunciton(){
        String sql = "SELECT * FROM function WHERE 1=1 ORDER BY `orders`";
        List<Function> functions=modelManager.find(sql);
        return functions;
    }
}
