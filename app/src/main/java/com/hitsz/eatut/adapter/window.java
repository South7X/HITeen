package com.hitsz.eatut.adapter;

/**
 * @author lixiang
 */
public class window {
    private String name;
    private float score;
    private int imageID;

    public window(String name, float score, int imageID){
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
