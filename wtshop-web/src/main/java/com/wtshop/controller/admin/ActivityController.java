package com.wtshop.controller.admin;

import com.alibaba.fastjson.JSONArray;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.upload.UploadFile;
import com.wtshop.Message;
import com.wtshop.Pageable;
import com.wtshop.entity.ProductImage;
import com.wtshop.model.*;
import com.wtshop.service.*;
import com.wtshop.util.ApiResult;
import com.wtshop.util.DateUtils;
import com.wtshop.util.ReadProper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 蔺哲 on 2017/7/11.
 */
@ControllerBind(controllerKey = "/admin/activity")
public class ActivityController extends BaseController {

    private ActivityService activityService=enhance(ActivityService.class);
    private FuDaiService fuDaiService = enhance(FuDaiService.class);
    private FuDaiProductService fuDaiProductService = enhance(FuDaiProductService.class);
    private FudaiImgService fudaiImgService = enhance(FudaiImgService.class);    //福袋列表
    private ProductImageService productImageService = enhance(ProductImageService.class);

    public void list() {
        Pageable pageable = getBean(Pageable.class);

        pageable.setOrderProperty("create_date");
        pageable.setOrderDirection("desc");


        Page<Activity> activiList = activityService.findPage(pageable);

        List<Activity> ddd = activiList.getList();

        for (Activity activity : activiList.getList()) {

            Date  time=new Date();
            Date form=activity.getBeginDate();
            Date to = activity.getEndDate();
            if(form!=null&&to!=null&&activity.getStatus()==0){
                int state= DateUtils.belongCalendar(time,form,to);
                activity.getEndDate();
                activity.put("isTime",state);
            }



        }

        setAttr("pageable", pageable);
        setAttr("page", activiList);
        render("/admin/activity/list.ftl");
    }

    //去添加页面
    public void add() {
        setAttr("fuDaiQuestionImage", ReadProper.getResourceValue("fuDaiDefaultImage"));
        //用户选择日期
        Date beginDate = null;
        Date endDate = null;
        Date stime = null;
        Date etime = null;
        Date now = new Date();
        if (beginDate == null) {
            beginDate =now ;

        }
        if (endDate == null) {

            endDate = org.apache.commons.lang3.time.DateUtils.addDays(now, 1);
            stime = org.apache.commons.lang3.time.DateUtils.addDays(now, 1);
        }
        //设置日期限制



        etime = now;
        setAttr("stime", stime);
        setAttr("etime", etime);
        setAttr("beginDate", beginDate);
        setAttr("endDate", endDate);
        render("/admin/activity/add.ftl");
    }

    //保存福袋信息
    public void save() {
        List<UploadFile> uploadFiles = getFiles();


        Activity activity = getModel(Activity.class);





      activityService.save(activity);
        addFlashMessage(SUCCESS_MESSAGE);
        redirect("/admin/activity/list.jhtml");
    }

    //去修改页面
    public void toEdit() {
        Long id = getParaToLong("id");
        Activity activity = activityService.find(id);

        Date now = new Date();
        Date stime = null;
        Date etime = null;
        Date beginDate = null;
        Date endDate = null;
        if (activity.getBeginDate() == null) {
            beginDate =now ;

        }else {
            beginDate=activity.getBeginDate();
        }
        if (activity.getEndDate() == null) {

            endDate = org.apache.commons.lang3.time.DateUtils.addDays(now, 1);

        }else {

            endDate=activity.getEndDate();
        }
        if (stime == null) {
            stime =now ;

        }else{
            stime=activity.getBeginDate();
        }
        if (etime == null) {

            etime = org.apache.commons.lang3.time.DateUtils.addDays(now, 1);

        }else{
            etime=activity.getEndDate();
        }
        setAttr("beginDate", beginDate);
        setAttr("endDate", endDate);
        setAttr("stime", stime);
        setAttr("etime", etime);
        setAttr("activity", activity);
        render("/admin/activity/add.ftl");
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
        Long activityId = getParaToLong("id");
        List list = activityService.queryByActivityId(activityId);
        Activity fd = activityService.find(activityId);
        setAttr("activityId", activityId);
        setAttr("indexNum", list.size());
        setAttr("activityProductList", list);
        setAttr("fd", fd);
        render("/admin/activity/addGoods.ftl");
    }

    public void saveGoogs() {
        List<ActivityProduct> list = getModels(ActivityProduct.class);
        Long activityId = getParaToLong("activityId");
        activityService.saveOrUpdate(list, activityId);
        redirect("/admin/activity/addGoods.jhtml?id="+activityId);
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
        Long id = getParaToLong("id");
        Activity activity = activityService.find(id);
        activity.setStatus(FuDai.State_UnActive);
        activityService.update(activity);
        redirect("/admin/activity/list.jhtml");
    }


    /**
     * 启用福袋
     */
    public void publish() {
        Long id = getParaToLong("id");
        Activity activity = activityService.find(id);


        activity.setStatus(FuDai.State_Active);
        activityService.update(activity);
        redirect("/admin/activity/list.jhtml");
    }


}
