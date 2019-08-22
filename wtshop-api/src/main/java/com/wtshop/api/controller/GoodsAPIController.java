package com.wtshop.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.taobao.api.internal.toplink.embedded.websocket.util.StringUtil;
import com.wtshop.Pageable;
import com.wtshop.RequestContextHolder;
import com.wtshop.Setting;
import com.wtshop.api.common.result.GoodsListResult;
import com.wtshop.api.common.result.GoodsMessageResult;
import com.wtshop.api.common.result.SearchResult;
import com.wtshop.api.interceptor.ErrorInterceptor;
import com.wtshop.entity.SpecificationItem;
import com.wtshop.exception.ResourceNotFoundException;
import com.wtshop.interceptor.WapInterceptor;
import com.wtshop.model.*;
import com.wtshop.service.*;
import com.wtshop.util.ApiResult;
import com.wtshop.util.RedisUtil;
import com.wtshop.util.ShareCodeUtils;
import com.wtshop.util.SystemUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Controller - 货品
 * 
 *
 */
@ControllerBind(controllerKey = "/api/goods")
@Before({WapInterceptor.class, ErrorInterceptor.class})
public class GoodsAPIController extends BaseAPIController {
	private MemberFavoriteGoodsService mfgGoodsService=enhance(MemberFavoriteGoodsService.class);
	private ProductCategoryService productCategoryService = enhance(ProductCategoryService.class);
	private ConsultationService consultationService = enhance(ConsultationService.class);
	private GoodsService goodsService = enhance(GoodsService.class);
	private MemberService memberService = enhance(MemberService.class);
	private SearchService searchService = enhance(SearchService.class);
	private ReviewService reviewService = enhance(ReviewService.class);
	private ProductService productService = enhance(ProductService.class);
	private FootPrintService footPrintService= enhance(FootPrintService.class);
	private BrandService brandService = enhance(BrandService.class);
	private EffectService effectService = enhance(EffectService.class);
	private AreaService areaService = enhance(AreaService.class);
	private ReceiverService receiverService = enhance(ReceiverService.class);
	private AreaDescribeService areaDescribeService = enhance(AreaDescribeService.class);
	private MiaobiLogService miaobiLogService = enhance(MiaobiLogService.class);
	private SpecialGoodsService specialGoodsService = enhance(SpecialGoodsService.class);
	private IdentifierService identifierService = enhance(IdentifierService.class);
	private FullReductionService fullReductionService =enhance(FullReductionService.class);
	private SpecialCouponService specialCouponService=enhance(SpecialCouponService.class);

	/**
	 * 列表
	 * {"msg":"","code":1,"data":{"orderType":"all","page":{"totalRow":2,"pageNumber":1,"firstPage":true,"lastPage":true,"totalPage":1,"pageSize":20,"list":[{"attribute_value0":null,"attribute_value1":null,"attribute_value10":null,"attribute_value11":null,"attribute_value12":null,"attribute_value13":null,"attribute_value14":null,"attribute_value15":null,"attribute_value16":null,"attribute_value17":null,"attribute_value18":null,"attribute_value19":null,"attribute_value2":null,"attribute_value3":null,"attribute_value4":null,"attribute_value5":null,"attribute_value6":null,"attribute_value7":null,"attribute_value8":null,"attribute_value9":null,"brand_id":null,"caption":"测试三","create_date":"2017-05-26 10:13:21","generate_method":1,"hits":0,"id":69,"image":"/upload/image/201705/3fb120a8-eb91-4662-9df6-ad64ec8f11ba.png","introduction":null,"is_delivery":true,"is_list":true,"is_marketable":true,"is_top":false,"keyword":null,"market_price":99.000000,"memo":null,"modify_date":"2017-05-26 10:13:21","month_hits":0,"month_hits_date":"2017-05-26 10:13:21","month_sales":0,"month_sales_date":"2017-05-26 10:13:21","name":"测试三","parameter_values":null,"price":77.000000,"product_category_id":245,"product_images":null,"sales":0,"score":0.0,"score_count":0,"seo_description":null,"seo_keywords":null,"seo_title":null,"sn":"201705261616","specification_items":null,"total_score":0,"type":0,"unit":"瓶","version":0,"week_hits":0,"week_hits_date":"2017-05-26 10:13:21","week_sales":0,"week_sales_date":"2017-05-26 10:13:21","weight":100},{"attribute_value0":null,"attribute_value1":null,"attribute_value10":null,"attribute_value11":null,"attribute_value12":null,"attribute_value13":null,"attribute_value14":null,"attribute_value15":null,"attribute_value16":null,"attribute_value17":null,"attribute_value18":null,"attribute_value19":null,"attribute_value2":null,"attribute_value3":null,"attribute_value4":null,"attribute_value5":null,"attribute_value6":null,"attribute_value7":null,"attribute_value8":null,"attribute_value9":null,"brand_id":50,"caption":"控油去油 长效保湿 调节水油平衡","create_date":"2017-05-22 14:19:21","generate_method":2,"hits":0,"id":64,"image":"/upload/image/201705/ba801bbb-37be-45e1-a576-e59df54753e5.jpg","introduction":"<p><img src=\"/upload/image/201705/e13f093e-0f6c-4771-88cb-be595666c409.png\"/></p>","is_delivery":true,"is_list":true,"is_marketable":true,"is_top":false,"keyword":null,"market_price":298.800000,"memo":null,"modify_date":"2017-05-22 17:50:27","month_hits":0,"month_hits_date":"2017-05-22 14:19:21","month_sales":0,"month_sales_date":"2017-05-22 14:19:21","name":"新品上市 欧莱雅男士洗面奶矿漠泥长效控油保湿洁面护肤品套装","parameter_values":"[{\"group\":\"产品参数\",\"entries\":[{\"name\":\"化妆品净含量\",\"value\":\"套装容量\"},{\"name\":\"产地\",\"value\":\"中国\"},{\"name\":\"功效\",\"value\":\"补水\"},{\"name\":\"规格类型\",\"value\":\"正常规格\"},{\"name\":\"化妆品保质期\",\"value\":\"3年\"},{\"name\":\"适合肤质\",\"value\":\"油性肤质\"}]}]","price":249.000000,"product_category_id":245,"product_images":"[{\"source\":\"/upload/image/201705/73e96e1e-2a89-46e9-a11b-5f942d0553ff-source.png\",\"large\":\"/upload/image/201705/73e96e1e-2a89-46e9-a11b-5f942d0553ff-large.jpg\",\"medium\":\"/upload/image/201705/73e96e1e-2a89-46e9-a11b-5f942d0553ff-medium.jpg\",\"thumbnail\":\"/upload/image/201705/73e96e1e-2a89-46e9-a11b-5f942d0553ff-thumbnail.jpg\"},{\"source\":\"/upload/image/201705/bb23ff84-b8f9-4258-a7f8-f481057991a3-source.jpg\",\"large\":\"/upload/image/201705/bb23ff84-b8f9-4258-a7f8-f481057991a3-large.jpg\",\"medium\":\"/upload/image/201705/bb23ff84-b8f9-4258-a7f8-f481057991a3-medium.jpg\",\"thumbnail\":\"/upload/image/201705/bb23ff84-b8f9-4258-a7f8-f481057991a3-thumbnail.jpg\"}]","sales":0,"score":0.0,"score_count":0,"seo_description":null,"seo_keywords":null,"seo_title":null,"sn":"201705221111","specification_items":null,"total_score":0,"type":0,"unit":"套","version":8,"week_hits":0,"week_hits_date":"2017-05-22 14:19:21","week_sales":0,"week_sales_date":"2017-05-22 14:19:21","weight":1000}]},"title":"洗面奶","productCategory":{"create_date":"2017-05-18 09:31:58","grade":1,"id":245,"image":"/upload/image/201705/358782ae-05fe-4a0d-96e2-0566e1f295ce.jpg","modify_date":"2017-05-23 13:40:05","name":"洗面奶","orders":1,"parent_id":243,"seo_description":null,"seo_keywords":null,"seo_title":null,"tree_path":",243,","version":2}}}
	 */
	public void list() {
		Long productCategoryId = getParaToLong("productCategoryIds");
		ProductCategory productCategory = productCategoryService.find(productCategoryId);
		if (productCategory == null) {
			throw new ResourceNotFoundException();
		}

		List<ProductCategory> thirdRoots = productCategoryService.findChildren(productCategoryId, false, null, false);
		List<Long> ProductCategorys = productCategoryService.getProductCategory(productCategoryId);

		String orderTypeName = getPara("orderTypes");
		Goods.OrderType orderType = StrKit.notBlank(orderTypeName) ? Goods.OrderType.valueOf(orderTypeName) : null;

		String startPriceStr = getPara("startPrices", null);
		BigDecimal startPrice = null;
		if (StrKit.notBlank(startPriceStr)) {
			startPrice = new BigDecimal(startPriceStr);
		}

		String endPriceStr = getPara("endPrices", null);
		BigDecimal endPrice = null;
		if (StrKit.notBlank(endPriceStr)) {
			endPrice = new BigDecimal(endPriceStr);
		}
		Integer pageNumber = getParaToInt("pageNumbers", 1);
		Integer pageSize = getParaToInt("pageSizes", 20);
		Pageable pageable = new Pageable(pageNumber, pageSize);
	    Page<Goods> page = goodsService.findPages(false ,false, null, ProductCategorys, null, null, null, null, startPrice, endPrice, true, true, null, null, null, null, orderType, pageable);
		String title = productCategory.getName();
		Object ob = new Object();
		if(orderType == null){
			ob = "all";
		}else {
			ob = orderType;
		}
		GoodsListResult goodsListResult = new GoodsListResult(title, productCategory, thirdRoots, ob, page);
		renderJson(ApiResult.success(goodsListResult));
	}

	/**
	 * 详情
	 * {"msg":"","code":1,"data":{"review":{"totalRow":0,"pageNumber":1,"firstPage":true,"lastPage":true,"totalPage":0,"pageSize":20,"list":[]},"consultationPages":{"totalRow":0,"pageNumber":1,"firstPage":true,"lastPage":true,"totalPage":0,"pageSize":20,"list":[]},"goods":{"attribute_value0":null,"attribute_value1":null,"attribute_value10":null,"attribute_value11":null,"attribute_value12":null,"attribute_value13":null,"attribute_value14":null,"attribute_value15":null,"attribute_value16":null,"attribute_value17":null,"attribute_value18":null,"attribute_value19":null,"attribute_value2":null,"attribute_value3":null,"attribute_value4":null,"attribute_value5":null,"attribute_value6":null,"attribute_value7":null,"attribute_value8":null,"attribute_value9":null,"brand_id":50,"caption":"控油去油 长效保湿 调节水油平衡","create_date":"2017-05-22 14:19:21","generate_method":1,"hits":0,"id":64,"image":"/upload/image/201705/ba801bbb-37be-45e1-a576-e59df54753e5.jpg","introduction":"<p><img src=\"/upload/image/201705/e13f093e-0f6c-4771-88cb-be595666c409.png\"/></p>","is_delivery":true,"is_list":true,"is_marketable":true,"is_top":false,"keyword":null,"market_price":298.800000,"memo":null,"modify_date":"2017-05-22 17:50:27","month_hits":0,"month_hits_date":"2017-05-22 14:19:21","month_sales":0,"month_sales_date":"2017-05-22 14:19:21","name":"新品上市 欧莱雅男士洗面奶矿漠泥长效控油保湿洁面护肤品套装","parameter_values":"[{\"group\":\"产品参数\",\"entries\":[{\"name\":\"化妆品净含量\",\"value\":\"套装容量\"},{\"name\":\"产地\",\"value\":\"中国\"},{\"name\":\"功效\",\"value\":\"补水\"},{\"name\":\"规格类型\",\"value\":\"正常规格\"},{\"name\":\"化妆品保质期\",\"value\":\"3年\"},{\"name\":\"适合肤质\",\"value\":\"油性肤质\"}]}]","price":249.000000,"product_category_id":245,"product_images":"[{\"source\":\"/upload/image/201705/73e96e1e-2a89-46e9-a11b-5f942d0553ff-source.png\",\"large\":\"/upload/image/201705/73e96e1e-2a89-46e9-a11b-5f942d0553ff-large.jpg\",\"medium\":\"/upload/image/201705/73e96e1e-2a89-46e9-a11b-5f942d0553ff-medium.jpg\",\"thumbnail\":\"/upload/image/201705/73e96e1e-2a89-46e9-a11b-5f942d0553ff-thumbnail.jpg\"},{\"source\":\"/upload/image/201705/bb23ff84-b8f9-4258-a7f8-f481057991a3-source.jpg\",\"large\":\"/upload/image/201705/bb23ff84-b8f9-4258-a7f8-f481057991a3-large.jpg\",\"medium\":\"/upload/image/201705/bb23ff84-b8f9-4258-a7f8-f481057991a3-medium.jpg\",\"thumbnail\":\"/upload/image/201705/bb23ff84-b8f9-4258-a7f8-f481057991a3-thumbnail.jpg\"}]","sales":0,"score":0.0,"score_count":0,"seo_description":null,"seo_keywords":null,"seo_title":null,"sn":"201705221111","specification_items":null,"total_score":0,"type":0,"unit":"套","version":8,"week_hits":0,"week_hits_date":"2017-05-22 14:19:21","week_sales":0,"week_sales_date":"2017-05-22 14:19:21","weight":1000},"title":"新品上市 欧莱雅男士洗面奶矿漠泥长效控油保湿洁面护肤品套装","favorite":false}}
	 * score 0 1 2   好评 中评 差评
	 */
	public void detail() throws ParseException {
		Long id = getParaToLong("goodIds");
		Long sPecialId = getParaToLong("sPecialIds",0l);
		if(sPecialId!=0){
			SpecialGoods ddd = specialGoodsService.find(sPecialId);
			if(ddd==null||ddd.getStatus()==1){
				renderJson(ApiResult.fail(60001,"商品已下架"));
				return;
			}

		}
		String type = getPara("type"); //是否喵币商品
		Goods goods = goodsService.find(id);

		Pageable pageable = new Pageable(1, 20);
		Boolean favorite = false;
		
		if (goods == null) {
			return;
		}
		List<Area> areas = areaService.findParents(goods.getArea(), true, null);
		goods.setAttributeValue0(areas.get(0).getName());
		
		RequestContextHolder.setRequestAttributes(getRequest());
		Member m=memberService.getCurrent();
		MemberFavoriteGoods ddd = mfgGoodsService.findGoods(m.getId(), goods.getId(), sPecialId);
		if (ddd!=null) {
			favorite = true;
		}
		Page<Consultation> consultationPages = consultationService.findPage(null, goods, true, pageable);
		Page<Review> reviewPages = reviewService.findPageList(null, goods, null, true, pageable);
		List<Review> list = reviewPages.getList();
        for(Review review:list){
			Date dd = review.getModifyDate();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String s = sdf.format(dd);
			Date date =  sdf.parse(s);
			review.setModifyDate(date);
			Member member = memberService.find(review.getMemberId());
			String nickname = member.getNickname();
			if(review.getIsAnonymous() != null && review.getIsAnonymous()){
				if(StringUtils.isNotBlank(nickname)){
					String first = nickname.substring(0,1);
					String end = nickname.substring(nickname.length()-1);
					review.setOrderContent(first+"**"+end);
				}else {
					review.setOrderContent("**");
				}
			}else {
				review.setOrderContent(nickname);
			}
        }
		Long reviewCount = reviewService.count(null, goods, null, true);//全部评论
		Long positiveCount = reviewService.count(null, goods, Review.Type.positive, true);
		Long moderateCount = reviewService.count(null, goods, Review.Type.moderate, true);
		Long negativeCount = reviewService.count(null, goods, Review.Type.negative, true);
		Long imagescount = 0L;
		List<Review> reviews = reviewService.count(null, goods, true);
		for(Review review : reviews){
			String images = review.getImages();
			if(images.contains("/")){
				imagescount += 1;
			}
		}
		List<Product> productList = productService.findProductList(id);
		String name = goods.getName();
		List<Tag> tags = goodsService.finTagList(goods.getId());



		//购物须知&正品保障&关税
		Setting setting = SystemUtils.getSetting();
		String settingShoppingCopyUrl = setting.getShoppingCopyUrl();
		String certifiedCopyUrl = setting.getCertifiedCopyUrl();
		String taxExplainUrl = setting.getTaxExplainUrl();
		Product byGoodsId = productService.findByGoodsId(goods.getId());
		Integer stock = byGoodsId.getStock();


		//插入会员足迹
		if (m !=null){
			boolean isHas = footPrintService.findByTime(goods.getId(),m.getId());
			if(!isHas){
				Footprint footprint=new  Footprint();
				footprint.setGoodsId(goods.getId());
				footprint.setMemberId(m.getId());
				footPrintService.save(footprint);
			}
		}
		//收货地址
		Receiver aDefault = receiverService.findDefault(m);

		//商品配送
		String receiveTime = null;
		if(aDefault != null){
			AreaDescribe areaDescribe = areaDescribeService.findByAreaId(aDefault.getAreaId());
			//判断本级地区是否填写
			if(areaDescribe != null && areaDescribe.getReceivingBegintime() != null){
				receiveTime = areaDescribe.getReceivingBegintime();
			}else {
				AreaDescribe areaDescribes = areaDescribeService.findByAreaId(areaService.find(aDefault.getAreaId()).getParentId());
				if(areaDescribes !=null){
					receiveTime = areaDescribes.getReceivingBegintime();
				}

			}
		}
	String 	freeMoney=RedisUtil.getString("freeMoney");
		String freMon;
		if(freeMoney==null||freeMoney.trim().equals("")||freeMoney.trim().equals("null")||freeMoney.trim().equals("0")){
			freMon=	"包邮";
		}else{
			freMon="订单满"+freeMoney+"元包邮";
		}


		GoodsMessageResult goodsMessageResult = new GoodsMessageResult(stock,goods,name, favorite, consultationPages, reviewPages, reviewCount,positiveCount,moderateCount,negativeCount,imagescount,tags,productList,settingShoppingCopyUrl,certifiedCopyUrl,taxExplainUrl,aDefault,receiveTime,freMon);
		renderJson(ApiResult.success(goodsMessageResult));


	}

	/**
	 * 评论及晒图
	 */

	public void reviewDetail() throws ParseException {
		Long id = getParaToLong("goodIds");
		Long type = getParaToLong("type");
		Integer pageNumber = getParaToInt("pageNumber",1);
		Goods goods = goodsService.find(id);
		Pageable pageable = new Pageable(pageNumber, 10);
		if( type == null){
			Page<Review> reviewPages = reviewService.findPage(null, goods, null, true, pageable);
			List<Review> list = reviewPages.getList();
			for(Review review:list){
				Date dd = review.getModifyDate();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String s = sdf.format(dd);
				Date date =  sdf.parse(s);
				review.setModifyDate(date);
				Member member = review.getMember();
				String nickname = member.getNickname();
				if(review.getIsAnonymous() != null && review.getIsAnonymous()){
					if(nickname != null){
						String first = nickname.substring(0,1);
						String end = nickname.substring(nickname.length()-1);
						review.setOrderContent(first+"**"+end);
					}else{
						review.setOrderContent("**");
					}
				}else {
					review.setOrderContent(nickname);
				}
			}
			renderJson(ApiResult.success(reviewPages));
		}
		else if (0 == type){
			Page<Review> reviewPages = reviewService.findPage(null, goods, Review.Type.positive, true, pageable);
			List<Review> list = reviewPages.getList();
			for(Review review:list){
				Date dd = review.getModifyDate();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String s = sdf.format(dd);
				Date date =  sdf.parse(s);
				review.setModifyDate(date);
				Member member = review.getMember();
				String nickname = member.getNickname();
				if(review.getIsAnonymous() != null && review.getIsAnonymous()){
					if(nickname != null){
						String first = nickname.substring(0,1);
						String end = nickname.substring(nickname.length()-1);
						review.setOrderContent(first+"**"+end);
					}else{
						review.setOrderContent("**");
					}
				}else {
					review.setOrderContent(nickname);
				}
			}
			renderJson(ApiResult.success(reviewPages));
		}else if (1 == type){
			Page<Review> reviewPages = reviewService.findPage(null, goods, Review.Type.moderate, true, pageable);
			List<Review> list = reviewPages.getList();
			for(Review review:list){
				Date dd = review.getModifyDate();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String s = sdf.format(dd);
				Date date =  sdf.parse(s);
				review.setModifyDate(date);
				Member member = review.getMember();
				String nickname = member.getNickname();
				if(review.getIsAnonymous() != null && review.getIsAnonymous()){
					if(nickname != null){
						String first = nickname.substring(0,1);
						String end = nickname.substring(nickname.length()-1);
						review.setOrderContent(first+"**"+end);
					}else{
						review.setOrderContent("**");
					}
				}else {
					review.setOrderContent(nickname);
				}
			}
			renderJson(ApiResult.success(reviewPages));
		}else if (2 == type){
			Page<Review> reviewPages = reviewService.findPage(null, goods, Review.Type.negative, true, pageable);
			List<Review> list = reviewPages.getList();
			for(Review review:list){
				Member member = review.getMember();
				Date dd = review.getModifyDate();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String s = sdf.format(dd);
				Date date =  sdf.parse(s);
				review.setModifyDate(date);
				String nickname = member.getNickname();
				if(review.getIsAnonymous() != null && review.getIsAnonymous()){
					if(nickname != null){
						String first = nickname.substring(0,1);
						String end = nickname.substring(nickname.length()-1);
						review.setOrderContent(first+"**"+end);
					}else{
						review.setOrderContent("**");
					}
				}else {
					review.setOrderContent(nickname);
				}
			}
			renderJson(ApiResult.success(reviewPages));
		}else if( 3 == type){
			Page<Review> imagePage = reviewService.findPage(null, goods, true, pageable);
			List<Review> list = imagePage.getList();
			for(Review review :list){
				Date dd = review.getModifyDate();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String s = sdf.format(dd);
				Date date =  sdf.parse(s);
				review.setModifyDate(date);
				Member member = review.getMember();
				String nickname = member.getNickname();
				if(review.getIsAnonymous() != null && review.getIsAnonymous()){
					if(nickname != null){
						String first = nickname.substring(0,1);
						String end = nickname.substring(nickname.length()-1);
						review.setOrderContent(first+"**"+end);
					}else{
						review.setOrderContent("**");
					}
				}else {
					review.setOrderContent(nickname);
				}
			}

			renderJson(ApiResult.success(imagePage));
		}
		else {
			renderJson(ApiResult.fail("系统内部错误"));
		}

	}

	/**
	 * 到货通知
	 */
	public void goodsNotofy(){
		Long goodsId = getParaToLong("goodsId");
		Member member = memberService.getCurrent();
		ProductNotify productNotify = new ProductNotify();
		productNotify.setMemberId(member.getId());
		productNotify.setGoodsId(goodsId);
		productNotify.setHasSent(false);
		productNotify.save();
		renderJson(ApiResult.success("发送成功!"));
	}


	/**
	 * 搜索列表
	 */

	public void searchList(){
		JSONObject zonghe = new JSONObject();
		zonghe.put("综合排序","modifyDate");
		zonghe.put("销量由高到低","sell");
		zonghe.put("评论数由高到低","review");

		JSONObject price = new JSONObject();
		price.put("0","priceUp");
		price.put("1","priceDown");

		List<Brand> brandList = brandService.findBrandList();
		Map<String, List> brandSort = brandService.findBrandSort();

		List<Effect> effectList = effectService.findEffectList();
		Map<String, List> effectSort = effectService.findEffectSort();

		List<Area> areaList = areaService.findRoots();



		SearchResult searchResult = new SearchResult(zonghe, price, brandList, effectList, areaList, brandSort ,effectSort);
		renderJson(ApiResult.success(searchResult));


	}

	/**
	 * 搜索
	 */
	public void search(){

		Integer pageNumber = getParaToInt("pageNumber");
		Integer pageSize = getParaToInt("pageSizes", 999);
		Pageable pageable = new Pageable(pageNumber, pageSize);
		Long productCategoryId =null;
			if("null".equals(getPara("productCategoryId"))){
				productCategoryId=123123l;
			}
		 productCategoryId = getParaToLong("productCategoryId");

		//关键字
		String keyword = getPara("keyword");

		//最低价格
		String startPriceStr = getPara("startPrice");
		BigDecimal startPrice = null;
		if (StrKit.notBlank(startPriceStr)) {
			startPrice = new BigDecimal(startPriceStr);
		}

		//最高价格
		String endPriceStr = getPara("endPrice");
		BigDecimal endPrice = null;
		if (StrKit.notBlank(endPriceStr)) {
			endPrice = new BigDecimal(endPriceStr);
		}

		//品牌
		String[] brandIds = StringUtils.split(getPara("brandId"), ",");
		Long[] brandList = brandIds == null ? null :convertToLong(brandIds);

		//产地

		String[] areaIds = StringUtils.split(getPara("areaId"), ",");
		Long[] areaId = areaIds == null ? null :convertToLong(areaIds);


		//功效管理
		String[] effectIds = StringUtils.split(getPara("effectId"), ",");
		Long[] effectIdList = effectIds == null ? null :convertToLong(effectIds);

		//排序字段 综合 销量 评论 价格
		Boolean modifyDate = getParaToBoolean("modifyDate" ,false);
		Boolean sell = getParaToBoolean("sell",false);
		Boolean review = getParaToBoolean("review",false);
		Boolean priceUp = getParaToBoolean("priceUp",false);
		Boolean priceDown = getParaToBoolean("priceDown", false);

		Page<Goods> search = searchService.search(false, productCategoryId, brandList, areaId, effectIdList, keyword, startPrice, endPrice, pageable, modifyDate, sell, review, priceUp ,priceDown);
		renderJson(ApiResult.success(search));
	}
/**
 * 填写邀请码
 */
public void onShareCode(){
	String onShareCode = getPara("onShareCode","");
	String str = new String(onShareCode);
	onShareCode=str.toUpperCase();
	Member m=memberService.getCurrent();
	List<Member> me = memberService.findByShareCode(onShareCode);
	if((StringUtils.isNotEmpty(onShareCode)&&(me==null||me.size()==0))&&(!"VA3TYG".equals(onShareCode))){
		renderJson(ApiResult.fail("邀请码不存在!"));
		return;
	}

	List<Identifier> ddd = identifierService.findByOnCodeShare(onShareCode, m.getId(),"1");
	if(ddd!=null&&ddd.size()>0){
		renderJson(ApiResult.fail("门店使用中"));
		return;
	}
	if(m.getOnShareCode()==null||"null".equals(m.getOnShareCode())||"".equals(m.getOnShareCode().trim())){

	}else{
		renderJson(ApiResult.fail("不能重复绑定"));
		return;
	}
	Map<String,Object>  map=  new HashMap<>();
	//map.put("shareCode","");
		//判断上级及上上及是否有特殊人员
		Boolean bool1 = memberService.findSpByPhone(me.get(0).getPhone());
	String shareCode = ShareCodeUtils.idToCode(m.getId());
		if(bool1){
			map.put("shareCode",shareCode);
			m.setShareCode(shareCode);
			m.setHousekeeperId(2l);
		}else{
			List<Member> member2 =memberService.findByShareCode(me.get(0).getOnShareCode());
			Boolean bool2 =false;
			if(member2!=null&&member2.get(0).getPhone()!=null){
				bool2= memberService.findSpByPhone(member2.get(0).getPhone());
			}

			if(bool2){
				map.put("shareCode",shareCode);
				m.setShareCode(shareCode);
				m.setHousekeeperId(2l);
			}
		}

		m.setOnShareCode(onShareCode);


	JSONObject redisSetting = JSONObject.parseObject(RedisUtil.getString("redisSetting"));

	double sendMiaoBi=0;
	sendMiaoBi = redisSetting.getDouble("registerSending") ;//邀请码赠送喵币
	String linkShareCode = me.get(0).getLinkShareCode() + "_" + me.get(0).getShareCode();
	m.setLinkShareCode(linkShareCode);
	m.setOnShareCode(onShareCode);
	MiaobiLog miaobiLog = new MiaobiLog();
	miaobiLog.setMemberId(m.getId());
	miaobiLog.setCredit(BigDecimal.valueOf(sendMiaoBi));
	miaobiLog.setDebit(BigDecimal.ZERO);
	miaobiLog.setType(0);
	miaobiLog.setMemo("绑定邀请码赠送");
	miaobiLog.setBalance(m.getPoint().add(BigDecimal.valueOf(sendMiaoBi)).setScale(2, BigDecimal.ROUND_HALF_UP));
	//更新用户喵币
	m.setPoint(m.getPoint().add(BigDecimal.valueOf(sendMiaoBi)).setScale(2, BigDecimal.ROUND_HALF_UP));
	miaobiLogService.save(miaobiLog);
	memberService.update(m);
	//更新用户喵币
	m.setPoint(m.getPoint().add(BigDecimal.valueOf(sendMiaoBi)).setScale(2, BigDecimal.ROUND_HALF_UP));
	map.put("sendMiaobi",sendMiaoBi);
	map.put("title","恭喜成为VIP");
	map.put("type",1);
	map.put("miaobilId",0);
	renderJson(ApiResult.success(map,"绑定邀请码成功"));
}

	/**
	 * 绑定门店
	 * 填写onShareCode邀请码和idfCode识别码
	 */
	public void bindingStore(){
		String onShareCode = getPara("onShareCode","").toUpperCase();
		String idfCode = getPara("idfCode","").toUpperCase();
		Long fullReId = getParaToLong("fullReId");

		FullReduction ss = fullReductionService.find(fullReId);
		Member m=memberService.getCurrent();
		List<Member> me = memberService.findByShareCode(onShareCode);
		if((StringUtils.isNotEmpty(onShareCode)&&(me==null||me.size()==0))&&(!"VA3TYG".equals(onShareCode))){
			renderJson(ApiResult.fail("邀请码不存在!"));
			return;
		}
		if(ss==null){
			renderJson(ApiResult.fail("请选择满返规则"));
			return;
		}


		List<Identifier>	identifierL=identifierService.findByIdfCode(idfCode);
		Identifier identifier=new Identifier() ;
		if(identifierL.size()>0){
			if(identifierL.get(0).getStatus()!=0){
				renderJson(ApiResult.fail("识别码已失效!"));
				return;
			}else{
				identifier=identifierL.get(0);
			}

		}else{
			renderJson(ApiResult.fail("识别码不存在!"));
			return;
		}
		if(me.get(0).getStore()==null||"".equals(me.get(0).getStore().trim())){
			renderJson(ApiResult.fail("邀请码与识别码不匹配!"));
			return;
		}
		List<Identifier> ddd = identifierService.findByOnCodeShare(onShareCode, m.getId(),"1");
		if(ddd!=null&&ddd.size()>0){
			renderJson(ApiResult.fail("门店使用中"));
			return;
		}
		Map<String,Object>  map=  new HashMap<>();

		if(m.getOnShareCode()==null||"".equals(m.getOnShareCode().trim())){

			Boolean bool1 = memberService.findSpByPhone(me.get(0).getPhone());
			String shareCode = ShareCodeUtils.idToCode(m.getId());
			if(bool1){
				map.put("shareCode",shareCode);
				m.setShareCode(shareCode);
				m.setHousekeeperId(2l);
			}else{
				List<Member> member2 =memberService.findByShareCode(me.get(0).getOnShareCode());
				Boolean bool2 = memberService.findSpByPhone(member2.get(0).getPhone());
				if(bool2){
					map.put("shareCode",shareCode);
					m.setShareCode(shareCode);
					m.setHousekeeperId(2l);
				}
			}


		//	String shareCode = ShareCodeUtils.idToCode(m.getId());
		//	map.put("shareCode",shareCode);
		//	m.setShareCode(shareCode);
		//	m.setHousekeeperId(2l);
			m.setOnShareCode(onShareCode);
			String linkShareCode = me.get(0).getLinkShareCode() + "_" + me.get(0).getShareCode();
			m.setLinkShareCode(linkShareCode);
		}
		identifier.setStatus(1);
		identifier.setMemberId(m.getId());
		identifier.setShareCode(onShareCode);
		identifier.setMoney(ss.getMoney());
		identifier.setTotalMoney(ss.getTotalMoney());


		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 60);

		Date date = cal.getTime();

		Date date1 =new Date();
		identifier.setEndDate(date);
		identifier.setStartDate(date1);
		memberService.update(m);
		identifierService.update(identifier);

		map.put("title","恭喜绑定 成功");
		map.put("type",1);
		renderJson(ApiResult.success(map,"绑定成功"));
	}

	/**
	 * 绑定代金卡
	 * idfCode
	 */
	public void bindingSpecialCoupon(){

		String idSCCode = getPara("idSCCode","").toUpperCase();

		Member m=memberService.getCurrent();


		List<SpecialCoupon>	specialCouponList=specialCouponService.findByIdfCode(idSCCode);
		SpecialCoupon specialCoupon=new SpecialCoupon() ;
		if(specialCouponList!=null&&specialCouponList.size()>0){
			if(specialCouponList.get(0).getStatus()!=0){
				renderJson(ApiResult.fail("识别码已失效!"));
				return;
			}else{
				specialCoupon=specialCouponList.get(0);
			}

		}else{
			renderJson(ApiResult.fail("识别码不存在!"));
			return;
		}
		Map<String,Object>  map=  new HashMap<>();


		specialCoupon.setStatus(1);
		specialCoupon.setMemberId(m.getId());
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 60);

		Date date = cal.getTime();

		Date date1 =new Date();
		specialCoupon.setEndDate(date);
		specialCoupon.setStartDate(date1);
		memberService.update(m);
		specialCouponService.update(specialCoupon);

		map.put("title","恭喜绑定 成功");
		map.put("type",1);
		renderJson(ApiResult.success(map,"绑定成功"));
	}
	/**
	 * 绑定门店
	 * 验证识别码邀请码接口
	 * 填写onShareCode邀请码和idfCode识别码
	 */
	public void bindingStoreY(){
		String onShareCode = getPara("onShareCode","").toUpperCase().trim();
		String idfCode = getPara("idfCode","").toUpperCase().trim();
		Long fullReId = getParaToLong("fullReId");

		FullReduction ss = fullReductionService.find(fullReId);
		Member m=memberService.getCurrent();
		List<Member> me = memberService.findByShareCode(onShareCode);
		if((StringUtils.isNotEmpty(onShareCode)&&(me==null||me.size()==0))&&(!"VA3TYG".equals(onShareCode))){
			renderJson(ApiResult.fail("邀请码不存在!"));
			return;
		}
		List<Identifier>	identifierL=identifierService.findByIdfCode(idfCode);
		Identifier identifier=new Identifier() ;
		if(identifierL.size()>0){
			if(identifierL.get(0).getStatus()!=0){
				renderJson(ApiResult.fail("识别码已失效!"));
				return;
			}else{
				identifier=identifierL.get(0);
			}

		}else{
			renderJson(ApiResult.fail("识别码不存在!"));
			return;
		}
		List<Identifier> ddd = identifierService.findByOnCodeShare(onShareCode, m.getId(),"1");
		if(ddd!=null&&ddd.size()>0){
			renderJson(ApiResult.fail("门店使用中"));
			return;
		}
		if(me.get(0).getStore()==null||"".equals(me.get(0).getStore().trim())){
			renderJson(ApiResult.fail("邀请码与识别码不匹配!"));
			return;
		}

		Map<String,Object>  map=  new HashMap<>();
		renderJson(ApiResult.success(map,"验证成功"));
	}


	/**
	 * 门店列表接口
	 */

	public void storeList() {
		Member m=memberService.getCurrent();
		Member x=new Member();
		if(m.getOnShareCode()!=null){
			x=memberService.findByShareCode(m.getOnShareCode()).get(0);
		}
		List<Identifier>identifierLL=new LinkedList<Identifier>();
		List<Identifier>identifierL=identifierService.findByMemberId(m.getId());
		if(x.getIsStore()!=null&&x.getIsStore()){
			List<Identifier> identifierList = identifierService.findByOnCodeShare(m.getOnShareCode(),m.getId(),null);


			if(identifierList.size()==0){
				Identifier identifier=new Identifier();
				identifier.setStatus(4);
				identifier.put("store",x.getStore());
				identifier.setShareCode(x.getShareCode());
				identifierLL.add(identifier);

			}

		}
		identifierLL.addAll(identifierL);
		renderJson(ApiResult.success(identifierLL));
	}

	/**
	 * 门店列表接口
	 */

	public void storeSpecialCouponList() {
		Member m=memberService.getCurrent();
		List<SpecialCoupon>identifierL=specialCouponService.findByMemberId(m.getId());
		renderJson(ApiResult.success(identifierL));
	}
	/**
	 * 续约记录门店列表接口
	 */

	public void storeRecordingList() {
		String onShareCode = getPara("onShareCode","").toUpperCase();
		Member m=memberService.getCurrent();
		List<Identifier>identifierL=identifierService.findByOnCodeShareSB(onShareCode,m.getId());
		renderJson(ApiResult.success(identifierL));
	}
	/**
	 * 获取规格
	 */
	public void specifs() {
	//	Map<String,Object>  map=  new HashMap<>();

		//map.put("Specification", specificationService.findByCategoryId(categoryId));

		Long goodId = getParaToLong("goodId");
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		Goods goods = goodsService.find(goodId);
		if (goods == null || CollectionUtils.isEmpty(goods.getSpecificationItemsConverter())) {
			renderJson(ApiResult.success("无数据"));
		return;
	}
		List<SpecificationItem> dddd = goods.getSpecificationItemsConverter();
		renderJson(ApiResult.success(dddd));
}






	/**
	 * 获取规格
	 */
	public void productList() {
		String  categoryValue = getPara("spvalue");
		Long  goodId = getParaToLong("goodId");
		List<Product> productList=productService.findBySpvalue(categoryValue,goodId);
		renderJson(ApiResult.success(productList));

	}


}
