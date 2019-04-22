package com.wtshop.controller.admin;

import com.alibaba.fastjson.JSONArray;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.upload.UploadFile;
import com.wtshop.Message;
import com.wtshop.Pageable;
import com.wtshop.entity.ProductImage;
import com.wtshop.model.Activity;
import com.wtshop.model.FuDai;
import com.wtshop.model.FudaiImg;
import com.wtshop.model.FudaiProduct;
import com.wtshop.service.FuDaiProductService;
import com.wtshop.service.FuDaiService;
import com.wtshop.service.FudaiImgService;
import com.wtshop.service.ProductImageService;
import com.wtshop.util.ApiResult;
import com.wtshop.util.ReadProper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 蔺哲 on 2017/7/11.
 */
@ControllerBind(controllerKey = "/admin/fuDai")
public class FuDaiController extends BaseController {
    private FuDaiService fuDaiService = enhance(FuDaiService.class);
    private FuDaiProductService fuDaiProductService = enhance(FuDaiProductService.class);
    private FudaiImgService fudaiImgService = enhance(FudaiImgService.class);    //福袋列表
    private ProductImageService productImageService = enhance(ProductImageService.class);

    public void list() {
        Pageable pageable = getBean(Pageable.class);
        pageable.setOrderProperty("orders");
        pageable.setOrderDirection("desc");
        setAttr("pageable", pageable);
        setAttr("page", fuDaiService.findPage(pageable));
        render("/admin/fuDai/list.ftl");
    }

    //去添加页面
    public void add() {
        setAttr("fuDaiQuestionImage", ReadProper.getResourceValue("fuDaiDefaultImage"));
        render("/admin/fuDai/add.ftl");
    }

    //保存福袋信息
    public void save() {
        // 图片
        List<UploadFile> uploadFiles = getFiles();
        FuDai fuDai = getModel(FuDai.class);


        Long productId = getParaToLong("productId");
        Integer productImageIndex = getBeans(ProductImage.class, "productImages").size();

        if (CollectionUtils.isNotEmpty(uploadFiles)) {
            List<ProductImage> productImages = new ArrayList<ProductImage>();
            for (int i = 0; i < productImageIndex; i++) {
                ProductImage productImage = getBean(ProductImage.class, "productImages[" + i + "]");
                productImage.setFile(getFile("productImages[" + i + "].file"));
                productImages.add(productImage);
            }
            fuDai.setProductImagesConverter(productImages);
            fuDai.setStatus(0);
            productImageService.filter(fuDai.getProductImagesConverter());
        }

        //生成图片
        productImageService.generate(fuDai.getProductImagesConverter());
        List<ProductImage> productImagesConverter = fuDai.getProductImagesConverter();
        if (CollectionUtils.isNotEmpty(productImagesConverter)) {
            List<ProductImage> productImages = new ArrayList<ProductImage>();
            for (ProductImage productImage : productImagesConverter) {
                productImages.add(productImage);
            }
            fuDai.setProductImages(JSONArray.toJSONString(productImages));
        }

        if (StringUtils.isEmpty(fuDai.getImage()) && StringUtils.isNotEmpty(fuDai.getThumbnail())) {
            fuDai.setImage(fuDai.getThumbnail());
        }

        fuDai.setStatus(1);
        fuDaiService.save(fuDai);
        FudaiProduct fudaiProduct = new FudaiProduct(productId, fuDai.getId(), 1);
        fuDaiProductService.save(fudaiProduct);
        redirect("/admin/fuDai/list.jhtml");
    }

    //去修改页面
    public void toEdit() {
        Long fuDaiId = getParaToLong("id");
        setAttr("fuDai", fuDaiService.find(fuDaiId));
        render("/admin/fuDai/edit.ftl");
    }

    //修改福袋信息
    public void edit() {
        // 图片
        List<UploadFile> uploadFiles = getFiles();
        FuDai fuDai = getModel(FuDai.class);
        Long productId = getParaToLong("productId");
        Integer productImageIndex = getBeans(ProductImage.class, "productImages").size();
        List<ProductImage> productImages = new ArrayList<ProductImage>();
        for (int i = 0; i < productImageIndex; i++) {
            ProductImage productImage = getBean(ProductImage.class, "productImages[" + i + "]");
            productImage.setFile(getFile("productImages[" + i + "].file"));
            productImages.add(productImage);
        }
        fuDai.setProductImagesConverter(productImages);
        productImageService.filter(fuDai.getProductImagesConverter());


        //生成图片
        productImageService.generate(fuDai.getProductImagesConverter());
        List<ProductImage> productImagesConverter = fuDai.getProductImagesConverter();
        if (CollectionUtils.isNotEmpty(productImagesConverter)) {
            fuDai.setProductImages(JSONArray.toJSONString(productImages));
        }

        if (StringUtils.isEmpty(fuDai.getImage()) && StringUtils.isNotEmpty(fuDai.getThumbnail())) {
            fuDai.setImage(fuDai.getThumbnail());
        }
        fuDai.setStatus(0);
        fuDaiService.update(fuDai);
        fuDaiProductService.updateProduct(fuDai.getId(), productId);
        redirect("list.jhtml");
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
        FuDai fuDai = fuDaiService.find(id);
        fuDai.setStatus(status);
        fuDaiService.update(fuDai);
        renderJson("1");
    }

    public void addGoods() {
        Long fuDaiId = getParaToLong("id");
        List list = fuDaiService.queryByFuDaiId(fuDaiId);
        FuDai fd = fuDaiService.find(fuDaiId);
        setAttr("fuDaiId", fuDaiId);
        setAttr("indexNum", list.size());
        setAttr("fuDaiProductList", list);
        setAttr("fd", fd);
        render("/admin/fuDai/addGoods.ftl");
    }

    public void saveGoogs() {
        List<FudaiProduct> list = getModels(FudaiProduct.class);
        Long fuDaiId = getParaToLong("fuDaiId");
        fuDaiService.saveOrUpdate(list, fuDaiId);
        redirect("/admin/fuDai/list.jhtml");
    }

    public void imgList() {
        long fudaiId = getParaToLong("id");
        setAttr("fudaiId", fudaiId);
        setAttr("imgList", fudaiImgService.getImgList(fudaiId));
        render("/admin/fuDai/imglist.ftl");

    }

    public void saveImg() {
        FudaiImg img = getModel(FudaiImg.class);
        if (img.get("id") != null) {
            fudaiImgService.update(img);
        } else {
            fudaiImgService.save(img);
        }

        renderJson(ApiResult.success());
    }


    public void delImg() {
        fudaiImgService.delete(getParaToLong("id"));
        renderJson(ApiResult.success());
    }

    /**
     * 禁用福袋
     */
    public void disabled() {
        Long fudaiId = getParaToLong("id");
        FuDai fuDai = fuDaiService.find(fudaiId);
        fuDai.setStatus(FuDai.State_UnActive);
        fuDaiService.update(fuDai);
        redirect("list.jhtml");
    }


    /**
     * 启用福袋
     */
    public void publish() {
        Long fudaiId = getParaToLong("id");
        FuDai fuDai = fuDaiService.find(fudaiId);
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
