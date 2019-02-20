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
import com.wtshop.model.GroupBuy;
import com.wtshop.service.*;
import com.wtshop.util.ApiResult;
import com.wtshop.util.ReadProper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mrFeng on 2019/2/18
 * 团购管理
 */
@ControllerBind(controllerKey = "/admin/groupBuy")
public class GroupBuyController extends BaseController {
    private GroupBuyService fuDaiService = enhance(GroupBuyService.class);
    public void list() {
        Pageable pageable = getBean(Pageable.class);
        pageable.setOrderProperty("orders");
        pageable.setOrderDirection("desc");
        setAttr("pageable", pageable);
        setAttr("page", fuDaiService.findPage(pageable));
        render("/admin/groupBuy/list.ftl");
    }

    //去添加页面
    public void add() {
        setAttr("fuDaiQuestionImage", ReadProper.getResourceValue("fuDaiDefaultImage"));
        render("/admin/groupBuy/add.ftl");
    }

    //保存福袋信息
    public void save() {
        // 图片
        List<UploadFile> uploadFiles = getFiles();
        GroupBuy fuDai = getModel( GroupBuy.class);


        Long productId = getParaToLong("productId");
        Integer productImageIndex = getBeans(ProductImage.class, "productImages").size();

        if (CollectionUtils.isNotEmpty(uploadFiles)) {
            List<ProductImage> productImages = new ArrayList<ProductImage>();
            for (int i = 0; i < productImageIndex; i++) {
                ProductImage productImage = getBean(ProductImage.class, "productImages[" + i + "]");
                productImage.setFile(getFile("productImages[" + i + "].file"));
                productImages.add(productImage);
            }
           // fuDai.setProductImagesConverter(productImages);

        }




        fuDai.setStatus(1);
        fuDaiService.save(fuDai);
        FudaiProduct fudaiProduct = new FudaiProduct(productId, fuDai.getId(), 1);

        redirect("list.jhtml");
    }

    //去修改页面
    public void toEdit() {
        Long fuDaiId = getParaToLong("id");
        GroupBuy group = fuDaiService.find(fuDaiId);
        setAttr("groupBuy", group);

        render("/admin/groupBuy/edit.ftl");
    }

    //修改福袋信息
    public void edit() {
        // 图片
        List<UploadFile> uploadFiles = getFiles();
        GroupBuy fuDai = getModel( GroupBuy.class);

        fuDai.setIsMarketable(getParaToBoolean("isMarketable", false));
        fuDai.setIsList(getParaToBoolean("isList", false));
        fuDai.setIsTop(getParaToBoolean("isTop", false));
        fuDai.setIsSinglepurchase(getParaToBoolean("isSinglepurchase", false));


        Long productId = getParaToLong("productId");









        fuDaiService.update(fuDai);

        redirect("/admin/groupBuy/list.jhtml");
    }

    //删除福袋
    public void delete() {
        Long[] ids = getParaValuesToLong("ids");
        fuDaiService.delete(ids);
        redirect("list.jhtml");
    }

    public void status() {
        Long id = getParaToLong("fudaiId");
        int status = getParaToInt("status");
        GroupBuy fuDai = fuDaiService.find(id);
        fuDai.setStatus(status);
        fuDaiService.update(fuDai);
        renderJson("1");
    }

    public void addGoods() {
        Long fuDaiId = getParaToLong("id");
        List list = fuDaiService.queryByFuDaiId(fuDaiId);
        GroupBuy fd = fuDaiService.find(fuDaiId);
        setAttr("fuDaiId", fuDaiId);
        setAttr("indexNum", list.size());
        setAttr("fuDaiProductList", list);
        setAttr("fd", fd);
        render("/admin/groupBuy/addGoods.ftl");
    }

    public void saveGoogs() {
        List<FudaiProduct> list = getModels(FudaiProduct.class);
        Long fuDaiId = getParaToLong("fuDaiId");
        fuDaiService.saveOrUpdate(list, fuDaiId);
        redirect("/admin/groupBuy/list.jhtml");
    }

    public void imgList() {
        long fudaiId = getParaToLong("id");
        setAttr("fudaiId", fudaiId);

        render("/admin/groupBuy/imglist.ftl");

    }



    public void delImg() {

        renderJson(ApiResult.success());
    }

    /**
     * 禁用福袋
     */
    public void disabled() {
        Long fudaiId = getParaToLong("id");
        GroupBuy fuDai = fuDaiService.find(fudaiId);
        fuDai.setStatus(FuDai.State_UnActive);
        fuDaiService.update(fuDai);
        redirect("list.jhtml");
    }


    /**
     * 启用福袋
     */
    public void publish() {
        Long fudaiId = getParaToLong("id");
        GroupBuy fuDai = fuDaiService.find(fudaiId);
        List<FudaiProduct> list = fuDaiService.findSubListByFudaiId(fuDai.getId());
        if (CollectionUtils.isEmpty(list) || list.size() <= fuDai.getNum()) {
            addFlashMessage(Message.errMsg("福袋副产品数量需要大于福袋要抽取的副产品数量"));
            redirect("list.jhtml");
            return;
        }
        fuDai.setStatus(FuDai.State_Active);
        fuDaiService.update(fuDai);
        redirect("list.jhtml");
    }


}
