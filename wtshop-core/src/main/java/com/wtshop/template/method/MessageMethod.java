package com.wtshop.template.method;

import com.jfinal.i18n.I18n;
import com.jfinal.i18n.Res;
import com.wtshop.util.FreeMarkerUtils;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.utility.DeepUnwrap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 模板方法 - 多语言
 * 
 * 
 */
public class MessageMethod implements TemplateMethodModelEx {

	private final static Logger _logger = LoggerFactory.getLogger(MessageMethod.class);
	/**
	 * 执行
	 * 
	 * @param arguments
	 *            参数
	 * @return 结果
	 */
	@SuppressWarnings("rawtypes")
	public Object exec(List arguments) throws TemplateModelException {
		String code = FreeMarkerUtils.getArgument(0, String.class, arguments);
		if (StringUtils.isNotEmpty(code)) {
			String message = "-";
			try {
				Res resUtil = I18n.use();
				if (arguments.size() > 1) {
					Object[] args = new Object[arguments.size() - 1];
					for (int i = 1; i < arguments.size(); i++) {
						Object argument = arguments.get(i);
						if (argument != null && argument instanceof TemplateModel) {
							args[i - 1] = DeepUnwrap.unwrap((TemplateModel) argument);
						} else {
							args[i - 1] = argument;
						}
					}
					message = resUtil.format(code, args);
				} else {
					message = resUtil.get(code);
					//message= ReadProper.getI18n(code);
				}
			}catch (Exception ex){
				_logger.error("获取message信息失败: {}", code);
			}
			return new SimpleScalar(message);
		}
		return null;
	}

}