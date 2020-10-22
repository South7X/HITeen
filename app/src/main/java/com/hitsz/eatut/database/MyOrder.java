package com.hitsz.eatut.database;

import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;

public class MyOrder extends LitePalSupport {
    //我的订单内容，即OrderFragment的item
    private int id;
    private ArrayList<Integer> dishID_III;
    private int orderNo_II;
    private int userID;
    private long endTime;
    private boolean isPrepared;
    private boolean isPick;
    private float cost;


    public int getId() {
        return id;
    }

    public ArrayList<Integer> getDishID_III() {
        return dishID_III;
    }

    public int getOrderNo_II() {
        return orderNo_II;
    }

    public int getUserID() {
        return userID;
    }

    public boolean isPrepared() {
        return isPrepared;
    }

    public long getEndTime() {
        return endTime;
    }

    public boolean isPick() {
        return isPick;
    }

    public float getCost() {
        return cost;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDishID_III(ArrayList<Integer> dishID_III) {
        this.dishID_III = dishID_III;
    }

    public void setOrderNo_II(int orderNo_II) {
        this.orderNo_II = orderNo_II;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setPrepared(boolean prepared) {
        isPrepared = prepared;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public void setPick(boolean pick) {
        isPick = pick;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }
}
