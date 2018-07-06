package com.wtshop.controller.admin;

import com.jfinal.aop.Enhancer;
import com.jfinal.ext.route.ControllerBind;
import com.wtshop.Pageable;
import com.wtshop.model.CharacterCommend;
import com.wtshop.model.LikeCommend;
import com.wtshop.service.CharacterService;
import com.wtshop.service.GoodsService;
import com.wtshop.service.LikeService;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static com.wtshop.api.controller.BaseAPIController.convertToLong;

/**
 * Created by sq on 2017/9/7.
 */


@ControllerBind(controllerKey = "/admin/like")
public class LikeController extends BaseController{

    private LikeService likeService = Enhancer.enhance(LikeService.class);
    private GoodsService goodsService = Enhancer.enhance(GoodsService.class);

    /**
     * 列表
     */
    public void list(){
        Pageable pageable = getBean(Pageable.class);
        setAttr("pageable", pageable);
        setAttr("page", likeService.findPages(pageable));
        render("/admin/like/list.ftl");

    }

    /**
     * 添加
     */
    public void add(){
        List<LikeCommend> likeCommends = likeService.findAll();
        List<Long> goodsList = new ArrayList<Long>();
        if(likeCommends != null && likeCommends.size() > 0){
            for(LikeCommend likeCommend : likeCommends){
                if(!goodsList.contains(likeCommend.getGoodsId())){
                    goodsList.add(likeCommend.getGoodsId());
                }
            }
        }
        Pageable pageable = getBean(Pageable.class);
        pageable.setPageSize(10);
        setAttr("pageable", pageable);
        setAttr("page", goodsService.findPage(pageable ,goodsList));
        render("/admin/like/goods.ftl");
    }

    /**
     * 商品
     */
    public void goodsList(){
        String[] values = StringUtils.split(getPara("ids"), ",");
        Long[] ids = values == null ? null :convertToLong(values);
        for(Long id : ids){
            LikeCommend likeCommend = new LikeCommend();
            likeCommend.setGoodsId(id);
            likeService.save(likeCommend);
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
            likeService.delete(id);
        }
        renderJson(SUCCESS_MESSAGE);
    }


}
