package com.wtshop.api.common.result;

import java.io.Serializable;

/**
 * Created by sq on 2017/5/17.
 */
public class LoginIndexResult implements Serializable{

    private String url;

    public LoginIndexResult(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
