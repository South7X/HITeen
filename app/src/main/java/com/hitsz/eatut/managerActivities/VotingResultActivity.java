package com.hitsz.eatut.managerActivities;

import android.os.Bundle;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hitsz.eatut.R;
import com.hitsz.eatut.adapter.VotingResultsAdapter;
import com.hitsz.eatut.adapter.vote;
import com.hitsz.eatut.database.VoteInfo;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class VotingResultActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<vote> voteList =new ArrayList<>();
    List<VoteInfo> voteInfos = LitePal.findAll(VoteInfo.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_votresults);
        Intent intent = getIntent();
        findView();
        initVotes();
        initRecycle();
    }

    protected void findView(){
        recyclerView = (RecyclerView)findViewById(R.id.mvote_recycle);
    }

    //初始化投票信息
    private  void initVotes(){
        List<VoteInfo> voteInfos = LitePal.findAll(VoteInfo.class);
        for (VoteInfo voteInfo:voteInfos){
            String name = voteInfo.getVoteName();
            int agreeNum = voteInfo.getVoteAgree();
            int disagreeNum = voteInfo.getVoteDisagree();
            vote addVote = new vote(name, agreeNum, disagreeNum);
            voteList.add(addVote);
        }
    }

    public void initRecycle(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        voteList.clear();
        initVotes();
        VotingResultsAdapter adapter = new VotingResultsAdapter(voteList);
        recyclerView.setAdapter(adapter);

    }
}
