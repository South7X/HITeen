package com.hitsz.eatut.managerActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.hitsz.eatut.R;

/**
 * @author Lily
 */
public class AdvertisementActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertisement);

        Button button_viewvote=(Button)findViewById(R.id.button_viewvote);
        button_viewvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdvertisementActivity.this, VotingResultActivity.class);
                startActivity(intent);
            }
        });

        Button button_initvote=(Button)findViewById(R.id.button_initvote);
        button_initvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdvertisementActivity.this, AddVoteActivity.class);
                startActivity(intent);
            }
        });


        Button button_postimg=(Button)findViewById(R.id.button_postimg);
        button_postimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdvertisementActivity.this,PostActivity.class);
                startActivity(intent);
            }
        });

    }
}
