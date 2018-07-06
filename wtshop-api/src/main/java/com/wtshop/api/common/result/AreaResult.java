package com.wtshop.api.common.result;

import java.util.List;
import java.util.Map;

/**
 * Created by sq on 2017/9/14.
 */
public class AreaResult {

    private Long id;

    private String name;

    private Map<String,List> sonSort;

    private Long parentId;

    public AreaResult(Long id, String name, Map<String, List> sonSort, Long parentId) {
        this.id = id;
        this.name = name;
        this.sonSort = sonSort;
        this.parentId = parentId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, List> getSonSort() {
        return sonSort;
    }

    public void setSonSort(Map<String, List> sonSort) {
        this.sonSort = sonSort;
    }
}
