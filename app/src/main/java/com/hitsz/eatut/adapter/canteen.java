package com.hitsz.eatut.adapter;

public class canteen {
    private String name;
    private float score;
    private int imageID;
    private byte[] canteenshot;     //食堂照片

    public canteen(String name, float score, int imageID,byte[] canteenshot){
        this.imageID = imageID;
        this.name = name;
        this.score = score;
        this.canteenshot = canteenshot;
    }

    public String getName() {
        return name;
    }

    public int getImageID() {
        return imageID;
    }

    public float getScore() {
        return score;
    }

    public  byte[] getCanteenshot(){return canteenshot;}
}
