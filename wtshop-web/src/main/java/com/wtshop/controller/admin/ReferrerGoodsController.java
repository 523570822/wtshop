package com.wtshop.controller.admin;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Page;
import com.wtshop.Pageable;
import com.wtshop.model.ReferrerConfig;
import com.wtshop.service.ReferrerGoodsService;
import com.wtshop.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by 蔺哲 on 2017/9/7.
 */
@ControllerBind(controllerKey = "/admin/referrer_goods")
public class ReferrerGoodsController extends BaseController {
    private ReferrerGoodsService referrerGoodsService = enhance(ReferrerGoodsService.class);

    /**
     * 编辑推荐设置
     */
    public void toEdit() {
        String referrer_time = StringUtils.defaultIfBlank(RedisUtil.getString(ReferrerConfig.kReferrerConfig_Referrer_Time), "0");
        String referrer_num = StringUtils.defaultIfBlank(RedisUtil.getString(ReferrerConfig.kReferrerConfig_Referrer_Num), "0");
        setAttr("referrer_time", referrer_time);
        setAttr("referrer_num", referrer_num);
        render("/admin/referrer_goods/edit.ftl");
    }

    /**
     * 修改或保存
     */
    public void edit() {
        String referrer_time = getPara("referrer_time");
        String referrer_num = getPara("referrer_num");
        RedisUtil.setString(ReferrerConfig.kReferrerConfig_Referrer_Time, StringUtils.defaultIfBlank(referrer_time, "0"));
        RedisUtil.setString(ReferrerConfig.kReferrerConfig_Referrer_Num, StringUtils.defaultIfBlank(referrer_num, "0"));
        addFlashMessage(SUCCESS_MESSAGE);
        redirect("list.jhtml");
    }

    /**
     * 单个商品统计
     */
    public void queryPageByGoodsId() {
        Pageable pageable = getBean(Pageable.class);
        Long goodsId = getParaToLong("goodsId");
        Page page = referrerGoodsService.queryPageByGoodsId(pageable, goodsId);
        setAttr("goodsId", goodsId);
        setAttr("page", page);
        setAttr("pageable", pageable);
        render("/admin/referrer_goods/goodsList.ftl");
    }

    /**
     * 所有商品推荐统计 默认推荐数量排序
     */
    public void list() {
        Pageable pageable = getBean(Pageable.class);
        Page page = referrerGoodsService.queryByPage(pageable);
        setAttr("pageable", pageable);
        setAttr("page", page);
        render("/admin/referrer_goods/list.ftl");
    }
}
