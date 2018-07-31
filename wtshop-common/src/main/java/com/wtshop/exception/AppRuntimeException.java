package com.wtshop.exception;

import com.wtshop.constants.Code;

/**
 * Created by Administrator on 2017/5/23 0023.
 */
public class AppRuntimeException extends RuntimeException{

    private Integer code;
    private Object data;

    public AppRuntimeException() {
    }

    public AppRuntimeException(String message) {
        super(message);
        this.code = Code.SUCCESS;
    }

    public AppRuntimeException(Integer code, String message) {
        super(message);
        this.code = code;
    }
    public AppRuntimeException(Integer code, String message,Object data) {

        super(message);
        this.data=data;
        this.code = code;
    }
    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
