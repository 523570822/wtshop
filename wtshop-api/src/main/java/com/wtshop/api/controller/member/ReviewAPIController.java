package com.wtshop.api.controller.member;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.i18n.I18n;
import com.jfinal.i18n.Res;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;

import com.jfinal.upload.UploadFile;
import com.wtshop.FileType;
import com.wtshop.Message;
import com.wtshop.Pageable;
import com.wtshop.Setting;
import com.wtshop.api.common.result.ImageResult;
import com.wtshop.util.ApiResult;
import com.wtshop.api.common.result.member.ReviewAddResult;
import com.wtshop.api.common.result.member.ReviewFindResult;
import com.wtshop.api.common.result.member.ReviewListResult;
import com.wtshop.api.controller.BaseAPIController;
import com.wtshop.api.interceptor.ErrorInterceptor;
import com.wtshop.exception.ResourceNotFoundException;
import com.wtshop.interceptor.WapInterceptor;
import com.wtshop.model.*;
import com.wtshop.service.*;
import com.wtshop.util.SystemUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Controller - 评论
 * 
 *
 */
@ControllerBind(controllerKey = "/api/member/review")
@Before({WapInterceptor.class, ErrorInterceptor.class})
public class ReviewAPIController extends BaseAPIController {

	
	private FileService fileService = enhance(FileService.class);
	private MemberService memberService =enhance(MemberService.class);
	private OrderItemService orderItemService = enhance(OrderItemService.class);
	private ReviewService reviewService = enhance(ReviewService.class);
	private OrderService orderService = enhance(OrderService.class);
	
	/**
	 * 上传
	 */
	public void upload() {
		UploadFile file = getFile();
		FileType fileType = FileType.valueOf(getPara("fileTypes", "image"));

		JSONObject object = new JSONObject();
		if (fileType == null || file == null || file.getFile().length() <= 0) {
			renderJson(ApiResult.fail("请选择选图片"));
			return;
		}
		if (!fileService.isValid(fileType, file)) {
			renderJson(ApiResult.fail(Message.warn("admin.upload.invalid").toString()));
			return;
		}
		String url = fileService.upload(fileType, file, false);

		ImageResult imageResult = new ImageResult(file.getOriginalFileName(), url);
		if (StringUtils.isEmpty(url)) {
			renderJson(ApiResult.fail(Message.warn("admin.upload.error").toString()));
			return;
		}
		renderJson(ApiResult.success(imageResult));
	}

	/**
	 * 多图上传
	 */
	public void uploads() {
		List<UploadFile> files = getFiles();
		FileType fileType = FileType.valueOf(getPara("fileTypes", "image"));

		List<ImageResult> imageResult = new ArrayList<ImageResult>();
		if (fileType == null || files.size() == 0 ) {
			renderJson(ApiResult.fail("请选择选图片"));
			return;
		}
		for(UploadFile file : files){
			if (!fileService.isValid(fileType, file)) {
				renderJson(ApiResult.fail(Message.warn("admin.upload.invalid").toString()));
				return;
			}
			String url = fileService.upload(fileType, file, false);

			ImageResult image = new ImageResult(file.getOriginalFileName(), url);
			imageResult.add(image);
			if (StringUtils.isEmpty(url)) {
				renderJson(ApiResult.fail(Message.warn("admin.upload.error").toString()));
				return;
			}
		}

		renderJson(ApiResult.success(imageResult));
	}
	
	/**
	 * 列表
	 * {"msg":"","code":1,"data":{"review":false,"pendingOrderItems":{"totalRow":0,"pageNumber":1,"firstPage":true,"lastPage":true,"totalPage":0,"pageSize":10,"list":[]}}}
	 */
	public void list() {
		Boolean isReview = getParaToBoolean("isReviews", false);
		Integer pageNumber = getParaToInt("pageNumber");
		Pageable pageable = new Pageable();
		pageable.setPageSize(10);
		pageable.setPageNumber(pageNumber);
		Member member = memberService.getCurrent();
		Page<OrderItem> pendingOrderItems = reviewService.findPendingOrderItems(member, isReview, pageable);
		ReviewListResult reviewListResult = new ReviewListResult(isReview, pendingOrderItems);
		renderJson(ApiResult.success(reviewListResult));
	}
	
	/**
	 * 添加
	 * {"msg":"","code":1,"data":{"orderItem":{"create_date":"2017-05-24 15:06:10","id":95,"is_delivery":true,"is_review":false,"modify_date":"2017-05-24 15:06:10","name":"新品上市 欧莱雅男士洗面奶矿漠泥长效控油保湿洁面护肤品套装","order_id":83,"price":249.000000,"product_id":86,"quantity":1,"returned_quantity":0,"shipped_quantity":0,"sn":"201705221111","specifications":"[\"黑色\"]","thumbnail":"/upload/image/201705/73e96e1e-2a89-46e9-a11b-5f942d0553ff-thumbnail.jpg","type":0,"version":0,"weight":1000},"goods":{"attribute_value0":null,"attribute_value1":null,"attribute_value10":null,"attribute_value11":null,"attribute_value12":null,"attribute_value13":null,"attribute_value14":null,"attribute_value15":null,"attribute_value16":null,"attribute_value17":null,"attribute_value18":null,"attribute_value19":null,"attribute_value2":null,"attribute_value3":null,"attribute_value4":null,"attribute_value5":null,"attribute_value6":null,"attribute_value7":null,"attribute_value8":null,"attribute_value9":null,"brand_id":50,"caption":"控油去油 长效保湿 调节水油平衡","create_date":"2017-05-22 14:19:21","generate_method":1,"hits":0,"id":64,"image":"/upload/image/201705/ba801bbb-37be-45e1-a576-e59df54753e5.jpg","introduction":"<p><img src=\"/upload/image/201705/e13f093e-0f6c-4771-88cb-be595666c409.png\"/></p>","is_delivery":true,"is_list":true,"is_marketable":true,"is_top":false,"keyword":null,"market_price":298.800000,"memo":null,"modify_date":"2017-05-22 17:50:27","month_hits":0,"month_hits_date":"2017-05-22 14:19:21","month_sales":0,"month_sales_date":"2017-05-22 14:19:21","name":"新品上市 欧莱雅男士洗面奶矿漠泥长效控油保湿洁面护肤品套装","parameter_values":"[{\"group\":\"产品参数\",\"entries\":[{\"name\":\"化妆品净含量\",\"value\":\"套装容量\"},{\"name\":\"产地\",\"value\":\"中国\"},{\"name\":\"功效\",\"value\":\"补水\"},{\"name\":\"规格类型\",\"value\":\"正常规格\"},{\"name\":\"化妆品保质期\",\"value\":\"3年\"},{\"name\":\"适合肤质\",\"value\":\"油性肤质\"}]}]","price":249.000000,"product_category_id":245,"product_images":"[{\"source\":\"/upload/image/201705/73e96e1e-2a89-46e9-a11b-5f942d0553ff-source.png\",\"large\":\"/upload/image/201705/73e96e1e-2a89-46e9-a11b-5f942d0553ff-large.jpg\",\"medium\":\"/upload/image/201705/73e96e1e-2a89-46e9-a11b-5f942d0553ff-medium.jpg\",\"thumbnail\":\"/upload/image/201705/73e96e1e-2a89-46e9-a11b-5f942d0553ff-thumbnail.jpg\"},{\"source\":\"/upload/image/201705/bb23ff84-b8f9-4258-a7f8-f481057991a3-source.jpg\",\"large\":\"/upload/image/201705/bb23ff84-b8f9-4258-a7f8-f481057991a3-large.jpg\",\"medium\":\"/upload/image/201705/bb23ff84-b8f9-4258-a7f8-f481057991a3-medium.jpg\",\"thumbnail\":\"/upload/image/201705/bb23ff84-b8f9-4258-a7f8-f481057991a3-thumbnail.jpg\"}]","sales":0,"score":0.0,"score_count":0,"seo_description":null,"seo_keywords":null,"seo_title":null,"sn":"201705221111","specification_items":null,"total_score":0,"type":0,"unit":"套","version":8,"week_hits":0,"week_hits_date":"2017-05-22 14:19:21","week_sales":0,"week_sales_date":"2017-05-22 14:19:21","weight":1000}}}
	 */
	public void add() {
		Long id = getParaToLong("ids");
		OrderItem orderItem = orderItemService.find(id);
		
		Setting setting = SystemUtils.getSetting();
		if (!setting.getIsReviewEnabled()) {
			throw new ResourceNotFoundException();
		}
		if (orderItem == null) {
			throw new ResourceNotFoundException();
		}
		Goods goods = orderItem.getProduct().getGoods();
		ReviewAddResult reviewAddResult = new ReviewAddResult(orderItem, goods);
		renderJson(ApiResult.success(reviewAddResult));
	}
	
	
	/**
	 * 保存
	 *{"msg":"","code":1,"data":"您的商品评论已提交，正在等待审核"}
	 */
	@Before(Tx.class)
	public void save() {
		Logger logger = Logger.getLogger("订单保存:::::");
		String params = getPara("param");
		JSONArray arr = JSONArray.parseArray(params);
		Res resZh = I18n.use();
		String ip = getRequest().getRemoteAddr();
		Member member = memberService.getCurrent();
		for (int i = 0; i < arr.size(); i++) {
			JSONObject obj = arr.getJSONObject(i);
			Boolean anonymous = obj.getBoolean("anonymous");
			Long id = obj.getLong("ids");
			logger.info("订单项的id:             " + id);
			Long orderId = obj.getLong("orderId");
			logger.info("订单的id:             " + orderId);
			Integer score = obj.getInteger("scores");
			String content = obj.getString("contents");
			String[] images = StringUtils.split(obj.getString("image") ,",");
			if(StringUtils.isNotBlank(content)){
				OrderItem orderItem = orderItemService.find(id);

				Setting setting = SystemUtils.getSetting();
				if (!setting.getIsReviewEnabled()) {
					renderJson(ApiResult.fail(resZh.format("shop.review.disabled")));
					return;
				}
				if (orderItem == null) {
					renderJson(ApiResult.fail("评价商品不能为空!"));
					return;
				}
				if (!Setting.ReviewAuthority.anyone.equals(setting.getReviewAuthority()) && member == null) {
					renderJson(ApiResult.fail(resZh.format("shop.review.accessDenied")));
					return;
				}
				if (orderItem.getIsReview()) {
					renderJson(ApiResult.fail(resZh.format("shop.review.noPermission")));
					return;
				}
				orderItem.setIsReview(true);
				orderItem.update();

				Order order = orderService.find(orderId);
				if(order != null){
					logger.info("获取到了订单！！！！！！！！！！！");
					order.setStatus(Order.Status.completed.ordinal());
					orderService.update(order);
				}else {
					order = orderService.find(orderItem.getOrderId());
					if(order != null) {
						logger.info("没有获取到订单！！！！！！！！！！！");
						order.setStatus(Order.Status.completed.ordinal());
						orderService.update(order);
					}
				}
				reviewService.saveReview(member, ip, 1, anonymous, id, score, content, images);
			}else {
                renderJson(ApiResult.fail("系统错误,请稍后尝试"));
                return;
            }
		}
		renderJson(ApiResult.success(resZh.format("shop.review.success")));
	}
	
	/**
	 * 查看
	 * {"msg":"","code":1,"data":{"review":{"content":"\"eeeee\"","create_date":"2017-05-24 15:07:58","for_review_id":null,"goods_id":64,"id":31,"images":"null","ip":"127.0.0.1","is_show":false,"member_id":18,"modify_date":"2017-05-24 15:07:58","order_item_id":95,"product_id":86,"score":5,"version":0}}}
	 */
	public void view() {
		Long id = getParaToLong("ids");
		Review review = reviewService.find(id);

		ReviewFindResult reviewFindResult = new ReviewFindResult(review);
		renderJson(ApiResult.success(reviewFindResult));
	}
	
}
