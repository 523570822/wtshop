package com.wtshop.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.wtshop.model.Cart;
import com.wtshop.util.WebUtils;

/**
 * Interceptor - 购物车数量
 * 
 * 
 */
public class CartQuantityInterceptor implements Interceptor {

	/**
	 * 请求前处理
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @param handler
	 *            处理器
	 * @return 是否继续执行
	 */
	@Override
	public void intercept(Invocation inv) {
		Controller controller = inv.getController();
		WebUtils.removeCookie(controller.getRequest(), controller.getResponse(), Cart.QUANTITY_COOKIE_NAME);
		inv.invoke();
	}

}