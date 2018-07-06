package com.wtshop.dao;

import com.wtshop.model.Ticketsn;

/**
 * Created by Administrator on 2017/8/10.
 */
public class TicketSnDao  extends BaseDao<Ticketsn> {
    public TicketSnDao() {
        super(Ticketsn.class);
    }

    public  Ticketsn getTicketsnBySn(String sn){
            String sql="select * from ticketsn where sn="+sn;
            Ticketsn tsn=findBySql(sql);
            return  tsn;
    };
}
