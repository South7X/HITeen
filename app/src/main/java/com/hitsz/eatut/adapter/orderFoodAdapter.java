package com.hitsz.eatut.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hitsz.eatut.R;
import com.hitsz.eatut.database.orderFood;

import org.litepal.LitePal;

import java.util.List;

/**
 * @author zhang
 */
public class orderFoodAdapter  extends RecyclerView.Adapter<orderFoodAdapter.ViewHolder> {
    /*
    * 购物车item Adapter
    * */
    private List<order> morderFoodList;
    private ofaListener listener;
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView orderfoodName;
        TextView orderfoodPrice;
        Button cancelbtn;
        View orderfoodview;
        public ViewHolder(View view){
            super(view);
            orderfoodview=view;
            orderfoodName=view.findViewById(R.id.orderfood_name);
            orderfoodPrice=view.findViewById(R.id.orderfood_price);
            cancelbtn=view.findViewById(R.id.cancel_btn);
        }
    }
    public orderFoodAdapter(List<order> orderList,ofaListener listener){
        morderFoodList=orderList;
        this.listener=listener;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.orderfoods,parent,false);
        final ViewHolder holder=new ViewHolder(view);
        holder.cancelbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int position=holder.getAdapterPosition();
                order food=morderFoodList.get(position);
                LitePal.delete(orderFood.class,food.getId());
                if(listener!=null){
                    listener.ofa(position);
                }
            }
        });

        return holder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder,int position){
        order food=morderFoodList.get(position);
        holder.orderfoodName.setText(food.getName());
        String price = "￥" + String.valueOf(food.getDishPrice());
        holder.orderfoodPrice.setText(price);
    }
    @Override
    public int getItemCount(){
        return morderFoodList.size();
    }
    public interface ofaListener{
        void ofa(int position);
    }
}

