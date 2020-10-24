package com.hitsz.eatut.database;

import org.litepal.crud.LitePalSupport;

public class Comments extends LitePalSupport {
    private int id;

    private String commentText;

    private String userName;

    private String commentKeyWords;

    private String commentCanteen;

    private Boolean anonymousFlag;

    public Boolean getAnonymousFlag() {
        return anonymousFlag;
    }

    public String getCommentCanteen() {
        return commentCanteen;
    }

    public int getId() {
        return id;
    }

    public String getCommentKeyWords() {
        return commentKeyWords;
    }

    public String getCommentText() {
        return commentText;
    }

    public String getUserName() {
        return userName;
    }

    public void setAnonymousFlag(Boolean anonymousFlag) {
        this.anonymousFlag = anonymousFlag;
    }

    public void setCommentCanteen(String commentCanteen) {
        this.commentCanteen = commentCanteen;
    }

    public void setCommentKeyWords(String commentKeyWords) {
        this.commentKeyWords = commentKeyWords;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
