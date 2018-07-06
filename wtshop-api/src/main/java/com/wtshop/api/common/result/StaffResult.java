package com.wtshop.api.common.result;

import com.wtshop.model.CommissionHistory;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sq on 2017/9/14.
 */
public class StaffResult implements Serializable{

    private CommissionHistory staff ;

    private List<CommissionHistory> staffList;

    public StaffResult(CommissionHistory staff, List<CommissionHistory> staffList) {
        this.staff = staff;
        this.staffList = staffList;
    }

    public CommissionHistory getStaff() {
        return staff;
    }

    public void setStaff(CommissionHistory staff) {
        this.staff = staff;
    }

    public List<CommissionHistory> getStaffList() {
        return staffList;
    }

    public void setStaffList(List<CommissionHistory> staffList) {
        this.staffList = staffList;
    }
}
