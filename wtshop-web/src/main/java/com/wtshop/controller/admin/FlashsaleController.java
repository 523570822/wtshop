package com.wtshop.controller.admin;

import com.jfinal.ext.route.ControllerBind;
import com.wtshop.Pageable;
import com.wtshop.model.Flashsale;
import com.wtshop.model.FlashsaleDetail;
import com.wtshop.service.FlashsaleService;
import com.wtshop.service.ProductCategoryService;

import java.util.List;

/**
 * Created by 蔺哲 on 2017/5/17.
 */
@ControllerBind(controllerKey = "/admin/flashsale")
public class FlashsaleController extends BaseController{

    private FlashsaleService flashsaleService = enhance(FlashsaleService.class);
    private ProductCategoryService productCategoryService = enhance(ProductCategoryService.class);

    public void list(){
        Pageable pageable = getBean(Pageable.class);
        setAttr("pageable", pageable);
        setAttr("page",flashsaleService.findPage(pageable));
        render("/admin/flashsale/list.ftl");
    }
    public void add(){
        setAttr("productCategoryTree", productCategoryService.queryByGrade(0));
        render("/admin/flashsale/add.ftl");
    }
    public void save(){
        Flashsale flashsale = getModel(Flashsale.class);
        flashsaleService.save(flashsale);
        addFlashMessage(SUCCESS_MESSAGE);
        redirect("list.jhtml");
    }
    public void edit(){
        Long id = getParaToLong("id");
        setAttr("productCategoryTree", productCategoryService.queryByGrade(0));
        setAttr("flashsale",flashsaleService.find(id));
        render("/admin/flashsale/edit.ftl");
    }
    public void update(){
        Flashsale flashsale = getModel(Flashsale.class);
        flashsaleService.update(flashsale);
        addFlashMessage(SUCCESS_MESSAGE);
        redirect("list.jhtml");
    }
    public void delete(){
        Long[] ids = getParaValuesToLong("ids");
        flashsaleService.delete(ids);
        renderJson(SUCCESS_MESSAGE);
    }
    public void addGoods(){
        Long id = getParaToLong("id");
        List list = flashsaleService.queryListByFlashsaleId(id);
        setAttr("flashsaleId",id);
        setAttr("detailNum",list.size());
        setAttr("detailList",list);
        render("/admin/flashsale/addGoods.ftl");
    }
    public void saveGoogs(){
        List<FlashsaleDetail> list = getModels(FlashsaleDetail.class);
        Long flashsaleId = getParaToLong("flashsaleId");
        flashsaleService.saveOrUpdate(list,flashsaleId);
        addFlashMessage(SUCCESS_MESSAGE);
        redirect("list.jhtml");
    }
}
