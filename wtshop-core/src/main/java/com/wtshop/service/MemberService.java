package com.wtshop.service;

import com.jfinal.aop.Enhancer;
import com.jfinal.ext.plugin.monogodb.MongoKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.wtshop.Pageable;
import com.wtshop.Principal;
import com.wtshop.RequestContextHolder;
import com.wtshop.Setting;
import com.wtshop.dao.*;
import com.wtshop.exception.AppRuntimeException;
import com.wtshop.model.*;
import com.wtshop.util.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

/**
 * Service - 会员
 * 
 * 
 */
public class MemberService extends BaseService<Member> {

	/**
	 * 构造方法
	 */
	public MemberService() {
		super(Member.class);
	}
	private MemberDao memberDao = Enhancer.enhance(MemberDao.class);
	private TeamManagementDao teamManagementDao = Enhancer.enhance(TeamManagementDao.class);
	private MemberRankDao memberRankDao = Enhancer.enhance(MemberRankDao.class);
	private MailService mailService = Enhancer.enhance(MailService.class);
	private SmsService smsService = Enhancer.enhance(SmsService.class);
	private MrmfShopDao mrmfShopDao = Enhancer.enhance(MrmfShopDao.class);
	private StaffMemberDao staffMemberDao = Enhancer.enhance(StaffMemberDao.class);
	public List<Member> findMemberByOnShare(String shareCode){
		return memberDao.findMemberByOnShare(shareCode);
	}	public List<Member> findMemberByOnShareJ(String shareCode){
		return memberDao.findMemberByOnShareJ(shareCode);
	}
	public List<Member> findMemberByLinkShare(String shareCode){
		return memberDao.findMemberByLinkShare(shareCode);
	}
	public List<Member> findMemberByLinkShare(String shareCode,long housekeeper_id){
		return memberDao.findMemberByLinkShare(shareCode,housekeeper_id);
	}

	public List<Member> findMemberList(Long[] memberList){
		return memberDao.findMemberList(memberList);
	}
	/**
	 * 获取佣金技师
	 */
	public Member findStaffList(Long memberId, Long goodId){
		return memberDao.findStaffList(memberId, goodId);
	}


	/**
	 * 根据当前用户信息 获取所在店铺的id
	 */
	public String findOrganId(Member member){
		String phone = member.getPhone();
		BasicDBObject basicDBObject = new BasicDBObject();
		basicDBObject.put("shopPhone",phone);
		DBObject organ = MongoKit.getCollection("organ").findOne(basicDBObject);
		if(organ != null){
			String id = organ.get("_id").toString();
			return id;
		}
		return null;
	}


	/**
	 * 根据idList的集合获取member信息
	 */
	public List<Member> findMemberByList(List<Long> idList){
		return memberDao.findMemberByList(idList);
	}


	/**
	 *
	 * @param pageable
	 * @return
	 */
	public Page<Record> findBalancePage(Pageable pageable ){
		Double minMoney = null;
		Double maxMoney = null;
		String phone = null;
		String nickname = null;
		String searchProperty = pageable.getSearchProperty();
		String searchValue = pageable.getSearchValue();
		if(searchValue == null){
			return memberDao.findBalancePage(pageable ,null ,null, null, null);
		}
		if(searchProperty != null){
			switch (searchProperty){
				case "minMoney":
					minMoney = NumberUtils.toDouble(pageable.getSearchValue(), 0);
					break;
				case "maxMoney":
					maxMoney = NumberUtils.toDouble(pageable.getSearchValue(), 0);
					break;
				case "phone":
					phone = pageable.getSearchValue();
					break;
				case "nickname":
					nickname = pageable.getSearchValue();
					break;
			}
		}
		return memberDao.findBalancePage(pageable ,minMoney ,maxMoney, phone, nickname);
	}



	/**
	 *  个人信息
	 */
	public List<Member> findMySelfInterest(Long memberId){
		return memberDao.findMySelfInterest(memberId);
	}

	/**
	 *  个人信息
	 */
	public List<Member> findMySelfSkin(Long memberId){
		return memberDao.findMySelfSkin(memberId);
	}




	public Member register(String username, String password, String nickname, String remoteAddr,String onShareCode,String linkShareCode){
		Setting setting = SystemUtils.getSetting();
		//判断用户名是否是mongo数据库中数据
		//查询mongo数据库
	/*	BasicDBObject basicDBObject = new BasicDBObject();
		basicDBObject.put("phone",username);
		DBObject user = MongoKit.getCollection("user").findOne(basicDBObject);
		DBObject staff = MongoKit.getCollection("staff").findOne(basicDBObject);
		DBObject organ = MongoKit.getCollection("organ").findOne(basicDBObject);


		Boolean isVip = false;
		DBObject vip = MongoKit.getCollection("vipMember").findOne(basicDBObject);
		if(! ObjectUtils.isEmpty(vip)){
			isVip = true;
		}*/
		Boolean isVip = false;
		Member member = new Member();

		String phone = StringUtils.lowerCase(username);
		String mixUserName = nickname;
		if (StringUtils.isBlank(nickname)){
			if (phone.length() >= 11){
				mixUserName = StringUtils.substring(phone, 0, 3) + "****" + StringUtils.substring(phone, 7);
			}else {
				mixUserName = "-";
			}
		}
if(StringUtils.isNotEmpty(onShareCode)){
	member.setOnShareCode(onShareCode);
}
if(StringUtils.isNotEmpty(linkShareCode)){
	member.setLinkShareCode(linkShareCode);
}

		member.setUsername(mixUserName);
		member.setPassword(DigestUtils.md5Hex(password));
		member.setPhone(phone);
		member.setPoint(BigDecimal.ZERO);
		member.setBalance(BigDecimal.ZERO);
		member.setAmount(BigDecimal.ZERO);
		member.setPrestore(BigDecimal.ZERO);
		member.setCommission(BigDecimal.ZERO);
		member.setRecharge(BigDecimal.ZERO);
		member.setGender(1);
		member.setNickname(phone);
		member.setIsEnabled(true);
		member.setIsLocked(false);
		member.setLoginFailureCount(0);
		member.setLockedDate(null);
		member.setRegisterIp(remoteAddr);
		member.setLoginIp(remoteAddr);
		member.setLoginDate(new Date());
		member.setLoginPluginId(null);
		member.setOpenId(null);
		member.setLockKey(DigestUtils.md5Hex(UUID.randomUUID() + RandomStringUtils.randomAlphabetic(30)));
		member.setSafeKey(null);
		member.setMemberRankId(memberRankDao.findDefault().getId());
		member.setCart(null);
		member.setOrders(null);
		member.setPaymentLogs(null);
		member.setDepositLogs(null);
		member.setCouponCodes(null);
		member.setReceivers(null);
		member.setReviews(null);
		member.setConsultations(null);
		member.setFavoriteGoods(null);
		member.setProductNotifies(null);
		member.setInMessages(null);
		member.setOutMessages(null);
		member.setPointLogs(null);
		member.setIsDelete(false);
		member.setIsVip(isVip);
		member.setAvatar(setting.getAvatar());
		memberDao.save(member);

		//判断mogo是否存在 若存在 更新数据 若不存在 插入数据
/*		if(user != null){
			user.put("shopPassword", DigestUtils.md5Hex(password));
			MongoKit.getCollection("user").save(user);

		}else if(staff != null){
			staff.put("shopPassword",DigestUtils.md5Hex(password));
			MongoKit.getCollection("staff").save(staff);

		}else if(organ != null){
			organ.put("shopPassword",DigestUtils.md5Hex(password));
			MongoKit.getCollection("organ").save(organ);
		}else{
			Record record = new Record().set("phone" ,username).set("shopPassword" ,password);
			MongoKit.save("user",record);
		}*/
		return member;
	}

	/**
	 * 用户注册
	 * @param username
	 * @param password
	 * @return
	 */

	public Member register(String username,String password ,String remoteAddr,String onShareCode,String linkShareCode){
		return register(username, password, "", remoteAddr,onShareCode,linkShareCode);
	}


	/**
	 * 会员分页
	 */

	public Page<Member> findPages(Pageable pageable, Integer type){

		return memberDao.findPages(pageable, type);

	}
	/**
	 * 会员分页 并显示关注数量
	 */

	public Page<Member> findPage(Pageable pageable){

		return memberDao.findPage(pageable);

	}

	/**
	 * 判断用户名是否存在
	 * 
	 * @param phone
	 *            用户名(忽略大小写)
	 * @return 用户名是否存在
	 */
	public boolean usernameExists(String phone) {
		return memberDao.usernameExists(phone);
	}

	/**
	 * 判断用户名是否禁用
	 * 
	 * @param username
	 *            用户名(忽略大小写)
	 * @return 用户名是否禁用
	 */
	public boolean usernameDisabled(String username) {
		Assert.hasText(username);

		Setting setting = SystemUtils.getSetting();
		if (setting.getDisabledUsernames() != null) {
			for (String disabledUsername : setting.getDisabledUsernames()) {
				if (StringUtils.containsIgnoreCase(username, disabledUsername)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 判断E-mail是否存在
	 * 
	 * @param email
	 *            E-mail(忽略大小写)
	 * @return E-mail是否存在
	 */
	public boolean emailExists(String email) {
		return memberDao.emailExists(email);
	}

	/**
	 * 判断E-mail是否唯一
	 * 
	 * @param previousEmail
	 *            修改前E-mail(忽略大小写)
	 * @param currentEmail
	 *            当前E-mail(忽略大小写)
	 * @return E-mail是否唯一
	 */
	public boolean emailUnique(String previousEmail, String currentEmail) {
		if (StringUtils.equalsIgnoreCase(previousEmail, currentEmail)) {
			return true;
		}
		return !memberDao.emailExists(currentEmail);
	}

	/**
	 * 查找会员
	 * 
	 * @param loginPluginId
	 *            登录插件ID
	 * @param openId
	 *            openID
	 * @return 会员，若不存在则返回null
	 */
	public Member find(String loginPluginId, String openId) {
		return memberDao.find(loginPluginId, openId);
	}

	/**
	 * 根据用户名查找会员
	 *
	 * @param username
	 *            用户名(忽略大小写)
	 * @return 会员，若不存在则返回null
	 */
	public Member findByUsername(String username) {
		return memberDao.findByUsername(username);
	}


	/**
	 * 判断邀请码是否存在
	 *
	 * @param onShareCode
	 *            用户名(忽略大小写)
	 * @return 会员，若不存在则返回null
	 */
	public List<Member> findByShareCode(String onShareCode) {
		return memberDao.findByShareCode(onShareCode);
	}
	public List<Member> findMemberByHousekeeperId(long housekeeperId) {
		return memberDao.findMemberByHousekeeperId(housekeeperId);
	}



	/**
	 * 查询角色对应权限
	 * @param member
	 * @return
	 */
	public Map queryUser(Member member){
		Map map = new HashMap();
		map.put("hasUser",false);
		map.put("hasStaff",false);
		map.put("hasOrgan",false);
		map.put("hasVip",false);
		map.put("hasBangDing",false);
		map.put("shareCode",member.getShareCode());

		StaffMember staffMember = staffMemberDao.queryByMemberId(member.getId());
	/*	MrmfShop mrmfShop = mrmfShopDao.findByMemberId(member.getId());
		if(!ObjectUtils.isEmpty(mrmfShop)){
			map.put("hasOrgan",true); //组织(店铺)
		}*/
		if(!ObjectUtils.isEmpty(staffMember)){
			map.put("hasBangDing",true); //绑定
		}
	/*	if(com.wtshop.util.StringUtils.isEmpty(member.getPhone())){//如果手机号为空，则不会去mongo查询
			return map;
		}
		BasicDBObject basicDBObject = new BasicDBObject();
		basicDBObject.put("phone",member.getPhone());
		DBObject user = MongoKit.getCollection("user").findOne(basicDBObject);
		DBObject staff = MongoKit.getCollection("staff").findOne(basicDBObject);
		DBObject vip = MongoKit.getCollection("vipMember").findOne(basicDBObject);
		//判断角色  覆盖模式  默认最大店铺
		if(!ObjectUtils.isEmpty(user)){
			map.put("hasUser",true);
		}
		if(!ObjectUtils.isEmpty(staff)){
			map.put("hasStaff",true);
		}
		if(!ObjectUtils.isEmpty(vip)){
			map.put("hasVip",true);
		}*/
		return map;
	}

	public Member findByUsernames(String phone ,String password) {
		Member member = memberDao.findByUsername(phone);
/*		if (member != null) {
			//判断mongo是否存在 存在 更新mogo中密码  不存在插入数据
			BasicDBObject basicDBObject = new BasicDBObject();
			basicDBObject.put("phone",phone);
			DBObject user = MongoKit.getCollection("user").findOne(basicDBObject);

			DBObject staff = MongoKit.getCollection("staff").findOne(basicDBObject);

			DBObject organ = MongoKit.getCollection("organ").findOne(basicDBObject);

			if(user != null){
				user.put("shopPassword", DigestUtils.md5Hex(password));
				MongoKit.getCollection("user").save(user);

			}else if(staff != null){
				staff.put("shopPassword",DigestUtils.md5Hex(password));
				MongoKit.getCollection("staff").save(staff);

			}else if(organ != null){
				organ.put("shopPassword",DigestUtils.md5Hex(password));
				MongoKit.getCollection("organ").save(organ);
			}else{
				Record record = new Record().set("phone" ,phone).set("shopPassword" ,password);
				MongoKit.save("user",record);
			}
			return member;

		}*/
		return member;

	}

	/**
	 * 根据手机号查找会员
	 *
	 * @param phone
	 *            用户名(忽略大小写)
	 * @return 会员，若不存在则返回null
	 */
	public Member findByPhone(String phone) {
		return memberDao.findByUsername(phone);
	}
	
	/**
	 * 根据openId查找会员
	 *
	 *            
	 * @return 会员，若不存在则返回null
	 */
	public Member findByOpenId(String openId) {
		return memberDao.findByOpenId(openId);
	}

	/**
	 * 根据openId phone查找会员
	 *
	 *
	 * @return 会员，若不存在则返回null
	 */
	public Member findByOpenId(String type, String phone) {
		return memberDao.findByOpenId(type, phone);
	}

	/**
	 * 根据E-mail查找会员
	 * 
	 * @param email
	 *            E-mail(忽略大小写)
	 * @return 会员，若不存在则返回null
	 */
	public List<Member> findListByEmail(String email) {
		return memberDao.findListByEmail(email);
	}

	/**
	 * 查找会员分页
	 * 
	 * @param rankingType
	 *            排名类型
	 * @param pageable
	 *            分页信息
	 * @return 会员分页
	 */
	public Page<Member> findPage(Member.RankingType rankingType, Pageable pageable) {
		return memberDao.findPage(rankingType, pageable);
	}

	/**
	 * 判断会员是否登录
	 * 
	 * @return 会员是否登录
	 */
	public boolean isAuthenticated() {
		HttpServletRequest requestAttributes = RequestContextHolder.currentRequestAttributes();
		return requestAttributes != null && requestAttributes.getSession().getAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME) != null;
	}

	/**
	 * 获取当前登录会员
	 * 
	 * @return 当前登录会员，若不存在则返回null
	 */
	public Member getCurrent() {
		return getCurrent(false);
	}
/**
 *团队管理
 */
public Page<TeamManagement> getTeamManagementList(String onShareCode,Pageable pageable){
	Page<TeamManagement> page = teamManagementDao.getTeamManagementList(onShareCode,pageable);

	return page;
}/**
 *团队管理
 */
public Page<TeamManagement> getTeamManagementListFind(String onShareCode,Pageable pageable,String str){
	Page<TeamManagement> page = teamManagementDao.getTeamManagementList(onShareCode,pageable,str);

	return page;
}
	/**
	 * 获取当前登录会员
	 * 
	 * @param lock
	 *            是否锁定
	 * @return 当前登录会员，若不存在则返回null
	 */

	public Member getCurrent(boolean lock) {
		HttpServletRequest request = RequestContextHolder.currentRequestAttributes();
		if (StringUtils.isNotEmpty(request.getHeader("token")) && !"0".equals(request.getHeader("token")) ){
			String userId = RedisUtil.getString(request.getHeader("token"));
			if (userId == null ){
				throw new AppRuntimeException(422, "请先登录");
			}
			return memberDao.find(Long.valueOf(userId));
		}else{
			Principal principal = request != null ? (Principal) request.getSession().getAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME) : null;
			Long id = principal != null ? principal.getId() : null;
			if (id == null) {
				return memberDao.find(Long.valueOf("155"));
			}
			return memberDao.find(Long.valueOf(id));
		}
	}


	/**
	 * 获取当前登录用户名
	 * 
	 * @return 当前登录用户名，若不存在则返回null
	 */
	public String getCurrentUsername() {
		HttpServletRequest requestAttributes = RequestContextHolder.currentRequestAttributes();
		Principal principal = requestAttributes != null ? (Principal) requestAttributes.getSession().getAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME) : null;
		return principal != null ? principal.getUsername() : null;
	}



	/**
	 * 增加消费金额
	 * 
	 * @param member
	 *            会员
	 * @param amount
	 *            值
	 */
	public void addAmount(Member member, BigDecimal amount) {
		Assert.notNull(member);
		Assert.notNull(amount);

		if (amount.compareTo(BigDecimal.ZERO) == 0) {
			return;
		}

		Assert.notNull(member.getAmount());
		Assert.state(member.getAmount().add(amount).compareTo(BigDecimal.ZERO) >= 0);

		member.setAmount(member.getAmount().add(amount));
		MemberRank memberRank = member.getMemberRank();
		if (memberRank != null && BooleanUtils.isFalse(memberRank.getIsSpecial())) {
			MemberRank newMemberRank = memberRankDao.findByAmount(member.getAmount());
			if (newMemberRank != null && newMemberRank.getAmount() != null && newMemberRank.getAmount().compareTo(memberRank.getAmount()) > 0) {
				member.setMemberRank(newMemberRank);
			}
		}
		memberDao.update(member);
	}

	public Member save(Member member) {
		Assert.notNull(member);
		Member pMember = super.save(member);
		mailService.sendRegisterMemberMail(pMember);
		smsService.sendRegisterMemberSms(pMember);
		return pMember;
	}

    public void updateJieritixing() {
		memberDao.updateJieritixing();
    }
}