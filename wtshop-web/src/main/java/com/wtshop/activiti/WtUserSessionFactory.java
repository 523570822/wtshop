package com.wtshop.activiti;

import com.jfinal.aop.Enhancer;
import org.activiti.engine.impl.interceptor.Session;
import org.activiti.engine.impl.interceptor.SessionFactory;
import org.activiti.engine.impl.persistence.entity.UserIdentityManager;

/**
 * Created by Administrator on 2018/1/9 0009.
 */
public class WtUserSessionFactory implements SessionFactory {

    private WtUserEntityManager entityManager = Enhancer.enhance(WtUserEntityManager.class);

    public Class<?> getSessionType() {
        return UserIdentityManager.class;
    }

    public Session openSession() {
        return entityManager;
    }
}
