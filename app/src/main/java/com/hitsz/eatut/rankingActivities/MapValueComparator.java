package com.hitsz.eatut.rankingActivities;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class MapValueComparator implements Comparator<HashMap.Entry<Integer, Integer>> {
    @Override
    public int compare(HashMap.Entry<Integer, Integer> o1, HashMap.Entry<Integer, Integer> o2) {
        //降序
        int data1 = o1.getValue();
        int data2 = o2.getValue();
        return Integer.compare(data2, data1);
    }
}