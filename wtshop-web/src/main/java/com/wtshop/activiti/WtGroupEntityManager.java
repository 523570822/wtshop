package com.wtshop.activiti;

import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.GroupQuery;
import org.activiti.engine.impl.GroupQueryImpl;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.persistence.entity.GroupEntityManager;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/9 0009.
 */
public class WtGroupEntityManager extends GroupEntityManager {

    public WtGroupEntityManager() {
        super();
    }

    @Override
    public Group createNewGroup(String groupId) {
        return super.createNewGroup(groupId);
    }

    @Override
    public void insertGroup(Group group) {
        super.insertGroup(group);
    }

    @Override
    public void updateGroup(Group updatedGroup) {
        super.updateGroup(updatedGroup);
    }

    @Override
    public void deleteGroup(String groupId) {
        super.deleteGroup(groupId);
    }

    @Override
    public GroupQuery createNewGroupQuery() {
        return super.createNewGroupQuery();
    }

    @Override
    public List<Group> findGroupByQueryCriteria(GroupQueryImpl query, Page page) {
        System.out.println("findGroupByQueryCriteria");
        return super.findGroupByQueryCriteria(query, page);
    }

    @Override
    public long findGroupCountByQueryCriteria(GroupQueryImpl query) {
        System.out.println("findGroupCountByQueryCriteria");
        return super.findGroupCountByQueryCriteria(query);
    }

    @Override
    public List<Group> findGroupsByUser(String userId) {
        System.out.println("findGroupsByUser");
        return super.findGroupsByUser(userId);
    }

    @Override
    public List<Group> findGroupsByNativeQuery(Map<String, Object> parameterMap, int firstResult, int maxResults) {
        System.out.println("findGroupsByNativeQuery");
        return super.findGroupsByNativeQuery(parameterMap, firstResult, maxResults);
    }

    @Override
    public long findGroupCountByNativeQuery(Map<String, Object> parameterMap) {
        System.out.println("findGroupCountByNativeQuery");
        return super.findGroupCountByNativeQuery(parameterMap);
    }

    @Override
    public boolean isNewGroup(Group group) {
        return super.isNewGroup(group);
    }
}
