package com.wtshop.controller.admin;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.plugin.redis.Redis;
import com.jfinal.upload.UploadFile;
import com.wtshop.Pageable;
import com.wtshop.constants.Code;
import com.wtshop.entity.ParameterValue;
import com.wtshop.entity.ProductImage;
import com.wtshop.entity.SpecificationItem;
import com.wtshop.entity.SpecificationValue;
import com.wtshop.model.*;
import com.wtshop.service.*;
import com.wtshop.shiro.core.SubjectKit;
import com.wtshop.util.ApiResult;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

/**
 * Controller - 货品
 */
@ControllerBind(controllerKey = "/admin/goods")
public class GoodsController extends BaseController {



    private GoodsService goodsService = enhance(GoodsService.class);
    private ProductService productService = enhance(ProductService.class);
    private ProductCategoryService productCategoryService = enhance(ProductCategoryService.class);
    private BrandService brandService = enhance(BrandService.class);
    private PromotionService promotionService = enhance(PromotionService.class);
    private TagService tagService = enhance(TagService.class);
    private ProductImageService productImageService = enhance(ProductImageService.class);
    private ParameterValueService parameterValueService = enhance(ParameterValueService.class);
    private SpecificationItemService specificationItemService = enhance(SpecificationItemService.class);
    private AttributeService attributeService = enhance(AttributeService.class);
    private SpecificationService specificationService = enhance(SpecificationService.class);
    private AdminService adminService = enhance(AdminService.class);
    private AreaService areaService = enhance(AreaService.class);
    private EffectService effectService = enhance(EffectService.class);
    private GoodEffectService goodEffectService = enhance(GoodEffectService.class);
    private GoodsReviewService goodsReviewService = enhance(GoodsReviewService.class);
    private InterestCategoryService interestCategoryService = enhance(InterestCategoryService.class);
    public final static String kGoodsComment = "Goods:Comment:";

    /**
     * 检查编号是否存在
     */
    public void checkSn() {
        String sn = getPara("goods.sn");
        if (StringUtils.isEmpty(sn)) {
            renderJson(false);
            return;
        }
        renderJson(!goodsService.snExists(sn));
    }

    /**
     * 获取参数
     */
    public void parameters() {
        Long productCategoryId = getParaToLong("productCategoryId");
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        ProductCategory productCategory = productCategoryService.find(productCategoryId);
        if (productCategory == null || CollectionUtils.isEmpty(productCategory.getParameters())) {
            renderJson(data);
            return;
        }
        for (Parameter parameter : productCategory.getParameters()) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("group", parameter.getParameterGroup());
            item.put("names", parameter.getNamesConverter());
            data.add(item);
        }
        renderJson(data);
    }

    /**
     * 获取属性
     */
    public void attributes() {
        Long productCategoryId = getParaToLong("productCategoryId");
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        ProductCategory productCategory = productCategoryService.find(productCategoryId);
        if (productCategory == null || CollectionUtils.isEmpty(productCategory.getAttributes())) {
            renderJson(data);
            return;
        }
        for (Attribute attribute : productCategory.getAttributes()) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("id", attribute.getId());
            item.put("name", attribute.getName());
            item.put("options", attribute.getOptionsConverter());
            data.add(item);
        }
        renderJson(data);
    }

    /**
     * 获取规格
     */
    public void specifications() {
        Long productCategoryId = getParaToLong("productCategoryId");
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        ProductCategory productCategory = productCategoryService.find(productCategoryId);
        if (productCategory == null || CollectionUtils.isEmpty(productCategory.getSpecifications())) {
            renderJson(data);
            return;
        }
        for (Specification specification : productCategory.getSpecifications()) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("name", specification.getName());
            item.put("options", specification.getOptionsConverter());
            data.add(item);
        }
        renderJson(data);
    }

    /**
     * 添加
     */
    public void add() {
        List<InterestCategory> interestCategory = interestCategoryService.findAll();
        setAttr("area", areaService.findAll());
        setAttr("types", Goods.Type.values());
        setAttr("productCategoryTree",interestCategory);
        setAttr("brands", brandService.findAll());
        setAttr("effects", effectService.findAll());
        setAttr("promotions", promotionService.findAll());
        setAttr("tags", tagService.findList(Tag.Type.goods));
        setAttr("specifications", specificationService.findAll());
        render("/admin/goods/add.ftl");
    }

    /**
     * 提交审核
     */
    public void review() {
        String id = getPara("id");
        int state = getParaToInt("state");
        Goods goods = goodsService.find(Long.valueOf(id));
        if (goods == null || goods.getIsDelete().booleanValue()) {
            renderJson(ApiResult.fail("商品不存在"));
            return;
        }
        if (goods.getIsMarketable().booleanValue()) {
            renderJson(ApiResult.fail("商品已上架"));
            return;
        }
        if (state != Goods.State_Review_ProductSpecialist && state != Goods.State_Review_ProductManager
                && state != Goods.State_Review_Financial && state != Goods.State_Review_FinanceDirector) {
            renderJson(ApiResult.fail("错误的审核状态"));
            return;
        }

        if (state == Goods.State_Review_ProductManager && !SubjectKit.getSubject().hasRole(Code.R_ProductSpecialist)) {
            renderJson(ApiResult.fail("没有产品专员相关权限"));
            return;
        }
        if (state == Goods.State_Review_Financial && !SubjectKit.getSubject().hasRole(Code.R_ProductManager)) {
            renderJson(ApiResult.fail("没有产品主管相关权限"));
            return;
        }
        if (state == Goods.State_Review_FinanceDirector && !SubjectKit.getSubject().hasRole(Code.R_Finance)) {
            renderJson(ApiResult.fail("没有财务相关权限"));
            return;
        }
        if (state == Goods.State_Publish && !SubjectKit.getSubject().hasRole(Code.R_FinanceDirector)) {
            renderJson(ApiResult.fail("没有财务主管相关权限"));
            return;
        }
        goods.setCheck(state);
        goodsService.update(goods);
        renderJson(ApiResult.success("提交审核成功"));
    }

    /**
     * 打回
     */
    public void reject() {
        String id = getPara("id");
        int state = getParaToInt("state");
        Goods goods = goodsService.find(Long.valueOf(id));
        if (goods == null || goods.getIsDelete().booleanValue()) {
            renderJson(ApiResult.fail("商品不存在"));
            return;
        }
        if (goods.getIsMarketable().booleanValue()) {
            renderJson(ApiResult.fail("商品已上架"));
            return;
        }
        if (state != ReverseAuction.State_Review_ProductSpecialist){
            renderJson(ApiResult.fail("错误的目标状态"));
            return;
        }
        if (goods.getCheck().intValue() != ReverseAuction.State_Review_ProductSpecialist && goods.getCheck().intValue() != ReverseAuction.State_Review_ProductManager
                && goods.getCheck().intValue() != ReverseAuction.State_Review_Financial && goods.getCheck().intValue() != ReverseAuction.State_Review_FinanceDirector) {
            renderJson(ApiResult.fail("错误的活动状态"));
            return;
        }
        if (!SubjectKit.getSubject().hasRole(Code.R_ProductSpecialist) && !SubjectKit.getSubject().hasRole(Code.R_ProductManager)
                && !SubjectKit.getSubject().hasRole(Code.R_Finance) && !SubjectKit.getSubject().hasRole(Code.R_FinanceDirector)) {
            renderJson(ApiResult.fail("没有相关操作权限"));
        }
        Admin admin = adminService.getCurrent();
        String comment = getPara("comment");
        if (StringUtils.isNotBlank(comment)){
            Map<String, String> map = new HashMap<String, String>();
            map.put("admin_name", admin.getName());
            map.put("comment", comment);
            map.put("create_time", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            Redis.use().zadd(kGoodsComment + goods.getId(), System.currentTimeMillis(), map);
        }
        goods.setCheck(state);
        goodsService.update(goods);
        renderJson(ApiResult.success("已打回到产品专员"));
    }

    /**
     * 查看审批意见
     */
    public void comment() {
        String id = getPara("id");
        Set<Map<String, String>> set = Redis.use().zrevrange(kGoodsComment + id, 0, Long.MAX_VALUE);
        renderJson(ApiResult.success(set));
    }

    // 倒拍活动下架
    @Before(Tx.class)
    public void offline() {
        String id = getPara("id");
        Goods goods = goodsService.find(Long.valueOf(id));
        if (goods == null || goods.getIsDelete().booleanValue()) {
            renderJson(ApiResult.fail("商品不存在"));
            return;
        }
        if (!goods.getIsMarketable().booleanValue()) {
            renderJson(ApiResult.fail("商品已下架"));
            return;
        }
        goods.setIsMarketable(false);
        goods.setCheck(Goods.State_Draft);
        goodsService.update(goods);
        Db.update("update `goods_review` gr  set gr.`is_delete` = 1 where gr.`goods_id` = " + goods.getId() + " and gr.`state` != 8 and gr.`is_delete` = " + Code.FALSE);
        Redis.use().del(kGoodsComment + goods.getId());
        renderJson(ApiResult.success("下架成功"));
    }

    // 倒拍活动上架
    public void online() {
        String id = getPara("id");
        Goods goods = goodsService.find(Long.valueOf(id));
        if (goods == null || goods.getIsDelete().booleanValue()) {
            renderJson(ApiResult.fail("商品不存在"));
            return;
        }
        if (goods.getIsMarketable().booleanValue()) {
            renderJson(ApiResult.fail("商品已上架"));
            return;
        }
        goods.setIsMarketable(true);
        goods.setCheck(Goods.State_Publish);
        goodsService.update(goods);
        Redis.use().del(kGoodsComment + goods.getId());
        renderJson(ApiResult.success("上架成功"));
    }


    /**
     * 保存
     */
    public void save() {
        List<UploadFile> uploadFiles = getFiles();
        Goods goods = getModel(Goods.class);
        String typeName = getPara("type");
        goods.setType(StrKit.notBlank(typeName) ? Goods.Type.valueOf(typeName).ordinal() : null);
        goods.setIsMarketable(getParaToBoolean("isMarketable", false));
        goods.setIsList(getParaToBoolean("isList", false));
        goods.setIsTop(getParaToBoolean("isTop", false));
        goods.setIsDelivery(getParaToBoolean("isDelivery", false));
        goods.setIsVip(getParaToBoolean("isVip", false));
        goods.setIsDelete(false);
        goods.setPrice(BigDecimal.ZERO);
        goods.setMarketPrice(BigDecimal.ZERO);
        Long areaId = getParaToLong("areaId");
        goods.setAreaId(areaId);
        Product product = getModel(Product.class);
        HttpServletRequest request = getRequest();
        goods.setOperateIp(request.getRemoteAddr());
        Long productCategoryId = getParaToLong("productCategoryId");
        Long brandId = getParaToLong("brandId");
        Long[] promotionIds = getParaValuesToLong("promotionIds");
        Long[] tagIds = getParaValuesToLong("tagIds");
        Long[] effectIds = getParaValuesToLong("effectIds");

        // 图片
        Integer productImageIndex = getBeans(ProductImage.class, "productImages").size();
        if (CollectionUtils.isNotEmpty(uploadFiles)) {
            List<ProductImage> productImages = new ArrayList<ProductImage>();
            for (int i = 0; i < productImageIndex; i++) {
                ProductImage productImage = getBean(ProductImage.class, "productImages[" + i + "]");
                productImage.setFile(getFile("productImages[" + i + "].file"));
                productImages.add(productImage);
            }
            goods.setProductImagesConverter(productImages);
            productImageService.filter(goods.getProductImagesConverter());
        }


        InterestCategory productCategory = interestCategoryService.find(productCategoryId);
        if (productCategory != null) {
            goods.setProductCategoryId(productCategory.getId());
        }
        Brand brand = brandService.find(brandId);
        if (brand != null) {
            goods.setBrandId(brand.getId());
        }
        goods.setPromotions(new ArrayList<Promotion>(promotionService.findList(promotionIds)));
        goods.setTags(new ArrayList<Tag>(tagService.findList(tagIds)));
        goods.setEffects(new ArrayList<Effect>(effectService.findList(effectIds)));

        if (StringUtils.isNotEmpty(goods.getSn()) && goodsService.snExists(goods.getSn())) {
            addFlashMessage(new com.wtshop.Message(com.wtshop.Message.Type.error, "商品编号不能为空或已存在!"));
            redirect("list.jhtml");
            return;
        }

        //	添加的商品默认是待审核状态
        goods.setVerifyState(Goods.VerifyState.productManageAudit.ordinal());

        Admin admin = adminService.getCurrent();
        goodsService.save(goods, product, admin);
        addFlashMessage(SUCCESS_MESSAGE);
        redirect("/admin/goods/list.jhtml");
    }


    /**
     * 编辑
     */
    public void edit() {
        List<InterestCategory> interestCategory = interestCategoryService.findAll();
        Long id = getParaToLong("id");
        setAttr("types", Goods.Type.values());
        setAttr("productCategoryTree",interestCategory);
        setAttr("brands", brandService.findAll());
        setAttr("effects", effectService.findAll());
        setAttr("promotions", promotionService.findAll());
        setAttr("tags", tagService.findList(Tag.Type.goods));
        setAttr("specifications", specificationService.findAll());
        setAttr("area", areaService.findAll());
        Goods goods = goodsService.find(id);
        setAttr("goods", goods);
        render("/admin/goods/edit.ftl");
    }

    /**
     * 更新
     */
    public void update() {
        List<UploadFile> uploadFiles = getFiles();
        HttpServletRequest request = getRequest();
        Product product = getModel(Product.class);
        Long productCategoryId = getParaToLong("productCategoryId");
        Long brandId = getParaToLong("brandId");
        Long[] promotionIds = getParaValuesToLong("promotionIds");
        Long[] tagIds = getParaValuesToLong("tagIds");
        Long areaId = getParaToLong("areaId");
        Long[] effectIds = getParaValuesToLong("effectIds");
        String introduction=getPara("introduction");

        Goods goods = getModel(Goods.class);
        goods.setPrice(BigDecimal.ZERO);
        goods.setMarketPrice(BigDecimal.ZERO);
        goods.setIntroduction(introduction);
        goods.setIsMarketable(getParaToBoolean("isMarketable", false));
        goods.setIsList(getParaToBoolean("isList", false));
        goods.setIsTop(getParaToBoolean("isTop", false));
        goods.setIsDelivery(getParaToBoolean("isDelivery", false));
        goods.setIsVip(getParaToBoolean("isVip", false));
        goods.setIsDelete(false);
        goods.setAreaId(areaId);
        goods.setOperateIp(request.getRemoteAddr());
        Goods pGoods = goodsService.find(goods.getId());
        goods.setType(pGoods.getType());


        // 图片
        Integer productImageIndex = getBeans(ProductImage.class, "productImages").size();
        if (productImageIndex > 0 ) {
            List<ProductImage> productImages = new ArrayList<ProductImage>();
            for (int i = 0; i < productImageIndex; i++) {
                ProductImage productImage = getBean(ProductImage.class, "productImages[" + i + "]");
                productImage.setFile(getFile("productImages[" + i + "].file"));
                productImages.add(productImage);
            }
            goods.setProductImagesConverter(productImages);
            productImageService.filter(goods.getProductImagesConverter());
        } else  {


        }

        // 参数
        Integer parameterIndex = getBeans(ParameterValue.class, "parameterValues").size();
        if (0 < parameterIndex) {
            List<ParameterValue> parameterValues = new ArrayList<ParameterValue>();
            for (int i = 0; i < parameterIndex; i++) {
                ParameterValue parameterValue = getBean(ParameterValue.class, "parameterValues[" + i + "]");
                List<ParameterValue.Entry> entries = getBeans(ParameterValue.Entry.class, "parameterValueEntrys[" + i + "].entries");
                parameterValue.setEntries(entries);
                parameterValues.add(parameterValue);
            }
            goods.setParameterValues(JSONArray.toJSONString(parameterValues));
            goods.setParameterValuesConverter(parameterValues);
            parameterValueService.filter(goods.getParameterValuesConverter());
        }

        // 产品组
        Integer productsIndex = getBeans(Product.class, "productList").size();
        List<Product> products = new ArrayList<Product>();
        if (0 < productsIndex) {
            for (int i = 0; i < productsIndex; i++) {
                Product sProduct = getModel(Product.class, "productList[" + i + "]");
                List<SpecificationValue> specificationValues = getBeans(SpecificationValue.class, "productLists[" + i + "].specificationValues");
                sortList(specificationValues, "id", "ASC");
                sProduct.setSpecificationValues(JSONArray.toJSONString(specificationValues));
                products.add(sProduct);
            }
            productService.filter(products);
        }

        // 规格
        Integer specificationItemsIndex = getBeans(SpecificationItem.class, "specificationItems").size();
        List<SpecificationItem> specificationItems = new ArrayList<SpecificationItem>();
        if (0 < specificationItemsIndex) {
            for (int i = 0; i < specificationItemsIndex; i++) {
                SpecificationItem specificationItem = getBean(SpecificationItem.class, "specificationItems[" + i + "]");
                List<SpecificationItem.Entry> entries = getBeans(SpecificationItem.Entry.class, "specificationItemEntrys[" + i + "].entries");
                specificationItem.setEntries(entries);
                specificationItems.add(specificationItem);
            }
            goods.setSpecificationItems(JSONArray.toJSONString(specificationItems));
            goods.setSpecificationItemConverter(specificationItems);
            specificationItemService.filter(goods.getSpecificationItemsConverter());
        }

        ProductCategory productCategory = productCategoryService.find(productCategoryId);
        if (productCategory != null) {
            goods.setProductCategoryId(productCategory.getId());
        }
        Brand brand = brandService.find(brandId);
        if (brand != null) {
            goods.setBrandId(brand.getId());
        }
        goods.setPromotions(new ArrayList<Promotion>(promotionService.findList(promotionIds)));
        goods.setTags(new ArrayList<Tag>(tagService.findList(tagIds)));
        goods.setEffects(new ArrayList<Effect>(effectService.findList(effectIds)));

        Admin admin = adminService.getCurrent();


            goodsService.update(goods, product, admin, promotionIds, tagIds, effectIds);



        addFlashMessage(SUCCESS_MESSAGE);
        redirect("/admin/goods/list.jhtml");
    }


    /**
     * 申请更新
     */
    public void updateReview() {
        List<UploadFile> uploadFiles = getFiles();
        HttpServletRequest request = getRequest();
        Product product = getModel(Product.class);
        Long productCategoryId = getParaToLong("productCategoryId");
        Long brandId = getParaToLong("brandId");
        Long[] promotionIds = getParaValuesToLong("promotionIds");
        Long[] tagIds = getParaValuesToLong("tagIds");
        Long areaId = getParaToLong("areaId");
        Long[] effectIds = getParaValuesToLong("effectIds");
     //   String introduction=getPara("introduction");
        Goods goods = getModel(Goods.class);
      //  goods.setIntroduction(introduction);
        goods.setIsMarketable(getParaToBoolean("isMarketable", false));
        goods.setIsList(getParaToBoolean("isList", false));
        goods.setIsTop(getParaToBoolean("isTop", false));
        goods.setIsDelivery(getParaToBoolean("isDelivery", false));
        goods.setIsVip(getParaToBoolean("isVip", false));
        goods.setIsDelete(false);
        goods.setAreaId(areaId);
        goods.setPrice(BigDecimal.ZERO);
        goods.setMarketPrice(BigDecimal.ZERO);
        goods.setOperateIp(request.getRemoteAddr());
        Goods pGoods = goodsService.find(goods.getId());
        goods.setType(pGoods.getType());

        //  缓存内容
        Admin admin = adminService.getCurrent();
        GoodsReview goodsReview = new GoodsReview();
        goodsReview.setCreateDate(new Date());
        goodsReview.setIsDelete(Code.FALSE);
        goodsReview.setAdminId(admin.getId());
        goodsReview.setGoodsId(goods.getId());
        goodsReview.setState(Goods.State_Review_ProductManager);


        // 图片
        Integer productImageIndex = getBeans(ProductImage.class, "productImages").size();
        if (productImageIndex > 0 ) {
            List<ProductImage> productImages = new ArrayList<ProductImage>();
            for (int i = 0; i < productImageIndex; i++) {
                ProductImage productImage = getBean(ProductImage.class, "productImages[" + i + "]");
                productImage.setFile(getFile("productImages[" + i + "].file"));
                productImages.add(productImage);
            }
            goods.setProductImagesConverter(productImages);
            productImageService.filter(goods.getProductImagesConverter());

            //  保存为json
            goods.put("productImagesConverter", JSONObject.toJSON(goods.getProductImagesConverter()));
            //  保存图片内容
            productImageService.generate(goods.getProductImagesConverter());
            List<ProductImage> productImagesConverter = goods.getProductImagesConverter();
            if (CollectionUtils.isNotEmpty(productImagesConverter)) {
                List<ProductImage> tempProductImages = new ArrayList<ProductImage>();
                for (ProductImage productImage : productImagesConverter) {
                    tempProductImages.add(productImage);
                }
                //  保存为json
                goods.put("productImagesConverter", JSONObject.toJSON(tempProductImages));
            }
        } else  {

        }

        // 参数
        Integer parameterIndex = getBeans(ParameterValue.class, "parameterValues").size();
        if (0 < parameterIndex) {
            List<ParameterValue> parameterValues = new ArrayList<ParameterValue>();
            for (int i = 0; i < parameterIndex; i++) {
                ParameterValue parameterValue = getBean(ParameterValue.class, "parameterValues[" + i + "]");
                List<ParameterValue.Entry> entries = getBeans(ParameterValue.Entry.class, "parameterValueEntrys[" + i + "].entries");
                parameterValue.setEntries(entries);
                parameterValues.add(parameterValue);
            }
            goods.setParameterValues(JSONArray.toJSONString(parameterValues));
            goods.setParameterValuesConverter(parameterValues);
            parameterValueService.filter(goods.getParameterValuesConverter());

            //  保存为json
            goods.put("parameterValues", goods.getParameterValues());
            goods.put("parameterValuesConverter", JSONObject.toJSON(goods.getParameterValuesConverter()));

        }

        // 产品组
        Integer productsIndex = getBeans(Product.class, "productList").size();
        List<Product> products = new ArrayList<Product>();
        if (0 < productsIndex) {
            for (int i = 0; i < productsIndex; i++) {
                Product sProduct = getModel(Product.class, "productList[" + i + "]");
                List<SpecificationValue> specificationValues = getBeans(SpecificationValue.class, "productLists[" + i + "].specificationValues");
                sortList(specificationValues, "id", "ASC");
                sProduct.setSpecificationValues(JSONArray.toJSONString(specificationValues));
                products.add(sProduct);
            }
            productService.filter(products);
            goods.put("products", goods.getProducts());
        }

        // 规格
        Integer specificationItemsIndex = getBeans(SpecificationItem.class, "specificationItems").size();
        List<SpecificationItem> specificationItems = new ArrayList<SpecificationItem>();
        if (0 < specificationItemsIndex) {
            for (int i = 0; i < specificationItemsIndex; i++) {
                SpecificationItem specificationItem = getBean(SpecificationItem.class, "specificationItems[" + i + "]");
                List<SpecificationItem.Entry> entries = getBeans(SpecificationItem.Entry.class, "specificationItemEntrys[" + i + "].entries");
                specificationItem.setEntries(entries);
                specificationItems.add(specificationItem);
            }
            goods.setSpecificationItems(JSONArray.toJSONString(specificationItems));
            goods.setSpecificationItemConverter(specificationItems);
            specificationItemService.filter(goods.getSpecificationItemsConverter());

            goods.put("specificationItems", goods.getSpecificationItems());
            goods.put("specificationItemConverter", JSONObject.toJSON(goods.getSpecificationItemsConverter()));
        }

        ProductCategory productCategory = productCategoryService.find(productCategoryId);
        if (productCategory != null) {
            goods.setProductCategoryId(productCategory.getId());
        }
        Brand brand = brandService.find(brandId);
        if (brand != null) {
            goods.setBrandId(brand.getId());
        }

        goods.setPromotions(new ArrayList<Promotion>(promotionService.findList(promotionIds)));
        goods.put("promotions", goods.getPromotions());

        goods.setTags(new ArrayList<Tag>(tagService.findList(tagIds)));
        goods.put("tags", goods.getTags());

        goods.setEffects(new ArrayList<Effect>(effectService.findList(effectIds)));
        goods.put("effects", goods.getEffects());

        if (!goods.hasSpecification()) {
            if (product == null) {
                addFlashMessage(new com.wtshop.Message(com.wtshop.Message.Type.error, "产品不能为空!"));
                redirect("list.jhtml");
                return;
            }
            Product defaultProduct = goodsService.prepareReviewProduct(goods, product, admin);
            List<Product> productList = new ArrayList<Product>();
            productList.add(defaultProduct);
            goods.put("products", productList);
        }
        goodsService.update(goods);
        goodsReview.setSummary(goods.toJson());
        goodsReviewService.save(goodsReview);
        renderJson(ApiResult.success());
    }

    //商品审核页面
    public void verify() {
        Long goodsId = getParaToLong("goodsId");
        Long verifyId = getParaToLong("verifyId");
        Long draft = getParaToLong("draft");
        setAttr("verifyId", verifyId);
        setAttr("draft", draft);
        setAttr("goodsId", goodsId);
        setAttr("types", Goods.Type.values());
        setAttr("productCategoryTree", productCategoryService.findTree());
        setAttr("brands", brandService.findAll());
        setAttr("promotions", promotionService.findAll());
        setAttr("effects", effectService.findAll());
        setAttr("tags", tagService.findList(Tag.Type.goods));
        setAttr("specifications", specificationService.findAll());
        setAttr("area", areaService.findAll());
//        Map<String, Object> map = goodsService.getVerifyJsonGoods(verifyId, goodsId);
//        setAttr("goods", map.get("jgoods"));
//        setAttr("product", map.get("jproduct"));
//        setAttr("jgoods_promotions", map.get("jgoods_promotions"));
//        setAttr("jGoodsTag", map.get("jGoodsTag"));
//        setAttr("oldgoods", map.get("oldgoods"));
//        setAttr("oldProduct", map.get("oldProdcut"));
//        setAttr("gpdiff", map.get("gpdiff"));
//        setAttr("tagdiff", map.get("tagdiff"));
//        setAttr("jGoodsEffect", map.get("jGoodsEffect"));
//        setAttr("effectdiff", map.get("effectdiff"));
//        setAttr("productList", map.get("productList"));
        render("/admin/goods/verify.ftl");
        //renderJson(getAttr("json"));
    }

    /**
     * 列表
     */
    public void list() {
        String typeName = getPara("type");
        Goods.Type type = StrKit.notBlank(typeName) ? Goods.Type.valueOf(typeName) : null;
        Long productCategoryId = getParaToLong("productCategoryId");
        Long brandId = getParaToLong("brandId");
        Long promotionId = getParaToLong("promotionId");
        Long tagId = getParaToLong("tagId");
        Boolean isMarketable = getParaToBoolean("isMarketable");
        Boolean isList = getParaToBoolean("isList");
        Boolean isTop = getParaToBoolean("isTop");
        Boolean isOutOfStock = getParaToBoolean("isOutOfStock");
        Boolean isStockAlert = getParaToBoolean("isStockAlert");
        Pageable pageable = getBean(Pageable.class);
        Boolean isVip = getParaToBoolean("isVip");
        ProductCategory productCategory = productCategoryService.find(productCategoryId);
        Brand brand = brandService.find(brandId);
        Promotion promotion = promotionService.find(promotionId);
        Tag tag = tagService.find(tagId);
        setAttr("types", Goods.Type.values());
        setAttr("productCategoryTree", productCategoryService.findTree());
        setAttr("brands", brandService.findAll());
        setAttr("promotions", promotionService.findAll());
        setAttr("tags", tagService.findList(Tag.Type.goods));
        setAttr("effects", effectService.findAll());
        setAttr("type", type);
        setAttr("productCategoryId", productCategoryId);
        setAttr("brandId", brandId);
        setAttr("promotionId", promotionId);
        setAttr("tagId", tagId);
        setAttr("isMarketable", isMarketable);
        setAttr("isList", isList);
        setAttr("isTop", isTop);
        setAttr("isOutOfStock", isOutOfStock);
        setAttr("isStockAlert", isStockAlert);
        setAttr("isVip", isVip);
        setAttr("pageable", pageable);
        setAttr("page", goodsService.findPage(isVip, false, type, productCategory, brand, promotion, tag, null, null, null, isMarketable, isList, isTop, isOutOfStock, isStockAlert, null, null, pageable));
        render("/admin/goods/list.ftl");
    }

    /**
     * 删除
     */
    public void delete() {
        Long[] ids = getParaValuesToLong("ids");
        if (ids != null) {
            for (Long id : ids) {
                Goods goods = goodsService.find(id);
                goods.setIsDelete(true);
                goodsService.update(goods);
            }

        }
        renderJson(SUCCESS_MESSAGE);
    }



}