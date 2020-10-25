package com.hitsz.eatut.adapter;

import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.hitsz.eatut.R;
import com.hitsz.eatut.database.DishInfo;
import com.hitsz.eatut.database.ManagerOrderInfo;
import com.hitsz.eatut.database.MyOrder;

import org.litepal.LitePal;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
/**
 * @author Lily
 */
public class ManagerOrderAdapter extends RecyclerView.Adapter<ManagerOrderAdapter.ViewHolder> {
    /*
    管理者订单界面 Adapter
     */
    private List<OrderItem> mOrderList;
    private ofaListener listener;

//    public static int haveOrderPrepared(int no){
//        if(prepared)
//            return prepared_no;
//        else
//            return prepared_no;
//    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView orderNo;   //订餐编号
        TextView endTime;
        TextView orderName;
        TextView orderNum;
        Button finishButton;
        View orderView;
        public ViewHolder(View view){
            super(view);
            orderView=view;                                      //整个recyclerview单元
            orderNo = view.findViewById(R.id.manager_my_order);
            endTime = view.findViewById(R.id.manager_end_time);
            orderName = view.findViewById(R.id.manager_my_order_name);
            orderNum = view.findViewById(R.id.manager_dish_num);
            finishButton = view.findViewById(R.id.manager_done_btn);
        }
    }
    public ManagerOrderAdapter(List<OrderItem>orderList,ofaListener listener){
        mOrderList=orderList;
        this.listener=listener;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_item,parent,false);
        final ViewHolder holder=new ViewHolder(view);
        holder.finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                OrderItem orderItem = mOrderList.get(position);
                ContentValues values = new ContentValues();
                values.put("isPrepared", true);
                LitePal.update(MyOrder.class, values, orderItem.getMyOrderID());
                if(listener!=null){
                    listener.ofa(position);
                }
            }
        });
        return holder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder,int position){
        OrderItem orderInfo=mOrderList.get(position);  //position为list编号，从0开始
        holder.orderNo.setText(String.valueOf(orderInfo.getOrderNo_III()));
        holder.endTime.setText(String.valueOf(orderInfo.getEndTime()));
        ArrayList<Integer> allDishID = orderInfo.getDishID_IV();
        StringBuilder nameValue = new StringBuilder();
        for(int i=0;i<allDishID.size();i++){
            DishInfo temp=LitePal.where("id==?", "" + allDishID.get(i)).find(DishInfo.class).get(0);
            String name = temp.getBelongToCanteen() + "-" + temp.getBelongToWindow() + "-" + temp.getDishName();
            nameValue.append(name);
            nameValue.append("\n");//菜品名之间换行
        }
        holder.orderName.setText(nameValue.toString());
        String num = "共" + allDishID.size() + "件商品";
        holder.orderNum.setText(num);
    }

    public int getItemCount(){
        return mOrderList.size();
    }
    public interface ofaListener{
        void ofa(int position);
    }
}
