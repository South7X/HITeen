package com.hitsz.eatut;


import android.util.Log;

import com.hitsz.eatut.database.DishInfo;
import com.hitsz.eatut.database.MyOrder;
import com.hitsz.eatut.MapRelate.MapFunc;
import com.hitsz.eatut.datepicker.DateFormatUtils;

import org.litepal.LitePal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class StatisticData {
    private long dayPeriod = 86400000L;
    private long weekPeriod = 604800000L;
    private long monthPeriod = 2592000000L;
    private long yearPeriod = 31536000000L;
    public ArrayList<Long> EndTimeStatistic(int userID){
        /*
        * 获取当前用户的用餐时间列表；
        * Input: userID-用户ID，可以通过下面两行代码获取：
        SharedPreferences pref2 = this.getSharedPreferences("currentID",MODE_PRIVATE);
        int userID = pref2.getInt("userID", -1);
        * Output: 用户用餐时间List，为时间戳形式。
        * */
        List<MyOrder> myOrderList=LitePal.where("userID=?", "" + userID).find(MyOrder.class);
        ArrayList<Long> endTimeList = new ArrayList<>();
        for(MyOrder one: myOrderList){
            endTimeList.add(one.getEndTime());
        }
        return endTimeList;
    }
    private long[] weekStamp() {
        /*
         * 返回当前周的时间戳：本周7天每天的起止时间戳共有8个
         * */
        long[] weekStamp = new long[9];
        //获取当前周星期一的起始时间戳
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0,
                0, 0);
        int day_of_week = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0) {
            day_of_week = 7;
        }
        cal.add(Calendar.DATE, -day_of_week + 1);
        weekStamp[0] = cal.getTime().getTime();
        //完成weekStamp
        for (int i = 1; i < 8; i++) {
            weekStamp[i] = weekStamp[i - 1] + dayPeriod;
        }
        for (int i = 0; i < 8; i++) {
            Log.d("StatisticTest", DateFormatUtils.long2Str(weekStamp[i], true));
        }
        return weekStamp;
    }
    public float[] weekCost(int userID){
        /*
        * 返回本周七天开销数组
        * */
        List<MyOrder> myOrderList=LitePal.where("userID=?", "" + userID).find(MyOrder.class);
        float[] weekCost = new float[7];
        long[] weekStamp = weekStamp();
        for(MyOrder one:myOrderList){
            long endTime = one.getEndTime();
            float cost = one.getCost();
            for(int i=0;i<7;i++){
                //一天：[0:00, 24:00)左闭右开
                if(endTime>=weekStamp[i] && endTime<weekStamp[i+1]){
                    weekCost[i] += cost;
                }
            }
        }
        return weekCost;
    }
    public ArrayList<Float> CostStatistic(int userID, int classifiedFlag){
        /*
        * 获取当前用户的用餐支出列表；
        * Input:
        * userID-用户ID；
        * classifiedFlag-分类方式，0-周/1-月/2-年，对应7天内、30天内、365天内的数据；
        * Output: 用户用餐支出List;
        * */



        List<MyOrder> myOrderList=LitePal.where("userID=?", "" + userID).find(MyOrder.class);
        ArrayList<Float> costList = new ArrayList<>();
        long currentTime =  System.currentTimeMillis();
        //一周(ms): 604,800,000 一月(ms): 2,592,000,000 默认为平年(ms): 31,536,000,000
        //下为一周前、一月前、一年前的时间节点

        long weekAgo = currentTime - 604800000L;
        long monthAgo = currentTime - 2592000000L;
        long yearAgo = currentTime - 31536000000L;
        switch (classifiedFlag) {
            case 0:
                //周
                for(MyOrder one:myOrderList){
                    long endTime =one.getEndTime();
                    if(endTime >= weekAgo){
                        costList.add(one.getCost());
                    }
                }
                break;
            case 1:
                //月
                for(MyOrder one:myOrderList){
                    long endTime =one.getEndTime();
                    if(endTime >= monthAgo){
                        costList.add(one.getCost());
                    }
                }
                break;
            case 2:
                //年
                for(MyOrder one:myOrderList){
                    long endTime =one.getEndTime();
                    if(endTime >= yearAgo){
                        costList.add(one.getCost());
                    }
                }
                break;
            default:
                break;
        }
        return costList;
    }
    public HashMap<String, Integer> PreferStatistic(int userID, int classifiedFlag){
        /*
        * 用餐偏好统计，返回Map<Tag, Count>
        * input; userID, classifiedFlag;
        * 0-档口；
        * 1-口味：注意dishInfo里的Tag只有1个String、用$划分，e.g.Tag=tag1$tag$...
        * */
        List<MyOrder> myOrderList=LitePal.where("userID=?", "" + userID).find(MyOrder.class);
        HashMap<String, Integer> preferMap = new HashMap<>();
        MapFunc mapFunc = new MapFunc();
        switch (classifiedFlag){
            case 0:
                //档口
                //遍历所有订单，取出食物列表，遍历所有食物，得到所属档口，将其放入Map
                for(MyOrder one:myOrderList){
                    ArrayList<Integer> dishID = one.getDishID_III();
                    for(int i=0;i<dishID.size();i++){
                        DishInfo dishInfo = LitePal.where("id=?", ""+dishID.get(i)).find(DishInfo.class).get(0);
                        String canteen = dishInfo.getBelongToCanteen();
                        String window = dishInfo.getBelongToWindow();
                        String canteenWindow = canteen + "-" + window;
                        preferMap = mapFunc.initStringKeyMap(preferMap, canteenWindow);
                    }
                }
                break;
            case 1:
                //口味
                //遍历所有订单，取出食物列表，遍历所有食物，切割得到所有标签，将其放入Map
                for(MyOrder one: myOrderList){
                    ArrayList<Integer> dishID = one.getDishID_III();
                    for(int i=0;i<dishID.size();i++){
                        DishInfo dishInfo = LitePal.where("id=?", ""+dishID.get(i)).find(DishInfo.class).get(0);
                        String tagSeq = dishInfo.getDishTags();
                        String[] tags = tagSeq.split("$");
                        for(String tag:tags){
                            preferMap = mapFunc.initStringKeyMap(preferMap, tag);
                        }
                    }
                }
                break;
                default:
                    break;
        }
        return preferMap;
    }
}
