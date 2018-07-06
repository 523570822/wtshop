package com.wtshop.service;

import com.jfinal.aop.Before;
import com.jfinal.aop.Enhancer;
import com.jfinal.ext.plugin.monogodb.MongoKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.wtshop.Pageable;
import com.wtshop.Principal;
import com.wtshop.RequestContextHolder;
import com.wtshop.Setting;
import com.wtshop.dao.*;
import com.wtshop.exception.AppRuntimeException;
import com.wtshop.model.*;
import com.wtshop.util.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

/**
 * Service - 会员
 * 
 * 
 */
public class PartnerService extends BaseService<Partner> {

	/**
	 * 构造方法
	 */
	public PartnerService() {
		super(Partner.class);
	}
	
	private PartnerDao partnerDao = Enhancer.enhance(PartnerDao.class);




    @Before(Tx.class)
	public ApiResult updateMemberBalance(Order order, Refunds refunds ,Partner member){


		return  null;


	}






	/**
	 * 根据当前用户信息 获取所在店铺的id
	 */
	public String findOrganId(Partner member){
		//String phone = member.getPhone();
		BasicDBObject basicDBObject = new BasicDBObject();
		//basicDBObject.put("shopPhone",phone);
		DBObject organ = MongoKit.getCollection("organ").findOne(basicDBObject);
		if(organ != null){
			String id = organ.get("_id").toString();
			return id;
		}
		return null;
	}







	public Partner register(String username, String password, String nickname, String remoteAddr){
		Setting setting = SystemUtils.getSetting();
		//判断用户名是否是mongo数据库中数据
		//查询mongo数据库
		BasicDBObject basicDBObject = new BasicDBObject();
		basicDBObject.put("phone",username);
		DBObject user = MongoKit.getCollection("user").findOne(basicDBObject);
		DBObject staff = MongoKit.getCollection("staff").findOne(basicDBObject);
		DBObject organ = MongoKit.getCollection("organ").findOne(basicDBObject);


		Boolean isVip = false;
		DBObject vip = MongoKit.getCollection("vipMember").findOne(basicDBObject);
		if(! ObjectUtils.isEmpty(vip)){
			isVip = true;
		}

		Partner member = new Partner();

		String phone = StringUtils.lowerCase(username);
		String mixUserName = nickname;
		if (StringUtils.isBlank(nickname)){
			if (phone.length() >= 11){
				mixUserName = StringUtils.substring(phone, 0, 3) + "****" + StringUtils.substring(phone, 7);
			}else {
				mixUserName = "-";
			}
		}



		partnerDao.save(member);

		//判断mogo是否存在 若存在 更新数据 若不存在 插入数据
		if(user != null){
			user.put("shopPassword", DigestUtils.md5Hex(password));
			MongoKit.getCollection("user").save(user);

		}else if(staff != null){
			staff.put("shopPassword",DigestUtils.md5Hex(password));
			MongoKit.getCollection("staff").save(staff);

		}else if(organ != null){
			organ.put("shopPassword",DigestUtils.md5Hex(password));
			MongoKit.getCollection("organ").save(organ);
		}else{
			Record record = new Record().set("phone" ,username).set("shopPassword" ,password);
			MongoKit.save("user",record);
		}
		return member;
	}

	/**
	 * 用户注册
	 * @param username
	 * @param password
	 * @return
	 */

	public Partner register(String username,String password ,String remoteAddr){
		return register(username, password, "", remoteAddr);
	}


	/**
	 * 会员分页
	 */

	public Page<Partner> findPages(Pageable pageable, Integer type){

		return partnerDao.findPages(pageable, type);

	}


	public Partner findFirst(Long id) {
		return partnerDao.findPartnerByMId(id);

	}
}