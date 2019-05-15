package com.wtshop.dao;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.wtshop.Order;
import com.wtshop.Pageable;
import com.wtshop.model.Member;
import com.wtshop.model.MemberAttribute;
import com.wtshop.util.DateUtils;
import com.wtshop.util.SqlUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * Dao - 会员
 * 
 */
public class MemberDao extends BaseDao<Member> {
	
	/**
	 * 构造方法
	 */
	public MemberDao() {
		super(Member.class);
	}

	/**
	 * 查找技师所有集合
	 */

	public Member findStaffList(Long memberId, Long goodId){

		String sql = " SELECT m.* FROM referrer_goods r LEFT JOIN member m on r.member_id = m.id WHERE 1 = 1  AND `status` = 1  ";

		if(memberId != null){
			sql += " AND r.member_id = " + memberId;
		}
		if( goodId != null){

			sql += " AND goods_id = " + goodId;
		}

		sql += " GROUP BY staff_id,member_id,goods_id ";

		return modelManager.findFirst(sql);

	}



	/**
	 * 查询所有有效的会员
	 */
	public List<Member> findMemberList(Long[] memberList){
		String sql = " select * from member where 1 = 1 AND is_delete = 0 ";
		if(memberList != null && memberList.length > 0){
			sql +=" and id in " + SqlUtils.getSQLIn(Arrays.asList(memberList));
		}
		return modelManager.find(sql);
	}
	/**
	 * 查询所有直系下线会员
	 */
	public List<Member> findMemberByOnShare(String shareCode){
		String sql = " select * from member where 1 = 1 AND is_delete = 0 And share_code='"+shareCode+"'  ";
		return modelManager.find(sql);
	}
	/**
	 * 查询所有直系下线金牌及以上
	 */
	public List<Member> findMemberByOnShareJ(String shareCode){
		String sql = " select * from member where 1 = 1 AND is_delete = 0 And share_code='"+shareCode+"'and  housekeeper_id>1 ";
		return modelManager.find(sql);
	}
	/**
	 * 查询所有总下线会员
	 */
	public List<Member> findMemberByLinkShare(String shareCode){
		String sql = " select * from member where 1 = 1 AND is_delete = 0 and share_code is not null  And   link_share_code like '%"+shareCode+"%' ";
		return modelManager.find(sql);
	}	/**
	 * 查询所有总下线会员
	 */
	public List<Member> findMemberByLinkShare(String shareCode,long housekeeper_id){
		String sql = " select * from member where 1 = 1 AND is_delete = 0 and share_code is not null  And   link_share_code like '%"+shareCode+"%' and housekeeper_id="+housekeeper_id+" ";
		return modelManager.find(sql);
	}

	/**
	 * 根据idList的集合获取id的信息
	 *
	 */
	public List<Member> findMemberByList(List<Long> idList){

		if(idList != null & idList.size() > 0){
			String sql = " select * FROM member where 1 = 1 AND is_delete = 0 AND id in " + SqlUtils.getSQLIn(idList);
			return modelManager.find(sql);

		}else {
			return null;
		}

	}



	/**
	 * 用户钱包管理分页
	 */

	public Page<Record> findBalancePage(Pageable pageable, Double minMoney, Double maxMoney, String phone, String nickname){

		String sql = " from member where 1 = 1 ";
		if( minMoney != null ){
			sql += " AND balance >= " +minMoney ;
		}
		if( maxMoney != null ){
			sql += " AND balance <= " +maxMoney;
		}
		if( phone != null){
			sql += " AND phone like '%" + phone + "%'";
		}
		if( nickname != null){
			sql += " AND nickname like '%" + nickname + "%'";
		}

		String select = "SELECT id, phone, nickname, login_date, balance";


		// 排序属性、方向
		String ordersSQL = getOrders(pageable.getOrders());
		String orderProperty = pageable.getOrderProperty();
		Order.Direction orderDirection = pageable.getOrderDirection();
		if (StringUtils.isNotEmpty(orderProperty) && orderDirection != null) {
			switch (orderDirection) {
				case asc:
					sql += " ORDER BY " + orderProperty + " ASC ";
					break;
				case desc:
					sql += " ORDER BY " + orderProperty + " DESC ";
					break;
			}
		} else if (StrKit.isBlank(ordersSQL)) {
			ordersSQL = " ORDER BY login_date DESC";
		}

		sql += ordersSQL;

		return Db.paginate(pageable.getPageNumber(), pageable.getPageSize(), select, sql);

	}


	/**
	 * 个人信息
	 */
	public List<Member> findMySelfInterest(Long memberId){

		if(memberId == null){
			return null;

		}else {
			String sql = "SELECT name,c.id from member_interest_category i LEFT JOIN interest_category c on i.interest_category = c.id WHERE i.members = " + memberId;
			return super.findListBySql(sql);
		}

	}

	/**
	 * 个人信息
	 */
	public List<Member> findMySelfSkin(Long memberId){

		if(memberId == null){
			return null;

		}else {
			String sql = "SELECT name,c.id from member_skin_type i LEFT JOIN skin_type c on i.skin_type = c.id WHERE i.members = " + memberId;
			return super.findListBySql(sql);
		}

	}

	/**
	 * 会员分页
	 */

	public Page<Member> findPages(Pageable pageable ,Integer type){
		String sql = " FROM member where 1 = 1 AND is_delete = 0 " ;
		if(type != null){
			sql +=" and member_rank_id = " +type;
		}
		return super.findPage(sql,pageable);
	}

	public Page findPage(Pageable pageable){
		String select = "SELECT me.`id`,me.username,me.nickname,me.phone,tt.favoriteNum " ;
		String from = "from member me\n" +
				"LEFT JOIN (SELECT count(*) as favoriteNum,m.favorite_members FROM member_favorite_goods m WHERE 1=1 GROUP BY m.favorite_members) tt\n" +
				"ON tt.favorite_members=me.id";
		from+=" WHERE 1=1";
		String order = pageable.getOrderProperty();
		if(StringUtils.isEmpty(order)){
			pageable.setOrderProperty("favoriteNum");
			pageable.setOrderDirection("desc");
		}
		return super.findPages(select,from,pageable);
	}

	/**
	 * 判断用户名是否存在
	 * 
	 * @param phone
	 *            用户名(忽略大小写)
	 * @return 用户名是否存在
	 */
	public boolean usernameExists(String phone) {
		if (StringUtils.isEmpty(phone)) {
			return false;
		}
		String sql = "SELECT COUNT(*) FROM member WHERE phone = LOWER(?)";
		Long count = Db.queryLong(sql, phone);
		return count > 0;
	}

	/**
	 * 判断E-mail是否存在
	 * 
	 * @param email
	 *            E-mail(忽略大小写)
	 * @return E-mail是否存在
	 */
	public boolean emailExists(String email) {
		if (StringUtils.isEmpty(email)) {
			return false;
		}
		String sql = "SELECT COUNT(*) FROM member WHERE email = LOWER(?)";
		Long count = Db.queryLong(sql, email);
		return count > 0;
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
		if (StringUtils.isEmpty(loginPluginId) || StringUtils.isEmpty(openId)) {
			return null;
		}
		try {
			String sql = "SELECT * FROM member WHERE login_plugin_id = ? AND open_id = ?";
			return modelManager.findFirst(sql, loginPluginId, openId);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 根据用户名查找会员
	 * 
	 * @param phone
	 *            用户名(忽略大小写)
	 * @return 会员，若不存在则返回null
	 */
	public Member findByUsername(String phone) {
		if (StringUtils.isEmpty(phone)) {
			return null;
		}
		try {
			String sql = "SELECT * FROM member WHERE is_enabled = 1 and is_delete = 0 and phone = LOWER(?)";
			return modelManager.findFirst(sql, phone);
		} catch (Exception e) {
			return null;
		}
	}
	/**
	 * 查询是否存在邀请码
	 *
	 */
	public List<Member> findByShareCode(String onShareCode) {
		if (StringUtils.isEmpty(onShareCode)) {
			return null;
		}
		try {
			String sql = "SELECT * FROM member WHERE is_enabled = 1 and is_delete = 0 and share_code = UPPER(?)";
			return modelManager.find(sql, onShareCode);
		} catch (Exception e) {
			return null;
		}
	}
	public List<Member> findMemberByHousekeeperId(Long housekeeperId) {
		try {

			String sql = "SELECT sum(s.price) zprice, sum( s.price * s.commission_rate / 100 ) zcommission, m.* FROM member m LEFT JOIN ( SELECT o1.member_id dddd, o1.price, o1.commission_rate, o1.create_date sssasa, o1.`status`, m1.* FROM member m1 LEFT JOIN `order` o1 ON o1.member_id = m1.id WHERE o1.id IS NOT NULL AND m1.housekeeper_id <"+housekeeperId+" AND ( date(o1.create_date) BETWEEN date_sub( date_sub(now(), INTERVAL 7 MONTH), INTERVAL 1 DAY ) AND date_sub(now(), INTERVAL 7 DAY)) AND o1.`status` IN (2, 3, 4, 5, 9, 10)) s ON s.link_share_code LIKE concat('%', m.share_code, '%') WHERE m.housekeeper_id ="+housekeeperId+" GROUP BY id";
			return modelManager.find(sql);
			//return modelManager.find(sql, housekeeperId);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 根据手机号查找会员
	 *
	 * @param phone
	 *            手机号(忽略大小写)
	 * @return 会员，若不存在则返回null
	 */
	public Member findByPhone(String phone) {
		if (StringUtils.isEmpty(phone)) {
			return null;
		}
		try {
			String sql = "SELECT * FROM member WHERE mobile = LOWER(?)";
			return modelManager.findFirst(sql, phone);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 根据openId查找会员
	 * 
	 * @param openId
	 *            
	 * @return 会员，若不存在则返回null
	 */
	public Member findByOpenId(String openId) {
		if (StringUtils.isEmpty(openId)) {
			return null;
		}
		try {
			String sql = "SELECT * FROM member WHERE open_id = ?";
			return modelManager.findFirst(sql, openId);
		} catch (Exception e) {
			return null;
		}
	}


	/**
	 * 根据openId查找会员
	 *
	 * @param phone
	 *
	 * @return 会员，若不存在则返回null
	 */
	public Member findByOpenId(String type, String phone) {
		if (StringUtils.isEmpty(phone)) {
			return null;
		}
		try {
			String sql = "SELECT * FROM member WHERE open_id != null and phone = ? and register_type = ?";
			return modelManager.findFirst(sql, phone, type);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 根据E-mail查找会员
	 * 
	 * @param email
	 *            E-mail(忽略大小写)
	 * @return 会员，若不存在则返回null
	 */
	public List<Member> findListByEmail(String email) {
		if (StringUtils.isEmpty(email)) {
			return Collections.emptyList();
		}
		String sql = "SELECT * FROM member WHERE email = LOWER(?)";
		return modelManager.find(sql, email);
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
		String sqlExceptSelect = "FROM member WHERE 1 = 1 ";
		List<Order> orders = new ArrayList<Order>();
		if (rankingType != null) {
			switch (rankingType) {
			case point:
				orders.add(new Order("point", Order.Direction.desc));
				break;
			case balance:
				orders.add(new Order("balance", Order.Direction.desc));
				break;
			case amount:
				orders.add(new Order("amount", Order.Direction.desc));
				break;
			}
		}
		pageable.setOrders(orders);
		return super.findPage(sqlExceptSelect, pageable);
	}

	/**
	 * 查询会员注册数
	 * 
	 * @param beginDate
	 *            起始日期
	 * @param endDate
	 *            结束日期
	 * @return 会员注册数
	 */
	public Long registerMemberCount(Date beginDate, Date endDate) {
		String sqlExceptSelect = "FROM member WHERE 1 = 1 ";
		if (beginDate != null) {
			sqlExceptSelect += " AND create_date >= '" + DateUtils.getDateTime(beginDate) +"' ";
		}
		if (endDate != null) {
			sqlExceptSelect += " AND create_date <= '" + DateUtils.getDateTime(endDate) +"' ";
		}
		return super.count(sqlExceptSelect);
	}

	/**
	 * 清空会员注册项值
	 * 
	 * @param memberAttribute
	 *            会员注册项
	 */
	public void clearAttributeValue(MemberAttribute memberAttribute) {
		if (memberAttribute == null || memberAttribute.getType() == null || memberAttribute.getPropertyIndex() == null) {
			return;
		}

		String propertyName;
		switch (memberAttribute.getTypeName()) {
		case text:
		case select:
		case checkbox:
			propertyName = "attribute_value" + memberAttribute.getPropertyIndex();
			break;
		default:
			propertyName = String.valueOf(memberAttribute.getType());
			break;
		}
		String sql = "UPDATE member SET " + propertyName + " = null";
		Db.update(sql);
	}



	/**
	 * 转换为Order
	 *
	 *
	 *            Root
	 * @param orders
	 *            排序
	 * @return Order
	 */
	private String getOrders(List<Order> orders) {
		String orderSql = "";
		if (CollectionUtils.isNotEmpty(orders)) {
			orderSql = " ORDER BY ";
			for (Order order : orders) {
				String property = order.getProperty();
				Order.Direction direction = order.getDirection();
				switch (direction) {
					case asc:
						orderSql += property + " ASC, ";
						break;
					case desc:
						orderSql += property + " DESC,";
						break;
				}
			}
			orderSql = StringUtils.substring(orderSql, 0, orderSql.length() - 1);
		}
		return orderSql;
	}

    public void updateJieritixing() {
		String sql = "UPDATE member SET jieritixing_num=0" ;
		Db.update(sql);
    }
}