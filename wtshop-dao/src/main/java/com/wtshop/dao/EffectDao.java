package com.wtshop.dao;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.wtshop.model.Effect;
import com.wtshop.model.Tag;

import java.util.List;

/**
 * Dao - 标签
 * 
 * 
 */
public class EffectDao extends OrderEntity<Effect> {

	/**
	 * 构造方法
	 */
	public EffectDao() {
		super(Effect.class);
	}
	
	/**
	 * 查找标签
	 *
	 * @return 标签
	 */
	public List<Effect> findEffectList() {
		String sql = "SELECT id,name FROM effect WHERE 1 = 1  ORDER BY name ASC ";
		return modelManager.find(sql);
	}

	/**
	 * 查找标签
	 *
	 * @return 标签
	 */
	public List<Record> findEffectSort() {
		String sql = "SELECT id,name FROM effect WHERE 1 = 1  ORDER BY name ASC ";
		return Db.find(sql);
	}



}