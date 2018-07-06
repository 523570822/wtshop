package com.wtshop.util;

import com.jfinal.plugin.activerecord.Record;
import net.sourceforge.pinyin4j.PinyinHelper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 蔺哲 on 2017/8/25.
 */
public class PinYinUtil {
    public static String getPinYinHeadChar(String str) {
        String convert = "";
        for (int j = 0; j < str.length(); j++) {
            char word = str.charAt(j);
            // 提取汉字的首字母
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
            if (pinyinArray != null) {
                convert += pinyinArray[0].charAt(0);
            } else {
                convert += word;
            }
        }
        return convert;
    }

    /**
     * 通讯录首字母分组
     */
    public static Map<String,List> groupByUserName(List<Record> list) {
        String name1="";
        String oldPY = "";
        String thisPY = "";
        List nameList =new ArrayList();
        Map<String,List> map = new LinkedHashMap();
        for(int i=0;i<list.size();i++){
            name1 = list.get(i).get("name").toString();
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
}
