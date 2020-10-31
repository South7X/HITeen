package com.hitsz.eatut;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
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

import java.text.DecimalFormat;
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
    private TextView orderTotal;//订单总价
    private float total;

    //时间选择
    private TextView mTvSelectedTime;
    private CustomDatePicker mTimerPicker;
    private int i;
    private long mTimeStamp;//时间戳
    private Boolean wrongTimeFlag = false;
    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_check);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        findView();
        initOrder();
        initRecycle();
        initTimerPicker();
    }

    public void onClick(View v){
        switch(v.getId()){
            case R.id.confirm_btn:
                initOrder();
                if(wrongTimeFlag){
                    DialogWrongTime();
                }else if(orderFoodsList.isEmpty())
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
        orderTotal=findViewById(R.id.orderno_total);

        findViewById(R.id.ll_time).setOnClickListener(this);
        mTvSelectedTime = findViewById(R.id.tv_selected_time);

        SharedPreferences pref=this.getSharedPreferences("OrderCount",MODE_PRIVATE);
        SharedPreferences.Editor editor=pref.edit();
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
    private void DialogWrongTime(){
        builder.setTitle("无法提交");
        builder.setMessage("当前不在合法订餐时间范围内；请于当日0:00~19:00进行订餐！");
        builder.setPositiveButton("好吧", new DialogInterface.OnClickListener() {
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
        total = 0;
        for (orderFood orderfood:orderFoods){
            int dishID = orderfood.getDishID_II();
            Log.d("CheckActivityDishID", Integer.toString(dishID));
            DishInfo dishInfo = (LitePal.where("id = ?","" + dishID).find(DishInfo.class)).get(0);//int型要加""转成string
            Log.d("CheckActivityDishInfo", dishInfo.getDishName() + " "  + dishInfo.getDishPrice() + " " + dishInfo.getDishScore());
            total += dishInfo.getDishPrice();
            order newOrder = new order(dishInfo.getDishName(), dishInfo.getDishPrice(),
                    dishInfo.getDishScore(), orderfood.getId());
            orderFoodsList.add(newOrder);
        }
        //订单总价和订单内容直接关联，所以赋值放在这。
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        String totalValue = "￥" + decimalFormat.format(total);
        orderTotal.setText(totalValue);
    }
    private void initRecycle(){
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        orderFoodAdapter adapter=new orderFoodAdapter(orderFoodsList, new orderFoodAdapter.ofaListener() {
            @Override
            public void ofa(int position) {
                orderFoodsList.remove(position);
                initOrder();
                initRecycle();
            }
        });
        recyclerView.setAdapter(adapter);
    }
    private long[] endTimePeriod(){
        long hourPeriod = 3600000L;//一个小时的毫秒数
        long[] endTimeStamps = new long[6];
        //存放一天中合法时间段的节点时间戳：[6, 8] [11, 13] [17, 19]
        endTimeStamps[0] = DateUtils.getDayTime(6);
        endTimeStamps[1] = DateUtils.getDayTime(8);
        endTimeStamps[2] = DateUtils.getDayTime(11);
        endTimeStamps[3] = DateUtils.getDayTime(13);
        endTimeStamps[4] = DateUtils.getDayTime(17);
        endTimeStamps[5] = DateUtils.getDayTime(19);
        return endTimeStamps;

    }
    private void initTimerPicker() {
        /*时间选择控件初始化*/
        //控件可选的时间范围：传入的[beginTime, endTime]
        /*
        用户可选定时间段：0:00 - 8:00 早餐，8:00 - 13:00午餐，13:00 - 19:00晚餐；
        用户可订餐时间段：0:00 - 19:00;
        [0:00, 8:00]可定早餐，[8:00, 13:00]可定午餐，[13:00, 19:00]可定晚餐；且显然，最早取餐时间要在可订餐时间段内。
         */

        long[] endTimeStamps = endTimePeriod();
        long currentTime = System.currentTimeMillis();
        long beginTime = currentTime;
        long endTime = currentTime;
        //重要：因为mTVSelectedTime显示的currentTime会被传入，所以必须对其和较小订餐时间点进行比较
        if(currentTime<= endTimeStamps[1]){
            //currentTime < 8:00
            beginTime = endTimeStamps[0];
            endTime = endTimeStamps[1];
            currentTime = currentTime<beginTime?beginTime:currentTime;
        }else if(currentTime<= endTimeStamps[3]){
            //currentTime < 13:00
            beginTime = endTimeStamps[2];
            endTime = endTimeStamps[3];
            currentTime = currentTime<beginTime?beginTime:currentTime;
        }else if(currentTime<= endTimeStamps[5]){
            //currentTime < 19:00
            beginTime = endTimeStamps[4];
            endTime = endTimeStamps[5];
            currentTime = currentTime<beginTime?beginTime:currentTime;
        }
        if(beginTime == endTime){
            //在非法点餐时间, wrongTimeFlag置false，以触发DialogWrongTime弹窗
            String warning = "点餐开放时间为当日0:00-19:00";
            mTvSelectedTime.setText(warning);
            wrongTimeFlag = true;
        }
        //在合法点餐时间
        else{
            mTvSelectedTime.setText(DateFormatUtils.long2Str(currentTime, true));
            wrongTimeFlag = false;
        }
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
//    private void initTimerPicker() {
//        /*时间选择控件初始化*/
//        //时间范围：[beginTime, endTime]
//        String beginTime = "2019-1-1 00:00";
//        String endTime = "2020-12-31 00:00";
//        String currentTime = DateFormatUtils.long2Str(System.currentTimeMillis(), true);
//        //默认初始时间为当前时间
//        mTvSelectedTime.setText(currentTime);
//
//
//        // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd HH:mm
//        mTimerPicker = new CustomDatePicker(this, new CustomDatePicker.Callback() {
//            @Override
//            public void onTimeSelected(long timestamp) {
//                Log.d("CheckActivityTimeStamp", Long.toString(timestamp));//时间戳
//                mTimeStamp = timestamp;
//                mTvSelectedTime.setText(DateFormatUtils.long2Str(timestamp, true));
//            }
//        }, beginTime, endTime, currentTime);
//        // 允许点击屏幕或物理返回键关闭
//        mTimerPicker.setCancelable(true);
//        // 显示时和分
//        mTimerPicker.setCanShowPreciseTime(true);
//        // 允许循环滚动
//        mTimerPicker.setScrollLoop(true);
//        // 不允许滚动动画
//        mTimerPicker.setCanShowAnim(false);
//    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

