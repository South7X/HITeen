package com.hitsz.eatut.database;

import org.litepal.crud.LitePalSupport;

public class WindowInfo extends LitePalSupport {
    private int id;

    private String belongToCanteenName; //所属食堂名称

    private String windowName;          //档口名称

    private String windowAddress;       //档口位置

    private int windowScore;            //档口星级评分

    private int windowDishNumber;       //档口下菜品数量

    /**
     * setting method
     *
     */
    public void setId(int id) {
        this.id = id;
    }

    public void setBelongToCanteenName(String belongToCanteenName) {
        this.belongToCanteenName = belongToCanteenName;
    }

    public void setWindowAddress(String windowAddress) {
        this.windowAddress = windowAddress;
    }

    public void setWindowDishNumber(int windowDishNumber) {
        this.windowDishNumber = windowDishNumber;
    }

    public void setWindowName(String windowName) {
        this.windowName = windowName;
    }

    public void setWindowScore(int windowScore) {
        this.windowScore = windowScore;
    }

    /**
     * getting method
     *
     */
    public int getId() {
        return id;
    }

    public int getWindowDishNumber() {
        return windowDishNumber;
    }

    public String getBelongToCanteenName() {
        return belongToCanteenName;
    }

    public int getWindowScore() {
        return windowScore;
    }

    public String getWindowAddress() {
        return windowAddress;
    }

    public String getWindowName() {
        return windowName;
    }

}
