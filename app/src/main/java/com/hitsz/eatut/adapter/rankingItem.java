package com.hitsz.eatut.adapter;

public class rankingItem {
/*排行榜item*/
    private int rankingNum;
    private String canteen;
    private String dish;
    private int orderTimes;
    private int imageId;
    private float dishPrice;
    private float dishScore;
    private byte[] dishShot;

    public rankingItem(int rankingNum, String canteen, String dish, int orderTimes, int imageId, float dishScore, float dishPrice, byte[] dishShot){
        this.rankingNum = rankingNum;
        this.canteen = canteen;
        this.dish = dish;
        this.orderTimes = orderTimes;
        this.imageId = imageId;
        this.dishPrice = dishPrice;
        this.dishScore = dishScore;
        this.dishShot = dishShot;
    }

    public int getRankingNum() {
        return rankingNum;
    }

    public String getCanteen() {
        return canteen;
    }

    public String getDish() {
        return dish;
    }

    public int getOrderTimes() {
        return orderTimes;
    }

    public int getImageId() {
        return imageId;
    }

    public float getDishPrice() {
        return dishPrice;
    }

    public float getDishScore() {
        return dishScore;
    }

    public byte[] getDishShot() {
        return dishShot;
    }

    public void setRankingNum(int rankingNum) {
        this.rankingNum = rankingNum;
    }

    public void setCanteen(String canteen) {
        this.canteen = canteen;
    }

    public void setDish(String dish) {
        this.dish = dish;
    }

    public void setOrderTimes(int orderTimes) {
        this.orderTimes = orderTimes;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public void setDishScore(float dishScore) {
        this.dishScore = dishScore;
    }

    public void setDishPrice(float dishPrice) {
        this.dishPrice = dishPrice;
    }

    public void setDishShot(byte[] dishShot) {
        this.dishShot = dishShot;
    }
}
