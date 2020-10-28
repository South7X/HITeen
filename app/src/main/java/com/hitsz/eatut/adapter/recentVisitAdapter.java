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
public class recentVisitAdapter  extends RecyclerView.Adapter<recentVisitAdapter.ViewHolder> {
    /*
     * 排行榜item Adapter
     * */
    private List<recentVisit> recentVisitList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView recentImg;
        TextView recentCanteen;
        TextView recentDish;
        View recentView;
        public ViewHolder(View view){
            super(view);
            recentView = view;
            recentImg = view.findViewById(R.id.main_recent_img);
            recentCanteen = view.findViewById(R.id.main_recent_canteen);
            recentDish = view.findViewById(R.id.main_recent_dish);
        }
    }
    public recentVisitAdapter(List<recentVisit> recentVisitList){
        this.recentVisitList = recentVisitList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_visit_item,parent,false);
        final ViewHolder holder=new ViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder,int position){
        //注意要转化为String格式，否则会bug
        recentVisit re = recentVisitList.get(position);
        holder.recentImg.setImageResource(re.getImageId());
        holder.recentCanteen.setText(re.getCanteen());
        holder.recentDish.setText(re.getDish());
    }
    @Override
    public int getItemCount(){
        return recentVisitList.size();
    }
}

