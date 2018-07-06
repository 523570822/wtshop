package com.wtshop.service;

import com.jfinal.aop.Enhancer;
import com.wtshop.dao.AreaDao;
import com.wtshop.entity.AreaResult;
import com.wtshop.model.Area;
import com.wtshop.model.AreaDescribe;
import com.wtshop.model.Goods;
import com.wtshop.util.Assert;
import com.wtshop.util.PinYinUtil;
import com.wtshop.util.StringUtils;
import org.apache.commons.collections.CollectionUtils;

import java.util.*;

/**
 * Service - 地区
 * 
 * 
 */
public class AreaService extends BaseService<Area> {

	/**
	 * 构造方法
	 */
	public AreaService() {
		super(Area.class);
	}
	
	private AreaDao areaDao = Enhancer.enhance(AreaDao.class);
	private AreaDescribeService areaDescribeService = Enhancer.enhance(AreaDescribeService.class);
	private GoodsService goodsService = Enhancer.enhance(GoodsService.class);
	
	/**
	 * 查找顶级地区
	 * 
	 * @return 顶级地区
	 */
	public List<Area> findRoots() {
		return areaDao.findRoots(null);
	}


	/**
	 *多个商品 选出时间最长的商品
	 */

	public Goods findMaxLong(List<Goods> goodslist){
		if(goodslist != null && goodslist.size() > 0 ){

			Map<Integer, Long> treeMap = new TreeMap<Integer, Long>(new Comparator<Integer>() {
				@Override
				public int compare(Integer o1, Integer o2) {
					// return o1.compareTo(o2); // 默认：升序排列
					return o2.compareTo(o1);  // 降序排列
					// return 0;    // 只返回存储的第一个key的值，这里是"ccccc"
				}
			});

			List<Goods> emptyAreaDescribeList=new ArrayList<>();
			for(Goods goods : goodslist){
				AreaDescribe areaDescribe = areaDescribeService.findByAreaId(goods.getAreaId());
				if( areaDescribe != null){
					treeMap.put(areaDescribe.getReceivingEndtime(), goods.getId());
				}else {
					emptyAreaDescribeList.add(goods);
				}
			}
			for (Integer key : treeMap.keySet()) {
				return goodsService.find(treeMap.get(key));
			}

			if (emptyAreaDescribeList.size()>0){
				return emptyAreaDescribeList.get(0);
			}

		}else {
			return null;
		}
		return null;

	}




	/**
	 * 所有地区集合
	 */
	public List<AreaResult> findAreaList(){
		List<Area> areaList = areaDao.findall();
		List<AreaResult> areaResults = new ArrayList<>();
		for(Area area1 : areaList){
			AreaResult areaResult = new AreaResult();
			areaResult.setId(area1.getId());
			areaResult.setName(area1.getName());
			areaResult.setParentId(area1.getParentId());
			areaResults.add(areaResult);
		}

		List<AreaResult> guojia = searchAraes(null,areaResults);
		formateSonList(guojia,areaResults);
		return guojia;

	}

	/**
	 * 通讯录首字母分组
	 */
	public static Map<String,List> groupByUserName(List<AreaResult> list) {
		String name1="";
		String oldPY = "";
		String thisPY = "";
		List nameList =new ArrayList();
		Map<String,List> map = new LinkedHashMap();
		for(int i=0;i<list.size();i++){
			name1 = list.get(i).getName().toString();
			thisPY = PinYinUtil.getPinYinHeadChar(name1.substring(0,1)).toUpperCase();
			if(StringUtils.isEmpty(oldPY)){
				oldPY = thisPY;
			}
			if(!thisPY.equals(oldPY)){
				map.put(oldPY,nameList);
				nameList = new ArrayList();
			}
			oldPY = thisPY;
			nameList.add(list.get(i));
		}
		map.put(thisPY,nameList);
		List<String> k = new ArrayList<>();
		List list1 = new ArrayList<>();
		for (String key : map.keySet()) {
			if (key.charAt(0) < 'A' || key.charAt(0) > 'Z') {
				list1.addAll(map.get(key));
				k.add(key);
			}
		}
		for (String k1 : k) {
			map.remove(k1);
		}
		if (list1.size() > 0) {
			map.put("#", list1);
		}
		return map;
	}

	private void formateSonList(List<AreaResult> superAreas,List<AreaResult> areaList){
		for (AreaResult area :superAreas){
			List<AreaResult> sons = searchAraes(area.getId(),areaList);
			if (sons!=null && sons.size()>0){
				Collections.sort(sons, new Comparator<AreaResult>() {
					@Override
					public int compare(AreaResult o1, AreaResult o2) {
						String name1 = o1.getName();
						String name2 = o2.getName();
						String zm1 = PinYinUtil.getPinYinHeadChar(name1.substring(0,1)).toUpperCase();
						String zm2 = PinYinUtil.getPinYinHeadChar(name2.substring(0,1)).toUpperCase();
						return zm1.compareTo(zm2);
					}
				});

				area.setSonSort(groupByUserName(sons));
				formateSonList(sons,areaList);

			}
		}
	}

	private List<AreaResult> searchAraes(Long condition,List<AreaResult> areaList){
		List<AreaResult> resultAreas = new ArrayList<>();
		for(AreaResult area : areaList){
			if (area.getParentId()==null && condition==null){
				resultAreas.add(area);
			}
			if(area.getParentId()!=null &&(area.getParentId() + "").equals(condition + "")){
				resultAreas.add(area);
			}
		}
		return resultAreas;
	}

	/**
	 *
	 * @return
	 */
	public List<Area> findRoots1() {
		return areaDao.findRoots1(0);
	}
	/**
	 * @return
	 */
	public List<Area> findLists(Long id) {
		return areaDao.findList(id);
	}
	/**
	 * 查找顶级地区
	 * 
	 * @param count
	 *            数量
	 * @return 顶级地区
	 */
	public List<Area> findRoots(Integer count) {
		return areaDao.findRoots(count);
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
		return areaDao.findParents(area, recursive, count);
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
		return areaDao.findChildren(area, recursive, count);
	}
	
	/**
	 * 拼接成json类型
	 */
	public String createJSONData() {
		// 查询一级节点
		List<Area> areas = findRoots();
		StringBuffer sb = new StringBuffer("["); // 初始化根节点
		if (CollectionUtils.isNotEmpty(areas)) {
			for (Area area : areas) {
				sb.append("{\"value\":\"").append(area.getId()).append("\",");
				sb.append("\"text\":\"").append(area.getName()).append("\"");
				List<Area> childrens = area.getChildren();
				if (CollectionUtils.isNotEmpty(childrens)) {
					sb.append(",\"children\":[");
					for (Area children : childrens) { 
						sb.append("{\"value\":\"").append(children.getId()).append("\",");
						sb.append("\"text\":\"").append(children.getName()).append("\"");
						sb.append(getNodes(children).toString());    // 下级节点  
						//sb.append("},");
					}
					sb = new StringBuffer(sb.substring(0,sb.lastIndexOf(",")) + "]},");
				}else {
					sb.append("},");
				}
			}
			sb = new StringBuffer(sb.substring(0, sb.length() - 1)+ "]");
		}
		return sb.toString();
	}
	 
	/** 
     * 获得节点  
     */  
	private String getNodes(Area area) {
		List<Area> childrens = area.getChildren();
		StringBuffer sb = new StringBuffer();
		if (CollectionUtils.isNotEmpty(childrens)) {
			sb.append(",\"children\":[");
			for (Area children : childrens) {
				sb.append("{\"value\":\"").append(children.getId()).append("\",");
				sb.append("\"text\":\"").append(children.getName()).append("\"");
				sb.append(getNodes(children).toString());
			}
		} else {
			sb.append("},");
		}
		if (sb.toString().contains("children")) {
			sb = new StringBuffer(sb.substring(0, sb.lastIndexOf(",")));
			sb.append("]},");
		}
		return sb.toString();
	}
	
	public Area save(Area area) {
		Assert.notNull(area);

		setValue(area);
		return super.save(area);
	}

	public Area update(Area area) {
		Assert.notNull(area);

//		setValue(area);
//		for (Area children : areaDao.findChildren(area, true, null)) {
//			setValue(children);
//		}
		return super.update(area);
	}

//	public Area update(Area area, String... ignoreProperties) {
//		return super.update(area, ignoreProperties);
//	}

	public void delete(Long id) {
		super.delete(id);
	}

	public void delete(Long... ids) {
		super.delete(ids);
	}

	public void delete(Area area) {
		super.delete(area);
	}

	/**
	 * 设置值
	 * 
	 * @param area
	 *            地区
	 */
	private void setValue(Area area) {
		if (area == null) {
			return;
		}
		Area parent = area.getParent();
		if (parent != null) {
			area.setFullName(parent.getFullName() + area.getName());
			area.setTreePath(parent.getTreePath() + parent.getId() + Area.TREE_PATH_SEPARATOR);
		} else {
			area.setFullName(area.getName());
			area.setTreePath(Area.TREE_PATH_SEPARATOR);
		}
		area.setGrade(area.getParentIds().length);
	}


}