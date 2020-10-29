package com.hitsz.eatut.database;

import android.graphics.Bitmap;

import org.litepal.crud.LitePalSupport;

import java.io.ByteArrayOutputStream;
/**
 * @author Lily
 */
public class PostImg extends LitePalSupport {

    private byte[] postshot;//海报

    public PostImg(){
        super();
    }
//    public PostImg(byte[]postshot){
//        super();
//        this.postshot=postshot;
//    }

    // 图片存储
    public byte[] getPostshot() {
        return postshot;
    }
    public void setPostshot(byte[] postshot) {
        this.postshot = postshot;
    }
}
