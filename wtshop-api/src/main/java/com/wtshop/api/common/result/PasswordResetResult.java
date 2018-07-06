package com.wtshop.api.common.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by sq on 2017/5/16.
 */
@ApiModel(value = "密码重置", description = "PasswordResetResult")
public class PasswordResetResult {

    @ApiModelProperty(value = "安全码", dataType = "String")
    private String key;

    @ApiModelProperty(value = "用户姓名", dataType = "String")
    private String username;

    @ApiModelProperty(value = "名称", dataType = "String")
    private String title;

    public PasswordResetResult(String key, String username, String titime) {
        this.key = key;
        this.username = username;
        this.title = titime;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitime() {
        return title;
    }

    public void setTitime(String titime) {
        this.title = titime;
    }
}
