package com.wtshop.api.common.result.member;

import com.jfinal.plugin.activerecord.Page;
import com.wtshop.model.CommissionLog;
import com.wtshop.model.DepositLog;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created by sq on 2017/5/16.
 */
@ApiModel(value = "预存款记录", description = "DepositLogResult")
public class CommissionLogResult implements Serializable {

    @ApiModelProperty(value = "预存款记录", dataType = "DepositLog")
    private Page<CommissionLog> page;

    public CommissionLogResult(Page<CommissionLog> page) {
        this.page = page;
    }

    public Page<CommissionLog> getPage() {
        return page;
    }

    public void setPage(Page<CommissionLog> page) {
        this.page = page;
    }
}
