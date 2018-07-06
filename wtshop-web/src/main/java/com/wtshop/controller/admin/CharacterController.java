package com.wtshop.controller.admin;

import com.jfinal.aop.Enhancer;
import com.jfinal.ext.route.ControllerBind;
import com.wtshop.Pageable;
import com.wtshop.model.CharacterCommend;
import com.wtshop.model.Member;
import com.wtshop.service.CharacterService;
import com.wtshop.service.GoodsService;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static com.wtshop.api.controller.BaseAPIController.convertToLong;

/**
 * Created by sq on 2017/9/7.
 */


@ControllerBind(controllerKey = "/admin/character")
public class CharacterController extends BaseController{

    private CharacterService characterService = Enhancer.enhance(CharacterService.class);
    private GoodsService goodsService = Enhancer.enhance(GoodsService.class);

    /**
     * 列表
     */
    public void list(){
        Pageable pageable = getBean(Pageable.class);
        setAttr("pageable", pageable);
        setAttr("page", characterService.findPages(pageable));
        render("/admin/character/list.ftl");

    }

    /**
     * 添加
     */
    public void add(){
        List<CharacterCommend> characterCommends = characterService.findAll();
        List<Long> goodsList = new ArrayList<Long>();
        if(characterCommends != null && characterCommends.size() > 0){
            for(CharacterCommend characterCommend : characterCommends){
                if(!goodsList.contains(characterCommend.getGoodsId())){
                    goodsList.add(characterCommend.getGoodsId());
                }
            }
        }
        Pageable pageable = getBean(Pageable.class);
        pageable.setPageSize(10);
        setAttr("pageable", pageable);
        setAttr("page", goodsService.findPage(pageable ,goodsList));
        render("/admin/character/goods.ftl");
    }

    /**
     * 商品
     */
    public void goodsList(){
        String[] values = StringUtils.split(getPara("ids"), ",");
        Long[] ids = values == null ? null :convertToLong(values);
        for(Long id : ids){
            CharacterCommend characterCommend = new CharacterCommend();
            characterCommend.setGoodsId(id);
            characterService.save(characterCommend);
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
            characterService.delete(id);
        }
        renderJson(SUCCESS_MESSAGE);
    }


}
