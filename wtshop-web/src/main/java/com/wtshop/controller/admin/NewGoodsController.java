package com.wtshop.controller.admin;

import com.jfinal.aop.Enhancer;
import com.jfinal.ext.route.ControllerBind;
import com.wtshop.Pageable;
import com.wtshop.model.NewgoodsCommend;
import com.wtshop.service.GoodsService;
import com.wtshop.service.NewGoodsService;
import org.apache.commons.lang3.StringUtils;
import java.util.ArrayList;
import java.util.List;

import static com.wtshop.api.controller.BaseAPIController.convertToLong;

/**
 * Created by sq on 2017/9/7.
 */
@ControllerBind(controllerKey = "/admin/newGoods")
public class NewGoodsController extends BaseController{

    private GoodsService goodsService = Enhancer.enhance(GoodsService.class);
    private NewGoodsService newGoodsService = Enhancer.enhance(NewGoodsService.class);

    /**
     * 列表
     */
    public void list(){
        Pageable pageable = getBean(Pageable.class);
        setAttr("pageable", pageable);
        setAttr("page", newGoodsService.findPages(pageable));
        render("/admin/newGoods/list.ftl");

    }

    /**
     * 添加
     */
    public void add(){
        List<NewgoodsCommend> newgoodsCommends = newGoodsService.findAll();
        List<Long> goodsList = new ArrayList<Long>();
        if(newgoodsCommends != null && newgoodsCommends.size() > 0){
            for(NewgoodsCommend characterCommend : newgoodsCommends){
                if(!goodsList.contains(characterCommend.getGoodsId())){
                    goodsList.add(characterCommend.getGoodsId());
                }
            }
        }
        Pageable pageable = getBean(Pageable.class);
        pageable.setPageSize(10);
        setAttr("pageable", pageable);
        setAttr("page", goodsService.findPage(pageable ,goodsList));
        render("/admin/newGoods/goods.ftl");
    }

    /**
     * 商品
     */
    public void goodsList(){
        String[] values = StringUtils.split(getPara("ids"), ",");
        Long[] ids = values == null ? null :convertToLong(values);
        for(Long id : ids){
            NewgoodsCommend newgoodsCommend = new NewgoodsCommend();
            newgoodsCommend.setGoodsId(id);
            newGoodsService.save(newgoodsCommend);
        }
        addFlashMessage(SUCCESS_MESSAGE);
        redirect("list.jhtml");

    }

    /**
     * 删除
     */
    public void delete() {
        Long[] ids = getParaValuesToLong("ids");
        for(Long id : ids){
            newGoodsService.delete(id);
        }
        renderJson(SUCCESS_MESSAGE);
    }


}
