package com.wtshop.controller.shop;

import java.util.HashMap;
import java.util.Map;

import com.jfinal.ext.route.ControllerBind;
import com.wtshop.Message;
import com.wtshop.model.Member;
import com.wtshop.model.Product;
import com.wtshop.model.ProductNotify;
import com.wtshop.service.MemberService;
import com.wtshop.service.ProductNotifyService;
import com.wtshop.service.ProductService;

/**
 * Controller - 到货通知
 * 
 * 
 */
@ControllerBind(controllerKey = "/product_notify")
public class ProductNotifyController extends BaseController {

	private ProductNotifyService productNotifyService = enhance(ProductNotifyService.class);
	private MemberService memberService = enhance(MemberService.class);
	private ProductService productService = enhance(ProductService.class);

	/**
	 * 获取当前会员E-mail
	 */
	public void email() {
		Member member = memberService.getCurrent();
		String email = member != null ? member.getEmail() : null;
		Map<String, String> data = new HashMap<String, String>();
		data.put("email", email);
		renderJson(data);
	}

	/**
	 * 保存
	 */
	public void save() {
		String email = getPara("email");
		Long productId = getParaToLong("productId");
		
		Map<String, Object> data = new HashMap<String, Object>();
		
		Product product = productService.find(productId);
		if (product == null) {
			data.put("message", Message.warn("shop.productNotify.productNotExist"));
			renderJson(data);
			return;
		}
		if (!product.getIsMarketable()) {
			data.put("message", Message.warn("shop.productNotify.productNotMarketable"));
			renderJson(data);
			return;
		}
		if (!product.getIsOutOfStock()) {
			data.put("message", Message.warn("shop.productNotify.productInStock"));
			renderJson(data);
			return;
		}
		if (productNotifyService.exists(product, email)) {
			data.put("message", Message.warn("shop.productNotify.exist"));
			renderJson(data);
			return;
		}
		Member member = memberService.getCurrent();
//		ProductNotify productNotify = new ProductNotify();
//		productNotify.setHasSent(false);
//		productNotify.setMemberId(member != null ? member.getId() : null);
//		productNotify.setProductId(product.getId());
//		productNotifyService.save(productNotify);
		data.put("message", SUCCESS_MESSAGE);
		renderJson(data);
	}

}