package com.hitsz.eatut.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hitsz.eatut.R;

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
        TextView rankingNum;
        TextView rankingCanteen;
        TextView rankingDish;
        TextView rankingOrderTimes;
        View rankingView;
        public ViewHolder(View view){
            super(view);
            rankingView = view;
            rankingNum = view.findViewById(R.id.ranking_num);
            rankingCanteen = view.findViewById(R.id.ranking_canteen);
            rankingDish = view.findViewById(R.id.ranking_dish);
            rankingOrderTimes = view.findViewById(R.id.ranking_order_times);
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
        holder.rankingNum.setText(String.valueOf(ra.getRankingNum()));
        holder.rankingCanteen.setText(ra.getCanteen());
        holder.rankingDish.setText(ra.getDish());
        holder.rankingOrderTimes.setText(String.valueOf(ra.getOrderTimes()));
    }
    @Override
    public int getItemCount(){
        return rankingList.size();
    }
}

