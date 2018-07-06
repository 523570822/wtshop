package com.wtshop.service;

import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.render.FreeMarkerRender;
import com.wtshop.CommonAttributes;
import com.wtshop.Setting;
import com.wtshop.util.SystemUtils;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModelException;

/**
 * Service - 配置
 * 
 * 
 */
public class ConfigService {

	Prop prop = PropKit.use(CommonAttributes.wtshop_PROPERTIES_PATH);
	private String templateUpdateDelay = prop.get("template.update_delay");
	//private Integer messageCacheSeconds = prop.getInt("message.cache_seconds");

	//private Configuration freeMarkerConfigurer = FreeMarkerRender.getConfiguration();
	//@Resource(name = "messageSource")
	//private ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource;
	//@Resource(name = "localeResolver")
	//private FixedLocaleResolver fixedLocaleResolver;
	
	/**
	 * 初始化
	 */
	public void init() {
		try {
			Setting setting = SystemUtils.getSetting();
			setting.setSmtpPassword(null);
			setting.setKuaidi100Key(null);
			setting.setCnzzPassword(null);
			setting.setSmsKey(null);
//			ProxyFactory proxyFactory = new ProxyFactory(setting);
//			proxyFactory.setProxyTargetClass(true);
//			proxyFactory.addAdvice(new MethodBeforeAdvice() {
//
//				public void before(Method method, Object[] args, Object target) throws Throwable {
//					if (StringUtils.startsWith(method.getName(), "set")) {
//						throw new UnsupportedOperationException("Operation not supported");
//					}
//				}
//
//			});
			Configuration configuration = FreeMarkerRender.getConfiguration();
			//configuration.setSharedVariable("setting", proxyFactory.getProxy());
			configuration.setSharedVariable("locale", setting.getLocale());
			configuration.setSharedVariable("theme", setting.getTheme());
			if (setting.getIsDevelopmentEnabled()) {
				configuration.setSetting("template_update_delay", "0");
				//reloadableResourceBundleMessageSource.setCacheSeconds(0);
			} else {
				configuration.setSetting("template_update_delay", templateUpdateDelay);
				//reloadableResourceBundleMessageSource.setCacheSeconds(messageCacheSeconds);
			}
			//fixedLocaleResolver.setDefaultLocale(LocaleUtils.toLocale(setting.getLocale().toString()));
		} catch (TemplateModelException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (TemplateException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

}