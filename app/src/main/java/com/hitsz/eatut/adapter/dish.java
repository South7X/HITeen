package com.hitsz.eatut.adapter;

/**
 * @author zhang
 */
public class dish {
    private String name;
    private float dishPrice;
    private float dishScore;
    private int imageId;
    private int dishID_I;
    private byte[] dishShot;
    public dish(String name, int imageId,float dishPrice,float dishScore,int dishID_I,byte[] dishShot){
        this.name=name;
        this.imageId=imageId;
        this.dishPrice=dishPrice;
        this.dishScore=dishScore;
        this.dishID_I=dishID_I;
        this.dishShot=dishShot;
    }
    public String getName(){
        return name;
    }
    public int getImageId(){
        return imageId;
    }

    public int getDishID_I() {
        return dishID_I;
    }
    public float getDishPrice() {
        return dishPrice;
    }

    public float getDishScore() {
        return dishScore;
    }

    public byte[] getDishSot(){return dishShot;}
}
