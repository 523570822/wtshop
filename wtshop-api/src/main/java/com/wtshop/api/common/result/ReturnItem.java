package com.wtshop.api.common.result;

import com.wtshop.model.Goods;
import com.wtshop.model.Returns;
import com.wtshop.model.ReturnsItem;

import java.util.List;

/**
 * Created by sq on 2017/6/7.
 */
public class ReturnItem {

    private Returns returns;

    private List<ReturnsItem> returnsItem;

    public ReturnItem(Returns returns, List<ReturnsItem> returnsItem) {
        this.returns = returns;
        this.returnsItem = returnsItem;
    }

    public Returns getReturns() {
        return returns;
    }

    public void setReturns(Returns returns) {
        this.returns = returns;
    }

    public List<ReturnsItem> getReturnsItem() {
        return returnsItem;
    }

    public void setReturnsItem(List<ReturnsItem> returnsItem) {
        this.returnsItem = returnsItem;
    }
}
