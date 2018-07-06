package com.wtshop.model;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jfinal.plugin.activerecord.Db;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wtshop.Setting;
import com.wtshop.TemplateConfig;
import com.wtshop.entity.ParameterValue;
import com.wtshop.entity.ProductImage;
import com.wtshop.entity.SpecificationItem;
import com.wtshop.entity.SpecificationItem.Entry;
import com.wtshop.model.base.BaseGoods;
import com.wtshop.util.ObjectUtils;
import com.wtshop.util.SystemUtils;

/**
 * Model - 货品
 */
public class Goods extends BaseGoods<Goods> {

    private static final long serialVersionUID = 6507667739400691441L;

    public static final Goods dao = new Goods();

    //  0/草稿, 2/待审核, 4/待产品主管审核, 6/待财务审核, 7/待财务主管审核, 8/上线
    public final static int State_Draft = 0;
    public final static int State_Review_ProductSpecialist = 2;
    public final static int State_Review_ProductManager = 4;
    public final static int State_Review_Financial = 6;
    public final static int State_Review_FinanceDirector = 7;
    public final static int State_Publish = 8;

    /**
     * 点击数缓存名称
     */
    public static final String HITS_CACHE_NAME = "goodsHits";

    /**
     * 属性值属性个数
     */
    public static final int ATTRIBUTE_VALUE_PROPERTY_COUNT = 20;

    /**
     * 属性值属性名称前缀
     */
    public static final String ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX = "attributeValue";

    //  审核状态
    public enum VerifyState {
        draft,
        productAudit,
        productManageAudit,
        financeAudit,
        financeManageAudit,
        release
    }

    /**
     * 类型
     */
    public enum Type {
        /**
         * 普通商品
         */
        general,
        /**
         * 兑换商品
         */
        exchange,
        /**
         * 赠品
         */
        gift,
        /**
         * 倒拍商品
         */
        auction
    }

    /**
     * 静态生成方式
     */
    public enum GenerateMethod {

        /**
         * 无
         */
        none,

        /**
         * 即时
         */
        eager,

        /**
         * 延时
         */
        lazy
    }

    /**
     * 排序类型
     */
    public enum OrderType {

        /**
         * 置顶降序
         */
        topDesc,

        /**
         * 价格升序
         */
        priceAsc,

        /**
         * 价格降序
         */
        priceDesc,

        /**
         * 销量降序
         */
        salesDesc,

        /**
         * 评分降序
         */
        scoreDesc,

        /**
         * 日期降序
         */
        dateDesc
    }

    /**
     * 排名类型
     */
    public enum RankingType {

        /**
         * 评分
         */
        score,

        /**
         * 评分数
         */
        scoreCount,

        /**
         * 周点击数
         */
        weekHits,

        /**
         * 月点击数
         */
        monthHits,

        /**
         * 点击数
         */
        hits,

        /**
         * 周销量
         */
        weekSales,

        /**
         * 月销量
         */
        monthSales,

        /**
         * 销量
         */
        sales
    }

    private Area area;

    /**
     * 获取地区
     *
     * @return 地区
     */
    public Area getArea() {
        if (ObjectUtils.isEmpty(area)) {
            area = Area.dao.findById(getAreaId());
        }
        return area;
    }

    /**
     * 设置地区
     *
     * @param area 地区
     */
    public void setArea(Area area) {
        this.area = area;
    }

    /**
     * 商品图片
     */
    private List<ProductImage> productImages = new ArrayList<ProductImage>();

    /**
     * 参数值
     */
    private List<ParameterValue> parameterValues = new ArrayList<ParameterValue>();

    /**
     * 规格项
     */
    private List<SpecificationItem> specificationItems = new ArrayList<SpecificationItem>();

    /**
     * 促销
     */
    private List<Promotion> promotions = new ArrayList<Promotion>();

    /**
     * 标签
     */
    private List<Tag> tags = new ArrayList<Tag>();

    /**
     * 功效
     */
    private List<Effect> effects = new ArrayList<Effect>();

    /**
     * 评论
     */
    private List<Review> reviews = new ArrayList<Review>();

    /**
     * 咨询
     */
    private List<Consultation> consultations = new ArrayList<Consultation>();

    /**
     * 收藏会员
     */
    private List<Member> favoriteMembers = new ArrayList<Member>();

    /**
     * 商品
     */
    private List<Product> products = new ArrayList<Product>();

    /**
     * 商品分类
     */
    private ProductCategory productCategory;

    /**
     * 品牌
     */
    private Brand brand;

    /**
     * 获取类型
     *
     * @return 类型
     */
    public Type getTypeName() {
        return Type.values()[getType()];
    }

    /**
     * 静态生成方式
     */
    public GenerateMethod getGenerateMethodName() {
        return GenerateMethod.values()[getGenerateMethod()];
    }


    /**
     * 获取商品分类
     *
     * @return 商品分类
     */
    public ProductCategory getProductCategory() {
        if (ObjectUtils.isEmpty(productCategory)) {
            productCategory = ProductCategory.dao.findById(getProductCategoryId());
        }
        return productCategory;
    }

    /**
     * 设置商品分类
     *
     * @param productCategory 商品分类
     */
    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }

    /**
     * 获取品牌
     *
     * @return 品牌
     */
    public Brand getBrand() {
        if (ObjectUtils.isEmpty(brand)) {
            brand = Brand.dao.findById(getBrandId());
        }
        return brand;
    }

    /**
     * 设置品牌
     *
     * @param brand 品牌
     */
    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    /**
     * 获取商品图片
     *
     * @return 商品图片
     */
    public List<ProductImage> getProductImagesConverter() {
        if (CollectionUtils.isEmpty(productImages)) {
            JSONArray productImageArrays = JSONArray.parseArray(getProductImages());
            if (CollectionUtils.isNotEmpty(productImageArrays)) {
                for (int i = 0; i < productImageArrays.size(); i++) {
                    productImages.add(JSONObject.parseObject(productImageArrays.getString(i), ProductImage.class));
                }
            }
        }
        return productImages;
    }


    /**
     * 设置商品图片
     *
     * @param productImages 商品图片
     */
    public void setProductImagesConverter(List<ProductImage> productImages) {
        this.productImages = productImages;
    }

    /**
     * 获取参数值
     *
     * @return 参数值
     */
    public List<ParameterValue> getParameterValuesConverter() {
        if (CollectionUtils.isEmpty(parameterValues)) {
            JSONArray parameterValueArrays = JSONArray.parseArray(getParameterValues());
            if (CollectionUtils.isNotEmpty(parameterValueArrays)) {
                for (int i = 0; i < parameterValueArrays.size(); i++) {
                    parameterValues.add(JSONObject.parseObject(parameterValueArrays.getString(i), ParameterValue.class));
                }
            }
        }
        return parameterValues;
    }

    /**
     * 设置参数值
     *
     * @param parameterValues 参数值
     */
    public void setParameterValuesConverter(List<ParameterValue> parameterValues) {
        this.parameterValues = parameterValues;
    }

    /**
     * 获取规格项
     *
     * @return 规格项
     */
    public List<SpecificationItem> getSpecificationItemsConverter() {
        if (CollectionUtils.isEmpty(specificationItems)) {
            JSONArray specificationItemArrays = JSONArray.parseArray(getSpecificationItems());
            if (CollectionUtils.isNotEmpty(specificationItemArrays)) {
                for (int i = 0; i < specificationItemArrays.size(); i++) {
                    specificationItems.add(JSONObject.parseObject(specificationItemArrays.getString(i), SpecificationItem.class));
                }

            }
        }
        sortSpecificationItemsByIds(specificationItems);
        return specificationItems;
    }

    /**
     * 按数据库添加ID 排序
     *
     * @param specItems
     */
    private void sortSpecificationItemsByIds(List<SpecificationItem> specificationItems) {
        if (CollectionUtils.isEmpty(specificationItems))
            return;
        for (SpecificationItem items : specificationItems) {
            Collections.sort(items.getEntries(), new Comparator<Entry>() {
                public int compare(Entry obj1, Entry obj2) {
                    Entry entry1 = obj1;
                    Entry entry2 = obj2;
                    return entry1.getId().compareTo(entry2.getId());
                }
            });
        }
    }

    /**
     * 设置规格项
     *
     * @param specificationItems 规格项
     */
    public void setSpecificationItemConverter(List<SpecificationItem> specificationItems) {
        this.specificationItems = specificationItems;
    }

    /**
     * 获取促销
     *
     * @return 促销
     */
    public List<Promotion> getPromotions() {
        if (CollectionUtils.isEmpty(promotions)) {
            String sql = "SELECT p.* FROM `goods_promotion` gp LEFT JOIN `promotion` p ON gp.`promotions` = p.`id` WHERE gp.`goods` = ?";
            promotions = Promotion.dao.find(sql, getId());
        }
        return promotions;
    }

    public List<GoodsPromotion> getGoodsPromotionList() {
        String sql = "SELECT * from goods_promotion  g WHERE g.goods=" + getId();
        return GoodsPromotion.dao.find(sql);
    }

    /**
     * 设置促销
     *
     * @param promotions 促销
     */
    public void setPromotions(List<Promotion> promotions) {
        this.promotions = promotions;
    }

    /**
     * 获取标签
     *
     * @return 标签
     */
    public List<Tag> getTags() {
        if (CollectionUtils.isEmpty(tags)) {
            String sql = "SELECT t.* FROM `goods_tag` gt LEFT JOIN `tag` t ON gt.`tags` = t.`id` WHERE gt.`goods` = ?";
            tags = Tag.dao.find(sql, getId());
        }
        return tags;
    }

    public List<GoodsTag> getGoodsTag() {
        String sql = "select * from goods_tag where goods=" + getId();
        return GoodsTag.dao.find(sql);
    }

    /**
     * 设置标签
     *
     * @param tags 标签
     */
    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }


    /**
     * 获取功效
     *
     * @return 功效
     */
    public List<Effect> getEffects() {
        if (CollectionUtils.isEmpty(effects)) {
            String sql = "SELECT t.* FROM `goods_effct` gt LEFT JOIN `effect` t ON gt.`effect` = t.`id` WHERE gt.`goods` = ?";
            effects = Effect.dao.find(sql, getId());
        }
        return effects;
    }

    public List<GoodsEffct> getGoodsEffct() {
        String sql = "select * from goods_effct where goods=" + getId();
        return GoodsEffct.dao.find(sql);
    }

    /**
     * 设置功效
     *
     * @param effects 功效
     */
    public void setEffects(List<Effect> effects) {
        this.effects = effects;
    }

    /**
     * 获取评论
     *
     * @return 评论
     */
    public List<Review> getReviews() {
        if (CollectionUtils.isEmpty(reviews)) {
            String sql = "SELECT * FROM review WHERE goods_id = ?";
            reviews = Review.dao.find(sql, getId());
        }
        return reviews;
    }

    /**
     * 设置评论
     *
     * @param reviews 评论
     */
    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    /**
     * 获取咨询
     *
     * @return 咨询
     */
    public List<Consultation> getConsultations() {
        if (CollectionUtils.isEmpty(consultations)) {
            String sql = "SELECT * FROM consultation WHERE goods_id = ?";
            consultations = Consultation.dao.find(sql, getId());
        }
        return consultations;
    }

    /**
     * 设置咨询
     *
     * @param consultations 咨询
     */
    public void setConsultations(List<Consultation> consultations) {
        this.consultations = consultations;
    }

    /**
     * 获取收藏会员
     *
     * @return 收藏会员
     */
    public List<Member> getFavoriteMembers() {
        if (CollectionUtils.isEmpty(favoriteMembers)) {
            String sql = "SELECT m.* FROM `member_favorite_goods` mfg LEFT JOIN `member` m ON mfg.`favorite_members` = m.`id` WHERE mfg.`favorite_goods` = ?";
            favoriteMembers = Member.dao.find(sql, getId());
        }
        return favoriteMembers;
    }

    /**
     * 设置收藏会员
     *
     * @param favoriteMembers 收藏会员
     */
    public void setFavoriteMembers(List<Member> favoriteMembers) {
        this.favoriteMembers = favoriteMembers;
    }

    /**
     * 获取商品
     *
     * @return 商品
     */
    public List<Product> getProducts() {
        if (CollectionUtils.isEmpty(products)) {
            String sql = "SELECT * FROM product WHERE goods_id = ?";
            products = Product.dao.find(sql, getId());
        }
        return products;
    }

    /**
     * 设置商品
     *
     * @param products 商品
     */
    public void setProducts(List<Product> products) {
        this.products = products;
    }

    /**
     * 获取路径
     *
     * @return 路径
     */
    public String getPath() {
        TemplateConfig templateConfig = SystemUtils.getTemplateConfig("goodsContent");
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("goods", this);
        return templateConfig.getRealStaticPath(model);
    }

    /**
     * 获取URL
     *
     * @return URL
     */
    public String getUrl() {
        Setting setting = SystemUtils.getSetting();
        return setting.getSiteUrl() + getPath();
    }

    /**
     * 获取缩略图
     *
     * @return 缩略图
     */
    public String getThumbnail() {
        if (CollectionUtils.isEmpty(getProductImagesConverter())) {
            return null;
        }
        return getProductImagesConverter().get(0).getThumbnail();
    }

    /**
     * 获取是否库存警告
     *
     * @return 是否库存警告
     */
    public boolean getIsStockAlert() {
        return CollectionUtils.exists(getProducts(), new Predicate() {
            public boolean evaluate(Object object) {
                Product product = (Product) object;
                return product != null && product.getIsStockAlert();
            }
        });
    }

    /**
     * 获取是否缺货
     *
     * @return 是否缺货
     */
    public boolean getIsOutOfStock() {
        return CollectionUtils.exists(getProducts(), new Predicate() {
            public boolean evaluate(Object object) {
                Product product = (Product) object;
                return product != null && product.getIsOutOfStock();
            }
        });
    }

    /**
     * 获取规格项条目ID
     *
     * @return 规格项条目ID
     */
    public List<Integer> getSpecificationItemEntryIds() {
        List<Integer> specificationItemEntryIds = new ArrayList<Integer>();
        if (CollectionUtils.isNotEmpty(getSpecificationItemsConverter())) {
            for (SpecificationItem specificationItem : getSpecificationItemsConverter()) {
                if (CollectionUtils.isNotEmpty(specificationItem.getEntries())) {
                    for (Entry entry : specificationItem.getEntries()) {
                        specificationItemEntryIds.add(entry.getId());
                    }
                }
            }
            Collections.sort(specificationItemEntryIds);
        }
        return specificationItemEntryIds;
    }

    /**
     * 获取默认商品
     *
     * @return 默认商品
     */
    public Product getDefaultProduct() {
        return (Product) CollectionUtils.find(getProducts(), new Predicate() {
            public boolean evaluate(Object object) {
                Product product = (Product) object;
                return product != null && product.getIsDefault();
            }
        });
    }

    /**
     * 获取有效促销
     *
     * @return 有效促销
     */
    @SuppressWarnings("unchecked")
    public Set<Promotion> getValidPromotions() {
        if (!Type.general.equals(getType()) || CollectionUtils.isEmpty(getPromotions())) {
            return Collections.emptySet();
        }

        return new HashSet<Promotion>(CollectionUtils.select(getPromotions(), new Predicate() {
            public boolean evaluate(Object object) {
                Promotion promotion = (Promotion) object;
                return promotion != null && promotion.hasBegun() && !promotion.hasEnded() && CollectionUtils.isNotEmpty(promotion.getMemberRanks());
            }
        }));
    }

    /**
     * 是否存在规格
     *
     * @return 是否存在规格
     */
    public boolean hasSpecification() {
        return CollectionUtils.isNotEmpty(getSpecificationItemsConverter());
    }

    /**
     * 判断促销是否有效
     *
     * @param promotion 促销
     * @return 促销是否有效
     */
    public boolean isValid(Promotion promotion) {
        if (!Type.general.equals(getType()) || promotion == null || !promotion.hasBegun() || promotion.hasEnded() || CollectionUtils.isEmpty(promotion.getMemberRanks())) {
            return false;
        }
        if (getValidPromotions().contains(promotion)) {
            return true;
        }
        return false;
    }

    /**
     * 获取属性值
     *
     * @param attribute 属性
     * @return 属性值
     */
    public String getAttributeValue(Attribute attribute) {
        if (attribute == null || attribute.getPropertyIndex() == null) {
            return null;
        }

        try {
            String propertyName = ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX + attribute.getPropertyIndex();
            return (String) PropertyUtils.getProperty(this, propertyName);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 设置属性值
     *
     * @param attribute      属性
     * @param attributeValue 属性值
     */
    public void setAttributeValue(Attribute attribute, String attributeValue) {
        if (attribute == null || attribute.getPropertyIndex() == null) {
            return;
        }

        try {
            String propertyName = ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX + attribute.getPropertyIndex();
            PropertyUtils.setProperty(this, propertyName, attributeValue);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 移除所有属性值
     */
    public void removeAttributeValue() {
        for (int i = 0; i < ATTRIBUTE_VALUE_PROPERTY_COUNT; i++) {
            String propertyName = ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX + i;
            try {
                PropertyUtils.setProperty(this, propertyName, null);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e.getMessage(), e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e.getMessage(), e);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
    }

    /**
     * 持久化前处理
     */
    public void prePersist() {
        if (CollectionUtils.isNotEmpty(getProductImagesConverter())) {
            Collections.sort(getProductImagesConverter());
        }
    }

    /**
     * 更新前处理
     */
    public void preUpdate() {
        if (getTotalScore() != null && getScoreCount() != null && getScoreCount() > 0) {
            setScore((float) getTotalScore() / getScoreCount());
        } else {
            setScore(0F);
        }
        if (CollectionUtils.isNotEmpty(getProductImagesConverter())) {
            Collections.sort(getProductImagesConverter());
        }
    }

    /**
     * 删除前处理
     */
    public void preRemove() {
        List<Member> favoriteMembers = getFavoriteMembers();
        if (favoriteMembers != null) {
            for (Member favoriteMember : favoriteMembers) {
                favoriteMember.getFavoriteGoods().remove(this);
            }
        }
    }

    /**
     * 判断是否为新建对象
     *
     * @return 是否为新建对象
     */
    public boolean isNew() {
        return getId() == null;
    }


    public List<String> getTagIdList(){
        List<String> tagIdList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(this.getTags())){
            for (Tag tag : this.getTags()){
                tagIdList.add(tag.getId().toString());
            }
        }
        return tagIdList;
    }

    public List<String> getEffectIdList(){
        List<String> effectIdList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(this.getEffects())){
            for (Effect effect : this.getEffects()){
                effectIdList.add(effect.getId().toString());
            }
        }
        return effectIdList;
    }

}
