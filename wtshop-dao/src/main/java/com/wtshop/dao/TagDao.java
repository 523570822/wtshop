package com.wtshop.dao;

import java.util.List;

import com.wtshop.model.Tag;
import org.apache.commons.lang3.StringUtils;

/**
 * Dao - 标签
 * 
 * 
 */
public class TagDao extends OrderEntity<Tag> {
	
	/**
	 * 构造方法
	 */
	public TagDao() {
		super(Tag.class);
	}
	
	/**
	 * 查找标签
	 * 
	 * @param type
	 *            类型
	 * @return 标签
	 */
	public List<Tag> findList(Tag.Type type) {
		String sql = "SELECT * FROM tag WHERE 1 = 1 ";
		if (type != null) {
			sql += "AND type = " + type.ordinal();
		} 
		sql += " ORDER BY orders ASC ";
		return modelManager.find(sql);
	}

	/**
	 * 根据编号查找标签
	 *
	 * @param id
	 *            编号(忽略大小写)
	 * @return 标签
	 */
	public List<Tag> getTag(Long id) {

		String sql = "SELECT a.name FROM goods g LEFT JOIN goods_tag t on g.id = t.goods left join tag a on t.tags = a.id WHERE g.id = LOWER(?)";
		try {
			return modelManager.find(sql,id);
		} catch (Exception e) {
			return null;
		}
	}


}