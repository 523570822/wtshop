package com.wtshop.api.controller.member;

import com.alibaba.fastjson.JSONArray;
import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.upload.UploadFile;
import com.wtshop.FileType;
import com.wtshop.Pageable;
import com.wtshop.Setting;
import com.wtshop.api.common.result.OrderGoods;
import com.wtshop.api.common.result.member.OrderListResult;
import com.wtshop.api.interceptor.TokenInterceptor;
import com.wtshop.util.ApiResult;
import com.wtshop.api.common.result.OrderItemList;
import com.wtshop.api.common.result.ReturnItem;
import com.wtshop.api.common.result.member.ServiceListResult;
import com.wtshop.api.common.result.member.ServiceNotDetailsResult;
import com.wtshop.api.controller.BaseAPIController;
import com.wtshop.api.interceptor.ErrorInterceptor;
import com.wtshop.interceptor.WapInterceptor;
import com.wtshop.model.*;
import com.wtshop.service.*;
import com.wtshop.util.DateUtils;
import com.wtshop.util.SystemUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * Controller - 会员中心 - 售后服务
 * 
 * 
 */
@ControllerBind(controllerKey = "/api/member/service")
@Before({WapInterceptor.class, ErrorInterceptor.class, TokenInterceptor.class})
public class ServiceAPIController extends BaseAPIController {
	private ReturnsItemService returnsItemService = enhance(ReturnsItemService.class);
    private ReturnsService returnsService = enhance(ReturnsService.class);
	private OrderItemService orderItemService = enhance(OrderItemService.class);
	private OrderService orderService = enhance(OrderService.class);
	private MemberService memberService =enhance(MemberService.class);
	private FileService fileService = enhance(FileService.class);
	private ReturnsItemProgressService returnsItemProgressService = enhance(ReturnsItemProgressService.class);


	/**
	 * 客服电话
	 */
	public void phone(){
		Setting setting = SystemUtils.getSetting();
		String phone = setting.getPhone();
		renderJson(ApiResult.success(phone));
	}

	/**
	 * 申请列表
	 */
	public void refundList(){
		Member member = memberService.getCurrent();
		renderJson(ApiResult.success(orderService.findPages(member)));
	}


	/**
	 * 进度列表
	 */
	public void list() {
		Integer status = getParaToInt("status", 0);

        List<ReturnItem> retrunLists = new ArrayList<>();

        List<Returns> returnsList = returnsService.findByMember(memberService.getCurrent(), status);
        for(Returns returns : returnsList){
            List<ReturnsItem> returnItem = returnsItemService.findByReturnId(returns.getId());

            ReturnItem returnItems = new ReturnItem(returns, returnItem);
            retrunLists.add(returnItems);
        }
		ServiceListResult serviceListResult = new ServiceListResult(retrunLists);
		renderJson(ApiResult.success(serviceListResult));
	}

	/**
	 * 填写退货详情
	 * type 1退货 2换货
	 */
	public void alert_refund() {
		Long id = getParaToLong("orderItemIds");
		OrderItem orderItem = orderItemService.find(id);
		BigDecimal amount = orderItem.getPrice().multiply(new BigDecimal(orderItem.getQuantity()));

		Integer qualtity = getParaToInt("qualtity");
		Integer type = getParaToInt("type");

		String desc = getPara("desc");
		String reason = getPara("reason");
		String [] images = getParaValues("image");
		ReturnsItem returnsItem = getModel(ReturnsItem.class);

		if( 1 == type){ //换货
			amount = new BigDecimal(0);
		}

		if (orderItem == null || orderItem.getReturnableQuantity() <= 0) {
			renderJson(ApiResult.fail("可退货数有误!"));
			return;
		}
		
		Member member = memberService.getCurrent();
		if (member == null) {
			renderJson(ApiResult.fail("当前用户不能是空!"));
			return;
		}
		returnsItem.setCause(reason);
		returnsItem.setType(type);
		returnsItem.setDesc(desc);
		returnsItem.setImages(JSONArray.toJSONString(images));
		returnsItem.setQuantity(qualtity);
		returnsItem.setName(orderItem.getName());
		returnsItem.setSn(orderItem.getSn());
		returnsItem.setSpecifications(orderItem.getSpecifications());
		returnsItem.setAmount(amount);
		returnsItem.setMemberId(member.getId());
		returnsItem.setProductId(orderItem.getProductId());
		returnsItem.setStatus(ReturnsItem.Status.pendingReview.ordinal());
		List<ReturnsItem> returnsItems = new ArrayList<ReturnsItem>();
		returnsItems.add(returnsItem);
		Returns returns = new Returns();
		returns.setReturnsItems(returnsItems);
		returns.setIsDelete(false);
		returns.setCategory(type);
		orderService.returns(orderItem.getOrder(), returns, null);

		renderJson(ApiResult.success(returnsItem.getId(),"您的售后服务申请成功,请等待店家审核!"));
	}


	/**
	 * 退货退款(未发货)
	 */
	public void refund() {
		Long orderId = getParaToLong("orderId");
		String desc = getPara("desc");
		String reason = getPara("reason");
		String [] images = getParaValues("image");
		List<OrderItem> orderItemList = orderItemService.findOrderItemList(orderId);

		Member member = memberService.getCurrent();
		if (member == null) {
			renderJson(ApiResult.fail("当前用户不能是空!"));
			return;
		}
		List<ReturnsItem> returnsItems = new ArrayList<ReturnsItem>();
        for(OrderItem orderItem1 : orderItemList){
			ReturnsItem returnsItem = new ReturnsItem();
			returnsItem.setCause(reason);
			returnsItem.setType(1);
			returnsItem.setAmount(orderItem1.getPrice().multiply(new BigDecimal(orderItem1.getQuantity())));
			returnsItem.setDesc(desc);
			returnsItem.setImages(JSONArray.toJSONString(images));
			returnsItem.setQuantity(orderItem1.getQuantity());
			returnsItem.setName(orderItem1.getName());
			returnsItem.setSn(orderItem1.getSn());
			returnsItem.setSpecifications(orderItem1.getSpecifications());
			returnsItem.setMemberId(member.getId());
			returnsItem.setProductId(orderItem1.getProductId());
			returnsItem.setStatus(ReturnsItem.Status.pendingReview.ordinal());
			returnsItems.add(returnsItem);
		}


		Returns returns = new Returns();
		returns.setReturnsItems(returnsItems);
		returns.setIsDelete(false);
		returns.setType(0);
		returns.setCategory(2);
		orderService.returnd(orderService.find(orderId), returns, null);

		renderJson(ApiResult.success("您的售后服务申请成功,请等待店家审核!"));
	}




	
	/**
	 * 等待商家处理退货申请
	 */
	public void view_detail() {
		Long id = getParaToLong("orderItemIds");
		ReturnsItem returnsItem = returnsItemService.find(id);
		ServiceNotDetailsResult aReturn = new ServiceNotDetailsResult(returnsItem, "return");
		renderJson(ApiResult.success(aReturn));
	}
	
	/**
	 * 取消售后申请
	 */
	public void cancel() {
		Long id = getParaToLong("orderItemIds");
		
		Map<String, String> map = new HashMap<String, String>();
		ReturnsItem returnsItem = returnsItemService.find(id);
		if (returnsItem == null) {
			renderJson(ApiResult.fail("退货项不存在!"));
			return;
		}
		
		if (returnsItem.getStatus() != ReturnsItem.Status.pendingReview.ordinal()) {
			renderJson(ApiResult.fail("只有【待审核】才能取消!"));
			return;
		}
		
		Member member = memberService.getCurrent();
		if (member == null) {
			renderJson(ApiResult.fail("当前用户不能是空!"));
			return;
		}
		returnsItem.setStatus(ReturnsItem.Status.canceled.ordinal());
		returnsItem.setMemberId(member.getId());
		returnsItem.setModifyDate(DateUtils.getSysDate());
		returnsItem.update();

		List<ReturnsItemProgress> returnsItemProgresses = returnsItemProgressService.findByReturnId(returnsItem.getId());
		for( ReturnsItemProgress returnsItemProgress : returnsItemProgresses){
			returnsItemProgress.setStatus(0);
			returnsItemProgressService.update(returnsItemProgress);
		}

		ReturnsItemProgress returnsItemProgress = new ReturnsItemProgress();
		returnsItemProgress.setDesc("您的售后申请已取消");
		returnsItemProgress.setReturnsItemid(returnsItem.getId());
		returnsItemProgressService.save(returnsItemProgress);

		renderJson(ApiResult.success("取消成功!"));
	}


	/**
	 * 进度查询
	 */
	public void progress(){
		Long returnsItems = getParaToLong("returnsItem");
		List<ReturnsItemProgress> progress = returnsItemProgressService.findProgressById(returnsItems);
		renderJson(ApiResult.success(progress));

	}


	/**
	 * 上传
	 */
	public void upload() {
		UploadFile file = getFile();
		FileType fileType = FileType.valueOf(getPara("fileTypes", "image"));

		if (fileType == null || file == null || file.getFile().length() <= 0) {
			renderJson(ApiResult.fail("请选择选图片"));
			return;
		}
		if (!fileService.isValid(fileType, file)) {
			renderJson(ApiResult.fail(com.wtshop.Message.warn("admin.upload.invalid").toString()));
			return;
		}
		String url = fileService.upload(fileType, file, false);
		if (StringUtils.isEmpty(url)) {
			renderJson(ApiResult.fail(com.wtshop.Message.warn("admin.upload.error").toString()));
			return;
		}
		renderJson(ApiResult.success(url));
	}

	/**
	 * 售后搜索
	 */
	public void search(){
		String sn = getPara("sn");
		String name = getPara("name");
        //type=1 售后查询   type=2 进度查询
		Integer type = getParaToInt("type");
		Member member = memberService.getCurrent();
		if(1 == type){
			List<OrderItemList> orderItemLists = new ArrayList<>();
			List<OrderItem> orderItem = returnsItemService.findOrder(name, sn ,member);
			List<Long> orderIdList = new ArrayList<>();
			for(OrderItem orderItem1 : orderItem){
				if(!orderIdList.contains(orderItem1.getOrderId())){
					orderIdList.add(orderItem1.getOrderId());
				}
			}
			for(Long orderId : orderIdList){
				Order order = orderService.find(orderId);
				List<OrderItem> orderItemList = orderItemService.findOrderItemList(orderId);
				OrderItemList orderItemList1 = new OrderItemList(order, orderItemList);
				orderItemLists.add(orderItemList1);
			}
			renderJson(ApiResult.success(orderItemLists));
		}else if (2 == type){
			List<ReturnItem> retrunLists = new ArrayList<>();

            List<Returns> returnsList = returnsService.findReturnsList(name, sn ,member);
			for(Returns returns : returnsList){
                List<ReturnsItem> returnItem = returnsItemService.findByReturnId(returns.getId());

				ReturnItem returnItems = new ReturnItem(returns, returnItem);
				retrunLists.add(returnItems);
			}

			renderJson(ApiResult.success(retrunLists));




		}else{
			renderJson(ApiResult.fail("系统内部错误!"));
		}

	}


}
