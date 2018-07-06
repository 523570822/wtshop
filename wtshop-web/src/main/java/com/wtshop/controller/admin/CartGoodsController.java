package com.wtshop.controller.admin;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.wtshop.Pageable;
import com.wtshop.service.CartService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sq on 2017/8/17.
 */
@ControllerBind(controllerKey = "/admin/cartGoods")
public class CartGoodsController extends BaseController{

    private CartService cartService =  enhance(CartService.class);

    /**
     * 列表
     */
    public void list(){
        Pageable pageable = getBean(Pageable.class);
        Page<Record> page = cartService.findCartGoods(pageable);

        setAttr("pageable", pageable);
        setAttr("page",page);
        render("/admin/cartGoods/list.ftl");
    }

    /**
     * 查看
     */
    public void view (){
        Long productId = getParaToLong("id");
        Pageable pageable = getBean(Pageable.class);
        Page<Record> page = cartService.findCartGoodsMember(pageable ,productId);

        List<Long> memberList = new ArrayList<Long>(page.getList().size());
        for(Record record : page.getList()){
            memberList.add(record.getLong("id")) ;
        }

        setAttr("memberList",memberList);

        setAttr("pageable", pageable);
        setAttr("page",page);
        render("/admin/cartGoods/view.ftl");
    }
}
