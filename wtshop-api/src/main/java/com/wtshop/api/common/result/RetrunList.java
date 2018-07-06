package com.wtshop.api.common.result;

import com.wtshop.model.Returns;
import com.wtshop.model.ReturnsItem;

import java.util.List;

/**
 * Created by sq on 2017/7/24.
 */
public class RetrunList {

    private Returns aReturn;

    private List<ReturnsItem> returnItemList;

    public RetrunList(Returns aReturn, List<ReturnsItem> returnItemList) {
        this.aReturn = aReturn;
        this.returnItemList = returnItemList;
    }

    public Returns getaReturn() {
        return aReturn;
    }

    public void setaReturn(Returns aReturn) {
        this.aReturn = aReturn;
    }

    public List<ReturnsItem> getReturnItemList() {
        return returnItemList;
    }

    public void setReturnItemList(List<ReturnsItem> returnItemList) {
        this.returnItemList = returnItemList;
    }
}
