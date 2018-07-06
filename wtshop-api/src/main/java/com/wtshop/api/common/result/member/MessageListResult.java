package com.wtshop.api.common.result.member;

import com.jfinal.plugin.activerecord.Page;
import com.wtshop.model.DepositLog;
import com.wtshop.model.Message;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by sq on 2017/5/17.
 */
@ApiModel(value = "消息列表", description = "MessageListResult")
public class MessageListResult {

    @ApiModelProperty(value = "消息", dataType = "Message")
    private  Page<Message> page;

    @ApiModelProperty(value = "阅读", dataType = "Object")
    private  Object read;

    public MessageListResult(Page<Message> page, Object read) {
        this.page = page;
        this.read = read;
    }

    public Page<Message> getPage() {
        return page;
    }

    public void setPage(Page<Message> page) {
        this.page = page;
    }

    public Object getRead() {
        return read;
    }

    public void setRead(Object read) {
        this.read = read;
    }
}
