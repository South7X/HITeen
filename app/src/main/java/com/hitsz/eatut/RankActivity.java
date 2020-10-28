package com.hitsz.eatut;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hitsz.eatut.adapter.RankingAdapter;
import com.hitsz.eatut.adapter.rankingItem;
import com.hitsz.eatut.database.DishInfo;
import com.hitsz.eatut.database.MyOrder;
import com.hitsz.eatut.MapRelate.MapFunc;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class RankActivity extends AppCompatActivity implements View.OnClickListener{
    private Button weekBtn;
    private Button monthBtn;
    private RecyclerView recyclerView;
    private List<rankingItem> weekRankingList = new ArrayList<>();
    private List<rankingItem> monthRankingList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_rank);
        findView();
        initWeekRanking();
        initWeekRecycle();

    }
    public void onClick(View v){
        switch(v.getId()){
            case R.id.week_ranking_btn:
                initWeekRanking();
                initWeekRecycle();
                weekBtn.setTextColor(this.getResources().getColor(R.color.colorPrimary));
                monthBtn.setTextColor(this.getResources().getColor(R.color.black));
                break;
            case R.id.month_ranking_btn:
                initMonthRanking();
                initMonthRecycle();
                monthBtn.setTextColor(this.getResources().getColor(R.color.colorPrimary));
                weekBtn.setTextColor(this.getResources().getColor(R.color.black));
                break;
        }
    }
    protected void findView(){
        recyclerView = findViewById(R.id.ranking_recycle);
        weekBtn = findViewById(R.id.week_ranking_btn);
        weekBtn.setOnClickListener(this);
        monthBtn = findViewById(R.id.month_ranking_btn);
        monthBtn.setOnClickListener(this);
    }
    private void initWeekRanking(){
        /*查询最近一周的销量前10*/
        MapFunc mapFunc = new MapFunc();
        long currentTime =  System.currentTimeMillis();
        List<MyOrder> myOrderList = LitePal.findAll(MyOrder.class);
        if(!myOrderList.isEmpty()) {
            HashMap<Integer, Integer> weekOrderTimeMap = new HashMap<>();
            //Map赋值
            for (MyOrder one : myOrderList) {
                long endTime = one.getEndTime();
                long timePeriod = currentTime - endTime;
                //一周(ms): 604,800,000 一月(ms): 2,592,000,000
                if (timePeriod <= 604800000L) {
                    weekOrderTimeMap = mapFunc.initIntegerKeyMap(weekOrderTimeMap, one);
                }
            }
            //Map排序
            weekOrderTimeMap = mapFunc.sortIntegerKeyMap(weekOrderTimeMap);
            //将Map值复制到List中
            weekRankingList.clear();
            List<HashMap.Entry<Integer, Integer>> entryList = new ArrayList<HashMap.Entry<Integer, Integer>>(weekOrderTimeMap.entrySet());
            Iterator<HashMap.Entry<Integer, Integer>> iterator = entryList.iterator();
            HashMap.Entry<Integer, Integer> tmpEntry = null;
            int i = 0;
            while (iterator.hasNext() && i < 10) {
                tmpEntry = iterator.next();
                int dishID = tmpEntry.getKey();
                int orderTimes = tmpEntry.getValue();
                Log.d("RankingRelate", "weekMap复制到List中，dishID=" + dishID + " orderTimes=" + orderTimes);
                DishInfo dishInfo = (LitePal.where("id = ?", "" + dishID).find(DishInfo.class)).get(0);//int型要加""转成string
                rankingItem ra = new rankingItem(i + 1, dishInfo.getBelongToCanteen() + "-" + dishInfo.getBelongToWindow(),
                        dishInfo.getDishName(), orderTimes, dishInfo.getImageID(), dishInfo.getDishScore(), dishInfo.getDishPrice());
                weekRankingList.add(ra);
                i++;
            }
        }

    }
    private void initWeekRecycle(){
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        RankingAdapter weekAdapter = new RankingAdapter(weekRankingList);
        recyclerView.setAdapter(weekAdapter);
    }
    private void initMonthRanking(){
        /*查询最近一月的销量前10*/
        MapFunc mapFunc = new MapFunc();
        long currentTime =  System.currentTimeMillis();
        List<MyOrder> myOrderList = LitePal.findAll(MyOrder.class);
        if(!myOrderList.isEmpty()) {
            HashMap<Integer, Integer> monthOrderTimeMap = new HashMap<>();
            //Map赋值
            for (MyOrder one : myOrderList) {
                long endTime = one.getEndTime();
                long timePeriod = currentTime - endTime;
                //一周(ms): 604,800,000 一月(ms): 2,592,000,000
                if (timePeriod <= 2592000000L) {
                    monthOrderTimeMap = mapFunc.initIntegerKeyMap(monthOrderTimeMap, one);
                }
            }
            //Map排序
            monthOrderTimeMap = mapFunc.sortIntegerKeyMap(monthOrderTimeMap);
            //将Map值复制到List中
            monthRankingList.clear();
            List<HashMap.Entry<Integer, Integer>> entryList2 = new ArrayList<HashMap.Entry<Integer, Integer>>(monthOrderTimeMap.entrySet());
            Iterator<HashMap.Entry<Integer, Integer>> iterator2 = entryList2.iterator();
            HashMap.Entry<Integer, Integer> tmpEntry2 = null;
            int i = 0;
            while (iterator2.hasNext() && i < 10) {
                tmpEntry2 = iterator2.next();
                int dishID = tmpEntry2.getKey();
                int orderTimes = tmpEntry2.getValue();
                Log.d("RankingRelate", "monthMap复制到List中，dishID=" + dishID + " orderTimes=" + orderTimes);
                DishInfo dishInfo = (LitePal.where("id=?", "" + dishID).find(DishInfo.class)).get(0);
                rankingItem ra = new rankingItem(i + 1, dishInfo.getBelongToCanteen() + "-" + dishInfo.getBelongToWindow(),
                        dishInfo.getDishName(), orderTimes, dishInfo.getImageID(), dishInfo.getDishScore(), dishInfo.getDishPrice());
                monthRankingList.add(ra);
                i++;
            }
            Log.d("RankingRelate", "复制完成，weekListSize=" + weekRankingList.size() + " monthListSize=" + monthRankingList.size());
        }
    }
    private void initMonthRecycle(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        RankingAdapter monthAdapter = new RankingAdapter(monthRankingList);
        recyclerView.setAdapter(monthAdapter);
    }
//    private void initRanking(){
//        /*查询最近一周的销量前10 & 最近一个月的销量前10的菜品*/
//        MapFunc mapFunc = new MapFunc();
//        long currentTime =  System.currentTimeMillis();
//        List<MyOrder> myOrderList = LitePal.findAll(MyOrder.class);
//        if(!myOrderList.isEmpty()) {
//            HashMap<Integer, Integer> weekOrderTimeMap = new HashMap<>();
//            HashMap<Integer, Integer> monthOrderTimeMap = new HashMap<>();
//            //Map赋值
//            for (MyOrder one : myOrderList) {
//                long endTime = one.getEndTime();
//                long timePeriod = currentTime - endTime;
//                //一周(ms): 604,800,000 一月(ms): 2,592,000,000
//                if (timePeriod <= 2592000000L && timePeriod >= 604800000L) {
//                    monthOrderTimeMap = mapFunc.initIntegerKeyMap(monthOrderTimeMap, one);
//                } else if (timePeriod <= 604800000L) {
//                    monthOrderTimeMap = mapFunc.initIntegerKeyMap(monthOrderTimeMap, one);
//                    weekOrderTimeMap = mapFunc.initIntegerKeyMap(weekOrderTimeMap, one);
//                }
//            }
//            Log.d("RankingRelate", "Map初始化后，weekMapSize=" + weekOrderTimeMap.size() + " monthMapSize=" + monthOrderTimeMap.size());
//            //Map排序
//            weekOrderTimeMap = mapFunc.sortIntegerKeyMap(weekOrderTimeMap);
//            monthOrderTimeMap = mapFunc.sortIntegerKeyMap(monthOrderTimeMap);
//            Log.d("RankingRelate", "Map排序完成，weekMapSize=" + weekOrderTimeMap.size() + " monthMapSize=" + monthOrderTimeMap.size());
//            //将Map值复制到List中
//            List<HashMap.Entry<Integer, Integer>> entryList = new ArrayList<HashMap.Entry<Integer, Integer>>(weekOrderTimeMap.entrySet());
//            Iterator<HashMap.Entry<Integer, Integer>> iterator = entryList.iterator();
//            HashMap.Entry<Integer, Integer> tmpEntry = null;
//            int i = 0;
//            while (iterator.hasNext() && i < 10) {
//                tmpEntry = iterator.next();
//                int dishID = tmpEntry.getKey();
//                int orderTimes = tmpEntry.getValue();
//                Log.d("RankingRelate", "weekMap复制到List中，dishID=" + dishID + " orderTimes=" + orderTimes);
//                DishInfo dishInfo = (LitePal.where("id = ?", "" + dishID).find(DishInfo.class)).get(0);//int型要加""转成string
//                rankingItem ra = new rankingItem(i + 1, dishInfo.getBelongToCanteen() + " " + dishInfo.getBelongToWindow(), dishInfo.getDishName(), orderTimes, dishInfo.getImageID());
//                weekRankingList.add(ra);
//                i++;
//            }
//            List<HashMap.Entry<Integer, Integer>> entryList2 = new ArrayList<HashMap.Entry<Integer, Integer>>(monthOrderTimeMap.entrySet());
//            Iterator<HashMap.Entry<Integer, Integer>> iterator2 = entryList2.iterator();
//            HashMap.Entry<Integer, Integer> tmpEntry2 = null;
//            i = 0;
//            while (iterator2.hasNext() && i < 10) {
//                tmpEntry2 = iterator2.next();
//                int dishID = tmpEntry2.getKey();
//                int orderTimes = tmpEntry2.getValue();
//                Log.d("RankingRelate", "monthMap复制到List中，dishID=" + dishID + " orderTimes=" + orderTimes);
//                DishInfo dishInfo = (LitePal.where("id=?", "" + dishID).find(DishInfo.class)).get(0);
//                rankingItem ra = new rankingItem(i + 1, dishInfo.getBelongToCanteen() + "-" + dishInfo.getBelongToWindow(), dishInfo.getDishName(), orderTimes, dishInfo.getImageID());
//                monthRankingList.add(ra);
//                i++;
//            }
//            Log.d("RankingRelate", "复制完成，weekListSize=" + weekRankingList.size() + " monthListSize=" + monthRankingList.size());
//        }
//    }
//    private void initRecycle(){
//        //要分别设置layoutManager
//        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
//        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
//        weekRecyclerView.setLayoutManager(layoutManager);
//        monthRecyclerView.setLayoutManager(layoutManager2);
//        RankingAdapter weekAdapter = new RankingAdapter(weekRankingList);
//        RankingAdapter monthAdapter = new RankingAdapter(monthRankingList);
//        weekRecyclerView.setAdapter(weekAdapter);
//        monthRecyclerView.setAdapter(monthAdapter);
//    }
}
