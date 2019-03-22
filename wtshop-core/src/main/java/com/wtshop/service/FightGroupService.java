package com.wtshop.service;

import com.jfinal.aop.Enhancer;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.wtshop.Pageable;
import com.wtshop.dao.FightGroupDao;
import com.wtshop.dao.FuDaiProductDao;
import com.wtshop.dao.GroupBuyDao;
import com.wtshop.dao.OrderDao;
import com.wtshop.model.*;
import com.wtshop.util.CollectionUtils;
import com.wtshop.util.StringUtils;
import org.apache.commons.collections.map.HashedMap;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by 蔺哲 on 2017/7/11.
 */
public class FightGroupService extends BaseService<FightGroup> {
    public FightGroupService() {
        super( FightGroup.class);
    }

    private FightGroupDao fuDaiProductDao = Enhancer.enhance(FightGroupDao.class);


    private GroupBuyDao fuDaiDao = Enhancer.enhance(GroupBuyDao.class);
    private FuDaiProductService fuDaiProductService = Enhancer.enhance(FuDaiProductService.class);
    private MemberService memberService = Enhancer.enhance(MemberService.class);
    private OrderDao orderDao = Enhancer.enhance(OrderDao.class);
/**
 * 根据商品id   product_id 获取参团信息
 */
    public  List<FightGroup> findByProductId (Long productId){
        List<FightGroup> fightGroupList = fuDaiProductDao.findByProductId(productId);
        for (FightGroup fightGroup:fightGroupList) {
            Long time = 0L;
            time = Calendar.getInstance().getTimeInMillis();
           // fightGroup.set("ji_shi",fightGroup.getEndDate().getTime()- time);
            fightGroup.setJiShi(fightGroup.getEndDate().getTime()- time);

            
        }
    return    fightGroupList;
    }


    public void saveOrUpdate(List<FightGroup> newList, Long fuDaiId) {
        List<FightGroup> oldList = fuDaiProductDao.findByFudaiId(fuDaiId);
        List<Long> updateIdList = new ArrayList<Long>();

        if (oldList.size() > 0) {//修改保存删除
            for (FightGroup detail : newList) {//保存新增产品
                if (!StringUtils.isEmpty(detail.getId())) {
                    updateIdList.add(detail.getId());
                } else {

                    fuDaiProductDao.save(detail);
                }
            }

            for (FightGroup detail : oldList) {//删除前端删除产品
                if (!updateIdList.contains(detail.getId())) {
                    fuDaiProductDao.remove(detail);
                }
            }


        } else {//保存

        }
    }

    public List<FightGroup> queryByFuDaiId(Long id) {

        return fuDaiProductDao.findByFudaiId(id);
    }

    /**
     * 获取当前正在使用的福袋
     */
    public Page<GroupBuy> findPages(Pageable pageable,boolean status) {
        return fuDaiDao.findPages(pageable,status);
    }


    //福袋抽取副产品
    public List<Map<String, Object>> luckDraw(Order order) {
        long fudaiId = Long.parseLong(order.getActOrderId());
        List<Long> fudaiIdList = fuDaiProductService.lotteryProduct(fudaiId, order.getMemberId(), order.getId());
        List<Map<String, Object>> mapList = new ArrayList<>();
        if (fudaiIdList != null && fudaiIdList.size() > 0) {
            String ids = StringUtils.collectionToDelimitedString(fudaiIdList, ",");
            String sql = "SELECT g.name,  p.price,p.specification_values,g.image from fudai_product fp \n" +
                    "LEFT JOIN product p on fp.product_id=p.id LEFT JOIN  goods g on p.goods_id=g.id WHERE fp.id in (" + ids + ")";
            List<Record> list = Db.find(sql);
            if (list != null && list.size() > 0) {
                for (Record r : list) {
                    Map<String, Object> map = new HashedMap();
                    map.put("price", r.get("price"));
                    map.put("specification_values", r.get("specification_values"));
                    map.put("image", r.get("image"));
                    map.put("name", r.get("name"));
                    mapList.add(map);
                }
            }
        }
        return mapList;

    }

    //福袋获奖记录
    public Page winRecord(Pageable pageable) {
        Member member = memberService.getCurrent();

        Page page = Db.paginate(pageable.getPageNumber(), pageable.getPageSize(), "select create_date ,actOrderId, id, o.sn", " FROM `order`  o " +
                "   WHERE o.type = 2 and  o.member_id=" + member.getId() + "   and o.`status` in (2,3,4,5,9,10) and o.actOrderId>0" +
                "   AND is_delete =0 order by create_date desc");
        List<Record> list = page.getList();
        String sql = "";
        if (list != null && list.size() > 0) {
            for (Record record : list) {
                //获取福袋信息
                GroupBuy fuDai = fuDaiDao.find(Long.parseLong(record.get("actOrderId")));
                sql = "SELECT thumbnail,price FROM order_item WHERE order_id=" + record.get("id");
                List<Record> items = Db.find(sql);
                BigDecimal sum = new BigDecimal(0.0);
                for (Record er : items) {
                    sum = sum.add(er.getBigDecimal("price"));
                }

                record.set("fuDai", fuDai);
                record.set("items", items);
                record.set("sum", sum.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());

            }

        }

        return page;


    }

    //幸运排行榜
    public Page rankList(Pageable pageable) {

        String select = " SELECT o.id ,o.member_id ,SUM(oi.price) price,m.nickname,m.avatar,o.create_date ";

        String sql = " FROM order_item" +
            "  oi LEFT JOIN `order` o on oi.order_id=o.id LEFT JOIN fudai_product fp on fp.id=oi.actitem_id " +
            "LEFT JOIN member m on m.id=o.member_id WHERE o.type =2 and o.`status` in (2,3,4,5,9,10) and o.actOrderId>0 and fp.is_main=0   GROUP BY member_id ,order_id   ORDER BY  price DESC ";

        Page page = Db.paginate(pageable.getPageNumber(), pageable.getPageSize(), select,  sql);
        return page;
    }

    //获取幸运记录

    public Map<String, Object> getRankDetail(long orderId) {
        Map<String, Object> map = new HashedMap();
        Order order = orderDao.find(orderId);
        Member member = order.getMember();
        String sql = " SELECT oi.price,g.image  ,g.`name`,p.specification_values , g.caption FROM order_item  oi LEFT JOIN `order` o on oi.order_id=o.id LEFT JOIN product p on p.id=oi.product_id  LEFT JOIN goods g ON g.id=p.goods_id  WHERE  o.id= " + order.getId();
        List<Record> list = Db.find(sql);
        BigDecimal sum = new BigDecimal(0.0);
        if (!CollectionUtils.isEmpty(list)) {
            for (Record er : list) {
                sum = sum.add(er.getBigDecimal("price"));
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put("member", member);
        result.put("sum", sum);
        result.put("list", list);
        result.put("time", order.getCreateDate());
        return result;
    }


    public List<FightGroup> findSubListByFudaiId(Long fuDaiId){
        return fuDaiProductDao.findByFudaiId(fuDaiId);
    }
}