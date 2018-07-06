package com.wtshop.controller.admin;

import com.jfinal.ext.route.ControllerBind;
import com.wtshop.Pageable;
import com.wtshop.model.FreeUse;
import com.wtshop.service.FreeUseService;

/**
 * Created by 蔺哲 on 2017/5/17.
 */
@ControllerBind(controllerKey = "admin/freeUser")
public class FreeUseController extends BaseController{
    private FreeUseService freeUseService = enhance(FreeUseService.class);
    public void list(){
        Pageable pageable = getBean(Pageable.class);
        setAttr("pageable", pageable);
        setAttr("page",freeUseService.findPage(pageable));
        render("/admin/free_use/list.ftl");
    }
    public void add(){
        render("/admin/free_use/add.ftl");
    }
    public void save(){
        FreeUse freeUse = getModel(FreeUse.class);
        freeUseService.save(freeUse);
        addFlashMessage(SUCCESS_MESSAGE);
        redirect("list.jhtml");
    }
    public void edit(){
        Long id = getParaToLong("id");
        setAttr("freeUse",freeUseService.find(id));
        render("/admin/free_use/edit.ftl");
    }
    public void update(){
        FreeUse freeUse = getModel(FreeUse.class);
        freeUseService.update(freeUse);
        addFlashMessage(SUCCESS_MESSAGE);
        redirect("list.jhtml");
    }
    public void delete(){
        Long[] ids = getParaValuesToLong("ids");
        freeUseService.delete(ids);
        renderJson(SUCCESS_MESSAGE);
    }
}
