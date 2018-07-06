package com.wtshop.service;

import com.jfinal.aop.Enhancer;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.SqlPara;
import com.wtshop.Pageable;
import com.wtshop.dao.CouponDao;
import com.wtshop.dao.CouponShareDao;
import com.wtshop.dao.MemberDao;
import com.wtshop.model.Coupon;
import com.wtshop.model.CouponShare;
import com.wtshop.model.Member;
import org.apache.commons.collections.map.HashedMap;

import java.util.Map;

/**
 * Service - 广告
 * 
 * 
 */
public class CouponShareService extends BaseService<CouponShare> {
	private CouponShareDao csdao=Enhancer.enhance(CouponShareDao.class);
	private CouponDao couponDao=Enhancer.enhance(CouponDao.class);
	private MemberDao memberDao=Enhancer.enhance(MemberDao.class);
	private MemberService memberService = Enhancer.enhance(MemberService.class);
	private CouponCodeService css = Enhancer.enhance(CouponCodeService.class);
	private  static  Object lock=new  Object();

	/**
	 * 构造方法
	 */
	public CouponShareService() {
		super(CouponShare.class);
	}

	public Page getlist(Pageable pageable){
		//Kv cond = Kv.by("beginDate", beginTime).set("endDate", endTime);
		SqlPara sqlPara = Db.getSqlPara("couponShareList");
		Page page = Db.paginate(pageable.getPageNumber(), pageable.getPageSize(), sqlPara);
		return page;
	}


	//获取优惠券信息

	public Map<String,Object> info(int cShareId){
		Map<String,	Object> map=new HashedMap();
		CouponShare cs=csdao.find(new Long(cShareId));
		Coupon cp=couponDao.find(cs.getCouponId());
		map.put("couponShare",cs);
		map.put("coupon",cp);
		return  map;

	}


	//领取优惠券

	public  Map<String,Object>  receive(String phone,long cShareId,String ip){
			Map map=new HashedMap();
			map.put("code",0); //0 成功 1失败
			map.put("msg","领取成功"); //0 成功 1失败

		CouponShare cs=csdao.find(new Long(cShareId));
		if (cs==null){
			map.put("code",1);
			map.put("msg","该活动不存在");
			return  map;
		}

        if (cs.getCurrentNum()>=cs.getNumLimit()){
            map.put("code",1);
            map.put("msg","该优惠券已被领完");
            return  map;
        }

        Member member=memberDao.findByUsername(phone);
			if (member==null){
				member= memberService.register(phone, System.currentTimeMillis()+"" , ip);
			}

		Coupon cp=couponDao.find(cs.getCouponId());
			if (isreceived(member.getId(),cp.getId())){
			map.put("code",1);
			map.put("msg","该会员已领取该优惠券");
			return  map;
		     }


		     synchronized (lock){
				 CouponShare cs2=csdao.find(cShareId);
				 if (cs2.getCurrentNum()>=cs2.getNumLimit()){
					 map.put("code",1);
					 map.put("msg","该优惠券已被领完");
					 return  map;
				 }else {
						css.generate(cp,member,cShareId,0);
					 	cs2.setCurrentNum(cs2.getCurrentNum()+1);
					 	cs2.update();
				 }
			 }

			return map;





	}


//查询是否已领取优惠券

	public  boolean  isreceived(long mid,long couponid){
		return  csdao.isreceived(mid,couponid) ;
	}


}