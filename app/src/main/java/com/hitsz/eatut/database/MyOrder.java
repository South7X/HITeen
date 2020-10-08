package com.hitsz.eatut.database;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

public class MyOrder extends LitePalSupport {
    private int id;
    private int dishID_III;
    private String phone;
    private int orderNo_II;
    private boolean isPrepared;

    public boolean isPrepared() {
        return isPrepared;
    }

    public void setPrepared(boolean prepared) {
        isPrepared = prepared;
    }

    public int getId() {
        return id;
    }

    public int getOrderNo_II() {
        return orderNo_II;
    }
    public String getPhone(){
        return phone;
    }

    public int getDishID_III() {
        return dishID_III;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setDishID_III(int dishID_III) {
        this.dishID_III = dishID_III;
    }

    public void setOrderNo_II(int orderNo_II) {
        this.orderNo_II = orderNo_II;
    }

    public void setId(int id) {
        this.id = id;
    }
}
