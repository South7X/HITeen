package com.example.manger.db;

import android.graphics.Bitmap;

import org.litepal.crud.LitePalSupport;

import java.io.ByteArrayOutputStream;
/**
 * @author Lily
 */
public class PostImg extends LitePalSupport {
    //将图片转换成字节
    private  byte[]img(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
    private byte[] headshot;//头像
    public PostImg(){
        super();
    }
    public PostImg(byte[]headshot){
        super();
        this.headshot=headshot;
    }
    public byte[] getHeadshot() {
        return headshot;
    }
    public void setHeadshot(byte[] headshot) {
        this.headshot = headshot;
    }
}
