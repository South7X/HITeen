package com.hitsz.eatut.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hitsz.eatut.R;
import com.hitsz.eatut.database.Comment;
import com.hitsz.eatut.database.orderFood;
import com.hitsz.eatut.datepicker.DateFormatUtils;

import org.litepal.LitePal;

import java.util.List;

/**
 * @author zhang
 */
public class commentAdapter  extends RecyclerView.Adapter<commentAdapter.ViewHolder> {
    /*
     * 评论Adapter
     * */
    private List<commentItem> commentItemList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView userName;
        TextView postTime;
        TextView commentContent;
        View commentView;
        public ViewHolder(View view){
            super(view);
            commentView=view;
            userName=view.findViewById(R.id.comment_item_user);
            postTime=view.findViewById(R.id.comment_item_post_time);
            commentContent=view.findViewById(R.id.comment_content);
        }
    }
    public commentAdapter(List<commentItem> commentItemList){
        this.commentItemList = commentItemList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item,parent,false);
        final ViewHolder holder=new ViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder,int position){
        commentItem comment = commentItemList.get(position);
        holder.userName.setText(comment.getUserName());
        holder.postTime.setText(DateFormatUtils.long2Str(comment.getPostTime(), true));
        holder.commentContent.setText(comment.getCommentText());
    }
    @Override
    public int getItemCount(){
        return commentItemList.size();
    }
}

