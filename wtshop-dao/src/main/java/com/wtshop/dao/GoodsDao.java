
package com.wtshop.dao;

import java.math.BigDecimal;
import java.util.*;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Record;
import com.wtshop.model.*;
import com.wtshop.util.SqlUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.wtshop.Filter;
import com.wtshop.Order;
import com.wtshop.Pageable;
import com.wtshop.Setting;
import com.wtshop.util.DateUtils;
import com.wtshop.util.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Dao - 货品
 * 
 * 
 */

public class GoodsDao extends BaseDao<Goods> {

	/**
	 * 构造方法
	 */
	public GoodsDao() {
		super(Goods.class);
	}


	/**
	 * 根据品牌查找goods
	 */

	public List<Goods> findByBrandId(Long brandId){
		if(brandId == null){
			return null;
		}else {
			String sql = " Select * from goods where brand_id = " + brandId;
			return modelManager.find(sql);
		}

	}


	/**
	 * 新品推荐
	 */
	public List<Goods> findLikeList(){

		String sql = "  select g.id,g.name,g.price,g.market_price,g.image,g.caption,g.sales,g.commission_rate from like_commend n left join goods g on n.goods_id = g.id where 1 = 1 and g.is_delete = 0 and g.is_marketable = 1";

		return modelManager.find(sql);

	}

	/**
	 * 获取满减促销商品列表
	 *
	 */
	public List<Goods> findGoodsByPromId(Long promId){
		if(promId == null){
			return  null;
		}else {
			String sql = " SELECT d.id,d.name,d.sn,d.price,d.market_price,d.image, u.id product_id, d.caption,d.create_date,c.name cname FROM goods_promotion g LEFT JOIN promotion p on g.promotions = p.id LEFT JOIN goods d ON g.goods = d.id LEFT JOIN product_category c on d.product_category_id =c.id left join product u on d.id = u.goods_id where 1 = 1 and g.promotions = " + promId;
			return modelManager.find(sql);
		}

	}

	/**
	 * 获取满减促销商品列表
	 *
	 */
	public Page<Goods> findGoodsByPromId(Long promId, Pageable pageable){
		if(promId == null){
			return  null;
		}else {
			String sql = "  FROM goods_promotion g LEFT JOIN promotion p on g.promotions = p.id LEFT JOIN goods d ON g.goods = d.id LEFT JOIN product_category c on d.product_category_id =c.id left join product u on d.id = u.goods_id where 1 = 1 and g.promotions = " + promId;
			String select = " SELECT d.id,d.name,d.sn,d.price,d.market_price,d.image, u.id product_id, d.caption,g.create_date,c.name cname ,d.findGoods,d.sales,g.commission_rate ";
			return modelManager.paginate(pageable.getPageNumber(), pageable.getPageSize(), select, sql);
		}

	}



	/**
	 * 根据用户id查找购物项
	 *
	 * @param id
	 *            购物项id
	 * @return 购物车，若不存在则返回null
	 */
	public List<Goods> findCartItemList(Long id) {
		String sql = "SELECT c.id item_id ,p.id product_id,g.id,g.attribute_value0, g.id goodsId ,g.area_id, g.image,g.`name`,g.caption, g.caption,g.price,g.market_price ,c.quantity FROM cart_item c left join product p on c.product_id = p.id left join goods g on p.goods_id = g.id WHERE cart_id = " + id ;
		sql += " order by c.modify_date desc ";
		return modelManager.find(sql);

	}


	/**
	 * 新品推荐
	 */
	public List<Goods> findNewGoodsList(){

		String sql = " select g.id,g.name,g.price,g.market_price,g.image,g.caption,g.sales,g.commission_rate from newGoods_commend n left join goods g on n.goods_id = g.id where 1 = 1 and g.is_delete = 0 and g.is_marketable = 1;";

		return modelManager.find(sql);

	}

	/**
	 * 新品推荐
	 */
	public List<Goods> findCharactersList(){

		String sql = " select g.id,g.name,g.price,g.market_price,g.image,g.caption,g.sales,g.commission_rate from character_commend n left join goods g on n.goods_id = g.id where 1 = 1 and g.is_delete = 0 and g.is_marketable = 1";

		return modelManager.find(sql);

	}	/**
	 * 新品推荐
	 */
	public List<Goods> recommendList(Long id){

		String sql = " SELECT g.id,g.name,g.image,i.`name` attribute_value1,g.caption,g.create_date FROM goods g LEFT JOIN interest_category i ON g.product_category_id = i.id LEFT JOIN member_interest_category m ON i.id = m.interest_category WHERE m.members = '"+id+"' and g.is_delete<>1 ORDER BY m.weights * ( m.weights / ( SELECT SUM(m.weights) FROM goods g LEFT JOIN interest_category i ON g.product_category_id = i.id LEFT JOIN member_interest_category m ON i.id = m.interest_category WHERE m.members = '"+id+"' )) DESC LIMIT 14";

		return modelManager.find(sql);
	}
	/**
	 * 新品推荐
	 */
	public List<Goods> remainingRecommendList(Long id){
		String sql = "SELECT g.id,g.name,g.image,i.`name` attribute_value1 ,g.caption,g.hits,g.create_date FROM goods g LEFT JOIN interest_category i ON g.product_category_id = i.id LEFT JOIN member_interest_category m ON i.id = m.interest_category WHERE (m.members <> "+id+" OR m.members IS NOT NULL) and g.is_delete<>1 ORDER BY RAND() LIMIT 3";
		return modelManager.find(sql);
	}

	/**
	 * 新品推荐
	 */
	public List<Goods> remainingRecommendList(int i){
		String sql = "SELECT g.id, g. NAME, g.image, i.`name` attribute_value1, g.caption, g.create_date, g.hits FROM goods g LEFT JOIN interest_category i ON g.product_category_id = i.id WHERE g.is_delete <> 1 ORDER BY hits DESC LIMIT "+i+"";
		return modelManager.find(sql);
	}


	/**
	 * 销量由高到低
	 */
	public Page<Goods> findSell(Pageable pageable, String keyword){
		String sql = "  FROM goods g LEFT JOIN product p ON g.id = p.goods_id LEFT JOIN (SELECT count(i.product_id)count,i.product_id,o.`status` from order_item i LEFT JOIN `order` o ON i.order_id = o.id GROUP BY i.product_id HAVING `status` in (5,9,10)) m ON m.product_id = p.id where 1 = 1  ";
		if( keyword != null){
			sql += " AND (g.name like '%"+ keyword +"%' OR g.caption like '%"+ keyword +"%' )";
		}

		sql += " ORDER BY count DESC";

		String select = " SELECT g.id,g.name,g.price,g.market_price,g.image,g.caption,g.product_category_id  ";

		return modelManager.paginate(pageable.getPageNumber(), pageable.getPageSize(), select, sql);
	}


	/**
	 * 评论由高到低
	 */
	public Page<Goods> findReview(Pageable pageable, String keyword){

		String sql = "  from goods g LEFT JOIN (SELECT count(*) count,goods_id from review group by goods_id ) m ON g.id = m.goods_id where 1 = 1 ";

		if( keyword != null){
			sql += " AND (g.name like '%"+ keyword +"%' OR g.caption like '%"+ keyword +"%' )";
		}

		sql += " ORDER BY m.count desc ";

		String select = " SELECT g.id,g.name,g.price,g.market_price,g.image,g.caption,g.product_category_id  ";

		return modelManager.paginate(pageable.getPageNumber(), pageable.getPageSize(), select, sql);

	}



	/**
	 * 综合排序
	 */
	public Page<Goods> findTime(Pageable pageable, String keyword){

		String sql = "  from goods g where 1 = 1 and g.is_vip = 1 and g.is_delete = 0 ";

		if( keyword != null){
			sql += " AND (g.name like '%"+ keyword +"%' OR g.caption like '%"+ keyword +"%' )";
		}

		sql += " ORDER BY g.modify_date asc";

		String select = " SELECT g.id,g.name,g.price,g.market_price,g.image,g.caption,g.product_category_id  ";

		return modelManager.paginate(pageable.getPageNumber(), pageable.getPageSize(), select, sql);

	}


	/**
	 * 商品统计功能
	 */
	public Page<Record> findCount(Pageable pageable, String name){

		String sql  = " FROM vipGoods_history v LEFT JOIN goods g on v.goods_id = g.id where 1 = 1 and v.status = 2 and g.is_vip = 1 and g.is_delete = 0 and g.is_marketable = 1";
		String select = "SELECT sum(quantity) count,goods_id,goods_name,goods_price,g.create_date  ";

		if(name != null){
			sql += " and goods_name LIKE '%" + pageable.getSearchValue() +"%' " ;
		}

		sql += " GROUP BY v.goods_id ";

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
			ordersSQL = "ORDER BY count DESC";
		}
		sql += ordersSQL;

		return Db.paginate(pageable.getPageNumber(), pageable.getPageSize(), select, sql);


	}




	/**
	 * vip商品分页
	 */
	public Page<Goods> findVipPage(Pageable pageable){

		String sql = " From goods where 1 = 1 and is_vip = 1 and is_delete = 0 and is_marketable = 1 ";

		String select = " select price,image,name,id ";

		return modelManager.paginate(pageable.getPageNumber(), pageable.getPageSize(), select, sql);

	}


	/**
	 * 搜索功能
	 */
	public Page<Goods> search(Boolean is_vip, List<Long> productCategoryList, Long[] brandId, List<Long> areaList, Long[] functionId, String keyword, BigDecimal startPrice, BigDecimal endPrice, Pageable pageable, Boolean modify_date, Boolean sell, Boolean review, Boolean priceUp ,Boolean priceDown){
		Logger logger = LoggerFactory.getLogger("dddddd");
		String sqlExceptSelect = " from (select g.sales,g.hits,g.id,g.is_top, g.name,g.price,g.market_price,g.image,g.caption,g.product_category_id,g.is_delete,g.is_marketable,g.commission_rate,g.create_date FROM goods g left join goods_effct e on g.id = e.goods " +
				"LEFT JOIN (SELECT count(*) count,goods_id from review group by goods_id ) m ON g.id = m.goods_id " +
				"LEFT JOIN (SELECT count(p.goods_id)count,p.goods_id,o.`status`,p.stock from order_item i LEFT JOIN `order` o ON i.order_id = o.id " +
				"LEFT JOIN product p ON p.id =i.product_id GROUP BY p.goods_id ";
		if(sell){
			sqlExceptSelect += " AND o.`status` in (5,9,10) ";
		}
		sqlExceptSelect += " ) n ON n.goods_id =g.id LEFT JOIN product j ON j.goods_id = g.id WHERE 1 = 1 and g.is_delete = 0 ";
		String select = " select m.*,p.stock ";

		if(functionId != null){
			sqlExceptSelect += " AND e.effect in " + SqlUtils.getSQLIn(Arrays.asList(functionId));
		}
		if(keyword != null){
			sqlExceptSelect += " AND (g.name like '%"+ keyword +"%' OR g.caption like '%"+ keyword +"%'  OR g.keyword like '%"+ keyword +"%' )";
		}
		if(startPrice != null){
			sqlExceptSelect +=" AND g.price >= " + startPrice ;
		}
		if(endPrice != null){
			sqlExceptSelect +=" AND g.price <= " +endPrice ;
		}
		if(brandId != null){
			sqlExceptSelect += " AND g.brand_id in " + SqlUtils.getSQLIn(Arrays.asList(brandId));
		}
		if(areaList != null && areaList.size() > 0){
			sqlExceptSelect += " AND g.area_id in " + SqlUtils.getSQLIn(areaList);
		}

		sqlExceptSelect += " GROUP BY id ";

		if(modify_date){
			sqlExceptSelect += " ORDER BY g.modify_date ASC";
		}
		if(sell){
			sqlExceptSelect += " ORDER BY n.count DESC ";
		}
		if(review){
			sqlExceptSelect += " ORDER BY m.count DESC";
		}
		if(priceUp){
			sqlExceptSelect += " ORDER BY g.price ASC";
		}
		if(priceDown){
			sqlExceptSelect += " ORDER BY g.price DESC";
		}

		sqlExceptSelect +=" ) m LEFT JOIN product p ON m.id = p.goods_id where 1 = 1 ";

		if(productCategoryList != null && productCategoryList.size() > 0){
			sqlExceptSelect += " AND m.product_category_id IN " + SqlUtils.getSQLIn(productCategoryList);
		}
		if( !priceUp && !priceDown){
			sqlExceptSelect += " order by is_top desc ";
		}

		logger.info(sqlExceptSelect);
		return modelManager.paginate(pageable.getPageNumber(), pageable.getPageSize(), select, sqlExceptSelect);
	}

	
	/**
	 * 判断编号是否存在
	 * 
	 * @param sn
	 *            编号(忽略大小写)
	 * @return 编号是否存在
	 */
	public boolean snExists(String sn) {
		if (StringUtils.isEmpty(sn)) {
			return false;
		}

		String sql = "SELECT COUNT(*) FROM goods WHERE LOWER(sn) = LOWER(?)";
		Long count = Db.queryLong(sql, sn);
		return count > 0;
	}

	/**
	 * 根据订单id查找商品
	 */

	public List<Goods> findGoodsByItemId(Long id){

		String sql = " select g.id goods_id,g.market_price, g.name, g.caption ,g.image ,g.is_vip ,i.id order_itemId, i.quantity ,i.price ,i.is_review from order_item i LEFT JOIN product p ON i.product_id=p.id LEFT JOIN goods g on p.goods_id = g.id WHERE i.order_id= " + id;

		return modelManager.find(sql);

	}

	/**
	 * 根据订单id查找商品
	 */

	public List<Goods> findGoodsByOrderItemId(Long id){

		String sql = " select g.id,g.sales , g.name, g.caption ,g.image ,g.is_vip ,i.id order_itemId, i.quantity ,i.price ,i.is_review from order_item i LEFT JOIN product p ON i.product_id=p.id LEFT JOIN goods g on p.goods_id = g.id WHERE i.order_id= " + id;

		sql += " order by i.create_date desc ";

		return modelManager.find(sql);

	}

	/**
	 * 根据订单id查找商品
	 */

	public List<Goods> findGoodsByOrderId(Long orderId){

		String sql = "  SELECT g.id , g.name, g.caption ,g.image ,g.is_vip ,o.id order_itemId, o.quantity ,o.price  FROM order_item o LEFT JOIN product p ON o.product_id=p.id LEFT JOIN goods g on p.goods_id = g.id WHERE o.product_id NOT in ( SELECT t.product_id FROM `returns` r  LEFT JOIN returns_item t ON r.id =t.return_id where r.order_id = "+ orderId +" ) AND o.order_id = " +orderId;
		sql +=" order by o.create_date ";
		return modelManager.find(sql);

	}

	public List<Goods> findGoodsByPt(Long id){

		String sql = " select g.id , g.name, g.caption ,g.image ,g.is_vip ,g.market_price, i.quantity as 'check', i.id order_itemId, i.quantity ,i.price ,i.is_review from order_item i LEFT JOIN product p ON i.product_id=p.id LEFT JOIN goods g on p.goods_id = g.id WHERE i.order_id= " + id;

		return modelManager.find(sql);

	}

	/**
	 * 根据专题id获取商品信息
	 */
	public List<Goods> findGoodsByGoodsThemeId(Long id){
		String sql = "SELECT g.id,g.name,g.price,g.market_price,g.image from theme_product t LEFT JOIN product p on t.product_id = p.id LEFT JOIN goods g on p.goods_id = g.id WHERE t.goodsTheme_id =" + id;

		return modelManager.find(sql);
	}

	/**
	 * 根据购物项id查找商品
	 */

	public Goods findGoodsByCartItemId(Long id){

		String sql = "SELECT g.id,p.price,c.quantity,p.specification_values,g.image,g.area_id,g.`name`,g.caption ,g.product_category_id from cart_item c LEFT JOIN product p ON c.product_id = p.id LEFT JOIN goods g ON p.goods_id = g.id WHERE c.id = " + id;

		return modelManager.findFirst(sql);

	}

	/**
	 * 根据购物车id查找商品
	 */
	public Goods findGoodsByCartId(Long id){

		String sql = " select g.* from cart c LEFT JOIN cart_item i on c.id = i.cart_id LEFT JOIN product p on i.product_id = p.id LEFT JOIN goods g on p.goods_id = g.id WHERE c.id ="+id;
		return modelManager.findFirst(sql);
	}



	/**
	 * 获取抢拍商品信息
	 */

	public List<Goods> findGoodsByFlashType(){

		String sql = " SELECT g.id,g.name,g.price,g.market_price,g.image from goods g LEFT JOIN product p on p.goods_id = g.id LEFT JOIN flashsale_detail d on p.id = d.product_id LEFT JOIN flashsale f on f.id = d.flashsale_id WHERE f.type = 1 and g.is_delete = 0 and g.is_marketable = 1 ";
		return modelManager.find(sql);

	}

	/**
	 * 根据productId查询商品信息
	 */

	public Goods findGoodsByPro(Long productId){

		String sql = " SELECT g.* FROM goods g LEFT JOIN product p ON p.goods_id = g.id WHERE p.id  = " + productId;
		return modelManager.findFirst(sql);
	}

	/**
	 * 根据productId查询商品信息
	 */

	public List<Goods> findGoodsByProduct(Long productId){

		String sql = " SELECT g.id,g.name,g.price,g.market_price,g.image FROM goods g LEFT JOIN product p ON p.goods_id = g.id WHERE p.id  = " + productId;
		return modelManager.find(sql);
	}

	/**
	 * 根据productId查询商品信息
	 */

	public Goods findGoodsByProductId(Long fudaid){

		String sql = "  SELECT p.id,g.name,g.price,g.market_price,g.image FROM fudai_product f LEFT JOIN product p ON f.product_id = p.id LEFT JOIN goods g on p.goods_id = g.id WHERE f.id = " + fudaid;
		return modelManager.findFirst(sql);
	}

	/**
	 * 根据倒拍查询商品信息
	 */

	public List<Goods> findGoodsByReverseId(Long id){

		String sql = " SELECT g.id,g.name,g.price,g.market_price,g.image FROM goods g LEFT JOIN product p ON p.goods_id = g.id LEFT JOIN reverse_auction_detail r ON r.product_id = p.id where r.reverse_auction_id = " + id ;
		return modelManager.find(sql);
	}

	/**
	 * 根据goods id 查询商品信息
	 */
	public Goods findGoodsById(Long id){
		String sql = " select g.*,p.stock from goods g left join  product p on g.id = p.goods_id where 1 = 1 ";
		if( id != null){
			sql += "AND g.id = " + id ;
		}
		return modelManager.findFirst(sql);

	}

	/**
	 * 凑单shangpin
	 */
	public Page<Goods> findGoods(String price, Pageable pageable){

		if(price == null){
			return  null;
		}
		String sql = " from goods g left join product p on g.id = p.goods_id where 1 = 1  and g.is_delete = 0 and g.is_marketable = 1 ";
		String select = " select g.id,g.name,g.price,g.market_price,g.image,g.sales, p.id product_id,g.commission_rate  ";
		if(price != null){
			sql += " AND g.price <= " + price;
		}
		return modelManager.paginate(pageable.getPageNumber(), pageable.getPageSize(), select, sql);




	}


	/**
	 * 查询商品信息
	 *
	 * @param id
	 *
	 * @return
	 */
	public Goods findGoods(Long id) {

		String sql = "SELECT g.id,g.name,g.price,g.market_price,g.image from goods g left join product p on g.id = p.goods_id left join theme_product t on p.id = t.product_id where t.id = ?" ;

		return modelManager.findFirst(sql,id);
	}

	/**
	 * 根据编号查找货品
	 * 
	 * @param sn
	 *            编号(忽略大小写)
	 * @return 货品，若不存在则返回null
	 */
	public Goods findBySn(String sn) {
		if (StringUtils.isEmpty(sn)) {
			return null;
		}

		String sql = "SELECT * FROM goods WHERE LOWER(sn) = LOWER(?)";
		try {
			return modelManager.findFirst(sql, sn);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 查找货品
	 * 
	 * @param type
	 *            类型
	 * @param productCategory
	 *            商品分类
	 * @param brand
	 *            品牌
	 * @param promotion
	 *            促销
	 * @param tag
	 *            标签
	 * @param attributeValueMap
	 *            属性值Map
	 * @param startPrice
	 *            最低价格
	 * @param endPrice
	 *            最高价格
	 * @param isMarketable
	 *            是否上架
	 * @param isList
	 *            是否列出
	 * @param isTop
	 *            是否置顶
	 * @param isOutOfStock
	 *            是否缺货
	 * @param isStockAlert
	 *            是否库存警告
	 * @param hasPromotion
	 *            是否存在促销
	 * @param orderType
	 *            排序类型
	 * @param count
	 *            数量
	 * @param filters
	 *            筛选
	 * @param orders
	 *            排序
	 * @return 货品
	 */
	public List<Goods> findList(Goods.Type type, ProductCategory productCategory, Brand brand, Promotion promotion, Tag tag, Map<Attribute, String> attributeValueMap, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isOutOfStock,
			Boolean isStockAlert, Boolean hasPromotion, Goods.OrderType orderType, Integer count, List<Filter> filters, List<Order> orders) {
		String sql = "SELECT * FROM goods g WHERE 1 = 1 ";
		if (type != null) {
			sql += " AND g.type = " + type.ordinal();
		}
		if (productCategory != null) {
			sql += " AND EXISTS (SELECT 1 FROM product_category p WHERE g.product_category_id = p.id AND (p.`tree_path` LIKE '%" + ProductCategory.TREE_PATH_SEPARATOR + productCategory.getId() + ProductCategory.TREE_PATH_SEPARATOR + "%' OR g.product_category_id = "+ productCategory.getId() +")) ";
		}
		if (brand != null) {
			sql += " AND g.brand_id = " + brand.getId();
		}
		if (promotion != null) {
			sql += " AND EXISTS (SELECT 1 from goods_promotion gp WHERE gp.`goods` = g.`id` AND gp.promotions = " + promotion.getId() + ") ";
		}
		if (tag != null) {
			sql += " AND EXISTS (select 1 from goods_tag gt WHERE gt.`goods` = g.`id` AND gt.tags = " + tag.getId() + ") ";
		}
		if (attributeValueMap != null) {
			for (Map.Entry<Attribute, String> entry : attributeValueMap.entrySet()) {
				String propertyName = "attribute_value" + entry.getKey().getPropertyIndex();
				sql += " AND " + propertyName + " = '" + entry.getValue()+"'";
			}
		}
		if (startPrice != null && endPrice != null && startPrice.compareTo(endPrice) > 0) {
			BigDecimal temp = startPrice;
			startPrice = endPrice;
			endPrice = temp;
		}
		if (startPrice != null && startPrice.compareTo(BigDecimal.ZERO) >= 0) {
			sql += " AND g.price => " + startPrice;
		}
		if (endPrice != null && endPrice.compareTo(BigDecimal.ZERO) >= 0) {
			sql += " AND g.price <= " + endPrice ;
		}
		if (isMarketable != null) {
			sql += " AND g.is_marketable = " + isMarketable;
		}
		if (isList != null) {
			sql += " AND g.is_list = " + isList;
		}
		if (isTop != null) {
			sql += " AND g.is_top = " + isTop;
		}
		if (isOutOfStock != null) {
			String subquery = "";
			if (isOutOfStock) {
				subquery += "SELECT 1 FROM product p1 WHERE p1.`goods_id` = g.id AND p1.`stock` <= p1.`allocated_stock`";
			} else {
				subquery += "SELECT 1 FROM product p1 WHERE p1.`goods_id` = g.id AND p1.`stock` > p1.`allocated_stock`";
			}
			sql += "AND EXISTS (" + subquery + ") ";
		}
		if (isStockAlert != null) {
			String subquery = "";
			Setting setting = SystemUtils.getSetting();
			if (isStockAlert) {
				subquery += "SELECT 1 FROM product p1 WHERE p1.`goods_id` = g.id AND p1.`stock` <= p1.`allocated_stock` + " + setting.getStockAlertCount();
			} else {
				subquery += "SELECT 1 FROM product p1 WHERE p1.`goods_id` = g.id AND p1.`stock` > p1.`allocated_stock` + " + setting.getStockAlertCount();
			}
			sql += "AND EXISTS (" + subquery + ") ";
		}
		if (hasPromotion != null) {
			sql += "AND EXISTS (SELECT 1 from goods_promotion gp WHERE gp.`goods` = g.`id` AND gp.promotions = " + promotion.getId() + ") ";
		}
		if (orderType != null) {
			switch (orderType) {
			case topDesc:
				orders.add(new Order("is_top", Order.Direction.desc));
				orders.add(new Order("create_date", Order.Direction.desc));
				break;
			case priceAsc:
				orders.add(new Order("price", Order.Direction.asc));
				orders.add(new Order("create_date", Order.Direction.desc));
				break;
			case priceDesc:
				orders.add(new Order("price", Order.Direction.desc));
				orders.add(new Order("create_date", Order.Direction.desc));
				break;
			case salesDesc:
				orders.add(new Order("sales", Order.Direction.desc));
				orders.add(new Order("create_date", Order.Direction.desc));
				break;
			case scoreDesc:
				orders.add(new Order("score", Order.Direction.desc));
				orders.add(new Order("create_date", Order.Direction.desc));
				break;
			case dateDesc:
				orders.add(new Order("create_date", Order.Direction.desc));
				break;
			}
		} else if (CollectionUtils.isEmpty(orders)) {
			orders.add(new Order("is_top", Order.Direction.desc));
			orders.add(new Order("create_date", Order.Direction.desc));
		}
		return super.findList(sql, null, count, filters, orders);
	}

	/**
	 * 查找货品
	 * 
	 * @param productCategory
	 *            商品分类
	 * @param isMarketable
	 *            是否上架
	 * @param generateMethod
	 *            静态生成方式
	 * @param beginDate
	 *            起始日期
	 * @param endDate
	 *            结束日期
	 * @param first
	 *            起始记录
	 * @param count
	 *            数量
	 * @return 货品
	 */
	public List<Goods> findList(ProductCategory productCategory, Boolean isMarketable, Goods.GenerateMethod generateMethod, Date beginDate, Date endDate, Integer first, Integer count) {
		String sql = "SELECT * FROM goods g WHERE 1 = 1 ";
		if (productCategory != null) {
			sql += " AND EXISTS (SELECT 1 FROM product_category p WHERE g.product_category_id = p.id AND (p.`tree_path` LIKE '%" + ProductCategory.TREE_PATH_SEPARATOR + productCategory.getId() + ProductCategory.TREE_PATH_SEPARATOR + "%' OR g.product_category_id = "+ productCategory.getId() +")) ";
		}
		if (isMarketable != null) {
			sql += " AND is_marketable = " + isMarketable;
		}
		if (generateMethod != null) {
			sql += " AND generate_method = " + generateMethod;
		}
		if (beginDate != null) {
			sql += " AND create_date >= '" + DateUtils.formatDateTime(beginDate) + "' ";
		}
		if (endDate != null) {
			sql += " AND create_date <= '" + DateUtils.formatDateTime(endDate) + "' ";
		}
		return super.findList(sql, first, count, null, null);
	}

	/**
	 * 查找货品分页
	 * 
	 * @param type
	 *            类型
	 * @param productCategory
	 *            商品分类
	 * @param brand
	 *            品牌
	 * @param promotion
	 *            促销
	 * @param tag
	 *            标签
	 * @param attributeValueMap
	 *            属性值Map
	 * @param startPrice
	 *            最低价格
	 * @param endPrice
	 *            最高价格
	 * @param isMarketable
	 *            是否上架
	 * @param isList
	 *            是否列出
	 * @param isTop
	 *            是否置顶
	 * @param isOutOfStock
	 *            是否缺货
	 * @param isStockAlert
	 *            是否库存警告
	 * @param hasPromotion
	 *            是否存在促销
	 * @param orderType
	 *            排序类型
	 * @param pageable
	 *            分页信息
	 * @return 货品分页
	 */
	public Page<Goods> findPage(Boolean is_vip, Boolean is_delete, Goods.Type type, ProductCategory productCategory, Brand brand, Promotion promotion, Tag tag, Map<Attribute, String> attributeValueMap, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isOutOfStock,
			Boolean isStockAlert, Boolean hasPromotion, Goods.OrderType orderType, Pageable pageable) {
		String sqlExceptSelect = "FROM goods g WHERE 1 = 1  ";

		if(is_delete != null){
			sqlExceptSelect += " AND g.is_delete = " + is_delete;
		}

		if (type != null) {
			sqlExceptSelect += " AND g.type = " + type.ordinal();
		}
		if(is_vip != null){
			sqlExceptSelect += " AND is_vip is " + is_vip ;
		}
		if (productCategory != null) {
			sqlExceptSelect += " AND EXISTS (SELECT 1 FROM product_category p WHERE g.product_category_id = p.id AND (p.`tree_path` LIKE '%" + ProductCategory.TREE_PATH_SEPARATOR + productCategory.getId() + ProductCategory.TREE_PATH_SEPARATOR + "%' OR g.product_category_id = " + productCategory.getId() +" )) ";
			//sqlExceptSelect += " AND  g.product_category_id = " + productCategory.getId() +"  ";
		}
		if (brand != null) {
			sqlExceptSelect += " AND brand_id = " + brand.getId();
		}
		if (promotion != null) {
			sqlExceptSelect += " AND EXISTS (SELECT 1 from goods_promotion gp WHERE gp.`goods` = g.`id` AND gp.promotions = " + promotion.getId() + ") ";
		}
		if (tag != null) {
			sqlExceptSelect += " AND EXISTS (select 1 from goods_tag gt WHERE gt.`goods` = g.`id` AND gt.tags = " + tag.getId() + ") ";
		}
		if (MapUtils.isNotEmpty(attributeValueMap)) {
			for (Map.Entry<Attribute, String> entry : attributeValueMap.entrySet()) {
				String propertyName = "attribute_value" + entry.getKey().getPropertyIndex();
				sqlExceptSelect += "AND " + propertyName + " = '" + entry.getValue()+"'";
			}
		}
		if (startPrice != null && endPrice != null && startPrice.compareTo(endPrice) > 0) {
			BigDecimal temp = startPrice;
			startPrice = endPrice;
			endPrice = temp;
		}
		if (startPrice != null && startPrice.compareTo(BigDecimal.ZERO) >= 0) {
			sqlExceptSelect += " AND g.price >= " + startPrice;
		}
		if (endPrice != null && endPrice.compareTo(BigDecimal.ZERO) >= 0) {
			sqlExceptSelect += " AND g.price <= " + endPrice;
		}
		if (isMarketable != null) {
			sqlExceptSelect += " AND g.is_marketable = " + isMarketable;
		}
		if (isList != null) {
			sqlExceptSelect += " AND g.is_list = " + isList;
		}
		if (isTop != null) {
			sqlExceptSelect += " AND g.is_top = " + isTop;
		}
		if (isOutOfStock != null) {
			String subquery = "";
			if (isOutOfStock) {
				subquery += "SELECT 1 FROM product p1 WHERE p1.`goods_id` = g.id AND p1.`stock` <= p1.`allocated_stock`";
			} else {
				subquery += "SELECT 1 FROM product p1 WHERE p1.`goods_id` = g.id AND p1.`stock` > p1.`allocated_stock`";
			}
			sqlExceptSelect += " AND EXISTS (" + subquery + ") ";
		}
		if (isStockAlert != null) {
			String subquery = "";
			Setting setting = SystemUtils.getSetting();
			if (isStockAlert) {
				subquery += "SELECT 1 FROM product p1 WHERE p1.`goods_id` = g.id AND p1.`stock` <= p1.`allocated_stock` + " + setting.getStockAlertCount();
			} else {
				subquery += "SELECT 1 FROM product p1 WHERE p1.`goods_id` = g.id AND p1.`stock` > p1.`allocated_stock` + " + setting.getStockAlertCount();
			}
			sqlExceptSelect += " AND EXISTS (" + subquery + ") ";
		}
		if (hasPromotion != null) {
			sqlExceptSelect += "AND EXISTS (SELECT 1 from goods_promotion gp WHERE gp.`goods` = g.`id` AND gp.promotions = " + promotion.getId() + ") ";
		}
		List<Order> orders = new ArrayList<Order>();
		if (orderType != null) {
			switch (orderType) {
			case topDesc:
				orders.add(new Order("is_top", Order.Direction.desc));
				orders.add(new Order("create_date", Order.Direction.desc));
				break;
			case priceAsc:
				orders.add(new Order("price", Order.Direction.asc));
				orders.add(new Order("create_date", Order.Direction.desc));
				break;
			case priceDesc:
				orders.add(new Order("price", Order.Direction.desc));
				orders.add(new Order("create_date", Order.Direction.desc));
				break;
			case salesDesc:
				orders.add(new Order("sales", Order.Direction.desc));
				orders.add(new Order("create_date", Order.Direction.desc));
				break;
			case scoreDesc:
				orders.add(new Order("score", Order.Direction.desc));
				orders.add(new Order("create_date", Order.Direction.desc));
				break;
			case dateDesc:
				orders.add(new Order("create_date", Order.Direction.desc));
				break;
			}
		} else if (pageable == null || ((StringUtils.isEmpty(pageable.getOrderProperty()) || pageable.getOrderDirection() == null) && (CollectionUtils.isEmpty(pageable.getOrders())))) {
			orders.add(new Order("is_top", Order.Direction.desc));
			orders.add(new Order("create_date", Order.Direction.desc));
		}
		pageable.setOrders(orders);
		return super.findPage(sqlExceptSelect, pageable);
	}

	public Page<Goods> findPage(Boolean is_vip, Boolean is_delete, Goods.Type type, List<Long> productCategory, Brand brand, Promotion promotion, Tag tag, Map<Attribute, String> attributeValueMap, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isOutOfStock,
								Boolean isStockAlert, Boolean hasPromotion, Goods.OrderType orderType, Pageable pageable) {
		String sqlExceptSelect = "FROM goods g WHERE 1 = 1 ";
		String select = "select g.id,g.name,g.price,g.market_price,g.image,g.caption,g.product_category_id ";

		if(is_delete != null){
			sqlExceptSelect += " AND g.is_delete = " + is_delete;
		}

		if (type != null) {
			sqlExceptSelect += " AND g.type = " + type.ordinal();
		}
		if(is_vip != null){
			sqlExceptSelect += " AND is_vip is " + is_vip ;
		}
		if (productCategory.size() != 0) {
			String join = StringUtils.join(productCategory.toArray(), ",");
			String list = "(" + join + ")";

			sqlExceptSelect += " AND  g.product_category_id in " + list +"  ";
		}
		if (brand != null) {
			sqlExceptSelect += " AND brand_id = " + brand.getId();
		}
		if (promotion != null) {
			sqlExceptSelect += " AND EXISTS (SELECT 1 from goods_promotion gp WHERE gp.`goods` = g.`id` AND gp.promotions = " + promotion.getId() + ") ";
		}
		if (tag != null) {
			sqlExceptSelect += " AND EXISTS (select 1 from goods_tag gt WHERE gt.`goods` = g.`id` AND gt.tags = " + tag.getId() + ") ";
		}
		if (MapUtils.isNotEmpty(attributeValueMap)) {
			for (Map.Entry<Attribute, String> entry : attributeValueMap.entrySet()) {
				String propertyName = "attribute_value" + entry.getKey().getPropertyIndex();
				sqlExceptSelect += "AND " + propertyName + " = '" + entry.getValue()+"'";
			}
		}
		if (startPrice != null && endPrice != null && startPrice.compareTo(endPrice) > 0) {
			BigDecimal temp = startPrice;
			startPrice = endPrice;
			endPrice = temp;
		}
		if (startPrice != null && startPrice.compareTo(BigDecimal.ZERO) >= 0) {
			sqlExceptSelect += " AND g.price >= " + startPrice;
		}
		if (endPrice != null && endPrice.compareTo(BigDecimal.ZERO) >= 0) {
			sqlExceptSelect += " AND g.price <= " + endPrice;
		}
		if (isMarketable != null) {
			sqlExceptSelect += " AND g.is_marketable = " + isMarketable;
		}
		if (isList != null) {
			sqlExceptSelect += " AND g.is_list = " + isList;
		}
		if (isTop != null) {
			sqlExceptSelect += " AND g.is_top = " + isTop;
		}
		if (isOutOfStock != null) {
			String subquery = "";
			if (isOutOfStock) {
				subquery += "SELECT 1 FROM product p1 WHERE p1.`goods_id` = g.id AND p1.`stock` <= p1.`allocated_stock`";
			} else {
				subquery += "SELECT 1 FROM product p1 WHERE p1.`goods_id` = g.id AND p1.`stock` > p1.`allocated_stock`";
			}
			sqlExceptSelect += " AND EXISTS (" + subquery + ") ";
		}
		if (isStockAlert != null) {
			String subquery = "";
			Setting setting = SystemUtils.getSetting();
			if (isStockAlert) {
				subquery += "SELECT 1 FROM product p1 WHERE p1.`goods_id` = g.id AND p1.`stock` <= p1.`allocated_stock` + " + setting.getStockAlertCount();
			} else {
				subquery += "SELECT 1 FROM product p1 WHERE p1.`goods_id` = g.id AND p1.`stock` > p1.`allocated_stock` + " + setting.getStockAlertCount();
			}
			sqlExceptSelect += " AND EXISTS (" + subquery + ") ";
		}
		if (hasPromotion != null) {
			sqlExceptSelect += "AND EXISTS (SELECT 1 from goods_promotion gp WHERE gp.`goods` = g.`id` AND gp.promotions = " + promotion.getId() + ") ";
		}
		List<Order> orders = new ArrayList<Order>();
		if (orderType != null) {
			switch (orderType) {
				case topDesc:
					orders.add(new Order("is_top", Order.Direction.desc));
					orders.add(new Order("create_date", Order.Direction.desc));
					break;
				case priceAsc:
					orders.add(new Order("price", Order.Direction.asc));
					orders.add(new Order("create_date", Order.Direction.desc));
					break;
				case priceDesc:
					orders.add(new Order("price", Order.Direction.desc));
					orders.add(new Order("create_date", Order.Direction.desc));
					break;
				case salesDesc:
					orders.add(new Order("sales", Order.Direction.desc));
					orders.add(new Order("create_date", Order.Direction.desc));
					break;
				case scoreDesc:
					orders.add(new Order("score", Order.Direction.desc));
					orders.add(new Order("create_date", Order.Direction.desc));
					break;
				case dateDesc:
					orders.add(new Order("create_date", Order.Direction.desc));
					break;
			}
		} else if (pageable == null || ((StringUtils.isEmpty(pageable.getOrderProperty()) || pageable.getOrderDirection() == null) && (CollectionUtils.isEmpty(pageable.getOrders())))) {
			orders.add(new Order("is_top", Order.Direction.desc));
			orders.add(new Order("create_date", Order.Direction.desc));
		}
		pageable.setOrders(orders);
		return modelManager.paginate(pageable.getPageNumber(), pageable.getPageSize(), select, sqlExceptSelect);
	}



	public Page<Goods> findPage(Pageable pageable, List<Long> goodsList) {
		String sqlExceptSelect = " FROM goods WHERE 1 = 1 and is_vip = 0 and is_delete = 0  ";
		if( goodsList != null && goodsList.size() > 0){

			sqlExceptSelect += " and id NOT IN " + SqlUtils.getSQLIn(goodsList) ;
		}


		return super.findPage(sqlExceptSelect, pageable);
	}

	/**
	 * 查找货品分页
	 * 
	 * @param rankingType
	 *            排名类型
	 * @param pageable
	 *            分页信息
	 * @return 货品分页
	 */
	public Page<Goods> findPage(Goods.RankingType rankingType, Pageable pageable) {
		String sqlExceptSelect = "FROM goods WHERE 1 = 1 and is_delete = 0  ";
		List<Order> orderList = new ArrayList<Order>();
		if (rankingType != null) {
			switch (rankingType) {
			case score:
				orderList.add(new Order("score", Order.Direction.desc));
				orderList.add(new Order("score_count", Order.Direction.desc));
				break;
			case scoreCount:
				orderList.add(new Order("score_count", Order.Direction.desc));
				orderList.add(new Order("score", Order.Direction.desc));
				break;
			case weekHits:
				orderList.add(new Order("week_hits", Order.Direction.desc));
				break;
			case monthHits:
				orderList.add(new Order("month_hits", Order.Direction.desc));
				break;
			case hits:
				orderList.add(new Order("hits", Order.Direction.desc));
				break;
			case weekSales:
				orderList.add(new Order("week_sales", Order.Direction.desc));
				break;
			case monthSales:
				orderList.add(new Order("month_sales", Order.Direction.desc));
				break;
			case sales:
				orderList.add(new Order("sales", Order.Direction.desc));
				break;
			}
		}
		return super.findPage(sqlExceptSelect, pageable);
	}

	/**
	 * 查找收藏货品分页
	 * 
	 * @param member
	 *            会员
	 * @param pageable
	 *            分页信息
	 * @return 收藏货品分页
	 */
	public Page<Goods> findPage(Member member, Pageable pageable,  String keyword ) {
		if (member == null) {
			return null;
		}
		String sqlExceptSelect = "FROM member_favorite_goods fg LEFT join `goods` g ON fg.`favorite_goods` = g.`id` WHERE 1 = 1 and g.is_delete = 0 and fg.`favorite_members` = " + member.getId();
		String select = " select * ";
		if(keyword != null){
			 sqlExceptSelect += " AND (g.name like '%"+ keyword +"%' OR g.caption like '%"+ keyword +"%'  OR g.keyword like '%"+ keyword +"%' )";
		}

		sqlExceptSelect += " order by fg.create_date desc";
		return modelManager.paginate(pageable.getPageNumber(), pageable.getPageSize(), select, sqlExceptSelect);
	}

	/**
	 * 查询货品数量
	 * 
	 * @param type
	 *            类型
	 * @param favoriteMember
	 *            收藏会员
	 * @param isMarketable
	 *            是否上架
	 * @param isList
	 *            是否列出
	 * @param isTop
	 *            是否置顶
	 * @param isOutOfStock
	 *            是否缺货
	 * @param isStockAlert
	 *            是否库存警告
	 * @return 货品数量
	 */
	public Long count(Goods.Type type, Member favoriteMember, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isOutOfStock, Boolean isStockAlert) {
		String sql = "FROM goods g WHERE 1 = 1 and g.is_delete = 0 ";
		if (type != null) {
			sql += " AND g.type = " + type.ordinal();
		}
		if (favoriteMember != null) {
			sql += " AND EXISTS (SELECT 1 FROM member_favorite_goods mfg WHERE mfg.favorite_goods = g.id AND mfg.favorite_members = " + favoriteMember.getId() + ") ";
		}
		if (isMarketable != null) {
			sql += " AND g.is_marketable = " + isMarketable;
		}
		if (isList != null) {
			sql += " AND g.is_list = " + isList;
		}
		if (isTop != null) {
			sql += " AND g.is_top = " + isTop;
		}
		if (isOutOfStock != null) {
			String subquery = "";
			if (isOutOfStock) {
				subquery += "SELECT 1 FROM product p1 WHERE p1.`goods_id` = g.id AND p1.`stock` <= p1.`allocated_stock`";
			} else {
				subquery += "SELECT 1 FROM product p1 WHERE p1.`goods_id` = g.id AND p1.`stock` > p1.`allocated_stock`";
			}
			sql += "AND EXISTS (" + subquery + ") ";
		}
		if (isStockAlert != null) {
			String subquery = "";
			Setting setting = SystemUtils.getSetting();
			if (isStockAlert) {
				subquery += "SELECT 1 FROM product p1 WHERE p1.`goods_id` = g.id AND p1.`stock` <= p1.`allocated_stock` + " + setting.getStockAlertCount();
			} else {
				subquery += "SELECT 1 FROM product p1 WHERE p1.`goods_id` = g.id AND p1.`stock` > p1.`allocated_stock` + " + setting.getStockAlertCount();
			}
			sql += " AND EXISTS ( " + subquery + " ) ";
		}
		return super.count(sql);
	}

	/**
	 * 清空货品属性值
	 * 
	 * @param attribute
	 *            属性
	 */
	public void clearAttributeValue(Attribute attribute) {
		if (attribute == null || attribute.getPropertyIndex() == null || attribute.getProductCategory() == null) {
			return;
		}
		String sql = "UPDATE goods SET attribute_value" + attribute.getPropertyIndex() + " = null WHERE product_category_id = ?";
		Db.update(sql, attribute.getProductCategoryId());
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

}