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
    private String   openid;
    private Double   sendIntegra;

    public Double getSendIntegra() {
        return sendIntegra;
    }

    public void setSendIntegra(Double sendIntegra) {
        this.sendIntegra = sendIntegra;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    private String   unionid;

    public CodeResult(int code, String token, Long accountId,String shareCode,String openid,String unionid,Double sendIntegra) {
        this.code = code;
        this.token = token;
        this.accountId = accountId;
        this.shareCode = shareCode;
        this.openid = openid;
        this.unionid = unionid;
        this.sendIntegra = sendIntegra;
    }
    private String shareCode;

    public String getShareCode() { return shareCode; }

    public void setShareCode(String shareCode) { this.shareCode = shareCode; }

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
