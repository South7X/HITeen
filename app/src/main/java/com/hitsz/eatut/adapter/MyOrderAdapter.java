package com.hitsz.eatut.adapter;

import android.content.ContentValues;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.hitsz.eatut.R;
import com.hitsz.eatut.database.DishInfo;
import com.hitsz.eatut.database.MyOrder;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;
public class MyOrderAdapter  extends RecyclerView.Adapter<MyOrderAdapter.ViewHolder> {
    /*
    * 我的订单item Adapter
    * 显示：订单号、菜品名、截止时间、准备状态以及取餐按钮；
    * */
    private List<MyOrderItem> morderFoodList;
    private moaListener listener;
    private int currentO;
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView myOrder;
        TextView endTime;
        TextView isPrepared;
        TextView myOrderName;
        TextView myOrderCost;
        Button getOrder;
        View orderfoodview;
        public ViewHolder(View view){
            super(view);
            orderfoodview=view;
            myOrder=view.findViewById(R.id.my_order);
            myOrderName=view.findViewById(R.id.myorder_name);
            myOrderCost = view.findViewById(R.id.my_order_cost);
            isPrepared=view.findViewById(R.id.isdone);
            getOrder=view.findViewById(R.id.getorder_btn);
            endTime = view.findViewById(R.id.end_time);
        }
    }
    public MyOrderAdapter(List<MyOrderItem> orderList,moaListener listener){
        morderFoodList=orderList;
        this.listener=listener;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        currentO=2;
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.my_order_item,parent,false);
        final ViewHolder holder=new ViewHolder(view);
        holder.getOrder.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int position=holder.getAdapterPosition();
                if(morderFoodList.get(position).getIsPrepared()){
                    MyOrderItem myOrderItem=morderFoodList.get(position);
                    ContentValues values = new ContentValues();
                    values.put("isPick", true);
                    LitePal.update(MyOrder.class, values, myOrderItem.getMyOrderID());
                    Toast.makeText(v.getContext(),"已确认取餐",Toast.LENGTH_SHORT).show();
                    if(listener!=null){
                        listener.moa(position);
                    }
                }else{
                    Toast.makeText(v.getContext(),"订单还没有准备好哦",Toast.LENGTH_SHORT).show();
                }

            }
        });
        return holder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder,int position){
        MyOrderItem food=morderFoodList.get(position);
        //订单号
        holder.myOrder.setText(String.valueOf(food.getOrderNo_III()));
        //取餐时间
        holder.endTime.setText(String.valueOf(food.getEndTime()));
        ArrayList<Integer> allDishID = food.getDishID_IV();
        StringBuilder nameValue = new StringBuilder();
        float total = 0;
        for(int i=0;i<allDishID.size();i++){
            List<DishInfo> temp=LitePal.where("id==?", "" + allDishID.get(i)).find(DishInfo.class);
            String name = temp.get(0).getDishName();
            float price = temp.get(0).getDishPrice();
            nameValue.append(name);
            nameValue.append("\n");//菜品名之间换行
            total += price;
        }
        //订单菜品内容和花费
        holder.myOrderName.setText(nameValue.toString());
        String costValue = "共" + allDishID.size() + "件商品，实付￥" + total;
        holder.myOrderCost.setText(costValue);
        //订单完成情况
        if(food.getIsPrepared()){
            if(food.isPick())
            {
                holder.isPrepared.setText("已取餐");
                holder.getOrder.setEnabled(false);
                holder.getOrder.setBackgroundResource(R.drawable.square_btn_grey);
            }else {
                holder.isPrepared.setText("已完成");
            }
        }
        else{
            holder.isPrepared.setText("准备中");
        }
    }
    @Override
    public int getItemCount(){
        return morderFoodList.size();
    }
    public interface moaListener{
        void moa(int position);
    }
}

