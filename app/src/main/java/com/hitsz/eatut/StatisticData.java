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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class StatisticData {
    private long dayPeriod = 86400000L;
    private long weekPeriod = 604800000L;
    private long monthPeriod = 2678400000L;//31天
    private long yearPeriod = 31536000000L;

    public Object[] EndTimeStatistic(int userID){
        /*
        * 获取当前用户的用餐时间列表；
        * Input: userID-用户ID，可以通过下面两行代码获取：
        SharedPreferences pref2 = this.getSharedPreferences("currentID",MODE_PRIVATE);
        int userID = pref2.getInt("userID", -1);
        * Output: 用户用餐时间List，为时间戳形式。
        * */
        List<MyOrder> myOrderList=LitePal.where("userID=?", "" + userID).find(MyOrder.class);
        Object[] endTimes = new Object[myOrderList.size()];
        for(int i=0;i<myOrderList.size();i++)
        {
            endTimes[i] = myOrderList.get(i).getEndTime();
        }
        return endTimes;
    }
    private long[] getWeekStamp() {
        /*
         * 返回当前周的时间戳：本周7天每天的起止时间戳共有8个
         * */
        long[] weekStamp = new long[8];
        //获取当前周星期一的起始时间戳
        weekStamp[0] = DateUtils.getBeginDayOfWeek();
        //完成weekStamp
        for (int i = 1; i < 8; i++) {
            weekStamp[i] = weekStamp[i - 1] + dayPeriod;
        }
        return weekStamp;
    }
    public Object[] weekCost(int userID){
        /*
        * 返回本周七天开销数组
        * */
        List<MyOrder> myOrderList=LitePal.where("userID=?", "" + userID).find(MyOrder.class);
        Object[] weekCost = new Object[7];
        for(int i=0;i<7;i++){
            //初始化
            weekCost[i] = 0.0F;
        }
        long[] weekStamp = getWeekStamp();
        for(MyOrder one:myOrderList){
            long endTime = one.getEndTime();
            float cost = one.getCost();
            for(int i=0;i<7;i++){
                //一天：[0:00, 24:00)左闭右开
                if(endTime>=weekStamp[i] && endTime<weekStamp[i+1]){
                    float temp = (float)weekCost[i];
                    temp+=cost;
                    weekCost[i]=temp;
                }
            }
        }
        return weekCost;
    }
    private long[] getMonthStamp(){
        /*
        * 返回本月每天的起止时间戳，因为月份的天数不一样，所以用下个月的第一天作为条件判断
        * */
        long[] monthStamp = new long[32];
        long thisMonthBeginStamp = DateUtils.getBeginDayOfMonth(DateUtils.getNowMonth());//本月第一天
        monthStamp[0] = thisMonthBeginStamp;//第一天开始
        long nextMonthBeginStamp = DateUtils.getBeginDayOfMonth(DateUtils.getNowMonth()+1);//下一个月第一天
        long temp = thisMonthBeginStamp;
        int i=1;
        while(temp<nextMonthBeginStamp){
            monthStamp[i] = monthStamp[i-1] + dayPeriod;
            temp += dayPeriod;
            Log.d("StatisticTest", DateFormatUtils.long2Str(monthStamp[i], true));
            i+=1;
        }
        return monthStamp;
    }
    public Object[] monthCost(int userID){
        /*
         * 返回本月到目前为止每天的花销
         * */
        List<MyOrder> myOrderList=LitePal.where("userID=?", "" + userID).find(MyOrder.class);
        Object[] monthCost = new Object[31];//但不一定每个月份都有31天
        for(int i=0;i<31;i++){
            //初始化
            monthCost[i] = 0.0F;
        }
        long[] monthStamp = getMonthStamp();
        for(MyOrder one:myOrderList){
            long endTime = one.getEndTime();
            float cost = one.getCost();
            for(int i=0;i<31;i++){
                //一个月：[1, )左闭右开
                if(endTime>=monthStamp[i] && endTime<monthStamp[i+1]){
                    float temp = (float)monthCost[i];
                    temp+=cost;
                    monthCost[i]=temp;
                }
            }
        }
        return monthCost;
    }
    private long[] getYearStamp() {
        /*
         * 返回本年12个月的起止时间戳。
         * */
        long[] yearStamp = new long[13];
        //获得每个月的开始时间戳
        for(int i=0;i<12;i++){
            yearStamp[i] = DateUtils.getBeginDayOfMonth(i+1);
        }
        //单独求得下一年一月开始时间戳
        yearStamp[12] = yearStamp[11] + monthPeriod;
        return yearStamp;
    }
    public Object[] yearCost(int userID){
        /*
         * 返回本年12个月开销数组
         * */
        List<MyOrder> myOrderList=LitePal.where("userID=?", "" + userID).find(MyOrder.class);
        Object[] yearCost = new Object[12];
        for(int i=0;i<12;i++){
            //初始化
            yearCost[i] = 0.0F;
        }
        long[] yearStamp = getYearStamp();
        for(MyOrder one:myOrderList){
            long endTime = one.getEndTime();
            float cost = one.getCost();
            for(int i=0;i<12;i++){
                //一个月：[1, )左闭右开
                if(endTime>=yearStamp[i] && endTime<yearStamp[i+1]){
                    float temp = (float)yearCost[i];
                    temp+=cost;
                    yearCost[i]=temp;
                }
            }
        }
        return yearCost;
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
