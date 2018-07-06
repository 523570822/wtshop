package com.wtshop.service;

import com.jfinal.aop.Enhancer;
import com.wtshop.dao.AdDao;
import com.wtshop.dao.TargetPathDao;
import com.wtshop.model.Ad;

import java.util.ArrayList;
import java.util.List;

/**
 * Service - 广告
 * 
 * 
 */
public class AdService extends BaseService<Ad> {

	private AdDao adDao = Enhancer.enhance(AdDao.class);
	private TargetPathDao targetPathDao=Enhancer.enhance(TargetPathDao.class);

	/**
	 * 构造方法
	 */
	public AdService() {
		super(Ad.class);
	}



	public Ad save(Ad ad) {
		return super.save(ad);
	}

	public Ad update(Ad ad) {
		return super.update(ad);
	}

	public void delete(Long id) {
		super.delete(id);
	}

	public void delete(Long... ids) {
		super.delete(ids);
	}

	public void delete(Ad ad) {
		super.delete(ad);
	}

	/**
	 * 获取首页顶部广告位
	 */
	public List<Ad> findAdList(Long id){
		return adDao.findAdList(id);
	}

    public List<Ad> findAdByTarId(Long[] targetId) {
		List<Ad> result = new ArrayList<Ad>();
		if (targetId != null) {
			for (Long id : targetId) {
				List<Ad> re=adDao.findByTarId(id);
				if(re.size()>0){
					for(Ad r:re){
						result.add(r);
					}
				}

			}
		}
		return result;
	}

}