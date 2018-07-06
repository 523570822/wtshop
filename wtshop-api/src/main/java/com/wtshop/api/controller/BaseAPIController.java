package com.wtshop.api.controller;

import com.jfinal.core.Controller;
import com.wtshop.api.common.bean.BaseResponse;
import com.wtshop.api.common.bean.DataResponse;
import com.wtshop.api.common.bean.Require;
import com.wtshop.api.common.token.TokenManager;
import com.wtshop.constants.Code;
import com.wtshop.exception.AppRuntimeException;
import com.wtshop.model.Member;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Array;
import java.util.List;


public class BaseAPIController extends Controller {

    /**
     * 将String数组转换为Long类型数组
     */
    public static Long[] convertToLong(String[] strs) {
        Long[] longs = new Long[strs.length];
        for (int i = 0; i < strs.length; i++) {
            try {
                String str = strs[i];
                Long thelong = Long.parseLong(str);
                longs[i] = thelong;
            } catch (NumberFormatException e) {
            }
        }
        return longs;
    }

    /**
     * 将String数组转换为Double类型数组
     */
    public static Double[] convertToDouble(String[] strs) {
        Double[] longs = new Double[strs.length];
        for (int i = 0; i < strs.length; i++) {
            try {
                String str = strs[i];
                Double thelong = Double.parseDouble(str);
                longs[i] = thelong;
            } catch (NumberFormatException e) {
            }
        }
        return longs;
    }

    /**
     * 获取当前用户对象

     */
    protected Member getMember() {
        Member member = getSessionAttr(Member.PRINCIPAL_ATTRIBUTE_NAME);
        if (member == null) {
            String token = getPara("token");
            return StringUtils.isEmpty(token) ? null : TokenManager.getMe().validate(token);
        }
        return member;
    }

    /**
     * 响应接口不存在*
     */
    public void render404() {
        renderJson(new BaseResponse(Code.NOT_FOUND));
    }

    /**
     * 响应请求参数有误*
     */
    public void renderArgumentError(String message) {
        renderJson(new BaseResponse(Code.ARGUMENT_ERROR, message));
    }

    /**
     * 响应数组类型*
     */
    public void renderDataResponse(List<?> list) {
        DataResponse resp = new DataResponse();
        resp.setData(list);
        if (CollectionUtils.isEmpty(list)) {
            resp.setMessage("未查询到数据");
        } else {
            resp.setMessage("success");
        }
        renderJson(resp);
    }

    /**
     * 响应操作成功*
     */
    public void renderSuccess(String message) {
        renderJson(new BaseResponse().setMessage(message));        
    }

    /**
     * 响应操作失败*
     */
    public void renderFailed(String message) {
        renderJson(new BaseResponse(Code.FAIL, message));    
    }
    
    /**
     * 判断请求类型是否相同*
     */
    protected boolean methodType(String name) {
        return getRequest().getMethod().equalsIgnoreCase(name);
    }
    
    /**
     * 判断参数值是否为空
     */
    public boolean notNull(Require rules) {
        if (rules == null || rules.getLength() < 1) {
            return true;
        }

        for (int i = 0, total = rules.getLength(); i < total; i++) {
            Object key = rules.get(i);
            String message = rules.getMessage(i);
            BaseResponse response = new BaseResponse(Code.ARGUMENT_ERROR);
            
            if (key == null) {
                renderJson(response.setMessage(message));
                return false;
            }

            if (key instanceof String && StringUtils.isEmpty((String) key)) {
                renderJson(response.setMessage(message));
                return false;
            }

            if (key instanceof Array) {
                Object[] arr = (Object[]) key;

                if (arr.length < 1) {
                    renderJson(response.setMessage(message));
                    return false;
                }
            }
        }
        return true;
    }


    public String getApiParaAndCheck(String name, String msg) {
        String value = getPara(name);
        if (StringUtils.isEmpty(value)){
            throw new AppRuntimeException(Code.API_ERROR_ARGUMENT_NOT_EXIST, String.format("参数[%s]不能为空", name));
        }
        return value;
    }

}
