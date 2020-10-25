package com.hitsz.eatut.adapter;

public class vote {
    private String name;
    private int agreeNum;
    private int disagreeNum;

    public vote(String name, int agreeNum, int disagreeNum){
        this.name=name;
        this.agreeNum = agreeNum;
        this.disagreeNum = disagreeNum;
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
}
