package com.hitsz.eatut.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.hitsz.eatut.BaseClass;
import com.hitsz.eatut.R;
import com.hitsz.eatut.database.DishInfo;
import com.hitsz.eatut.database.orderFood;
//import com.hitsz.eatut.adapter.order;
import com.hitsz.eatut.BaseClass.Queue;

import org.litepal.LitePal;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;
import java.lang.String;

//食物适配器

/**
 * @author zhang
 */
public class DishAdapter extends RecyclerView.Adapter<DishAdapter.ViewHolder> {
    private List<dish> mFoodList;
    private List<order> morderFoodList;
    private AlertDialog.Builder builder;
    private int chedkedItem = 0;
    private daListener listener;
    //创建队列
    Queue queue = new Queue();
    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView foodImage;
        TextView foodName;
        TextView foodPrice;
        TextView foodScore;
        Button orderBtn;
        Button commentBtn;
        View foodview;
        public ViewHolder(View view){
            super(view);
            foodview=view;
            foodImage=view.findViewById(R.id.food_image);
            foodName=view.findViewById(R.id.food_name);
            foodPrice=view.findViewById(R.id.food_price);
            foodScore=view.findViewById(R.id.food_score);
            orderBtn=view.findViewById(R.id.order_btn);
            commentBtn=view.findViewById(R.id.cmt_btn);
        }
    }
    public DishAdapter(List<dish> foodsList,daListener listener){
        mFoodList=foodsList;
        this.listener=listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.dish,parent,false);
        final Calendar calendar=Calendar.getInstance();
        final ViewHolder holder=new ViewHolder(view);
        holder.foodview.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int position=holder.getAdapterPosition();
                dish food=mFoodList.get(position);
                //Toast.makeText(v.getContext(),"You click view"+food.getName(),Toast.LENGTH_SHORT).show();
            }
        });
        holder.foodImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int position=holder.getAdapterPosition();
                dish food=mFoodList.get(position);
                //Toast.makeText(v.getContext(),"You click image"+food.getName(),Toast.LENGTH_SHORT).show();
            }
        });
        holder.commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                dish food=mFoodList.get(position);
                final String name = food.getName();
                builder=new AlertDialog.Builder(v.getContext());
                builder.setTitle("点评");
                builder.setCancelable(true);
                final String[] scores = {"很糟糕( ｰ̀дｰ́ )", "一般般(๑˙ー˙๑)", "还行吧|ω・）", "挺好(๑Ő௰Ő๑)", "非常棒(｡･ω･｡)ﾉ♡"};

                builder.setSingleChoiceItems(scores, chedkedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        chedkedItem = which;
                    }
                });
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) chedkedItem = 0;
                        dialog.dismiss();List<DishInfo> dishInfos = LitePal.where("dishName like ?", name).find(DishInfo.class);
                        for (DishInfo dishInfo: dishInfos){
                            BaseClass.addDishScore(dishInfo.getBelongToCanteen(),
                                    dishInfo.getBelongToWindow(), dishInfo.getDishName(), chedkedItem + 1);
                        }
                        if(listener!=null){
                            listener.da();
                        }
                    }
                });
                builder.show();

            }
        });
        holder.orderBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int position=holder.getAdapterPosition();
                dish food=mFoodList.get(position);
                //入队操作
                queue.offer(food);

//                int hour=calendar.get(Calendar.HOUR_OF_DAY);
//                int minute=calendar.get(Calendar.MINUTE);
//                int second=calendar.get(Calendar.SECOND);
                orderFood one=new orderFood();
                one.setName(food.getName());
                one.setDishPrice(food.getDishPrice());
                one.setDishScore(food.getDishScore());
                one.setDishID_II(food.getDishID_I());
                one.save();
                Toast.makeText(v.getContext(),"已添加到订单",Toast.LENGTH_SHORT).show();
            }
        });
        return holder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder,int position){
        dish food=mFoodList.get(position);
        holder.foodImage.setImageResource(food.getImageId());
        holder.foodName.setText(food.getName());
        holder.foodPrice.setText(String.valueOf(food.getDishPrice()));
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        String score = decimalFormat.format(food.getDishScore());
        holder.foodScore.setText(score);
    }
    @Override
    public int getItemCount(){
        return mFoodList.size();
    }
    public interface daListener{
        void da();
    }
}
