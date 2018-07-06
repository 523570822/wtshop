package com.wtshop.service;

import java.util.Collections;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.jfinal.aop.Enhancer;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.wtshop.Filter;
import com.wtshop.Order;
import com.wtshop.Pageable;
import com.wtshop.Setting;
import com.wtshop.dao.*;
import com.wtshop.model.Goods;
import com.wtshop.model.Member;
import com.wtshop.model.OrderItem;
import com.wtshop.model.Review;
import com.wtshop.util.ApiResult;
import com.wtshop.util.Assert;
import com.wtshop.util.SystemUtils;

/**
 * Service - 评论
 * 
 * 
 */
public class ReviewService extends BaseService<Review> {

	/**
	 * 构造方法
	 */
	public ReviewService() {
		super(Review.class);
	}
	
	private ReviewDao reviewDao = Enhancer.enhance(ReviewDao.class);
	private MemberDao memberDao = Enhancer.enhance(MemberDao.class);
	private GoodsDao goodsDao = Enhancer.enhance(GoodsDao.class);
	private OrderDao orderDao = Enhancer.enhance(OrderDao.class);
	private OrderItemDao orderItemDao = Enhancer.enhance(OrderItemDao.class);
	private OrderItemService orderItemService =Enhancer.enhance(OrderItemService.class);

	/**
	 * 保存评论
	 */
	public ApiResult saveReview(Member member, String ip, Integer type, Boolean anonymous, Long id, Integer score, String content, String[] images){
		Setting setting = SystemUtils.getSetting();
		Review review = new Review();
		if(type == 0){
			review.setOrderScore(score);
			review.setOrderContent(content);
			review.setOrderImages(JSONArray.toJSONString(images));
			review.setIp(ip);
			review.setMemberId(member.getId());
			review.setProductId(null);
			review.setGoodsId(null);
			review.setOrderId(id);
			review.setIsDelete(false);
			review.setStatus(false);
			review.setIsAnonymous(anonymous);

		}else {
			review.setScore(score);
			review.setContent(content);
			review.setImages(JSONArray.toJSONString(images));
			review.setIp(ip);
			review.setMemberId(member.getId());
			review.setProductId(orderItemService.find(id).getProductId());
			review.setGoodsId(orderItemService.find(id).getProduct().getGoodsId());
			review.setOrderItemId(id);
			review.setIsAnonymous(anonymous);
			review.setIsDelete(false);
			review.setStatus(false);
		}

		if (setting.getIsReviewCheck()) {
			review.setIsShow(false);
			super.save(review);
		} else {
			review.setIsShow(true);
			super.save(review);
		}
		return ApiResult.success();
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
		return reviewDao.findList(member, goods, type, isShow, count, filters, orders);
	}

	/**
	 * 查找评论
	 * 
	 * @param memberId
	 *            会员ID
	 * @param goodsId
	 *            货品ID
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
	 * @param useCache
	 *            是否使用缓存
	 * @return 评论
	 */
	public List<Review> findList(Long memberId, Long goodsId, Review.Type type, Boolean isShow, Integer count, List<Filter> filters, List<Order> orders, boolean useCache) {
		Member member = memberDao.find(memberId);
		if (memberId != null && member == null) {
			return Collections.emptyList();
		}
		Goods goods = goodsDao.find(goodsId);
		if (goodsId != null && goods == null) {
			return Collections.emptyList();
		}
		return reviewDao.findList(member, goods, type, isShow, count, filters, orders);
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
		return reviewDao.findPages(member, goods, type, isShow, pageable,null,null);
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
	public Page<Record> findPageLists(Member member, Goods goods, Review.Type type, Boolean isShow, Pageable pageable) {
		return reviewDao.findPageList(member, goods, type, isShow, pageable);
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
	public Page<Review> findPageList(Member member, Goods goods, Review.Type type, Boolean isShow, Pageable pageable) {

		String searchProperty = pageable.getSearchProperty();
		String searchValue = pageable.getSearchValue();

		String name = null ;
		String nickname = null;
		 if("name".equals(searchProperty)){
			name = searchValue;
		}else if("nickname".equals(searchProperty)){
			nickname = searchValue;
		}

		return reviewDao.findPages(member, goods, type, isShow, pageable, nickname, name);
	}


	/**
	 * 查找评论分页
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
		return reviewDao.findPage(member, goods, isShow, pageable);
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
		return reviewDao.count(member, goods, type, isShow);
	}

	/**
	 * 查找评论  是否上传图片
	 *
	 * @param member
	 *            会员
	 * @param goods
	 *            货品
	 *
	 * @param isShow
	 *            是否显示
	 * @return 评论数量
	 */
	public List<Review> count(Member member, Goods goods,  Boolean isShow) {
		return reviewDao.count(member, goods, isShow);

	}
	/**
	 * 查找待评论的订单行
	 * 
	 * @param member
	 * 			会员
	 * @param isReview
	 * 			是否评价
	 * @param pageable
	 *            分页信息
	 * @return
	 * 		订单项分页
	 */
	public Page<OrderItem> findPendingOrderItems(Member member, Boolean isReview, Pageable pageable) {
		return orderItemDao.findPendingOrderItems(member, com.wtshop.model.Order.Status.completed, isReview, pageable);
	}
	
	/**
	 * 查找待评论数量
	 * 
	 * @param member
	 *            会员
	 *            是否显示
	 * @return 评论数量
	 */
	public Long count(Member member, Boolean isReview) {
		return orderItemDao.count(member, com.wtshop.model.Order.Status.completed, isReview);
	}
	
	/**
	 * 咨询回复
	 * 
	 *            回复咨询
	 */
	public void reply(Review review, Review replyReview) {
		if (review == null || replyReview == null) {
			return;
		}
		review.setIsShow(true);
		review.update();

		replyReview.setIsShow(true);
		replyReview.setProductId(review.getProductId());
		replyReview.setForReviewId(review.getId());
		reviewDao.save(replyReview);

		Goods goods = review.getProduct().getGoods();
		if (goods != null) {
			goods.setGenerateMethod(Goods.GenerateMethod.lazy.ordinal());
			goods.update();
		}
	}

	/**
	 * 判断是否拥有评论权限
	 * 
	 * @param member
	 *            会员
	 * @param goods
	 *            货品
	 * @return 是否拥有评论权限
	 */
	public boolean hasPermission(Member member, Goods goods) {
		Assert.notNull(member);
		Assert.notNull(goods);

		Setting setting = SystemUtils.getSetting();
		if (Setting.ReviewAuthority.purchased.equals(setting.getReviewAuthority())) {
			long reviewCount = reviewDao.count(member, goods, null, null);
			long orderCount = orderDao.count(null, com.wtshop.model.Order.Status.completed, member, goods, null, null, null, null, null, null);
			return orderCount > reviewCount;
		}
		return true;
	}

}