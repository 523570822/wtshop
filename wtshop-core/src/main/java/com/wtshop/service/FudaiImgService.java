package com.wtshop.service;

import com.jfinal.aop.Enhancer;
import com.wtshop.dao.FudaiImgDao;
import com.wtshop.model.FudaiImg;

import java.util.List;

/**
 * Created by Administrator on 2017/8/23.
 */
public class FudaiImgService extends  BaseService<FudaiImg> {
    private FudaiImgDao imgDao= Enhancer.enhance(FudaiImgDao.class);
    public  FudaiImgService(){
        super(FudaiImg.class);
    }

    public List<FudaiImg>  getImgList(long id){
        String sql="SELECT * from fudai_img WHERE fudaiId="+id+  "  ORDER BY orders";
        return  imgDao.findListBySql(sql);
    }
}
