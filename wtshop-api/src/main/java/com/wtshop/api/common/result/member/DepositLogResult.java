package com.wtshop.api.common.result.member;

import com.jfinal.plugin.activerecord.Page;
import com.wtshop.model.DepositLog;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created by sq on 2017/5/16.
 */
@ApiModel(value = "预存款记录", description = "DepositLogResult")
public class DepositLogResult implements Serializable {

    @ApiModelProperty(value = "预存款记录", dataType = "DepositLog")
    private Page<DepositLog> page;

    public DepositLogResult(Page<DepositLog> page) {
        this.page = page;
    }

    public Page<DepositLog> getPage() {
        return page;
    }

    public void setPage(Page<DepositLog> page) {
        this.page = page;
    }
}
