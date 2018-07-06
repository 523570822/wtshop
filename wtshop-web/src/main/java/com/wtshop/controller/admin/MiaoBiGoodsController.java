package com.wtshop.controller.admin;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Page;
import com.wtshop.Pageable;
import com.wtshop.model.MiaobiGoods;
import com.wtshop.service.MiaoBiGoodsService;
import org.apache.commons.lang3.StringUtils;

import static com.wtshop.controller.wap.BaseController.convertToLong;

/**
 * cms喵币商品维护
 * Created by 蔺哲 on 2017/9/12.
 */
@ControllerBind(controllerKey = "/admin/miaobi_goods")
public class MiaoBiGoodsController extends BaseController {
    private MiaoBiGoodsService miaoBiGoodsService = enhance(MiaoBiGoodsService.class);

    public void list(){
        Pageable pageable = getBean(Pageable.class);
        Page page = miaoBiGoodsService.findByPage(pageable);
        setAttr("pageable",pageable);
        setAttr("page",page);
        render("/admin/miaobi_goods/list.ftl");
    }
    public void add(){
        render("/admin/miaobi_goods/add.ftl");
    }
    public void save(){
        MiaobiGoods miaobiGoods = getModel(MiaobiGoods.class);
        miaoBiGoodsService.save(miaobiGoods);
        addFlashMessage(SUCCESS_MESSAGE);
        redirect("list.jhtml");
    }
    public void edit(){
        Long id = getParaToLong("id");
        MiaobiGoods miaobiGoods = miaoBiGoodsService.find(id);
        setAttr("miaobiGoods",miaobiGoods);
        render("/admin/miaobi_goods/edit.ftl");
    }
    public void update(){
        MiaobiGoods miaobiGoods = getModel(MiaobiGoods.class);
        miaoBiGoodsService.update(miaobiGoods);
        addFlashMessage(SUCCESS_MESSAGE);
        redirect("list.jhtml");
    }
    public void delete(){
        String[] values = StringUtils.split(getPara("ids"), ",");
        Long[] skuids = values == null ? null :convertToLong(values);
        miaoBiGoodsService.delete(skuids);
        renderJson(SUCCESS_MESSAGE);
    }
}
