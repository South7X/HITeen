package com.hitsz.eatut.managerActivities;

import android.os.Bundle;
import androidx.annotation.NonNull;
import android.content.Intent;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hitsz.eatut.R;
/**
 * @author Lily
 */
//菜单栏：反馈
public class ManagerActivity extends AppCompatActivity {
    private Button button_feedinfo;
    private Button button_order;
    private Button button_post;
    private  Button button_edit;

   protected void findView(){
       button_feedinfo=findViewById(R.id.button_feedback);
       button_order=(Button)findViewById(R.id.button_order);
       button_post=(Button)findViewById(R.id.button_advertiseview);
       button_edit=(Button)findViewById(R.id.button_edit);
   }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);
        //查看反馈信息
        findView();
        button_feedinfo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(ManagerActivity.this,FeedInfoActivity.class);
                startActivity(intent);
            }
        });
        //查看订单信息
        button_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ManagerActivity.this,OrderActivity.class);
                startActivity(intent);
            }
        });
        //查看推广信息
        button_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ManagerActivity.this,AdvertisementActivity.class);
                startActivity(intent);
            }
        });
        //修改食堂信息
        button_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ManagerActivity.this,EditActivity.class);
                startActivity(intent);
            }
        });
    }

}