package com.wtshop.controller.admin;

import com.alibaba.fastjson.JSONArray;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.upload.UploadFile;
import com.wtshop.Message;
import com.wtshop.Pageable;
import com.wtshop.entity.ProductImage;
import com.wtshop.model.FuDai;
import com.wtshop.model.FudaiImg;
import com.wtshop.model.FudaiProduct;
import com.wtshop.model.SpecialGoods;
import com.wtshop.service.*;
import com.wtshop.util.ApiResult;
import com.wtshop.util.ReadProper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 蔺哲 on 2017/7/11.
 */
@ControllerBind(controllerKey = "/admin/specialGoods")
public class SpecialGoodsController extends BaseController {
    private SpecialGoodsService fuDaiService = enhance(SpecialGoodsService.class);
    public void list() {
        Pageable pageable = getBean(Pageable.class);
        pageable.setOrderProperty("orders");
        pageable.setOrderDirection("desc");
        setAttr("pageable", pageable);
        setAttr("page", fuDaiService.findPage(pageable));
        render("/admin/specialGoods/list.ftl");
    }

    //去添加页面
    public void add() {
        setAttr("fuDaiQuestionImage", ReadProper.getResourceValue("fuDaiDefaultImage"));
        render("/admin/specialGoods/add.ftl");
    }

    //保存福袋信息
    public void save() {
        // 图片
        List<UploadFile> uploadFiles = getFiles();
        SpecialGoods fuDai = getModel(SpecialGoods.class);


        Long goodsId = getParaToLong("goodsId");
        Integer productImageIndex = getBeans(ProductImage.class, "productImages").size();
        fuDai.setStatus(1);
        fuDai.setGoodsId(goodsId);
        fuDaiService.save(fuDai);


        redirect("/admin/specialGoods/list.jhtml");
    }

    //去修改页面
    public void toEdit() {
        Long fuDaiId = getParaToLong("id");
        setAttr("fuDai", fuDaiService.find(fuDaiId));
        render("/admin/specialGoods/edit.ftl");
    }

    //修改福袋信息
    public void edit() {
        // 图片
        List<UploadFile> uploadFiles = getFiles();
        SpecialGoods fuDai = getModel(SpecialGoods.class);
        Long productId = getParaToLong("productId");
        fuDaiService.update(fuDai);
        redirect("/admin/specialGoods/list.jhtml");
    }

    //删除福袋
    public void delete() {
        Long[] ids = getParaValuesToLong("ids");
        fuDaiService.delete(ids);
        redirect("/admin/specialGoods/list.jhtml");
    }

    public void status() {
        Long id = getParaToLong("fudaiId");
        int status = getParaToInt("status");
        SpecialGoods fuDai = fuDaiService.find(id);
        fuDai.setStatus(status);
        fuDaiService.update(fuDai);
        renderJson("1");
    }

    public void addGoods() {
        Long fuDaiId = getParaToLong("id");
        List list = fuDaiService.queryByFuDaiId(fuDaiId);
        SpecialGoods fd = fuDaiService.find(fuDaiId);
        setAttr("fuDaiId", fuDaiId);
        setAttr("indexNum", list.size());
        setAttr("fuDaiProductList", list);
        setAttr("fd", fd);
        render("/admin/specialGoods/addGoods.ftl");
    }

    public void saveGoogs() {
        List<FudaiProduct> list = getModels(FudaiProduct.class);
        Long fuDaiId = getParaToLong("fuDaiId");
        fuDaiService.saveOrUpdate(list, fuDaiId);
        redirect("/admin/specialGoods/list.jhtml");
    }

    public void imgList() {
        long fudaiId = getParaToLong("id");
        setAttr("fudaiId", fudaiId);
        render("/admin/specialGoods/imglist.ftl");

    }


    /**
     * 禁用福袋
     */
    public void disabled() {
        Long fudaiId = getParaToLong("id");
        SpecialGoods fuDai = fuDaiService.find(fudaiId);
        fuDai.setStatus(FuDai.State_UnActive);
        fuDaiService.update(fuDai);
        redirect("/admin/specialGoods/list.jhtml");
    }


    /**
     * 启用福袋
     */
    public void publish() {
        Long fudaiId = getParaToLong("id");
        SpecialGoods fuDai = fuDaiService.find(fudaiId);
        fuDai.setStatus(FuDai.State_Active);
        fuDaiService.update(fuDai);
        redirect("/admin/specialGoods/list.jhtml");
    }


}
