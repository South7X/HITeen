package com.hitsz.eatut.adapter;

import java.util.ArrayList;

/**
 * @author Lily
 */
public class OrderItem {
    private ArrayList<Integer> dishID_IV;
    private boolean isPrepared;
    private int orderNo_III;
    private int myOrderID;
    private String endTime;
    private float cost;


    public OrderItem( ArrayList<Integer> dishID_IV, boolean isPrepared,int orderNo_III,int myOrderID,String endTime,  float cost){
        this.dishID_IV=dishID_IV;
        this.isPrepared=isPrepared;
        this.orderNo_III=orderNo_III;
        this.myOrderID=myOrderID;
        this.endTime=endTime;
        this.cost=cost;
    }

    public ArrayList<Integer> getDishID_IV() {
        return dishID_IV;
    }

    public float getCost() {
        return cost;
    }

    public int getMyOrderID() {
        return myOrderID;
    }

    public int getOrderNo_III() {
        return orderNo_III;
    }

    public String getEndTime() {
        return endTime;
    }

    public boolean isPrepared() {
        return isPrepared;
    }
}
