package com.hitsz.eatut;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


// TODO：查看投票button，查看宣传海报
// what‘s new 页面内部
public class adActivity extends AppCompatActivity{
    private Button rankingEntryButton;
    private Button showPostButton;

    protected void findView() {
        showPostButton = findViewById(R.id.showpost_btn);
        rankingEntryButton = findViewById(R.id.ranking_entry_btn);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad);

        findView();
        rankingEntryButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(adActivity.this, RankActivity.class);
                startActivity(intent);
            }
        });

        showPostButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(adActivity.this, ShowPostActivity.class);
                startActivity(intent);
            }
        });
    }

}
