package com.hitsz.eatut.adapter;

public class MyOrderItem {
    private int dishID_IV;
    private String name;
    private boolean isPrepared;
    private int orderNo_III;
    private int myOrderID;


    public MyOrderItem(int dishID_IV,String name,boolean isPrepared,int orderNo_III,int myOrderID){
        this.dishID_IV=dishID_IV;
        this.name=name;
        this.isPrepared=isPrepared;
        this.orderNo_III=orderNo_III;
        this.myOrderID=myOrderID;

    }

    public void setIsPrepared(boolean isPrepared) {
        this.isPrepared = isPrepared;
    }

    public void setMyOrderID(int myOrderID) {
        this.myOrderID = myOrderID;
    }

    public int getMyOrderID() {
        return myOrderID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean getIsPrepared() {
        return isPrepared;
    }


    public int getDishID_IV() {
        return dishID_IV;
    }

    public void setDishID_IV(int dishID_IV) {
        this.dishID_IV = dishID_IV;
    }

    public int getOrderNo_III() {
        return orderNo_III;
    }

    public void setOrderNo_III(int orderNo_III) {
        this.orderNo_III = orderNo_III;
    }
}
