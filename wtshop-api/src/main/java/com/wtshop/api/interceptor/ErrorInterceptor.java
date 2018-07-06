package com.wtshop.api.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.wtshop.util.ApiResult;
import com.wtshop.exception.AppRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ErrorInterceptor implements Interceptor {
    private static final Logger logger = LoggerFactory.getLogger(ErrorInterceptor.class);
    public void intercept(Invocation i) {
        try {
            i.invoke();
        } catch (AppRuntimeException appEx) {
            logger.error(appEx.getMessage());
            i.getController().renderJson(ApiResult.fail(appEx.getCode(), appEx.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            i.getController().renderJson(ApiResult.fail());
        }
    }
}
