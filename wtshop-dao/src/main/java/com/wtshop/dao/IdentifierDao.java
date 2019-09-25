package com.wtshop.dao;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.wtshop.Pageable;
import com.wtshop.model.Identifier;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Dao - 品牌
 * 
 * 
 */
public class IdentifierDao extends   BaseDao<Identifier> {

	/**
	 * 构造方法
	 */
	public IdentifierDao() {
		super(Identifier.class);
	}

	/**
	 * 查询品牌列表
	 */
	public List<Record> findBrandSort(){

		String sql = " select id , name from brand order by name";
		return Db.find(sql);

	}

    public List<Identifier> findByIdfCode(String idfCode) {
		if (StringUtils.isEmpty(idfCode)) {
			return null;
		}
		try {
			String sql = "SELECT * FROM identifier i where   i.code= UPPER(?)";
			return modelManager.find(sql, idfCode);
		} catch (Exception e) {
			return null;
		}
    }

	public List<Identifier> findByOnCodeShare(String onCodeShare,Long memberId,String status) {
		if (StringUtils.isEmpty(onCodeShare)) {
			return null;
		}
		try {
			String sql = "SELECT *, m.store FROM identifier i LEFT JOIN member m ON i.share_code = m.share_code where   i.share_code= UPPER(?) and i.member_id= '"+memberId+"'";
			if (StringUtils.isNotEmpty(status)) {
				sql=sql+" and i.status= "+status;
			}
			return modelManager.find(sql, onCodeShare);
		} catch (Exception e) {
			return null;
		}
	}


	public List<Identifier> findByOnCodeShareSB(String onCodeShare,Long memberId) {
		if (StringUtils.isEmpty(onCodeShare)) {
			return null;
		}
		try {
			String sql = "SELECT i.*, m.store FROM identifier i LEFT JOIN member m ON i.share_code = m.share_code where  i.status<>'1' and  i.share_code= UPPER(?) and i.member_id= '"+memberId+"'";
			return modelManager.find(sql, onCodeShare);

		} catch (Exception e) {
			return null;
		}
	}
	public List<Identifier> findByMemberId(Long memberId) {
		try {
			String sql = "SELECT * FROM ( SELECT i.*, m.store FROM identifier i LEFT JOIN member m ON i.share_code = m.share_code WHERE i.member_id = ? ORDER BY i.`status`  ) j GROUP BY j.share_code";
			return modelManager.find(sql, memberId);
		} catch (Exception e) {
			return null;
		}
	}
	public int update(String sql) {
		try {

			return Db.update(sql);
		} catch (Exception e) {
			return 0;
		}
	}

	public List<Identifier> findByDay(String s, String i) {
		try {
			String sql = "SELECT i.* FROM identifier i where i=1    ";
			if(StringUtils.isNotEmpty(s)){
				sql=sql+" and i.status="+s ;
			}
			if(StringUtils.isNotEmpty(i)){
				sql=sql+" and DATEDIFF(i.end_date,NOW())="+i ;
			}


			return modelManager.find(sql);
		} catch (Exception e) {
			return null;
		}
	}
	public Page<Identifier> findPages(String select,String sql, Pageable pageable){
		return super.findPages(select,sql,pageable);
	}
}