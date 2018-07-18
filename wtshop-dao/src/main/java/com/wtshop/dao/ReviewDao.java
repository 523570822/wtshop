package com.wtshop.dao;

import java.util.List;

import com.jfinal.kit.LogKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.wtshop.Filter;
import com.wtshop.Order;
import com.wtshop.Pageable;
import com.wtshop.model.Goods;
import com.wtshop.model.Member;
import com.wtshop.model.Review;
import com.wtshop.util.Assert;
import org.apache.commons.lang3.StringUtils;

/**
 * Dao - 评论
 * 
 * 
 */
public class ReviewDao extends BaseDao<Review> {
	
	/**
	 * 构造方法
	 */
	public ReviewDao() {
		super(Review.class);
	}
	
	/**
	 * 查找评论
	 * 
	 * @param member
	 *            会员
	 * @param goods
	 *            货品
	 * @param type
	 *            类型
	 * @param isShow
	 *            是否显示
	 * @param count
	 *            数量
	 * @param filters
	 *            筛选
	 * @param orders
	 *            排序
	 * @return 评论
	 */
	public List<Review> findList(Member member, Goods goods, Review.Type type, Boolean isShow, Integer count, List<Filter> filters, List<Order> orders) {
		String sql = "SELECT * FROM review WHERE 1 = 1 ";
		if (member != null) {
			sql += " AND member_id = " + member.getId();
		}
		if (goods != null) {
			sql += " AND goods_id = " + goods.getId();
		}
		if (type != null) {
			switch (type) {
			case positive:
				sql += " AND score = 0";
				break;
			case moderate:
				sql += " AND score = 1";
				break;
			case negative:
				sql += " AND score = 2";
				break;
			}
		}
		if (isShow != null) {
			sql += " AND is_show = " + isShow;
		}
		return super.findList(sql, null, count, filters, orders);
	}

	/**
	 * 查找评论分页
	 * 
	 * @param member
	 *            会员
	 * @param goods
	 *            货品
	 * @param type
	 *            类型
	 * @param isShow
	 *            是否显示
	 * @param pageable
	 *            分页信息
	 * @return 评论分页
	 */
	public Page<Review> findPage(Member member, Goods goods, Review.Type type, Boolean isShow, Pageable pageable) {
		String sqlExceptSelect = "FROM review WHERE 1 = 1 and order_id is null ";
		String select = "select * ";
		if (member != null) {
			sqlExceptSelect += " AND member_id = " + member.getId();
		}
		if (goods != null) {
			sqlExceptSelect += " AND goods_id = " + goods.getId();
		}
		if (type != null) {
			switch (type) {
			case positive:
				sqlExceptSelect += " AND score = 0";
				break;
			case moderate:
				sqlExceptSelect += " AND score = 1";
				break;
			case negative:
				sqlExceptSelect += " AND score = 2";
				break;
			}
		}
		if (isShow != null) {
			sqlExceptSelect += " AND is_show = " + isShow;
		}
		sqlExceptSelect += " AND for_review_id is null";
		// 过滤条件

		// 搜索属性、搜索值
		String searchProperty = pageable.getSearchProperty();
		String searchValue = pageable.getSearchValue();
			if (StringUtils.isNotEmpty(searchProperty) && StringUtils.isNotEmpty(searchValue)) {
				if(searchProperty.equals("status")){
					sqlExceptSelect += " AND " + searchProperty + " = " + StringUtils.trim(searchValue) + " ";
				}else {
					sqlExceptSelect += " AND " + searchProperty + " LIKE '%" + StringUtils.trim(searchValue) + "%' ";
				}

		}
		sqlExceptSelect += " order by create_date desc";
		return modelManager.paginate(pageable.getPageNumber(), pageable.getPageSize(), select, sqlExceptSelect);
	}


	/**
	 * 查找评论分页+会员信息
	 *
	 * @param member
	 *            会员
	 * @param goods
	 *            货品
	 * @param type
	 *            类型
	 * @param isShow
	 *            是否显示
	 * @param pageable
	 *            分页信息
	 * @return 评论分页
	 */
	public Page<Review> findPages(Member member, Goods goods, Review.Type type, Boolean isShow, Pageable pageable,String nickname, String name) {
		String sqlExceptSelect = "FROM review r left join member m on r.member_id = m.id left join goods g on r.goods_id = g.id WHERE 1 = 1 AND r.is_delete = 0 and r.order_id is null";
		String select = "select r.*,m.nickname,m.avatar ";
		if (member != null) {
			sqlExceptSelect += " AND member_id = " + member.getId();
		}
		if (goods != null) {
			sqlExceptSelect += " AND goods_id = " + goods.getId();
		}
		if( nickname != null ){
			sqlExceptSelect += " AND m.nickname LIKE '%" + nickname +"%' " ;
		}
		if( name != null ){
			sqlExceptSelect += " AND g.name LIKE '%" + name +"%' " ;
		}
		if (type != null) {
			switch (type) {
				case positive:
					sqlExceptSelect += " AND r.score in (5,4)";
					break;
				case moderate:
					sqlExceptSelect += " AND r.score in (2,3)";
					break;
				case negative:
					sqlExceptSelect += " AND r.score in (1)";
					break;
			}
		}
		if (isShow != null) {
			sqlExceptSelect += " AND r.is_show = " + isShow;
		}
		sqlExceptSelect += "  order by r.create_date desc";
		return modelManager.paginate(pageable.getPageNumber(), pageable.getPageSize(), select, sqlExceptSelect);
	}

	/**
	 * 查找评论分页+会员信息
	 *
	 * @param member
	 *            会员
	 * @param goods
	 *            货品
	 * @param type
	 *            类型
	 * @param isShow
	 *            是否显示
	 * @param pageable
	 *            分页信息
	 * @return 评论分页
	 */
	public Page<Record> findPageList(Member member, Goods goods, Review.Type type, Boolean isShow, Pageable pageable) {
		String sqlExceptSelect = "FROM review r left join member m on r.member_id = m.id WHERE 1 = 1 AND r.is_delete = 0";
		String select = "select r.*,m.nickname,m.avatar ";
		if (member != null) {
			sqlExceptSelect += " AND member_id = " + member.getId();
		}
		if (goods != null) {
			sqlExceptSelect += " AND goods_id = " + goods.getId();
		}
		if (type != null) {
			switch (type) {
				case positive:
					sqlExceptSelect += " AND score in (5,4)";
					break;
				case moderate:
					sqlExceptSelect += " AND score in (2,3)";
					break;
				case negative:
					sqlExceptSelect += " AND score in (1)";
					break;
			}
		}
		if (isShow != null) {
			sqlExceptSelect += " AND is_show = " + isShow;
		}
		sqlExceptSelect += "  order by r.create_date desc";
		return Db.paginate(pageable.getPageNumber(), pageable.getPageSize(), select, sqlExceptSelect);
	}


	/**
	 * 查找带图片的评论分页
	 *
	 * @param member
	 *            会员
	 * @param goods
	 *            货品
	 * @param isShow
	 *            是否显示
	 * @param pageable
	 *            分页信息
	 * @return 评论分页
	 */
	public Page<Review> findPage(Member member, Goods goods,  Boolean isShow, Pageable pageable) {
		String sqlExceptSelect = "FROM review r left join member m on r.member_id = m.id WHERE 1 = 1 AND IMAGES like '%/%'";
		String select = "select r.*,m.avatar,m.nickname";
		if (member != null) {
			sqlExceptSelect += " AND member_id = " + member.getId();
		}
		if (goods != null) {
			sqlExceptSelect += " AND goods_id = " + goods.getId();
		}
		if (isShow != null) {
			sqlExceptSelect += " AND is_show = " + isShow;
		}

		return modelManager.paginate(pageable.getPageNumber(), pageable.getPageSize(), select, sqlExceptSelect);
	}

	/**
	 * 查找评论数量
	 * 
	 * @param member
	 *            会员
	 * @param goods
	 *            货品
	 * @param type
	 *            类型
	 * @param isShow
	 *            是否显示
	 * @return 评论数量
	 */
	public Long count(Member member, Goods goods, Review.Type type, Boolean isShow) {
		String sqlExceptSelect = "FROM review WHERE 1 = 1 and is_delete = 0 ";
		if (member != null) {
			sqlExceptSelect += " AND member_id = " + member.getId();
		}
		if (goods != null) {
			sqlExceptSelect += " AND goods_id = " + goods.getId();
		}
		if (type != null) {
			switch (type) {
			case positive:
				sqlExceptSelect += " AND score in (4 ,5 )";
				break;
			case moderate:
				sqlExceptSelect += " AND score in (3, 2) ";
				break;
			case negative:
				sqlExceptSelect += " AND score in (1)";
				break;
			}
		}
		if (isShow != null) {
			sqlExceptSelect += " AND is_show = " + isShow;
		}
		return super.count(sqlExceptSelect);
	}

	/**
	 * 查找评论数量
	 *
	 * @param member
	 *            会员
	 * @param goods
	 *            货品
	 * @param isShow
	 *            是否显示
	 * @return 评论数量
	 */
	public List<Review> count(Member member, Goods goods, Boolean isShow) {
		String sqlExceptSelect = "select * FROM review WHERE 1 = 1 AND IMAGES IS NOT NULL";
		if (member != null) {
			sqlExceptSelect += " AND member_id = " + member.getId();
		}
		if (goods != null) {
			sqlExceptSelect += " AND goods_id = " + goods.getId();
		}
		if (isShow != null) {
			sqlExceptSelect += " AND is_show = " + isShow;
		}
		return modelManager.find(sqlExceptSelect);
	}


	/**
	 * 计算货品总评分
	 * 
	 * @param goods
	 *            货品
	 * @return 货品总评分，仅计算显示评论
	 */
	public long calculateTotalScore(Goods goods) {
		Assert.notNull(goods);

		String sql = "SELECT SUM(score) FROM review WHERE goods_id = ? AND is_show = ?";
		Long totalScore = Db.queryLong(sql, goods.getId(), true);
		return totalScore != null ? totalScore : 0L;
	}

	/**
	 * 计算货品评分次数
	 * 
	 * @param goods
	 *            货品
	 * @return 货品评分次数，仅计算显示评论
	 */
	public long calculateScoreCount(Goods goods) {
		Assert.notNull(goods);

		String sql = "SELECT COUNT(*) FROM review WHERE goods_id = ? AND is_show = ?";
		return Db.queryInt(sql, goods.getId(), true);
	}

}