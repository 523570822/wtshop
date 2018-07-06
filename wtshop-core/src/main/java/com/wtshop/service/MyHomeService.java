package com.wtshop.service;

import com.jfinal.aop.Enhancer;
import com.jfinal.ext.plugin.monogodb.MongoKit;
import com.jfinal.plugin.activerecord.Record;
import com.mongodb.*;
import com.wtshop.dao.*;
import com.wtshop.model.*;
import com.wtshop.util.*;
import org.bson.types.ObjectId;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by 蔺哲 on 2017/8/23.
 */
public class MyHomeService {
    private StaffMemberDao staffMemberDao= Enhancer.enhance(StaffMemberDao.class);
    private MemberService memberService = Enhancer.enhance(MemberService.class);
    private ReferrerGoodsDao referrerGoodsDao= Enhancer.enhance(ReferrerGoodsDao.class);
    private ReferrerConfigDao referrerConfigDao= Enhancer.enhance(ReferrerConfigDao.class);
    private StaffOrganDao staffOrganDao = Enhancer.enhance(StaffOrganDao.class);
    private MrmfShopDao mrmfShopDao = Enhancer.enhance(MrmfShopDao.class);
    /**
     * 查询跟我有关的用户
     * @param userName   模糊用户名字
     * @return
     */
    public ApiResult queryMyUsers(Long staffId,String userName,String type,String goodsId){
        List<Record> list = staffMemberDao.queryList(staffId,userName);
        if(list.size()==0){
            return new ApiResult(1,"暂无数据");
        }
        Collections.sort(list, new Comparator<Record>() {
            @Override
            public int compare(Record o1, Record o2) {
                String name1 = StringUtils.isEmpty(o1.get("alias_name"))?o1.get("name").toString():o1.get("alias_name").toString();
                String name2 = StringUtils.isEmpty(o2.get("alias_name"))?o2.get("name").toString():o2.get("alias_name").toString();
                String zm1 = PinYinUtil.getPinYinHeadChar(name1.substring(0,1)).toUpperCase();
                String zm2 = PinYinUtil.getPinYinHeadChar(name2.substring(0,1)).toUpperCase();
                return zm1.compareTo(zm2);
            }
        });
        if("1".equals(type)){//如果需要检查间隔
           queryTimes(list,staffId,Long.valueOf(goodsId));
        }
        Map map = groupByUserName(list);
        return new ApiResult(1,"",map);
    }
    /**
     * 检查配置间隔
     */
    public List queryTimes(List<Record> list,Long staffId,Long goodsId){
        ReferrerConfig cfg = referrerConfigDao.getConfig();
        if(!ObjectUtils.isEmpty(cfg)){//配置不为空时，计算，否则全部设null
            for(int i=0;i<list.size();i++){
                Map thisData = list.get(i).getColumns();
                if(cfg.getReferrerTime()==null){
                    thisData.put("intervalTime",null);
                    continue;
                }
                //取每条数据找最近推荐条数
                ReferrerGoods ref = referrerGoodsDao.findOne(staffId,Long.valueOf(thisData.get("member_id").toString()),goodsId);
                if(ObjectUtils.isEmpty(ref)){
                    thisData.put("intervalTime",null);
                    continue;
                }
                //然后把创建时间和设置的时间对比
                Long oldTime = ref.getCreateDate().getTime()/1000;
                Long nowTime = DateUtils.getNowDateTime();
                Long tims = cfg.getReferrerTime();
                if(oldTime+tims>nowTime){
                    thisData.put("intervalTime",oldTime+tims-nowTime);
                }else {
                    thisData.put("intervalTime",null);
                }
            }
        }else {
            for(int i=0;i<list.size();i++){
                list.get(i).getColumns().put("intervalTime",null);
            }
        }

        return list;
    }
    /**
     * 通讯录首字母分组
     */
    public Map<String,List> groupByUserName(List<Record> list) {
        String name1="";
        String oldPY = "";
        String thisPY = "";
        List nameList =new ArrayList();
        Map<String,List> map = new LinkedHashMap();
        for(int i=0;i<list.size();i++){
            name1 = StringUtils.isEmpty(list.get(i).get("alias_name"))?list.get(i).get("name").toString():list.get(i).get("alias_name").toString();
            thisPY = PinYinUtil.getPinYinHeadChar(name1.substring(0,1)).toUpperCase();
            if(StringUtils.isEmpty(oldPY)){
                oldPY = thisPY;
            }
            if(!thisPY.equals(oldPY)){
                map.put(oldPY,nameList);
                nameList = new ArrayList();
            }
            oldPY = thisPY;
            nameList.add(list.get(i));
        }
        map.put(thisPY,nameList);
        List<String> k = new ArrayList<>();
        List list1 = new ArrayList<>();
        for (String key : map.keySet()) {
            if (key.charAt(0) < 'A' || key.charAt(0) > 'Z') {
                list1.addAll(map.get(key));
                k.add(key);
            }
        }
        for (String k1 : k) {
            map.remove(k1);
        }
        if (list1.size() > 0) {
            map.put("#", list1);
        }
        return map;
    }
    /**
     * 添加客户
     *@param  memberId 用户手机号
     *@param  staffId 技师id
     */
    public ApiResult addMember(Long memberId,Long staffId){
        Member member = memberService.find(memberId);
        if(member.getPhone() != null){
            DBObject staff = MongoKit.getCollection("staff").findOne(new BasicDBObject("phone", member.getPhone()));
            MrmfShop mrmfShop = mrmfShopDao.findByMemberId(memberId);
            if(!ObjectUtils.isEmpty(mrmfShop)){
                return new ApiResult(0,"您已经是店铺管理员了!");
            }
            if(!ObjectUtils.isEmpty(staff)){
                return new ApiResult(0,"您已经是技师了!");
            }
        }
        Member staffs = memberService.find(staffId);
        DBObject staff = MongoKit.getCollection("staff").findOne(new BasicDBObject("phone", staffs.getPhone()));
        if(ObjectUtils.isEmpty(staff)){
            return new ApiResult(0,"该技师不存在");
        }
        StaffMember staffMember = staffMemberDao.queryByMemberId(memberId);
        Member organ = new Member();
        if(ObjectUtils.isEmpty(staffMember)){
            String organId = "";
            if(staff.get("organId") != null){
                organId = staff.get("organId").toString();
            }
            staffMember = new StaffMember(staffId,memberId,organId);
            staffMemberDao.save(staffMember);
            return new ApiResult(1,"添加成功",staffMember);
        }else {
            return new ApiResult(0,"已与技师绑定关系");
        }
    }
    /**
     *查看技师的单一用户信息
     */
    public ApiResult queryMember(Long userId){
        Member member = memberService.find(userId);
        if(ObjectUtils.isEmpty(member)){
            return new ApiResult(0,"不存在该用户");
        }
        return new ApiResult(1,"",member);
    }
    /**
     * 修改客户备注
     * @param aliasName   备注
     * @param memberId     客户id
     * @return 结果对象
     */
    public ApiResult aliasName(String aliasName,Long memberId){
       StaffMember staffMember = staffMemberDao.queryByMemberId(memberId);
       if(ObjectUtils.isEmpty(staffMember)){
           return new ApiResult(0,"当前顾客不存在");
       }
        staffMember.setAliasName(aliasName);
       staffMemberDao.update(staffMember);
        return new ApiResult(1,"修改成功",staffMember);
    }
    /**
     * 我的店铺
     * @param phone 技师手机号
     * @return 结果对象
     */
    public ApiResult queryMyOrgan(String phone){
        DBObject staff = MongoKit.getCollection("staff").findOne(new BasicDBObject("phone", phone));
        if(ObjectUtils.isEmpty(staff)){
            return new ApiResult(0,"技师信息不存在");
        }
        Object organId = staff.get("organId");
       if(ObjectUtils.isEmpty(organId)){
           return new ApiResult(0,"暂无店铺");
       }
        DBObject organ = MongoKit.getCollection("organ").findOne(new BasicDBObject("_id", organId.toString()));
        if(ObjectUtils.isEmpty(organ)){
            return new ApiResult(0,"未存在店铺");
        }
        return new ApiResult(1,"",organ);
    }

    /**
     * 技师扫码入职店铺
     * @param member
     * @param organId 入职店铺id
     * @return
     */
    public ApiResult addOrgan(Member member, String organId){
        DBCollection staffDB = MongoKit.getCollection("staff");
        if(StringUtils.isEmpty(member.getPhone())){
            return new ApiResult(0,"请先绑定手机号","");
        }
        DBObject staff = staffDB.findOne(new BasicDBObject("phone", member.getPhone()));
        if(ObjectUtils.isEmpty(staff)){//新建技师
            staff = new BasicDBObject();
            staff.put("phone",member.getPhone());
            staff.put("createTime",new Date());
            staff.put("name",member.getName());
            staff.put("parentId",organId);//所属总公司id
            staff.put("dutyId",0);//部门id
            staff.put("sex",member.getGender()==null?"女":member.getGender()==0?"男":"女");
            staff.put("idcard",null);//身份证号
//            staff.put("accessDay",DateUtils.format(new Date()));
            staff.put("deleteFlag",false);
            staff.put("card",false);
            staff.put("flag",0);
            staff.put("money",0.0D);
            staff.put("moneyAll",0.0D);
            staff.put("zuohuoNum",0);
            staff.put("level",1);//级别
            staff.put("status",2);//待审核
        }else if(!ObjectUtils.isEmpty(staff.get("organId"))){
            return new ApiResult(0,"技师已存在店面",staff);
        }
        staff.put("accessDay",DateUtils.format(new Date()));
        staff.put("organId",organId);
        staffDB.save(staff);
        StaffOrgan staffOrgan = new StaffOrgan(1,member.getId(),organId);
        staffOrganDao.save(staffOrgan);
        return new ApiResult(1,"技师和店铺关系绑定成功，详细信息店铺管理员可通过后台查看",staff);
    }
    /**
     * 删除技师与门店关系
     *@param member 技师用户
     *@param organId 店面id
     *@return 结果对象
     */
    public ApiResult removeOrgan(Member member,String organId){
        DBObject staff = MongoKit.getCollection("staff").findOne(new BasicDBObject("phone", member.getPhone()));
        if(ObjectUtils.isEmpty(staff)){
            return new ApiResult(0,"技师不存在");
        }
        staff.put("organId",null);
        MongoKit.getCollection("staff").save(staff);
   //     staffMemberDao.updateByStaffId(member.getId());

        StaffOrgan staffOrgan = staffOrganDao.findStaffOrgan(organId,member.getId());
        if(!ObjectUtils.isEmpty(staffOrgan)){
            staffOrgan.setIsLeave(0);
            staffOrgan.setLeaveDate(new Date());
            staffOrganDao.update(staffOrgan);
        }
        return new ApiResult(1,"解除成功",staff);
    }
    /**
     * @param strName 分组字段，首字母大写
     * 推荐商品排序
     */
    public Map sortByTime(List list, String strName){
        Map map = new LinkedHashMap();
        List timeList = new ArrayList();
        Date oldTime = null;
        Date thisTime = new Date();
        String time = "";
       try {
           for(int i=0;i<list.size();i++){
               Object obj1 = list.get(i);
               Method m1 = obj1.getClass().getMethod("get"+strName);
               thisTime = (Date) m1.invoke(obj1);
               if(oldTime==null){
                   oldTime = thisTime;
               }
               if(!DateUtils.isSameDate(oldTime,thisTime)){
                   map.put(time,timeList);
                   timeList = new ArrayList();
               }
               oldTime = thisTime;
               time = DateUtils.formatDate(thisTime);
               timeList.add(list.get(i));
           }
       }catch (Exception e){
           e.printStackTrace();
       }
        map.put(time,timeList);
       return map;
    }
}
