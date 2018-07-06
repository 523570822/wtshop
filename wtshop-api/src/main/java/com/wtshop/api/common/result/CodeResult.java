package com.wtshop.api.common.result;

import java.io.Serializable;

/**
 * 描述：
 *
 * @author Shi Qiang
 * @date 2018/5/25 14:44
 */
public class CodeResult implements Serializable{

    private int code;

    private String token;

    private Long accountId;

    public CodeResult(int code, String token, Long accountId) {
        this.code = code;
        this.token = token;
        this.accountId = accountId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
