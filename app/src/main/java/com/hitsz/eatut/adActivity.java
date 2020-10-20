package com.hitsz.eatut;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.hitsz.eatut.rankingActivities.RankActivity;

public class adActivity extends AppCompatActivity implements View.OnClickListener{
    private Button rankingEntryButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad);
        rankingEntryButton = findViewById(R.id.ranking_entry_btn);
        rankingEntryButton.setOnClickListener(this);
    }
    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.ranking_entry_btn:
                Log.d("RankingEntryBtn", "Click");
                Intent intent = new Intent(this, RankActivity.class);
                startActivity(intent);
                break;
        }
    }
}
