package com.wtshop.api.common.result;

import com.wtshop.model.ProductCategory;

import java.util.List;

/**
 * Created by sq on 2017/7/6.
 */
public class ProductCategoryModel {

    private String product_name;

    private List<ProductCategory> product;

    public ProductCategoryModel(String product_name, List<ProductCategory> product) {
        this.product_name = product_name;
        this.product = product;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public List<ProductCategory> getProduct() {
        return product;
    }

    public void setProduct(List<ProductCategory> product) {
        this.product = product;
    }
}
