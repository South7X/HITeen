package com.hitsz.eatut.adapter;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.hitsz.eatut.R;
import com.hitsz.eatut.database.MyOrder;
import com.hitsz.eatut.database.orderFood;

import org.litepal.LitePal;

import java.util.List;
public class MyOrderAdapter  extends RecyclerView.Adapter<MyOrderAdapter.ViewHolder> {
    private List<MyOrderItem> morderFoodList;
    private moaListener listener;
    private int currentO;
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView myOrder;
        TextView myOrderName;
        TextView isPrepared;
        Button getOrder;
        View orderfoodview;
        public ViewHolder(View view){
            super(view);
            orderfoodview=view;
            myOrder=view.findViewById(R.id.my_order);
            myOrderName=view.findViewById(R.id.myorder_name);
            isPrepared=view.findViewById(R.id.isdone);
            getOrder=view.findViewById(R.id.getorder_btn);
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
                    LitePal.delete(MyOrder.class,myOrderItem.getMyOrderID());
                    if(listener!=null){
                        listener.moa(position);
                    }
                }else{
                    Toast.makeText(v.getContext(),"订单还没有准备好哦！",Toast.LENGTH_SHORT).show();
                }

            }
        });
        return holder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder,int position){
        MyOrderItem food=morderFoodList.get(position);
        holder.myOrder.setText(String.valueOf(food.getOrderNo_III()));
        holder.myOrderName.setText(String.valueOf(food.getName()));
        if(food.getIsPrepared()){
            holder.isPrepared.setText("已经准备好啦！");
            holder.getOrder.setBackgroundColor(Color.parseColor("#caffc107"));
        }
        else{
            holder.isPrepared.setText("还没准备好！");
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

