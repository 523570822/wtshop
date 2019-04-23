package com.wtshop.controller.admin;

import com.jfinal.ext.route.ControllerBind;
import com.wtshop.Pageable;
import com.wtshop.model.SkinType;
import com.wtshop.service.SkinTypeService;

/**
 * Created by sq on 2017/8/21.
 */
@ControllerBind(controllerKey = "admin/skinType")
public class SkinTypeController extends BaseController{

    private SkinTypeService skinTypeService = enhance(SkinTypeService.class);

    /**
     * 列表
     */
    public void list(){
        Pageable pageable = getBean(Pageable.class);
        setAttr("pageable",pageable);
        setAttr("page",skinTypeService.findPage(pageable));
        render("/admin/skin_type/list.ftl");
    }

    /**
     * 添加
     */
    public void add(){
        render("/admin/skin_type/add.ftl");
    }

    /**
     * 保存
     */
    public void save(){
        SkinType model = getModel(SkinType.class);
        skinTypeService.save(model);
        addFlashMessage(SUCCESS_MESSAGE);
        redirect("/admin/skinType/list.jhtml");
    }

    /**
     * 编辑
     */
    public void edit(){
        Long id = getParaToLong("id");
        setAttr("skinType", skinTypeService.find(id));
        render("/admin/skin_type/edit.ftl");
    }

    /**
     * 更新
     */
    public void update(){
        SkinType model = getModel(SkinType.class);
        skinTypeService.update(model);
        addFlashMessage(SUCCESS_MESSAGE);
        redirect("/admin/skinType/list.jhtml");
    }

    /**
     * 删除
     */
    public void delete(){
        Long[] ids = getParaValuesToLong("ids");
        skinTypeService.delete(ids);
        renderJson(SUCCESS_MESSAGE);
    }



}
