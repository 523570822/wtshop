package com.wtshop.service;

import com.jfinal.aop.Enhancer;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.redis.Redis;
import com.wtshop.Pageable;
import com.wtshop.dao.ReferrerConfigDao;
import com.wtshop.dao.ReferrerGoodsDao;
import com.wtshop.dao.StaffMemberDao;
import com.wtshop.model.*;
import com.wtshop.util.ApiResult;
import com.wtshop.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 蔺哲 on 2017/9/5.
 */
public class ReferrerGoodsService extends BaseService<ReferrerGoods> {
    public ReferrerGoodsService() {
        super(ReferrerGoods.class);
    }

    private ReferrerGoodsDao referrerGoodsDao = Enhancer.enhance(ReferrerGoodsDao.class);
    private ReferrerConfigDao referrerConfigDao = Enhancer.enhance(ReferrerConfigDao.class);
    private MyHomeService myHomeService = Enhancer.enhance(MyHomeService.class);
    private GoodsService goodsService = Enhancer.enhance(GoodsService.class);
    private MemberService memberService = Enhancer.enhance(MemberService.class);
    private InformationService informationService = Enhancer.enhance(InformationService.class);
    private StaffMemberDao staffMemberDao = Enhancer.enhance(StaffMemberDao.class);

    /**
     * 得到当前推荐商品配置
     *
     * @return
     */
    public ReferrerConfig getReferrerConfig() {
        return referrerConfigDao.getConfig();
    }

    /**
     * 保存或修改配置
     */
    public void saveOrUpdate(ReferrerConfig referrerConfig) {
        if (referrerConfig.getId() == null) {
            referrerConfigDao.save(referrerConfig);
        } else {
            referrerConfigDao.update(referrerConfig);
        }
    }

    /**
     * 统计单个商品推荐详情
     */
    public Page queryPageByGoodsId(Pageable page, Long goodsId) {
        return referrerGoodsDao.queryPageByGoodsId(page, goodsId);
    }

    /**
     * 分页统计商品推荐
     */
    public Page queryByPage(Pageable page) {
        return referrerGoodsDao.queryByPage(page);
    }

    /**
     * 推荐给用户
     *
     * @param ids     用户列表
     * @param staffId 推荐技师
     * @param goodsId 商品id
     */
    public ApiResult referrerGoods(Long[] ids, Long goodsId, Long staffId) {//用户，技师id问题
        if (null == goodsId) {
            return new ApiResult(0, "商品不存在");
        }

        String referrer_time = StringUtils.defaultIfBlank(RedisUtil.getString(ReferrerConfig.kReferrerConfig_Referrer_Time), "0");
        if (!StringUtils.equals(referrer_time, "0")){
            //  限制了推送间隔，则需要做验证
            String key = ReferrerConfig.kRecommendStaff + staffId;
            String referrer_num = StringUtils.defaultIfBlank(RedisUtil.getString(ReferrerConfig.kReferrerConfig_Referrer_Num), "0");
            if (!StringUtils.equals(referrer_time, "0")){
                //  限制了推送频率，则需要验证
                Long count = Redis.use().get(key);
                if (count != null && count >= NumberUtils.toLong(referrer_num)){
                    return new ApiResult(0, "推荐次数过于频繁，请稍后再试");
                }
                if (count == null || count == 0){
                    Redis.use().setex(key, NumberUtils.toInt(referrer_time), 1L);
                }else {
                    Redis.use().set(key, count + 1L);
                }
            }
        }

        Goods goods = goodsService.findGoodsById(goodsId);
        for (Long memberId : ids) {
            ReferrerGoods referrerGoods = new ReferrerGoods(staffId, memberId, goodsId, 1);
            try {
                informationService.staffMessage(goods, memberService.find(staffId), memberService.find(memberId));
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.save(referrerGoods);
        }
        //发推送
        Map result = new HashMap();
        List staffMembers = staffMemberDao.findAliasName(ids);
        result.put("goods", goods);
        result.put("members", staffMembers);
        return new ApiResult(1, "推荐成功", result);
    }

    /**
     * 分页查询单个客户推荐记录
     *
     * @param
     * @return
     */
    public ApiResult queryReferrerGoods(int pageNum, int pageSize, Long memberId) {
        Page PageInfo = referrerGoodsDao.queryReferrerGoods(pageNum, pageSize, memberId);
        Map map = myHomeService.sortByTime(PageInfo.getList(), "CreateDate");
        Map result = new HashMap();
        result.put("pageNumber", PageInfo.getPageNumber());
        result.put("pageSize", PageInfo.getPageSize());
        result.put("totalPage", PageInfo.getTotalPage());
        result.put("totalRow", PageInfo.getTotalRow());
        result.put("data", map);
        return new ApiResult(1, "", result);
    }
}
