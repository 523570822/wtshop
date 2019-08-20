package com.wtshop.controller.admin;

import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.plugin.redis.Redis;
import com.wtshop.Filter;
import com.wtshop.Message;
import com.wtshop.Pageable;
import com.wtshop.constants.Code;
import com.wtshop.model.Admin;
import com.wtshop.model.ReverseAuction;
import com.wtshop.model.ReverseAuctionDetail;
import com.wtshop.service.AdminService;
import com.wtshop.service.ProductService;
import com.wtshop.service.ReverseAuctionDetailService;
import com.wtshop.service.ReverseAuctionService;
import com.wtshop.shiro.core.SubjectKit;
import com.wtshop.util.ApiResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import redis.clients.jedis.Jedis;

import java.text.ParseException;
import java.util.*;

import static com.wtshop.api.controller.BaseAPIController.convertToLong;


/**
 * Created by 蔺哲 on 2017/5/10.
 * 倒拍控制器
 */
@ControllerBind(controllerKey = "/admin/reverseAuction")
public class ReverseAuctionController extends BaseController {

    private ReverseAuctionService reverseAuctionService = enhance(ReverseAuctionService.class);
    private ReverseAuctionDetailService reverseAuctionDetailService = enhance(ReverseAuctionDetailService.class);
    private ProductService productService = enhance(ProductService.class);
    private AdminService adminService = enhance(AdminService.class);

    public final static String kReverseAuctionComment = "ReverseAuction:Comment:";
    public final static String kReverseAuctionPublish = "ReverseAuction:Publish:";
    public final static String kReverseAuctionDelete = "ReverseAuction:Delete:";
    public final static String kReverseAuctionSetting = "ReverseAuction:Setting:";

    /**
     * 列表
     */
    public void list() {

        Pageable pageable = getBean(Pageable.class);
        pageable.setFilter(Filter.eq("is_delete", Code.FALSE));
        setAttr("pageable", pageable);
        setAttr("page", reverseAuctionService.findPage(pageable));
        render("/admin/reverseAuction/list.ftl");
    }

    /**
     * 添加倒拍
     */
    public void add() {
        render("/admin/reverseAuction/add.ftl");
    }

    /**
     * 提交审核
     */
    public void review() {
        String auctionId = getPara("auctionId");
        int state = getParaToInt("state");
        ReverseAuction reverseAuction = reverseAuctionService.find(Long.valueOf(auctionId));
        if (reverseAuction == null) {
            renderJson(ApiResult.fail("活动不存在"));
            return;
        }
        if (reverseAuction.isOnline()) {
            renderJson(ApiResult.fail("活动已上架"));
            return;
        }
        if (org.apache.commons.collections.CollectionUtils.isEmpty(reverseAuction.getReverseAuctionDetails())) {
            renderJson(ApiResult.fail("请先添加商品"));
            return;
        }

        if (state != ReverseAuction.State_Review_ProductSpecialist && state != ReverseAuction.State_Review_ProductManager
                && state != ReverseAuction.State_Review_Financial && state != ReverseAuction.State_Review_FinanceDirector) {
            renderJson(ApiResult.fail("错误的审核状态"));
            return;
        }

        if (state == ReverseAuction.State_Review_ProductManager && !SubjectKit.getSubject().hasRole(Code.R_ProductSpecialist)) {
            renderJson(ApiResult.fail("没有产品专员相关权限"));
            return;
        }
        if (state == ReverseAuction.State_Review_Financial && !SubjectKit.getSubject().hasRole(Code.R_ProductManager)) {
            renderJson(ApiResult.fail("没有产品主管相关权限"));
            return;
        }
        if (state == ReverseAuction.State_Review_FinanceDirector && !SubjectKit.getSubject().hasRole(Code.R_Finance)) {
            renderJson(ApiResult.fail("没有财务相关权限"));
            return;
        }
        if (state == ReverseAuction.State_Publish && !SubjectKit.getSubject().hasRole(Code.R_FinanceDirector)) {
            renderJson(ApiResult.fail("没有财务主管相关权限"));
            return;
        }
        reverseAuction.setState(state);
        reverseAuctionService.update(reverseAuction);
        renderJson(ApiResult.success("提交审核成功"));
    }

    /**
     * 打回
     */
    public void reject() {
        String auctionId = getPara("auctionId");
        int state = getParaToInt("state");
        ReverseAuction reverseAuction = reverseAuctionService.find(Long.valueOf(auctionId));
        if (reverseAuction == null) {
            renderJson(ApiResult.fail("活动不存在"));
            return;
        }
        if (reverseAuction.isOnline()) {
            renderJson(ApiResult.fail("活动已上架"));
            return;
        }
        if (org.apache.commons.collections.CollectionUtils.isEmpty(reverseAuction.getReverseAuctionDetails())) {
            renderJson(ApiResult.fail("请先添加商品"));
            return;
        }
        if (state != ReverseAuction.State_Review_ProductSpecialist){
            renderJson(ApiResult.fail("错误的目标状态"));
            return;
        }
        if (reverseAuction.getState().intValue() != ReverseAuction.State_Review_ProductSpecialist && reverseAuction.getState().intValue() != ReverseAuction.State_Review_ProductManager
                && reverseAuction.getState().intValue() != ReverseAuction.State_Review_Financial && reverseAuction.getState().intValue() != ReverseAuction.State_Review_FinanceDirector) {
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
            Redis.use().zadd(kReverseAuctionComment + auctionId, System.currentTimeMillis(), map);
        }

        reverseAuction.setState(state);
        reverseAuctionService.update(reverseAuction);
        renderJson(ApiResult.success("已打回到产品专员"));
    }

    /**
     * 查看倒拍意见
     */
    public void comment() {
        String auctionId = getPara("auctionId");
        Set<Map<String, String>> set = Redis.use().zrevrange(kReverseAuctionComment + auctionId, 0, Long.MAX_VALUE);
        renderJson(ApiResult.success(set));
    }

    /**
     * 保存倒拍
     */
    public void save() throws ParseException {
        String expireDate = getPara("expireDate");
        ReverseAuction reverseAuction = getModel(ReverseAuction.class);
        reverseAuction.setState(ReverseAuction.State_Draft);
        reverseAuction.setIsDelete(Code.FALSE);
        reverseAuction.setOrders((int) (System.currentTimeMillis() / 1000));
        reverseAuction.setExpireDate(DateUtils.parseDate(expireDate, "yyyy-MM-dd HH:mm:ss"));
        reverseAuctionService.save(reverseAuction);
        redirect("list.jhtml");
    }

    /**
     * 编辑
     */
    public void edit() {
        Long id = getParaToLong("id");
        setAttr("reverseAuction", reverseAuctionService.find(id));
        render("/admin/reverseAuction/edit.ftl");
    }

    /**
     * 更新
     */
    public void update() throws ParseException {

        ReverseAuction reverseAuction = getModel(ReverseAuction.class);
        ReverseAuction dbReverseAuction = reverseAuctionService.find(reverseAuction.getId());
        if (!dbReverseAuction.isOnline()) {
            String expireDate = getPara("expireDate");
            reverseAuction.setExpireDate(DateUtils.parseDate(expireDate, "yyyy-MM-dd HH:mm:ss"));
            reverseAuctionService.update(reverseAuction);
            addFlashMessage(SUCCESS_MESSAGE);
        } else {
            addFlashMessage(new Message(Message.Type.error, "活动已经上线，不可修改"));
        }
        redirect("list.jhtml");
    }


    /**
     * 编辑商品
     */
    public void editGoods() {
        Long id = getParaToLong("id");
        ReverseAuction reverseauction = reverseAuctionService.find(id);
        Integer detailNum = 0;
        if (reverseauction.getReverseAuctionDetails().size() > 0) {
            detailNum = reverseauction.getReverseAuctionDetails().size();
        }
        setAttr("detailNum", detailNum);
        setAttr("reverseAuction", reverseauction);
        render("/admin/reverseAuction/editGoods.ftl");
    }

    /**
     * 保存或修改商品
     */
    public void saveReverseAuctionDetail() {
        Long reverseAuctionId = getParaToLong("reverseAuctionId");
        ReverseAuction oldReverseauction = reverseAuctionService.find(reverseAuctionId);
        if (oldReverseauction == null) {
            addFlashMessage(new Message(Message.Type.error, "活动不存在"));
            redirect("list.jhtml");
        }
        if (oldReverseauction.isOnline()) {
            addFlashMessage(new Message(Message.Type.error, "活动已发布，不可修改"));
            redirect("list.jhtml");
        }
        List<ReverseAuctionDetail> list = getModels(ReverseAuctionDetail.class);
        reverseAuctionDetailService.updateList(list, reverseAuctionId);
        addFlashMessage(SUCCESS_MESSAGE);
        redirect("list.jhtml");
    }

    /**
     * 查看倒拍商品
     */
    public void showGoods() {
        Long reverseAuctionId = getParaToLong("reverseAuctionId");
        ReverseAuction reverseauction = reverseAuctionService.find(reverseAuctionId);
        setAttr("reverseauction", reverseauction);
        render("/admin/reverseAuction/showGoods.ftl");
    }

    /**
     * 倒拍删除
     */
    public void delete() {
        String[] values = org.apache.commons.lang3.StringUtils.split(getPara("ids"), ",");
        Long[] ids = values == null ? null : convertToLong(values);
        if (ids != null){
            for (Long auctioId : ids){
                Redis.use().del(kReverseAuctionComment + String.valueOf(auctioId));
            }
        }
        reverseAuctionService.delete(ids);
        renderJson(ApiResult.success());
    }

    /**
     * flag 0表示倒拍商品添加  1表示商品主题添加
     */
    public void choose() {
        String flag = getPara("flag");
        Pageable pageable = getBean(Pageable.class);
        setAttr("flag", flag);
        setAttr("pageable", pageable);
        setAttr("page", productService.findReverseAuctionByPage(pageable, flag));
        render("/admin/reverseAuction/choose.ftl");
    }

    /**
     * 帮抢商品选择
     */
    public void chooseGoods() {
        String flag = getPara("flag");
        Pageable pageable = getBean(Pageable.class);
        setAttr("flag", flag);
        setAttr("pageable", pageable);
        setAttr("page", productService.findFuDaiGoodsByPage(pageable));
        render("/admin/reverseAuction/choose.ftl");
    }

    // 倒拍活动下架
    @Before(Tx.class)
    public void offline() {
        String auctionId = getPara("auctionId");
        ReverseAuction reverseAuction = reverseAuctionService.find(Long.valueOf(auctionId));
        if (reverseAuction == null) {
            renderJson(ApiResult.fail("活动不存在"));
            return;
        }
        if (!reverseAuction.isOnline()) {
            renderJson(ApiResult.fail("活动不是上架状态"));
            return;
        }
        reverseAuction.setState(ReverseAuction.State_Stop);
        reverseAuctionService.update(reverseAuction);
        sendCmdMsg(kReverseAuctionDelete + reverseAuction.getId());

        Redis.use().del(kReverseAuctionComment + auctionId);

        renderJson(ApiResult.success("下架成功"));
    }

    // 倒拍活动上架
    @Before(Tx.class)
    public void online() {
        String auctionId = getPara("auctionId");
        ReverseAuction reverseAuction = reverseAuctionService.find(Long.valueOf(auctionId));
        if (reverseAuction == null) {
            renderJson(ApiResult.fail("活动不存在"));
            return;
        }
        if (reverseAuction.isOnline()) {
            renderJson(ApiResult.fail("活动已上架"));
            return;
        }
        if (org.apache.commons.collections.CollectionUtils.isEmpty(reverseAuction.getReverseAuctionDetails())) {
            renderJson(ApiResult.fail("请先添加商品"));
            return;
        }
        reverseAuction.setState(ReverseAuction.State_Publish);
        reverseAuctionService.update(reverseAuction);
        sendCmdMsg(kReverseAuctionPublish + reverseAuction.getId());
        Redis.use().del(kReverseAuctionComment + auctionId);
        renderJson(ApiResult.success("上架成功"));
    }


    //修改配置
    public void config() {
        String setting = (String) org.apache.commons.lang3.ObjectUtils.defaultIfNull(Redis.use("queue").get(Code.kAuctionSetting), "");
        String[] settings = org.apache.commons.lang3.StringUtils.splitByWholeSeparatorPreserveAllTokens(setting, ",");
        if (org.apache.commons.lang3.StringUtils.isBlank(setting) || settings.length != 3) {
            setAttr("maxPayTimeInSecond", 180);
            setAttr("downPeriodInSecond", 10);
            setAttr("downPriceInCent", 1);
        } else {
            setAttr("maxPayTimeInSecond", settings[0]);
            setAttr("downPeriodInSecond", settings[1]);
            setAttr("downPriceInCent", settings[2]);
        }
        render("/admin/reverseAuction/config.ftl");
    }

    //修改配置
    public void saveConfig() {
        int maxPayTimeInSecond = getParaToInt("maxPayTimeInSecond");
        int downPeriodInSecond = getParaToInt("downPeriodInSecond");
        int downPriceInCent = getParaToInt("downPriceInCent");
        Redis.use("queue").set(Code.kAuctionSetting, maxPayTimeInSecond + "," + downPeriodInSecond + "," + downPriceInCent); //  设置默认值
        sendCmdMsg(kReverseAuctionSetting);
        addFlashMessage(SUCCESS_MESSAGE);
        redirect("list.jhtml");
    }

    public void sendCmdMsg(String cmdString) {
        Jedis jedis = Redis.use("queue").getJedis();
        try {
            jedis.rpush("ReverseAuction", cmdString);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (jedis != null) {
                Redis.use("queue").close(jedis);
            }
        }
    }


}
