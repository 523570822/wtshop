package com.wtshop.util;

import com.alibaba.fastjson.JSONObject;
import com.wtshop.constants.Code;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * Created by weitong on 17/5/15.
 */
public class ApiResult implements Serializable {

    private Integer code;
    private String msg;
    private Object data;

    public ApiResult() {
        this.code = Code.SUCCESS;
        this.msg = "";
    }

    public ApiResult(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ApiResult(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ApiResult(Integer code, String msg, String key, Object data) {
        this.code = code;
        this.msg = msg;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(key, data);
        this.data = jsonObject;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }


    //  默认方法

    public static ApiResult success(){
        return successMsg("请求成功");
    }


    public static ApiResult successJson(){
        return new ApiResult(Code.SUCCESS, "请求成功", new JSONObject());
    }

    public static ApiResult successMsg(String msg){
        ApiResult apiBean = new ApiResult(Code.SUCCESS, msg, null);
        return apiBean;
    }

    public static ApiResult success(Object data, String msg){
        ApiResult apiBean = new ApiResult(Code.SUCCESS, msg, data);
        return apiBean;
    }

    public static ApiResult success(String key, Object data, String msg){
        ApiResult apiBean = new ApiResult(Code.SUCCESS, msg, key, data);
        return apiBean;
    }

    public static ApiResult success(Object data){
        ApiResult apiBean = new ApiResult(Code.SUCCESS, "", data);
        return apiBean;
    }

    public static ApiResult apiSuccess(Object data){
        ApiResult apiBean = new ApiResult(Code.API_SUCCESS, "", data);
        return apiBean;
    }


    public static ApiResult fail(){
        return fail("请求失败");
    }

    public static ApiResult fail(String msg){
        ApiResult apiBean = new ApiResult(Code.FAIL, msg);
        return apiBean;
    }

    public static ApiResult fail(String msg,Object data){
        ApiResult apiBean = new ApiResult(Code.FAIL, msg,data);
        return apiBean;
    }

    public static ApiResult fail(Integer code, String msg){
        ApiResult apiBean = new ApiResult(code, msg);
        return apiBean;
    }


    public ApiResult and(String key, Object val){
        if (StringUtils.isNotEmpty(key) && !ObjectUtils.isEmpty(val)){
            ((JSONObject)this.data).put(key, val);
        }
        return this;
    }

    public boolean resultSuccess(){
        return this.getCode() == Code.SUCCESS;
    }


}
