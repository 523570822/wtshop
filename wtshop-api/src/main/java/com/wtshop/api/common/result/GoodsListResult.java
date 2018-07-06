package com.wtshop.api.common.result;


import com.jfinal.plugin.activerecord.Page;
import com.wtshop.model.Goods;
import com.wtshop.model.ProductCategory;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sq on 2017/5/16.
 */

@ApiModel(value = "货物列表", description = "GoodsListResult")
public class GoodsListResult  implements Serializable {

    @ApiModelProperty(value = "名称", dataType = "String")
    private String title;

    @ApiModelProperty(value = "商品当前分类信息", dataType = "ProductCategoryModel")
    private ProductCategory productCategory;

    @ApiModelProperty(value = "商品当前分类子类", dataType = "ProductCategoryModel")
    private List<ProductCategory> thirdRoots;

    @ApiModelProperty(value = "排序", dataType = "Object")
    private Object orderType;

    @ApiModelProperty(value = "分页", dataType = "Goods")
    private Page<Goods> page;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }

    public List<ProductCategory> getThirdRoots() {
        return thirdRoots;
    }

    public void setThirdRoots(List<ProductCategory> thirdRoots) {
        this.thirdRoots = thirdRoots;
    }

    public Object getOrderType() {
        return orderType;
    }

    public void setOrderType(Object orderType) {
        this.orderType = orderType;
    }

    public Page<Goods> getPage() {
        return page;
    }

    public void setPage(Page<Goods> page) {
        this.page = page;
    }

    public GoodsListResult(String title, ProductCategory productCategory, List<ProductCategory> thirdRoots, Object orderType, Page<Goods> page) {
        this.title = title;
        this.productCategory = productCategory;
        this.thirdRoots = thirdRoots;
        this.orderType = orderType;
        this.page = page;
    }
}
