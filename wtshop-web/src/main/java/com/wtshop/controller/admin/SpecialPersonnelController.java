package com.wtshop.controller.admin;


import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.LogKit;

import com.wtshop.Message;
import com.wtshop.Pageable;

import com.wtshop.model.Brand;
import com.wtshop.model.Brand.Type;
import com.wtshop.model.Goods;
import com.wtshop.model.Member;
import com.wtshop.model.SpecialPersonnel;

import com.wtshop.service.GoodsService;
import com.wtshop.service.MemberService;
import com.wtshop.service.SpecialPersonnelService;
import com.wtshop.util.ShareCodeUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

import static com.wtshop.api.controller.BaseAPIController.convertToLong;

/**
 * Controller - 品牌
 * 
 * 
 */
@ControllerBind(controllerKey = "/admin/special")
public class SpecialPersonnelController extends BaseController {

	private SpecialPersonnelService brandService = enhance(SpecialPersonnelService.class);
	private GoodsService goodsService = enhance(GoodsService.class);
	private MemberService memberService = enhance(MemberService.class);

	/**
	 * 添加
	 */
	public void add() {
		setAttr("types", Type.values());
		render("/admin/special_personnel/add.ftl");
	}

	/**
	 * 保存
	 */
	public void save() {
		SpecialPersonnel specialPersonnel = getModel(SpecialPersonnel.class);


		   Member member = memberService.findByPhone(specialPersonnel.getPhone());
if(member==null){
	addFlashMessage(Message.errMsg("手机号不存在"));

	redirect("/admin/special/list.jhtml");
}else{
	brandService.save(specialPersonnel);
if(!StringUtils.isNotEmpty(member.getShareCode())){
	String shareCode = ShareCodeUtils.idToCode(member.getId());
	member.setShareCode(shareCode);
	member.setHousekeeperId(2l);
	memberService.update(member);
}
	addFlashMessage(SUCCESS_MESSAGE);
	redirect("/admin/special/list.jhtml");
}

	}

	/**
	 * 编辑
	 */
	public void edit() {
		Long id = getParaToLong("id");
		setAttr("types", Type.values());
		setAttr("specialPersonnel", brandService.find(id));
		render("/admin/special_personnel/edit.ftl");
	}

	/**
	 * 更新
	 */
	public void update() {
		SpecialPersonnel brand = getModel(SpecialPersonnel.class);

		Member member = memberService.findByPhone(brand.getPhone());
		if(member==null){
			addFlashMessage(Message.errMsg("手机号不存在"));

			redirect("/admin/special/list.jhtml");
		}else{

			brandService.update(brand);
			if(!StringUtils.isNotEmpty(member.getShareCode())){
				String shareCode = ShareCodeUtils.idToCode(member.getId());
				member.setShareCode(shareCode);
				member.setHousekeeperId(2l);
				memberService.update(member);

			}
			addFlashMessage(SUCCESS_MESSAGE);
			redirect("/admin/special/list.jhtml");
		}

	}

	/**
	 * 列表
	 */
	public void list() {
		Pageable pageable = getBean(Pageable.class);
		setAttr("page", brandService.findPage(pageable));
		LogKit.info(">" + pageable.getPageNumber());
		setAttr("pageable", pageable);
		render("/admin/special_personnel/list.ftl");
	}

	/**
	 * 删除
	 */
	public void delete() {

		String[] values = StringUtils.split(getPara("ids"), ",");
		Long[] idList = values == null ? null :convertToLong(values);
		if (idList != null) {
			for (Long id : idList) {
				List<Goods> goodsList = goodsService.findByBrandId(id);
				if (goodsList != null &&  goodsList.size() > 0) {
					for(Goods goods : goodsList){
						renderJson(Message.error("admin.brand.deleteExistNotAllowed", goods.getName()));
						return;
					}

				}
			}
			brandService.delete(idList);
		}
		renderJson(SUCCESS_MESSAGE);
	}


	/**
	 * 禁用福袋
	 */
	public void disabled() {
		Long id = getParaToLong("id");
		SpecialPersonnel activity = brandService.find(id);
		activity.setStatus(false);
		brandService.update(activity);
		redirect("/admin/special/list.jhtml");
	}


	/**
	 * 启用福袋
	 */
	public void publish() {
		Long id = getParaToLong("id");
		SpecialPersonnel activity = brandService.find(id);


		activity.setStatus(true);
		brandService.update(activity);
		redirect("/admin/special/list.jhtml");
	}

}