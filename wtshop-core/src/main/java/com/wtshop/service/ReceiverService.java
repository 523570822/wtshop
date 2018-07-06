package com.wtshop.service;

import com.wtshop.dao.AreaDao;
import com.wtshop.dao.AreaDescribeDao;
import com.wtshop.model.AreaDescribe;
import org.apache.commons.lang3.BooleanUtils;

import com.jfinal.aop.Enhancer;
import com.jfinal.plugin.activerecord.Page;
import com.wtshop.Pageable;
import com.wtshop.dao.ReceiverDao;
import com.wtshop.model.Member;
import com.wtshop.model.Receiver;
import com.wtshop.util.Assert;

import java.util.List;

/**
 * Service - 收货地址
 * 
 * 
 */
public class ReceiverService extends BaseService<Receiver> {

	/**
	 * 构造方法
	 */
	public ReceiverService() {
		super(Receiver.class);
	}

	private AreaDescribeDao areaDescribeDao = Enhancer.enhance(AreaDescribeDao.class);
	private AreaDao areaDao = Enhancer.enhance(AreaDao.class);
	private ReceiverDao receiverDao = Enhancer.enhance(ReceiverDao.class);
	
	/**
	 * 查找默认收货地址
	 * 
	 * @param member
	 *            会员
	 * @return 默认收货地址，若不存在则返回最新收货地址
	 */
	public Receiver findDefault(Member member) {
		return receiverDao.findDefault(member);
	}

	/**
	 * 查找收货地址分页
	 * 
	 * @param member
	 *            会员
	 * @param pageable
	 *            分页信息
	 * @return 收货地址分页
	 */
	public Page<Receiver> findPage(Member member, Pageable pageable) {
		Page<Receiver> page = receiverDao.findPage(member, pageable);
		//商品配送
		String receiveTime = null;
		List<Receiver> list = page.getList();
		if(list != null && list.size() > 0){
			for(Receiver receiver : list){
				AreaDescribe areaDescribe = areaDescribeDao.findByAreaId(receiver.getAreaId());
				//判断本级地区是否填写
				if(areaDescribe != null && areaDescribe.getReceivingBegintime() != null){
					receiveTime = areaDescribe.getReceivingBegintime();
				}else {
					AreaDescribe areaDescribes = areaDescribeDao.findByAreaId(areaDao.find(receiver.getAreaId()).getParentId());
					if(areaDescribes !=null){
						receiveTime = areaDescribes.getReceivingBegintime();
					}
				}
				receiver.setZipCode(receiveTime);
			}
		}
		return page;
	}
	
	public Receiver save(Receiver receiver) {
		Assert.notNull(receiver);

		if (BooleanUtils.isTrue(receiver.getIsDefault())) {
			receiverDao.setDefault(receiver);
		}
		return super.save(receiver);
	}

	public Receiver update(Receiver receiver) {
		Assert.notNull(receiver);

		if (BooleanUtils.isTrue(receiver.getIsDefault())) {
			receiverDao.setDefault(receiver);
		}
		return super.update(receiver);
	}

}