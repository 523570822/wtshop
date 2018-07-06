package com.wtshop.controller.admin;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;
import com.wtshop.Pageable;
import com.wtshop.model.Ad;
import com.wtshop.model.TargetPath;
import com.wtshop.service.AdPositionService;
import com.wtshop.service.AdService;
import com.wtshop.service.TargetPathService;

/**
 * Controller - 广告
 * 
 * 
 */
@ControllerBind(controllerKey = "/admin/ad")
public class AdController extends BaseController {

	private AdService adService = enhance(AdService.class);
	private AdPositionService adPositionService = enhance(AdPositionService.class);
	private TargetPathService targetPathService=enhance(TargetPathService.class);

	/**
	 * 添加
	 */
	public void add() {
		setAttr("types", Ad.Type.values());
		setAttr("adPositions", adPositionService.findAll());
		setAttr("targetTitleTree",targetPathService.findAll());
		render("/admin/ad/add.ftl");
	}

	/**
	 * 保存
	 */
	public void save() {
		Ad ad = getModel(Ad.class);
		String typeName = getPara("type");

		String targetId = getPara("targetId");
		if(!StrKit.isBlank(targetId)){
			Long tarId = new Long(targetId);
			ad.setTargetId(tarId);
			TargetPath targetPath=targetPathService.find(tarId);
			ad.setUrl(targetPath.getTargetPath());
		}

		Ad.Type type = StrKit.notBlank(typeName) ? Ad.Type.valueOf(typeName) : null;
		ad.setType(type.ordinal());
		Long adPositionId = getParaToLong("adPositionId");
		ad.setAdPositionId(adPositionService.find(adPositionId).getId());
		if (ad.getBeginDate() != null && ad.getEndDate() != null && ad.getBeginDate().after(ad.getEndDate())) {
			redirect(ERROR_VIEW);
			return;
		}
		if (Ad.Type.text.ordinal() == ad.getType()) {
			ad.setPath(null);
		} else {
			ad.setContent(null);
		}
		adService.save(ad);
		addFlashMessage(SUCCESS_MESSAGE);
		redirect("/admin/ad/list.jhtml");
	}

	/**
	 * 编辑
	 */
	public void edit() {
		Long id = getParaToLong("id");
		Ad ad = adService.find(id);
		String type = null;
		if(Ad.Type.text.ordinal() == ad.getType() ){
			type = "text";
		}else {
			type = "image";
		}

		setAttr("types", Ad.Type.values());
		setAttr("ad", adService.find(id));
		setAttr("type", type);
		setAttr("adPositions", adPositionService.findAll());
		render("/admin/ad/edit.ftl");
	}

	/**
	 * 更新
	 */
	public void update() {
		Ad ad = getModel(Ad.class);
		Long adPositionId = getParaToLong("adPositionId");
		String type = getPara("type");

		if("image".equals(type)){
			ad.setType(1);
		}else {
			ad.setType(0);
		}

		String targetId = getPara("targetId");
		if(!StrKit.isBlank(targetId)){
			Long tarId = new Long(targetId);
			ad.setTargetId(tarId);
			TargetPath targetPath=targetPathService.find(tarId);
			ad.setUrl(targetPath.getTargetPath());
		}

		ad.setAdPosition(adPositionService.find(adPositionId));
		if (ad.getBeginDate() != null && ad.getEndDate() != null && ad.getBeginDate().after(ad.getEndDate())) {
			redirect(ERROR_VIEW);
			return;
		}
		if (Ad.Type.text.equals(ad.getType())) {
			ad.setPath(null);
		} else {
			ad.setContent(null);
		}
		ad.setAdPositionId(adPositionId);
		adService.update(ad);
		addFlashMessage(SUCCESS_MESSAGE);
		redirect("/admin/ad/list.jhtml");
	}

	/**
	 * 列表
	 */
	public void list() {
		Pageable pageable = getBean(Pageable.class);
		setAttr("pageable", pageable);
		setAttr("page", adService.findPage(pageable));
		render("/admin/ad/list.ftl");
	}

	/**
	 * 删除
	 */
	public void delete() {
		Long[] ids = getParaValuesToLong("ids");
		adService.delete(ids);
		renderJson(SUCCESS_MESSAGE);
	}

}