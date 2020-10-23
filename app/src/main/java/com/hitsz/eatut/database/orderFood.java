package com.hitsz.eatut.database;
import org.litepal.crud.LitePalSupport;

/**
 * @author zhang
 */
public class orderFood extends LitePalSupport {
    //购物车内容，即CheckActivity的item
    private int id;
    private int dishID_II;
    private int userID;

    public int getId() {
        return id;
    }

    public int getDishID_II() {
        return dishID_II;
    }

    public int getUserID() {
        return userID;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setDishID_II(int dishID_II) {
        this.dishID_II = dishID_II;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

}