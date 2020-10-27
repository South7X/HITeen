package com.hitsz.eatut.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.hitsz.eatut.R;
import com.hitsz.eatut.database.VoteInfo;
import com.hitsz.eatut.view.VotingView;
import org.litepal.LitePal;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.lang.String;
import java.util.Set;

public class ViewVoteAdapter extends RecyclerView.Adapter<ViewVoteAdapter.ViewHolder> {
    private List<vote> mVoteList;
    private int leftNum;
    private int rightNum;
    private String muserID;
    private ViewVoteAdapter.vvaListener listener;


    static class ViewHolder extends  RecyclerView.ViewHolder {
        TextView votename;
        Button btn_agree;
        Button btn_disagree;
        View voteView;
        VotingView votingView;
        TextView cantVot;
        LinearLayout btnBar;
        public ViewHolder(View view){
            super(view);
            voteView = view;
            votename = view.findViewById(R.id.votename_textview);
            votingView = view.findViewById(R.id.votingview);
            btn_agree = view.findViewById(R.id.btn_agree);
            btn_disagree = view.findViewById(R.id.btn_disagree);
            cantVot = view.findViewById(R.id.tv_voted);
            btnBar = view.findViewById(R.id.ll_vote);
        }
    }
    public ViewVoteAdapter(List<vote> voteList , String userID, vvaListener listener){
        mVoteList = voteList;
        muserID = userID;
        this.listener = listener;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vote_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);

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
                    voteInfo.AddId(muserID);
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
                    voteInfo.AddId(muserID);
                    voteInfo.save();
                }
                holder.votingView.setNum(leftNum, rightNum);
                holder.votingView.setClickMode(2);
                holder.btn_agree.setEnabled(false);
                holder.btn_disagree.setEnabled(false);
                Toast.makeText(v.getContext(),"投票成功",Toast.LENGTH_SHORT).show();
                if(listener!=null){
                    listener.vva(position);
                }
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        vote vot = mVoteList.get(position);
        holder.votename.setText("  " + vot.getName() + "  ");
        holder.votingView.setNum(vot.getAgreeNum(), vot.getDisagreeNum());
//        String[] votedID = vot.getVotedID();
//        Set<String> set = new HashSet<>(Arrays.asList(votedID));
//        boolean flag = set.contains(muserID);
        boolean flag = vot.getIsVoted();
        if(flag){
            holder.cantVot.setVisibility(View.VISIBLE);
            holder.cantVot.setText("已投票");
            holder.btnBar.setVisibility(View.INVISIBLE);
            holder.btn_agree.setEnabled(false);
            holder.btn_disagree.setEnabled(false);
            holder.votingView.setClickMode(3);
        }
        else{
            holder.cantVot.setVisibility(View.INVISIBLE);
            holder.btnBar.setVisibility(View.VISIBLE);
            holder.btn_agree.setEnabled(true);
            holder.btn_disagree.setEnabled(true);
            holder.votingView.setClickMode(0);
        }
    }

    @Override
    public int getItemCount(){
        return mVoteList.size();
    }

    public interface vvaListener{
        void vva(int position);
    }

}

