package com.wtshop.api.utils;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Created by weitong on 17/5/15.
 */
public class SwaggerUtil {

    private static SwaggerUtil instance;
    private Docket docket;

    public Docket createRestApi() {
        String packageString = StringUtils.removeEnd(ClassUtils.getPackageName(SwaggerUtil.class), "utils") + "controller";
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(packageString))
                .build();
    }
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("接口服务文档")
                .description("接口服务文档")
                .contact("WangtianSoft")
                .version("1.0")
                .build();
    }

    private SwaggerUtil() {
        this.docket = createRestApi();
    }

    public static SwaggerUtil getInstance() {
        if (instance == null)
            instance = new SwaggerUtil();
        return instance;
    }

}
