package com.wtshop.api.common.result;

import com.wtshop.model.ProductCategory;
import io.swagger.annotations.ApiModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by sq on 2017/5/17.
 */
@ApiModel(value = "商品分类", description = "ProductCategoryResult")
public class ProductCategoryResult {

    private List<ProductCategoryModel> list ;

    public ProductCategoryResult(List<ProductCategoryModel> list) {
        this.list = list;
    }

    public List<ProductCategoryModel> getList() {
        return list;
    }

    public void setList(List<ProductCategoryModel> list) {
        this.list = list;
    }
}
