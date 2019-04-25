package com.wtshop.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.ext.route.ControllerBind;
import com.wtshop.Filter;
import com.wtshop.Pageable;
import com.wtshop.constants.Code;
import com.wtshop.dao.GoodsReviewCommentDao;
import com.wtshop.model.*;
import com.wtshop.service.*;
import com.wtshop.shiro.core.SubjectKit;
import com.wtshop.util.ApiResult;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.*;

/**
 * Created by Administrator on 2017/8/28.
 */
@ControllerBind(controllerKey = "/admin/goodsReview")
public class GoodsReviewController extends BaseController {

    private AdminService adminService = enhance(AdminService.class);
    private GoodsReviewService goodsReviewService = enhance(GoodsReviewService.class);
    private GoodsReviewCommentService goodsReviewCommentService = enhance(GoodsReviewCommentService.class);
    private GoodsReviewCommentDao goodsReviewCommentDao = enhance(GoodsReviewCommentDao.class);
    private GoodsService goodsService = enhance(GoodsService.class);
    private ProductCategoryService productCategoryService = enhance(ProductCategoryService.class);
    private BrandService brandService = enhance(BrandService.class);
    private PromotionService promotionService = enhance(PromotionService.class);
    private TagService tagService = enhance(TagService.class);
    private ProductImageService productImageService = enhance(ProductImageService.class);
    private ParameterValueService parameterValueService = enhance(ParameterValueService.class);
    private SpecificationItemService specificationItemService = enhance(SpecificationItemService.class);
    private AttributeService attributeService = enhance(AttributeService.class);
    private SpecificationService specificationService = enhance(SpecificationService.class);
    private AreaService areaService = enhance(AreaService.class);
    private EffectService effectService = enhance(EffectService.class);
    private GoodEffectService goodEffectService = enhance(GoodEffectService.class);

    public void list() {
        Boolean isDelete = getParaToBoolean("isDelete", false);
        Pageable pageable = getBean(Pageable.class);
        pageable.getFilters().add(Filter.eq("is_delete", isDelete ? Code.TRUE : Code.FALSE));
        setAttr("pageable", pageable);
        setAttr("page", goodsReviewService.findGoodsReviewPage(pageable));
        setAttr("isDelete", isDelete);
        render("/admin/goodsReview/list.ftl");
    }

    /**
     * 提交审核
     */
    public void review() {
        Long id = getParaToLong("id");
        int state = getParaToInt("state");
        GoodsReview goodsReview = goodsReviewService.find(id);
        if (goodsReview == null || goodsReview.getIsDelete().intValue() == Code.TRUE) {
            renderJson(ApiResult.fail("审核记录不存在"));
            return;
        }
        if (state != Goods.State_Review_ProductSpecialist && state != Goods.State_Review_ProductManager
                && state != Goods.State_Review_Financial && state != Goods.State_Review_FinanceDirector) {
            renderJson(ApiResult.fail("错误的审核状态"));
            return;
        }

        if (state == Goods.State_Review_ProductSpecialist && !SubjectKit.getSubject().hasRole(Code.R_ProductSpecialist)) {
            renderJson(ApiResult.fail("没有产品专员相关权限"));
            return;
        }
        if (state == Goods.State_Review_ProductManager && !SubjectKit.getSubject().hasRole(Code.R_ProductSpecialist)) {
            renderJson(ApiResult.fail("没有产品主管相关权限"));
            return;
        }
        if (state == Goods.State_Review_Financial && !SubjectKit.getSubject().hasRole(Code.R_ProductManager)) {
            renderJson(ApiResult.fail("没有财务相关权限"));
            return;
        }
        if (state == Goods.State_Review_FinanceDirector && !SubjectKit.getSubject().hasRole(Code.R_Finance)) {
            renderJson(ApiResult.fail("没有财务主管相关权限"));
            return;
        }
        goodsReview.setState(state);
        goodsReviewService.update(goodsReview);
        renderJson(ApiResult.success("提交审核成功"));
    }

    /**
     * 打回
     */
    public void reject() {
        Long id = getParaToLong("id");
        int state = getParaToInt("state");
        GoodsReview goodsReview = goodsReviewService.find(id);
        if (goodsReview == null || goodsReview.getIsDelete().intValue() == Code.TRUE) {
            renderJson(ApiResult.fail("审核记录不存在"));
            return;
        }
        if (state != Goods.State_Review_ProductSpecialist && state != Goods.State_Review_ProductManager
                && state != Goods.State_Review_Financial && state != Goods.State_Review_FinanceDirector) {
            renderJson(ApiResult.fail("错误的审核状态"));
            return;
        }
        if (goodsReview.getState().intValue() != Goods.State_Review_ProductSpecialist && goodsReview.getState().intValue() != Goods.State_Review_ProductManager
                && goodsReview.getState().intValue() != Goods.State_Review_Financial && goodsReview.getState().intValue() != Goods.State_Review_FinanceDirector) {
            renderJson(ApiResult.fail("错误的审核记录状态"));
            return;
        }
        if (!SubjectKit.getSubject().hasRole(Code.R_ProductSpecialist) && !SubjectKit.getSubject().hasRole(Code.R_ProductManager)
                && !SubjectKit.getSubject().hasRole(Code.R_Finance) && !SubjectKit.getSubject().hasRole(Code.R_FinanceDirector)) {
            renderJson(ApiResult.fail("没有相关操作权限"));
        }
        Admin admin = adminService.getCurrent();
        String comment = getPara("comment");
        if (org.apache.commons.lang3.StringUtils.isNotBlank(comment)){
            GoodsReviewComment goodsReviewComment = new GoodsReviewComment();
            goodsReviewComment.setSummary(comment);
            goodsReviewComment.setCommentDate(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            goodsReviewComment.setIsDelete(Code.FALSE);
            goodsReviewComment.setAdminId(admin.getId());
            goodsReviewComment.setCreateDate(new Date());
            goodsReviewComment.setGoodsReviewId(goodsReview.getId());
            goodsReviewCommentService.save(goodsReviewComment);
        }
        goodsReview.setState(state);
        goodsReviewService.update(goodsReview);
        renderJson(ApiResult.success("已打回到产品专员"));
    }

    /**
     * 查看审批意见
     */
    public void comment() {
        Long id = getParaToLong("id");
        List<GoodsReviewComment> goodsReviewCommentList = goodsReviewCommentDao.findListBySql("select * from goods_review_comment where goods_review_id = " + id + " order by create_date desc");
        List<Map> list = new ArrayList<Map>();
        if (CollectionUtils.isNotEmpty(goodsReviewCommentList)){
            for (GoodsReviewComment goodsReviewComment : goodsReviewCommentList){
                Map<String, String> map = new HashMap<String, String>();
                map.put("admin_name", goodsReviewComment.getAdmin().getName());
                map.put("create_time", goodsReviewComment.getCommentDate());
                map.put("comment", goodsReviewComment.getSummary());
                list.add(map);
            }
        }
        renderJson(ApiResult.success(list));
    }

    /**
     * 关闭
     */
    public void reviewClose() {
        Long id = getParaToLong("id");
        GoodsReview goodsReview = goodsReviewService.find(id);
        if (goodsReview == null || goodsReview.getIsDelete().intValue() == Code.TRUE) {
            renderJson(ApiResult.fail("审核记录不存在"));
            return;
        }
//        if (goodsReview.getState().intValue() != Goods.State_Draft && goodsReview.getState().intValue() != Goods.State_Review_ProductSpecialist && goodsReview.getState().intValue() != Goods.State_Review_ProductManager
//                && goodsReview.getState().intValue() != Goods.State_Review_Financial && goodsReview.getState().intValue() != Goods.State_Review_FinanceDirector) {
//            renderJson(ApiResult.fail("错误的审核记录状态"));
//            return;
//        }
        goodsReview.setIsDelete(Code.TRUE);
        goodsReview.update();
        renderJson(ApiResult.success());
    }

    /**
     * 编辑
     */
    public void edit() {
        Long id = getParaToLong("id");
        GoodsReview goodsReview = goodsReviewService.find(id);
        if (goodsReview == null || goodsReview.getIsDelete().intValue() == Code.TRUE) {
            renderJson(ApiResult.fail("审核记录不存在"));
            return;
        }
        if (goodsReview.getState().intValue() != Goods.State_Review_ProductSpecialist && goodsReview.getState().intValue() != Goods.State_Review_ProductManager
                && goodsReview.getState().intValue() != Goods.State_Review_Financial && goodsReview.getState().intValue() != Goods.State_Review_FinanceDirector) {
            renderJson(ApiResult.fail("错误的审核记录状态"));
            return;
        }
        Goods goods = JSONObject.toJavaObject(JSONObject.parseObject(goodsReview.getSummary()), Goods.class);
        boolean sb = goods.hasSpecification();
        setAttr("isReview", "1");
        setAttr("goodsReview", goodsReview);
        setAttr("types", Goods.Type.values());
        setAttr("productCategoryTree", productCategoryService.findTree());
        setAttr("brands", brandService.findAll());
        setAttr("effects", effectService.findAll());
        setAttr("promotions", promotionService.findAll());
        setAttr("tags", tagService.findList(Tag.Type.goods));
        setAttr("specifications", specificationService.findAll());
        setAttr("area", areaService.findAll());
        setAttr("goods", goods);
        render("/admin/goods/edit.ftl");
    }

    /**
     * 通过审批
     */
    public void reviewPass() {
        Long pageGoodsReviewId = getParaToLong("goodsReviewId");
        if (pageGoodsReviewId == null){
            renderJson(ApiResult.fail("审核记录不存在"));
            return;
        }
        GoodsReview goodsReview = goodsReviewService.find(pageGoodsReviewId);
        if (goodsReview == null || goodsReview.getIsDelete().intValue() == Code.TRUE) {
            renderJson(ApiResult.fail("审核记录不存在"));
            return;
        }
        if (goodsReview.getState().intValue() != Goods.State_Review_FinanceDirector) {
            renderJson(ApiResult.fail("错误的审核记录状态"));
            return;
        }

        Goods goods = JSONObject.toJavaObject(JSONObject.parseObject(goodsReview.getSummary()), Goods.class);

        Admin admin = adminService.getCurrent();

        List<Long> promotionsIdList = new ArrayList<Long>();
        if (CollectionUtils.isNotEmpty(goods.getPromotions())){
            for (Promotion entity : goods.getPromotions()){
                promotionsIdList.add(entity.getId());
            }
        }

        List<Long> tagsIdList = new ArrayList<Long>();
        if (CollectionUtils.isNotEmpty(goods.getTags())){
            for (Tag entity : goods.getTags()){
                tagsIdList.add(entity.getId());
            }
        }

        List<Long> effectIdList = new ArrayList<Long>();
        if (CollectionUtils.isNotEmpty(goods.getEffects())){
            for (Effect entity : goods.getEffects()){
                effectIdList.add(entity.getId());
            }
        }
        boolean ssssss = goods.hasSpecification();
        goodsReview.setState(Goods.State_Publish);

        Product product = goods.getDefaultProduct();
     //   ssssss123

        List<Product> products = goods.getProducts();
        if (goods.hasSpecification()) {
            if (CollectionUtils.isEmpty(products)) {
                addFlashMessage(new com.wtshop.Message(com.wtshop.Message.Type.error, "商品规格不能为空!"));
                redirect("/admin/goods/list.jhtml");
                return;
            }
            goodsService.update(goods,  products,  admin,promotionsIdList.toArray(new Long[]{}), tagsIdList.toArray(new Long[]{}), effectIdList.toArray(new Long[]{}));

        } else {
            if (product == null) {
                addFlashMessage(new com.wtshop.Message(com.wtshop.Message.Type.error, "产品不能为空!"));
                redirect("/admin/goods/list.jhtml");
                return;
            }
            goodsService.update(goods,product, admin, promotionsIdList.toArray(new Long[]{}), tagsIdList.toArray(new Long[]{}), effectIdList.toArray(new Long[]{}));
        }

        goodsReview.update();
        renderJson(ApiResult.success());
    }

}
