package com.wtshop.api.common.result;

import com.wtshop.model.Cart;
import com.wtshop.model.CartItem;
import com.wtshop.model.Goods;
import com.wtshop.model.Product;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sq on 2017/5/17.
 */

public class CartListResult implements Serializable{


    private List<CartGoodsResult> CartGoodsResult;

    private List<Goods> cartList;


    private Double subtract;

    private Double promSubtract;


    public CartListResult(List<com.wtshop.api.common.result.CartGoodsResult> cartGoodsResult, List<Goods> cartList, Double subtract, Double promSubtract) {
        CartGoodsResult = cartGoodsResult;
        this.cartList = cartList;
        this.subtract = subtract;
        this.promSubtract = promSubtract;
    }

    public List<Goods> getCartList() {
        return cartList;
    }

    public void setCartList(List<Goods> cartList) {
        this.cartList = cartList;
    }

    public List<com.wtshop.api.common.result.CartGoodsResult> getCartGoodsResult() {
        return CartGoodsResult;
    }

    public void setCartGoodsResult(List<com.wtshop.api.common.result.CartGoodsResult> cartGoodsResult) {
        CartGoodsResult = cartGoodsResult;
    }

    public Double getPromSubtract() {
        return promSubtract;
    }

    public void setPromSubtract(Double promSubtract) {
        this.promSubtract = promSubtract;
    }


    public Double getSubtract() {
        return subtract;
    }

    public void setSubtract(Double subtract) {
        this.subtract = subtract;
    }


}
