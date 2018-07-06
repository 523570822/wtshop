package com.wtshop.controller.admin;

import com.jfinal.ext.route.ControllerBind;
import com.wtshop.Pageable;
import com.wtshop.model.Function;
import com.wtshop.service.OrganizationService;
import com.wtshop.util.XssUtil;


import java.util.List;
import java.util.Map;

/**
 * Created by 蔺哲 on 2017/7/21.
 */
@ControllerBind(controllerKey = "/admin/organization")
public class OrganizationController extends BaseController{
    private OrganizationService organizationService = enhance(OrganizationService.class);

    /**
     * 列表页面
     */
    public void list(){
        render("/admin/organization/list.ftl");
    }

    /**
     * 所有节点
     */
    public void queryAll(){
        List<Function> function = organizationService.findAllFunciton();
        renderJson(function);
    }
    /**
     * 父节点下子节点
     */
    public void query(){
        Long parentId = getParaToLong("parentId");
        Map par = getRequest().getParameterMap();
//        String sidx = getPara("sidx");
//        String sort = getPara("sort");
        String name = getPara("name");
        Pageable pageable = getBean(Pageable.class);
        renderJson(organizationService.queryByPage(parentId,pageable,name));
    }
    /**
     *
     */
    public void toAdd(){
        setAttr("parentId",getPara("parentId"));
        render("/admin/organization/add.ftl");
    }
    public void save(){
        Function function = getModel(Function.class);
        String name = getPara("function.name");
        function.setName(XssUtil.xssEncode(name));
        organizationService.save(function);
        redirect("list.jhtml");
    }
    public void toUpdate(){
        Long functionId = getParaToLong("functionId");
        setAttr("function",organizationService.find(functionId));
        render("/admin/organization/update.ftl");
    }
    public void update(){
        Function function = getModel(Function.class);
        String name = getPara("function.name");
        function.setName(XssUtil.xssEncode(name));
        organizationService.update(function);
        redirect("list.jhtml");
    }
}
