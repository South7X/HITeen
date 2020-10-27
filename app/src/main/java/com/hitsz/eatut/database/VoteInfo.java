package com.hitsz.eatut.database;

import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

public class VoteInfo extends LitePalSupport {
    private long voteDdl;       //截止日期
    private String voteName;    //投票名

    private int voteAgree;      //菜品赞成人数
    private int voteDisagree;   //菜品反对人数

    //    private S votedId = new String[]{"null"};   //已投票用户id
    private List<String> votedId = new ArrayList<String>();
    public void initVotedId() {
        votedId.add("null");
    }

    public void AddId(String id){
        votedId.add(id);
    }

    public void setVoteName(String voteName) {this.voteName = voteName;}

    public void setVoteAgree(int agreeNum) {
        this.voteAgree = agreeNum;
    }

    public void setVoteDisagree(int disagreeNum) {
        this.voteDisagree = disagreeNum;
    }

    public void setVoteDdl(long voteDdl) {
        this.voteDdl = voteDdl;
    }

    public void InitNum(){
        this.voteAgree = 0;
        this.voteDisagree = 0;
    }

    public String getVoteName() {return voteName;}

    public int getVoteAgree() {
        return voteAgree;
    }

    public int getVoteDisagree() {
        return voteDisagree;
    }

    public long getVoteDdl() {
        return voteDdl;
    }

    //    public String[] getVotedId() {
//        return votedId;
//    }
    public boolean isVoted(String id){
        if (votedId.contains(id)){
            return true;
        }
        else return false;
    }
}


