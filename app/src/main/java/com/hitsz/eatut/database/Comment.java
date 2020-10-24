package com.hitsz.eatut.database;

import org.litepal.crud.LitePalSupport;

public class Comment extends LitePalSupport {
    private int id;
    private long postTime;
    private String commentText;
    private String userName;
    private String commentKeyWords;
    private String commentCanteen;
    private Boolean anonymousFlag;

    public int getId() {
        return id;
    }

    public long getPostTime() {
        return postTime;
    }

    public String getCommentText() {
        return commentText;
    }

    public String getUserName() {
        return userName;
    }

    public String getCommentKeyWords() {
        return commentKeyWords;
    }

    public String getCommentCanteen() {
        return commentCanteen;
    }

    public Boolean getAnonymousFlag() {
        return anonymousFlag;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPostTime(long postTime) {
        this.postTime = postTime;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public void setCommentCanteen(String commentCanteen) {
        this.commentCanteen = commentCanteen;
    }

    public void setCommentKeyWords(String commentKeyWords) {
        this.commentKeyWords = commentKeyWords;
    }

    public void setAnonymousFlag(Boolean anonymousFlag) {
        this.anonymousFlag = anonymousFlag;
    }
}
