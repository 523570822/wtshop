package com.wtshop.dao;

import com.jfinal.plugin.activerecord.Page;

import com.wtshop.Pageable;
import com.wtshop.model.Member;
import com.wtshop.model.MemberAttribute;
import com.wtshop.model.TeamManagement;
import com.wtshop.util.DateUtils;
import com.wtshop.util.SqlUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * Dao - 会员
 * 
 */
public class TeamManagementDao extends BaseDao<TeamManagement> {

	/**
	 * 构造方法
	 */
	public TeamManagementDao() {
		super(TeamManagement.class);
	}

	/**
	 * 查找技师所有集合
	 */

	public Page<TeamManagement> getTeamManagementList(String onShareCode,Pageable pageable){
	//	String sql = "SELECT count(dsb1.id) memeber_num, dsb.* FROM ( SELECT count(o.id) order_num, mm.id, mm.avatar, mm.share_code, mm.create_date, mm.we_chat_number, mm.phone, mm.nickname, sum(o.price) price_num FROM ( SELECT m.id, m.avatar, m.share_code, m.create_date, m.we_chat_number, m.phone, m.nickname FROM member m WHERE m.on_share_code = '"+onShareCode+"' ) mm LEFT JOIN `order` o ON mm.id = o.member_id WHERE o.on_share_code = '"+onShareCode+"' AND mm.share_code IS NOT NULL GROUP BY mm.id ) dsb LEFT JOIN member dsb1 ON dsb1.link_share_code LIKE concat('%', dsb.share_code, '%')";
	//	String sql = "SELECT count(dsb1.id) memeber_num, dsb.* FROM ( SELECT count(o.id) order_num, mm.*, sum(o.price) price_num FROM ( SELECT m.id, m.avatar, m.share_code, m.create_date, m.we_chat_number, m.phone, m.nickname, h.`name` housername  FROM member m LEFT JOIN houserkeeper h ON m.housekeeper_id = h.id WHERE m.on_share_code = '"+onShareCode+"' ) mm LEFT JOIN `order` o ON mm.id = o.member_id WHERE o.on_share_code = '"+onShareCode+"' AND mm.share_code IS NOT NULL GROUP BY mm.id ) dsb LEFT JOIN member dsb1 ON dsb1.link_share_code LIKE concat('%', dsb.share_code, '%')";
	//	String sql = "";
		String select ="SELECT count(dsb1.id) memeber_num, dsb.* ";
		String	sqlExceptSelect=" FROM ( SELECT count(o.id) order_num, mm.*, sum(o.price) price_num FROM ( SELECT m.id, m.avatar, m.share_code, m.create_date, m.we_chat_number, m.phone, m.nickname,m.phone_status, h.`name` housername  FROM member m LEFT JOIN houserkeeper_grade h ON m.housekeeper_id = h.id WHERE m.on_share_code = '"+onShareCode+"' ) mm LEFT JOIN `order` o ON mm.id = o.member_id WHERE o.on_share_code = '"+onShareCode+"' AND mm.share_code IS NOT NULL GROUP BY mm.id ) dsb LEFT JOIN member dsb1 ON dsb1.link_share_code LIKE concat('%', dsb.share_code, '%')";

		return modelManager.paginate(pageable.getPageNumber(), pageable.getPageSize(), select, sqlExceptSelect);

	}
	/**
	 * 查找技师所有集合
	 */

	public Page<TeamManagement> getTeamManagementList(String onShareCode,Pageable pageable,String str){
	//	String sql = "SELECT count(dsb1.id) memeber_num, dsb.* FROM ( SELECT count(o.id) order_num, mm.id, mm.avatar, mm.share_code, mm.create_date, mm.we_chat_number, mm.phone, mm.nickname, sum(o.price) price_num FROM ( SELECT m.id, m.avatar, m.share_code, m.create_date, m.we_chat_number, m.phone, m.nickname FROM member m WHERE m.on_share_code = '"+onShareCode+"' ) mm LEFT JOIN `order` o ON mm.id = o.member_id WHERE o.on_share_code = '"+onShareCode+"' AND mm.share_code IS NOT NULL GROUP BY mm.id ) dsb LEFT JOIN member dsb1 ON dsb1.link_share_code LIKE concat('%', dsb.share_code, '%')";
	//	String sql = "SELECT count(dsb1.id) memeber_num, dsb.* FROM ( SELECT count(o.id) order_num, mm.*, sum(o.price) price_num FROM ( SELECT m.id, m.avatar, m.share_code, m.create_date, m.we_chat_number, m.phone, m.nickname, h.`name` housername  FROM member m LEFT JOIN houserkeeper h ON m.housekeeper_id = h.id WHERE m.on_share_code = '"+onShareCode+"' ) mm LEFT JOIN `order` o ON mm.id = o.member_id WHERE o.on_share_code = '"+onShareCode+"' AND mm.share_code IS NOT NULL GROUP BY mm.id ) dsb LEFT JOIN member dsb1 ON dsb1.link_share_code LIKE concat('%', dsb.share_code, '%')";
	//	String sql = "";

		String select ="SELECT count(dsb1.id) memeber_num, dsb.* ";
		//String	sqlExceptSelect=" FROM ( SELECT count(o.id) order_num, mm.*, sum(o.price) price_num FROM ( SELECT m.id, m.avatar,m.on_share_code, m.share_code, m.create_date, m.we_chat_number, m.phone, m.nickname,m.phone_status, h.`name` housername  FROM member m LEFT JOIN houserkeeper h ON m.housekeeper_id = h.id WHERE m.on_share_code = '"+onShareCode+"'  and ( m.phone LIKE concat('%', '"+str+"', '%') or m.nickname LIKE concat('%', '"+str+"', '%') or m.we_chat_number LIKE concat('%', '"+str+"', '%')) ) mm LEFT JOIN `order` o ON mm.id = o.member_id WHERE mm.on_share_code = '"+onShareCode+"' AND mm.share_code IS NOT NULL GROUP BY mm.id ) dsb LEFT JOIN member dsb1 ON dsb1.link_share_code LIKE concat('%', dsb.share_code, '%') GROUP BY dsb.id";
		String	sqlExceptSelect=" FROM ( SELECT count(o.id) order_num, mm.*, sum(o.price) price_num FROM ( SELECT m.id, m.avatar,m.on_share_code, m.share_code, m.create_date, m.we_chat_number, m.phone, m.nickname,m.phone_status, h.`name` housername  FROM member m LEFT JOIN houserkeeper_grade h ON m.housekeeper_id = h.id WHERE m.on_share_code = '"+onShareCode+"'  and ( m.phone LIKE concat('%', '"+str+"', '%') or m.nickname LIKE concat('%', '"+str+"', '%') or m.we_chat_number LIKE concat('%', '"+str+"', '%')) ) mm LEFT JOIN (SELECT * from `order` where (type = 0 OR type IS NULL or type=2) AND on_share_code = '"+onShareCode+"' )  o ON mm.id = o.member_id WHERE mm.on_share_code = '"+onShareCode+"'   GROUP BY mm.id ) dsb LEFT JOIN member dsb1 ON dsb1.link_share_code LIKE concat('%', dsb.share_code, '%') GROUP BY dsb.id";

		return modelManager.paginate(pageable.getPageNumber(), pageable.getPageSize(), select, sqlExceptSelect);

	}




}