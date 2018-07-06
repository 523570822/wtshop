package com.wtshop.dao;

import com.wtshop.model.Ticketsnrecord;

import java.util.List;

/**
 * Created by Administrator on 2017/8/10.
 */
public class TicketSnRecordDao extends BaseDao<Ticketsnrecord> {
    public TicketSnRecordDao() {
        super(Ticketsnrecord.class);
    }

    //根据snid获取记录
   public   List<Ticketsnrecord>  getListBySnId(long snid){
        String sql= "SELECT * from ticketsnrecord t WHERE t.ticketSnId="+snid;
        return  (List<Ticketsnrecord>)findListBySql(sql);
    }

    //
}
