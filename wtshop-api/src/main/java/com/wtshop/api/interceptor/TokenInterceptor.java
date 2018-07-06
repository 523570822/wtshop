package com.wtshop.api.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;
import com.wtshop.RequestContextHolder;
import com.wtshop.api.common.bean.BaseResponse;
import com.wtshop.api.common.token.TokenManager;
import com.wtshop.constants.Code;
import com.wtshop.model.Member;
import com.wtshop.service.MemberService;
import com.wtshop.util.ApiResult;
import com.wtshop.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

import static com.jfinal.aop.Enhancer.enhance;

public class TokenInterceptor implements Interceptor {

    private MemberService memberService = enhance(MemberService.class);

    public void intercept(Invocation i) {
        Controller controller = i.getController();
        HttpServletRequest request = RequestContextHolder.currentRequestAttributes();
        String token = request.getHeader("token");
        if (StringUtils.isEmpty(token)) {
            controller.renderJson(new BaseResponse(Code.ARGUMENT_ERROR, "Token不能为空！"));
            return;
        }

        String userId = RedisUtil.getString(token);
        String newToken = RedisUtil.getString("TOKEN:" + userId);
        if(StringUtils.isEmpty(newToken)){
            controller.renderJson(new ApiResult(Code.ARGUMENT_ERROR, "Token不能为空！"));
            return;
        }

        if (!newToken.equals(token)) {
            controller.renderJson(new ApiResult(Code.TOKEN_INVALID, "Token失效"));
            return;
        }
        i.invoke();
    }
}
