package com.hitsz.eatut.MapRelate;

import com.hitsz.eatut.database.MyOrder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

public class MapFunc {

    public HashMap<Integer, Integer> initIntegerKeyMap(HashMap<Integer, Integer>map, MyOrder myOrder) {
        /*
        * 输入订单对象，返回<dishID, count>Map
        *  */
            ArrayList<Integer> dishIDs = myOrder.getDishID_III();
            for (int i = 0; i < dishIDs.size(); i++) {
                //遍历所有dishID，对应Key
                int tempKey = dishIDs.get(i);
                if (map.containsKey(tempKey)) {
                    //已经记录过该ID，则+1操作
                    int currentValue = map.get(tempKey);
                    map.put(tempKey, currentValue + 1);
                } else {
                    //否则创建该ID
                    map.put(tempKey, 1);
                }
            }
            return map;
    }
    public HashMap<String, Integer> initStringKeyMap(HashMap<String, Integer>map, String key) {
        /*
         * 输入String，返回<String, count>Map
         *  */
        if(map.containsKey(key)){
            int currentValue = map.get(key);
            map.put(key, currentValue + 1);
        }else{
            map.put(key, 1);
        }
        return map;
    }
    public HashMap<Integer, Integer> sortIntegerKeyMap(HashMap<Integer, Integer> map){
        /*
        * <Integer, Integer>HashMap根据value排序
        * */
        if(map == null||map.isEmpty()){
            return null;
        }
        HashMap<Integer, Integer> sortedMap = new LinkedHashMap<>();
        List<HashMap.Entry<Integer, Integer>> entryList = new ArrayList<HashMap.Entry<Integer, Integer>>(map.entrySet());
        entryList.sort(new MapValueComparator());
        Iterator<HashMap.Entry<Integer, Integer>> iterator = entryList.iterator();
        HashMap.Entry<Integer, Integer> tmpEntry = null;
        while(iterator.hasNext()){
            tmpEntry = iterator.next();
            sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
        }
        return sortedMap;
    }

}
