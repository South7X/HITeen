package com.hitsz.eatut.adapter;

public class vote {
    private String name;
    private int agreeNum;
    private int disagreeNum;
    private long voteDdl;
    //    private String[] votedID;
    private boolean isVoted;

    public vote(String name, int agreeNum, int disagreeNum, long voteDdl, boolean isVoted){
        this.name=name;
        this.agreeNum = agreeNum;
        this.disagreeNum = disagreeNum;
        this.voteDdl = voteDdl;
//        this.votedID =votedID;
        this.isVoted = isVoted;
    }

    public String getName() {
        return name;
    }

    public int getAgreeNum() {
        return agreeNum;
    }

    public int getDisagreeNum() {
        return disagreeNum;
    }

    public long getVoteDdl() {
        return voteDdl;
    }

    public boolean getIsVoted() {
        return isVoted;
    }
}
