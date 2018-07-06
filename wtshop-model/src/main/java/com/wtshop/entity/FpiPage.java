package com.wtshop.entity;

import org.apache.poi.ss.formula.functions.T;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 蔺哲 on 2017/7/26.
 */
public class FpiPage implements Serializable {
    private List<T> data;
    private int page = 1;
    private int size = 20;

    private boolean needTotal = true;
    private int total = 0;

    private String sortField;
    private String sortOrder;

    // 自定义查询参数Map，只用于与前端grid组件参数续传使用，数据库查询不使用该参数
    private Map params;

    /**
     * 翻页信息类初始化
     */
    public FpiPage() {
    }

    /**
     * 翻页信息类初始化
     *
     * @param page
     *            当前页
     */
    public FpiPage(int page) {
        this.page = page;
    }

    /**
     * 翻页信息类初始化
     *
     * @param page
     *            当前页
     * @param size
     *            每页记录数
     */
    public FpiPage(int page, int size) {
        this.page = page;
        this.size = size;
    }

    public FpiPage(Map flipMap) {
        if (flipMap != null) {
            int p = Integer.parseInt((String) flipMap.get("page"));
            int s = Integer.parseInt((String) flipMap.get("size"));
            String sf = (String) flipMap.get("sortField");
            String so = (String) flipMap.get("sortOrder");
            this.page = p;
            this.size = s;
            this.sortField = sf;
            this.sortOrder = so;
        }
    }

    /**
     * 获取当前为第几页
     *
     * @return 当前第几页
     */
    public int getPage() {
        return page;
    }

    /**
     * 设置当前查询第几页
     *
     * @param page
     *            当前第几页参数
     */
    public void setPage(int page) {
        this.page = page;
    }

    /**
     * 获取翻页查询每页输出记录数
     *
     * @return 翻页查询每页输出记录数
     */
    public int getSize() {
        return size;
    }

    /**
     * 设置翻页查询每页输出记录数
     *
     * @param size
     *            翻页查询每页输出记录数
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * 获取总记录数
     *
     * @return 总记录数
     */
    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    /**
     * 获取总页数
     *
     * @return 总页数
     */
    public Integer getPages() {
        BigDecimal sizeBD = new BigDecimal(size);
        BigDecimal totalBD = new BigDecimal(total);

        return new Integer(totalBD.divide(sizeBD, 0, BigDecimal.ROUND_CEILING).intValue());
    }

    /**
     * 获取翻页起始加载记录位置
     *
     * @return 翻页起始加载位置
     */
    public Integer getStartAt() {
        Integer p = getPage();
        p = p != null && p - 1 >= 0 ? p - 1 : 0;
        return p * getSize();
    }

    /**
     * 获取翻页查询结果数据
     *
     * @return 翻页查询结果数据
     */
    public List<T> getData() {
        if (data == null)
            data = new ArrayList<T>();
        return data;
    }

    /**
     * 当前页数据对象列表
     *
     * @param data
     *            当前页数据对象列表
     */
    public void setData(List<T> data) {
        if (this.data == null)
            this.data = new ArrayList<T>();
        this.data.clear();
        if (data != null)
            this.data.addAll(data);
    }

    /**
     * 获取当前查询结果数据记录数
     *
     * @return 当前查询结果数据记录数
     */
    public Integer getDataCount() {
        if (data != null) {
            return new Integer(data.size());
        } else {
            return new Integer(0);
        }
    }

    /**
     * 设置是否需要提取总记录数，在不需要取总记录数的情况下设置为false以优化性能
     *
     * @param needTotal
     *            是否要提取总记录数
     */
    public void setNeedTotal(boolean needTotal) {
        this.needTotal = needTotal;
    }

    /**
     * 是否需要提取总记录数
     *
     * @return 是否提取总记录数
     */
    public boolean isNeedTotal() {
        return needTotal;
    }

    /**
     * 获取排序字段
     *
     * @return 排序字段
     */
    public String getSortField() {
        return sortField;
    }

    /**
     * 设置排序字段
     *
     * @param sortField
     *            排序字段
     */
    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    /**
     * 获取排序顺序，asc or desc
     *
     * @return 排序顺序
     */
    public String getSortOrder() {
        return sortOrder;
    }

    /**
     * 设置排序顺序，asc or desc
     *
     * @param sortOrder
     *            排序顺序
     */
    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public int getFirstPageValue() {
        int firstPageValue = 0;
        int currentPageNum = getPage();
        int pageMaxSize = getSize();
        int totalSize = getTotal();

        // 当前页-1后，与每页显示记录数相乘；
        firstPageValue = (currentPageNum - 1) * pageMaxSize;
        // 如果超出最大页，则不返回任何结果
		/*
		 * if (firstPageValue > totalSize) { // 最后一页 // 设置当前页为最后一页
		 * currentPageNum = totalSize / pageMaxSize; setPage(currentPageNum);
		 * firstPageValue = totalSize - pageMaxSize; }
		 */
        // 如果小于零，赋值为0
        if (firstPageValue < 0) {
            firstPageValue = 0;
            setPage(1);
        }

        return firstPageValue;
    }

    /**
     * 获取自定义查询参数Map，只为前端grid组件参数续传用，数据库查询操作不使用该参数
     *
     * @return 自定义查询参数Map
     */
    public Map getParams() {
        if (params == null)
            params = new HashMap();
        return params;
    }

    /**
     * 设置自定义查询参数Map，只为前端grid组件参数续传用，数据库查询操作不使用该参数
     *
     * @param params
     *            自定义查询参数Map
     */
    public void setParams(Map params) {
        this.params = params;
    }
}
