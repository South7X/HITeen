package com.hitsz.eatut.rankingActivities;

import com.hitsz.eatut.database.MyOrder;
import com.hitsz.eatut.rankingActivities.MapValueComparator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

public class RankingMapFunc {

    public HashMap<Integer, Integer> initMap(HashMap<Integer, Integer>map, MyOrder myOrder) {
        /*
        * 将MyOrder中的dishID放入Map
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
    public HashMap<Integer, Integer> sortMap(HashMap<Integer, Integer> map){
        /*
        * HashMap根据value排序
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
