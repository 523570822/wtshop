package com.wtshop.activiti;

import com.jfinal.aop.Enhancer;
import org.activiti.engine.impl.interceptor.Session;
import org.activiti.engine.impl.interceptor.SessionFactory;
import org.activiti.engine.impl.persistence.entity.GroupIdentityManager;

/**
 * Created by Administrator on 2018/1/9 0009.
 */
public class WtGroupSessionFactory implements SessionFactory {

    private WtGroupEntityManager entityManager = Enhancer.enhance(WtGroupEntityManager.class);

    public Class<?> getSessionType() {
        return GroupIdentityManager.class;
    }

    public Session openSession() {
        return entityManager;
    }
}
