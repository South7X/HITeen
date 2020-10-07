package com.hitsz.eatut.database;

import org.litepal.crud.LitePalSupport;
/**
 * @author Lily
 */
public class FeedInfo extends LitePalSupport {
    private int id;
    private String userName;
    private String feedInfo;

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id=id;
    }

    public String getUserName(){
        return userName;
    }

    public void setUserName(String userName){
        this.userName=userName;
    }

    public String getFeedInfo(){
        return feedInfo;
    }

    public void setFeedInfo(String feedInfo){
        this.feedInfo=feedInfo;
    }
}
