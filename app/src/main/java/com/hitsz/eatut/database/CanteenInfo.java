package com.hitsz.eatut.database;

import org.litepal.crud.LitePalSupport;

public class CanteenInfo extends LitePalSupport {
    private int id;

    private String canteenName;     //食堂名字

    private String canteenAddress;  //食堂地址

    private float canteenScore;       //食堂星级评分

    private int canteenWindowNumber;//食堂档口数量

    private int ImageID;            //食堂图片id


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

    public void setCanteenScore(float canteenScore) {
        this.canteenScore = canteenScore;
    }

    public void setImageID(int imageID) {
        ImageID = imageID;
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

    public float getCanteenScore() {
        return canteenScore;
    }

    public String getCanteenName() {
        return canteenName;
    }

    public int getCanteenWindowNumber() {
        return canteenWindowNumber;
    }

    public int getImageID() {
        return ImageID;
    }
}
