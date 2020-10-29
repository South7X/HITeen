package com.hitsz.eatut.adapter;

/**
 * @author lixiang
 */
public class window {
    private String name;
    private float score;
    private int imageID;
    private byte[] winShot;

    public window(String name, float score, int imageID,byte[] winShot){
        this.imageID = imageID;
        this.name = name;
        this.score = score;
        this.winShot= winShot;
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

    public byte[] getWinShot() {
        return winShot;
    }
}
