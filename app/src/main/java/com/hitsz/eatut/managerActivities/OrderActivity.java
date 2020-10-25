package com.hitsz.eatut.managerActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.ContentValues;
import android.os.Bundle;
import android.util.Log;

import com.hitsz.eatut.R;
import com.hitsz.eatut.adapter.ManagerOrderAdapter;
import com.hitsz.eatut.adapter.OrderItem;
import com.hitsz.eatut.database.DishInfo;
import com.hitsz.eatut.database.ManagerOrderInfo;
import com.hitsz.eatut.database.MyOrder;
import com.hitsz.eatut.database.UserInfo;
import com.hitsz.eatut.database.orderFood;
import com.hitsz.eatut.datepicker.DateFormatUtils;
import com.hitsz.eatut.ui.Main_ui.MainFragment;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
/**
 * @author Lily
 */

public class OrderActivity extends AppCompatActivity {
    /*
    管理者查看订单界面
    判断菜品是否已完成，返回给用户信息
    功能：显示菜名，截止时间，已完成按钮
     */
//    public static boolean haveOrderPrepared(int id){
//        boolean isprepared=true;
//        List<ManagerOrderInfo> list= LitePal.findAll(ManagerOrderInfo.class);
//        for(ManagerOrderInfo per:list){
//            if(per.getId()==id){
//                isprepared=false;
//            }
//        }
//        if(isprepared)
//        {
//            //若已经完成订单，修改MyOrder中的isPrepared
//            ContentValues values = new ContentValues();
//            values.put("isPrepared", true);
//            LitePal.update(MyOrder.class, values, id);
//        }
//        return isprepared;
//    }

//    public static void saveOrderToManager(int OrderNo){
//        //将购物车中提交的orderFood加入ManagerOrderInfo database
//        List<orderFood> orderFoodsList=LitePal.findAll(orderFood.class);
//        for(orderFood per:orderFoodsList){
//            int dishID = per.getDishID_II();
//            int userID = per.getUserID();
//            Log.d("OrderActivityDishID", Integer.toString(dishID));
//            DishInfo dishInfo = (LitePal.where("id = ?", "" + dishID).find(DishInfo.class)).get(0);//int型要加""转成string
//            Log.d("OrderActivityDishInfo", dishInfo.getDishName() + " "  + dishInfo.getDishPrice() + " " + dishInfo.getDishScore());
//            Log.d("OrderActivityUserID", Integer.toString(userID));
//            UserInfo userInfo = (LitePal.where("id = ?", "" + userID).find(UserInfo.class)).get(0);
//            Log.d("OrderActivityUserInfo", userInfo.getTelephoneNumber());
//            ManagerOrderInfo info  = new ManagerOrderInfo();
//            info.setDishPrice(dishInfo.getDishPrice());
//            info.setDishScore(dishInfo.getDishScore());
//            info.setName(dishInfo.getDishName());
//            info.setPhone(userInfo.getTelephoneNumber());
//            info.setOrderNo(OrderNo);
////            Log.d("manager", per.getName());
////            ManagerOrderInfo info=new ManagerOrderInfo();
////            info.setDishPrice(per.getDishPrice());
////            info.setDishScore(per.getDishScore());
////            info.setName(per.getName());
////            info.setPhone(phone);
////            info.setOrderNo(OrderNo);
//            info.save();
//        }
//    }



    private RecyclerView recyclerView;
    private List<OrderItem> orderInfoList=new ArrayList<>();
    private ManagerOrderAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        findview();
        initOrder();
        initRecycle();
    }
    protected void findview(){
        recyclerView=(RecyclerView)findViewById(R.id.recycler_order);

    }
    //通过recyclerview显示litepal中内容
    private void initOrder(){
        /*
        **查询 MyOrder里面的菜品
         */
        List<MyOrder> MyOrderInfo = LitePal.where("isPrepared = ?", ""+0).find(MyOrder.class);
        orderInfoList.clear();
        for(MyOrder OrderInfo : MyOrderInfo){
            long endTimeStamp = OrderInfo.getEndTime();
            String endTime = DateFormatUtils.long2Str(endTimeStamp, true);
            OrderItem orderItem=new OrderItem(OrderInfo.getDishID_III(), OrderInfo.isPrepared(), OrderInfo.getOrderNo_II(), OrderInfo.getId(), endTime,  OrderInfo.getCost());
            orderInfoList.add(orderItem);
        }
        Collections.sort(orderInfoList, new Comparator<OrderItem>(){
            @Override
            public int compare(OrderItem o1, OrderItem o2){
                long endTime1 = DateFormatUtils.str2Long(o1.getEndTime(), true);
                long endTime2 = DateFormatUtils.str2Long(o2.getEndTime(), true);
                return (int)(endTime1 - endTime2);
            }
        });
    }

    private void initRecycle(){
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new ManagerOrderAdapter(orderInfoList, new ManagerOrderAdapter.ofaListener() {
            @Override
            public void ofa(int position) {
                orderInfoList.remove(position);
                initOrder();
                initRecycle();
            }
        });
        recyclerView.setAdapter(adapter);
    }
}
//TODO:连接队列

//    private Button button_neworder=null;
//    private Button button_deleorder=null;
//    private TextView tv_order=null;
//    private boolean IsReading=false;
//    Queue<Integer> queue = new Queue<>();
//    private int index=0;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_order);
//
//        tv_order = (TextView)findViewById(R.id.text_order);
//        button_neworder=(Button)findViewById(R.id.button_test_neworder);
//        button_deleorder=(Button)findViewById(R.id.button_test_deleorder);
//        button_neworder.setOnClickListener(listener);
//        button_deleorder.setOnClickListener(listener);
//
//        for(int i=0;i<=99;i++){
//            queue.offer(i);//初始队列1~100
//        }
//
//    }
//
//    public void RefreshData()
//    {
//        if(IsReading)
//        {
//            String s=(new Integer(queue.display(index))).toString();
//            tv_order.setText(s);
//        }
//    }
//
//    OnClickListener listener=new View.OnClickListener(){
//        @Override
//        public void onClick(View v)
//        {
//            switch(v.getId())
//            {
//                case R.id.button_test_neworder:
//                    IsReading=true;
//                    index = index + 1;
//
//                    RefreshData();
//                    break;
//                case R.id.button_test_deleorder:
//                    //TODO:
//                    break;
//                default:
//                    break;
//            }
//        }
//    };


