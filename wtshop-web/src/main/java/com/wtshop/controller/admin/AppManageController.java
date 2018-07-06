package com.wtshop.controller.admin;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.ext2.kit.RandomKit;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;
import com.wtshop.Pageable;
import com.wtshop.model.AppManage;
import com.wtshop.service.AppManageService;
import com.wtshop.util.ApiResult;

@ControllerBind(controllerKey = "/admin/appManage")
public class AppManageController extends BaseController {
    private AppManageService appManageService = enhance(AppManageService.class);

    public void add() {
        setAttr("appId", RandomKit.randomStr());
        setAttr("appKey", RandomKit.randomMD5Str());
        render("/admin/app_manage/add.ftl");
    }

    public void save() {
        AppManage appManage = getModel(AppManage.class);
        appManageService.save(appManage);
        Cache redis = Redis.use();
        redis.hset("appManage", appManage.getAppId(), appManage);
        redirect("list.jhtml");

    }

    public void list() {
        Pageable pageable = getBean(Pageable.class);
        setAttr("pageable", pageable);
        setAttr("page", appManageService.findPage(pageable));
        render("/admin/app_manage/list.ftl");
    }

    public void delete() {
        Long[] ids = getParaValuesToLong("ids");
        if (ids != null) {
            Cache redis = Redis.use();
            for (Long id : ids) {
                AppManage appManage = appManageService.find(id);
                redis.hdel("appManage", appManage.getAppId());
                appManageService.delete(id);
            }
            renderJson(ApiResult.success());
        }
    }

    public void edit() {
        AppManage appManage = appManageService.find(getParaToLong("id"));
        setAttr("appManage", appManage);
        render("/admin/app_manage/edit.ftl");
    }

    public void update() {
        AppManage appManage = getModel(AppManage.class);
        appManageService.update(appManage);
        Cache redis = Redis.use();
        redis.hset("appManage", appManage.getAppId(), appManage);
        addFlashMessage(SUCCESS_MESSAGE);
        redirect("list.jhtml");
    }

}
