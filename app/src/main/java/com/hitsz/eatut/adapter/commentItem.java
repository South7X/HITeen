package com.hitsz.eatut.adapter;

public class commentItem {
    private long postTime;
    private String commentText;
    private String userName;
    public commentItem(long postTime, String commentText, String userName){
        this.postTime = postTime;
        this.commentText = commentText;
        this.userName = userName;
    }

    public long getPostTime() {
        return postTime;
    }

    public String getUserName() {
        return userName;
    }

    public String getCommentText() {
        return commentText;
    }
}
