package com.hitsz.eatut.adapter;

public class rankingItem {
/*排行榜item*/
    private int rankingNum;
    private String canteen;
    private String dish;
    private int orderTimes;
    public rankingItem(int rankingNum, String canteen, String dish, int orderTimes){
        this.rankingNum = rankingNum;
        this.canteen = canteen;
        this.dish = dish;
        this.orderTimes = orderTimes;
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
}
