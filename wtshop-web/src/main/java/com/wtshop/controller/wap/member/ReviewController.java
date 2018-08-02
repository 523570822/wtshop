package com.wtshop.controller.wap.member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.kit.StrKit;
import com.wtshop.api.common.result.ImageResult;
import com.wtshop.model.*;
import com.wtshop.service.*;
import com.wtshop.util.ApiResult;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.i18n.I18n;
import com.jfinal.i18n.Res;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.upload.UploadFile;
import com.wtshop.FileType;
import com.wtshop.Message;
import com.wtshop.Pageable;
import com.wtshop.Setting;
import com.wtshop.controller.wap.BaseController;
import com.wtshop.exception.ResourceNotFoundException;
import com.wtshop.interceptor.WapMemberInterceptor;
import com.wtshop.util.SystemUtils;

/**
 * Controller - 评论
 * 
 *
 */
@ControllerBind(controllerKey = "/wap/member/review")
@Before(WapMemberInterceptor.class)
public class ReviewController extends BaseController {

	private FileService fileService = enhance(FileService.class);
	private MemberService memberService =enhance(MemberService.class);
	private OrderItemService orderItemService = enhance(OrderItemService.class);
	private ReviewService reviewService = enhance(ReviewService.class);

	private GoodsService goodsService = enhance(GoodsService.class);

	private ProductCategoryService productCategoryService = enhance(ProductCategoryService.class);
	private BrandService brandService = enhance(BrandService.class);
	private PromotionService promotionService = enhance(PromotionService.class);
	private TagService tagService = enhance(TagService.class);

	private EffectService effectService = enhance(EffectService.class);

	 /**
	 * 上传
	 */
	public void upload() {
		UploadFile file = getFile();
		FileType fileType = FileType.valueOf(getPara("fileType", "image"));
		
		Map<String, Object> data = new HashMap<String, Object>();
		if (fileType == null || file == null || file.getFile().length() <= 0) {
			data.put(MESSAGE, "请选择选图片");
			data.put(STATUS, ERROR);
			renderJson(data);
			return;
		}
		if (!fileService.isValid(fileType, file)) {
			data.put(MESSAGE, Message.warn("admin.upload.invalid"));
			data.put(STATUS, ERROR);
			renderJson(data);
			return;
		}
		String url = fileService.upload(fileType, file, false);
		if (StringUtils.isEmpty(url)) {
			data.put(MESSAGE, Message.warn("admin.upload.error"));
			data.put(STATUS, ERROR);
			renderJson(data);
			return;
		}
		data.put(MESSAGE, "上传成功!");
		data.put(STATUS, SUCCESS);

		data.put("url", url);
		renderJson(data);
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
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(MESSAGE, "上传成功!");
		data.put(STATUS, SUCCESS);
		data.put("url", imageResult);
		renderJson(data);
	}
	
	/**
	 * 列表
	 */
	public void list() {
		Boolean isReview = getParaToBoolean("isReview", false);
		
		Pageable pageable = new Pageable();
		Member member = memberService.getCurrent();
		setAttr("orderItems", reviewService.findPendingOrderItems(member, isReview, pageable));
		setAttr("isReview", isReview);
		setAttr("title" , "待评价交易 - 会员中心");
		render("/wap/member/review/list.ftl");
	}
	
	/**
	 * 添加
	 */
	public void add() {
		Long id = getParaToLong("id");
	//	OrderItem orderItem = orderItemService.find(id);
		
		Setting setting = SystemUtils.getSetting();
		if (!setting.getIsReviewEnabled()) {
			throw new ResourceNotFoundException();
		}


		String typeName = getPara("type");
		Goods.Type type = StrKit.notBlank(typeName) ? Goods.Type.valueOf(typeName) : null;
		Long productCategoryId = getParaToLong("productCategoryId");
		Long brandId = getParaToLong("brandId");
		Long promotionId = getParaToLong("promotionId");
		Long tagId = getParaToLong("tagId");
		Boolean isMarketable = getParaToBoolean("isMarketable");
		Boolean isList = getParaToBoolean("isList");
		Boolean isTop = getParaToBoolean("isTop");
		Boolean isOutOfStock = getParaToBoolean("isOutOfStock");
		Boolean isStockAlert = getParaToBoolean("isStockAlert");
		Pageable pageable = getBean(Pageable.class);
		pageable.setPageSize(20);
		//pageable.setd
	//	pageable.DEFAULT_PAGE_SIZE
		Boolean isVip = getParaToBoolean("isVip");
		ProductCategory productCategory = productCategoryService.find(productCategoryId);
		Brand brand = brandService.find(brandId);
		Promotion promotion = promotionService.find(promotionId);
		Tag tag = tagService.find(tagId);
		setAttr("types", Goods.Type.values());
		setAttr("productCategoryTree", productCategoryService.findTree());
		setAttr("brands", brandService.findAll());
		setAttr("promotions", promotionService.findAll());
		setAttr("tags", tagService.findList(Tag.Type.goods));
		setAttr("effects", effectService.findAll());
		setAttr("type", type);
		setAttr("productCategoryId", productCategoryId);
		setAttr("brandId", brandId);
		setAttr("promotionId", promotionId);
		setAttr("tagId", tagId);
		setAttr("isMarketable", isMarketable);
		setAttr("isList", isList);
		setAttr("isTop", isTop);
		setAttr("isOutOfStock", isOutOfStock);
		setAttr("isStockAlert", isStockAlert);
		setAttr("isVip", isVip);
		setAttr("pageable", pageable);
		setAttr("page", goodsService.findPage(isVip, false, type, productCategory, brand, promotion, tag, null, null, null, isMarketable, isList, isTop, isOutOfStock, isStockAlert, null, null, pageable));




		setAttr("title" , "评价交易 - 会员中心");
		render("/wap/member/review/add.ftl");
	}
	
	
	/**
	 * 保存
	 */
	@Before(Tx.class)
	public void save() {
		Long id = getParaToLong("ids");
		Integer score = getParaToInt("score");
		String content = getPara("content");
		String [] images = getParaValues("images");
		String  imagesT = getPara("imagesT");
		String name = getPara("namee");
		
		//OrderItem orderItem = orderItemService.find(id);

		if(id==null||id==0){
			Map<String, String> map = new HashMap<String, String>();

			map.put("status", "0");
			map.put(MESSAGE,  "请选择评价商品");
			renderJson(map);
			return;
		}

		if(content==null||content.trim().equals("")){
			Map<String, String> map = new HashMap<String, String>();

			map.put("status", "0");
			map.put(MESSAGE,  "评价不能为空");
			renderJson(map);
			return;
		}

		Res resZh = I18n.use();
		Setting setting = SystemUtils.getSetting();
		Map<String, String> map = new HashMap<String, String>();
/*
		if (orderItem == null) {
			map.put(STATUS, ERROR);
			map.put(MESSAGE, "评价商品不能为空!");
			renderJson(map);
			return;
		}*/
		
		Member member = memberService.getCurrent();
		/*if (!Setting.ReviewAuthority.anyone.equals(setting.getReviewAuthority()) && member == null) {
			map.put(STATUS, ERROR);
			map.put(MESSAGE, resZh.format("shop.review.accessDenied"));
			renderJson(map);
			return;
		}*/
	/*	if (orderItem.getIsReview()) {
			map.put(STATUS, ERROR);
			map.put(MESSAGE, resZh.format("shop.review.noPermission"));
			renderJson(map);
			return;
		}
		orderItem.setIsReview(true);
		orderItem.update();
		*/
		Review review = new Review();

		//带加入的昵称

		if(name==null||name.trim().equals("")){
			map.put("status", "0");
			map.put(MESSAGE,  "昵称不能为空");
			renderJson(map);
		}else{
			review.setIsAnonymous(true);

			review.setName(name);
		}


		review.setScore(score);
		review.setContent(content);
		review.setImages(JSONArray.toJSONString(images));
		review.setIp(getRequest().getRemoteAddr());
		review.setMemberId(member.getId());
		review.setProductId(23757818l);
		review.setGoodsId(id);
		review.setOrderItemId(1l);
		review.setIsDelete(false);
		review.setIsShow(true);
		review.setIsAnonymous(true);
		review.setStatus(true);
		review.setAvatarUrl(imagesT);


			review.setIsShow(true);
			reviewService.save(review);
			map.put(STATUS, SUCCESS);
			map.put("status", "0");
			map.put(MESSAGE, resZh.format("shop.review.success"));
			renderJson(map);

	}
	
	/**
	 * 查看
	 */
	public void view() {
		Long id = getParaToLong("id");
		Review review = reviewService.find(id);
		
		setAttr("review", review);
		setAttr("title" , "评价详情 - 会员中心");
		render("/wap/member/review/view.ftl");
	}
	public void updataXL (){
		Long id = getParaToLong("id");
		Long num = getParaToLong("num");
		Map<String, Object> data = new HashMap<String, Object>();
		Goods pGoods = goodsService.find(id);
		pGoods.setSales(num);
		pGoods.update();
		data.put(MESSAGE, "上传成功!");
		data.put(STATUS, SUCCESS);
		renderJson(data);
	}
	
	
}
