package com.hitsz.eatut.adapter;

import java.util.ArrayList;

public class MyOrderItem {
    /*
    * 我的订单item
    * */
    private ArrayList<Integer> dishID_IV;
    private boolean isPrepared;
    private int orderNo_III;
    private int myOrderID;
    private String endTime;
    private boolean isPick;


    public MyOrderItem( ArrayList<Integer> dishID_IV, boolean isPrepared,int orderNo_III,int myOrderID,String endTime, boolean isPick){
        this.dishID_IV=dishID_IV;
        this.isPrepared=isPrepared;
        this.orderNo_III=orderNo_III;
        this.myOrderID=myOrderID;
        this.endTime=endTime;
        this.isPick=isPick;

    }



    public int getMyOrderID() {
        return myOrderID;
    }


    public boolean getIsPrepared() {
        return isPrepared;
    }

    public int getOrderNo_III() {
        return orderNo_III;
    }

    public String getEndTime() {
        return endTime;
    }

    public boolean isPick() {
        return isPick;
    }


    public void setIsPrepared(boolean isPrepared) {
        this.isPrepared = isPrepared;
    }

    public void setMyOrderID(int myOrderID) {
        this.myOrderID = myOrderID;
    }

    public  ArrayList<Integer> getDishID_IV() {
        return dishID_IV;
    }

    public void setDishID_IV( ArrayList<Integer> dishID_IV) {
        this.dishID_IV = dishID_IV;
    }

    public void setOrderNo_III(int orderNo_III) {
        this.orderNo_III = orderNo_III;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setPick(boolean pick) {
        isPick = pick;
    }
}
