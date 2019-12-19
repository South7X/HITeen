package com.hitsz.eatut.database;

import org.litepal.crud.LitePalSupport;

public class DishInfo extends LitePalSupport {
    private int id;

    private String dishName;        //菜品名字

    private String belongToWindow;  //所属档口名称

    private String belongToCanteen; //所属食堂名称

    private float dishPrice;        //菜品价格

    private int dishScore;          //菜品星级评分

    private String dishTags;
                                    //菜品标签
    /**
     * setting method
     *
     */

    public void setId(int id) {
        this.id = id;
    }

    public void setBelongToWindow(String belongToWindow) {
        this.belongToWindow = belongToWindow;
    }

    public void setBelongToCanteen(String belongToCanteen) {
        this.belongToCanteen = belongToCanteen;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public void setDishPrice(float dishPrice) {
        this.dishPrice = dishPrice;
    }

    public void setDishScore(int dishScore) {
        this.dishScore = dishScore;
    }

    public void setDishTags(String dishTags) {
        this.dishTags = dishTags;
    }
    /**
     * getting method
     *
     */

    public int getId() {
        return id;
    }

    public float getDishPrice() {
        return dishPrice;
    }

    public int getDishScore() {
        return dishScore;
    }

    public String getBelongToWindow() {
        return belongToWindow;
    }

    public String getBelongToCanteen() {
        return belongToCanteen;
    }

    public String getDishName() {
        return dishName;
    }

    public String getDishTags() {
        return dishTags;
    }
}
