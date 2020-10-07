package com.hitsz.eatut.adapter;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hitsz.eatut.R;
import com.hitsz.eatut.database.FeedInfo;

import org.litepal.LitePal;

import java.util.List;

/**
 * @author Lily
 */
public class FeedbackAdapter extends RecyclerView.Adapter <FeedbackAdapter.ViewHolder>{
    private List<FeedbackItem> mFeedbackList;
    private ofaListener listener;
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView feedInfo;
        View feedView;
        Button button_delete;
        public ViewHolder(View view){
            super(view);
            feedView=view;                                      //整个recyclerview单元
            feedInfo=view.findViewById(R.id.feedinfo_textview);    //内部textview部件
            button_delete=view.findViewById(R.id.feed_delete);
        }
    }
    public FeedbackAdapter(List<FeedbackItem> feedList,ofaListener listener){
        mFeedbackList=feedList;
        this.listener=listener;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.feedback_item,parent,false);
        final ViewHolder holder=new ViewHolder(view);
        holder.feedView.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void  onClick(View v){
                int position=holder.getAdapterPosition();
                FeedbackItem feedInfo=mFeedbackList.get(position);
                Toast.makeText(v.getContext(),"You click view"+feedInfo.getFeedInfo(),Toast.LENGTH_SHORT).show();
            }
        });
        holder.button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                FeedbackItem feedbackItem=mFeedbackList.get(position);
                LitePal.delete(FeedInfo.class,feedbackItem.getUserID());
                if(listener!=null){
                    listener.ofa(position);
                }
            }
        });
        return holder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder,int position){
        FeedbackItem feedInfo=mFeedbackList.get(position);  //position为list编号，从0开始
        holder.feedInfo.setText(feedInfo.getFeedInfo());
    }
    @Override
    public int getItemCount(){
        return mFeedbackList.size();
    }
    public interface ofaListener{
        void ofa(int position);
    }

}