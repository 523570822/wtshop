package com.wtshop.controller.admin;

import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.plugin.ehcache.CacheName;
import com.jfinal.plugin.ehcache.EvictInterceptor;
import com.wtshop.Message;
import com.wtshop.model.Area;
import com.wtshop.model.AreaDescribe;
import com.wtshop.service.AreaDescribeService;
import com.wtshop.service.AreaService;
import com.wtshop.util.ObjectUtils;
import com.wtshop.util.StringUtils;
import org.apache.commons.collections.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller - 地区
 * 
 * 
 */
@ControllerBind(controllerKey = "/admin/area")
public class AreaController extends BaseController {

	private AreaService areaService = enhance(AreaService.class);
	private AreaDescribeService areaDescribeService = enhance(AreaDescribeService.class);
	/**
	 * 添加
	 */
	public void add() {
		String flag = getPara("flag");
		setAttr("flag",StringUtils.isEmpty(flag)?0:flag);
		Long parentId = getParaToLong("parentId");
		setAttr("parent", areaService.find(parentId));
		render("/admin/area/add.ftl");
	}

	/**
	 * 保存
	 */
	@Before(EvictInterceptor.class) 
	@CacheName("wapArea")
	public void save() {
		Area area = getModel(Area.class);
		Long parentId = getParaToLong("parentId");
		parentId=(parentId==null?0:parentId);
		String flag = getPara("flag");
		Area pArea = areaService.find(parentId);
		if (pArea != null) {
			area.setParentId(pArea.getId());
		} else {
			area.setParentId(0L);
		}
		
		area.setFullName(null);
		area.setTreePath(null);
		area.setGrade(null);
		area.setChildren(null);
		area.setMembers(null);
		area.setReceivers(null);
		area.setOrder(null);
		area.setDeliveryCenters(null);
		area.setFreightConfigs(null);
		areaService.save(area);
		addFlashMessage(SUCCESS_MESSAGE);
		//传递flag和parent属性
		redirect("list.jhtml?flag="+flag+"&parentId="+parentId);
	}

	/**
	 * 编辑
	 */
	public void edit() {
		Long id = getParaToLong("id");
		String flag = getPara("flag");
		setAttr("flag",StringUtils.isEmpty(flag)?0:flag);
		setAttr("area", areaService.find(id));
		setAttr("areaDescribe", areaDescribeService.findByAreaId(id));
		render("/admin/area/edit.ftl");
	}

	/**
	 * 更新
	 */
	@Before(Tx.class)
	public void update() {
		Area area = getModel(Area.class);
		String flag = getPara("flag");
		String parentId = getPara("parentId");
		AreaDescribe areaDescribe = getModel(AreaDescribe.class);
		areaDescribe.setAreaId(area.getId());
//		area.remove("full_name", "tree_path", "grade", "parent_id");
		areaService.update(area);
		if(areaDescribe.getId()==null){
			areaDescribeService.save(areaDescribe);
		}else {
			areaDescribeService.update(areaDescribe);
		}
		addFlashMessage(SUCCESS_MESSAGE);
		//传递flag和parent属性
		redirect("list.jhtml?flag="+flag+"&parentId="+parentId);
	}

	/**
	 * 列表
	 */
	public void list() {
		Long parentId = 0L;
		String id = getPara("parentId");
		String flag = getPara("flag");
		if(!("undefined".equals(id)||null == id || "".equals(id))){
			parentId = Long.parseLong(id);
		}
		setAttr("parentId",parentId);
		setAttr("flag",flag);
		Area parent = areaService.find(parentId);
		if (parent != null) {
			setAttr("parent", parent);
			setAttr("areas",areaService.findLists(parentId));
		} else {
			setAttr("areas", areaService.findRoots1());
		}
		render("/admin/area/list.ftl");
	}
	/**
	 * 列表化地区
	 */
	public void listToJq(){
		Long parentId = getParaToLong("parentId");
		//Area parent = areaService.find(parentId);
		List<Area> list = areaService.findLists(parentId);
		if (list == null) {
			list = areaService.findRoots1();
		}
		Area area = areaService.find(parentId);
		Map map= new HashMap();
		map.put("list",list);
		map.put("parentId", ObjectUtils.isEmpty(area)?null:area.getParentId());
		renderJson(map);
	}
	/**
	 * 删除
	 */
	public void delete() {
		Long id = getParaToLong("id");
		Area area = areaService.find(id);
		if (CollectionUtils.isNotEmpty(area.getChildren())) {
			renderJson( "存在下级地区");
			return;
		}
		try{
			areaService.delete(id);
		}catch (Exception e){
			renderJson(new Message(Message.Type.warn,"存在关联地区无法删除"));
			return;
		}

		renderJson(SUCCESS_MESSAGE);
	}

}