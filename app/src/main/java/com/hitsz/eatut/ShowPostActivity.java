package com.hitsz.eatut;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.util.LinkifyCompat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;

import com.hitsz.eatut.database.PostImg;

import org.litepal.LitePal;

import java.util.List;

public class ShowPostActivity extends AppCompatActivity {
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_post);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        imageView=findViewById(R.id.post_image);
        List<PostImg> postImgList = LitePal.findAll(PostImg.class);
        if(!postImgList.isEmpty()) {
            for (PostImg postImg : postImgList) {
                byte[] imagess = postImg.getPostshot();
                Bitmap bms = BitmapFactory.decodeByteArray(imagess, 0, imagess.length);
                imageView.setImageBitmap(bms);
            }
        }else{
            Log.d("ImgTest", "数据库为空！");
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}