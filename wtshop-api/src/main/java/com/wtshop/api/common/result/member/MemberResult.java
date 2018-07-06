package com.wtshop.api.common.result.member;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/9 0009.
 */
public class MemberResult implements Serializable {
    private String id;
    private String username;
    private String phone;

    public MemberResult() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
