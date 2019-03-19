package com.wtshop.service;

import com.jfinal.aop.Before;
import com.jfinal.aop.Enhancer;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.wtshop.Pageable;
import com.wtshop.dao.CommissionDao;
import com.wtshop.dao.CommissionLogDao;
import com.wtshop.dao.DepositLogDao;
import com.wtshop.dao.MemberDao;
import com.wtshop.model.*;
import com.wtshop.util.ApiResult;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Service - 预存款记录
 * 
 * 
 */
public class CommissionLogService extends BaseService<CommissionLog> {

	/**
	 * 构造方法
	 */
	public CommissionLogService() {
		super(CommissionLog.class);
	}
	
	private CommissionLogDao depositLogDao = Enhancer.enhance(CommissionLogDao.class);
	private MemberDao memberDao = Enhancer.enhance(MemberDao.class);
	private MemberService memberService = Enhancer.enhance(MemberService.class);
	private ExchangeService exchangeService = Enhancer.enhance(ExchangeService.class);
	private MrmfShopService mrmfShopService = Enhancer.enhance(MrmfShopService.class);
	/**
	 * 用户充值总金额
	 */
	public Record findRechange(Long memberId){
		return depositLogDao.findRechange(memberId);
	}


	/**
	 * 提现成功，更新信息
	 */
	@Before(Tx.class)
	public  void exchengeUpdate(String price, String partner_trade_no){
		Member member = memberService.getCurrent();

		if(partner_trade_no.startsWith("B")){
			BigDecimal balance = member.getBalance();
			//获取充值金额 跟新余额
			BigDecimal money = new BigDecimal(price);
			BigDecimal bigDecimal = balance.subtract(money).setScale(2, BigDecimal.ROUND_HALF_DOWN);
			member.setBalance(bigDecimal);
			memberService.update(member);

			CommissionLog depositLog = new CommissionLog();
			depositLog.setMemberId(member.getId());
			depositLog.setBalance(member.getBalance());
			depositLog.setType(DepositLog.Type.withdraw.ordinal());
			depositLog.setCredit(new BigDecimal(0));
			depositLog.setDebit(new BigDecimal(price));
			depositLog.setStatus(0);
			depositLog.setMemo("余额提现支出");
			//提现进度
			ExchangeProgress exchangeProgress = new ExchangeProgress();
			exchangeProgress.setDepositId(depositLog.getId());
			exchangeProgress.setStatus(1);
			exchangeService.save(exchangeProgress);

			this.save(depositLog);
		}else {
			MrmfShop mrmfShop = mrmfShopService.findMrmfShop(member);

			BigDecimal bigDecimal = mrmfShop.getCommission().subtract(new BigDecimal(price)).setScale(2, BigDecimal.ROUND_HALF_DOWN);
			mrmfShop.setCommission(bigDecimal);
			mrmfShopService.update(mrmfShop);

		}







	}


	/**
	 * 存款支付成功 更新信息
	 */
	public ApiResult paySuccess(Map<Object, Object> map){
		ApiResult returnStatus = ApiResult.fail();
		Member member = memberService.getCurrent();
		BigDecimal balance = member.getBalance();
		//获取充值金额 跟新余额
		BigDecimal money = new BigDecimal(map.get("cash_fee").toString());
		BigDecimal bigDecimal = balance.add(money).setScale(2, BigDecimal.ROUND_HALF_DOWN);
		member.setBalance(bigDecimal);
		memberDao.update(member);

		//交易记录
		CommissionLog depositLog = new CommissionLog();
		depositLog.setMemberId(member.getId());
		depositLog.setType(DepositLog.Type.recharge.ordinal());
		depositLog.setCredit(money);
		depositLog.setDebit(new BigDecimal(0));
		depositLog.setMemo("充值收入");
		depositLog.setBalance(member.getBalance());
		depositLogDao.save(depositLog);
//		returnStatus.setSuccess(true);
		returnStatus = ApiResult.success();
		return returnStatus;
	}
	
	/**
	 * 查找预存款记录分页
	 * 
	 * @param member
	 *            会员
	 * @param pageable
	 *            分页信息
	 * @return 预存款记录分页
	 */
	public Page<CommissionLog> findPage(Member member, Pageable pageable ,Integer type) {
		Page<CommissionLog> page = depositLogDao.findPage(member, pageable, type);
		for(CommissionLog depositLog : page.getList()){
			if(depositLog.getOperator() != null){
				depositLog.setMemo(depositLog.getOperator());
			}

		}
		return page;
	}

	public CommissionLog findDeposit(Long id){
		return depositLogDao.findDeposit(id);
	}

	/**
	 * 分页
	 */
	public Page<CommissionLog> findPageBySearch(Pageable pageable){

		String nickname = null;
		String userPhone = null;
		String searchProperty = pageable.getSearchProperty();

		if(searchProperty != null){
			switch (searchProperty){
				case "nickname":
					nickname = pageable.getSearchValue();
					break;
				case "userPhone":
					userPhone = pageable.getSearchValue();
					break;
				default:
			}

		}
		return depositLogDao.findPageBySearch(pageable ,userPhone ,nickname);

	}

}