package com.hitsz.eatut.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.hitsz.eatut.R;
import com.hitsz.eatut.database.VoteInfo;
import com.hitsz.eatut.database.orderFood;
import com.hitsz.eatut.managerActivities.VotingResultActivity;
import com.hitsz.eatut.view.VotingView;
import org.litepal.LitePal;

import java.util.List;
import java.lang.String;

public class VotingResultsAdapter extends RecyclerView.Adapter<VotingResultsAdapter.ViewHolder> {
    private List<vote> mVoteList;
    public boolean isChanged;
    private VotingResultsAdapter.vraListener listener;


    static class ViewHolder extends  RecyclerView.ViewHolder {
        TextView votename;
        View voteView;
        VotingView votingView;
        public ViewHolder(View view){
            super(view);
            voteView = view;
            votename = view.findViewById(R.id.mvotename_textview);
            votingView = view.findViewById(R.id.mvotingview);
        }
    }
    public VotingResultsAdapter(List<vote> voteList, vraListener listener){
        this.listener = listener;
        mVoteList = voteList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mvote_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.votingView.setClickMode(3);
//        int position = holder.getAdapterPosition();
//        vote vot = mVoteList.get(position);
//        holder.votingView.setNum(vot.getAgreeNum(), vot.getDisagreeNum());

        holder.votename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                vote vot = mVoteList.get(position);
                String name = vot.getName();
                List<VoteInfo> voteInfos = LitePal.where("voteName like ?", name).find(VoteInfo.class);
                for (VoteInfo voteInfo: voteInfos){
                    voteInfo.delete();
                }
                Toast.makeText(v.getContext(),"删除成功",Toast.LENGTH_SHORT).show();
                if(listener!=null){
                    listener.vra(position);
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        vote vot = mVoteList.get(position);
        String delString = " " +vot.getName() + "  [删除] ";
        holder.votename.setText(delString);
        holder.votingView.setNum(vot.getAgreeNum(), vot.getDisagreeNum());

    }

    @Override
    public int getItemCount(){
        return mVoteList.size();
    }

    public interface vraListener{
        void vra(int position);
    }

}

