package com.wtshop.controller.admin;

import com.jfinal.ext.route.ControllerBind;
import com.wtshop.Message;
import com.wtshop.Pageable;
import com.wtshop.constants.Code;
import com.wtshop.dao.TicketconfigDao;
import com.wtshop.model.Ticketconfig;
import com.wtshop.service.ActService;
import com.wtshop.util.ApiResult;
import org.apache.commons.collections.map.HashedMap;

import java.util.Map;

/**
 * Controller - 优惠券
 * 
 * 
 */
@ControllerBind(controllerKey = "/admin/ticketConfig")
public class TicketConfigController extends BaseController {

	private TicketconfigDao tcd=enhance(TicketconfigDao.class);


	public void add() {
		render("/admin/ticketConfig/add.ftl");
	}
	/**
	 * 保存
	 */
	public void save() {
		Ticketconfig cs = getModel(Ticketconfig.class);
		Map<String,String> sqlmap=new HashedMap();
		sqlmap.put("eTime","endTime");
		sqlmap.put("condition","and  state=0");
		sqlmap.put("table","ticketconfig");
		ApiResult ar= ActService.addCheckActTime(cs.getBeginTime(),cs.getEndTime(),sqlmap);
		if (ar.getCode()== Code.FAIL){
			addFlashMessage(new Message(Message.Type.error,ar.getMsg()));
		}else {
			tcd.save(cs);
			addFlashMessage(SUCCESS_MESSAGE);
		}
		redirect("list.jhtml");
	}

	/**
	 * 编辑
	 */
	public void edit() {
		Long id = getParaToLong("id");
		Ticketconfig cs=tcd.find(id);
		setAttr("ticketconfig", cs);
		render("/admin/ticketConfig/edit.ftl");
	}

	/**
	 * 更新
	 */
	public void update() {
		Ticketconfig coupon = getModel(Ticketconfig.class);
		Map<String,String> sqlmap=new HashedMap();
		sqlmap.put("eTime","endTime");
		sqlmap.put("condition","and  state=0");
		sqlmap.put("table","ticketconfig");
		ApiResult ar= ActService.updateCheckActTime(coupon.getBeginTime(),coupon.getEndTime(),coupon.getId(),sqlmap);
		if (ar.getCode()== Code.FAIL){
			addFlashMessage(new Message(Message.Type.error,ar.getMsg()));
		}else {

			tcd.update(coupon);
			addFlashMessage(SUCCESS_MESSAGE);
		}

		redirect("list.jhtml");
	}

	/**
	 * 列表
	 */
	public void list() {
		Pageable pageable = getBean(Pageable.class);
		setAttr("pageable", pageable);
		setAttr("page", 		tcd.findPage(pageable));
		render("/admin/ticketConfig/list.ftl");
	}


	/**
	 * 编辑
	 */
	public void delete() {
		Long[] ids = getParaValuesToLong("ids");
		if (ids != null) {
			for (Long id : ids) {
				 Ticketconfig cfg=  tcd.find(id);
				 cfg.setState(1);
				 tcd.update(cfg);
			}
		}
		redirect("list.jhtml");
	}









}