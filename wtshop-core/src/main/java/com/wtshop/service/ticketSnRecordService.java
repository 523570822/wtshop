package com.wtshop.service;

import com.jfinal.aop.Enhancer;
import com.wtshop.dao.TicketSnRecordDao;
import com.wtshop.model.Ticketsnrecord;

/**
 * Created by Administrator on 2017/8/10.
 */
public class ticketSnRecordService extends BaseService<Ticketsnrecord>  {
    private TicketSnRecordDao ticketSnRecordDao= Enhancer.enhance(TicketSnRecordDao.class);
    public ticketSnRecordService() {
        super(Ticketsnrecord.class);
    }


}
