
package com.wtshop.dao;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.wtshop.Filter;
import com.wtshop.Order;
import com.wtshop.Pageable;
import com.wtshop.Setting;
import com.wtshop.model.*;
import com.wtshop.util.DateUtils;
import com.wtshop.util.SqlUtils;
import com.wtshop.util.SystemUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;

/**
 * Dao - 货品
 * 
 * 
 */

public class ActivityDao extends BaseDao<Activity> {

	/**
	 * 构造方法
	 */
	public ActivityDao() {
		super(Activity.class);
	}



}