package com.hitsz.eatut;

import android.os.Bundle;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hitsz.eatut.R;
import com.hitsz.eatut.adapter.ViewVoteAdapter;
import com.hitsz.eatut.adapter.VotingResultsAdapter;
import com.hitsz.eatut.adapter.vote;
import com.hitsz.eatut.database.VoteInfo;

import android.content.SharedPreferences;
import android.util.Log;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class ViewVoteActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<vote> voteList =new ArrayList<>();
    private long currenttime = System.currentTimeMillis();
    List<VoteInfo> voteInfos = LitePal.findAll(VoteInfo.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewvote);
        Intent intent = getIntent();
        findView();
        initRecycle();
    }

    protected void findView(){
        recyclerView = (RecyclerView)findViewById(R.id.vote_recycle);
    }

    //初始化投票信息
    private  void initVotes(String userID){
        List<VoteInfo> voteInfos = LitePal.findAll(VoteInfo.class);
        for (VoteInfo voteInfo:voteInfos){
            if((currenttime - voteInfo.getVoteDdl()) < 0){
                String name = voteInfo.getVoteName();
                int agreeNum = voteInfo.getVoteAgree();
                int disagreeNum = voteInfo.getVoteDisagree();
                long voteDdl = voteInfo.getVoteDdl();
                boolean isVoted = voteInfo.isVoted(userID);
//                String[] votedID =voteInfo.getVotedId();
                vote addVote = new vote(name, agreeNum, disagreeNum, voteDdl, isVoted);
                voteList.add(addVote);
            }
        }
    }

    private void initRecycle(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        SharedPreferences pref2 = this.getSharedPreferences("currentID",MODE_PRIVATE);
        int id = pref2.getInt("userID", -1);
        String userID = Integer.toString(id);
//        Log.d("用户id", userID);
        voteList.clear();
        initVotes(userID);
        ViewVoteAdapter adapter = new ViewVoteAdapter(voteList, userID, new ViewVoteAdapter.vvaListener() {
            @Override
            public void vva(int position) {
                initRecycle();
            }
        });
        recyclerView.setAdapter(adapter);
    }
}
