package com.wtshop.api.common.result.member;

import com.wtshop.model.Member;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by sq on 2017/5/17.
 */

@ApiModel(value = "个人资料修改", description = "MerberEditResult")
public class MerberEditResult {

    @ApiModelProperty(value = "会员", dataType = "Member")
    private Member member;

    public MerberEditResult(Member member) {
        this.member = member;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
