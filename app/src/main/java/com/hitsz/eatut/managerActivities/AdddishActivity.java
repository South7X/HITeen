package com.hitsz.eatut.managerActivities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.hitsz.eatut.R;

import android.Manifest;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

import static com.hitsz.eatut.BaseClass.addNewDish;
import static com.hitsz.eatut.managerActivities.PostActivity.CHOOSE_PHOTO;

/**
 * @author Lily
 */
public class AdddishActivity extends AppCompatActivity implements View.OnClickListener {
    EditText text_dishname = null;
    EditText text_dishprice = null;
    EditText text_belongToCanteenName = null;
    EditText text_belogToWindowName = null;
    EditText text_tags = null;

    Button btn_postimg;
    private byte[]images=new byte[1024];
    private Bitmap bitmap;
    private ImageView image_dish;

    //将图片转换成字节
    private  byte[]img(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.button_submit:
                String dn = text_dishname.getText().toString();
                String btc = text_belongToCanteenName.getText().toString();
                String btw = text_belogToWindowName.getText().toString();
                String dp_string = text_dishprice.getText().toString();
                String tags = text_tags.getText().toString();
                float dp = Float.valueOf(dp_string);
                addNewDish(dn, btc, btw, dp, tags,images);
                Toast.makeText(this, "增加菜品成功", Toast.LENGTH_SHORT).show();
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adddish);
        Button button_edit=(Button)findViewById(R.id.button_submit);
        button_edit.setOnClickListener(this);

        Button btn_postimg=(Button)findViewById(R.id.button_postimg);
        btn_postimg.setOnClickListener(this);
        //TODO:未能展示上传的图片
        image_dish = findViewById(R.id.img_dish);

        text_dishname = (EditText) findViewById(R.id.text_dishname);
        text_belongToCanteenName =(EditText) findViewById(R.id.text_belongtocateen);
        text_belogToWindowName =(EditText) findViewById(R.id.text_belongtowindow);
        text_dishprice = (EditText) findViewById(R.id.text_dishprice);
        text_tags =(EditText) findViewById(R.id.text_tags);

        // 相册点击事件
        btn_postimg.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(AdddishActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(AdddishActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            } else {
                openAlbum();
            }
        });

    }
    // 打开相册
    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
    }
    // 路径
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            //如果document类型的Uri,则通过document来处理
            String docID = DocumentsContract.getDocumentId(uri);
            assert uri != null;
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docID.split(":")[1];     //解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;

                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/piblic_downloads"), Long.valueOf(docID));

                imagePath = getImagePath(contentUri, null);

            }

        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //如果是content类型的uri，则使用普通方式使用
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            //如果是file类型的uri，直接获取路径即可
            imagePath = uri.getPath();

        }
        displayImage(imagePath);
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            bitmap = BitmapFactory.decodeFile(imagePath);;
            Toast.makeText(this, "上传成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }
    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //通过Uri和selection来获取真实的图片路径
        Cursor cursor;
        cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this, "you denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    // 处理从相机或相册返回的数据，存入数据库
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= 19) {  //4.4及以上的系统使用这个方法处理图片；
                        handleImageOnKitKat(data);
                    } else {
                        handleImageBeforeKitKat(data);  //4.4及以下的系统使用这个方法处理图片
                    }
                }
            default:
                break;
        }

        // TODO:将图片转化为二进制数，存入数据库
        images = img(bitmap);
    }
}
