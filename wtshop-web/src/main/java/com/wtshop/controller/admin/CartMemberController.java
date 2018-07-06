package com.wtshop.controller.admin;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.wtshop.Pageable;
import com.wtshop.service.CartService;

import java.util.ArrayList;
import java.util.List;

import static com.wtshop.model.Seo.Type.goodsList;

/**
 * Created by sq on 2017/8/17.
 */
@ControllerBind(controllerKey = "/admin/cartMember")
public class CartMemberController extends BaseController{

    private CartService cartService =  enhance(CartService.class);

    /**
     * 列表
     */

    public void list(){
        Pageable pageable = getBean(Pageable.class);
        Page<Record> page = cartService.findCartMember(pageable);
        setAttr("pageable", pageable);
        setAttr("page",page);
        render("/admin/cartMember/list.ftl");
    }

    /**
     * 查看
     */
    public void view (){
        Long memberId = getParaToLong("id");
        Pageable pageable = getBean(Pageable.class);
        Page<Record> page = cartService.findCartMemberMessage(pageable ,memberId);
        List<Long> memberList = new ArrayList<Long>(page.getList().size());
        for(Record record : page.getList()){
            memberList.add(record.getLong("id")) ;
        }

        setAttr("memberList",memberList);

        setAttr("pageable", pageable);
        setAttr("page",page);
        render("/admin/cartMember/view.ftl");
    }




}
