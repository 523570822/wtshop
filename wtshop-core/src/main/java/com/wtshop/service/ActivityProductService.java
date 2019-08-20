package com.wtshop.service;

import com.alibaba.fastjson.JSON;
import com.jfinal.aop.Enhancer;
import com.jfinal.plugin.redis.Redis;
import com.wtshop.dao.FuDaiDao;
import com.wtshop.dao.FuDaiProductDao;
import com.wtshop.dao.OrderItemDao;
import com.wtshop.model.*;
import com.wtshop.util.ObjectUtils;

import java.util.*;

/**
 * Created by 蔺哲 on 2017/7/11.
 */
public class ActivityProductService extends BaseService<ActivityProduct> {

    public ActivityProductService() {
        super(ActivityProduct.class);
    }

    private FuDaiProductDao fuDaiProductDao = Enhancer.enhance(FuDaiProductDao.class);
    private OrderItemDao orderItemDao = Enhancer.enhance(OrderItemDao.class);
    private FuDaiDao fuDaiDao = Enhancer.enhance(FuDaiDao.class);
    private ProductService productService = Enhancer.enhance(ProductService.class);



    /**
     * 根据帮抢Id 获取主帮抢商品信息
     */
    public FudaiProduct findPrimary(Long id) {
        return fuDaiProductDao.findPrimary(id);
    }

    /**
     * 根据帮抢Id 获取所有帮抢商品信息
     */
    public List<FudaiProduct> findMessage(Long id) {
        return fuDaiProductDao.findMessage(id);
    }

    /**
     * 根据帮抢Id 获取副产品
     */
    public List<FudaiProduct> findByPro(Long id, List<Long> productIdList) {
        return fuDaiProductDao.findByPro(id, productIdList);
    }

    /**
     * 根据帮抢Id 抽奖
     */
    public List<Long> lotteryProduct(long fudaiId, long memberId, long orderId) {
        FuDai fd = fuDaiDao.find(fudaiId);  //  待抽取的帮抢
        Map<Long, Long> exportCountMap = new HashMap<>();   //  单商品抽取次数
        List<FudaiProduct> fdSubProductList = fuDaiProductDao.findByFudaiId(fd.getId());    //  帮抢副产品列表
        List<FudaiProduct> randomList = new ArrayList<>();
        for (FudaiProduct fudaiProduct : fdSubProductList) {
            if (fudaiProduct.getGrandPrix().intValue() > 0 && Redis.use().exists("FD:MEMBER:" + memberId + ":" + fudaiProduct.getId())) {
                continue;
            }
            randomList.add(fudaiProduct);
            exportCountMap.put(fudaiProduct.getId(), 0l);
        }
        List<Long> resultIdList = new ArrayList<>();
        int count = fd.getNum();
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            LinkedHashMap<Long, FudaiProduct> sortMap = new LinkedHashMap<>();
            Long maxRandom = 0l;
            for (FudaiProduct subFudaiProduct : randomList) {
                maxRandom += (long) (subFudaiProduct.getProbability().doubleValue() * 100000);    // 抽取概率系数，100000表示最小概率支持1/100000
                sortMap.put(maxRandom, subFudaiProduct);
            }
            //  根据随机值，由小到大判断是否抽中
            int resultIndex = random.nextInt(maxRandom.intValue());
            int currentIndex = 0;
            for (Long sortVal : sortMap.keySet()) {
                if (sortVal > resultIndex) {
                    FudaiProduct entity = sortMap.get(sortVal);
                    //  抽中当前商品
                    resultIdList.add(entity.getId());
                    toOrder(entity, orderId);
                    if (entity.getGrandPrix().intValue() > 0) {
                        Redis.use().setex("FD:MEMBER:" + memberId + ":" + entity.getId(), entity.getGrandPrix().intValue(), entity.getGrandPrix());
                    }
                    //  单个商品在单次抽取中限制了抽取数量
                    if (entity.getMaxNum().intValue() > 0) {
                        exportCountMap.put(entity.getId(), exportCountMap.get(entity.getId()).longValue() + 1);
                        if (exportCountMap.get(entity.getId()).intValue() >= entity.getMaxNum().intValue()) {
                            randomList.remove(currentIndex);
                        }
                    }
                    break;
                }
                currentIndex++;
            }
        }

        return resultIdList;
    }

    private void toOrder(FudaiProduct fudaiProduct, Long orderId) {
        Product product = productService.find(fudaiProduct.getProductId());
        //插入订单子项
        OrderItem orderItem = new OrderItem();
        orderItem.setSn(product.getSn());
        orderItem.setName(product.getName());
        orderItem.setType(Order.Type.fudai.ordinal());
        orderItem.setPrice(product.getPrice());
        orderItem.setWeight(product.getWeight());
        orderItem.setIsDelivery(product.getIsDelivery());
        orderItem.setThumbnail(product.getThumbnail());
        orderItem.setQuantity(1);
        orderItem.setShippedQuantity(0);
        orderItem.setReturnedQuantity(0);
        orderItem.setProductId(product.getId());
        orderItem.setSpecifications(JSON.toJSONString(product.getSpecifications()));
        orderItem.setOrderId(orderId);
        orderItem.setActitemId(fudaiProduct.getId());
        orderItemDao.save(orderItem);
    }

}
