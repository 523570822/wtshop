package com.wtshop.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.wtshop.RequestContextHolder;
import com.wtshop.shiro.core.SubjectKit;

public class AdminInterceptor implements Interceptor {
	
	public void intercept(Invocation ai) {
		Controller controller = ai.getController();
		RequestContextHolder.setRequestAttributes(controller.getRequest());
		if (SubjectKit.isAuthed()) {
			ai.invoke();
		} else {
			controller.redirect("/admin/login.jhtml");
		}
	}

}
