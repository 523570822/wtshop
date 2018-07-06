package com.wtshop.util;

/**
 * Created by Administrator on 2017/7/10.
 */
//指定概率抽奖程序
//https://yq.aliyun.com/articles/44578

import org.apache.commons.collections.map.HashedMap;

import java.util.*;

/**
 * 不同概率抽奖工具包
 * @author:rex
 * @date:2014年10月20日
 * @version:1.0
 */
public class LotteryUtil {
    /**
     * 抽奖
     *
     * @param orignalRates 原始的概率列表，保证顺序和实际物品对应
     * @return 物品的索引
     */
    public static int lottery(List<Double> orignalRates) {
        if (orignalRates == null || orignalRates.isEmpty()) {
            return -1;
        }

        int size = orignalRates.size();

        // 计算总概率，这样可以保证不一定总概率是1
        double sumRate = 0d;
        for (double rate : orignalRates) {
            sumRate += rate;
        }

        // 计算每个物品在总概率的基础下的概率情况
        List<Double> sortOrignalRates = new ArrayList<Double>(size);
        Double tempSumRate = 0d;
        for (double rate : orignalRates) {
            tempSumRate += rate;
            sortOrignalRates.add(tempSumRate / sumRate);
        }

        // 根据区块值来获取抽取到的物品索引
        double nextDouble = Math.random();
        sortOrignalRates.add(nextDouble);
        Collections.sort(sortOrignalRates);

        return sortOrignalRates.indexOf(nextDouble);
    }

    //抽奖 参数:  key( probability:概率 和 id)
    public static Map<String,Object> lotteryMap(List<Map<String,Object>> orignalRates) {
        if (orignalRates == null || orignalRates.isEmpty()) {
            return null;
        }

        int size = orignalRates.size();

        // 计算总概率，这样可以保证不一定总概率是1
        double sumRate = 0d;
        for (Map<String,Object> map: orignalRates) {
            double rate=(double)map.get("probability");
            sumRate += rate;
        }


        // 计算每个物品在总概率的基础下的概率情况
        List<Double> sortOrignalRates = new ArrayList<>();
        Double tempSumRate = 0d;
        for (Map<String,Object> map:  orignalRates) {
            tempSumRate += (double)map.get("probability");
           double newProbability=  tempSumRate / sumRate;
            sortOrignalRates.add(newProbability);
        }

        // 根据区块值来获取抽取到的物品索引
        double nextDouble = Math.random();
        sortOrignalRates.add(nextDouble);
        Collections.sort(sortOrignalRates);

        int index=sortOrignalRates.indexOf(nextDouble);

        Map<String,Object> map=orignalRates.get(index);
        map.put("index",index);

        return map;

    }

    public static void main(String[] args) {

        List<Map<String,Object>> list=new ArrayList<>();
        Map<String,Object> map =new HashedMap();  map.put("id",55L); map.put("probability",0.1); list.add(map);
        Map<String,Object> map1 =new HashedMap();  map1.put("id",2L); map1.put("probability",0.9); list.add(map1);
        Map<String,Object> map3 =new HashedMap();  map3.put("id",44L); map3.put("probability",0.3); list.add(map3);
        Map<String,Object> map4 =new HashedMap();  map4.put("id",66L); map4.put("probability",0.4); list.add(map4);
        Map<String,Object> map5 =new HashedMap();  map5.put("id",3L); map5.put("probability",0.8); list.add(map5);
        Map<String,Object> map6 =new HashedMap();  map6.put("id",19L); map6.put("probability",0.21); list.add(map6);
        Map<String,Object> map7 =new HashedMap();  map7.put("id",321L); map7.put("probability",0.21); list.add(map7);
        Map<Long, Integer> count = new HashMap();
        double num = 1000000;
        for (int i=0;i<num;i++){
            Map<String,Object> mapx=lotteryMap(list);
            Long id=(long)mapx.get("id");
              Integer value = count.get(id);
              count.put(id, value == null ? 1 : value + 1);


        }


        for (Map.Entry<Long, Integer> entry : count.entrySet()) {

            for (Map<String,Object> m:list){
                if (((Long)m.get("id")).equals(entry.getKey())){
                    System.out.println("id:" + entry.getKey()+", 中奖次数count=" + entry.getValue() +",抽奖前概率:"+m.get("probability") +", 实际得奖概率="
                            + entry.getValue() / num);
                }

            }

        }



    }

}