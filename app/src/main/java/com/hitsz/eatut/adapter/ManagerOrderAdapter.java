package com.hitsz.eatut.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.hitsz.eatut.R;
import com.hitsz.eatut.database.ManagerOrderInfo;
import com.hitsz.eatut.database.orderFood;
import org.litepal.LitePal;
import java.util.List;
/**
 * @author Lily
 */
public class ManagerOrderAdapter extends RecyclerView.Adapter<ManagerOrderAdapter.ViewHolder> {
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
        TextView orderInfo; //菜品名称
        View orderView;
        Button button_finish;
        public ViewHolder(View view){
            super(view);
            orderView=view;                                      //整个recyclerview单元
            orderInfo=view.findViewById(R.id.ordername_textview);    //内部textview部件
            orderNo=view.findViewById(R.id.orderno_textview);
            button_finish=view.findViewById(R.id.order_finish);
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
        holder.button_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                OrderItem orderItem=mOrderList.get(position);
                LitePal.delete(ManagerOrderInfo.class,orderItem.getId());
                //   List<ManagerOrderInfo> deleteList=LitePal.where("orderno=?","String.valueOf(orderItem.getOrderno())").find(ManagerOrderInfo.class);
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
        holder.orderNo.setText(toString().valueOf(orderInfo.getOrderno()));
        holder.orderInfo.setText(orderInfo.getOrdername());
    }

    public int getItemCount(){
        return mOrderList.size();
    }
    public interface ofaListener{
        void ofa(int position);
    }
}
