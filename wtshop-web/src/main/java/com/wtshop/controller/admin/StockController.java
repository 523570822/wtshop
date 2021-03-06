package com.wtshop.controller.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wtshop.model.*;
import com.wtshop.service.*;
import org.apache.commons.lang3.StringUtils;

import com.jfinal.ext.route.ControllerBind;
import com.wtshop.Pageable;

/**
 * Controller - 库存
 * 
 * 
 */
@ControllerBind(controllerKey = "/admin/stock")
public class StockController extends BaseController {

	private StockLogService stockLogService = enhance(StockLogService.class);
	private ProductService productService = enhance(ProductService.class);
	private AdminService adminService = enhance(AdminService.class);
	private GoodsService goodsService = enhance(GoodsService.class);

	/**
	 * 商品选择
	 */
	public void productSelect() {
		String keyword = getPara("q");
		Integer count = getParaToInt("limit");
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		if (StringUtils.isEmpty(keyword)) {
			renderJson(data);
			return;
		}
		List<Product> products = productService.search(null, keyword, null, count);
		for (Product product : products) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("id", product.getId());
			item.put("sn", product.getSn());
			item.put("name", product.getName());
			item.put("stock", product.getStock());
			item.put("allocatedStock", product.getAllocatedStock());
			item.put("specifications", product.getSpecifications());
			data.add(item);
		}
		renderJson(data);
	}

	/**
	 * 入库
	 */
	public void stockIn() {
		Long productId = getParaToLong("productId");
		setAttr("product", productService.find(productId));
		render("/admin/stock/stock_in.ftl");
	}

	/**
	 * 入库
	 */
	public void stockInSubmit() {
		Long productId = getParaToLong("productId");
		Integer quantity = getParaToInt("quantity");
		Goods goods = goodsService.findGoodsByProductId(productId);
		String memo = getPara("memo");
		Product product = productService.find(productId);
		if (product == null) {
			redirect(ERROR_VIEW);
			return;
		}
		if (quantity == null || quantity <= 0) {
			redirect(ERROR_VIEW);
			return;
		}
		Admin admin = adminService.getCurrent();
		productService.addStock(product, quantity, StockLog.Type.stockIn, admin, memo);
		addFlashMessage(SUCCESS_MESSAGE);
		redirect("log.jhtml");
	}

	/**
	 * 出库
	 */
	public void stockOut() {
		Long productId = getParaToLong("productId");
		setAttr("product", productService.find(productId));
		render("/admin/stock/stock_out.ftl");
	}

	/**
	 * 出库
	 */
	public void stockOutSubmit() {
		Long productId = getParaToLong("productId");
		Integer quantity = getParaToInt("quantity");
		String memo = getPara("memo");
		Product product = productService.find(productId);
		if (product == null) {
			redirect(ERROR_VIEW);
			return;
		}
		if (quantity == null || quantity <= 0) {
			redirect(ERROR_VIEW);
			return;
		}
		if (product.getStock() - quantity < 0) {
			redirect(ERROR_VIEW);
			return;
		}
		Admin admin = adminService.getCurrent();
		productService.addStock(product, -quantity, StockLog.Type.stockOut, admin, memo);
		addFlashMessage(SUCCESS_MESSAGE);
		redirect("log.jhtml");
	}

	/**
	 * 记录
	 */
	public void log() {
		Pageable pageable = getBean(Pageable.class);
		setAttr("page", stockLogService.findPageList(pageable));
		setAttr("pageable", pageable);
		render("/admin/stock/log.ftl");
	}

}