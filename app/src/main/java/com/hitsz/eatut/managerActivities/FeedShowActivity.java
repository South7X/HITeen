package com.hitsz.eatut.managerActivities;

import androidx.appcompat.app.AppCompatActivity;
import com.hitsz.eatut.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
/**
 * @author Lily
 */
//提交反馈成功界面
public class FeedShowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_show);

        TextView tv = (TextView) findViewById(R.id.text_feed_info);
        Intent intent = getIntent();
        String data = intent.getStringExtra("et1");
        tv.setText(data);

        Button button_back = (Button) findViewById(R.id.button_back);
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
