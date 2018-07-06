package com.wtshop.dao;

import com.wtshop.model.Admin;
import com.wtshop.model.Organ;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by sq on 2017/7/21.
 */
public class OrganDao extends BaseDao<Organ>{
    /**
     * 构造方法
     */
    public OrganDao() {
        super(Organ.class);
    }

    /**
     * 根据用户名查找管理员
     *
     * @param username
     *            用户名(忽略大小写)
     * @return 管理员，若不存在则返回null
     */
    public Organ findByUsername(String username) {
        if (StringUtils.isEmpty(username)) {
            return null;
        }
        try {
            String sql = "SELECT * FROM organ WHERE LOWER(admin_name) = LOWER(?)";
            return modelManager.findFirst(sql, username);
        } catch (Exception e) {
            return null;
        }
    }
}
