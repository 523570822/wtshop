###会员注册开始==========
#sql("getVIPAdd")


select year, month,day , SUM(registerMemberCount)  registerMemberCount  from (SELECT year(create_date) year ,  month(create_date)-1  month  ,DAY(create_date) day ,
1 registerMemberCount
  FROM member  WHERE  1=1  #@getVIPAddSQL()
#end

#define getVIPAddSQL()
#if(beginDate != '')
 and DATE(create_date)>='#(beginDate)'
#end

#if(endDate != '')
 and DATE(create_date)<='#(endDate )'
#end

)t

#if(timeType ==0)
     GROUP BY year
  #elseif(timeType==1)
       GROUP BY year  ,month
  #elseif(timeType==2)
     GROUP BY year,month,DAY
#end

#end



###会员注册结束==========



#查询指定时间内新增会员---开始
#sql("getVIPAddAll")
SELECT username,phone ,email,birth, FORMAT(balance,2) balance ,FORMAT(amount,2) amount ,create_date FROM  member pt   WHERE 1=1
#@getVIPAddAllSQL()
#end



#define getVIPAddAllSQL()

#if(beginDate != null)
 and DATE(pt.create_date)>='#(beginDate)'
#end

#if(endDate != null)
 and DATE(pt.create_date)<='#(endDate )'
#end

#if(phone != null)
 and m.`phone` LIKE '%#(phone)%'
#end

#if(nickname != null)
 and m.`nickname` LIKE '%#(nickname)%'
#end

#end

#查询指定时间内新增会员---结束



#查询关注用户---开始
#sql("getGuanzhuVip")
SELECT year, month,day ,COUNT(DISTINCT favorite_members) num  from
(SELECT favorite_members, year(create_date) year ,  month(create_date)  month  ,DAY(create_date) day  from  member_favorite_goods  WHERE 1=1
#@getVIPAddSQL()
#end
#查询关注用户---结束




#查询消费用户---开始
#sql("getXiaoFeiVip")
SELECT year, month,day ,COUNT(DISTINCT member_id) num  from
(SELECT member_id, year(create_date) year ,  month(create_date)  month  ,DAY(create_date) day  from  `order`   WHERE 1=1 and (status=5   or   status=9)
#@getVIPAddSQL()
#end
#查询消费用户---结束





#查询关注商品用户详细列表---开始
#sql("getGzVipListExcel")

SELECT  m.nickname name,m.phone ,m.email,m.birth, FORMAT(m.balance,2) balance ,FORMAT(m.amount,2) amount ,m.create_date,
g.`name`,g.price,pt.create_date gzsj
  FROM member_favorite_goods pt LEFT JOIN member m on pt.favorite_members=m.id LEFT JOIN goods g ON g.id=pt.favorite_goods   WHERE 1=1
#@getVIPAddAllSQL()
#end
#查询关注商品用户---结束



#查询关注消费用户详细列表---开始
#sql("getXfVipListExcel")

SELECT  m.username name ,m.phone ,m.email,m.birth, FORMAT(m.balance,2) balance ,FORMAT(m.amount,2) amount ,m.create_date, pt.create_date orderTime,pt.amount orderAmount
  FROM `order` pt LEFT JOIN member m on pt.member_id=m.id   WHERE 1=1 and (status=5 or   status=9)
#@getVIPAddAllSQL()
#end
#查询关注商品用户---结束

#查询订单创建数量--开始
#sql("createOrderNum")
select year, month,day , SUM(num)  num  from (SELECT year(create_date) year ,  month(create_date)  month  ,DAY(create_date) day ,
1 num
  FROM `order`    WHERE 1=1   #@getVIPAddSQL()
  #end
#查询订单创建数量---结束

#查询订单完成数量--开始
#sql("completeOrderNum")
select year, month,day , SUM(num)  num  from (SELECT year(create_date) year ,  month(create_date)  month  ,DAY(create_date) day ,
1 num
  FROM `order`    WHERE 1=1  and (status=5 or   status=9)   #@getVIPAddSQL()
  #end
#查询订单完成数量---结束

#查询创建订单金额--开始
#sql("createOrderMoney")
select year, month,day , SUM(amount)  amount  from (SELECT year(create_date) year ,  month(create_date)  month  ,DAY(create_date) day ,
amount
  FROM `order`    WHERE 1=1   #@getVIPAddSQL()
    #end
#查询创建订单金额--结束


#查询完成订单金额--开始
#sql("completeOrderMoney")
select year, month,day , SUM(amount)  amount  from (SELECT year(create_date) year ,  month(create_date)  month  ,DAY(create_date) day ,
amount
  FROM `order`    WHERE 1=1   and (status=5 or   status=9)  #@getVIPAddSQL()
      #end
#查完成订单金额--结束




#查询财务订单列表--开始
#sql("getCaiWuListOrder")
SELECT pt.type,pt.id,pt.sn,pt.create_date,m.`nickname` name ,pt.`status`,pt.quantity,pt.price,pt.price,pt.miaobi_paid,pt.amount_paid,pt.fee,pt.freight,pt.amount,pt.weiXin_paid,pt.aLi_paid,pt.order_no
   from  `order`   pt  LEFT JOIN member m on m.id=pt.member_id WHERE pt.status in( 5, 9 ,3 ,4)  #@getVIPAddAllSQL()
      #end
#查询财务订单列表--结束

#查询财务订单商品列表--开始
#sql("getCaiWuListOrderItem")
SELECT pt.`name` goodsName,pt.quantity,pt.price
from order_item pt  WHERE pt.
      #end
#查询财务订单列表--结束

#查询财务订单导出--开始
#sql("getCaiwuListExcel")
SELECT case o.type  WHEN 0 THEN '普通订单' WHEN 1 THEN '兑换订单' WHEN 2 THEN '帮抢订单' WHEN 3 THEN '倒拍订单' WHEN 4 THEN '喵币商品订单' WHEN 5 THEN '满减订单'  WHEN 6 THEN 'vip置换订单' END type ,
o.sn,o.create_date,m.`nickname` name ,case o.`status`  WHEN 5 THEN '已完成'   end status ,
pt.`name` goodsName,pt.quantity,pt.price,o.miaobi_paid,o.amount_paid,o.amount,o.fee,o.freight,o.id,o.weiXin_paid,o.aLi_paid,o.order_no from
order_item pt LEFT JOIN `order` o  on o.id=pt.order_id LEFT JOIN member m on m.id=o.member_id WHERE o.status in( 5, 9 ,3 ,4) #@getVIPAddAllSQL()
 #end
#查询财务订单导出--结束




#查询退货管理导出--开始
#sql("caiWuReturnGoods")
SELECT o.type, o.sn , r.sn return_sn ,r.modify_date, m.nickname,  r.`type` return_type ,pt.`name` goodsName,  pt.quantity, pt.amount   FROM `returns` r   LEFT JOIN returns_item pt on r.id=pt.return_id  LEFT JOIN `order` o on o.id=r.order_id
LEFT JOIN member m on m.id=pt.member_id  WHERE r.`type` = 6 AND r.category = 2  #@getVIPAddAllSQL()
#end
#查询退货管理导出--结束

#查询退货管理--开始
#sql("caiWuReturnGoodsExcel")
SELECT case o.type  WHEN 0 THEN '普通订单' WHEN 1 THEN '兑换订单' WHEN 2 THEN '帮抢订单' WHEN 3 THEN '倒拍订单' WHEN 4 THEN '喵币商品订单' WHEN 5 THEN '满减订单' END type,
 o.sn , r.sn return_sn ,r.modify_date, m.nickname,  r.`type` return_type   ,pt.`name` goodsName,  pt.quantity, pt.amount   FROM `returns` r   LEFT JOIN returns_item pt on r.id=pt.return_id  LEFT JOIN `order` o on o.id=r.order_id
LEFT JOIN member m on m.id=pt.member_id  WHERE r.`type` = 6 AND r.category = 2 #@getVIPAddAllSQL()
#end
#查询退货管理--结束



#查询用户足迹列表--开始
#sql("userFootPrint")
SELECT m.id uid,m.phone,m.nickname nickname, m.`username` username, m.`phone`, COUNT(*) goodsNum FROM footprint pt, member m
WHERE m.id=pt.memberId #@getVIPAddAllSQL()
GROUP BY pt.memberId
#end
#查询用户足迹列表--结束

#查询用户足迹详情列表--开始
#sql("userDetailsFootPrint")
SELECT g.id goodsId,g.`name`,pt.create_date FROM footprint pt LEFT JOIN goods g on g.id=pt.goodsId WHERE pt.memberId=#(uid)
#@getVIPAddAllSQL()
#end
#查询用户足迹详情列表--结束

#查询商品足迹列表--开始



#sql("goodsFootPrint")
SELECT g.id goodsId,g.`name`, COUNT(*) userNum  FROM footprint pt LEFT JOIN goods g on g.id=pt.goodsId
WHERE 1=1  #@getVIPAddAllSQL()
#if(p.searchValue != null)
 and (g.id like  '%#(p.searchValue)%'  or g.name like   '%#(p.searchValue)%' )
#end
GROUP BY pt.goodsId

#if(p.orderProperty != null)
 ORDER  BY  #(p.orderProperty)   #(p.orderDirection)
#end

#end
#查询商品足迹列表--结束




#查询商品详情足迹列表--开始
#sql("goodsrDetailsFootPrint")
SELECT m.id uid,m.phone, m.username, m.nickname,pt.create_date from footprint pt LEFT JOIN member m on m.id=pt.memberId WHERE pt.goodsId=#(goodsId) #@getVIPAddAllSQL()
#end
#查询商品详情足迹列表--结束


#查看商品审核列表--开始
#define getGoodsOperFlowListSQL()

#if(submitId != null)
  and  submitId=#(submitId)
#end

and pt.verifyResult>0


#if(sumitSTime != null)
  and DATE(pt.create_date)>='#(sumitSTime)'
#end

#if(sumitETime != null)
 and DATE(pt.create_date)<='#(sumitETime )'
#end

#if(verifySTime != null)
 and  DATE(pt.verifyTime)>='#(verifySTime)'
#end

#if(verifyETime != null)
 and DATE(pt.verifyTime)<='#(verifyETime)'
#end


#if(p.searchProperty=='goodsId'  && p.searchValue !=null)
 and pt.goodsId='#(p.searchValue)'
#end

#if(p.searchProperty=='submitName' && p.searchValue !=null)
 and ad.username like '%#(p.searchValue)%'
#end

#if(verifyResult !='-1')
 and  verifyResult=#(verifyResult)
#end

#if(p.orderProperty !=null  )
   ORDER  BY  #(p.orderProperty)        #(p.orderDirection)
#else
 ORDER  BY pt.create_date desc
#end


#end



#sql("getGoodsOperFlowList")
SELECT  pt.optType, pt.verifySuggest, pt.id,pt.info,pt.create_date, ad.username  submitName, pt.verifyResult ,ad2.name verifyName,
pt.verifyTime,pt.goodsId,g.name goodsName  from goods_oper_flow pt  LEFT JOIN admin ad on  pt.submitId=ad.id  LEFT JOIN admin ad2 on ad2.id=pt.verifyId  LEFT  JOIN  goods g
on g.id=pt.goodsId
WHERE 1=1  #@getGoodsOperFlowListSQL()
#end
#查看商品审核列表--结束










