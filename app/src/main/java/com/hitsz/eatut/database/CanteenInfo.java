package com.hitsz.eatut.database;

import org.litepal.crud.LitePalSupport;

public class CanteenInfo extends LitePalSupport {
    private int id;

    private String canteenName;     //食堂名字

    private String canteenAddress;  //食堂地址

    private int canteenScore;       //食堂星级评分

    private int canteenWindowNumber;//食堂档口数量


    /**
     * setting method
     *
     */
    public void setId(int id) {
        this.id = id;
    }

    public void setCanteenWindowNumber(int canteenWindowNumber) {
        this.canteenWindowNumber = canteenWindowNumber;
    }

    public void setCanteenName(String canteenName) {
        this.canteenName = canteenName;
    }

    public void setCanteenAddress(String canteenAddress) {
        this.canteenAddress = canteenAddress;
    }

    public void setCanteenScore(int canteenScore) {
        this.canteenScore = canteenScore;
    }

    /**
     * getting method
     *
     */
    public int getId() {
        return id;
    }


    public String getCanteenAddress() {
        return canteenAddress;
    }

    public int getCanteenScore() {
        return canteenScore;
    }

    public String getCanteenName() {
        return canteenName;
    }

    public int getCanteenWindowNumber() {
        return canteenWindowNumber;
    }
}
