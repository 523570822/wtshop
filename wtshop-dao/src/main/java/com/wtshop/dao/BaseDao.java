package com.wtshop.dao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.jfinal.json.Json;
import com.jfinal.kit.LogKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.*;
import com.wtshop.Filter;
import com.wtshop.Order;
import com.wtshop.Pageable;
import com.wtshop.util.Assert;
import com.wtshop.util.GenericsUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * Dao - 基类
 *
 *
 */
public class BaseDao <M extends Model<M>> {

	/** "ID"属性名称 */
	public static final String ID = "id";

	/** "创建日期"属性名称 */
	public static final String CREATE_DATE = "create_date";

	/** "修改日期"属性名称 */
	public static final String MODIFY_DATE = "modify_date";

	/** "版本"属性名称 */
	public static final String VERSION = "version";

	/** 实体类类型 */
	private Class<M> modelClass;

	protected M modelManager;

	public Class<M> getModelClass() {
		return modelClass;
	}

	public void setModelClass(Class<M> modelClass) {
		this.modelClass = modelClass;
	}

	/**
	 * 构造方法
	 */
	@SuppressWarnings("unchecked")
	public BaseDao(Class<M> entityClass) {
		this.setModelClass(GenericsUtils.getSuperClassGenricType(entityClass));
		try {
			modelManager = modelClass.newInstance();
		} catch (InstantiationException e) {
			LogKit.error("instance model fail!" + e);
		} catch (IllegalAccessException e) {
			LogKit.error("instance model fail!" + e);
		}
	}

	public String getTableName() {
		Table table = TableMapping.me().getTable(getModelClass());
		return table.getName();
	}

	/**
	 * 查找实体对象
	 *
	 * @param id
	 *            ID
	 * @return 实体对象，若不存在则返回null
	 */
	public M find(Long id) {
		if (id == null) {
			return null;
		}
		return modelManager.findById(id);
	}

	// 根据sql获取实体类

	public  M findBySql(String sql){
		 return   (M)modelManager.findFirst(sql);
	}

	public  List<M> findListBySql(String sql){
		return    (List<M>)modelManager.find(sql);
	}

	/**
	 * 查找实体对象集合
	 *
	 * @param first
	 *            起始记录
	 * @param count
	 *            数量
	 * @param filters
	 *            筛选
	 * @param orders
	 *            排序
	 * @return 实体对象集合
	 */
	public List<M> findList(Integer first, Integer count, List<Filter> filters, List<Order> orders) {
		String sql = "SELECT * FROM `" + getTableName() + "` WHERE 1 = 1 ";
		return findList(sql, first, count, filters, orders);
	}

	/**
	 * 查找实体对象分页
	 *
	 * @param pageable
	 *            分页信息
	 * @return 实体对象分页
	 */
	public Page<M> findPage(Pageable pageable) {
		String sqlExceptSelect = "FROM `" + getTableName() + "` WHERE 1 = 1 ";

		return findPage(sqlExceptSelect, pageable);
	}

	public  M findByLast( ){
		String sql = "select * from  `" + getTableName() + "` order by id DESC limit 1";

		return   (M)modelManager.findFirst(sql);
	}
	/**
	 * 查询实体对象数量
	 *
	 * @param filters
	 *            筛选
	 * @return 实体对象数量
	 */
	public long count(Filter... filters) {
		String sql = "SELECT COUNT(*) FROM `" + getTableName() + "` WHERE 1 = 1 ";
		sql += getFilters(ArrayUtils.isNotEmpty(filters) ? Arrays.asList(filters) : null);
		return Db.queryLong(sql);
	}


	/**
	 * 查询实体对象数量
	 *
	 * @param sqlExceptSelect   写法 : 从from 往后的语句
	 *            筛选
	 * @return 实体对象数量
	 */
	public Long count(String sqlExceptSelect) {
		String sql = "SELECT COUNT(1) " + sqlExceptSelect;
		return Db.queryLong(sql);
	}

	/**
	 * 持久化实体对象
	 *
	 * @param model
	 *            实体对象
	 */
	public void save(M model) {
		Assert.notNull(model);
		model.set(CREATE_DATE, new Date());
		model.set(MODIFY_DATE, new Date());
		model.set(VERSION, 0);
		model.save();
	}
	/**
	 * 持久化实体对象
	 *
	 * @param model
	 *            实体对象
	 */
	public M saveModel(M model) {
		Assert.notNull(model);
		model.set(CREATE_DATE, new Date());
		model.set(MODIFY_DATE, new Date());
		model.set(VERSION, 0);
		model.save();
		return model;
	}

	/**
	 * 更新实体对象
	 *
	 * @param model
	 *            实体对象
	 * @return 实体对象
	 */
	public M update(M model) {
		Assert.notNull(model);
		model.set(MODIFY_DATE, new Date());
		M pModel = find(model.getLong(ID));
		model.set(VERSION, pModel.getLong(VERSION) + 1);
		model.update();
		return model;
	}

	/**
	 * 移除实体对象
	 *
	 * @param model
	 *            实体对象
	 */
	public void remove(M model) {
		if (model != null) {
			model.delete();
		}
	}

	/**
	 * 查找实体对象集合
	 *
	 * @param sql
	 *            查询条件
	 * @param first
	 *            起始记录
	 * @param count
	 *            数量
	 * @param filters
	 *            筛选
	 * @param orders
	 *            排序
	 * @return 实体对象集合
	 */
	protected List<M> findList(String sql, Integer first, Integer count, List<Filter> filters, List<Order> orders) {
		Assert.notNull(sql);

		String sqlFilters = getFilters(filters);
		sql += sqlFilters;

		String sqlOrders = getOrders(orders);
		if (StrKit.isBlank(sqlOrders)) {
			if (compareClass()) {
				sqlOrders = " ORDER BY " + OrderEntity.ORDER_NAME + " ASC ";
			} else {
				sqlOrders = " ORDER BY " + CREATE_DATE + " DESC ";
			}
		}
		sql += sqlOrders;

		if (first != null && 0 < first) {
			sql += " LIMIT 0, " + first;
		}
		if (count != null && 0 < count) {
			sql += " LIMIT 0, " + count;
		}
		return modelManager.find(sql);
	}


	/**
	 * 查找实体对象分页
	 *
	 *
	 *            查询条件
	 * @param pageable
	 *            分页信息
	 * @return 实体对象分页
	 */
	public Page<M> findPage(String sqlExceptSelect, Pageable pageable,String... flag) {
		Assert.notNull(sqlExceptSelect);
		if (pageable == null) {
			pageable = new Pageable();
		}
		//String modelName = StrKit.firstCharToLowerCase(getModelClass().getSimpleName());
		String select = "SELECT * ";
		if(flag.length>0){
			select = "SELECT p.*";
		}
		// 过滤条件
		String filtersSQL = getFilters(pageable.getFilters());
		LogKit.info("filtersSQL:" + filtersSQL);

		// 搜索属性、搜索值
		String searchProperty = pageable.getSearchProperty();
		String searchValue = pageable.getSearchValue();
		if (StringUtils.isNotEmpty(searchProperty) && StringUtils.isNotEmpty(searchValue)) {
			filtersSQL += " AND " + searchProperty + " LIKE '%" + StringUtils.trim(searchValue) + "%' ";
		}
		sqlExceptSelect += filtersSQL;

		String ordersSQL = getOrders(pageable.getOrders());
		LogKit.info("ordersSQL:" + ordersSQL);

		// 排序属性、方向
		String orderProperty = pageable.getOrderProperty();
		Order.Direction orderDirection = pageable.getOrderDirection();
		if (StringUtils.isNotEmpty(orderProperty) && orderDirection != null) {
			switch (orderDirection) {
			case asc:
				sqlExceptSelect += " ORDER BY " + orderProperty + " ASC ";
				break;
			case desc:
				sqlExceptSelect += " ORDER BY " + orderProperty + " DESC ";
				break;
			}
		} else if (StrKit.isBlank(ordersSQL)) {
			if (compareClass()) {
				ordersSQL = " ORDER BY " + OrderEntity.ORDER_NAME +" ASC ";
			} else {
				ordersSQL = " ORDER BY " + CREATE_DATE + " DESC ";
			}
		}
		sqlExceptSelect += ordersSQL;
		LogKit.info("sqlExceptSelect:" + sqlExceptSelect);
		return modelManager.paginate(pageable.getPageNumber(), pageable.getPageSize(), select, sqlExceptSelect);

	}

	/**
	 * 自定义分页查询
	 * @param select
	 * @param sqlExceptSelect
	 * @param pageable
	 * @return
	 */
	protected Page<M> findPages(String select,String sqlExceptSelect, Pageable pageable) {
		Assert.notNull(sqlExceptSelect);
		if (pageable == null) {
			pageable = new Pageable();
		}
		// 过滤条件
		String filtersSQL = getFilters(pageable.getFilters());
		LogKit.info("filtersSQL:" + filtersSQL);

		// 搜索属性、搜索值
		String searchProperty = pageable.getSearchProperty();
		String searchValue = pageable.getSearchValue();
		if (StringUtils.isNotEmpty(searchProperty) && StringUtils.isNotEmpty(searchValue)) {
			filtersSQL += " AND " + searchProperty + " LIKE '%" + StringUtils.trim(searchValue) + "%' ";
		}
		sqlExceptSelect += filtersSQL;

		String ordersSQL = getOrders(pageable.getOrders());
		LogKit.info("ordersSQL:" + ordersSQL);

		// 排序属性、方向
		String orderProperty = pageable.getOrderProperty();
		Order.Direction orderDirection = pageable.getOrderDirection();
		if (StringUtils.isNotEmpty(orderProperty) && orderDirection != null) {
			switch (orderDirection) {
				case asc:
					sqlExceptSelect += " ORDER BY " + orderProperty + " ASC ";
					break;
				case desc:
					sqlExceptSelect += " ORDER BY " + orderProperty + " DESC ";
					break;
			}
		} else if (StrKit.isBlank(ordersSQL)) {
			if (compareClass()) {
				ordersSQL = " ORDER BY i." + OrderEntity.ORDER_NAME +" ASC ";
			} else {
				ordersSQL = " ORDER BY i." + CREATE_DATE + " DESC ";
			}
		}
		sqlExceptSelect += ordersSQL;
		LogKit.info("sqlExceptSelect:" + sqlExceptSelect);
		return modelManager.paginate(pageable.getPageNumber(), pageable.getPageSize(), select, sqlExceptSelect);

	}
	/**
	 * 自定义分页查询
	 * @param select
	 * @param sqlExceptSelect
	 * @param pageable
	 * @return
	 */
	protected Page<M>  findPages(String select,String sqlExceptSelect, Pageable pageable,String jian) {
		Assert.notNull(sqlExceptSelect);
		if (pageable == null) {
			pageable = new Pageable();
		}
		// 过滤条件
		String filtersSQL = getFilters(pageable.getFilters());
		LogKit.info("filtersSQL:" + filtersSQL);

		// 搜索属性、搜索值
		String searchProperty = pageable.getSearchProperty();
		String searchValue = pageable.getSearchValue();
		if (StringUtils.isNotEmpty(searchProperty) && StringUtils.isNotEmpty(searchValue)) {
			filtersSQL += " AND "+jian+"." + searchProperty + " LIKE '%" + StringUtils.trim(searchValue) + "%' ";
		}
		sqlExceptSelect += filtersSQL;

		String ordersSQL = getOrders(pageable.getOrders());
		LogKit.info("ordersSQL:" + ordersSQL);

		// 排序属性、方向
		String orderProperty = pageable.getOrderProperty();
		Order.Direction orderDirection = pageable.getOrderDirection();
		if (StringUtils.isNotEmpty(orderProperty) && orderDirection != null) {
			switch (orderDirection) {
				case asc:
					sqlExceptSelect += " ORDER BY " + orderProperty + " ASC ";
					break;
				case desc:
					sqlExceptSelect += " ORDER BY " + orderProperty + " DESC ";
					break;
			}
		} else if (StrKit.isBlank(ordersSQL)) {
			if (compareClass()) {
				ordersSQL = " ORDER BY " + OrderEntity.ORDER_NAME +" ASC ";
			} else {
				ordersSQL = " ORDER BY " + CREATE_DATE + " DESC ";
			}
		}
		sqlExceptSelect += ordersSQL;
		LogKit.info("sqlExceptSelect:" + sqlExceptSelect);
		return modelManager.paginate(pageable.getPageNumber(), pageable.getPageSize(), select, sqlExceptSelect);

	}
	/**
	 * 转换为Predicate
	 *
	 *
	 *            Root
	 * @param filters
	 *            筛选
	 * @return Predicate
	 */
	private String getFilters(List<Filter> filters) {
		String sql = "";
		if (CollectionUtils.isEmpty(filters)) {
			return "";
		}
		for (Filter filter : filters) {
			if (filter == null) {
				continue;
			}
			String property = filter.getProperty();
			Filter.Operator operator = filter.getOperator();
			Object value = filter.getValue();
			Boolean ignoreCase = filter.getIgnoreCase();
			switch (operator) {
			case eq:
				if (value != null) {
					if (BooleanUtils.isTrue(ignoreCase) && value instanceof String) {
						sql +=" AND "+  property + " = " + ((String) value).toLowerCase();
					} else {
						sql +=" AND "+ property + " = " + value;
					}
				} else {
					sql +=" AND "+ property + " IS NULL ";
				}
				break;
			case ne:
				if (value != null) {
					if (BooleanUtils.isTrue(ignoreCase) && value instanceof String) {
						sql +=" AND "+ property + " != " + ((String) value).toLowerCase();
					} else {
						sql +=" AND "+ property + " != " + value;
					}
				} else {
					sql +=" AND "+ property + " IS NOT NULL ";
				}
				break;
			case gt:
				if (value instanceof Number) {
					sql +=" AND "+ property + " > " + (Number) value;
				}
				break;
			case lt:
				if (value instanceof Number) {
					sql +=" AND "+ property + " < " + (Number) value;
				}
				break;
			case ge:
				if (value instanceof Number) {
					sql +=" AND "+ property + " >= " + (Number) value;
				}
				break;
			case le:
				if (value instanceof Number) {
					sql +=" AND "+ property + " <= " + (Number) value;
				}
				break;
			case like:
				if (value instanceof String) {
					if (BooleanUtils.isTrue(ignoreCase)) {
						sql += " AND " + property + " LIKE '%" + ((String) value).toLowerCase() + "'";
					} else {
						sql += " AND " + property + " LIKE '%" + (String) value + "'";
					}
				}
				break;
			case in:
				sql +=" AND "+ property + " IN(" + value + ")";
				break;
			case isNull:
				sql +=" AND "+ property + " IS NULL";
				break;
			case isNotNull:
				sql +=" AND "+ property + " IS NOT NULL";
				break;
			}
		}
		return sql;
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

	/**
	 * 判断一个类Class1和另一个类Class2是否相同或是另一个类的超类或接口。
	 * @return
	 *
	 */
	private boolean compareClass() {
		try {
			Class<?> onwClass = Class.forName("com.wtshop.dao." + modelClass.getSimpleName() + "Dao");
			return OrderEntity.class.isAssignableFrom(onwClass);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}


	//==================延迟增删改缓冲到json操作===============开始

	//注意:primaryKey可以是物理表的id,也可以是多个and条件的字段,并不只只是表的主键

	public static  ThreadLocal<Map<String,Map<String,List>>>  localJsonDb=new  ThreadLocal();


	//获取local 增,删,改,其中的list
	private    List<JSONObject>  getlocalList(String listname){
		Map<String,Map<String,List>> local =    localJsonDb.get();
		if (local==null){
			Map<String, List> map = new HashedMap();
			 local=new HashedMap();
			switch (listname){
				case "jRemoveList":
					map.put("jRemoveList", new ArrayList());
					break;
				case  "jUpdateList":
					map.put("jUpdateList", new ArrayList());
					break;
				case "jSaveList":
					map.put("jSaveList", new ArrayList());
					break;
			}
			local.put(getTableName(),map);
			localJsonDb.set(local);
			return  local.get(getTableName()).get(listname);
		}else {
			Map<String, List> map = new HashedMap();
		    map =  local.get(getTableName());
			if (map !=null ){
				List<JSONObject> list=map.get(listname);
				if (list==null){
					list=new ArrayList<JSONObject>();
					map.put(listname,list);
					return  list;
				}else {
					return list;
				}
			}else {
			      map = new HashedMap();
				switch (listname){
					case "jRemoveList":
						map.put("jRemoveList", new ArrayList());
						break;
					case  "jUpdateList":
						map.put("jUpdateList", new ArrayList());
						break;
					case "jSaveList":
						map.put("jSaveList", new ArrayList());
						break;
				}
				local.put(getTableName(),map);
				return  local.get(getTableName()).get(listname);
			}
		}


	}
	//缓冲新增

	public  int jSave(M model){
		List<JSONObject> list=getlocalList("jSaveList");
		list.add(JSON.parseObject(model.toJson()));
		return  list.size()-1;
	}
	//缓冲编辑

	public  void  jUpdate(M model){
		List<JSONObject> list=getlocalList("jUpdateList");
		list.add(JSON.parseObject(model.toJson()));
	}

	//缓冲删除

	public  void jremove(M model){
		if (model !=null){
			List<JSONObject> list=getlocalList("jRemoveList");
			list.add(JSON.parseObject(model.toJson()));
		}
	}
	//通过查询sql删除,sql 的 格式 sleect  *
	public  void jremoveBySql(String sql){
		List	<M > list=findListBySql(sql);
		if (!CollectionUtils.isEmpty(list)){
			for (M m:list){
				jremove(m);
			}
		}

	}


	//获取增删改json字符串
	public  static  String getlocalDbStr(){
		Map<String,Map<String,List>> local=  localJsonDb.get();
		String result="";
		if (local ==null || local.size()<=0){
			return  result;
		}
		String o = JSONObject.toJSONString(local, SerializerFeature.WriteMapNullValue);
		 return  o;
	}

	public static void  clearLocalJsonDb(){
		localJsonDb.remove();
		localJsonDb.set(null);
	}
//转化原始model 再转 JSONArray
	public  static JSONArray toJsonList(List  list){
		JSONArray array=new  JSONArray();
		for (Object o:list){
		     JSONObject Jobj=JSON.parseObject(	((Model)o).toJson() );
			array.add(Jobj);
		}
		return  array;
	}

	//根据主键查找主键一样json对象
	public  static  JSONObject searchJsonObj(JSONArray source,JSONObject target,String [] primaryKey){

		JSONObject result=null;
		for (Object subO:source){
			JSONObject J2=(JSONObject)subO;

			int eqCount=0;
			for ( String pk: primaryKey){
				if (!target.get(pk).equals(J2.get(pk))){
					break;
				}
				eqCount++;
			}

			if (eqCount==primaryKey.length){
				result=J2;
				break;
			}
		}
		return  result;

	}

	//根据主键查找arr1不在arr2中的元素

	public  static  JSONArray getDiffJsonObjArr(JSONArray arra1, JSONArray array2, String [] primaryKey){
		JSONArray  diffArr=new JSONArray();
		for (Object o:arra1){
			JSONObject J1=(JSONObject)o;
			if (array2==null ||array2.size()<=0){
				diffArr.add(J1);
			}

			if (searchJsonObj(array2,J1,primaryKey) ==null){
				diffArr.add(J1);
			}
		}
		return  diffArr;
	}


	/***
	 *根据数据库查询的list合并json的增删改
	 * @param root
	 * @param tableName
	 * @param dbList 从数据库中查询的表数据
	 * @param primaryKey 主键列表
	 * @return
	 */
	public static   JSONArray  getTableJson(JSONObject root, String tableName,List dbList, String [] primaryKey) {
		//先删除
		JSONArray dbarr = new JSONArray();
		JSONObject table = root.getJSONObject(tableName);
		JSONArray jarr = null;
		if (table == null) {
			return dbarr;
		} else {
			jarr = table.getJSONArray("jRemoveList");
			dbarr = toJsonList(dbList);
			mergeList(dbarr, jarr, 2, primaryKey);//合并删除
			jarr = table.getJSONArray("jUpdateList");
			mergeList(dbarr, jarr, 0, primaryKey);//合并更新
			jarr = table.getJSONArray("jSaveList");
			mergeList(dbarr, jarr, 1, primaryKey);//合并新增
		}

		return dbarr;

	}




	/**
	 *
	 //合并jsonarray
	 * @param dbArr 数据库list
	 * @param jsonArr jsonlist
	 * @param type   type(0:更新,1:新增,2:删除)
	 * @param primaryKey primaryKey:主键列表
	 * @return
	 */


	public  static  JSONArray mergeList( JSONArray dbArr,JSONArray jsonArr,int type, String [] primaryKey ){
		if (dbArr==null &&jsonArr==null){
			return  new  JSONArray();
		}
		if (jsonArr==null){
			return  dbArr;
		}

		if (dbArr==null && type==1){
			return  jsonArr;
		}
		switch (type){
			case 0:
			return 	updateMerge(dbArr,jsonArr,primaryKey);
			case 1:
				return saveMerge(dbArr,jsonArr,primaryKey);
			case 2:
				return deleteMerge(dbArr,jsonArr,primaryKey);
		}
		return  null;
	}


	//编辑类型合并
	private static JSONArray saveMerge( JSONArray dbArr,JSONArray jsonArr ,String [] primaryKey ){
		dbArr.addAll(jsonArr);
		return  dbArr;
	}

	//编辑类型合并
	private static JSONArray updateMerge( JSONArray dbArr,JSONArray jsonArr ,String [] primaryKey ){
		int index=0;
		for (Object o:dbArr){
			JSONObject J1=(JSONObject)o;
			JSONObject J2= searchJsonObj(jsonArr,J1,primaryKey);
			if (J2 !=null){
				dbArr.set(index,J2);
			}

			index++;
		}
		return  dbArr;
	}

	private static JSONArray deleteMerge( JSONArray dbArr,JSONArray jsonArr,String [] primaryKey ){

		Iterator  it =dbArr.iterator();
		while (it.hasNext()) {
			JSONObject J1 = (JSONObject)it.next();
			if (searchJsonObj(jsonArr,J1,primaryKey) !=null){
				it.remove();
			}
		}
		return  dbArr;
	}

	//比较两个jsonarray所有不想交的元素,注意两个list的除了主键可能其他字段不一样,不能用contains等方法,可改为双map效率更高
	public  static  JSONArray getAllDiff(JSONArray dbArr,JSONArray jsonArr,String [] primaryKey){

		if (CollectionUtils.isEmpty(dbArr)){
			return jsonArr;
		}

		if (CollectionUtils.isEmpty(jsonArr)){
			return dbArr;
		}


		JSONArray alldiff= getDiffJsonObjArr(dbArr,jsonArr,primaryKey);
		alldiff.addAll(getDiffJsonObjArr(jsonArr,dbArr,primaryKey));
		return  alldiff;
	}


	//审核成功写入json数据
public static void  jsonWriteDb(JSONObject root, String tableName, BaseDao dao,Class<?> moldeClass){
		JSONObject table=root.getJSONObject(tableName);
		//删除
		if (table==null){return;}
		JSONArray jarr=null;
		jarr=table.getJSONArray("jRemoveList");
		if (jarr !=null){
			for (Object o :jarr){
				JSONObject jobj=(JSONObject)o;
				Object m =JSON.parseObject(JSONObject.toJSONString(jobj, SerializerFeature.WriteMapNullValue),moldeClass);
				dao.remove((Model) m);
			}
		}

		//更新
	jarr=table.getJSONArray("jUpdateList");
	if (jarr !=null){
		for (Object o :jarr){
			JSONObject jobj=(JSONObject)o;
			Object m =JSON.parseObject(JSONObject.toJSONString(jobj, SerializerFeature.WriteMapNullValue),moldeClass);
			dao.update((Model) m);
		}
	}

	//新增

	jarr=table.getJSONArray("jSaveList");
	if (jarr !=null){
		for (Object o :jarr){
			JSONObject jobj=(JSONObject)o;
			Object m =JSON.parseObject(JSONObject.toJSONString(jobj, SerializerFeature.WriteMapNullValue),moldeClass);
			dao.save((Model) m);
		}
	}



	};


//==============延迟增删改缓冲到json操作========结束



}