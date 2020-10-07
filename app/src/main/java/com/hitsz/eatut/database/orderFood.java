package com.hitsz.eatut.database;
import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

/**
 * @author zhang
 */
public class orderFood extends LitePalSupport {
    private int id;
    private String name;
    private float dishPrice;
    private float dishScore;
    private int dishID_II;
//    private int hour;
//    private int minute;
//    private int second;


    public int getDishID_II() {
        return dishID_II;
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

    public void setDishID_II(int dishID_II) {
        this.dishID_II = dishID_II;
    }
}