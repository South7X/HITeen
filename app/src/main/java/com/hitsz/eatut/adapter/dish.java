package com.hitsz.eatut.adapter;

import com.hitsz.eatut.database.DishInfo;

/**
 * @author zhang
 */
public class dish {
    private String name;
    private float dishPrice;
    private float dishScore;
    private int imageId;
    private int dishID_I;
    public dish(String name, int imageId,float dishPrice,float dishScore,int dishID_I){
        this.name=name;
        this.imageId=imageId;
        this.dishPrice=dishPrice;
        this.dishScore=dishScore;
        this.dishID_I=dishID_I;
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
}
