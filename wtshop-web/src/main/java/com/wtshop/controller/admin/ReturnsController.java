package com.wtshop.controller.admin;

import com.google.zxing.common.detector.MathUtils;
import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.wtshop.Pageable;
import com.wtshop.Setting;
import com.wtshop.dao.ReturnsItemDao;
import com.wtshop.model.*;
import com.wtshop.service.*;
import com.wtshop.util.DateUtils;
import com.wtshop.util.MathUtil;
import com.wtshop.util.SystemUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Controller - 退货单
 * 
 * 
 */
@ControllerBind(controllerKey = "/admin/returns")
public class ReturnsController extends BaseController {

	private ReturnsService returnsService = enhance(ReturnsService.class);
	private AdminService adminService = enhance(AdminService.class);
	private DeliveryCorpService deliveryCorpService = enhance((DeliveryCorpService.class));
	private ShippingMethodService shippingMethodService = enhance(ShippingMethodService.class);
	private AreaService areaService = enhance(AreaService.class);
	private ReturnsItemProgressService returnsItemProgressService = enhance(ReturnsItemProgressService.class);
	private RefundsService refundsService = enhance(RefundsService.class);
	private ReturnsItemService returnsItemService = enhance(ReturnsItemService.class);
	private ProductService productService  = enhance(ProductService.class);
	private StockLogService stockLogService = enhance(StockLogService.class);
	/**
	 * 查看
	 */
	public void view() {
		Long id = getParaToLong("id");
		setAttr("returns", returnsService.find(id));
		setAttr("delivers",deliveryCorpService.findAll());
		setAttr("shippingMethods",shippingMethodService.findAll());
		setAttr("area",areaService.findAll());
		render("/admin/returns/view.ftl");
	}

	/**
	 * 列表
	 */
	public void list() {
		Pageable pageable = getBean(Pageable.class);
		Page<Returns> pages = returnsService.findPages(pageable);
		setAttr("pageable", pageable);
		setAttr("page", pages);
		render("/admin/returns/list.ftl");
	}

	/**
	 * 删除
	 */
	public void delete() {
		Long[] ids = getParaValuesToLong("ids");
		for(Long id : ids){
			Returns returns = returnsService.find(id);
			returns.setIsDelete(true);
			returnsService.update(returns);
		}
		renderJson(SUCCESS_MESSAGE);
	}

	/**
	 * 保存退货项数据
	 */
	public void edit(){
		Long sn = getParaToLong("sn");
		Returns returns = returnsService.findReturnsBySn(sn);
		String trackingNo = getPara("trackingNo");
		String freight = getPara("freight");
		String shipper = getPara("shipper");
		String zipCode = getPara("zipCode");
		String address = getPara("address");
		String phone = getPara("phone");
		String memo = getPara("memo");

		Long shippingMethodId = getParaToLong("shippingMethodId");
		Long deliveryCorpId = getParaToLong("deliveryCorpId");
		Long areaId = getParaToLong("areaId");

		returns.setTrackingNo(trackingNo);
		returns.setFreight(new BigDecimal(freight));
		returns.setShipper(shipper);
		returns.setZipCode(zipCode);
		returns.setAddress(address);
		returns.setOperator("admin");
		returns.setAreaId(areaId);
		returns.setPhone(phone);
		returns.setShippingMethod(shippingMethodService.find(shippingMethodId));
		returns.setDeliveryCorp(deliveryCorpService.find(deliveryCorpId));
		returns.setAreaName(areaService.find(areaId));
		returns.setMemo(memo);
		returnsService.update(returns);
		setAttr("returns", returns);
		setAttr("delivers",deliveryCorpService.findAll());
		setAttr("shippingMethods",shippingMethodService.findAll());
		render("/admin/returns/view.ftl");
	}


	/**
	 * 审核
	 */
	@Before(Tx.class)
	public void review() {
		Long id = getParaToLong("id");
		Returns returns = returnsService.find(id);
		List<ReturnsItemProgress> returnsItemProgresses = returnsItemProgressService.findByReturnId(id);
		for( ReturnsItemProgress returnsItemProgress : returnsItemProgresses){
			returnsItemProgress.setStatus(0);
			returnsItemProgressService.update(returnsItemProgress);
		}
		if( returns != null){
			ReturnsItemProgress returnsItemProgress = new ReturnsItemProgress();
			returnsItemProgress.setDesc("您的审核已通过, 正在处理中");
			if(returns.getStatus() != 0){
				returnsItemProgress.setDesc("您的审核已通过, 请将商品寄回");
			}
			returnsItemProgress.setReturnsId(returns.getId());
			returnsItemProgressService.save(returnsItemProgress);
			Admin admin = adminService.getCurrent();
			returns.setOperator(admin);
			returns.setType(Returns.Type.pass.ordinal());
			returns.setModifyDate(DateUtils.getSysDate());
			returnsService.update(returns);
		}
		setAttr("returns", returns);
		setAttr("delivers",deliveryCorpService.findAll());
		setAttr("shippingMethods",shippingMethodService.findAll());
		render("/admin/returns/view.ftl");
	}
	/**
	 * 收货
	 */
	public void returns(){
		Long id = getParaToLong("id");
		Returns returns = returnsService.find(id);
		List<ReturnsItemProgress> returnsItemProgresses = returnsItemProgressService.findByReturnId(id);
		for( ReturnsItemProgress returnsItemProgress : returnsItemProgresses){
			returnsItemProgress.setStatus(0);
			returnsItemProgressService.update(returnsItemProgress);
		}
		if( returns != null){
			ReturnsItemProgress returnsItemProgress = new ReturnsItemProgress();
			returnsItemProgress.setDesc("您的服务单商品已收到");
			returnsItemProgress.setReturnsId(returns.getId());
			returnsItemProgressService.save(returnsItemProgress);
			Admin admin = adminService.getCurrent();
			returns.setOperator(admin);
			returns.setType(Returns.Type.returned.ordinal());
			returns.setModifyDate(DateUtils.getSysDate());
			returnsService.update(returns);
		}
		setAttr("returns", returns);
		setAttr("delivers",deliveryCorpService.findAll());
		setAttr("shippingMethods",shippingMethodService.findAll());
		render("/admin/returns/view.ftl");
	}

	/**
	 *退款
	 */
	@Before(Tx.class)
	public void refund(){
		Long id = getParaToLong("id");
		Returns returns = returnsService.find(id);
		List<ReturnsItemProgress> returnsItemProgresses = returnsItemProgressService.findByReturnId(id);
		for( ReturnsItemProgress returnsItemProgress : returnsItemProgresses){
			returnsItemProgress.setStatus(0);
			returnsItemProgressService.update(returnsItemProgress);
		}
		if( returns != null){
			ReturnsItemProgress returnsItemProgress = new ReturnsItemProgress();
			returnsItemProgress.setDesc("已退款,请注意查收");
			returnsItemProgress.setReturnsId(returns.getId());
			returnsItemProgressService.save(returnsItemProgress);
			Admin admin = adminService.getCurrent();
			returns.setOperator(admin);
			returns.setType(Returns.Type.refund.ordinal());
			returns.setModifyDate(DateUtils.getSysDate());
			returns.update();
			refundsService.saveRefunds(returns);
		}
		setAttr("returns", returns);
		setAttr("delivers",deliveryCorpService.findAll());
		setAttr("shippingMethods",shippingMethodService.findAll());
		render("/admin/returns/view.ftl");
	}

	/**
	 * 已处理，生成新的订单
	 */
	@Before(Tx.class)
	public void handle(){
		Long id = getParaToLong("id");
		Returns returns = returnsService.find(id);
		List<ReturnsItemProgress> returnsItemProgresses = returnsItemProgressService.findByReturnId(id);
		for( ReturnsItemProgress returnsItemProgress : returnsItemProgresses){
			returnsItemProgress.setStatus(0);
			returnsItemProgressService.update(returnsItemProgress);
		}
		if( returns != null){
			ReturnsItemProgress returnsItemProgress = new ReturnsItemProgress();
			returnsItemProgress.setDesc("生成新的订单");
			returnsItemProgress.setReturnsId(returns.getId());
			returnsItemProgressService.save(returnsItemProgress);
			Admin admin = adminService.getCurrent();
			returns.setOperator(admin);
			returns.setType(Returns.Type.handle.ordinal());
			returns.setModifyDate(DateUtils.getSysDate());
			returns.update();

			Order order = returns.getOrder();
			order.setStatus(Order.Status.canceled.ordinal());
			order.update();

		}
		setAttr("returns", returns);
		setAttr("delivers",deliveryCorpService.findAll());
		setAttr("shippingMethods",shippingMethodService.findAll());
		render("/admin/returns/view.ftl");
	}

	/**
	 * 已完成
	 */
	@Before(Tx.class)
	public void complete(){
		Long id = getParaToLong("id");
		Returns returns = returnsService.find(id);
		List<ReturnsItemProgress> returnsItemProgresses = returnsItemProgressService.findByReturnId(id);
		for( ReturnsItemProgress returnsItemProgress : returnsItemProgresses){
			returnsItemProgress.setStatus(0);
			returnsItemProgressService.update(returnsItemProgress);
		}
		if( returns != null){
			ReturnsItemProgress returnsItemProgress = new ReturnsItemProgress();
			returnsItemProgress.setDesc("订单已完成");
			returnsItemProgress.setReturnsId(returns.getId());
			returnsItemProgressService.save(returnsItemProgress);
			Admin admin = adminService.getCurrent();
			returns.setOperator(admin);
			returns.setType(Returns.Type.complete.ordinal());
			returns.setModifyDate(DateUtils.getSysDate());
			returns.update();

			Order order = returns.getOrder();
			order.setStatus(Order.Status.completed.ordinal());
			order.update();

			//增加库存
			if( 0 != returns.getStatus()){
				List<ReturnsItem> returnsItems = returnsItemService.findByReturnId(returns.getId());
				if(returnsItems != null && returnsItems.size() > 0){
					for(ReturnsItem returnsItem : returnsItems){
						Product product = productService.find(returnsItem.getProductId());
						product.setStock(product.getStock()+ returnsItem.getQuantity());
						productService.update(product);

						StockLog stockLog = new StockLog();
						stockLog.setStock(returnsItem.getQuantity());
						stockLog.setInQuantity(returnsItem.getQuantity());
						stockLog.setOutQuantity(0);
						stockLog.setType(0);
						stockLog.setProductId(returnsItem.getProductId());
						stockLogService.save(stockLog);

					}


				}
			}



		}
		setAttr("returns", returns);
		setAttr("delivers",deliveryCorpService.findAll());
		setAttr("shippingMethods",shippingMethodService.findAll());
		render("/admin/returns/view.ftl");
	}



}