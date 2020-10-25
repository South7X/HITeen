package com.hitsz.eatut.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.hitsz.eatut.R;
import com.hitsz.eatut.database.VoteInfo;
import com.hitsz.eatut.view.VotingView;
import org.litepal.LitePal;

import java.util.List;
import java.lang.String;

public class ViewVoteAdapter extends RecyclerView.Adapter<ViewVoteAdapter.ViewHolder> {
    private List<vote> mVoteList;
    private int leftNum;
    private int rightNum;

    static class ViewHolder extends  RecyclerView.ViewHolder {
        TextView votename;
        Button btn_agree;
        Button btn_disagree;
        View voteView;
        VotingView votingView;
        public ViewHolder(View view){
            super(view);
            voteView = view;
            votename = view.findViewById(R.id.votename_textview);
            votingView = view.findViewById(R.id.votingview);
            btn_agree = view.findViewById(R.id.btn_agree);
            btn_disagree = view.findViewById(R.id.btn_disagree);
        }
    }
    public ViewVoteAdapter(List<vote> voteList){
        mVoteList = voteList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vote_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.votingView.setClickMode(0);
//        holder.voteView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int position = holder.getAdapterPosition();
//                vote vot = mVoteList.get(position);
//            }
//        });
        holder.btn_agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                vote vot = mVoteList.get(position);
                String name = vot.getName();
                leftNum = vot.getAgreeNum() + 1;
                rightNum = vot.getDisagreeNum();
                List<VoteInfo> voteInfos = LitePal.where("voteName like ?", name).find(VoteInfo.class);
                for (VoteInfo voteInfo: voteInfos){
                    voteInfo.setVoteAgree(leftNum);
                    voteInfo.save();
                }
                holder.votingView.setNum(leftNum, rightNum);
                holder.votingView.setClickMode(1);
                holder.btn_agree.setEnabled(false);
                holder.btn_disagree.setEnabled(false);
                Toast.makeText(v.getContext(),"投票成功",Toast.LENGTH_SHORT).show();
            }
        });
        holder.btn_disagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                vote vot = mVoteList.get(position);
                String name = vot.getName();
                leftNum = vot.getAgreeNum();
                rightNum = vot.getDisagreeNum() + 1;
                List<VoteInfo> voteInfos = LitePal.where("voteName like ?", name).find(VoteInfo.class);
                for (VoteInfo voteInfo: voteInfos){
                    voteInfo.setVoteDisagree(rightNum);
                    voteInfo.save();
                }
                holder.votingView.setNum(leftNum, rightNum);
                holder.votingView.setClickMode(2);
                holder.btn_agree.setEnabled(false);
                holder.btn_disagree.setEnabled(false);
                Toast.makeText(v.getContext(),"投票成功",Toast.LENGTH_SHORT).show();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        vote vot = mVoteList.get(position);
        holder.votename.setText(vot.getName());
        holder.votingView.setNum(vot.getAgreeNum(), vot.getDisagreeNum());
    }

    @Override
    public int getItemCount(){
        return mVoteList.size();
    }


}

