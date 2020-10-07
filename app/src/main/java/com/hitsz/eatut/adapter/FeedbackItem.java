package com.hitsz.eatut.adapter;
/**
 * @author Lily
 */
public class FeedbackItem {
    private int userID;
    private String feedInfo;

    public FeedbackItem(int userID, String feedInfo){
        this.userID=userID;
        this.feedInfo=feedInfo;
    }

    public void setFeedInfo(String feedInfo){
        this.feedInfo=feedInfo;
    }
    public void setUserID(int userID){
        this.userID=userID;
    }

    public int getUserID(){
        return userID;
    }

    public String getFeedInfo(){
        return feedInfo;
    }
}
