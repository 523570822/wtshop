package com.wtshop.controller.admin;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;
import com.wtshop.Pageable;
import com.wtshop.model.GoodsTheme;
import com.wtshop.model.TargetPath;
import com.wtshop.model.ThemeProduct;
import com.wtshop.service.GoodsThemeService;
import com.wtshop.service.ProductCategoryService;
import com.wtshop.service.TargetPathService;
import com.wtshop.service.ThemeProductService;
import freemarker.template.utility.NumberUtil;

import java.util.List;

/**
 * Created by 蔺哲 on 2017/5/16.
 */
@ControllerBind(controllerKey = "/admin/goodsTheme")
public class GoodsThemeController extends BaseController{
    private GoodsThemeService goodsThemeService = enhance(GoodsThemeService.class);
    private ThemeProductService themeProductService = enhance(ThemeProductService.class);
    private ProductCategoryService productCategoryService = enhance(ProductCategoryService.class);
    private TargetPathService targetPathService=enhance(TargetPathService.class);
    /**
     * 专题列表
     */
    public void list(){
        Pageable pageable = getBean(Pageable.class);
        setAttr("pageable", pageable);
        setAttr("page",goodsThemeService.findPage(pageable));
        render("/admin/goodsTheme/list.ftl");
    }

    /**
     * 添加专题
     */
    public void add(){
        setAttr("productCategoryTree", productCategoryService.queryByGrade(0));
        setAttr("targetTitleTree",targetPathService.findAll());
        render("/admin/goodsTheme/add.ftl");
    }

    /**
     * 保存专题
     */
    public void save(){
        GoodsTheme goodsTheme = getModel(GoodsTheme.class);
        String targetId = getPara("targetId");
        if(!StrKit.isBlank(targetId)){
            Integer tarId = new Integer(targetId);
            goodsTheme.setTargetId(tarId);
        }
        goodsThemeService.save(goodsTheme);
        addFlashMessage(SUCCESS_MESSAGE);
        redirect("list.jhtml");
    }

    /**
     * 编辑专题
     */
    public void edit(){
       Long goodsThemeId = getParaToLong("id");
       setAttr("productCategoryTree", productCategoryService.queryByGrade(0));
       setAttr("goodsTheme",goodsThemeService.find(goodsThemeId));
       render("/admin/goodsTheme/edit.ftl");
    }

    /**
     * 更新专题
     */
    public void update(){
        GoodsTheme goodsTheme = getModel(GoodsTheme.class);
        String targetId = getPara("targetId");
        if(!StrKit.isBlank(targetId)){
            Integer tarId = new Integer(targetId);
            goodsTheme.setTargetId(tarId);
        }
        goodsThemeService.update(goodsTheme);
        addFlashMessage(SUCCESS_MESSAGE);
        redirect("list.jhtml");
    }

    /**
     * 删除专题
     */
    public void delete(){
        Long goodsThemeId = getParaToLong("ids");
        goodsThemeService.delete(goodsThemeId);
        renderJson(SUCCESS_MESSAGE);
    }

    /**
     * 添加专题对应商品
     */
    public void addGoods(){
        Long id = getParaToLong("id");
        setAttr("goodsThemeId",id);
        List list = themeProductService.queryByGoodsThemeId(id);
        setAttr("goodsNum",list.size());
        setAttr("themeProduct",list);
        render("/admin/goodsTheme/addGoods.ftl");
    }

    /**
     * 保存专题商品
     */
    public void saveGoogs(){
        Long goodsThemeId = getParaToLong("goodsThemeId");
        List<ThemeProduct> themeProducts = getModels(ThemeProduct.class);
        goodsThemeService.saveOrupdate(themeProducts,goodsThemeId);
        addFlashMessage(SUCCESS_MESSAGE);
        redirect("list.jhtml");
    }

    /**
     * 得到目标页面选项的二级标题
     */
    public void levelState(){
        int urltype = getParaToInt("urlType");
        renderJson(targetPathService.findByUrltype(urltype));
    }
}
