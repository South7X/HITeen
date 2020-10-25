package com.hitsz.eatut.database;

import org.litepal.crud.LitePalSupport;
/**
 * @author Lily
 */
public class ManagerOrderInfo extends LitePalSupport {
    private int id;
    private String name;
    private  String phone;
    private float dishPrice;
    private float dishScore;
    private int orderNo;

    public String getPhone() {
        return phone;
    }

    public int getOrderNo() {
        return orderNo;
    }

    public float getDishScore() {
        return dishScore;
    }

    public float getDishPrice() {
        return dishPrice;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setDishPrice(float dishPrice) {
        this.dishPrice = dishPrice;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDishScore(float dishScore) {
        this.dishScore = dishScore;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
