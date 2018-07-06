package com.wtshop.controller.admin;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;
import com.wtshop.Pageable;
import com.wtshop.model.Ad;
import com.wtshop.model.GoodsTheme;
import com.wtshop.model.TargetPath;
import com.wtshop.service.AdService;
import com.wtshop.service.GoodsThemeService;
import com.wtshop.service.TargetPathService;

import java.util.List;

/**
 * Created by admin on 2017/6/22.
 */
@ControllerBind(controllerKey = "/admin/targetPath")
public class TargetPathController extends BaseController{

    private TargetPathService targetPathService=enhance(TargetPathService.class);
    private GoodsThemeService goodsThemeService=enhance(GoodsThemeService.class);
    private AdService adService = enhance(AdService.class);

    public void list(){
        Pageable pageable = getBean(Pageable.class);
        setAttr("pageable", pageable);
        setAttr("page1",targetPathService.findAll());
        String targetId = getPara("targetId");
        if(!StrKit.isBlank(targetId)){
            Integer tarId = new Integer(targetId);
            setAttr("page",targetPathService.findById(tarId,pageable));
            setAttr("tarId",tarId);
        }else {
            setAttr("page",targetPathService.findLevel(pageable));
        }
        render("/admin/targetPath/list.ftl");
    }

    /**
     * 编辑路径
     */
    public void edit(){
        Long targetId = getParaToLong("id");
        setAttr("urlType",targetPathService.find(targetId).getUrltype());
        setAttr("targetPath",targetPathService.find(targetId));
        setAttr("targetPathList",targetPathService.findAll());
        render("/admin/targetPath/edit.ftl");
    }

    /**
     * 更新路径
     */
    public void update(){
        TargetPath targetPath = getModel(TargetPath.class);
//        Long targetId = getParaToLong("id");
        Long targetId=getParaToLong("targetId");
        TargetPath targetPath1=targetPathService.find(targetId);
        targetPath.setUrltype(targetPath1.getUrltype());
        targetPath.setLevelState(new Long(2));

        targetPathService.update(targetPath);
        addFlashMessage(SUCCESS_MESSAGE);
        redirect("list.jhtml");
    }
    /**
     * 删除路径
     */
    public void delete(){
        Long[] targetId = getParaValuesToLong("ids");
        List<GoodsTheme> goodsThemeList=goodsThemeService.findGoodsThemeByTarId(targetId);
        if(goodsThemeList.size()>0){
            for(GoodsTheme goodsTheme:goodsThemeList){
                goodsTheme.setTargetId(1);
                goodsThemeService.update(goodsTheme);
            }
        }
        List<Ad> adList=adService.findAdByTarId(targetId);
        if(adList.size()>0){
            for(Ad ad:adList){
                ad.setTargetId(new Long(1));
                adService.update(ad);
            }
        }
        targetPathService.delete(targetId);
        renderJson(SUCCESS_MESSAGE);
    }

    /**
     * 添加路径
     */
    public void add(){
        setAttr("targetPathList",targetPathService.findAll());
        render("/admin/targetPath/add.ftl");
    }

    /**
     * 保存路径
     */
    public void save(){
        TargetPath targetPath = getModel(TargetPath.class);
        Long targetId=getParaToLong("targetId");
        if(0 == targetId){
            TargetPath maxTargetPath = targetPathService.findMaxUrlType();
            targetPath.setUrltype(maxTargetPath.getUrltype()+1);
            targetPath.setLevelState(new Long(1));
        }else {
            TargetPath targetPath1=targetPathService.find(targetId);
            targetPath.setUrltype(targetPath1.getUrltype());
            targetPath.setLevelState(new Long(2));
        }

        targetPathService.save(targetPath);
        addFlashMessage(SUCCESS_MESSAGE);
        redirect("list.jhtml");
    }
}
