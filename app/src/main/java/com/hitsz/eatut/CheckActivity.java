package com.hitsz.eatut;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hitsz.eatut.adapter.order;
import com.hitsz.eatut.adapter.orderFoodAdapter;
import com.hitsz.eatut.database.DishInfo;
import com.hitsz.eatut.database.UserInfo;
import com.hitsz.eatut.database.orderFood;
import com.hitsz.eatut.datepicker.CustomDatePicker;
import com.hitsz.eatut.datepicker.DateFormatUtils;
import com.hitsz.eatut.managerActivities.OrderActivity;
import com.hitsz.eatut.ui.Order_ui.OrderFragment;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

//import static com.hitsz.eatut.managerActivities.OrderActivity.saveOrderToManager;

/**
 * @author zhang
 */
public class CheckActivity extends AppCompatActivity implements View.OnClickListener{
    /*
    * 购物车页面
    * 功能：显示当前订单、用户手机号和地址、取餐时间和订单号。
    * */
    private RecyclerView recyclerView;
    private List<order> orderFoodsList=new ArrayList<>();
    private Button confirmbtn;
    private AlertDialog.Builder builder;
    private TextView orderNO;//订单号
    private TextView phonenum;//用户电话号码
    //时间选择
    private TextView mTvSelectedTime;
    private CustomDatePicker mTimerPicker;
    private int i;
    private long mTimeStamp;//时间戳
    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_check);
        findView();
        initOrder();
        initRecycle();
        initTimerPicker();
    }
    public void onClick(View v){
        switch(v.getId()){
            case R.id.confirm_btn:
                initOrder();
                if(orderFoodsList.isEmpty())
                {
                    DialogEmpty();
                }
                else {
                    i+=1;
                    Toast.makeText(v.getContext(),String.valueOf(i),Toast.LENGTH_SHORT).show();
                    SharedPreferences pref=this.getSharedPreferences("OrderCount",MODE_PRIVATE);
                    SharedPreferences.Editor editor=pref.edit();
                    editor.putInt("count",i);
                    editor.apply();
                    for(order one:orderFoodsList){
                        Log.d("order", one.getName());
                    }
                    OrderFragment.saveOrderToMyOrder(i, mTimeStamp);
                    Log.d("CheckActivityTimeStamp", Long.toString(mTimeStamp));
                    OrderActivity.saveOrderToManager(i);
                    LitePal.deleteAll(orderFood.class);
                    DialogSuccess();
                }
                break;
            case R.id.ll_time:
                //日期格式为yyyy-MM-dd HH:mm
                mTimerPicker.show(mTvSelectedTime.getText().toString());
                break;

        }
    }
    protected void findView(){
        recyclerView=(RecyclerView)findViewById(R.id.check_recycle);
        confirmbtn=(Button)findViewById(R.id.confirm_btn);
        builder=new AlertDialog.Builder(CheckActivity.this);
        confirmbtn.setOnClickListener(this);
        orderNO=findViewById(R.id.orderno_textview);
        phonenum=findViewById(R.id.phone_textview);

        findViewById(R.id.ll_time).setOnClickListener(this);
        mTvSelectedTime = findViewById(R.id.tv_selected_time);

        SharedPreferences pref=this.getSharedPreferences("OrderCount",MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor=pref.edit();
        i=pref.getInt("count",-1);
        orderNO.setText(String.valueOf(i+1));
        SharedPreferences pref2=this.getSharedPreferences("currentID",MODE_PRIVATE);
        int userID = pref2.getInt("userID", -1);
        UserInfo userInfo = LitePal.where("id = ?", "" + userID).find(UserInfo.class).get(0);
        String phoneNumber = userInfo.getTelephoneNumber();
        phonenum.setText(phoneNumber);
        Log.d("CheckActivityIDnPhone", userID + " " + phoneNumber);

    }
    private void DialogEmpty(){
        builder.setTitle("不想吃饭？");
        builder.setMessage("您还没有选择菜品惹。");
        builder.setPositiveButton("没饭吃=A=", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                AlertDialog dialog=builder.show();
                dialog.dismiss();
                finish();
            }
        });
        builder.show();
    }
    private void DialogSuccess(){
        builder.setTitle("NICE!!!");
        builder.setMessage("订单提交成功！");
        builder.setPositiveButton("有饭吃！www", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                AlertDialog dialog=builder.show();
                dialog.dismiss();
                finish();
            }
        });
        builder.show();
    }
    private void initOrder(){
        List<orderFood> orderFoods = LitePal.findAll(orderFood.class);
        orderFoodsList.clear();
        for (orderFood orderfood:orderFoods){
            int dishID = orderfood.getDishID_II();
            Log.d("CheckActivityDishID", Integer.toString(dishID));
            DishInfo dishInfo = (LitePal.where("id = ?","" + dishID).find(DishInfo.class)).get(0);//int型要加""转成string
            Log.d("CheckActivityDishInfo", dishInfo.getDishName() + " "  + dishInfo.getDishPrice() + " " + dishInfo.getDishScore());
            order newOrder = new order(dishInfo.getDishName(), dishInfo.getDishPrice(),
                    dishInfo.getDishScore(), orderfood.getId());
            orderFoodsList.add(newOrder);
        }
    }
    private void initRecycle(){
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        orderFoodAdapter adapter=new orderFoodAdapter(orderFoodsList, new orderFoodAdapter.ofaListener() {
            @Override
            public void ofa(int position) {
                orderFoodsList.remove(position);
                initRecycle();
            }
        });
        recyclerView.setAdapter(adapter);
    }
    private void initTimerPicker() {
        /*时间选择控件初始化*/
        //时间范围：[beginTime, endTime]
        String beginTime = "2019-1-1 00:00";
        String endTime = "2020-12-31 00:00";
        String currentTime = DateFormatUtils.long2Str(System.currentTimeMillis(), true);
        //默认初始时间为当前时间
        mTvSelectedTime.setText(currentTime);


        // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd HH:mm
        mTimerPicker = new CustomDatePicker(this, new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                Log.d("CheckActivityTimeStamp", Long.toString(timestamp));//时间戳
                mTimeStamp = timestamp;
                mTvSelectedTime.setText(DateFormatUtils.long2Str(timestamp, true));
            }
        }, beginTime, endTime, currentTime);
        // 允许点击屏幕或物理返回键关闭
        mTimerPicker.setCancelable(true);
        // 显示时和分
        mTimerPicker.setCanShowPreciseTime(true);
        // 允许循环滚动
        mTimerPicker.setScrollLoop(true);
        // 不允许滚动动画
        mTimerPicker.setCanShowAnim(false);
    }
}

