package com.wtshop.activiti;

import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.Picture;
import org.activiti.engine.identity.User;
import org.activiti.engine.identity.UserQuery;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.UserQueryImpl;
import org.activiti.engine.impl.persistence.entity.IdentityInfoEntity;
import org.activiti.engine.impl.persistence.entity.UserEntityManager;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/9 0009.
 */
public class WtUserEntityManager extends UserEntityManager {

    public WtUserEntityManager() {
        super();
    }

    @Override
    public User createNewUser(String userId) {
        return super.createNewUser(userId);
    }

    @Override
    public void insertUser(User user) {
        super.insertUser(user);
    }

    @Override
    public void updateUser(User updatedUser) {
        super.updateUser(updatedUser);
    }

    @Override
    public User findUserById(String userId) {
        System.out.println("findUserById");
        return super.findUserById(userId);
    }

    @Override
    public void deleteUser(String userId) {
        super.deleteUser(userId);
    }

    @Override
    public List<User> findUserByQueryCriteria(UserQueryImpl query, Page page) {
        System.out.println("findUserByQueryCriteria");
        return super.findUserByQueryCriteria(query, page);
    }

    @Override
    public long findUserCountByQueryCriteria(UserQueryImpl query) {
        System.out.println("findUserCountByQueryCriteria");
        return super.findUserCountByQueryCriteria(query);
    }

    @Override
    public List<Group> findGroupsByUser(String userId) {
        System.out.println("findGroupsByUser");
        return super.findGroupsByUser(userId);
    }

    @Override
    public UserQuery createNewUserQuery() {
        return super.createNewUserQuery();
    }

    @Override
    public IdentityInfoEntity findUserInfoByUserIdAndKey(String userId, String key) {
        System.out.println("findUserInfoByUserIdAndKey");
        return super.findUserInfoByUserIdAndKey(userId, key);
    }

    @Override
    public List<String> findUserInfoKeysByUserIdAndType(String userId, String type) {
        System.out.println("findUserInfoKeysByUserIdAndType");
        return super.findUserInfoKeysByUserIdAndType(userId, type);
    }

    @Override
    public Boolean checkPassword(String userId, String password) {
        return super.checkPassword(userId, password);
    }

    @Override
    public List<User> findPotentialStarterUsers(String proceDefId) {
        System.out.println("findPotentialStarterUsers");
        return super.findPotentialStarterUsers(proceDefId);
    }

    @Override
    public List<User> findUsersByNativeQuery(Map<String, Object> parameterMap, int firstResult, int maxResults) {
        System.out.println("findUsersByNativeQuery");
        return super.findUsersByNativeQuery(parameterMap, firstResult, maxResults);
    }

    @Override
    public long findUserCountByNativeQuery(Map<String, Object> parameterMap) {
        System.out.println("findUserCountByNativeQuery");
        return super.findUserCountByNativeQuery(parameterMap);
    }

    @Override
    public boolean isNewUser(User user) {
        return super.isNewUser(user);
    }

    @Override
    public Picture getUserPicture(String userId) {
        return super.getUserPicture(userId);
    }

    @Override
    public void setUserPicture(String userId, Picture picture) {
        super.setUserPicture(userId, picture);
    }
}
