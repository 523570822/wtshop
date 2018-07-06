package com.wtshop.controller.admin;

import com.jfinal.ext.route.ControllerBind;
import com.wtshop.Pageable;
import com.wtshop.model.InterestCategory;
import com.wtshop.service.InterestCategoryService;

import java.util.List;

/**
 * Created by sq on 2017/8/21.
 */
@ControllerBind(controllerKey = "admin/interestCategory")
public class InterestCategoryController extends BaseController{

    private InterestCategoryService interestCategoryService = enhance(InterestCategoryService.class);

    /**
     * 列表
     */
    public void list(){
        Pageable pageable = getBean(Pageable.class);
        setAttr("pageable",pageable);
        setAttr("page",interestCategoryService.findPage(pageable));
        render("/admin/interest_category/list.ftl");
    }

    /**
     * 添加
     */
    public void add(){
        render("/admin/interest_category/add.ftl");
    }

    /**
     * 保存
     */
    public void save(){
        InterestCategory model = getModel(InterestCategory.class);
        interestCategoryService.save(model);
        addFlashMessage(SUCCESS_MESSAGE);
        redirect("list.jhtml");
    }

    /**
     * 编辑
     */
    public void edit(){
        Long id = getParaToLong("id");
        setAttr("interestCategory", interestCategoryService.find(id));
        render("/admin/interest_category/edit.ftl");
    }

    /**
     * 更新
     */
    public void update(){
        InterestCategory model = getModel(InterestCategory.class);
        interestCategoryService.update(model);
        addFlashMessage(SUCCESS_MESSAGE);
        redirect("list.jhtml");
    }

    /**
     * 删除
     */
    public void delete(){
        Long[] ids = getParaValuesToLong("ids");
        interestCategoryService.delete(ids);
        renderJson(SUCCESS_MESSAGE);
    }



}
