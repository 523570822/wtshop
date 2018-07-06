package com.wtshop.interceptor;

import org.apache.commons.lang3.StringUtils;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.render.Render;
import com.wtshop.CommonAttributes;
import com.wtshop.RequestContextHolder;
import com.wtshop.Setting;
import com.wtshop.util.SystemUtils;

public class ThemeInterceptor implements Interceptor {

	@Override
	public void intercept(Invocation inv) {
		Controller controller = inv.getController();
		RequestContextHolder.setRequestAttributes(controller.getRequest());
		inv.invoke();
		Render render = controller.getRender();
		String view = render.getView();
		if (StringUtils.containsAny(view, CommonAttributes.THEME_NAME)) {
			Setting setting = SystemUtils.getSetting();
			String theme = setting.getTheme();
			controller.setAttr("theme", theme);
			render.setView(StringUtils.replace(render.getView(), CommonAttributes.THEME_NAME, theme));
		}
	}

}
