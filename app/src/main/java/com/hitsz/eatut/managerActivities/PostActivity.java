package com.hitsz.eatut.managerActivities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ContentResolver;
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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hitsz.eatut.R;
import com.hitsz.eatut.database.PostImg;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

import static android.os.Build.*;

/**
 * @author Lily
 */


public class PostActivity extends AppCompatActivity {

    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;
    // 拍照，相册 按钮
    private Button xiangce;
    private TextView post_text;
    private ImageView img_view;
    private Uri imageUri;
    private byte[]images=new byte[1024];
    // 从相册获得图片
    private Bitmap bitmap;

    //将图片转换成字节
    private  byte[]img(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        //paizhao=(Button)findViewById(R.id.paizhao);
        xiangce=(Button)findViewById(R.id.xiangce);
        // 展示拟上传的图片
        img_view=(ImageView)findViewById(R.id.img_view);
        // 显示文字
        post_text=(TextView)findViewById(R.id.text_post);
        post_text.setText("  上传并展示一张图片 "+"\n"+"  新图片将覆盖旧图片 ");
        //img = (ImageView)this.findViewById(R.id.img);

        // 相册点击事件
        xiangce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(PostActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(PostActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    openAlbum();
                }
            }
        });

        // 拍照点击事件
//        paizhao.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //创建file对象，用于存储拍照后的图片；
//                File outputImage = new File(getExternalCacheDir(), "output_image.png");
//
//                try {
//                    if (outputImage.exists()) {
//                        outputImage.delete();
//                    }
//                    outputImage.createNewFile();
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                // TODO: 查看authority用处，后面接什么？
//                if (VERSION.SDK_INT >= 24) {
//                    imageUri = FileProvider.getUriForFile(PostActivity.this,
//                            "com.example.userlogin.fileprovider", outputImage);
//                } else {
//                    imageUri = Uri.fromFile(outputImage);
//                }
//
//                //启动相机程序
//                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//                startActivityForResult(intent, TAKE_PHOTO);
//            }
//        });
    }


    @RequiresApi(api = VERSION_CODES.KITKAT)
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

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
    }

    // 处理从相机或相册返回的数据，存入数据库
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    if (VERSION.SDK_INT >= 19) {  //4.4及以上的系统使用这个方法处理图片；
                        handleImageOnKitKat(data);
                    } else {
                        handleImageBeforeKitKat(data);  //4.4及以下的系统使用这个方法处理图片
                    }
                }
            default:
                break;
        }

        // 将图片转化为二进制数，存入数据库
        images = img(bitmap);
        PostImg shuju = new PostImg();
        shuju.setPostshot(images);
        shuju.save();

    }


//    private ImageView img;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_post);
//        img = (ImageView)this.findViewById(R.id.img);
//    }
//
//    //图片点击事件
//    public void getPicture(View v) {
//        Intent intent = new Intent(Intent.ACTION_PICK,null);
//        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
//        //intent 待启动的Intent 100（requestCode）请求码，返回时用来区分是那次请求
//        startActivityForResult(intent ,100);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        //返回成功，请求码（对应启动时的requestCode）
//        if(resultCode == RESULT_OK && requestCode==100) {
//            Uri uri = data.getData();
//            ContentResolver cr = this.getContentResolver();
//            //异常捕获
//            try {
//                //根据Uri获取流文件
//                InputStream is = cr.openInputStream(uri);
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inSampleSize =3;
//                Bitmap bitmap = BitmapFactory.decodeStream(is,null,options);
//                img.setImageBitmap(bitmap);
//            }
//            catch(Exception e) {
//                Log.i("lyf", e.toString());
//            }
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }
}