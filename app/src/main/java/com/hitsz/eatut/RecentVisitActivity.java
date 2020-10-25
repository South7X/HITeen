package com.hitsz.eatut;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hitsz.eatut.R;
import com.hitsz.eatut.StatisticData;
import com.hitsz.eatut.adapter.DishAdapter;
import com.hitsz.eatut.adapter.MyOrderAdapter;
import com.hitsz.eatut.adapter.MyOrderItem;
import com.hitsz.eatut.adapter.dish;
import com.hitsz.eatut.database.DishInfo;
import com.hitsz.eatut.database.MyOrder;
import com.hitsz.eatut.database.orderFood;
import com.hitsz.eatut.datepicker.DateFormatUtils;
import com.hitsz.eatut.managerActivities.OrderActivity;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
public class RecentVisitActivity extends AppCompatActivity implements View.OnClickListener{
    /*
     * 最近浏览界面
     * 功能：显示最近点的十个菜
     * */
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private List<dish> dishList = new ArrayList<>();
    private int userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_visit);
        SharedPreferences pref2=this.getSharedPreferences("currentID",MODE_PRIVATE);
        userID = pref2.getInt("userID", -1);
        findView();
        initfoods();
        initRecycle();
    }
    @Override
    protected void onRestart(){
        //当提交订单回到最近浏览页面时，应更新内容
        super.onRestart();
        initfoods();
        initRecycle();
    }
    protected void findView() {
        recyclerView = (RecyclerView) findViewById(R.id.recent_visit_recycle);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            //设置fab下滑消失上滑显示
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy > 0 && fab.getVisibility() == View.VISIBLE){
                    fab.hide();
                }else if(dy < 0 && fab.getVisibility() != View.VISIBLE){
                    fab.show();
                }
            }
        });
        fab=findViewById(R.id.fab);
        fab.setOnClickListener(this);
    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.fab:
                Intent intent=new Intent(this, CheckActivity.class);
                startActivity(intent);
                break;
        }
    }
    //初始化菜品信息
    private void initfoods(){
        //取10个菜就好啦
        int showNum = 10;
        List<MyOrder> myOrderList = LitePal.where("userID = ?", "" + userID).find(MyOrder.class);
        //按下单时间降序排序
        if(!myOrderList.isEmpty()) {
            Collections.sort(myOrderList, new Comparator<MyOrder>() {
                @Override
                public int compare(MyOrder m1, MyOrder m2) {
                    long orderTime1 = m1.getOrderTime();
                    long orderTime2 = m2.getOrderTime();
                    return (int) (orderTime2 - orderTime1);
                }
            });
            List<Integer> recentDishIDs = new ArrayList<>();
            for (MyOrder one : myOrderList) {
                if (recentDishIDs.size() >= showNum) {
                    break;
                }
                List<Integer> dishIDs = one.getDishID_III();
                for (Integer i : dishIDs) {
                    //如果已经有这道菜就跳过
                    if (!recentDishIDs.contains(i))
                        recentDishIDs.add(i);
                }
            }
            dishList.clear();
            int i = 0;
            while(i < showNum && i < recentDishIDs.size()){
                DishInfo dishInfo = LitePal.where("id = ?", "" + recentDishIDs.get(i)).find(DishInfo.class).get(0);
                String name = dishInfo.getDishName();
                int image = dishInfo.getImageID();
                float price = dishInfo.getDishPrice();
                float score = dishInfo.getDishScore();
                int dishID = dishInfo.getId();
                dish addDish = new dish(name, image, price, score, dishID);
                dishList.add(addDish);
                i+=1;
            }
        }
    }

    private void initRecycle(){
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        DishAdapter adapter=new DishAdapter(userID, dishList, new DishAdapter.daListener() {
            @Override
            public void da() {
                dishList.clear();
                initfoods();
                initRecycle();
            }
        });
        recyclerView.setAdapter(adapter);
    }
}
