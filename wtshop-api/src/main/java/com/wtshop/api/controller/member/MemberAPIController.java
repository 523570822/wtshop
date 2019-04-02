package com.wtshop.api.controller.member;

import com.alibaba.common.logging.Logger;
import com.alibaba.common.logging.LoggerFactory;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import com.wtshop.FileType;
import com.wtshop.Message;
import com.wtshop.Pageable;
import com.wtshop.Setting;
import com.wtshop.api.common.result.PointResult;
import com.wtshop.api.common.result.member.CountResult;
import com.wtshop.api.common.result.member.MemberMessageResult;
import com.wtshop.util.*;
import com.wtshop.api.controller.BaseAPIController;
import com.wtshop.api.interceptor.ErrorInterceptor;
import com.wtshop.interceptor.WapInterceptor;
import com.wtshop.model.*;
import com.wtshop.service.*;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@ControllerBind(controllerKey = "/api/member")
@Before({WapInterceptor.class, ErrorInterceptor.class})
public class MemberAPIController extends BaseAPIController {

	private Logger log = LoggerFactory.getLogger("MemberAPIController");
	/** 每页记录数 */
	private static final int PAGE_SIZE = 10;
	/** 最新订单数 */
	private static final int NEW_ORDER_COUNT = 6;
	private OrderService orderService = enhance(OrderService.class);
	private FileService fileService = enhance(FileService.class);
	private FeedbackService feedbackService = enhance(FeedbackService.class);
	private InterestCategoryService interestCategoryService = enhance(InterestCategoryService.class);
	private SkinTypeService skinTypeService = enhance(SkinTypeService.class);
	private CertificatesService certificatesService = enhance(CertificatesService.class);
	private ReceiverService receiverService = enhance(ReceiverService.class);
	private MemberService memberService = enhance(MemberService.class);
	private MiaobiLogService miaobiLogService = enhance(MiaobiLogService.class);
	private MrmfShopService mrmfShopService = enhance(MrmfShopService.class);
	private DepositLogService depositLogService = enhance(DepositLogService.class);


	/**
	 * 新用户赠送喵币
	 */
	public void send(){
		JSONObject redisSetting = JSONObject.parseObject(RedisUtil.getString("redisSetting"));
		Setting setting = SystemUtils.getSetting();
		Member member = memberService.getCurrent();
		MiaobiLog miaobiLog = miaobiLogService.findLogByMemberId(member.getId());
		boolean isRegisterSending= redisSetting.getBoolean("isRegisterSending") ? true : false;
		if(isRegisterSending && miaobiLog == null ){
			Double registerSending = redisSetting.getDouble("registerSending");
			member.setPoint(new BigDecimal(registerSending));
			memberService.update(member);
			String price = MathUtil.getInt(registerSending.toString());
			String desc = " 首次注册赠送您" + price +"喵币";
			String sendMiaoBiImg = setting.getSendMiaoBiImg();

			MiaobiLog pointLog = new MiaobiLog();
			pointLog.setType(2);
			pointLog.setCredit(new BigDecimal(registerSending));
			pointLog.setDebit(BigDecimal.ZERO);
			pointLog.setBalance(member.getPoint());
			pointLog.setMemo("注册赠送");
			pointLog.setMemberId(member.getId());
			miaobiLogService.save(pointLog);

			PointResult pointResult = new PointResult(desc, sendMiaoBiImg, price);

			renderJson(ApiResult.success(pointResult));
			return;

		}else {
			renderJson(ApiResult.fail("赠送失败"));
		}

	}

	/**
	 * 订单数量接口
	 */
	public void count(){
		Member member = memberService.getCurrent();
		Long memberPendingPaymentOrderCount = orderService.count(null, Order.Status.pendingPayment, member, null, null, null, null, null, null, false);
		Long memberPendingShipmentOrderCount = orderService.count(null, Order.Status.pendingShipment, member, null, null, null, null, null, null, false);
		Long memberReceivedOrderCount = orderService.count(null, Order.Status.shipped, member, null, null, null, null, null, null, false);
		Long pendingReviewCount = orderService.count(null, Order.Status.noReview, member, null, null, null, null, null, null, false);

		CountResult countResult = new CountResult(memberPendingPaymentOrderCount, memberPendingShipmentOrderCount, memberReceivedOrderCount, pendingReviewCount);
		renderJson(ApiResult.success(countResult));
	}



	/**
	 * 首页
	 * {"msg":"","code":1,"data":{"memberReceivedOrderCount":0,"pendingReviewCount":0,"member":{"address":null,"amount":0E-12,"area_id":null,"attribute_value0":null,"attribute_value1":null,"attribute_value2":null,"attribute_value3":null,"attribute_value4":null,"attribute_value5":null,"attribute_value6":null,"attribute_value7":null,"attribute_value8":null,"attribute_value9":null,"avatar":null,"balance":0E-12,"birth":null,"create_date":"2017-05-09 15:40:29","email":"sq@163.com","gender":null,"id":18,"is_enabled":true,"is_locked":false,"lock_key":"b16f3025929c413e27fb4fd39a4f46ee","locked_date":null,"login_date":"2017-05-24 14:12:32","login_failure_count":0,"login_ip":"127.0.0.1","login_plugin_id":null,"member_rank_id":1,"mobile":null,"modify_date":"2017-05-24 14:12:32","name":null,"nickname":"sq","open_id":null,"password":"e10adc3949ba59abbe56e057f20f883e","phone":null,"point":0,"register_ip":"127.0.0.1","safe_key_expire":null,"safe_key_value":null,"username":"sq123","version":3,"zip_code":null},"memberPendingShipmentOrderCount":0,"memberPendingPaymentOrderCount":0}}
	 */
	public void index() {


		Member member = memberService.getCurrent();
		Map memberType = memberService.queryUser(member);
		Long memberPendingPaymentOrderCount = orderService.count(null, Order.Status.pendingPayment, member, null, null, null, null, null, null, false);
		Long memberPendingShipmentOrderCount = orderService.count(null, Order.Status.pendingShipment, member, null, null, null, null, null, null, false);
		Long memberReceivedOrderCount = orderService.count(null, Order.Status.received, member, null, null, null, null, null, null, false);
		Long pendingReviewCount = orderService.count(null, Order.Status.noReview, member, null, null, null, null, null, null, false);
		String birth = "";
		if(member != null && member.getBirth() != null){
			birth = DateUtils.format(member.getBirth());
		}
		member.setDirectOffline(memberService.findMemberByOnShare(member.getShareCode()).size());
		//总下线
		member.setTotalOffline(memberService.findMemberByLinkShare(member.getShareCode()).size());

		if(StringUtils.isNotEmpty(member.getOnShareCode())){

			Member member1 =memberService.find(	ShareCodeUtils.codeToId(member.getOnShareCode()));

			member.setAttributeValue0(member1.getWeChatQcode());
			member.setAttributeValue1(member1.getWeChatNumber());
		}
		String phone = member.getPhone();
		Integer gender = member.getGender();
		String sign = member.getSign();
		Certificates certificates = certificatesService.queryByMemberId(member.getId());
		Receiver aDefault = receiverService.findDefault(member);
		List<Member> interest = memberService.findMySelfInterest(member.getId());
		List<InterestCategory> interestCategories = interestCategoryService.findAll();
		List<Member> skin = memberService.findMySelfSkin(member.getId());
		List<SkinType> skinTypes = skinTypeService.findAll();
        Double commission = 0d;
        Double prestore = 0d;

		//预存款和分佣金额
/*		MrmfShop mrmfShop = mrmfShopService.findMrmfShop(member);
		if(mrmfShop != null){
			commission = mrmfShop.getCommission().doubleValue();
			prestore = mrmfShop.getPrestore().doubleValue();
		}*/

		Record rechange = depositLogService.findRechange(member.getId());
		String price = "0";
		if(rechange != null && rechange.get("price") != null){
			price = rechange.get("price").toString();
		}



		MemberMessageResult memberMessageResult = null;
		if(certificates != null){
			memberMessageResult = new MemberMessageResult(memberPendingPaymentOrderCount, memberPendingShipmentOrderCount, memberReceivedOrderCount, pendingReviewCount, member,certificates,sign,birth, phone, gender, aDefault, interest, interestCategories, skin, skinTypes ,memberType, commission, prestore, MathUtil.getInt(price));

		}else{
			memberMessageResult = new MemberMessageResult(memberPendingPaymentOrderCount, memberPendingShipmentOrderCount, memberReceivedOrderCount, pendingReviewCount, member,certificates,sign,birth, phone, gender, aDefault, interest, interestCategories, skin, skinTypes,memberType, commission, prestore, MathUtil.getInt(price));
		}
		renderJson(ApiResult.success(memberMessageResult));

	}

	/**
	 * 佣金首页
	 */
	public void commissionIndex() {
		Member member = memberService.getCurrent();

		//直系下线

		member.setDirectOffline(memberService.findMemberByOnShare(member.getShareCode()).size());
		//总下线
		member.setTotalOffline(memberService.findMemberByLinkShare(member.getShareCode()).size());
	/*	Map<String, Object> item = new HashMap<String, Object>();*/
		if(StringUtils.isNotEmpty(member.getOnShareCode())){

			Member member1 =memberService.find(	ShareCodeUtils.codeToId(member.getOnShareCode()));

			member.setAttributeValue0(member1.getWeChatQcode());
			member.setAttributeValue1(member1.getWeChatNumber());
		/*	item.put("member", member);

			item.put("onWeChatNumber",member1.getWeChatNumber());
			item.put("onWeChatQcode",member1.getWeChatQcode());*/
		}

		member.setAttributeValue2("rxm/goods/commission.html");



		renderJson(ApiResult.success(member));
	}
	/**
	 * 佣金首页
	 */
	public void commissionNumber() {
        Member member = memberService.getCurrent();
        Integer num = member.getNumber();
        num++;
        member.setNumber(num);
        memberService.update(member);
        renderJson(ApiResult.success("成功"));
	}

	/**
	 * 团队管理
	 */
	@Deprecated
	public void teamManagement(){
		Member member = memberService.getCurrent();
		Integer pageNumber = getParaToInt("pageNumbers");

		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);

		Page<TeamManagement> page = memberService.getTeamManagementList(member.getShareCode(),pageable);


		//List<TeamManagement> te = memberService.getTeamManagementList("ESA99Y");
		if(page.getList().get(0).getId()==null){
			page=null;
		}

		renderJson(ApiResult.success(page));

	}
	/**
	 * 团队管理
	 */
	public void teamManagementFind() throws UnsupportedEncodingException {
		Member member = memberService.getCurrent();


		//String str = getPara("str");
		String str =  URLDecoder.decode(getPara("str"),"UTF-8");
		Integer pageNumber = getParaToInt("pageNumbers");

		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);

		Page<TeamManagement> page = memberService.getTeamManagementListFind(member.getShareCode(),pageable,str);


		//List<TeamManagement> te = memberService.getTeamManagementList("ESA99Y");
		if(page.getList().size()>0&&page.getList().get(0).getId()==null){
			page=null;
		}

		renderJson(ApiResult.success(page));

	}

	/**
	 *调出 管家 昵称和邀请码
	 */
	public void housekeeperNickname(){
		JSONObject redisSetting = JSONObject.parseObject(RedisUtil.getString("redisSetting"));
		Double shareSending = redisSetting.getDouble("shareSending");
		Double registerSending = redisSetting.getDouble("registerSending");
		Double vipSending = redisSetting.getDouble("vipSending");
		Double zongHe=registerSending+vipSending;
		Member member = memberService.getCurrent();
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		if(StringUtils.isNotEmpty(member.getShareCode())){
			resultMap.put("shareCode",member.getShareCode());
			resultMap.put("avatar",member.getAvatar());
			resultMap.put("nickName",member.getNickname());
			resultMap.put("zongHe",zongHe);
			resultMap.put("shareSending",shareSending);
		}else if(StringUtils.isNotEmpty(member.getOnShareCode())){

			Long idd = ShareCodeUtils.codeToId(member.getOnShareCode());

			Member mem = memberService.find(idd);
			resultMap.put("shareCode",mem.getShareCode());
			resultMap.put("avatar",mem.getAvatar());
			resultMap.put("nickName",mem.getNickname());
			resultMap.put("zongHe",zongHe);
			resultMap.put("shareSending",shareSending);
		}else {
			renderJson(ApiResult.fail(7,"没有自己及上级邀请码"));
			return;
		}

		renderJson(ApiResult.success(resultMap));

	}





	/**
	 * 审核用户名和身份证
	 */

	public void IDcardNmae(){
		Member member = memberService.getCurrent();
		String name = getPara("name");
		log.info("_______________________________上传姓名：_____" + name);
		String idCard = getPara("idCard");
		String phone = getPara("phone");
		log.info("_______________________________上传身份证号：_____" + idCard);
		Certificates certificates = certificatesService.queryByMemberId(member.getId());
		if( certificates == null){
			Certificates certificates1 = new Certificates();
			certificates1.setIdCard(idCard);
			certificates1.setPhone(phone);
			certificates1.setName(name);
			certificates1.setMemberId(member.getId());
			certificates1.setState(1);
			certificatesService.save(certificates1);
			renderJson(ApiResult.successMsg("上传成功,等待后台审核"));
			log.info("_______________________________上传成功姓名和身份证号成功：____________________________" );
		}
		certificates.setState(1);
		certificates.setIdCard(idCard);
		certificates.setName(name);
		certificates.setPhone(phone);
		certificatesService.update(certificates);
		log.info("_______________________________上传成功姓名和身份证号成功：____________________________" );
		renderJson(ApiResult.successMsg("上传成功,等待后台审核"));

	}
	
	/**
	 * 上传身份证 正面
	 */
	public void IDcardPosUpload() {
		log.info("_______________________________开始上传正面_____________________________________");
		UploadFile file = getFile();
		FileType fileType = FileType.valueOf(getPara("fileType", "image"));
		Member member = memberService.getCurrent();
		if (member == null) {
			renderJson(ApiResult.fail("当前用户不能为空!"));
			return;
		}
		if (fileType == null || file == null || file.getFile().length() <= 0) {
			renderJson(ApiResult.fail("请选择选图片"));
			return;
		}
		if (!fileService.isValid(fileType, file)) {
			renderJson(ApiResult.fail(Message.warn("admin.upload.invalid").toString()));
			return;
		}

		String url = fileService.upload(fileType, file, false);
		if (StringUtils.isEmpty(url)) {
			renderJson(ApiResult.fail(Message.warn("admin.upload.error").toString()));
			return;
		}
		log.info("_______________________________上传正面成功，返回url_____________________________________");


		Certificates certificates = certificatesService.queryByMemberId(member.getId());
		if( certificates == null){
			Certificates certificates1 = new Certificates();
			certificates1.setPositiveImg(url);
			certificates1.setMemberId(member.getId());
			certificates1.setState(0);
			certificatesService.save(certificates1);
			renderJson(ApiResult.successMsg("上传成功,等待后台审核"));
			log.info("_______________________________上传正面成功，等待审核_____________________________________");
			return;
		}
		certificates.setPositiveImg(url);
		certificatesService.update(certificates);
		renderJson(ApiResult.successMsg("上传成功,等待后台审核"));
		log.info("_______________________________上传正面成功，等待审核_____________________________________");
	}

	/**
	 * 上传身份证 反面
	 */
	public void IDcardOppUpload() {
		log.info("_______________________________开始上传反面_____________________________________");
		UploadFile file = getFile();
		FileType fileType = FileType.valueOf(getPara("fileType", "image"));
		Member member = memberService.getCurrent();
		if (member == null) {
			renderJson(ApiResult.fail("当前用户不能为空!"));
			return;
		}
		if (fileType == null || file == null || file.getFile().length() <= 0) {
			renderJson(ApiResult.fail("请选择选图片"));
			return;
		}
		if (!fileService.isValid(fileType, file)) {
			renderJson(ApiResult.fail(Message.warn("admin.upload.invalid").toString()));
			return;
		}
		String url = fileService.upload(fileType, file, false);
		if (StringUtils.isEmpty(url)) {
			renderJson(ApiResult.fail(Message.warn("admin.upload.error").toString()));
			return;
		}
		log.info("_______________________________上传反面成功，返回url_____________________________________");
		Certificates certificates = certificatesService.queryByMemberId(member.getId());
		if( certificates == null){
			Certificates certificates1 = new Certificates();
			certificates1.setOppositeImg(url);
			certificates1.setMemberId(member.getId());
			certificates1.setState(0);
			certificatesService.save(certificates1);
			renderJson(ApiResult.successMsg("上传成功,等待后台审核"));
			log.info("_______________________________上传反面成功，等待审核_____________________________________");
			return;
		}

		certificates.setOppositeImg(url);
		certificatesService.update(certificates);
		log.info("_______________________________上传反面成功，等待审核_____________________________________");
		renderJson(ApiResult.successMsg("上传成功,等待后台审核"));
	}

	/**
	 * 上传头像
	 */
	public void avatarUpload() {
		UploadFile file = getFile();
		FileType fileType = FileType.valueOf(getPara("fileType", "image"));
		Member member = memberService.getCurrent();
		if (member == null) {
			renderJson(ApiResult.fail("当前用户不能为空!"));
			return;
		}
		if (fileType == null || file == null || file.getFile().length() <= 0) {
			renderJson(ApiResult.fail("请选择选图片"));
			return;
		}
		if (!fileService.isValid(fileType, file)) {
			renderJson(ApiResult.fail(Message.warn("admin.upload.invalid").toString()));
			return;
		}
		String url = fileService.upload(fileType, file, true);
		if (StringUtils.isEmpty(url)) {
			renderJson(ApiResult.fail(Message.warn("admin.upload.error").toString()));
			return;
		}
		member.setAvatar(url);
		memberService.update(member);
		renderJson(ApiResult.successMsg("上传头像成功!"));
	}
	/**
	 * 上传头像
	 */
	public void weChatUpload() {
		UploadFile file = getFile();
		FileType fileType = FileType.valueOf(getPara("fileType", "image"));
		String weChatNumber = getPara("weChatNumber");
		Member member = memberService.getCurrent();
		if (member == null) {
			renderJson(ApiResult.fail("当前用户不能为空!"));
			return;
		}
		if (fileType == null || file == null || file.getFile().length() <= 0) {
			member.setWeChatNumber(weChatNumber);
			memberService.update(member);
			renderJson(ApiResult.successMsg("上传成功!"));
			return;
		}
		if (!fileService.isValid(fileType, file)) {
			renderJson(ApiResult.fail(Message.warn("admin.upload.invalid").toString()));
			return;
		}
		String url = fileService.upload(fileType, file, true);
		if (StringUtils.isEmpty(url)) {
			renderJson(ApiResult.fail(Message.warn("admin.upload.error").toString()));
			return;
		}
		member.setWeChatNumber(weChatNumber);
		member.setWeChatQcode(url);
		memberService.update(member);
		renderJson(ApiResult.successMsg("上传成功!"));
	}
	/**
	 * 意见反馈
	 */

	public void feedback(){
		String content = getPara("content");
		String feedbackUrl = getPara("feedbackUrl");
		Member member = memberService.getCurrent();
		Feedback memberFeedback = new Feedback();
		memberFeedback.setContent(content);
		memberFeedback.setMemberId(member.getId());
		memberFeedback.setImage(feedbackUrl);
		feedbackService.save(memberFeedback);
		renderJson(ApiResult.successMsg("反馈成功"));
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
		if (! fileService.isValid(fileType, file)) {
			renderJson(ApiResult.fail(Message.warn("admin.upload.invalid").toString()));
			return;
		}
		String url = fileService.upload(fileType, file, true);
		if (StringUtils.isEmpty(url)) {
			renderJson(ApiResult.fail(Message.warn("admin.upload.error").toString()));
			return;
		}
		renderJson(ApiResult.success(url));
	}

    /**
     * 身份证审核
     */
    public void IDcard(){
        Member member = memberService.getCurrent();
        Certificates certificates = certificatesService.queryByMemberId(member.getId());
        if(certificates != null){
            if(1 == certificates.getState()){
                renderJson(ApiResult.success(certificates));
            }else {
                renderJson(ApiResult.fail("正在审核您的身份信息"));
            }

        }else {
            renderJson(ApiResult.fail("您还未上传身份信息"));
        }
    }

	/**
	 * 设置
	 */
	public void about(){
		render("/admin/about/about.ftl");
	}



}
