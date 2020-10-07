package com.hitsz.eatut.managerActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ViewUtils;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hitsz.eatut.managerActivities.FeedShowActivity;
import com.hitsz.eatut.R;
import com.hitsz.eatut.database.FeedInfo;
import com.facebook.stetho.Stetho;

import org.litepal.LitePal;
/**
 * @author Lily
 */
//输入反馈界面
public class FeedbackActivity extends AppCompatActivity {
    EditText text_feedinfo = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        Stetho.initializeWithDefaults(this);
        SQLiteDatabase db = LitePal.getDatabase();
        Button button_feedinfo = (Button) findViewById(R.id.button_submit);
        button_feedinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_feedinfo = (EditText) findViewById(R.id.text_feedback);
                String context = text_feedinfo.getText().toString();//保存反馈输入
                FeedInfo feed1=new FeedInfo();
                feed1.setFeedInfo(context);
                feed1.save();
                //跳转至提交成功界面，传入反馈内容到show活动
                Intent intent=new Intent(FeedbackActivity.this, FeedShowActivity.class);
                intent.putExtra("et1",context);
                FeedbackActivity.this.startActivity(intent);
                Toast.makeText(FeedbackActivity.this,"感谢您的反馈，我们会尽快处理您的意见",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
