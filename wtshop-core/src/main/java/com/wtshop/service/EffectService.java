package com.wtshop.service;

import com.jfinal.aop.Enhancer;
import com.jfinal.plugin.activerecord.Record;
import com.wtshop.Filter;
import com.wtshop.Order;
import com.wtshop.dao.EffectDao;
import com.wtshop.dao.TagDao;
import com.wtshop.model.Effect;
import com.wtshop.model.Tag;
import com.wtshop.util.PinYinUtil;
import com.wtshop.util.StringUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Service - 标签
 * 
 * 
 */
public class EffectService extends BaseService<Effect> {

	/**
	 * 构造方法
	 */
	public EffectService() {
		super(Effect.class);
	}
	
	private EffectDao effectDao = Enhancer.enhance(EffectDao.class);
	

	public List<Effect> findEffectList(){
		return effectDao.findEffectList();
	}

	public Map<String,List> findEffectSort(){
		List<Record> effects = effectDao.findEffectSort();
		Collections.sort(effects, new Comparator<Record>() {
			@Override
			public int compare(Record o1, Record o2) {
				String name1 = StringUtils.isEmpty(o1.get("name"))?o1.get("name").toString():o1.get("name").toString();
				String name2 = StringUtils.isEmpty(o2.get("name"))?o2.get("name").toString():o2.get("name").toString();
				String zm1 = PinYinUtil.getPinYinHeadChar(name1.substring(0,1)).toUpperCase();
				String zm2 = PinYinUtil.getPinYinHeadChar(name2.substring(0,1)).toUpperCase();
				return zm1.compareTo(zm2);
			}
		});
		Map<String, List> stringListMap = PinYinUtil.groupByUserName(effects);
		return stringListMap;
	}

	public Effect save(Effect effect) {
		return super.save(effect);
	}

	public Effect update(Effect effect) {
		return super.update(effect);
	}


	public void delete(Long id) {
		super.delete(id);
	}

	public void delete(Long... ids) {
		super.delete(ids);
	}

	public void delete(Effect effect) {
		super.delete(effect);
	}
}