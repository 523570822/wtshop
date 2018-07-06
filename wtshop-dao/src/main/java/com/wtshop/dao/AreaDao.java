package com.wtshop.dao;

import com.wtshop.model.Area;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;

import java.util.*;

/**
 * Dao - 地区
 * 
 * 
 */
public class AreaDao extends OrderEntity<Area> {
	
	/**
	 * 构造方法
	 */
	public AreaDao() {
		super(Area.class);
	}

	/**
	 * 查找顶级地区
	 * 
	 * @param count
	 *            数量
	 * @return 顶级地区
	 */
	public List<Area> findRoots(Integer count) {
		String sql = "SELECT * FROM area WHERE parent_id =0 ORDER BY name ASC ";
		if (count != null) {
			sql += "LIMIT 0, " + count;
		}
		return modelManager.find(sql);
	}

    public List<Area> findall(){
		String sql = " select id, name, parent_id from area order by name ";
		return modelManager.find(sql);
	}

	/**
	 * 连表显示
	 * @param count
	 * @return
	 */
	public List<Area> findRoots1(Integer count) {
		String sql = "SELECT a.*,ad.entrepot_name,ad.entrepot_message,ad.receiving_beginTime,ad.receiving_endTime FROM area a LEFT JOIN area_describe ad ON a.id=ad.areaId WHERE parent_id =0 ORDER BY orders ASC ";
		if (count >0) {
			sql += "LIMIT 0, " + count;
		}
		return modelManager.find(sql);
	}

	/**
	 * 链表实现
	 * @param id
	 * @return
	 */
	public List<Area> findList(Long id) {
		if (id == null) {
			return null;
		}
		String sql = "SELECT a.*,ad.entrepot_name,ad.entrepot_message,ad.receiving_beginTime,ad.receiving_endTime from area a LEFT JOIN area_describe ad ON a.id=ad.areaId where a.parent_id="+id;
		return modelManager.find(sql);
		//return Db.find(sql);
	}
	/**
	 * 查找上级地区
	 * 
	 * @param area
	 *            地区
	 * @param recursive
	 *            是否递归
	 * @param count
	 *            数量
	 * @return 上级地区
	 */
	public List<Area> findParents(Area area, boolean recursive, Integer count) {
		List<Area> areas = new ArrayList<>();
		if (area == null || area.getParent() == null) {
			areas.add(area);
			return areas;
		}
		String sql = "";

		if (recursive) {
			sql = "SELECT * FROM area WHERE id IN (?) ORDER BY grade ASC ";
		} else {
			sql = "SELECT * FROM area WHERE area_id = ? ";
		}
		if (count != null) {
			sql += "LIMIT 0, " + count;
		}
		if (recursive) {
			List<Long> parentIds = Arrays.asList(area.getParentIds());
			StringBuffer sb = new StringBuffer();
			if (parentIds != null && 0 < parentIds.size()) {
				int size = parentIds.size() - 1;
	            for (int i = 0; i < parentIds.size(); i++) {
	            	 sb.append(i == size ? parentIds.get(size) : parentIds.get(i) + ",");
	            }
			}
			areas = modelManager.find(sql, sb.toString());
		} else {
			areas = modelManager.find(sql, area.getParent());
		}
		return areas;
	}

	/**
	 * 查找下级地区
	 * 
	 * @param area
	 *            地区
	 * @param recursive
	 *            是否递归
	 * @param count
	 *            数量
	 * @return 下级地区
	 */
	public List<Area> findChildren(Area area, boolean recursive, Integer count) {
		if (recursive) {
			String sql = "";
			if (area != null) {
				sql = "SELECT * FROM area WHERE tree_path LIKE '%" + Area.TREE_PATH_SEPARATOR + area.getId() + Area.TREE_PATH_SEPARATOR + "%' ORDER BY grade ASC, orders ASC ";
			} else {
				sql = "SELECT * FROM area ORDER BY grade ASC, orders ASC ";
			}
			if (count != null) {
				sql += "LIMIT 0, " + count;
			}
			List<Area> result = modelManager.find(sql);
			sort(result);
			return result;
		} else {
			String sql = "SELECT * FROM area WHERE parent_id = ? ORDER BY orders ASC ";
			if (count != null) {
				sql += "LIMIT 0, " + count;
			}
			return modelManager.find(sql, area.getId());
		}
	}
	
	/**
	 * 排序地区
	 * 
	 * @param areas
	 *            地区
	 */
	private void sort(List<Area> areas) {
		if (CollectionUtils.isEmpty(areas)) {
			return;
		}
		final Map<Long, Integer> orderMap = new HashMap<Long, Integer>();
		for (Area area : areas) {
			orderMap.put(area.getId(), area.getOrders());
		}
		Collections.sort(areas, new Comparator<Area>() {
			public int compare(Area area1, Area area2) {
				Long[] ids1 = (Long[]) ArrayUtils.add(area1.getParentIds(), area1.getId());
				Long[] ids2 = (Long[]) ArrayUtils.add(area2.getParentIds(), area2.getId());
				Iterator<Long> iterator1 = Arrays.asList(ids1).iterator();
				Iterator<Long> iterator2 = Arrays.asList(ids2).iterator();
				CompareToBuilder compareToBuilder = new CompareToBuilder();
				while (iterator1.hasNext() && iterator2.hasNext()) {
					Long id1 = iterator1.next();
					Long id2 = iterator2.next();
					Integer order1 = orderMap.get(id1);
					Integer order2 = orderMap.get(id2);
					compareToBuilder.append(order1, order2).append(id1, id2);
					if (!iterator1.hasNext() || !iterator2.hasNext()) {
						compareToBuilder.append(area1.getGrade(), area2.getGrade());
					}
				}
				return compareToBuilder.toComparison();
			}
		});
	}

}