package com.wtshop.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.aop.Enhancer;
import com.jfinal.ext.plugin.monogodb.MongoKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.plugin.ehcache.CacheKit;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.wtshop.Filter;
import com.wtshop.Pageable;
import com.wtshop.Setting;
import com.wtshop.dao.*;
import com.wtshop.entity.Organ;
import com.wtshop.entity.ProductImage;
import com.wtshop.model.*;
import com.wtshop.util.ApiResult;
import com.wtshop.util.Assert;
import com.wtshop.util.RedisUtil;
import com.wtshop.util.SystemUtils;
import com.xiaoleilu.hutool.util.ArrayUtil;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * Service - 货品
 */
public class GroupRemindService extends BaseService<GroupRemind> {

    /**
     * 构造方法
     */
    public GroupRemindService() {
        super(GroupRemind.class);
    }

    private CacheManager cacheManager = CacheKit.getCacheManager();
    private GoodsDao goodsDao = Enhancer.enhance(GoodsDao.class);
    private GroupRemindDao groupRemindDao = Enhancer.enhance(GroupRemindDao.class);
    private ProductDao productDao = Enhancer.enhance(ProductDao.class);
    private SnDao snDao = Enhancer.enhance(SnDao.class);
    private ProductCategoryDao productCategoryDao = Enhancer.enhance(ProductCategoryDao.class);
    private BrandDao brandDao = Enhancer.enhance(BrandDao.class);
    private PromotionDao promotionDao = Enhancer.enhance(PromotionDao.class);
    private TagDao tagDao = Enhancer.enhance(TagDao.class);
    private AttributeDao attributeDao = Enhancer.enhance(AttributeDao.class);
    private StockLogDao stockLogDao = Enhancer.enhance(StockLogDao.class);
    private ProductImageService productImageService = Enhancer.enhance(ProductImageService.class);
    private ProductService productService = Enhancer.enhance(ProductService.class);


    /**
     * 根据品牌查找商品
     */
    public List<Goods> findByBrandId(Long id) {
        return goodsDao.findByBrandId(id);
    }


    /**
     * 新品推荐
     */
    public List<Goods> findLikeList() {
        return goodsDao.findLikeList();
    }

    /**
     * 获取满减促销商品列表
     */
    public List<Goods> findGoodsByPromId(Long promId) {

        return goodsDao.findGoodsByPromId(promId);

    }

    /**
     * 获取满减促销商品列表
     */
    public Page<Goods> findGoodsByPromId(Long promId, Pageable pageable) {

        return goodsDao.findGoodsByPromId(promId, pageable);

    }


    /**
     * 购物车id获取商品信息
     */
    public List<Goods> findCartItemList(Long id) {

        return goodsDao.findCartItemList(id);
    }
    public List<GroupRemind> findBymemGroup(Long memid,Long groupid) {

        return groupRemindDao.findBymemGroup(memid,groupid);
    }

    /**
     * 获取凑单商品
     */
    public Page<Goods> findGoods(String price, Pageable pageable) {
        return goodsDao.findGoods(price, pageable);
    }


    /**
     * 新品推荐
     */
    public List<Goods> findNewGoodsList() {
        return goodsDao.findNewGoodsList();
    }

    /**
     * 个性推荐
     */
    public List<Goods> findCharactersList() {
        return goodsDao.findCharactersList();
    }

    /**
     * 销量由高到低
     */
    public Page<Goods> findSell(Pageable pageable, String keyword) {
        return goodsDao.findSell(pageable, keyword);
    }

    /**
     * 评论数由高到低
     */
    public Page<Goods> findReview(Pageable pageable, String keyword) {
        return goodsDao.findReview(pageable, keyword);
    }

    /**
     * 综合排序
     */
    public Page<Goods> findTime(Pageable pageable, String keyword) {
        return goodsDao.findTime(pageable, keyword);
    }


    /**
     * vip商品统计
     */
    public Page<Record> findCount(Pageable pageable) {
        String name = pageable.getSearchValue();
        return goodsDao.findCount(pageable, name);
    }


    /**
     * 获取店铺信息
     */

    public Organ findOrgan(String id) {
        Organ organ1 = new Organ();
        BasicDBObject basicDBObject = new BasicDBObject();
        basicDBObject.put("_id", id);
        DBObject organ = MongoKit.getCollection("organ").findOne(basicDBObject);
        organ1.setName(organ.get("name").toString());
        organ1.setImage(organ.get("logo").toString());
        organ1.setId(id.toString());
        return organ1;

    }

    /**
     * 分配库存
     *
     * @param order 订单
     */
    private void allocateStock(Order order) {
        if (order == null || BooleanUtils.isNotFalse(order.getIsAllocatedStock())) {
            return;
        }
        if (order.getOrderItems() != null) {
            for (OrderItem orderItem : order.getOrderItems()) {
                Product product = orderItem.getProduct();
                if (product != null) {
                    productService.addAllocatedStock(product, orderItem.getQuantity() - orderItem.getShippedQuantity());
                }
            }
        }
        order.setIsAllocatedStock(true);
    }


    /**
     * 根据手机号获取店铺列表
     */

    public List<DBObject> findOrganList(String phone) {

        BasicDBObject basicDBObject = new BasicDBObject();
        basicDBObject.put("phone", phone);
        basicDBObject.put("state", true);
        DBCursor member = MongoKit.getCollection("vipMember").find(basicDBObject);
        List<DBObject> dbObjects = new ArrayList<>();
        if (member != null) {
            dbObjects = member.toArray();
        }

        return dbObjects;
    }


    /**
     * vip商品列表
     */
    public Page<Goods> findVipPage(Pageable pageable) {
        return goodsDao.findVipPage(pageable);
    }


    /**
     * 判断编号是否存在
     *
     * @param sn 编号(忽略大小写)
     * @return 编号是否存在
     */
    public boolean snExists(String sn) {
        return goodsDao.snExists(sn);
    }

    /**
     * 根据专题id获取商品信息
     */
    public List<Goods> findGoodsByGoodsThemeId(Long id) {
        return goodsDao.findGoodsByGoodsThemeId(id);
    }


    /**
     * 根据themeId查询商品信息
     *
     * @param id 编号(忽略大小写)
     * @return goods
     */
    public Goods findGoodsByThemeId(Long id) {
        return goodsDao.findGoods(id);
    }

    /**
     * 根据订单id查找对应商品
     */
    public List<Goods> findGoodsByItemId(Long id) {
        return goodsDao.findGoodsByItemId(id);
    }

    /**
     * 根据订单id查找对应商品
     */
    public List<Goods> findGoodsByOrderItemId(Long id) {
        return goodsDao.findGoodsByOrderItemId(id);
    }

    /**
     * 根据订单id 商品id查找有无订单
     */
    public List<Goods> findGoodsByOrderId(Long id) {
        return goodsDao.findGoodsByOrderId(id);
    }

    public List<Goods> findGoodsByPt(Long id) {
        return goodsDao.findGoodsByPt(id);
    }


    /**
     * 根据购物项id查找对应商品
     */
    public Goods findGoodsByCartItemId(Long id) {
        return goodsDao.findGoodsByCartItemId(id);
    }

    /**
     * 根据购物车id获取商品
     */
    public Goods findGoodsByCartId(Long id) {
        return goodsDao.findGoodsByCartId(id);
    }

    /**
     * 根据GOOds id 查找对应商品
     */

    public Goods findGoodsById(Long id) {
        return goodsDao.findGoodsById(id);
    }


    /**
     * @param productId 编号(忽略大小写)
     * @return goods
     */
    public Goods findGoodsByPro(Long productId) {
        return goodsDao.findGoodsByPro(productId);
    }


    /**
     * @param productId 编号(忽略大小写)
     * @return goods
     */
    public List<Goods> findGoodsByProduct(Long productId) {
        return goodsDao.findGoodsByProduct(productId);
    }

    /**
     * @param fudaiId 编号(忽略大小写)
     * @return goods
     */
    public Goods findGoodsByProductId(Long fudaiId) {
        return goodsDao.findGoodsByProductId(fudaiId);
    }


    /**
     * 根据id获取抢拍商品信息
     *
     * @param id 编号(忽略大小写)
     * @return goods
     */
    public List<Goods> findGoodsByReverseId(Long id) {
        return goodsDao.findGoodsByReverseId(id);
    }


    /**
     * 获取当前限时抢购商品信息
     *
     * @return goods
     * @param编号(忽略大小写)
     */
    public List<Goods> findGoodsByFlashType() {
        return goodsDao.findGoodsByFlashType();
    }


    /**
     * 查找标签
     *
     * @param id 编号(忽略大小写)
     * @return list
     */
    public List<Tag> finTagList(Long id) {
        return tagDao.getTag(id);
    }


    /**
     * 根据编号查找货品
     *
     * @param sn 编号(忽略大小写)
     * @return 货品，若不存在则返回null
     */
    public Goods findBySn(String sn) {
        return goodsDao.findBySn(sn);
    }

    /**
     * 查找货品
     *
     * @param type              类型
     * @param productCategory   商品分类
     * @param brand             品牌
     * @param promotion         促销
     * @param tag               标签
     * @param attributeValueMap 属性值Map
     * @param startPrice        最低价格
     * @param endPrice          最高价格
     * @param isMarketable      是否上架
     * @param isList            是否列出
     * @param isTop             是否置顶
     * @param isOutOfStock      是否缺货
     * @param isStockAlert      是否库存警告
     * @param hasPromotion      是否存在促销
     * @param orderType         排序类型
     * @param count             数量
     * @param filters           筛选
     * @param orders            排序
     * @return 货品
     */
    public List<Goods> findList(Goods.Type type, ProductCategory productCategory, Brand brand, Promotion promotion, Tag tag, Map<Attribute, String> attributeValueMap, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isOutOfStock,
                                Boolean isStockAlert, Boolean hasPromotion, Goods.OrderType orderType, Integer count, List<Filter> filters, List<com.wtshop.Order> orders) {
        return goodsDao.findList(type, productCategory, brand, promotion, tag, attributeValueMap, startPrice, endPrice, isMarketable, isList, isTop, isOutOfStock, isStockAlert, hasPromotion, orderType, count, filters, orders);
    }

    /**
     * 查找货品
     *
     * @param type              类型
     * @param productCategoryId 商品分类ID
     * @param brandId           品牌ID
     * @param promotionId       促销ID
     * @param tagId             标签ID
     * @param attributeValueMap 属性值Map
     * @param startPrice        最低价格
     * @param endPrice          最高价格
     * @param isMarketable      是否上架
     * @param isList            是否列出
     * @param isTop             是否置顶
     * @param isOutOfStock      是否缺货
     * @param isStockAlert      是否库存警告
     * @param hasPromotion      是否存在促销
     * @param orderType         排序类型
     * @param count             数量
     * @param filters           筛选
     * @param orders            排序
     * @param useCache          是否使用缓存
     * @return 货品
     */
    public List<Goods> findList(Goods.Type type, Long productCategoryId, Long brandId, Long promotionId, Long tagId, Map<Long, String> attributeValueMap, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isOutOfStock, Boolean isStockAlert,
                                Boolean hasPromotion, Goods.OrderType orderType, Integer count, List<Filter> filters, List<com.wtshop.Order> orders, boolean useCache) {
        ProductCategory productCategory = productCategoryDao.find(productCategoryId);
        if (productCategoryId != null && productCategory == null) {
            return Collections.emptyList();
        }
        Brand brand = brandDao.find(brandId);
        if (brandId != null && brand == null) {
            return Collections.emptyList();
        }
        Promotion promotion = promotionDao.find(promotionId);
        if (promotionId != null && promotion == null) {
            return Collections.emptyList();
        }
        Tag tag = tagDao.find(tagId);
        if (tagId != null && tag == null) {
            return Collections.emptyList();
        }
        Map<Attribute, String> map = new HashMap<Attribute, String>();
        if (attributeValueMap != null) {
            for (Map.Entry<Long, String> entry : attributeValueMap.entrySet()) {
                Attribute attribute = attributeDao.find(entry.getKey());
                if (attribute != null) {
                    map.put(attribute, entry.getValue());
                }
            }
        }
        if (MapUtils.isNotEmpty(attributeValueMap) && MapUtils.isEmpty(map)) {
            return Collections.emptyList();
        }
        return goodsDao.findList(type, productCategory, brand, promotion, tag, map, startPrice, endPrice, isMarketable, isList, isTop, isOutOfStock, isStockAlert, hasPromotion, orderType, count, filters, orders);
    }

    /**
     * 查找货品
     *
     * @param productCategory 商品分类
     * @param isMarketable    是否上架
     * @param generateMethod  静态生成方式
     * @param beginDate       起始日期
     * @param endDate         结束日期
     * @param first           起始记录
     * @param count           数量
     * @return 货品
     */
    public List<Goods> findList(ProductCategory productCategory, Boolean isMarketable, Goods.GenerateMethod generateMethod, Date beginDate, Date endDate, Integer first, Integer count) {
        return goodsDao.findList(productCategory, isMarketable, generateMethod, beginDate, endDate, first, count);
    }

    /**
     * 查找货品分页
     *
     * @param type              类型
     * @param productCategory   商品分类
     * @param brand             品牌
     * @param promotion         促销
     * @param tag               标签
     * @param attributeValueMap 属性值Map
     * @param startPrice        最低价格
     * @param endPrice          最高价格
     * @param isMarketable      是否上架
     * @param isList            是否列出
     * @param isTop             是否置顶
     * @param isOutOfStock      是否缺货
     * @param isStockAlert      是否库存警告
     * @param hasPromotion      是否存在促销
     * @param orderType         排序类型
     * @param pageable          分页信息
     * @return 货品分页
     */
    public Page<Goods> findPage(Boolean is_vip, Boolean is_delete, Goods.Type type, ProductCategory productCategory, Brand brand, Promotion promotion, Tag tag, Map<Attribute, String> attributeValueMap, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isOutOfStock,
                                Boolean isStockAlert, Boolean hasPromotion, Goods.OrderType orderType, Pageable pageable) {
        return goodsDao.findPage(is_vip, is_delete, type, productCategory, brand, promotion, tag, attributeValueMap, startPrice, endPrice, isMarketable, isList, isTop, isOutOfStock, isStockAlert, hasPromotion, orderType, pageable);
    }


    public Page<Goods> findPages(Boolean is_vip, Boolean is_delete, Goods.Type type, List<Long> productCategory, Brand brand, Promotion promotion, Tag tag, Map<Attribute, String> attributeValueMap, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isOutOfStock,
                                 Boolean isStockAlert, Boolean hasPromotion, Goods.OrderType orderType, Pageable pageable) {
        return goodsDao.findPage(is_vip, is_delete, type, productCategory, brand, promotion, tag, attributeValueMap, startPrice, endPrice, isMarketable, isList, isTop, isOutOfStock, isStockAlert, hasPromotion, orderType, pageable);
    }

    /**
     * 查找货品分页
     *
     * @param rankingType 排名类型
     * @param pageable    分页信息
     * @return 货品分页
     */
    public Page<Goods> findPage(Goods.RankingType rankingType, Pageable pageable) {
        return goodsDao.findPage(rankingType, pageable);
    }


    /**
     * 查找货品分页
     *
     * @param pageable  排名类型
     * @param goodsList 分页信息
     * @return 货品分页
     */
    public Page<Goods> findPage(Pageable pageable, List<Long> goodsList) {
        return goodsDao.findPage(pageable, goodsList);
    }

    /**
     * 查找收藏货品分页
     *
     * @param member   会员
     * @param pageable 分页信息
     * @return 收藏货品分页
     */
    public Page<Goods> findPage(Member member, Pageable pageable, String keyword) {
        return goodsDao.findPage(member, pageable, keyword);
    }

    /**
     * 查询货品数量
     *
     * @param type           类型
     * @param favoriteMember 收藏会员
     * @param isMarketable   是否上架
     * @param isList         是否列出
     * @param isTop          是否置顶
     * @param isOutOfStock   是否缺货
     * @param isStockAlert   是否库存警告
     * @return 货品数量
     */
    public Long count(Goods.Type type, Member favoriteMember, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isOutOfStock, Boolean isStockAlert) {
        return goodsDao.count(type, favoriteMember, isMarketable, isList, isTop, isOutOfStock, isStockAlert);
    }

    /**
     * 查看点击数
     *
     * @param id ID
     * @return 点击数
     */
    public long viewHits(Long id) {
        Assert.notNull(id);

        Ehcache cache = cacheManager.getEhcache(Goods.HITS_CACHE_NAME);
        Element element = cache.get(id);
        Long hits;
        if (element != null) {
            hits = (Long) element.getObjectValue() + 1;
        } else {
            Goods goods = goodsDao.find(id);
            if (goods == null) {
                return 0L;
            }
            hits = goods.getHits() + 1;
        }
        cache.put(new Element(id, hits));
        return hits;
    }


    /**
     * 订单锁定
     *
     * @param order  订单
     * @param member 会员
     */
    public void lock(Order order, Member member) {
        Setting setting = SystemUtils.getSetting();
        Assert.notNull(order);
        Assert.notNull(member);

        boolean isLocked = order.getLockExpire() != null && order.getLockExpire().after(new Date()) && StringUtils.isNotEmpty(order.getLockKey()) && !StringUtils.equals(order.getLockKey(), member.getLockKey());
        if (!isLocked && StringUtils.isNotEmpty(member.getLockKey())) {
            order.setLockKey(member.getLockKey());
            order.setLockExpire(DateUtils.addSeconds(new Date(), setting.getCommomPayTime()));
        }
    }

    /**
     * 增加点击数
     *
     * @param goods  货品
     * @param amount 值
     */
    public void addHits(Goods goods, long amount) {
        Assert.notNull(goods);
        Assert.state(amount >= 0);

        if (amount == 0) {
            return;
        }

        Calendar nowCalendar = Calendar.getInstance();
        Calendar weekHitsCalendar = DateUtils.toCalendar(goods.getWeekHitsDate());
        Calendar monthHitsCalendar = DateUtils.toCalendar(goods.getMonthHitsDate());
        if (nowCalendar.get(Calendar.YEAR) > weekHitsCalendar.get(Calendar.YEAR) || nowCalendar.get(Calendar.WEEK_OF_YEAR) > weekHitsCalendar.get(Calendar.WEEK_OF_YEAR)) {
            goods.setWeekHits(amount);
        } else {
            goods.setWeekHits(goods.getWeekHits() + amount);
        }
        if (nowCalendar.get(Calendar.YEAR) > monthHitsCalendar.get(Calendar.YEAR) || nowCalendar.get(Calendar.MONTH) > monthHitsCalendar.get(Calendar.MONTH)) {
            goods.setMonthHits(amount);
        } else {
            goods.setMonthHits(goods.getMonthHits() + amount);
        }
        goods.setHits(goods.getHits() + amount);
        goods.setWeekHitsDate(new Date());
        goods.setMonthHitsDate(new Date());
        goodsDao.update(goods);
    }

    /**
     * 增加销量
     *
     * @param goods  货品
     * @param amount 值
     */
    public void addSales(Goods goods, long amount) {
        Assert.notNull(goods);
        Assert.state(amount >= 0);

        if (amount == 0) {
            return;
        }

        Calendar nowCalendar = Calendar.getInstance();
        Calendar weekSalesCalendar = DateUtils.toCalendar(goods.getWeekSalesDate());
        Calendar monthSalesCalendar = DateUtils.toCalendar(goods.getMonthSalesDate());
        if (nowCalendar.get(Calendar.YEAR) > weekSalesCalendar.get(Calendar.YEAR) || nowCalendar.get(Calendar.WEEK_OF_YEAR) > weekSalesCalendar.get(Calendar.WEEK_OF_YEAR)) {
            goods.setWeekSales(amount);
        } else {
            goods.setWeekSales(goods.getWeekSales() + amount);
        }
        if (nowCalendar.get(Calendar.YEAR) > monthSalesCalendar.get(Calendar.YEAR) || nowCalendar.get(Calendar.MONTH) > monthSalesCalendar.get(Calendar.MONTH)) {
            goods.setMonthSales(amount);
        } else {
            goods.setMonthSales(goods.getMonthSales() + amount);
        }
        goods.setSales(goods.getSales() + amount);
        goods.setWeekSalesDate(new Date());
        goods.setMonthSalesDate(new Date());
        goods.setGenerateMethod(Goods.GenerateMethod.lazy.ordinal());
        goodsDao.update(goods);
    }

    /**
     * 保存
     *
     * @param goods    货品
     * @param product  商品
     * @param operator 操作员
     * @return 货品
     */
    @Before(Tx.class)
    public Goods save(Goods goods, Product product, Admin operator) {
        Assert.notNull(goods);
        Assert.isTrue(goods.isNew());
        Assert.notNull(goods.getType());
        Assert.isTrue(!goods.hasSpecification());
        Assert.notNull(product);
        Assert.isTrue(product.isNew());
        Assert.state(!product.hasSpecification());

        switch (goods.getTypeName()) {
            case general:
                product.setExchangePoint(0L);
                break;
            case exchange:
                product.setPrice(BigDecimal.ZERO);
                product.setRewardPoint(0L);
                goods.setPromotions(null);
                break;
        }
        if (product.getMarketPrice() == null) {
            product.setMarketPrice(calculateDefaultMarketPrice(product.getPrice()));
        }
        if (product.getRewardPoint() == null) {
            product.setRewardPoint(calculateDefaultRewardPoint(product.getPrice()));
        }
        product.setAllocatedStock(0);
        product.setIsDefault(true);
        product.setGoods(goods);
        product.setSpecificationValues(null);
        product.setCartItems(null);
        product.setOrderItems(null);
        product.setShippingItems(null);
        product.setProductNotifies(null);
        product.setStockLogs(null);
        product.setGiftPromotions(null);

        goods.setPrice(product.getPrice());
        goods.setMarketPrice(product.getMarketPrice());
        goods.setScore(0F);
        goods.setTotalScore(0L);
        goods.setScoreCount(0L);
        goods.setHits(0L);
        goods.setWeekHits(0L);
        goods.setMonthHits(0L);
        goods.setSales(0L);
        goods.setWeekSales(0L);
        goods.setMonthSales(0L);
        goods.setWeekHitsDate(new Date());
        goods.setMonthHitsDate(new Date());
        goods.setWeekSalesDate(new Date());
        goods.setMonthSalesDate(new Date());
        goods.setGenerateMethod(Goods.GenerateMethod.eager.ordinal());
        goods.setSpecificationItems(null);
        goods.setReviews(null);
        goods.setConsultations(null);
        goods.setFavoriteMembers(null);
        goods.setProducts(null);
        setValue(goods);
        goodsDao.save(goods);

        setValue(product);
        product.setGoodsId(goods.getId());

        productDao.save(product);
        stockIn(product, operator, false);

        List<Promotion> promotions = goods.getPromotions();
        if (CollectionUtils.isNotEmpty(promotions)) {
            for (Promotion promotion : promotions) {
                GoodsPromotion goodsPromotion = new GoodsPromotion();
                goodsPromotion.setGoods(goods.getId());
                goodsPromotion.setPromotions(promotion.getId());
                goodsPromotion.save();
            }
        }

        List<Tag> tags = goods.getTags();
        if (CollectionUtils.isNotEmpty(tags)) {
            for (Tag tag : tags) {
                GoodsTag goodsTag = new GoodsTag();
                goodsTag.setGoods(goods.getId());
                goodsTag.setTags(tag.getId());
                goodsTag.save();
            }
        }

        List<Effect> effects = goods.getEffects();
        if (CollectionUtils.isNotEmpty(effects)) {
            for (Effect effect : effects) {
                GoodsEffct effct = new GoodsEffct();
                effct.setGoods(goods.getId());
                effct.setEffect(effect.getId());
                effct.save();
            }
        }

        return goods;
    }

    /**
     * 保存
     *
     * @param goods    货品
     * @param products 商品
     * @param operator 操作员
     * @return 货品
     */
    @Before(Tx.class)
    public Goods save(Goods goods, List<Product> products, Admin operator) {
        Assert.notNull(goods);
        Assert.isTrue(goods.isNew());
        Assert.notNull(goods.getType());
        Assert.isTrue(goods.hasSpecification());
        Assert.notEmpty(products);

        //final List<SpecificationItem> specificationItems = goods.getSpecificationItemConverter();
        if (CollectionUtils.exists(products, new Predicate() {
            private Set<List<Integer>> set = new HashSet<List<Integer>>();

            public boolean evaluate(Object object) {
                Product product = (Product) object;
                return product == null || !product.isNew() || !product.hasSpecification() || !set.add(product.getSpecificationValueIds()/*) || !specificationValueService.isValid(specificationItems, product.getSpecificationValues()*/);
            }
        })) {
            throw new IllegalArgumentException();
        }

        Product defaultProduct = (Product) CollectionUtils.find(products, new Predicate() {
            public boolean evaluate(Object object) {
                Product product = (Product) object;
                return product != null && product.getIsDefault();
            }
        });
        if (defaultProduct == null) {
            defaultProduct = products.get(0);
            defaultProduct.setIsDefault(true);
        }

        for (Product product : products) {
            switch (goods.getTypeName()) {
                case general:
                    product.setExchangePoint(0L);
                    break;
                case exchange:
                    product.setPrice(BigDecimal.ZERO);
                    product.setRewardPoint(0L);
                    goods.setPromotions(null);
                    break;

            }
            if (product.getMarketPrice() == null) {
                product.setMarketPrice(calculateDefaultMarketPrice(product.getPrice()));
            }
            if (product.getRewardPoint() == null) {
                product.setRewardPoint(calculateDefaultRewardPoint(product.getPrice()));
            }
            if (product != defaultProduct) {
                product.setIsDefault(false);
            }
            product.setAllocatedStock(0);
            product.setGoods(goods);
            product.setCartItems(null);
            product.setOrderItems(null);
            product.setShippingItems(null);
            product.setProductNotifies(null);
            product.setStockLogs(null);
            product.setGiftPromotions(null);
        }

        goods.setPrice(defaultProduct.getPrice());
        goods.setMarketPrice(defaultProduct.getMarketPrice());
        goods.setScore(0F);
        goods.setTotalScore(0L);
        goods.setScoreCount(0L);
        goods.setHits(0L);
        goods.setWeekHits(0L);
        goods.setMonthHits(0L);
        goods.setSales(0L);
        goods.setWeekSales(0L);
        goods.setMonthSales(0L);
        goods.setWeekHitsDate(new Date());
        goods.setMonthHitsDate(new Date());
        goods.setWeekSalesDate(new Date());
        goods.setMonthSalesDate(new Date());
        goods.setGenerateMethod(Goods.GenerateMethod.eager.ordinal());
        goods.setReviews(null);
        goods.setConsultations(null);
        goods.setFavoriteMembers(null);
        goods.setProducts(null);
        setValue(goods);
        goodsDao.save(goods);

        for (Product product : products) {
            setValue(product);
            product.setGoodsId(goods.getId());

            productDao.save(product);
            stockIn(product, operator, false);
        }

        List<Promotion> promotions = goods.getPromotions();
        if (CollectionUtils.isNotEmpty(promotions)) {
            for (Promotion promotion : promotions) {
                GoodsPromotion goodsPromotion = new GoodsPromotion();
                goodsPromotion.setGoods(goods.getId());
                goodsPromotion.setPromotions(promotion.getId());
                goodsPromotion.save();
            }
        }

        List<Tag> tags = goods.getTags();
        if (CollectionUtils.isNotEmpty(tags)) {
            for (Tag tag : tags) {
                GoodsTag goodsTag = new GoodsTag();
                goodsTag.setGoods(goods.getId());
                goodsTag.setTags(tag.getId());
                goodsTag.save();
            }
        }

        List<Effect> effects = goods.getEffects();
        if (CollectionUtils.isNotEmpty(effects)) {
            for (Effect effect : effects) {
                GoodsEffct effct = new GoodsEffct();
                effct.setGoods(goods.getId());
                effct.setEffect(effect.getId());
                effct.save();
            }
        }

        return goods;
    }








    public void delete(Long id) {
        super.delete(id);
    }


    /**
     * 设置货品值
     *
     * @param goods 货品
     */
    private void setValue(Goods goods) {
        if (goods == null) {
            return;
        }

        productImageService.generate(goods.getProductImagesConverter());
        List<ProductImage> productImagesConverter = goods.getProductImagesConverter();
        if (CollectionUtils.isNotEmpty(productImagesConverter)) {
            List<ProductImage> productImages = new ArrayList<ProductImage>();
            for (ProductImage productImage : productImagesConverter) {
                productImages.add(productImage);
            }
            goods.setProductImages(JSONArray.toJSONString(productImages));
        }

        if (StringUtils.isEmpty(goods.getImage()) && StringUtils.isNotEmpty(goods.getThumbnail())) {
            goods.setImage(goods.getThumbnail());
        }
        if (goods.isNew()) {
            if (StringUtils.isEmpty(goods.getSn())) {
                String sn;
                do {
                    sn = snDao.generate(Sn.Type.goods);
                } while (snExists(sn));
                goods.setSn(sn);
            }
        }
    }

    /**
     * 设置商品值
     *
     * @param product 商品
     */
    private void setValue(Product product) {
        if (product == null) {
            return;
        }

        if (product.isNew()) {
            Goods goods = product.getGoods();
            if (goods != null && StringUtils.isNotEmpty(goods.getSn())) {
                String sn;
                int i = product.hasSpecification() ? 1 : 0;
                do {
                    sn = goods.getSn() + (i == 0 ? "" : "_" + i);
                    i++;
                } while (productDao.snExists(sn));
                product.setSn(sn);
            }
        }
    }

    /**
     * 计算默认市场价
     *
     * @param price 价格
     * @return 默认市场价
     */
    private BigDecimal calculateDefaultMarketPrice(BigDecimal price) {
        Assert.notNull(price);

        Setting setting = SystemUtils.getSetting();
        Double defaultMarketPriceScale = setting.getDefaultMarketPriceScale();
        return defaultMarketPriceScale != null ? setting.setScale(price.multiply(new BigDecimal(String.valueOf(defaultMarketPriceScale)))) : BigDecimal.ZERO;
    }

    /**
     * 计算默认赠送积分
     *
     * @param price 价格
     * @return 默认赠送积分
     */
    private long calculateDefaultRewardPoint(BigDecimal price) {
        Assert.notNull(price);

        Setting setting = SystemUtils.getSetting();
        Double defaultPointScale = setting.getDefaultPointScale();
        return defaultPointScale != null ? price.multiply(new BigDecimal(String.valueOf(defaultPointScale))).longValue() : 0L;
    }

    /**
     * 根据规格值ID查找商品
     *
     * @param products              商品
     * @param specificationValueIds 规格值ID
     * @return 商品
     */
    private Product find(Collection<Product> products, final List<Integer> specificationValueIds) {
        if (CollectionUtils.isEmpty(products) || CollectionUtils.isEmpty(specificationValueIds)) {
            return null;
        }

        return (Product) CollectionUtils.find(products, new Predicate() {
            public boolean evaluate(Object object) {
                Product product = (Product) object;
                return product != null && product.getSpecificationValueIds() != null && product.getSpecificationValueIds().equals(specificationValueIds);
            }
        });
    }

    /**
     * 根据规格值ID判断商品是否存在
     *
     * @param products              商品
     * @param specificationValueIds 规格值ID
     * @return 商品是否存在
     */
    private boolean exists(Collection<Product> products, final List<Integer> specificationValueIds) {
        return find(products, specificationValueIds) != null;
    }

    @Override
    public long count() {
        return super.count();
    }

    /**
     * 入库
     *
     * @param product  商品
     * @param operator 操作员
     * @param isJson   是否缓存到json
     */
    private void stockIn(Product product, Admin operator, boolean isJson) {
        if (product == null || product.getStock() == null || product.getStock() <= 0) {
            return;
        }

        StockLog stockLog = new StockLog();
        stockLog.setType(StockLog.Type.stockIn.ordinal());
        stockLog.setInQuantity(product.getStock());
        stockLog.setOutQuantity(0);
        stockLog.setStock(product.getStock());
        stockLog.setOperator(operator);
        stockLog.setMemo(null);
        stockLog.setProductId(product.getId());
        if (isJson) {
            stockLogDao.jSave(stockLog);
        } else {
            stockLogDao.save(stockLog);
        }


    }



    //  为申请准备数据
    @Before(Tx.class)
    public Product prepareReviewProduct(Goods goods, Product product, Admin operator) {
        Assert.notNull(goods);
        Assert.isTrue(!goods.isNew());
        Assert.isTrue(!goods.hasSpecification());
        Assert.notNull(product);
        Assert.isTrue(product.isNew());
        Assert.state(!product.hasSpecification());

        Goods pGoods = goodsDao.find(goods.getId());
        switch (pGoods.getTypeName()) {
            case general:
                product.setExchangePoint(0L);
                break;
            case exchange:
                product.setPrice(BigDecimal.ZERO);
                product.setRewardPoint(0L);
                goods.setPromotions(null);
                break;

        }
        if (product.getMarketPrice() == null) {
            product.setMarketPrice(calculateDefaultMarketPrice(product.getPrice()));
        }
        if (product.getRewardPoint() == null) {
            product.setRewardPoint(calculateDefaultRewardPoint(product.getPrice()));
        }
        product.setAllocatedStock(0);
        product.setIsDefault(true);
        product.setGoodsId(pGoods.getId());
        product.setSpecificationValues(null);
        product.setCartItems(null);
        product.setOrderItems(null);
        product.setShippingItems(null);
        product.setProductNotifies(null);
        product.setStockLogs(null);
        product.setGiftPromotions(null);
        if (!pGoods.hasSpecification()) {
            Product defaultProduct = pGoods.getDefaultProduct();
            defaultProduct.setPrice(product.getPrice());
            defaultProduct.setCost(product.getCost());
            defaultProduct.setMarketPrice(product.getMarketPrice());
            defaultProduct.setRewardPoint(product.getRewardPoint());
            defaultProduct.setExchangePoint(product.getExchangePoint());
            setValue(defaultProduct);
        }
        return product;
    }


}