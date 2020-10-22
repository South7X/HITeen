package com.hitsz.eatut.adapter;

public class order {
    /*
    * 购物车item
    * */
    private String name;
    private float dishPrice;
    private float dishScore;
    private int id;

    public order (String name, float dishPrice, float dishScore, int id){
        this.dishPrice = dishPrice;
        this.dishScore = dishScore;
        this.id = id;
        this.name = name;
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
}
