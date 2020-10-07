package com.hitsz.eatut.adapter;

public class canteen {
    private String name;
    private float score;
    private int imageID;

    public canteen(String name, float score, int imageID){
        this.imageID = imageID;
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public int getImageID() {
        return imageID;
    }

    public float getScore() {
        return score;
    }
}
