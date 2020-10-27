package com.hitsz.eatut.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hitsz.eatut.R;

import java.text.DecimalFormat;
import java.util.List;

/**
 * @author zhang
 */
public class RankingAdapter  extends RecyclerView.Adapter<RankingAdapter.ViewHolder> {
    /*
     * 排行榜item Adapter
     * */
    private List<rankingItem> rankingList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView rankingImg;
        TextView rankingNum;
        TextView rankingCanteen;
        TextView rankingDish;
        TextView rankingOrderTimes;
        TextView rankingScore;
        TextView rankingPrice;
        View rankingView;
        public ViewHolder(View view){
            super(view);
            rankingView = view;
            rankingImg = view.findViewById(R.id.ranking_img);
            rankingNum = view.findViewById(R.id.ranking_num);
            rankingCanteen = view.findViewById(R.id.ranking_canteen);
            rankingDish = view.findViewById(R.id.ranking_dish);
            rankingOrderTimes = view.findViewById(R.id.ranking_order_times);
            rankingScore = view.findViewById(R.id.ranking_score);
            rankingPrice = view.findViewById(R.id.ranking_price);
        }
    }
    public RankingAdapter(List<rankingItem> rankingList){
        this.rankingList = rankingList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.ranking_item,parent,false);
        final ViewHolder holder=new ViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder,int position){
        //注意要转化为String格式，否则会bug
        rankingItem ra = rankingList.get(position);
        holder.rankingImg.setImageResource(ra.getImageId());
        String num = "TOP" + String.valueOf(ra.getRankingNum());
        holder.rankingNum.setText(num);
        holder.rankingCanteen.setText(ra.getCanteen());
        holder.rankingDish.setText(ra.getDish());
        String orderTimes = String.valueOf(ra.getOrderTimes()) + "份";
        holder.rankingOrderTimes.setText(orderTimes);
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        String score = decimalFormat.format(ra.getDishScore()) + "分";
        holder.rankingScore.setText(score);
        String price ="￥" + decimalFormat.format(ra.getDishPrice());
        holder.rankingPrice.setText(price);
    }
    @Override
    public int getItemCount(){
        return rankingList.size();
    }
}

