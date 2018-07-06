package com.wtshop.model;

import com.jfinal.plugin.activerecord.Page;

import java.util.List;

/**
 * Created by sq on 2017/6/2.
 */
public class Pages<T> extends Page{


    private List<T> list;				// list result of this page
    private int pageNumber;				// page number
    private int pageSize;				// result amount of this page
    private int totalPage;				// total page
    private int totalRow;				// total row

    /**
     * Constructor.
     * @param list the list of paginate result
     * @param pageNumber the page number
     * @param pageSize the page size
     * @param totalPage the total page of paginate
     * @param totalRow the total row of paginate
     */
    public Pages(List<T> list, int pageNumber, int pageSize, int totalPage, int totalRow) {
        this.list = list;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalPage = totalPage;
        this.totalRow = totalRow;
    }

    public Pages() {

    }

    /**
     * Return list of this page.
     */
    @Override
    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    /**
     * Return page number.
     */
    public int getPageNumber() {
        return pageNumber;
    }

    /**
     * Return page size.
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * Return total page.
     */
    public int getTotalPage() {
        return totalPage;
    }

    /**
     * Return total row.
     */
    public int getTotalRow() {
        return totalRow;
    }

    public boolean isFirstPage() {
        return pageNumber == 1;
    }

    public boolean isLastPage() {
        return pageNumber >= totalPage;
    }

    public String toString() {
        StringBuilder msg = new StringBuilder();
        msg.append("pageNumber : ").append(pageNumber);
        msg.append("\npageSize : ").append(pageSize);
        msg.append("\ntotalPage : ").append(totalPage);
        msg.append("\ntotalRow : ").append(totalRow);
        return msg.toString();
    }
}

