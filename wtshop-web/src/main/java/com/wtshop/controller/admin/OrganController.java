package com.wtshop.controller.admin;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;
import com.wtshop.Message;
import com.wtshop.Pageable;
import com.wtshop.model.Ad;
import com.wtshop.model.Organ;
import com.wtshop.service.OrganService;
import com.sun.tools.corba.se.idl.constExpr.Or;
import org.apache.commons.codec.digest.DigestUtils;


/**
 * Created by sq on 2017/7/21.
 */
@ControllerBind(controllerKey = "/admin/organ")
public class OrganController extends BaseController{

    private OrganService organService = enhance(OrganService.class);

    /**
     * 检查用户名是否存在
     *
     */
    public void checkout(){
        String name = getPara("name");
        Organ organ = organService.findByUsername(name);
        if(organ != null){
            renderJson(ERROR_MESSAGE);
            return ;
        }else {
            renderJson(SUCCESS_MESSAGE);
        }

    }



    /**
     * 店铺列表
     */
    public void list(){
        Pageable pageable = getBean(Pageable.class);
        setAttr("pageable", pageable);
        setAttr("page", organService.findPage(pageable));
        render("/admin/organ/list.ftl");
    }

    /**
     * 店铺添加
     */
    public void add(){
        setAttr("status", Organ.Status.values());
        render("/admin/organ/add.ftl");
    }


    /**
     * 店铺保存
     */
    public void save(){
        Organ organ = getModel(Organ.class);
        Organ organs = organService.findByUsername(organ.getAdminName());
        if(organs == null){
            String status = getPara("status");
            String adminPassword = organ.getAdminPassword();
            Organ.Status type = StrKit.notBlank(status) ? Organ.Status.valueOf(status) : null;
            organ.setStatus(type.ordinal());
            organ.setAdminPassword( DigestUtils.md5Hex(adminPassword));
            organService.save(organ);
            addFlashMessage(SUCCESS_MESSAGE);
            redirect("list.jhtml");
        }else {
            addFlashMessage(Message.error("message.error.username"));
            redirect("list.jhtml");
        }

    }

    /**
     * 编辑
     */

    public void edit(){
        Long id = getParaToLong("id");
        Organ organ = organService.find(id);
        String type = null;
        if(Organ.Status.init.ordinal() == organ.getStatus() ){
            type = "init";
        }else if(Organ.Status.through.ordinal() == organ.getStatus()){
            type = "through";
        }else if(Organ.Status.delete.ordinal() == organ.getStatus()){
            type = "delete";
        }else {
            type = "using";
        }
        setAttr("organ",organ);
        setAttr("type",type);
        setAttr("status", Organ.Status.values());
        render("/admin/organ/edit.ftl");
    }

    /**
     * 更新
     */
    public void update(){

        Organ organ= getModel(Organ.class);
        Organ organs = organService.findByUsername(organ.getAdminName());
        if(organs == null) {
            String status = getPara("status");
            String adminPassword = organ.getAdminPassword();
            Organ.Status type = StrKit.notBlank(status) ? Organ.Status.valueOf(status) : null;
            organ.setStatus(type.ordinal());
            organ.setAdminPassword(DigestUtils.md5Hex(adminPassword));
            organService.update(organ);
            addFlashMessage(SUCCESS_MESSAGE);
            redirect("/admin/organ/list.jhtml");
        }else {
            addFlashMessage(Message.error("message.error.username"));
            redirect("list.jhtml");
        }
    }

    /**
     * 删除
     */
    public void delete() {
        Long[] ids = getParaValuesToLong("ids");
        for(Long id : ids){
            Organ organ = organService.find(id);
            organ.setStatus(Organ.Status.delete.ordinal());
            organService.update(organ);
        }
        renderJson(SUCCESS_MESSAGE);

    }
}
