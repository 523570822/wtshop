###搜索历史倒拍订单
#sql("getHistoryOrderList")
 SELECT g.`name`,p.specification_values ,rh.create_date, CASE WHEN  rh.member_id >0 THEN m.username ELSE rh.robot_name END username
,rh.current_price,rd.auction_price,g.image,m.avatar
  FROM reverse_auction_histroy rh LEFT JOIN  reverse_auction_detail rd on rh.reverseauction_detail_id= rd.id
LEFT JOIN product p ON p.id=rd.product_id  LEFT JOIN goods  g ON p.goods_id= g.id LEFT JOIN member m ON m.id=rh.member_id
  WHERE rh.reverseauction_id=#para(actId)
#end

###详情中历史订单成交量
#sql("topHistoryOrderList")
SELECT rh.create_date, CASE WHEN  rh.member_id >0 THEN m.username ELSE rh.robot_name END username
,rh.current_price,m.avatar
  FROM reverse_auction_histroy rh LEFT JOIN  reverse_auction_detail rd on rh.reverseauction_detail_id= rd.id
LEFT JOIN product p ON p.id=rd.product_id   LEFT JOIN member m ON m.id=rh.member_id
  WHERE rh.reverseauction_id=#para(actId) AND p.id=#para(goodsId) LIMIT 5
#end

###评价
#sql("daopaipingjia")
SELECT  m.avatar,m.username,r.score, r.content,r.modify_date ,r.images  FROM review r LEFT JOIN member m on r.member_id=m.id
WHERE r.goods_id=#para(goodsId)
#end

###商品详情

#sql("goods")
SELECT introduction from goods WHERE id=#para(goodsId)
#end



#define getVIPAddAllSQL()

#if(beginDate != '')
 and DATE(pt.create_date)>='#(beginDate)'
#end

#if(endDate != '')
 and DATE(pt.create_date)<='#(endDate )'
#end
#end


###倒拍订单
#sql("daopaiOrder")
SELECT CASE  WHEN m.id> 0 then 0 ELSE  1  END type,
 CASE  WHEN m.id> 0 then m.nickname ELSE pt.robot_name END mname,g.`name`,p.specification_values,rd.auction_cost,rd.auction_price,pt.current_price,
 pt.create_date
FROM  reverse_auction_histroy pt LEFT JOIN member m on m.id=pt.member_id LEFT JOIN reverse_auction_detail rd on
rd.id=pt.reverseauction_detail_id LEFT JOIN product p on p.id=rd.product_id LEFT JOIN goods g on g.id=p.goods_id where 1=1 and pt.reverseauction_id=#para(id)    #@getVIPAddAllSQL()
#end


###优惠券分享列表
#sql("couponShareList")
SELECT pt.*,c.`name` FROM coupon_share pt LEFT JOIN coupon c on pt.couponId=c.id
#end

