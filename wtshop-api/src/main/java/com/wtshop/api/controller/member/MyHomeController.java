package com.wtshop.api.controller.member;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Page;
import com.wtshop.CommonAttributes;
import com.wtshop.Pageable;
import com.wtshop.api.common.result.StaffResult;
import com.wtshop.api.controller.BaseAPIController;
import com.wtshop.api.interceptor.ErrorInterceptor;
import com.wtshop.interceptor.WapInterceptor;
import com.wtshop.model.*;
import com.wtshop.service.*;
import com.wtshop.util.ApiResult;
import com.wtshop.util.ObjectUtils;
import com.wtshop.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

/**
 * Controller - 我的页面
 * Created by 蔺哲 on 2017/8/22.
 */
@ControllerBind(controllerKey = "/api/member/myHome")
@Before({WapInterceptor.class, ErrorInterceptor.class})
public class MyHomeController extends BaseAPIController {
    public static final String headUrl = PropKit.use(CommonAttributes.wtshop_PROPERTIES_PATH).get("headUrl");
    //    public static final String headUrl = "http://shop.rxmao.cn";
    private MemberService memberService = enhance(MemberService.class);
    private MyHomeService myHomeService = enhance(MyHomeService.class);
    private FootPrintService footPrintService = enhance(FootPrintService.class);
    private ReferrerGoodsService referrerGoods = enhance(ReferrerGoodsService.class);
    private CommissionService commissionService = enhance(CommissionService.class);
    private MiaoBiGoodsService miaoBiGoodsService = enhance(MiaoBiGoodsService.class);
    private ReceiverService receiverService = enhance(ReceiverService.class);
    private AreaDescribeService areaDescribeService = enhance(AreaDescribeService.class);
    private AreaService areaService = enhance(AreaService.class);
    private ProductService productService = enhance(ProductService.class);
    private ButlerUpgradeLogService  butlerUpgradeLogService=enhance(ButlerUpgradeLogService.class);
    public static final int pageSizes = 10;


    //佣金记录
    public void commissionList() {
        Member staff = memberService.getCurrent();
        Integer pageNumber = getParaToInt("pageNumber");
        Date startDate = getParaToDate("startDate");
        Date endDate = getParaToDate("endDate");
        Integer type = getParaToInt("type");
        Pageable pageable = new Pageable(pageNumber, pageSizes);
        Page<CommissionHistory> historyList = commissionService.findHistoryList(staff, startDate, endDate, pageable, type);
        renderJson(ApiResult.success(historyList));
    }

    /**
     * 技师明细
     */
    public void staffInfoList() {
        String organId = memberService.findOrganId(memberService.getCurrent());
        if (organId == null) {
            renderJson(ApiResult.fail("您当前店铺身份有误,请联系后台管理员!"));
            return;
        }
        Integer pageNumber = getParaToInt("pageNumber");
        Pageable pageable = new Pageable(pageNumber, pageSizes);
        String staffName = getPara("staffName");
        Date startDate = getParaToDate("startDate");
        Date endDate = getParaToDate("endDate");
        //获取技师
        Page<CommissionHistory> staff = commissionService.findStaff(Long.parseLong(organId), startDate, endDate, pageable, staffName);
        List<StaffResult> staffResults = new ArrayList<>();
        for (CommissionHistory commissionHistory : staff.getList()) {
            Long staffId = commissionHistory.getStaffId();
            List<CommissionHistory> staffList = commissionService.findStaffList(staffId, startDate, endDate);
            StaffResult staffResult = new StaffResult(commissionHistory, staffList);
            if (staffId != null) {
                staffResults.add(staffResult);
            }
        }

        renderJson(ApiResult.success(staffResults));
    }

    //喵币商城
    public void miaoBiShop() {
        Member member = memberService.getCurrent();
        Integer pageNumber = getParaToInt("pageNumbers");
        // Page list = miaoBiGoodsService.findByPage(pageNumber,pageSizes);
        Page list = miaoBiGoodsService.findByPage(pageNumber, 9000);
        Map map = new HashMap();
        map.put("member", member);
        map.put("list", list);
        renderJson(new ApiResult(1, "", map));
    }

    //填写订单
    public void miaobiCheckout() {
        Long miaobiGoodsId = getParaToLong("miaobiGoodsId");
        Map result = new HashMap();
        Member member = memberService.getCurrent();

        if(StringUtils.isEmpty(member.getOnShareCode())){
            renderJson(ApiResult.fail(7,"请填写邀请码"));
            return;
        }

        if (ObjectUtils.isEmpty(member)) {
            renderJson(ApiResult.success(0, "用户信息不存在"));
        }
        //获取默认的收货地址
        Receiver defaultReceiver = receiverService.findDefault(member);
        //商品信息
        MiaobiGoods miaobiGoods = miaoBiGoodsService.find(miaobiGoodsId);

        if(member != null && miaobiGoods != null){
            if(member.getPoint().doubleValue() < miaobiGoods.getPriceMiaobi().doubleValue()){
                renderJson(ApiResult.fail("您的喵币不满足兑换条件!"));
                return;
            }
        }
        //商品配送
        String receiveTime = null;
        if (defaultReceiver != null) {
            AreaDescribe areaDescribe = areaDescribeService.findByAreaId(defaultReceiver.getAreaId());
            //判断本级地区是否填写
            if (areaDescribe != null && areaDescribe.getReceivingBegintime() != null) {
                receiveTime = areaDescribe.getReceivingBegintime();
            } else {
                AreaDescribe areaDescribes = areaDescribeService.findByAreaId(areaService.find(defaultReceiver.getAreaId()).getParentId());
                if (areaDescribes != null) {
                    receiveTime = areaDescribes.getReceivingBegintime();
                }
            }
        }
        result.put("member", member);
        result.put("receiveTime", receiveTime);
        result.put("defaultReceiver", defaultReceiver);
        result.put("miaobiGoods", miaobiGoods);

        renderJson(ApiResult.success(result));
    }
    //猜您喜欢

    /**
     * 我的足迹
     */
    public void myfoot() {
        Member member = memberService.getCurrent();
        Integer pageNumber = getParaToInt("pageNumbers");
        renderJson(footPrintService.queryMyFootPrint(member.getId()));
    }

    /**
     * 删除我的足迹
     */
    public void delMyfoot() {
        String[] values = StringUtils.split(getPara("id"), ",");
        Long[] ids = values == null ? null : convertToLong(values);

        footPrintService.delete(ids);


        renderJson(new ApiResult(1, "删除成功"));
    }

    /**
     * 我的二维码
     */
    public void code() {//需要携带自己的id
        Object staffId = "";
        Member member = memberService.getCurrent();
        String memberId = RedisUtil.getString(getPara("token"));
        if (StringUtils.isEmpty(memberId)) {
            staffId = member.getId();
        } else {
            staffId = memberId;
        }
        String url = headUrl + "/api/member/myHome/addMember.jhtml?staffId=" + staffId;
        if (url != null && !"".equals(url)) {
            ServletOutputStream stream = null;
            try {
                int width = 200;//图片的宽度
                int height = 200;//高度
                stream = getResponse().getOutputStream();
                QRCodeWriter writer = new QRCodeWriter();
                BitMatrix m = writer.encode(url, BarcodeFormat.QR_CODE, height, width);
                MatrixToImageWriter.writeToStream(m, "png", stream);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (stream != null) {
                    try {
                        stream.flush();
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 用户技师绑定关系
     */
    public void addMember() throws IOException {
        String isRxm = getRequest().getParameter("isRxm");
        if (StringUtils.isEmpty(isRxm) || "false".equals(isRxm)) {
            redirect("/openApp.html");
        } else {
            Long staffId = getParaToLong("staffId");
            Member member = memberService.getCurrent();
            renderJson(myHomeService.addMember(member.getId(), staffId));
        }
    }

    /**
     * 我的顾客
     */
    public void myMember() {
        Member member = memberService.getCurrent();
        String userName = getPara("userName");
        String type = getPara("type");
        String goodsId = getPara("goodsId");
        renderJson(myHomeService.queryMyUsers(member.getId(), userName, type, goodsId));
    }

    /**
     * 查看我的顾客
     */
    public void queryMember() {
        Long userId = getParaToLong("userId");
        renderJson(myHomeService.queryMember(userId));
    }

    /**
     * 客户备注
     */
    public void aliasName() {
        Long userId = getParaToLong("userId");
        String aliasNmae = getPara("aliasName");
        Member member = memberService.getCurrent();
        renderJson(myHomeService.aliasName(aliasNmae, userId));
    }

    /**
     * 给客户推荐商品
     */
    public void addReferrerGoods() {
        String[] values = StringUtils.split(getPara("id"), ",");
        Long[] ids = values == null ? null : convertToLong(values);
        Long goodsId = getParaToLong("goodsId");
        Product product = productService.findByGoodsId(goodsId);
        if (product.getStock() == 0) {
            renderJson(ApiResult.fail("该商品库存不足,请稍后尝试!"));
            return;
        }
        Member member = memberService.getCurrent();
        renderJson(referrerGoods.referrerGoods(ids, goodsId, member.getId()));
    }

    /**
     * 给顾客推荐过的商品
     */
    public void memberReferrerGoods() {
        Long memberId = getParaToLong("memberId");
        Integer pageNumber = getParaToInt("pageNumbers");
        renderJson(referrerGoods.queryReferrerGoods(pageNumber, pageSizes, memberId));
    }

    /**
     * 欢迎
     */
    public void welcome() {
        renderJson(new ApiResult(1, "你好", "呵呵哒"));
    }

    /**
     * 扫码入职
     */
    public void addOrgan() {
        String isRxm = getRequest().getParameter("isRxm");
        if (StringUtils.isEmpty(isRxm) || "false".equals(isRxm)) {
            redirect("/openApp.html");
        } else {
            Member member = memberService.getCurrent();
            if (StringUtils.isEmpty(getRequest().getHeader("token")) && "0".equals(getRequest().getHeader("token"))) {
                renderJson(new ApiResult(0, "绑定失败,请先登陆", ""));
            } else {
                String redisKey = "addOrganOfstaffId" + member.getId();
                if (StringUtils.isEmpty(RedisUtil.getString(redisKey))) {
                    String organId = getPara("organId");
                    ApiResult result = myHomeService.addOrgan(member, organId);
                    RedisUtil.setString(redisKey, 3, "true");
                    renderJson(result);
                }
            }
        }
    }

    /**
     * 服务门店
     */
    public void myOrgan() {
        Member member = memberService.getCurrent();
        ApiResult result = myHomeService.queryMyOrgan(member.getPhone());
        renderJson(result);
    }

    /**
     * 解除关系
     */
    public void removeOrgan() throws IOException {
        String organId = getPara("organId");
        Member member = memberService.getCurrent();
        renderJson(myHomeService.removeOrgan(member, organId));
    }

    /**
     * 获取是否需要提醒弹出升级协议
     */
public void findButler(){
    Member member = memberService.getCurrent();
    Map<String,Object> map=new HashMap<>();
    if(member==null||member.getShareCode()==null){
        map.put("status",1);/////
        renderJson(new ApiResult(1,"",map));
        return;
    }

    List<Member> mmss = memberService.findMemberByOnShareJ(member.getShareCode());


//升级金牌
   // map.put("status",1);
    if(mmss.size()>=15){
        List<ButlerUpgradeLog> butlerUpgradeLog=   butlerUpgradeLogService.findByMemberId(member.getId());
         if(butlerUpgradeLog.size()==0){
             map.put("status",2);
             renderJson(new ApiResult(1,"",map));
             return;
         }else if(butlerUpgradeLog.get(0).getStatus()==2){
             map.put("status",2);
             renderJson(new ApiResult(1,"",map));
             return;
         }else {
             map.put("status",1); ////
             renderJson(new ApiResult(1,"",map));
             return;
         }
    }else{
        map.put("status",1);/////
        renderJson(new ApiResult(1,"",map));
        return;
    }


}
    /**
     *
     * 取消提醒弹出升级协议
     */
    public void cancelButler(){
        Member member = memberService.getCurrent();
        Map<String,Object> map=new HashMap<>();
            List<ButlerUpgradeLog> butlerUpgradeLogList=   butlerUpgradeLogService.findByMemberId(member.getId());
            if(butlerUpgradeLogList.size()==0){
                ButlerUpgradeLog butlerUpgradeLog=new ButlerUpgradeLog();
                butlerUpgradeLog.setMemberId(member.getId());
                butlerUpgradeLog.setStatus(0);
                butlerUpgradeLogService.save(butlerUpgradeLog);
            }else {
                ButlerUpgradeLog butlerUpgradeLog= butlerUpgradeLogList.get(0);
                butlerUpgradeLog.setStatus(0);
                butlerUpgradeLogService.update(butlerUpgradeLog);

            }
        renderJson(new ApiResult(1,"取消成功"));
        return;
    }

    /**
     * 获取是否需要提醒弹出升级协议
     */
    public void agreeButler() throws UnsupportedEncodingException {
        Member member = memberService.getCurrent();
        List<Member> mmss = memberService.findMemberByOnShareJ(member.getShareCode());
        ButlerUpgradeLog butlerUpgradeLog = getModel(ButlerUpgradeLog.class);

        String name= URLDecoder.decode(butlerUpgradeLog.getName(),"UTF-8");
        String address= URLDecoder.decode(butlerUpgradeLog.getAddress(),"UTF-8");
        //String bank_name = URLDecoder.decode(butlerUpgradeLog.getBankName(),"UTF-8");
      ///  String bank = URLDecoder.decode(butlerUpgradeLog.getBank(),"UTF-8");
        butlerUpgradeLog.setName(name);
        butlerUpgradeLog.setAddress(address);
       // butlerUpgradeLog.setBankName(bank_name);
     //   butlerUpgradeLog.setBank(bank);
        Map<String,Object> map=new HashMap<>();
        //升级金牌
        if(mmss.size()<15){
            renderJson( ApiResult.fail("升级失败升级人数不够"));
            return;
        }else if(member.getHousekeeperId()<=2){
            List<ButlerUpgradeLog> butlerUpgradeLogList=   butlerUpgradeLogService.findByMemberId(member.getId());
            if(butlerUpgradeLogList.size()==0){

                butlerUpgradeLog.setMemberId(member.getId());
                butlerUpgradeLog.setStatus(1);
                butlerUpgradeLog.setUpDate(new Date());
                butlerUpgradeLogService.save(butlerUpgradeLog);
            }else {
                butlerUpgradeLog.setId(butlerUpgradeLogList.get(0).getId());
                butlerUpgradeLog.setMemberId(member.getId());
                butlerUpgradeLog.setStatus(1);
                butlerUpgradeLog.setUpDate(new Date());
                butlerUpgradeLogService.update(butlerUpgradeLog);
            }
            member.setHousekeeperId(3l);
            memberService.update(member);
        }else if(member.getHousekeeperId()>2){
            renderJson( ApiResult.fail("已经升过级了"));
            return;
        }
        renderJson(new ApiResult(1,"恭喜您成为金牌掌柜"));
        return;
    }
}
