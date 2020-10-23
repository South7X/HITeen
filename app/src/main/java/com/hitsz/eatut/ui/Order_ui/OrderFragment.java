package com.hitsz.eatut.ui.Order_ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.hitsz.eatut.R;
import com.hitsz.eatut.adapter.MyOrderAdapter;
import com.hitsz.eatut.adapter.MyOrderItem;
import com.hitsz.eatut.database.DishInfo;
import com.hitsz.eatut.database.MyOrder;
import com.hitsz.eatut.database.orderFood;
import com.hitsz.eatut.datepicker.DateFormatUtils;
import com.hitsz.eatut.managerActivities.OrderActivity;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class OrderFragment extends Fragment {
    /*
    * 我的订单页面
    * 功能：显示所有订单（订单号、菜品、截止时间、准备状态）
    * */
    private RecyclerView recyclerView;
    private List<MyOrderItem> myOrderItemList= new ArrayList<>();
    public static void saveOrderToMyOrder(int i, long timeStamp){
        //将购物车中提交的orderFood加入MyOrder database
        List<orderFood> list= LitePal.findAll(orderFood.class);
        ArrayList<Integer> allDishID = new ArrayList<>();// 存放该订单的菜品
        for(orderFood one:list){
            int dishID = one.getDishID_II();
            allDishID.add(dishID);
        }
        MyOrder myOrder=new MyOrder();
        myOrder.setDishID_III(allDishID);
        myOrder.setOrderNo_II(i);
        myOrder.setUserID(list.get(0).getUserID());
        myOrder.setPrepared(false);
        myOrder.setEndTime(timeStamp);
        myOrder.setPick(false);
        myOrder.save();
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_order, container, false);
        recyclerView=root.findViewById(R.id.myorder_recycle);
        initMyOrder();
        initRecycle();
        return root;

    }
    private void initMyOrder(){
        SharedPreferences pref2 = getActivity().getSharedPreferences("currentID",MODE_PRIVATE);
        int userID = pref2.getInt("userID", -1);
        List<MyOrder> myOrderList=LitePal.where("userID==?", "" + userID).find(MyOrder.class);
        myOrderItemList.clear();
        for(MyOrder one:myOrderList){
            ArrayList<Integer> allDishID =one.getDishID_III();
            int orderNO=one.getOrderNo_II();
            int myOrderID=one.getId();
            boolean isprepared = OrderActivity.haveOrderPrepared(myOrderID);
            boolean isPick = one.isPick();
            long endTimeStamp = one.getEndTime();
            String endTime = DateFormatUtils.long2Str(endTimeStamp, true);
            MyOrderItem myOrderItem = new MyOrderItem(allDishID, isprepared, orderNO, myOrderID, endTime, isPick);
            myOrderItemList.add(myOrderItem);
        }
        //对myOrderItemList做个排序：已取餐的放在下面；按照订单号排序
        Collections.sort(myOrderItemList, new Comparator<MyOrderItem>() {
                    @Override
                    public int compare(MyOrderItem m1, MyOrderItem m2) {
                        boolean isPick1 = m1.isPick();
                        boolean isPick2 = m2.isPick();
                        int orderNo1 = m1.getOrderNo_III();
                        int orderNo2 = m2. getOrderNo_III();
                        if(isPick1 && !isPick2)
                            return 1;
                        else if(!isPick1 && isPick2)
                            return -1;
                        else{
                            return orderNo2 - orderNo1;
                        }
                    }
                });
    }
    private void initRecycle(){
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        MyOrderAdapter adapter=new MyOrderAdapter(myOrderItemList,new MyOrderAdapter.moaListener() {
            @Override
            public void moa(int position) {
                myOrderItemList.clear();
                initMyOrder();
                initRecycle();
            }
            });
        recyclerView.setAdapter(adapter);
    }

}