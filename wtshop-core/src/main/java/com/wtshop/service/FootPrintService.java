package com.wtshop.service;

import com.jfinal.aop.Enhancer;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import com.wtshop.Pageable;
import com.wtshop.dao.FootprintDao;
import com.wtshop.dao.ProductDao;
import com.wtshop.model.Footprint;
import com.wtshop.util.ApiResult;
import com.wtshop.util.DateUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by Administrator on 2017/8/17.
 */
public class FootPrintService extends BaseService<Footprint> {
    public FootPrintService() {
        super(Footprint.class);
    }

    private MyHomeService myHomeService = Enhancer.enhance(MyHomeService.class);
    private ProductDao productDao = Enhancer.enhance(ProductDao.class);
    private FootprintDao footprintDao = Enhancer.enhance(FootprintDao.class);

    //获取用户足迹列表
    public Page getUserFootPrint(Date sTime, Date eTime, String phone, String nickname, Pageable pageable) {
        String sTimeStr = DateUtils.formatDate(sTime);
        String eTimeStr = DateUtils.formatDate(eTime);
        Kv cond = Kv.by("beginDate", sTimeStr).set("endDate", eTimeStr).set("phone", phone).set("nickname", nickname);
        SqlPara sqlPara = Db.getSqlPara("userFootPrint", cond);
        Page page = Db.paginate(pageable.getPageNumber(), pageable.getPageSize(), sqlPara);
        return page;
    }

    //获取用户详情列表

    public Page<Footprint> getUserDetailsFootPrint(Date sTime, Date eTime, Pageable pageable, Long uid) {
        String sTimeStr = DateUtils.formatDate(sTime);
        String eTimeStr = DateUtils.formatDate(eTime);
        return footprintDao.getUserDetailsFootPrint(sTimeStr, eTimeStr, pageable, uid);
    }

    //获取商品足迹列表

    public Page getGoodsFootPrint(Date sTime, Date eTime, Pageable pageable) {
        String sTimeStr = DateUtils.formatDate(sTime);
        String eTimeStr = DateUtils.formatDate(eTime);
        Kv cond = Kv.by("beginDate", sTimeStr).set("endDate", eTimeStr).set("p", pageable);
        SqlPara sqlPara = Db.getSqlPara("goodsFootPrint", cond);
        Page page = Db.paginate(pageable.getPageNumber(), pageable.getPageSize(), sqlPara);

        List<Record> list = page.getList();
        if (list != null && list.size() > 0) {
            for (Record r : list) {
                long goodsId = r.get("goodsId"); //名称规格
                String sql = "SELECT sum(stock)  num from product  WHERE goods_id=" + goodsId;
                BigDecimal num = Db.queryBigDecimal(sql);
                r.set("stock", num);
            }
        }

        return page;
    }

    //商品足迹详情列表
    public Page<Footprint> getGoodsrDetailsFootPrint(Date sTime, Date eTime, Pageable pageable, Long goodsId) {
        String sTimeStr = DateUtils.formatDate(sTime);
        String eTimeStr = DateUtils.formatDate(eTime);

        String phone = null;
        String nickname = null;
        String searchProperty = pageable.getSearchProperty();
        if (searchProperty != null) {
            switch (searchProperty) {
                case "phone":
                    phone = pageable.getSearchValue();
                    break;
                case "nickname":
                    nickname = pageable.getSearchValue();
                    break;

            }
        }
        return footprintDao.getGoodsrDetailsFootPrint(sTimeStr, eTimeStr, pageable, goodsId, phone, nickname);
    }

    //用户自身足迹
    public ApiResult queryMyFootPrint(Long memberId) {
        List<Footprint> footprints = footprintDao.queryMyFootPrint(memberId);
        Map map = myHomeService.sortByTime(footprints, "CreateDate");
        Map result = new HashMap();

        result.put("data", map);
        return new ApiResult(1, "", result);
    }

    /**
     * 查询十分钟内是否有记录
     */
    public boolean findByTime(Long goodsId, Long memberId) {
        Date endTime = DateUtils.addDate(new Date(), 12, -10);
        return footprintDao.findByTime(endTime, goodsId, memberId) > 0 ? true : false;
    }
}
