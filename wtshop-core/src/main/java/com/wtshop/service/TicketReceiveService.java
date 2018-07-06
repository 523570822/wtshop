package com.wtshop.service;

import com.jfinal.plugin.activerecord.Page;
import com.wtshop.Pageable;
import com.wtshop.dao.TicketReceiveDao;
import com.wtshop.model.Member;
import com.wtshop.model.Ticketreceive;

import java.util.List;

import static com.jfinal.aop.Enhancer.enhance;

/**
 * Created by sq on 2017/8/14.
 */
public class TicketReceiveService extends BaseService<Ticketreceive>{

    public TicketReceiveService(){super(Ticketreceive.class);}

    private TicketReceiveDao ticketReceiveDao = enhance(TicketReceiveDao.class);

    /**
     * 查找优惠码分页
     *
     * @param member
     *            会员
     * @param pageable
     *            分页信息
     * @return 优惠码分页
     */
    public Page<Ticketreceive> findSharePage(Member member, Pageable pageable, Boolean isUsed, List<Long> productCategory  , Boolean hasExpired, Boolean isEnabled) {
        return ticketReceiveDao.findSharePage(member, pageable, isUsed , productCategory ,hasExpired, isEnabled);
    }

    /**
     * 查找优惠券分页
     *

     *            是否已过期
     * @param pageable
     *            分页信息
     * @return 优惠券分页
     */
    public Page<Ticketreceive> findSharePage(Boolean hasExpired, Pageable pageable ,List<Long> productCategory ) {
        return ticketReceiveDao.findSharePage( hasExpired, pageable ,productCategory);
    }



}
