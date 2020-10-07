package com.hitsz.eatut.managerActivities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import com.hitsz.eatut.R;
import java.io.InputStream;
/**
 * @author Lily
 */
public class PostActivity extends AppCompatActivity {

    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        img = (ImageView)this.findViewById(R.id.img);
    }

    //图片点击事件
    public void getPicture(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK,null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
        //intent 待启动的Intent 100（requestCode）请求码，返回时用来区分是那次请求
        startActivityForResult(intent ,100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //返回成功，请求码（对应启动时的requestCode）
        if(resultCode == RESULT_OK && requestCode==100) {
            Uri uri = data.getData();
            ContentResolver cr = this.getContentResolver();
            //异常捕获
            try {
                //根据Uri获取流文件
                InputStream is = cr.openInputStream(uri);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize =3;
                Bitmap bitmap = BitmapFactory.decodeStream(is,null,options);
                img.setImageBitmap(bitmap);
            }
            catch(Exception e) {
                Log.i("lyf", e.toString());
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}