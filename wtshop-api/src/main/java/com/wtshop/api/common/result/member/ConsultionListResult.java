package com.wtshop.api.common.result.member;

import com.jfinal.plugin.activerecord.Page;
import com.wtshop.model.Consultation;
import com.wtshop.model.ProductCategory;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created by sq on 2017/5/16.
 */
@ApiModel(value = "咨询列表", description = "ConsultionListResult")
public class ConsultionListResult implements Serializable{

    @ApiModelProperty(value = "咨询", dataType = "Consultation")
    private Page<Consultation> consultation;

    public ConsultionListResult(Page<Consultation> consultation) {
        this.consultation = consultation;
    }

    public Page<Consultation> getConsultation() {
        return consultation;
    }

    public void setConsultation(Page<Consultation> consultation) {
        this.consultation = consultation;
    }
}
