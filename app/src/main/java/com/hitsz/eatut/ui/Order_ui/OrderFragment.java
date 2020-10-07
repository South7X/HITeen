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
import com.hitsz.eatut.managerActivities.AddWindowActivity;
import com.hitsz.eatut.managerActivities.OrderActivity;

import org.litepal.LitePal;
import org.litepal.exceptions.DataSupportException;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import static android.content.Context.MODE_PRIVATE;
import static com.hitsz.eatut.managerActivities.OrderActivity.haveOrderPrepared;


public class OrderFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<MyOrderItem> myOrderItemList= new ArrayList<>();
    private String currentPhone;
    public static void saveOrderToMyOrder(int i,String phone){
        List<orderFood> list= LitePal.findAll(orderFood.class);
        for(orderFood one:list){
            Log.d("myorderitem", one.getName());
            MyOrder myOrder=new MyOrder();
            myOrder.setDishID_III(one.getDishID_II());
            myOrder.setOrderNo_II(i);
            myOrder.setPhone(phone);
            myOrder.setPrepared(false);
            myOrder.save();
        }
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
        SharedPreferences pref2=getActivity().getSharedPreferences("CurrentPhone",MODE_PRIVATE);
        currentPhone=pref2.getString("Phone","Get_Phone_Fail");
        List<MyOrder> myOrderList=LitePal.where("phone==?",currentPhone).find(MyOrder.class);
        myOrderItemList.clear();
        for(MyOrder one:myOrderList){
            int id=one.getDishID_III();
            int orderNO=one.getOrderNo_II();
            int myOrderID=one.getId();
            List<DishInfo> temp= LitePal.where("id==?",String.valueOf(id)).find(DishInfo.class);
            Log.d("mytemp", temp.toString());
            String name=temp.get(0).getDishName();
            Log.d("single", name);
            boolean isprepared;
            isprepared= OrderActivity.haveOrderPrepared(myOrderID);
            MyOrderItem myOrderItem=new MyOrderItem(id,name,isprepared,orderNO,myOrderID);
            myOrderItemList.add(myOrderItem);

        }
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