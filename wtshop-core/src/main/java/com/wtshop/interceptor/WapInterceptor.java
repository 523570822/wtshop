package com.wtshop.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.wtshop.RequestContextHolder;

public class WapInterceptor implements Interceptor {

	@Override
	public void intercept(Invocation inv) {
		Controller controller = inv.getController();
		RequestContextHolder.setRequestAttributes(controller.getRequest());
		inv.invoke();
	}

}
