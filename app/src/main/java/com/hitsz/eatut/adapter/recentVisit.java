package com.hitsz.eatut.adapter;

public class recentVisit {
    /*排行榜item*/
    private String canteen;
    private String dish;
    private int imageId;
    private byte[] dishShot;


    public recentVisit(String canteen, String dish, int imageId, byte[] dishShot) {
        this.canteen = canteen;
        this.dish = dish;
        this.imageId = imageId;
        this.dishShot = dishShot;
    }


    public String getCanteen() {
        return canteen;
    }

    public String getDish() {
        return dish;
    }

    public int getImageId() {
        return imageId;
    }

    public byte[] getDishShot() {
        return dishShot;
    }

    public void setCanteen(String canteen) {
        this.canteen = canteen;
    }

    public void setDish(String dish) {
        this.dish = dish;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public void setDishShot(byte[] dishShot) {
        this.dishShot = dishShot;
    }
}