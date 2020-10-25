package com.hitsz.eatut.database;

import org.litepal.crud.LitePalSupport;

public class VoteInfo extends LitePalSupport {
    private int id;
    private String voteName;    //投票名

    private int voteAgree;      //菜品赞成人数
    private int voteDisagree;   //菜品反对人数

    public void setVoteName(String voteName) {this.voteName = voteName;}

    public void setVoteAgree(int agreeNum) {
        this.voteAgree = agreeNum;
    }

    public void setVoteDisagree(int disagreeNum) {
        this.voteDisagree = disagreeNum;
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
}


